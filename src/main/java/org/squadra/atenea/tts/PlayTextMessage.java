package org.squadra.atenea.tts;

import javazoom.jl.player.Player;


/**
 * Clase estatica que reproduce en audio un mensaje de texto
 * @author Leandro Morrone
 */
public class PlayTextMessage {
	
	/**
	 * Reproduce un mensaje de texto utilizando el sintetizador de Google
	 * @param message Mensaje de texto a ser reproducido
	 */
    public static void play(String message){
        Synthesiser synthesiser = new Synthesiser();
        try {
            Player player = new Player(synthesiser.getMP3Data(message));
            player.play();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}