package org.squadra.atenea.actions;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

/*
 * Clase que gestiona la captura de clicks en la pantalla
 * @author lucas
 */
public class MouseEventHandler implements NativeMouseInputListener,NativeKeyListener  {

	private ListOfAction actionsRecorded;
	private List<Click> clicks = new ArrayList<Click>();
	private JDialog frame = new JDialog();
	private int X1, Y1, X2, Y2;
	private String clickType;
	private BufferedImage screen;
	private File dir = new File("images");
	private String actionName;
	private boolean controlKeyPressed;

	/*
	 * Constructor que crea el archivo del nuevo icono
	 * 
	 * @param fileName nombre del archivo que se va a usar para guardar la ruta
	 * del icono
	 */
	public MouseEventHandler(String fileName, String clickType) {
		if (!dir.exists())
			dir.mkdir();

		actionsRecorded = ListOfAction.getInstance();
		actionName = fileName;
		this.clickType = clickType;
		X1 = X2 = Y1 = Y2 = -1;
		controlKeyPressed = false;

		Toolkit tk = Toolkit.getDefaultToolkit();  
		int xSize = ((int) tk.getScreenSize().getWidth());  
		int ySize = ((int) tk.getScreenSize().getHeight());  
		frame.setSize(xSize,ySize);  
		frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		frame.setUndecorated(true);  
		//frame.setModal(true);
	}

	/*
	 * Metodo que agrega las acciones nuevas
	 */
	public void finish() {
		actionsRecorded.addAction(actionName,clicks);
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
System.out.println(NativeKeyEvent.getKeyText(e.getKeyCode()));
		if (!controlKeyPressed && NativeKeyEvent.getKeyText(e.getKeyCode()) == "Ctrl")
		{
			controlKeyPressed = true; 
			try {
				screen = new Robot().createScreenCapture(new
						Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			frame.add(new Component() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					g.drawImage(screen, 0, 0, getWidth(), getHeight(), this);
					
					if (X1 != -1 && X2 != -1)
					{
						Graphics2D graph = (Graphics2D) g;
						graph.setColor(Color.red);

						graph.draw(new  Rectangle2D.Float(
								Math.min(X1, X2), Math.min(Y1, Y2),
								Math.abs(X1-X2), Math.abs(Y1-Y2)));
					}
				}
			});
			
//			try {
//				new Robot().keyPress(KeyEvent.VK_ESCAPE);
//				new Robot().keyRelease(KeyEvent.VK_ESCAPE);
//			} catch (AWTException e1) {
//				e1.printStackTrace();
//			}
			
			frame.setVisible(true);	
		}
	}

	/*
	 * Metodo que detecta el click en la pantalla
	 */
	public void nativeMousePressed(NativeMouseEvent e) {

		X1 = e.getX();
		Y1 = e.getY();
		
		System.out.println("primer click "+X1+ "  "+Y1);
	}

	public void nativeMouseClicked(NativeMouseEvent e) {
		X1 = X2 = Y1 = Y2 = -1;
	}


	public void nativeMouseReleased(NativeMouseEvent e) {

		X2 = e.getX();
		Y2 = e.getY();

		String iconName = dir.toString() + File.separatorChar + "icon" + new java.util.Date().getTime() + ".jpg";

		try {
			// guardamos la imagen del icono indicado
			BufferedImage bufferedImage =screen.getSubimage(Math.min(X1, X2), Math.min(Y1, Y2),
					Math.abs(X1-X2), Math.abs(Y1-Y2)) ;
			FileOutputStream out = new FileOutputStream(iconName);
			ImageIO.write(bufferedImage, "jpg", out);
			System.out.println("Grabando imagen");

			clicks.add(new Click(clickType.toString(), iconName));

			X1 = X2 = Y1 = Y2 = -1;

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void nativeMouseMoved(NativeMouseEvent e) {
	}

	public void nativeMouseDragged(NativeMouseEvent e) {
		
		X2 = e.getX();
		Y2 = e.getY();

		frame.repaint();
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		if (NativeKeyEvent.getKeyText(e.getKeyCode()) == "Ctrl")
		{
			controlKeyPressed = false; 
			frame.dispose();
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
	}

}