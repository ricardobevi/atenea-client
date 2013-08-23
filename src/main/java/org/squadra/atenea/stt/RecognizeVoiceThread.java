package org.squadra.atenea.stt;

import java.io.IOException;
import java.util.Date;

import lombok.extern.log4j.Log4j;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.ateneacommunication.Message;
import org.squadra.atenea.exceptions.GoogleTTSException;
import org.squadra.atenea.gui.MainGUI;
import org.squadra.atenea.gui.MainGUIPrototype;
import org.squadra.atenea.history.HistoryItem;
import org.squadra.atenea.tts.MessageProcessor;

/**
 * Hilo de ejecucion que se encarga de la traduccion de voz a texto, se comunica con el servidor
 * para obtener una respuesta y la reproduce.
 * @author Facundo D'Aranno, Leandro Morrone
 */
@Log4j
public class RecognizeVoiceThread implements Runnable {

	/** Objeto que contiene las variables de configuracion y estado del sistema */
	private Atenea atenea = Atenea.getInstance();

	@Override
	public void run() {

		Recognizer recognizer = new Recognizer();
		String googleResponse = "";
		Boolean responseOk = true;
		Message outputMessage = new Message();
		
		// Envio a Google el audio y el idioma y guardo la respuesta devuelta
		try {
			googleResponse = recognizer
					.getRecognizedDataForWave(atenea.getWaveFilePath(), atenea.getLanguageCode())
					.getResponse();
			if (googleResponse == null) {
				throw new Exception();
			}
			
		} catch (GoogleTTSException e) {
			outputMessage = new Message("No logro conectarme a Internet.", Message.ERROR);
			e.printStackTrace();
			log.error("Error al conectarse a Internet");
			responseOk = false;
		} catch (IOException e) {
			outputMessage = new Message("Tuve un problema interno, podrías repetírmelo?.", Message.ERROR);
			e.printStackTrace();
			log.error("Error al grabar archivo de audio wav");
			responseOk = false;
		} catch (Exception e) {
			outputMessage = new Message("Disculpa, podrías hablar un poco más alto?", Message.ERROR);
			googleResponse = "...";
			log.debug("Google Voice devuelve null");
			responseOk = false;
		}
		
		//MainGUIPrototype.getInstance().setTxtEntradaAudio(googleResponse);
		MainGUI.getInstance().setTxtInput(googleResponse);
		
		// Agrego un item al historial
		Atenea.getInstance().getHistory().addItem(new HistoryItem(
						Atenea.getInstance().getUser(), 
						HistoryItem.INPUT_VOICE_MESSAGE,
						googleResponse, new Date()));
		
		// Si hay internet, envio el mensaje de entrada al servidor
		if (responseOk) {
			
			try {
				Message inputMessage = new Message(googleResponse);
				
				// ESTA LINEA ENVIA EL MENSAJE AL SERVIDOR Y RECIBE LA RESPUESTA
				outputMessage = atenea.getClient().dialog(inputMessage);
				log.debug(inputMessage.getText());
				log.debug(outputMessage.getText());
			} catch (Exception e) {
				outputMessage = new Message("No logro conectarme al servidor.", Message.ERROR);
				log.error("Error de conexion con el servidor");
			}
		} 
	
		MessageProcessor.processMessage(outputMessage);
		
	}
}