package org.squadra.atenea.stt;

import java.io.IOException;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;
import org.squadra.atenea.aiengine.Message;
import org.squadra.atenea.exceptions.GoogleTTSException;
import org.squadra.atenea.gui.MainGUI;
import org.squadra.atenea.tts.PlayTextMessage;

/**
 * Hilo de ejecucion que se encarga de la traduccion de voz a texto, se comunica con el servidor
 * para obtener una respuesta y la reproduce.
 * @author Facundo D'Aranno, Leandro Morrone
 */
public class RecognizeVoiceThread implements Runnable {

	/** Objeto que contiene las variables de configuracion y estado del sistema */
	private Atenea atenea;

	/**
	 * Constructor
	 * @param atenea
	 */
	public RecognizeVoiceThread(Atenea atenea) {
		this.atenea = atenea;
	}

	@Override
	public void run() {

		Recognizer recognizer = new Recognizer();
		String googleResponse = "";
		Boolean responseOk = true;
		
		// Envio a Google el audio y el idioma y guardo la respuesta devuelta
		try {
			googleResponse = recognizer
					.getRecognizedDataForWave(atenea.getWaveFilePath(), atenea.getLanguageCode())
					.getResponse();
			MainGUI.getInstance().setTxtEntradaAudio(new String(googleResponse.getBytes("ISO-8859-1"), "UTF-8"));
			
		} catch (GoogleTTSException e) {
			MainGUI.getInstance().setTxtSalida("No logro conectarme a Internet.");
			e.printStackTrace();
			responseOk = false;
		} catch (IOException e) {
			MainGUI.getInstance().setTxtSalida("Tuve un problema interno, ¿podrías repetirmelo?.");
			e.printStackTrace();
			responseOk = false;
		} catch (Exception e) {
			MainGUI.getInstance().setTxtSalida("Disculpa, ¿podrías hablar un poco más alto?");
			e.printStackTrace();
			responseOk = false;
		}	
		
		// Si hay internet, envio el mensaje de entrada al servidor
		if (responseOk) {
			try {
				Message inputMessage = new Message(
						new String(googleResponse.getBytes("ISO-8859-1"), "UTF-8"));
				
				// ESTA LINEA ENVIA EL MENSAJE AL SERVIDOR Y RECIBE LA RESPUESTA
				Message outputMessage = atenea.getClient().dialog(inputMessage);
				
				MainGUI.getInstance().setTxtSalida(outputMessage.getText());
				
			} catch (Exception e) {
				MainGUI.getInstance().setTxtSalida("No logro conectarme al servidor.");
			}
		} 
		
		try {
			atenea.setState(AteneaState.PLAYING);
			MainGUI.getInstance().setTxtEstadoDelSistema(atenea.getStateText());
			PlayTextMessage.play(MainGUI.getInstance().getTxtSalida());
		} catch (Exception e) {
			MainGUI.getInstance().setTxtSalida("No logro conectarme a Internet.");
			e.printStackTrace();
		}
		
		atenea.setState(AteneaState.WAITING);
		MainGUI.getInstance().setTxtEstadoDelSistema(atenea.getStateText());
		
	}
}