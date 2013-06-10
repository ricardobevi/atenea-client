package org.squadra.atenea.tts;

import javazoom.jl.player.Player;


/**
 * Clase estatica que reproduce un archivo MP3 que obtiene del sintetizador
 * @author Leandro Morrone
 */
public class PlayMP3 {
	
    public static void play(String mensaje){
        Synthesiser synthesiser = new Synthesiser();
        try {
            Player player = new Player(synthesiser.getMP3Data(mensaje));
            player.play();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}