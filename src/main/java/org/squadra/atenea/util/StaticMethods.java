package org.squadra.atenea.util;

/**
 * Clase para la implementacion de metodos estaticos generales para todo el proyecto.
 * @author Leandro Morrone
 */
public class StaticMethods {

	private StaticMethods() {
	}
	
	/**
	 * Varifica que sistema operativo se esta utilizando y devuelve el nombre.
	 * @author Leandro Morrone
	 * @return Nombre del sistema operativo sin la version (windows, mac, linux o unknown).
	 */
	public static String getSOName() {
		String OS = System.getProperty("os.name").toLowerCase();
		
		if (OS.indexOf("win") >= 0) {
			return "windows";
		}
		else if (OS.indexOf("mac") >= 0) {
			return "mac";
		}
		else if (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 ) {
			return "linux";
		}
		else {
			return "unknown";
		}
	}
}
