package org.squadra.atenea.speech;

import javax.swing.JTextField;

import javazoom.jl.player.Player;

import org.squadra.atenea.synthesiser.Synthesiser;

/**
 * Clase estatica que traduce texto a voz 
 * mediante la API de google
 * @author tempuses
 *
 */
public class PlayMP3 {
	
    public static void play(JTextField mensaje){
        Synthesiser synthesiser = new Synthesiser();
        try {
        	// Reproduzco el texto
            Player player = new Player(synthesiser.getMP3Data(mensaje.getText()));
            player.play();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void play(String mensaje){
        Synthesiser synthesiser = new Synthesiser();
        try {
        	// Reproduzco el texto
            Player player = new Player(synthesiser.getMP3Data(mensaje));
            player.play();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}