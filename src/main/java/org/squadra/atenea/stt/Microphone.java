package org.squadra.atenea.stt;

import javax.sound.sampled.AudioFileFormat;

import lombok.extern.log4j.Log4j;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;
import org.squadra.atenea.ateneacommunication.Message;
import org.squadra.atenea.tts.MessageProcessor;

/**
 * Clase que funciona como interfaz entre la GUI y el AudioRecorder.
 * Esto reduce la logica en la GUI, permite que estos metodos sean llamados desde
 * otras clases, y reduce el acomplamiento entre AudioRecorder y Atenea.
 * @author Leandro Morrone
 */
@Log4j
public class Microphone {

	/** Objeto para grabar, detener y reproducir audio */
	private AudioRecorder recorder;
	
	/**
	 * Constructor: instancia el recorder
	 */
	public Microphone() {
		recorder = new AudioRecorder();
	}
	
	/**
	 * Inicia la grabacion de voz y cambia el estado de atenea a grabando.
	 */
	public void startRecording() {
		try {
			Atenea.getInstance().setState(AteneaState.RECORDING);
			recorder.startRecording();
		} catch (Exception e) {
			(new Thread() {
				public void run() {
					Message outputMessage = new Message(
							"No tienes encendido tu micrófono.", Message.ERROR);
					MessageProcessor.processMessage(outputMessage);
				}
			}).start();
			log.error("No se detecta el microfono.");
		}

	}
	
	/**
	 * Detiene la grabacion, guarda el archivo de audio y llama al hilo encargado
	 * del reconocimiento del mensaje.
	 */
	public void stopRecordingAndRecognize() {
		Atenea.getInstance().setState(AteneaState.PROCESSING);
		recorder.stopRecording();
		recorder.generateAudioFile(Atenea.getInstance().getWaveFilePath(), AudioFileFormat.Type.WAVE);
		new Thread(new RecognizeVoiceThread()).start();
	}
	
	/**
	 * Reproduce el audio grabado.
	 */
	public void playRecording() {
		Atenea.getInstance().setState(AteneaState.PLAYING);
		recorder.playRecording();
		Atenea.getInstance().setState(AteneaState.WAITING);
	}
}
