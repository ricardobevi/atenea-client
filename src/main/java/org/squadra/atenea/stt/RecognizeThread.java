package org.squadra.atenea.stt;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;
import org.squadra.atenea.gui.MainGUI;
import org.squadra.atenea.tts.PlayMP3;

/**
 * Funcion que se encarga de la traduccion de voz a texto y ejecuta la conversion texto a voz
 * @author tempuses
 *
 */
public class RecognizeThread implements Runnable {

	private Atenea atenea;
	private Boolean hasInternet = true;

	public RecognizeThread(Atenea atenea) {
		super();
		this.atenea = atenea;
	}

	@Override
	public void run() {

		Recognizer recognizer = new Recognizer();
		String googleResponse = "";
		
		// Envio a Google el audio y el idioma y guardo la respuesta devuelta

		try {
			// Creo un hilo que envie el audio a Google y reciba el texto
			googleResponse = recognizer
					.getRecognizedDataForWave(atenea.getWaveFilePath(), atenea.getLanguageCode())
					.getResponse();
			MainGUI.getInstance().setTxtEntradaAudio(googleResponse);

		} catch (Exception e) {
			MainGUI.getInstance().setTxtSalida("No logro conectarme a internet.");
			hasInternet = false;
		}

		if (hasInternet) {
			try {
				MainGUI.getInstance().setTxtSalida(atenea.getClient().dialog(googleResponse)); 
				//mainGUI.setTxtSalida(response); 
				
			} catch (Exception e) {
				MainGUI.getInstance().setTxtSalida("No logro conectarme al servidor.");
			}
			atenea.setState(AteneaState.PLAYING);
			MainGUI.getInstance().setTxtEstadoDelSistema(atenea.getStateText());
			
			PlayMP3.play(MainGUI.getInstance().getTxtSalida());
			
			atenea.setState(AteneaState.WAITING);
			MainGUI.getInstance().setTxtEstadoDelSistema(atenea.getStateText());
		} 
		
	}
}