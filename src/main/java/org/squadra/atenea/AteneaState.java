package org.squadra.atenea;

public class AteneaState {
	
	public static final int WAITING = 0;
	public static final int RECORDING = 1;
	public static final int PLAYING = 2;
	public static final int PROCESSING = 3;

	private int state;

	public AteneaState() {
		state = WAITING;
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
		}
		return strState;
	}
	
}
