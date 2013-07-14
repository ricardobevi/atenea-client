package org.squadra.atenea.macros;

import javax.swing.*;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;

/*
 * Clase que genera la UI 
 * @author lucas
 */
public class MacrosGui {
	private JFrame f = new JFrame("Macros");
	private JPanel pnlNorth = new JPanel();
	private JPanel pnlSouth = new JPanel();
	// private JLabel imagen;
	private JButton beginRecord = new JButton("Begin Record");
	private JButton stopRecord = new JButton("Stop Record");
	private JButton Play = new JButton("Play");
	private JTextField name = new JTextField(10);
	private CheckboxGroup CheckBoxGrp = new CheckboxGroup();
	private Checkbox click = new Checkbox("Click", CheckBoxGrp, true);
	private Checkbox dobleClick = new Checkbox("Doble Click", CheckBoxGrp, true);
	private MouseEventHandler example;
	
	
	public MacrosGui() {

		

		/*
		 * Metodo para el boton de inicio de grabacion
		 */
		beginRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Thread.sleep(500);
					f.setExtendedState(JFrame.ICONIFIED);
					GlobalScreen.registerNativeHook();

					example = new MouseEventHandler(name.getText(), CheckBoxGrp.getSelectedCheckbox().getLabel());

				} catch (NativeHookException ex) {
					System.err.println("There was a problem registering the native hook.");
					System.err.println(ex.getMessage());
					System.exit(1);
				} catch (Exception e1) {
				}
				// Inicio el proceso de captura de clicks
				GlobalScreen.getInstance().addNativeMouseListener(example);
				GlobalScreen.getInstance().addNativeMouseMotionListener(example);
				System.out.println("Inicio de captura");
			}
		});

		/*
		 * Metodo para el boton de detener grabacion
		 */
		stopRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Cierro el archivo donde se guardan los clicks
				example.finish();
				// Termino el proceso de captura de clicks
				GlobalScreen.getInstance().removeNativeMouseListener(example);
				GlobalScreen.getInstance().removeNativeMouseMotionListener(example);
				GlobalScreen.unregisterNativeHook();
				

				System.out.println("Fin de captura");
			}
		});

		/*
		 * Metodo para el boton de Play
		 */
		Play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Thread.sleep(500);
					f.setExtendedState(JFrame.ICONIFIED);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				System.out.println("Reproduciendo...");
				// Saco los espacios introducidos en el textbox
				String names = name.getText().replaceAll("\\ ", "");
				String[] files = null;

				// Separo las acciones con '+'
				// Ej: Word + word_pegar
				if (names.contains("+")) {
					files = names.split("\\+");
				} else {
					files = new String[1];
					files[0] = names;
				}
				new Executer(files).execute();
			}
		});

		// Agrego los botones y el textfield a los paneles
		pnlSouth.add(beginRecord);
		pnlSouth.add(stopRecord);
		pnlSouth.add(Play);
		pnlNorth.add(name);
		pnlNorth.add(click);
		pnlNorth.add(dobleClick);
		// pnlNorth.add(imagen);
		f.getContentPane().setLayout(new BorderLayout());
		// Agrego los paneles a la ventana
		f.getContentPane().add(pnlNorth, BorderLayout.NORTH);
		f.getContentPane().add(pnlSouth, BorderLayout.SOUTH);

	}

	/*
	 * Metodo para iniciar la UI
	 */
	public void launchFrame() {
		f.pack(); // Adjusts panel to components for display
		f.setVisible(true);
	}
}