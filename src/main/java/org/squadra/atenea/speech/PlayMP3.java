package org.squadra.atenea.speech;

import javax.swing.JTextField;

import javazoom.jl.player.Player;

import org.squadra.atenea.synthesiser.Synthesiser;

// Funci�n que se encarga de la traducci�n de texto a voz
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
}