package org.squadra.atenea.speech;

// Listening del micr�fono
public class MicrophoneState implements Runnable {
	
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}