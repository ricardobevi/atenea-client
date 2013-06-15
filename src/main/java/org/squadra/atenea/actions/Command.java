package org.squadra.atenea.actions;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Clase para la ejecucion de comandos del sistema operativo, tanto en Windows como Linux.
 * @author Leandro Morrone
 */
public class Command {

	private final static int OUTPUT_OK = 0;
	private final static int OUTPUT_ERROR = 1;
	private final static int ERROR_INCOMPATIBLE_SO = 2;
	private final static int ERROR_INVALID_SCRIPT = 3;
	private final static int ERROR_RUNNING_SCRIPT = 4;
	
	/** Nombre del sistema operativo (windows, linux, mac o unknown) */
	private String osName;

	/** Comando o ruta del script */
	private String script;
	
	/** Ruta del archivo donde se grabara la salida del script */
	private String outputFilePath;

	/**
	 * Carga un comando para ser ejecutado.
	 * @author Leandro Morrone
	 * @param osName Nombre del SO sobre el que se ejecuta el programa.
	 * @param script Comando de SO o ruta del archivo con el script a ejecutar.
	 * @param outputFilePath ruta del archivo para guardar la salida del script.
	 */
	public Command(String osName, String script, String outputFilePath) {
		this.osName = osName;
		this.script = script;
		this.outputFilePath = outputFilePath;
	}

	/**
	 * Ejecuta el comando y guarda la salida en un archivo de texto.
	 * @author Leandro Morrone
	 * @return 0 si se ejecuta correctamente, >0 si ocurre algun tipo de error.
	 */
	public int run() {

		String[] cmd;
		
		// Armo la estructura segun el sistema operativo
		if (osName == "windows") {
			cmd = new String[3];
			cmd[0] = "cmd.exe";
			cmd[1] = "/C";
			cmd[2] = script;
		}
		else if (osName == "linux") {
			cmd = new String[1];
			cmd[0] = script;
		}
		else {
			return ERROR_INCOMPATIBLE_SO;
		}

		try {
			System.out.println("Ejecutando script:\n" + script);
			
			// Ejecuto el script
			Runtime rt = Runtime.getRuntime();
			Process proc = rt.exec(cmd);
			
			// Creo un hilo que obtenga la salida por error
			StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR", outputFilePath);
			// Creo un hilo que obtenga la salida por ejecucion correcta
			StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT", outputFilePath);
			errorGobbler.start();
			outputGobbler.start();

			// Devuelvo 1 si hay error y 0 si es correcto
			int exitValue = proc.waitFor();
			if(exitValue == 1) {
				return OUTPUT_ERROR;
			}
			else {
				return OUTPUT_OK;
			}

		} catch (IOException e) {
			System.out.println("ERROR: Script no encontrado.");
			return ERROR_INVALID_SCRIPT;
		} catch (InterruptedException e) {
			System.out.println("ERROR: Ejecucion interrumpida");
			return ERROR_RUNNING_SCRIPT;
		}
	}
}

/**
 * Thread que cuando se ejecuta, lee los datos de salida del comando y los almacena
 * en un archivo de texto.
 * @author Leandro Morrone
 */
class StreamGobbler extends Thread {
	
	/** Almacena la salida del script */
	private InputStream is;
	
	/** Indica el tipo de salida (ERROR o OUTPUT) */
	private String type;
	
	/** Ruta del archivo donde se grabara la salida del script */
	private String outputFilePath;

	/**
	 * Carga los datos que requiere el thread para su ejecucion.
	 * @param is Salida del script.
	 * @param type Tipo de salida.
	 * @param outputFilePath ruta del archivo para guardar la salida del script.
	 */
	StreamGobbler(InputStream is, String type, String outputFilePath) {
		this.is = is;
		this.type = type;
		this.outputFilePath = outputFilePath;
	}

	/**
	 * Sobreescribe el metodo run del Thread. Lee los datos de salida del comando que son
	 * almacenados en un buffer, y los guarda en un archivo de texto.
	 * @author Leandro Morrone
	 */
	@Override
	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			FileWriter file = new FileWriter(outputFilePath, false);
			PrintWriter printWriter = new PrintWriter(file);
			
			// Lectura de la salida del script linea por linea
			while ((line = br.readLine()) != null) {
				System.out.println(type + ">" + line);
				// Escribo la linea leida en el archivo
				printWriter.println(line);
			}
			file.close();
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}