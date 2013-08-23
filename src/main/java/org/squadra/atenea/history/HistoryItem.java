package org.squadra.atenea.history;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa un item del historial de conversacion.
 * @author Leandro Morrone
 *
 */
public class HistoryItem {
	
	/** Usuario que registra la accion */
	@Getter @Setter private String user;
	
	/** Tipo de item (debe ser una de las constantes definidas) */
	@Getter @Setter private int type;
	
	/** Fecha en que se almaceno el item */
	@Getter @Setter private Date date;
	
	/** Mensaje */
	@Getter @Setter private String message;
	
	// Constantes con los tipos de item posibles.
	public static final int INPUT_TEXT_MESSAGE = 1;
	public static final int INPUT_VOICE_MESSAGE = 2;
	public static final int INPUT_ACTION = 3;
	public static final int OUTPUT_MESSAGE = 4;
	public static final int OUTPUT_ACTION = 5;
	public static final int OUTPUT_ERROR = 6;
	
	/**
	 * Constructor
	 * @param type Tipo del item de historial
	 * @param message Mensaje
	 * @param date Fecha de creacion
	 */
	public HistoryItem(String user, int type, String message, Date date) {
		this.user = user;
		this.type = type;
		this.message = message;
		this.date = date;
	}

	@Override
	public String toString() {
		return "HistoryItem [user=" + user + ", type=" + type + ", message=" + message + 
				", date=" + date + "]";
	}
	
	
	
}
