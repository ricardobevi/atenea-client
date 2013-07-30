package org.squadra.atenea.stt;

import javax.sound.sampled.AudioFileFormat;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;

/**
 * Clase que funciona como interfaz entre la GUI y el AudioRecorder.
 * Esto reduce la logica en la GUI, permite que estos metodos sean llamados desde
 * otras clases, y reduce el acomplamiento entre AudioRecorder y Atenea.
 * @author Leandro Morrone
 */
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
		Atenea.getInstance().setState(AteneaState.RECORDING);
		recorder.startRecording();
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
