package org.squadra.atenea.gui;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.squadra.atenea.actions.Executer;
import org.squadra.atenea.actions.ListOfAction;
import org.squadra.atenea.actions.MouseEventHandler;


/*
 * Clase que genera la UI 
 * @author lucas
 */
public class ActionsGUI {
	private JFrame f = new JFrame("Macros");
	private JPanel pnlNorth = new JPanel();
	private JPanel pnlSouth = new JPanel();
	private JButton beginRecord = new JButton("Begin Record");
	private JButton stopRecord = new JButton("Stop Record");
	private JButton Play = new JButton("Play");
	private JTextField name = new JTextField(10);
	private CheckboxGroup CheckBoxGrp = new CheckboxGroup();
	private Checkbox click = new Checkbox("Click", CheckBoxGrp, true);
	private Checkbox dobleClick = new Checkbox("Doble Click", CheckBoxGrp, true);
	private MouseEventHandler mouseHandler;
	private Executer executer = new Executer();


	public ActionsGUI() {

		/*
		 * Metodo para el boton de inicio de grabacion
		 */
		beginRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Thread.sleep(500);
					f.setExtendedState(JFrame.ICONIFIED);

					GlobalScreen.registerNativeHook();

					mouseHandler = new MouseEventHandler(name.getText(), CheckBoxGrp.getSelectedCheckbox().getLabel());

				} catch (NativeHookException ex) {
					System.err.println("There was a problem registering the native hook.");
					System.err.println(ex.getMessage());
					System.exit(1);
				} catch (Exception e1) {
				}
				// Inicio el proceso de captura de clicks
				GlobalScreen.getInstance().addNativeMouseListener(mouseHandler);
				GlobalScreen.getInstance().addNativeMouseMotionListener(mouseHandler);
				 GlobalScreen.getInstance().addNativeKeyListener(mouseHandler);
				System.out.println("Inicio de captura");
			}
		});

		/*
		 * Metodo para el boton de detener grabacion
		 */
		stopRecord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				mouseHandler.finish();

				// Termino el proceso de captura de clicks
				GlobalScreen.getInstance().removeNativeMouseListener(mouseHandler);
				GlobalScreen.getInstance().removeNativeMouseMotionListener(mouseHandler);
				GlobalScreen.getInstance().removeNativeKeyListener(mouseHandler);
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
				executer.execute(files);
			}
		});

		/**
		 *  Evento que cierra la ventana de macros
		 */
		f.addWindowListener( new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				//Escribo las acciones al archivo
				ListOfAction.getInstance().writeToFile();
				f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			//	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

	}


	/*
	 * Metodo para iniciar la UI
	 */
	public void launchFrame() {
		f.pack(); // Adjusts panel to components for display
		f.setVisible(true);
	}
}