package org.squadra.atenea.stt;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

/**
 * Microphone class that contains methods to capture audio from microphone
 *
 * @author Luke Kuza
 */
public class Microphone {

    /**
     * TargetDataLine variable to receive data from microphone
     */
    private TargetDataLine targetDataLine;

    /**
     * Enum for current Microphone state
     */
    public enum CaptureState {
        PROCESSING_AUDIO, STARTING_CAPTURE, CLOSED
    }

    /**
     * Variable for enum
     */
    CaptureState state;

    /**
     * Variable for the audios saved file type
     */
    private AudioFileFormat.Type fileType;
    
    /**
     * Variable to save the audio input stream
     */
    private AudioInputStream audioInputStream;

    /**
     * Variable that holds the saved audio file
     */
    private File audioFile;

    /**
     * Gets the current state of Microphone
     *
     * @return PROCESSING_AUDIO is returned when the Thread is recording Audio and/or saving it to a file<br>
     *         STARTING_CAPTURE is returned if the Thread is setting variables<br>
     *         CLOSED is returned if the Thread is not doing anything/not capturing audio
     */
    public CaptureState getState() {
        return state;
    }

    /**
     * Sets the current state of Microphone
     *
     * @param state State from enum
     */
    private void setState(CaptureState state) {
        this.state = state;
    }

    public File getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(File audioFile) {
        this.audioFile = audioFile;
    }

    public AudioFileFormat.Type getFileType() {
        return fileType;
    }

    public void setFileType(AudioFileFormat.Type fileType) {
        this.fileType = fileType;
    }

    public TargetDataLine getTargetDataLine() {
        return targetDataLine;
    }

    public void setTargetDataLine(TargetDataLine targetDataLine) {
        this.targetDataLine = targetDataLine;
    }

    public AudioInputStream getAudioInputStream() {
		return audioInputStream;
	}

	public void setAudioInputStream(AudioInputStream audioInputStream) {
		this.audioInputStream = audioInputStream;
	}

    /**
     * Constructor
     *
     * @param fileType File type to save the audio in<br>
     *                 Example, to save as WAVE use AudioFileFormat.Type.WAVE
     */
    public Microphone(AudioFileFormat.Type fileType) {
        setState(CaptureState.CLOSED);
        setFileType(fileType);
    }


    /**
     * Captures audio from the microphone and saves it a file
     *
     * @param audioFile The File to save the audio to
     * @throws Exception Throws an exception if something went wrong
     */
    public void captureAudioToFile(File audioFile) throws Exception {
        setState(CaptureState.STARTING_CAPTURE);
        setAudioFile(audioFile);

        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
        setTargetDataLine((TargetDataLine) AudioSystem.getLine(dataLineInfo));

        //Get Audio
        new Thread(new CaptureThread()).start();
    }

    /**
     * Captures audio from the microphone and saves it a file
     *
     * @param audioFile The fully path (String) to a file you want to save the audio in
     * @throws Exception Throws an exception if something went wrong
     */
    public void captureAudioToFile(String audioFile) throws Exception {
        setState(CaptureState.STARTING_CAPTURE);
        File file = new File(audioFile);
        setAudioFile(file);

        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
        setTargetDataLine((TargetDataLine) AudioSystem.getLine(dataLineInfo));

        //Get Audio
        new Thread(new CaptureThread()).start();
    }

    /**
     * The audio format to save in
     *
     * @return Returns AudioFormat to be used later when capturing audio from microphone
     */
    private AudioFormat getAudioFormat() {
        float sampleRate = 8000.0F;
        //8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        //8,16
        int channels = 1;
        //1,2
        boolean signed = true;
        //true,false
        boolean bigEndian = false;
        //true,false
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
    Thread hilito;
    /**
     * Close the microphone capture, saving all processed audio to the specified file.<br>
     * If already closed, this does nothing
     */
    public void close() {
        if (getState() == CaptureState.CLOSED) {
        }
        else {
        	setState(CaptureState.CLOSED);
            getTargetDataLine().stop();
            getTargetDataLine().close();
        }
    }
    

	/**
     * Thread to capture the audio from the microphone and save it to a file
     * @author modificada por Leandro Morrone
     */
    private class CaptureThread implements Runnable {

        /**
         * Run method for thread
         */
        public void run() {
            try {
                setState(CaptureState.PROCESSING_AUDIO);
                AudioFileFormat.Type fileType = getFileType();
                File audioFile = getAudioFile();
                getTargetDataLine().open(getAudioFormat());
                getTargetDataLine().start();
                setAudioInputStream(new AudioInputStream(getTargetDataLine()));
                // Creo el hilo para detener la grabacion si hay silencios
                //new Thread(new CheckSilenceThread()).start();
                AudioSystem.write(getAudioInputStream(), fileType, audioFile);
                setState(CaptureState.CLOSED);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } 
    }
    
    
    /**
     * Thread que calcula los PCM (Pulse Code Modulation) detectados desde el microfono mientras
     * se esta grabando, y detiene la grabacion si detecta un silencio prolongado.
     * @author Leandro Morrone
     */
    private class CheckSilenceThread implements Runnable {

    	private final static float MAX_8_BITS_SIGNED = Byte.MAX_VALUE;
    	private final static float MAX_8_BITS_UNSIGNED = 0xff;
    	private final static float MAX_16_BITS_SIGNED = Short.MAX_VALUE;
    	private final static float MAX_16_BITS_UNSIGNED = 0xffff;

    	AudioFormat	format = getAudioInputStream().getFormat();
        int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
    	byte[] buffer = new byte[bufferSize];
    	
    	@Override
    	public void run() {
    		ByteArrayOutputStream out = new ByteArrayOutputStream();
    		
    		// Mientras este grabando, calculo los PCM
    		/*while (getState() == CaptureState.PROCESSING_AUDIO) {
    			int count = getTargetDataLine().read(buffer, 0, buffer.length);
    			float level = calculatePCM();
    			System.out.println(level + " - " + count);

    			if (count > 0) {
    				out.write(buffer, 0, count);
    			}
    		}*/
    	}
    	
    	/**
    	 * Calcula el nivel de PCM para un instante dado de la gabacion
    	 * @return Nivel de PCM (Pulse Code Modulation)
    	 * @author modificada por Leandro Morrone
    	 */
    	private float calculatePCM() {
    		
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
    	
    }
}
