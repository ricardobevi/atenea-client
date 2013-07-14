package org.squadra.atenea.macros;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;

import javax.imageio.ImageIO;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

/*
 * Clase que gestiona la captura de clicks en la pantalla
 * @author lucas
 */
public class MouseEventHandler implements NativeMouseInputListener {

	private FileWriter fichero;
	private PrintWriter pw;
	private int clicks;
	private int X1, Y1, X2, Y2;
	private String clickType;
	private boolean completed;
	private File dir = new File("images");

	/*
	 * Constructor que crea el archivo del nuevo icono
	 * 
	 * @param fileName nombre del archivo que se va a usar para guardar la ruta
	 * del icono
	 */
	public MouseEventHandler(String fileName, String clickType) throws IOException {
		if (!dir.exists())
			dir.mkdir();

		fichero = new FileWriter(dir.toString() + File.separatorChar + fileName + ".txt", true);
		this.clickType = clickType;
		clicks = 0;
		completed = false;
	}

	/*
	 * Metodo que cierra el archivo creado
	 */
	public void finish() {
		try {
			fichero.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Metodo que detecta el click en la pantalla
	 */
	public void nativeMouseClicked(NativeMouseEvent e) {

		clicks++;
		// Si es el primer click que hago, registro la posicion
		if (clicks == 1) {
			X1 = e.getX();
			Y1 = e.getY();
			System.out.println("Primer click");
		}
		// Si es el segundo click que hago, puedo capturar el icono
		else if (clicks == 2) {
			X2 = e.getX();
			Y2 = e.getY();
			completed = true;
			System.out.println("Segundo click");
		}
	}

	public void nativeMousePressed(NativeMouseEvent e) {
	}

	public void nativeMouseReleased(NativeMouseEvent e) {
	}

	public void nativeMouseMoved(NativeMouseEvent e) {
		if (Math.abs(X2 - e.getX()) > 150 || Math.abs(Y2 - e.getY()) > 150) {
			if (completed) {
				completed = false;
				// obtenemos el tama�o del rectangulo
				Rectangle rectangleTam = new Rectangle(X1, Y1, X2 - X1, Y2 - Y1);
				try {
					String iconName = dir.toString() + File.separatorChar + "icon" + new java.util.Date().getTime() + ".jpg";
					Robot robot = new Robot();
					// guardamos la imagen del icono indicado
					BufferedImage bufferedImage = robot.createScreenCapture(rectangleTam);
					FileOutputStream out = new FileOutputStream(iconName);
					ImageIO.write(bufferedImage, "jpg", out);
					System.out.println("Grabando imagen");
					// Escibo el tipo de click a realizar sobre el icono y la
					// ruta
					// del icono, en el archivo de la accion
					pw = new PrintWriter(fichero);
					pw.println(clickType.toString());
					pw.println(iconName.toString());
					pw.close();
					System.out.println("Grabando archivo");
					// tomamos una captura de pantalla
					// BufferedImage image = new Robot().createScreenCapture(new
					// Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
					// ImageIO.write(image, "jpg", new File("images" +
					// File.separatorChar + "screenshot.jpg"));

				} catch (Exception ex) {
				}
			}
		}
	}

	public void nativeMouseDragged(NativeMouseEvent e) {
	}

}