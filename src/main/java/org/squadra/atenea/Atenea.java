package org.squadra.atenea;

import javax.sound.sampled.AudioFileFormat;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.squadra.atenea.gui.MainGUI;
import org.squadra.atenea.stt.Microphone;
import org.squadra.atenea.util.StaticMethods;
import org.squadra.atenea.webservice.AteneaWs;

/**
 * Esta clase carga la configuracion del programa, contiene el estado del sistema y 
 * lanza la interfaz de usuario.
 * @author Leandro Morrone
 */
public @Data class Atenea {

	/** Constante con el nombre del sistema operativo sobre el que corre la aplicacion */
	public static final String SO_NAME = StaticMethods.getSOName();
	
	/** Variable que contiene el cliente del Web Service */
	@Getter @Setter private AteneaWs client;
	
	/** Variable que almacena el estado actual del sistema */
	private AteneaState state;
	
	/** Objeto microfono que contiene las funciones de captura de audio */
	@Getter @Setter private Microphone microphone;
	
	/** Ruta donde se guarda el archivo WAVE al finalizar la grabacion por voz */
	@Getter @Setter private String waveFilePath;
	
	/** Codigo de idioma para realizar la traduccion de voz a texto */
	@Getter @Setter private String languageCode;
	
	/**
	 * Constructor: Carga la configuracion, inicializa las variables y lanza la GUI principal
	 * @author Leandro Morrone
	 */
	public Atenea() {
		
		// ACA HAY QUE CARGAR LA CONFIGURACION DEL SISTEMA
		waveFilePath = "./prueba.wav";
		languageCode = "es-ES";
		
		this.state = new AteneaState();
		this.microphone = new Microphone(AudioFileFormat.Type.WAVE);
		
		MainGUI.createInstance(this);
	}

	/**
	 * Obtiene el estado del sistema en formato de entero
	 * @return estado actual del sistema
	 * @author Leandro Morrone
	 */
	public int getState() {
		return state.getState();
	}
	
	/**
	 * Obtiene el estado del sistema en formato de texto
	 * @return estado actual del sistema
	 * @author Leandro Morrone
	 */
	public String getStateText() {
		return state.toString();
	}

	/**
	 * Modifica el estado actual del sistema
	 * @param state nuevo estado
	 * @author Leandro Morrone
	 */
	public void setState(int state) {
		this.state.setState(state);
	}
	
}
