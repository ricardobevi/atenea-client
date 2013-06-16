package org.squadra.atenea.tts;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;


/**
 * Clase estatica que reproduce en audio un mensaje de texto
 * @author Leandro Morrone
 */
public class PlayTextMessage {
	
	/**
	 * Reproduce un mensaje de texto utilizando el sintetizador de Google
	 * @param message Mensaje de texto a ser reproducido
	 * @throws Exception 
	 * @throws JavaLayerException 
	 */
    public static void play(String message) throws Exception {
        Synthesiser synthesiser = new Synthesiser();
        Player player = new Player(synthesiser.getMP3Data(message));
        player.play();
    }
}