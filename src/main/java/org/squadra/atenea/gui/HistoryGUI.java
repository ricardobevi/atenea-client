package org.squadra.atenea.gui;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


import org.apache.commons.lang.StringEscapeUtils;
//import org.apache.commons.lang3.StringEscapeUtils;
import org.squadra.atenea.history.History;
import org.squadra.atenea.history.HistoryItem;
import org.squadra.atenea.history.HistoryItemComparator;

/**
 * Interfaz web para mostrar el historial de conversacion con Atenea.
 * @author Leandro
 *
 */
public class HistoryGUI {
	
	/**
	 * Constructor.
	 * Crea un archivo HTML listando los items de historial y lo abre en el
	 * navegador web por defecto.
	 */
	public HistoryGUI() {
		
		//TODO: borrar esto porque hay que utilizar el que ya esta cargado en Atenea
		History history = new History(Resources.HistoryElements.jsonPath);
		history.loadHistoryFile();
		
		String html 
		  =	"<html xmlns='http://www.w3.org/1999/xhtml'>"
		  + "<head>"
		  + "	<meta http-equiv='Content-Type' content='text/html; charset=utf-8' />"
		  + "	<title>Atenea - Historial</title>"
		  + "	<link type='text/css' rel='stylesheet' href='./history.css' />"	
		  + "</head>"
		  + "<body onload='window.location.hash=\"foot\"'>"
		  + "	<div id='header'>"
		  + "		<div id='title'>Atenea - Historial de conversaci&oacute;n</div>"
		  + "	</div>"
		  + "	<div id='content'>"
		  + "		<table id='history_table' cellpadding='5' cellspacing='0' border='0' width='100%'>";
		
		// Obtengo y ordeno los items de historial por fecha.
		ArrayList<HistoryItem> historyItems = (ArrayList<HistoryItem>) history.getHistoryItems();
		Collections.sort(historyItems, new HistoryItemComparator());
		
		String datePrev = "";
		// Recorro los items de historial.
		for (HistoryItem item : historyItems) {
			
			// Guardo la fecha en formato corto.
			DateFormat dfDateShort = new SimpleDateFormat("dd/MM/yy");
			String dateShortStr = dfDateShort.format(item.getDate());
			
			// Cuando hay un cambio de fecha imprimo en el HTML una linea con la fecha
			// en formato largo.
			if (!dateShortStr.equals(datePrev)) {
				DateFormat dfDateFull = new SimpleDateFormat("dd - MMMM - yyyy", new Locale("es", "ES"));
				String dateFullStr = (dfDateFull.format(item.getDate())).replace("-", "de");
				html += "<tr><td colspan='6' class='date_full'>" + dateFullStr + "</td></tr>";
				datePrev = dateShortStr;
			}
			
			// Guardo la hora.
			DateFormat dfTime = new SimpleDateFormat("HH:mm:ss");
			String timeStr = dfTime.format(item.getDate());
			
			String image = "";
			String typeStr = "";
			
			// Obtengo el tipo y la imagen correspondiente.
			switch (item.getType()) {
				case HistoryItem.INPUT_TEXT_MESSAGE:
					image = Resources.HistoryElements.Images.input_message;
					typeStr = "Mensaje de texto";
					break;
				case HistoryItem.INPUT_VOICE_MESSAGE:
					image = Resources.HistoryElements.Images.input_message;
					typeStr = "Mensaje de voz";
					break;
				case HistoryItem.OUTPUT_MESSAGE:
					image = Resources.HistoryElements.Images.output_message;
					typeStr = "Respuesta";
					break;
				case HistoryItem.OUTPUT_ACTION:
					image = Resources.HistoryElements.Images.output_action;
					typeStr = "Ejecutar acción";
					break;
				case HistoryItem.OUTPUT_ERROR:
					image = Resources.HistoryElements.Images.output_error;
					typeStr = "Problema";
					break;
				case HistoryItem.INPUT_ACTION:
					image = Resources.HistoryElements.Images.input_action;
					typeStr = "Enseñar acción";
					break;
				default:
					image = Resources.HistoryElements.Images.unknown;
					typeStr = "Desconocido";
					break;
			}
			
			// Imprimo la columna del item de historial en el HTML.
			html 
			 += "<tr class='item'>"
	          + "	<td width='7%'>" + timeStr + "</td>"
	          + "	<td width='10%' class='date_short'>" + dateShortStr + "</td>"
	          + "	<td width='3%'><img src='" + image + "' alt='' /></td>"
	          + "	<td width='17%'>[" + StringEscapeUtils.escapeHtml(typeStr) + "]</td>"
	          + "   <td width='53%'>" + StringEscapeUtils.escapeHtml(item.getMessage()) + "</td>"
	          + "   <td width='10%'>" + item.getUser() + "</td>"
	          + "</tr>";
		}
		
		html 
		 += "		</table>"
		  + "		<div id='foot'>&Uacute;ltimo mensaje</div>"	
		  + "	</body>"
		  + "</html>";
		
		// Creo o re-escribo el archivo HTML.
		try {
			File file = new File(Resources.HistoryElements.htmlPath);
	        if (!file.exists()) {
	            file.createNewFile();
	        }
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(html);
			bw.close();
		} catch (IOException e) {
			System.out.println("Error al escribir archivo HTML.");
			e.printStackTrace();
		}
		// TODO: probar si el archivo no existe o esta vacio.
		// Abro el archivo HTML generado con el navegador por defecto.
		try {
			File historyFile = new File(Resources.HistoryElements.htmlPath);
			Desktop.getDesktop().open(historyFile);
		} catch (IOException e) {
			System.out.println("Error al abrir archivo HTML.");
			e.printStackTrace();
		}
	}
}