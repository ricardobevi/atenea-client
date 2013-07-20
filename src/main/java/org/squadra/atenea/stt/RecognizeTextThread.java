package org.squadra.atenea.stt;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.aiengine.Message;
import org.squadra.atenea.gui.MainGUI;
import org.squadra.atenea.tts.MessageProcessor;

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
		
		String textMessage = MainGUI.getInstance().getTxtEntradaTexto();
		Message outputMessage = new Message();
		
		try {
			Message inputMessage = new Message(textMessage);
			
			// ESTA LINEA ENVIA EL MENSAJE AL SERVIDOR Y RECIBE LA RESPUESTA
			outputMessage = atenea.getClient().dialog(inputMessage);

		} catch (Exception e) {
			outputMessage = new Message("No logro conectarme al servidor.", Message.ERROR);
		}
		
		MessageProcessor.processMessage(atenea, outputMessage);
	}

}
