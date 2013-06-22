package org.squadra.atenea.exceptions;

@SuppressWarnings("serial")
public class GoogleTTSException extends Exception {

	public GoogleTTSException() {
		super();
	}

	public GoogleTTSException(String message) {
		super(message);
	}

	public GoogleTTSException(String message, Throwable cause) {
		super(message, cause);
	}

	public GoogleTTSException(Throwable cause) {
		super(cause);
	}
}
