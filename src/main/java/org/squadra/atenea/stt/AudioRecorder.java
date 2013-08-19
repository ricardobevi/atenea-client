package org.squadra.atenea.stt;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.squadra.atenea.Atenea;

/**
 * Clase que contiene los metodos para capturar audio del microfono, reproducirlo y grabar
 * un archivo de audio de salida.
 * 
 * http://www.java-tips.org/java-se-tips/javax.sound/capturing-audio-with-java-sound-api.html
 * http://stackoverflow.com/questions/5800649/detect-silence-when-recording
 * 
 * @author Leandro Morrone
 */
public class AudioRecorder {

	/** Variable que indica si se esta capturando audio del microfono */
	private boolean running;
	
	/** Array que sirve para leer y grabar la salida de audio temporalmente */
	private ByteArrayOutputStream out;
	
	/** Contiene las especificaciones del tipo de audio (ej: muestreo, canales) */
	private AudioFormat format;

	// Constantes utilizadas para la deteccion de silencio y timeout
	private final static int TIMEOUT_RECORDING = 15;
	private final static int TIMEOUT_SILENCE = 3;
	private final static float MIN_PCM = 0.04f;
	
	// Constantes utilizadas para el calculo del PCM
	private final static float MAX_8_BITS_SIGNED = Byte.MAX_VALUE;
	private final static float MAX_8_BITS_UNSIGNED = 0xff;
	private final static float MAX_16_BITS_SIGNED = Short.MAX_VALUE;
	private final static float MAX_16_BITS_UNSIGNED = 0xffff;

	/**
	 * Constructor.
	 */
	public AudioRecorder() {
		this.format = getFormat();
	}

	/**
	 * Carga las especificaciones tecnicas del tipo de audio a capturar.
	 * @return AudioFormat
	 */
	private AudioFormat getFormat() {
		float sampleRate = 8000.0F; // 8000,11025,16000,22050,44100
		int sampleSizeInBits = 16; // 8,16
		int channels = 1; // 1,2
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}

	/**
	 * Detiene la grabacion.
	 */
	public void stopRecording() {
		running = false;
	}

	/**
	 * Inicia la grabacion de voz por el microfono.
	 * @throws LineUnavailableException No se detectaron microfonos.
	 */
	public void startRecording() throws LineUnavailableException {
		final AudioFormat format = getFormat();
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
		line.open(format);
		line.start();
		// llamo al hilo que captura audio y lo guarda en un buffer
		new Thread(new CaptureThread(line)).start();
	}
	
	/**
	 * Reproduce la grabacion.
	 */
	public void playRecording() {
		try {
			byte audio[] = out.toByteArray();
			InputStream input = new ByteArrayInputStream(audio);
			final AudioFormat format = getFormat();
			final AudioInputStream ais = new AudioInputStream(input, format,
					audio.length / format.getFrameSize());
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			final SourceDataLine line = (SourceDataLine) AudioSystem
					.getLine(info);
			line.open(format);
			line.start();
			// llamo al hilo que reproduce el audio del buffer
			new Thread(new PlayThread(ais, line)).start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Guarda el contenido del buffer en un archivo de audio de salida.
	 * @param audioFilePath Ruta del archivo de audio de salida
	 * @param fileType Formato del archivo de audio de salida (se recomienda WAVE)
	 */
	public void generateAudioFile(String audioFilePath, AudioFileFormat.Type fileType) {
		byte[] audio = out.toByteArray();
		InputStream input = new ByteArrayInputStream(audio);
		try {
			final AudioFormat format = getFormat();
			final AudioInputStream ais = new AudioInputStream(input, format,
					audio.length / format.getFrameSize());
			AudioSystem.write(ais, fileType, new File(audioFilePath));
			input.close();
			System.out.println("Archivo grabado!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Thread que captura el audio del microfono y lo almacena en el buffer.
	 * @author Leandro Morrone
	 */
	private class CaptureThread implements Runnable {
		
		int bufferSize;
		byte buffer[];
		TargetDataLine line;
		
		public CaptureThread(TargetDataLine line) {
			this.line = line;
			bufferSize = (int) format.getSampleRate() * format.getFrameSize();
			buffer = new byte[bufferSize];	
		}

		public void run() {

			out = new ByteArrayOutputStream();
			running = true;
			ArrayList<Float> lastPCMs = new ArrayList<Float>();
			int recordingTime = 0;

			while (running) {
				int count = line.read(buffer, 0, buffer.length);
				
				// Calculo los PCM de la ultima muestra capturada
				float level = calculatePCM(buffer);
				System.out.println("PCM: " + level);
				lastPCMs.add(level);
				
				// Si pasaron 15 segundos desde el inicio de la grabacion, la detengo.
				if (++recordingTime >= TIMEOUT_RECORDING) {
					System.out.println("============== TIMEOUT =============");
					Atenea.getInstance().getMicrophone().stopRecordingAndRecognize();
				}
				// Si se detecta silencio, detengo la captura de voz.
				else if (silenceDetected(lastPCMs)) {
					System.out.println("============== SILENCIO =============");
					Atenea.getInstance().getMicrophone().stopRecordingAndRecognize();
				}
				
				if (count > 0) {
					out.write(buffer, 0, count);
				}
			}
			line.stop();
			line.close();
		}
    }
	
	/**
	 * Verifica si hay un silencio prolongado.
	 * @param lastPCMs array con los ultimos PCM calculados.
	 * @return true si se considera silencio prolongado o false si no.
	 */
	private boolean silenceDetected(ArrayList<Float> lastPCMs) {
		
		if(lastPCMs.size() >= TIMEOUT_SILENCE) {
			// Mantengo el array de PCMs de tama�o fijo (simulo una cola circular)
			lastPCMs.remove(0);
			
			// Calculo el promedio de los ultimos PCM
			float sum = 0;
			for(int i = 0; i < lastPCMs.size(); i++) {
				sum += lastPCMs.get(i);
			}
			float average = sum / lastPCMs.size();
			System.out.println("Promedio: " + average);
			
			// Si el promedio es bajo se considera que hay silencio prolongado
			// El silencio "absoluto" es aprox 0.01, pero por el ruido se considera 0.04
			if(average < MIN_PCM) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Calcula el nivel de PCM para un instante dado de la grabacion.
	 * @param buffer Contiene la ultima porcion del audio capturado
	 * @return Nivel de PCM (Pulse Code Modulation) del audio del buffer
	 */
	private float calculatePCM(byte[] buffer) {
		int max = 0;
		boolean use16Bit = (format.getSampleSizeInBits() == 16);
		boolean signed = (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED);
		boolean bigEndian = (format.isBigEndian());
		
		if (use16Bit) {
			for (int i = 0; i < buffer.length; i += 2) {
				int value = 0;
				int hiByte = (bigEndian ? buffer[i] : buffer[i + 1]);
				int loByte = (bigEndian ? buffer[i + 1] : buffer[i]);
				if (signed) {
					short shortVal = (short) hiByte;
					shortVal = (short) ((shortVal << 8) | (byte) loByte);
					value = shortVal;
				} else {
					value = (hiByte << 8) | loByte;
				}
				max = Math.max(max, value);
			}
		} else {
			for (int i = 0; i < buffer.length; i++) {
				int value = 0;
				if (signed) {
					value = buffer[i];
				} else {
					short shortVal = 0;
					shortVal = (short) (shortVal | buffer[i]);
					value = shortVal;
				}
				max = Math.max(max, value);
			}
		}
		// expreso el maximo valor de 8 o 16 bits como float entre 0.0 y 1.0
		if (signed) {
			if (use16Bit) {
				return (float) max / MAX_16_BITS_SIGNED;
			} else {
				return (float) max / MAX_8_BITS_SIGNED;
			}
		} else {
			if (use16Bit) {
				return (float) max / MAX_16_BITS_UNSIGNED;
			} else {
				return (float) max / MAX_8_BITS_UNSIGNED;
			}
		}
	}

	
	/**
	 * Thread que reproduce el audio almacenado en el buffer.
	 * @author Leandro Morrone
	 */
	private class PlayThread implements Runnable {

		int bufferSize;
		byte buffer[];
		AudioInputStream ais;
		SourceDataLine line;
		
		public PlayThread(AudioInputStream ais, SourceDataLine line) {
			this.ais = ais;
			this.line = line;
			bufferSize = (int) format.getSampleRate() * format.getFrameSize();
			buffer = new byte[bufferSize];
		}

		public void run() {
			try {
				int count;
				while ((count = ais.read(buffer, 0, buffer.length)) != -1) {
					if (count > 0) {
						line.write(buffer, 0, count);
					}
				}
				line.drain();
				line.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
	
}