package org.squadra.atenea.stt;


/**
 * Listening del microfono
 * @author tempuses
 * */
public class MicrophoneStateThread implements Runnable {
	
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