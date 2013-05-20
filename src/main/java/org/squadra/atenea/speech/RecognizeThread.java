package org.squadra.atenea.speech;

import javax.swing.JButton;
import javax.swing.JTextField;

import org.squadra.atenea.recognizer.GoogleResponse;
import org.squadra.atenea.recognizer.Recognizer;
import org.squadra.atenea.webservice.AteneaWs;

// Funci�n que se encarga de la traducci�n de voz a texto
public class RecognizeThread implements Runnable {

	private JTextField mensaje;
	private String rutaArchivoWav; // Variable para guardar la ruta del archivo
									// .wav de salida
	private String codigoDeIdioma; // Variable con el idioma utilizado por el
									// sintetizador de voz
	private AteneaWs client;
	private JButton record;
	private String response;
	private Boolean hasInternet = true;

	public RecognizeThread(JTextField mensaje, String rutaArchivoWav,
			String codigoDeIdioma, AteneaWs client, JButton record) {
		super();
		this.mensaje = mensaje;
		this.rutaArchivoWav = rutaArchivoWav;
		this.codigoDeIdioma = codigoDeIdioma;
		this.client = client;
		this.record = record;
	}

	@Override
	public void run() {

		Recognizer recognizer = new Recognizer();
		mensaje.setText("reconociendo...");
		// Env�o a Google el audio y el idioma y guardo la respuesta
		// devuelta

		try {
			// Creo un hilo que envie el audio a Google y reciba el texto
			GoogleResponse googleResponse = recognizer
					.getRecognizedDataForWave(rutaArchivoWav, codigoDeIdioma);
			response = googleResponse.getResponse();

		} catch (Exception e) {
			mensaje.setText("UPS!! No logre conectarme a internet y requiero de ella para funcionar");
			hasInternet = false;

		}

		// Guardo el texto recibido ya decodificado
		// String texto = new
		// String(googleResponse.getResponse().getBytes(),"UTF-8");
		// Imprimo el texto por pantalla

		if (hasInternet) {

			try {
				mensaje.setText(client.dialog(response));
			} catch (Exception e) {
				mensaje.setText("No logró conectarme al servidor");
			}
			

			System.out.println("****************" + mensaje.getText() + "****************");

			PlayMP3.play(mensaje);
			
		} 
			
		record.setEnabled(true);
		

	}
}