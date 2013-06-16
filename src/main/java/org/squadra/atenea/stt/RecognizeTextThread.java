package org.squadra.atenea.stt;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;
import org.squadra.atenea.gui.MainGUI;
import org.squadra.atenea.tts.PlayTextMessage;

/**
 * Hilo de ejecucion que se comunica con el servidor y le envia el texto recibido de la interfaz
 * para obtener una respuesta y la reproduce.
 * @author Leandro Morrone
 */
public class RecognizeTextThread implements Runnable {

	/** Objeto que contiene las variables de configuracion y estado del sistema */
	private Atenea atenea;

	/**
	 * Constructor
	 * @param atenea
	 */
	public RecognizeTextThread(Atenea atenea) {
		this.atenea = atenea;
	}
	
	@Override
	public void run() {
		String inputMessage = MainGUI.getInstance().getTxtEntradaTexto();
		
		try {
			MainGUI.getInstance().setTxtSalida(atenea.getClient().dialog(inputMessage));

		} catch (Exception e) {
			MainGUI.getInstance().setTxtSalida("No logro conectarme al servidor.");
		}
		
		atenea.setState(AteneaState.PLAYING);
		MainGUI.getInstance().setTxtEstadoDelSistema(atenea.getStateText());
		
		// Reproduzco la respuesta, si se pudo conectar a internet
		try {
			PlayTextMessage.play(MainGUI.getInstance().getTxtSalida());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		atenea.setState(AteneaState.WAITING);
		MainGUI.getInstance().setTxtEstadoDelSistema(atenea.getStateText());
	}

}
