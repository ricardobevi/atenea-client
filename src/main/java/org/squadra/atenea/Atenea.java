package org.squadra.atenea;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.squadra.atenea.gui.MainGUI;
import org.squadra.atenea.stt.Microphone;
import org.squadra.atenea.util.StaticMethods;
import org.squadra.atenea.ateneacommunication.AteneaWs;


/**
 * Esta clase carga la configuracion del programa, contiene el estado del sistema y 
 * lanza la interfaz de usuario. Es un singleton para que pueda ser llamada desde 
 * cualquier parte del proyecto.
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
	
	/** Singleton */
	private static Atenea INSTANCE = null;
	
	/**
	 * Crea una instancia del objeto Atenea, y si ya existe la devuelve
	 * @return instancia singleton de la interfaz principal
	 * @author Leandro Morrone
	 */
	public static Atenea createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Atenea();
		}
		return INSTANCE;
	}
	
	/**
	 * Devuelve la instancia de Atenea
	 * @return instancia singleton de la interfaz principal
	 * @author Leandro Morrone
	 */
	public static Atenea getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Constructor privado: Carga la configuracion e inicializa las variables necesarias
	 * @author Leandro Morrone
	 */
	private Atenea() {
		
		// ACA HAY QUE CARGAR LA CONFIGURACION DEL SISTEMA
		waveFilePath = "./audioInput.wav";
		languageCode = "es-ES";
		
		this.state = new AteneaState();
		this.microphone = new Microphone();
	}
	
	/**
	 * Lanza la interfaz de usuario principal
	 * @author Leandro Morrone
	 */
	public void launchMainGUI() {
		MainGUI.createInstance();
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
		MainGUI.getInstance().setTxtEstadoDelSistema(this.getStateText());
	}
	
}
