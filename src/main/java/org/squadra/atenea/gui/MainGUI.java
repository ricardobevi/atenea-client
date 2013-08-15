package org.squadra.atenea.gui;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;

/**
 * Interfaz de usuario principal del programa.
 * Esta estructurada con Java Swing.
 * Permite el acceso del usuario a las principales funciones de Atenea.
 * @author Leandro Morrone
 * 
 */
@SuppressWarnings("serial")
public class MainGUI extends JFrame {
	
	/** Objeto que contiene las variables de configuracion y estado del sistema */
	private Atenea atenea = Atenea.getInstance();
	
	//TODO: borrar
	public static void main(String[] args) {
		new MainGUI();
	}
	
	/** Singleton */
	private static MainGUI INSTANCE = null;
	
	/**
	 * Crea una instancia de la interfaz, y si ya existe la devuelve
	 * @return instancia singleton de la interfaz principal
	 * @author Leandro Morrone
	 */
	public static MainGUI createInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MainGUI();
		}
		return INSTANCE;
	}
	
	/**
	 * Devuelve la instancia de la interfaz de usuario
	 * @return instancia singleton de la interfaz principal
	 * @author Leandro Morrone
	 */
	public static MainGUI getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Constructor privado: inicia la interfaz principal
	 * @author Leandro Morrone
	 */
	private MainGUI() {
		initComponents();
	}
	
	// Puntos utilizados para el drag de la interfaz
	private Point startDrag;
	private Point startLoc;
	
	// Color actual de la interfaz
	private GUIResources.Colors guiColor = GUIResources.Colors.GREEN;
	private boolean mainButtonOver = false;
	
	// Indica si el programa esta en modo de entrada de texto o audio.
	private boolean inputTextMode = false;
	
	// Elementos swing de la interfaz de usuario
	private JLabel lblBackground;
	private JLabel lblMainButton;
	private JLabel lblCloseButton;
	private JLabel lblMinimizeButton;
	private JLabel lblSettingButton;
	private JLabel lblHelpButton;
	private JLabel lblActionsButton;
	private JLabel lblRateButton;
	private JLabel lblInputButton;
	private JLabel lblHistoryButton;
	private JTextArea txtInput;
	private JScrollPane cpInput;
	private JTextArea txtOutput;
	private JScrollPane cpOutput;
	
	// Elementos para minimizar la interfaz a la barra de tareas
	private SystemTray tray;
	private PopupMenu trayMenu;
	private TrayIcon trayIcon;
	
	
	/**
	 * Inicializo los componentes de la interfaz de usuario y la muestro en pantalla
	 * @author Leandro Morrone
	 */
	private void initComponents() {
		
		//=================== PROPIEDADES DE LA VENTANA =====================
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setTitle("Atenea - " + Atenea.getInstance().getStateText());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		setSize(547, 184);
		setBackground(new Color(0,0,0,0));
		setIconImage(GUIResources.Images.ateneaIcon);
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);
		setAlwaysOnTop(true); //TODO: leer de archivo config
		
		//========================== BACKGROUND ============================= 
		
		lblBackground = new JLabel();
		lblBackground.setIcon(GUIResources.Images.Backgrounds.main);
		lblBackground.setBounds(0, 0, 
				lblBackground.getIcon().getIconWidth(), 
				lblBackground.getIcon().getIconHeight());
		
		lblBackground.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mousePressed(java.awt.event.MouseEvent evt) {
            	backgroundMousePressed(evt);
            }
		});
		lblBackground.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			@Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {
            	backgroundMouseDragged(evt);
            }
        });
		
		//=================== BOTON CENTRAL PRINCIPAL =======================
		
		lblMainButton = new JLabel();
		lblMainButton.setIcon(GUIResources.Images.MainButton.getByColor(guiColor));
		lblMainButton.setBounds(85, 24, 
				lblMainButton.getIcon().getIconWidth(), 
				lblMainButton.getIcon().getIconHeight());

		lblMainButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
					lblMainButton.setIcon(GUIResources.Images.MainButton.getByLightColor(guiColor));
	            	mainButtonMouseClicked();
				}
            }
			@Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblMainButton.setIcon(GUIResources.Images.MainButton.getByLightColor(guiColor));
				mainButtonOver = true;
            }
			@Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
				lblMainButton.setIcon(GUIResources.Images.MainButton.getByColor(guiColor));
				mainButtonOver = true;
            }
			@Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
					lblMainButton.setIcon(GUIResources.Images.MainButton.getByColor(guiColor));
				}
            }
        });
        
		//====================== BOTON PARA CERRAR ==========================
		
		lblCloseButton = new JLabel();
		lblCloseButton.setIcon(GUIResources.Images.CloseButton.grey);
		lblCloseButton.setToolTipText("Cerrar");
		lblCloseButton.setBounds(503, 33, 
				lblCloseButton.getIcon().getIconWidth(), 
				lblCloseButton.getIcon().getIconHeight());

		lblCloseButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
	            	closeButtonMouseClicked();
				}
            }
			@Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblCloseButton.setIcon(GUIResources.Images.CloseButton.getByColor(guiColor));
            }
			@Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
				lblCloseButton.setIcon(GUIResources.Images.CloseButton.grey);
            }
        });
		
		//===================== BOTON PARA MINIMIZAR ========================
		
		lblMinimizeButton = new JLabel();
		lblMinimizeButton.setIcon(GUIResources.Images.MinimizeButton.grey);
		lblMinimizeButton.setToolTipText("Minimizar");
		lblMinimizeButton.setBounds(503, 64, 
				lblMinimizeButton.getIcon().getIconWidth(), 
				lblMinimizeButton.getIcon().getIconHeight());

		lblMinimizeButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
					minimizeButtonMouseClicked();
				}
            }
			@Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblMinimizeButton.setIcon(GUIResources.Images.MinimizeButton.getByColor(guiColor));
            }
			@Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
				lblMinimizeButton.setIcon(GUIResources.Images.MinimizeButton.grey);
            }
        });
		
		//===================== ICONO BARRA DE TAREAS =======================
		
		if (SystemTray.isSupported()) {
			
			tray = SystemTray.getSystemTray();
			
			// Creo el menu contextual y agrego los items
			trayMenu = new PopupMenu();
			//TODO: agregar acciones a los items
			MenuItem item1 = new MenuItem("Item 1");
			MenuItem item2 = new MenuItem("Item 2");
			MenuItem item3 = new MenuItem("Item 3");
			trayMenu.add(item1);
			trayMenu.add(item2);
			trayMenu.add(item3);
			
			// Creo el icono y le agrego maximizacion haciendo doble click
			trayIcon = new TrayIcon(GUIResources.Images.ateneaIcon, "Atenea", trayMenu);
			trayIcon.setImageAutoSize(true);
			trayIcon.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
	            public void mouseClicked(java.awt.event.MouseEvent evt) {
					if (evt.getClickCount() >= 2) {
						setVisible(true);
						setExtendedState(JFrame.NORMAL);
					}
	            }
			});
			
			// Cuando la aplicacion cambia a estado minimizado, agrego el icono
			// a la barra de tareas, si se maximiza lo remuevo.
			this.addWindowStateListener(new WindowStateListener() {
				public void windowStateChanged(WindowEvent e) {
					if (e.getNewState() == JFrame.ICONIFIED || 
						e.getNewState() == 7) {
						try {
							tray.add(trayIcon);
							setVisible(false);
						} catch (AWTException ex) {
							ex.printStackTrace();
						}
					} 
					else if (e.getNewState() == JFrame.NORMAL || 
						e.getNewState()==JFrame.MAXIMIZED_BOTH) {
						tray.remove(trayIcon);
						setVisible(true);
					}
				}
			});
		} 
		else {
			System.out.println("TrayIcon no soportado.");
		}
		
		
		//==================== BOTON DE CONFIGURACION =======================
		
		lblSettingButton = new JLabel();
		lblSettingButton.setIcon(GUIResources.Images.SettingButton.grey);
		lblSettingButton.setToolTipText("Configuración");
		lblSettingButton.setBounds(503, 95, 
				lblSettingButton.getIcon().getIconWidth(), 
				lblSettingButton.getIcon().getIconHeight());

		lblSettingButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
	            	settingButtonMouseClicked();
				}
            }
			@Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblSettingButton.setIcon(GUIResources.Images.SettingButton.getByColor(guiColor));
            }
			@Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
				lblSettingButton.setIcon(GUIResources.Images.SettingButton.grey);
            }
        });
		
		//======================== BOTON DE AYUDA ===========================
		
		lblHelpButton = new JLabel();
		lblHelpButton.setIcon(GUIResources.Images.HelpButton.grey);
		lblHelpButton.setToolTipText("Ayuda");
		lblHelpButton.setBounds(503, 126, 
				lblHelpButton.getIcon().getIconWidth(), 
				lblHelpButton.getIcon().getIconHeight());

		lblHelpButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
	            	helpButtonMouseClicked();
				}
            }
			@Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblHelpButton.setIcon(GUIResources.Images.HelpButton.getByColor(guiColor));
            }
			@Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
				lblHelpButton.setIcon(GUIResources.Images.HelpButton.grey);
            }
        });
		
		//================== BOTON DE ENSEÑAR ACCIONES ======================
		
		lblActionsButton = new JLabel();
		lblActionsButton.setIcon(GUIResources.Images.ActionsButton.grey);
		lblActionsButton.setToolTipText("Enseñar acción");
		lblActionsButton.setBounds(19, 33, 
				lblActionsButton.getIcon().getIconWidth(), 
				lblActionsButton.getIcon().getIconHeight());

		lblActionsButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
	            	actionsButtonMouseClicked();
				}
            }
			@Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblActionsButton.setIcon(GUIResources.Images.ActionsButton.light_grey);
            }
			@Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
				lblActionsButton.setIcon(GUIResources.Images.ActionsButton.grey);
            }
        });
		
		//================= BOTON DE CALIFICAR RESPUESTA ====================
		
		lblRateButton = new JLabel();
		lblRateButton.setIcon(GUIResources.Images.RateButton.grey);
		lblRateButton.setToolTipText("Calificar respuesta");
		lblRateButton.setBounds(19, 95, 
				lblRateButton.getIcon().getIconWidth(), 
				lblRateButton.getIcon().getIconHeight());

		lblRateButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
	            	rateButtonMouseClicked();
				}
            }
			@Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblRateButton.setIcon(GUIResources.Images.RateButton.light_grey);
            }
			@Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
				lblRateButton.setIcon(GUIResources.Images.RateButton.grey);
            }
        });
		
		//========= BOTON PARA CAMBIAR A ENTRADA POR TECLADO/VOZ ============
		
		lblInputButton = new JLabel();
		lblInputButton.setIcon(GUIResources.Images.InputButton.grey);
		lblInputButton.setToolTipText("Cambiar modo de entrada");
		lblInputButton.setBounds(215, 33, 
				lblInputButton.getIcon().getIconWidth(), 
				lblInputButton.getIcon().getIconHeight());

		lblInputButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
	            	inputButtonMouseClicked();
				}
            }
			@Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblInputButton.setIcon(GUIResources.Images.InputButton.light_grey);
				txtAreaLight(cpInput, true);
            }
			@Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
				lblInputButton.setIcon(GUIResources.Images.InputButton.grey);
				txtAreaLight(cpInput, false);
            }
        });
		
		//=================== BOTON PARA VER HISTORIAL ======================
		
		lblHistoryButton = new JLabel();
		lblHistoryButton.setIcon(GUIResources.Images.HistoryButton.grey);
		lblHistoryButton.setToolTipText("Historial");
		lblHistoryButton.setBounds(215, 95, 
				lblHistoryButton.getIcon().getIconWidth(), 
				lblHistoryButton.getIcon().getIconHeight());

		lblHistoryButton.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (SwingUtilities.isLeftMouseButton(evt)) {
	            	historyButtonMouseClicked();
				}
            }
			@Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblHistoryButton.setIcon(GUIResources.Images.HistoryButton.light_grey);
				txtAreaLight(cpOutput, true);
            }
			@Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
				lblHistoryButton.setIcon(GUIResources.Images.HistoryButton.grey);
				txtAreaLight(cpOutput, false);
            }
        });
		
		//===================== TEXTAREA DE ENTRADA =========================
		
		txtInput = new JTextArea();
		txtInput.setFont(new Font("Arial", Font.PLAIN, 12));
		txtInput.setLineWrap(true);
		txtInput.setWrapStyleWord(true);
		txtInput.setMargin(new Insets(3, 5, 3, 5));
		txtInput.setBackground(new Color(240, 240, 240));
		txtInput.setEditable(false);
		cpInput = new JScrollPane();
		cpInput.setBorder(new LineBorder(new Color(175, 175, 175), 4));
		cpInput.setViewportView(txtInput);
		cpInput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		cpInput.setBounds(282, 33, 212, 56);
		txtInput.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblInputButton.setIcon(GUIResources.Images.InputButton.light_grey);
				txtAreaLight(cpInput, true);
            }
			@Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
				lblInputButton.setIcon(GUIResources.Images.InputButton.grey);
				txtAreaLight(cpInput, false);
            }
        });
		
		//====================== TEXTAREA DE SALIDA =========================
		
		txtOutput = new JTextArea();
		txtOutput.setFont(new Font("Arial", Font.PLAIN, 12));
		txtOutput.setLineWrap(true);
		txtOutput.setWrapStyleWord(true);
		txtOutput.setMargin(new Insets(3, 5, 3, 5));
		txtOutput.setBackground(new Color(240, 240, 240));
		txtOutput.setEditable(false);
		cpOutput = new JScrollPane();
		cpOutput.setBorder(new LineBorder(new Color(175, 175, 175), 4));
		cpOutput.setViewportView(txtOutput);
		cpOutput.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		cpOutput.setBounds(282, 95, 212, 56);
		txtOutput.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
				lblHistoryButton.setIcon(GUIResources.Images.HistoryButton.light_grey);
				txtAreaLight(cpOutput, true);
            }
			@Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
				lblHistoryButton.setIcon(GUIResources.Images.HistoryButton.grey);
				txtAreaLight(cpOutput, false);
            }
        });

		
		//===================================================================
		// Agrego todos los elementos a la interfaz en diferentes capas
		
		JLayeredPane layeredPane = getLayeredPane();
		layeredPane.add(lblBackground, new Integer(1));   //el Integer es el z-index
		
		layeredPane.add(lblMainButton, new Integer(2));
		layeredPane.add(lblCloseButton, new Integer(2));
		layeredPane.add(lblMinimizeButton, new Integer(2));
		layeredPane.add(lblSettingButton, new Integer(2));
		layeredPane.add(lblHelpButton, new Integer(2));
		
		layeredPane.add(cpInput, new Integer(3));
		layeredPane.add(cpOutput, new Integer(3));
		
		layeredPane.add(lblActionsButton, new Integer(4));
		layeredPane.add(lblRateButton, new Integer(4));
		layeredPane.add(lblInputButton, new Integer(4));
		layeredPane.add(lblHistoryButton, new Integer(4));
	}
	
	
	/**
	 * Se ejecuta presionando sobre el boton principal.
	 * 
	 * 
	 */
	protected void mainButtonMouseClicked() {
		if (atenea.getState() == AteneaState.WAITING) {
			atenea.getMicrophone().startRecording();
		}
		else if (atenea.getState() == AteneaState.RECORDING) {
			atenea.getMicrophone().stopRecordingAndRecognize();
		}
	}
	
	/**
	 * Se ejecuta presionando sobre el boton de cerrar.
	 * Cierra el programa.
	 */
	protected void closeButtonMouseClicked() {
		dispose();
	}
	
	/**
	 * Se ejecuta presionando sobre el boton de minimizar.
	 * Minimiza la aplicacion, creando un icono en la barra de tareas.
	 */
	protected void minimizeButtonMouseClicked() {
		setExtendedState(JFrame.ICONIFIED);
	}
	
	/**
	 * Se ejecuta presionando sobre el boton de configuracion.
	 * Abre la pantalla de configuracion.
	 */
	protected void settingButtonMouseClicked() {
		// TODO Auto-generated method stub
	}

	/**
	 * Se ejecuta presionando sobre el boton de ayuda.
	 * Abre la seccion de ayuda del sitio web.
	 */
	protected void helpButtonMouseClicked() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Se ejecuta presionando sobre el boton de nueva accion.
	 * Abre la pantalla de grabacion de macros.
	 */
	protected void actionsButtonMouseClicked() {
		ActionsGUI win = new ActionsGUI();
		win.launchFrame();
		Atenea.getInstance().setState(AteneaState.LEARNING);
	}
	
	/**
	 * Se ejecuta presionando sobre el boton de calificar respuesta.
	 * Despliega las opciones para calificar la respuesta.
	 */
	protected void rateButtonMouseClicked() {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Se ejecuta presionando sobre el boton de entrada.
	 * Cambia el modo de entrada de voz a teclado y viceversa
	 */
	protected void inputButtonMouseClicked() {
		if (inputTextMode) {
			inputTextMode = false;
			txtInput.setBackground(new Color(240, 240, 240));
			txtInput.setEditable(false);
		} else {
			inputTextMode = true;
			txtInput.setBackground(Color.WHITE);
			txtInput.setEditable(true);
		}
	}
	
	/**
	 * Se ejecuta presionando sobre el boton de historial.
	 * Abre la pantalla de historial en el navegador web por defecto.
	 */
	protected void historyButtonMouseClicked() {
		// TODO Auto-generated method stub
	}

	
	/**
	 * Se ejecuta cuando se hace click sobre el fondo de la interfaz.
	 * Guarda la posicion donde se hizo click.
	 * @param evt Evento capturado del mouse.
	 */
	protected void backgroundMousePressed(MouseEvent evt) {
		this.startDrag = this.getScreenLocation(evt);
        this.startLoc = this.getLocation();
	}
	
	/**
	 * Se ejecuta cuando se hace un arrastre de la interfaz.
	 * Mueve la interfaz a medida que se arrastra el mouse.
	 * @param evt Evento capturado del mouse.
	 */
	protected void backgroundMouseDragged(MouseEvent evt) {
		Point current = this.getScreenLocation(evt);
	    Point offset = new Point((int) current.getX() - (int) startDrag.getX(),
	        (int) current.getY() - (int) startDrag.getY());

	    Point new_location = new Point(
	        (int) (this.startLoc.getX() + offset.getX()), (int) (this.startLoc
	            .getY() + offset.getY()));
	        this.setLocation(new_location);
	}
	
	private Point getScreenLocation(MouseEvent e) {
        Point cursor = e.getPoint();
        Point target_location = this.getLocationOnScreen();
        return new Point((int) (target_location.getX() + cursor.getX()),
            (int) (target_location.getY() + cursor.getY()));
    }
	
	public void setTxtInput(String txt) {
		txtInput.setText(txt);
	}
	
	public String getTxtInput() {
		return txtInput.getText();
	}
	
	public void setTxtOutput(String txt) {
		txtOutput.setText(txt);
	}
	
	public String getTxtOutput() {
		return txtOutput.getText();
	}
	
	/**
	 * Cambia el color actual de la GUI segun el estado en que se encuentra
	 * y cambia el titulo de la aplicacion.
	 * @param state Estado de Atenea
	 */
	public void changeGUIByState(int state) {
		switch (state) {
			case AteneaState.WAITING: 
				guiColor = GUIResources.Colors.GREEN;
				break;
			case AteneaState.RECORDING: 
				guiColor = GUIResources.Colors.RED;
				break;
			case AteneaState.PROCESSING: 
				guiColor = GUIResources.Colors.YELLOW;
				break;
			case AteneaState.PLAYING: 
				guiColor = GUIResources.Colors.ORANGE;
				break;
			case AteneaState.LEARNING: 
				guiColor = GUIResources.Colors.BLUE;
				break;
			default:
				guiColor = GUIResources.Colors.YELLOW;
		}
		if (mainButtonOver) {
			lblMainButton.setIcon(GUIResources.Images.MainButton.getByLightColor(guiColor));
			//TODO: cambiar los colores del trayIcon
		}
		else {
			lblMainButton.setIcon(GUIResources.Images.MainButton.getByColor(guiColor));
		}
		setTitle("Atenea - " + Atenea.getInstance().getStateText());
	}
	
	
	private void txtAreaLight(JScrollPane area, boolean light) {
		if (light) {
			area.setBorder(new LineBorder(new Color(190, 190, 190), 4));
		}
		else {
			area.setBorder(new LineBorder(new Color(175, 175, 175), 4));
		}
	}
}
