package org.squadra.atenea.tts;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;
import org.squadra.atenea.actions.Command;
import org.squadra.atenea.ateneacommunication.Message;
import org.squadra.atenea.gui.MainGUI;
import org.squadra.atenea.gui.MainGUIPrototype;

/**
 * Clase que se encarga de procesar el mensaje retornado por el servidor. Actua segun
 * el tipo de mensaje devuelto, por ejemplo, reproduce una respuesta, ejecuta una
 * accion, etc.
 * @author Leandro Morrone
 */
public class MessageProcessor {

	/**
	 * Este metodo procesa el mensaje de salida segun su tipo.
	 * Si es una orden la ejecuta y reproduce el mensaje de voz.
	 * @param message Mensaje de salida devuelto por el servidor
	 */
	public static void processMessage(Message message) {
		
		// Si es una orden la ejecuto
		if(message.getType() == Message.ORDER) {
			processOrder(message.getOrder());
		}
		
		String outputText = message.getText();
		
		// Muestro por pantalla el mensaje de salida
		MainGUI.getInstance().setTxtOutput(outputText);
		//MainGUIPrototype.getInstance().setTxtSalida(outputText);
		
		// Reproduzco el mensaje mostrado
		Atenea.getInstance().setState(AteneaState.PLAYING);
		try {
			PlayTextMessage.play(outputText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Atenea.getInstance().setState(AteneaState.WAITING);
	}
	
	private static void processOrder(String orden) {
		Command cmd = new Command(Atenea.SO_NAME, orden, "./salida.txt");
		cmd.run();
	}
	
}
