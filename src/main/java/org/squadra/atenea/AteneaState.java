package org.squadra.atenea;

/**
 * Esta clase se utiliza para almacenar y obtener el estado del sistema
 * @author Leandro Morrone
 */
public class AteneaState {
	
	/** Indica que el sistema se encuentra en estado de espera */
	public static final int WAITING = 0;
	
	/** Indica que el sistema se encuentra grabando un mensaje de voz */
	public static final int RECORDING = 1;
	
	/** Indica que el sistema se encuentra reproduciendo audio */
	public static final int PLAYING = 2;
	
	/** Indica que el sistema se encuentra procesando el mensaje de entrada */
	public static final int PROCESSING = 3;
	
	/** Indica que el sistema se encuentra procesando el mensaje de entrada */
	public static final int LEARNING = 4;

	/** Variable que representa el estado del sistema */
	private int state;

	/**
	 * Constructor: inicializa el sistema en estado de espera
	 */
	public AteneaState() {
		this.state = WAITING;
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public String toString() {
		String strState = "";
		switch (state) {
			case WAITING:
				strState = "Esperando";
				break;
			case RECORDING:
				strState = "Grabando";
				break;
			case PLAYING:
				strState = "Reproduciendo";
				break;
			case PROCESSING:
				strState = "Procesando";
				break;
			case LEARNING:
				strState = "Aprendiendo";
				break;
		}
		return strState;
	}
	
}
