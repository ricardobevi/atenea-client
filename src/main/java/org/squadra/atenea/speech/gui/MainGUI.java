package org.squadra.atenea.speech.gui;

import groovy.transform.EqualsAndHashCode;

import javax.sound.sampled.AudioFileFormat;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import lombok.Data;
import lombok.ToString;

import org.squadra.atenea.speech.MicrophoneState;
import org.squadra.atenea.speech.RecognizeThread;
import org.squadra.atenea.speech.microphone.Microphone;

import com.spring.webservices.taller1.service.HolaMundoService;

@SuppressWarnings("serial")
@EqualsAndHashCode
@ToString
public @Data
class MainGUI extends javax.swing.JFrame {

	private String rutaArchivoWav; // Variable para guardar la ruta del archivo
									// .wav de salida
	private String codigoDeIdioma; // Variable con el idioma utilizado por el
									// sintetizador de voz
	private JButton record; // Componentes de la interfaz de usuario (biblioteca
							// Swing)
	private JTextField mensaje;
	private JButton stop;
	private Microphone microphone = new Microphone(AudioFileFormat.Type.WAVE); // Creo
																				// un
																				// objeto
																				// Microphone
	private HolaMundoService client;

	// Funcion que se ejecuta al hacer click en el boton Grabar
	private void recordActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			record.setEnabled(false);
			stop.setEnabled(true);
			// Inicio la captura de voz y la guardo en un archivo .wav
			mensaje.setText( "grabando...");
			microphone.captureAudioToFile(rutaArchivoWav);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Funcion que se ejecuta al hacer click en el boton Reproducir
	private void stopActionPerformed(java.awt.event.ActionEvent evt) {
		stop.setEnabled(false);
		// Finalizo la captura de voz
		microphone.close();

		// Creo un hilo que envie el audio a Google y reciba el texto
		new Thread(new RecognizeThread(mensaje, rutaArchivoWav, codigoDeIdioma,
				client, record)).start();

	}

	/******************************************************************************************************************/

	public HolaMundoService getClient() {
		return client;
	}

	public void setClient(HolaMundoService client) {
		this.client = client;
	}

	public MainGUI() {
		// Creo la interfaz de usuario y sus componentes
		initComponents();
		// Creo un hilo que reconozca el microfono
		new Thread(new MicrophoneState()).start();
	}

	private void initComponents() {

		// Inicializo las variables y los componentes de la interfaz de usuario
		rutaArchivoWav = "./prueba.wav";
		codigoDeIdioma = "es-ES";
		record = new JButton("Grabar");
		stop = new JButton("Reproducir");
		stop.setEnabled(false);
		mensaje = new JTextField("esperando...");
		mensaje.setEditable(false);

		// Seteo el evento para cerrar la aplicacin
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		// Seteo el ttulo de la aplicacin
		setTitle("Atenea");

		// Seteo el tamao de la ventana
		this.setSize(700, 100);

		// Agrego el evento click al boton de Grabar
		record.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				recordActionPerformed(evt);
			}
		});

		// Agrego el evento click al botn de Reproducir
		stop.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				stopActionPerformed(evt);
			}
		});

		// Creacin de la interfaz de usuario
		// Tutorial para armar la estructura por cdigo:
		// http://docs.oracle.com/javase/tutorial/uiswing/layout/groupExample.html
		// Tutorial de Java Swing:
		// http://docs.oracle.com/javase/tutorial/uiswing/components/index.html

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setHorizontalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.LEADING)
								.addComponent(mensaje)
								.addGroup(
										layout.createSequentialGroup()
												.addGroup(
														layout.createParallelGroup(
																GroupLayout.Alignment.LEADING)
																.addComponent(
																		record))
												.addGroup(
														layout.createParallelGroup(
																GroupLayout.Alignment.LEADING)
																.addComponent(
																		stop)))));

		layout.setVerticalGroup(layout
				.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.BASELINE)
								.addComponent(record).addComponent(stop))
				.addComponent(mensaje));

	}

}
