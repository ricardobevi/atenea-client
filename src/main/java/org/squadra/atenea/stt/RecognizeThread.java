package org.squadra.atenea.stt;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;
import org.squadra.atenea.gui.MainGUI;
import org.squadra.atenea.tts.PlayTextMessage;

/**
 * Hilo de ejecucion que se encarga de la traduccion de voz a texto, se comunica con el servidor
 * para obtener una respuesta y la reproduce.
 * @author Facundo D'Aranno, Leandro Morrone
 */
public class RecognizeThread implements Runnable {

	/** Objeto que contiene las variables de configuracion y estado del sistema */
	private Atenea atenea;

	/**
	 * Constructor
	 * @param atenea
	 */
	public RecognizeThread(Atenea atenea) {
		this.atenea = atenea;
	}

	@Override
	public void run() {

		Recognizer recognizer = new Recognizer();
		String googleResponse = "";
		Boolean hasInternet = true;
		
		// Envio a Google el audio y el idioma y guardo la respuesta devuelta
		try {
			googleResponse = recognizer
					.getRecognizedDataForWave(atenea.getWaveFilePath(), atenea.getLanguageCode())
					.getResponse();
			MainGUI.getInstance().setTxtEntradaAudio(googleResponse);

		} catch (Exception e) {
			MainGUI.getInstance().setTxtSalida("No logro conectarme a internet.");
			hasInternet = false;
		}
		
		// Si hay internet, envio el mensaje de entrada al servidor
		if (hasInternet) {
			try {
				MainGUI.getInstance().setTxtSalida(atenea.getClient().dialog(googleResponse));
				
				// Descomentar la siguiente linea para que el sistema repita lo que entendio
				// mainGUI.setTxtSalida(response); 
				
			} catch (Exception e) {
				MainGUI.getInstance().setTxtSalida("No logro conectarme al servidor.");
			}
			atenea.setState(AteneaState.PLAYING);
			MainGUI.getInstance().setTxtEstadoDelSistema(atenea.getStateText());
			
			PlayTextMessage.play(MainGUI.getInstance().getTxtSalida());
			
			atenea.setState(AteneaState.WAITING);
			MainGUI.getInstance().setTxtEstadoDelSistema(atenea.getStateText());
		} 
		
	}
}