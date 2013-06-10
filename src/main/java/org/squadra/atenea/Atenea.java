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
 * Esta clase carga la configuracion del programa y lanza la interfaz de usuario.
 * @author Leandro Morrone, Facundo D'Aranno
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
	
	/**  */
	@Getter @Setter private String waveFilePath;
	
	/**  */
	@Getter @Setter private String languageCode;
	
	public Atenea() {
		
		// ACA HAY QUE CARGAR LA CONFIGURACION DEL SISTEMA
		waveFilePath = "./prueba.wav";
		languageCode = "es-ES";
		
		this.state = new AteneaState();
		this.microphone = new Microphone(AudioFileFormat.Type.WAVE);
		MainGUI.createInstance(this);
	}

	public int getState() {
		return state.getState();
	}
	
	public String getStateText() {
		return state.toString();
	}

	public void setState(int state) {
		this.state.setState(state);
	}
	
}
