package org.squadra.atenea.history;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Clase para almacenar el historial de conversacion y acciones.
 * Contiene los metodos para leer y escribir el archivo de historial, asi como
 * agregar items o vaciarlo.
 * @author Leandro Morrone
 *
 */
public class History {

	/** Lista para almacenar todos los items de historial */
	private List<HistoryItem> historyItems = new ArrayList<HistoryItem>();
	
	/** Ruta del archivo de historial */
	private transient String historyFilePath;
	
	/**
	 * Constructor
	 * @param historyFilePath
	 */
	public History(String historyFilePath) {
		this.historyFilePath = historyFilePath;
	}
	
	/**
	 * Agrega un nuevo item al historial y actualiza el archivo.
	 * @param item Item de historial a agregar
	 */
	public void addItem(HistoryItem item) {
		historyItems.add(item);
		updateHistoryFile();
	}
	
	/**
	 * Vacia la lista de historial y escribe el archivo vacio.
	 */
	public void clear() {
		historyItems.clear();
		updateHistoryFile();
	}

	/**
	 * Getter de la lista de items de historial.
	 * @return
	 */
	public List<HistoryItem> getHistoryItems() {
		return historyItems;
	}
	
	/**
	 * Carga el archivo JSON de historial en memoria.
	 * Debe ejecutarse despues del constructor.
	 */
	public void loadHistoryFile() {
		
		Gson gson = new GsonBuilder().create();
		
		try {
			// Valido si existe el archivo, sino lo creo
			File file = new File(historyFilePath);
	        if (!file.exists()) {
	            file.createNewFile();
	        }
	        
	        // Cargo el archivo JSON en el objeto History
	        FileReader fr = new FileReader(historyFilePath);
			BufferedReader br = new BufferedReader(fr);
			History tempHistory = gson.fromJson(br, History.class);
			try {
				this.historyItems = tempHistory.getHistoryItems();
			} catch (NullPointerException e) {
				System.out.println("Archivo vacio.");
			}
			br.close();
			
			System.out.println(this);
	 
		} catch (IOException e) {
			System.out.println("Error al cargar archivo.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Re-rescribe el archivo JSON con los items de historial contenidos
	 * actualmente en la lista.
	 */
	public void updateHistoryFile() {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		// Convierto el objeto History a formato Json
		String json = gson.toJson(this);
		System.out.println(json);
	 
		try {
			// Valido si existe el archivo, sino lo creo
			File file = new File(historyFilePath);
	        if (!file.exists()) {
	            file.createNewFile();
	        }
	        
			// Escribo el archivo JSON
			FileWriter writer = new FileWriter(historyFilePath);
			writer.write(json);
			writer.close();
	 
		} catch (IOException e) {
			System.out.println("Error al escribir archivo.");
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		String str = "History:\n";
		for(HistoryItem item : historyItems) {
			str += item + "\n";
		}
		return str;
	}
}
