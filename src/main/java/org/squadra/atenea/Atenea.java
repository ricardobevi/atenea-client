package org.squadra.atenea;

import javax.sound.sampled.AudioFileFormat;

import org.squadra.atenea.gui.MainGUI;
import org.squadra.atenea.stt.Microphone;
import org.squadra.atenea.util.StaticMethods;
import org.squadra.atenea.webservice.AteneaWs;

/**
 * Esta clase carga la configuracion del programa y lanza la interfaz de usuario.
 * @author Leandro Morrone, Facundo D'Aranno
 */
public class Atenea {

	/** Constante con el nombre del sistema operativo sobre el que corre la aplicacion */
	public static final String SO_NAME = StaticMethods.getSOName();
	
	/** Variable que contiene el cliente del Web Service */
	private AteneaWs client;
	
	/** Variable que almacena el estado actual del sistema */
	private AteneaState state;
	
	/** Objeto microfono que contiene las funciones de captura de audio */
	private Microphone microphone;
	
	public Atenea() {
		this.state = new AteneaState();
		this.microphone = new Microphone(AudioFileFormat.Type.WAVE);
		MainGUI mainGUI = new MainGUI(this);		
	}

	public AteneaWs getClient() {
		return client;
	}

	public void setClient(AteneaWs client) {
		this.client = client;
	}

	public int getState() {
		return state.getState();
	}

	public void setState(int state) {
		this.state.setState(state);
	}

	public Microphone getMicrophone() {
		return microphone;
	}

	public void setMicrophone(Microphone microphone) {
		this.microphone = microphone;
	}

	
}
