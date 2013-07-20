package org.squadra.atenea.tts;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;
import org.squadra.atenea.actions.Command;
import org.squadra.atenea.aiengine.Message;
import org.squadra.atenea.gui.MainGUI;

public class MessageProcessor {

	/**
	 * Este metodo procesa el mensaje de salida segun su tipo.
	 * Si es una orden la ejecuta y reproduce el mensaje de voz.
	 * @param atenea
	 * @param message Mensaje de salida devuelto por el servidor
	 */
	public static void processMessage(Atenea atenea, Message message) {
		
		// Si es una orden la ejecuto
		if(message.getType() == Message.ORDER) {
			processOrder(message.getOrder());
		}
		
		// Muestro por pantalla el mensaje de salida
		MainGUI.getInstance().setTxtSalida(message.getText());
		
		// Reproduzco el mensaje mostrado
		atenea.setState(AteneaState.PLAYING);
		try {
			PlayTextMessage.play(MainGUI.getInstance().getTxtSalida());
		} catch (Exception e) {
			MainGUI.getInstance().setTxtSalida("No logro conectarme a Internet.");
			e.printStackTrace();
		}
		atenea.setState(AteneaState.WAITING);
	}
	
	private static void processOrder(String orden) {
		Command cmd = new Command(Atenea.SO_NAME, "control", "./salida.txt");
		cmd.run();
	}
	
}
