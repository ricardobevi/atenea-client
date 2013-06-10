package org.squadra.atenea.gui;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

import org.squadra.atenea.Atenea;
import org.squadra.atenea.AteneaState;
import org.squadra.atenea.stt.MicrophoneStateThread;
import org.squadra.atenea.stt.RecognizeThread;

@SuppressWarnings("serial")
public class MainGUI extends JFrame {
	
	private Atenea atenea;
	
	private static MainGUI INSTANCE = null;
	
	// Componentes de la interfaz de usuario
	private JTextField txtEstado;
	private JTextField txtTiempoDeRespuesta;
	private JTextField txtMetaData;
	private JLabel lblEstado;
	private JLabel lblEntradaAudio;
	private JTextArea txtEntradaAudio;
	private JLabel lblEntradaTexto;
	private JTextArea txtEntradaTexto;
	private JLabel lblSalida;
	private JTextArea txtSalida;
	private JLabel lblTiempoDeRespuesta;
	private JLabel lblMetaData;
	private JButton btnValorarBien;
	private JButton btnValorarMal;
	private JButton btnEnviar;
	private JButton btnGrabarDetener;
	private JButton btnNuevoTermino;
	private JButton btnNuevaAccion;
	private JButton btnValorarRegular;

	public static MainGUI createInstance(Atenea atenea) {
		if (INSTANCE == null) {
			INSTANCE = new MainGUI(atenea);
		}
		return INSTANCE;
	}
	
	public static MainGUI getInstance() {
		return INSTANCE;
	}
	
	private MainGUI(Atenea atenea) {
		this.atenea = atenea;
		initComponents();
		new Thread(new MicrophoneStateThread()).start();
	}
	
	private void initComponents() {
		
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.setSize(new Dimension(486, 428));
		this.setTitle("Atenea (Interfaz de prueba)");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		lblEntradaAudio = new JLabel("Entrada de audio:");
		
		txtEntradaAudio = new JTextArea();
		txtEntradaAudio.setBackground(UIManager.getColor("TextField.disabledBackground"));
		txtEntradaAudio.setBorder(UIManager.getBorder("TextField.border"));
		txtEntradaAudio.setEditable(false);
		txtEntradaAudio.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		btnGrabarDetener = new JButton("Grabar / Detener");
		btnGrabarDetener.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (atenea.getState() == AteneaState.WAITING) {
					try {
						atenea.setState(AteneaState.RECORDING);
						setTxtEstadoDelSistema(atenea.getStateText());
						atenea.getMicrophone().captureAudioToFile(atenea.getWaveFilePath());
					} catch (Exception e1) {
						System.out.println("Error al grabar archivo de audio");
					}
				}
				else if (atenea.getState() == AteneaState.RECORDING) {
					atenea.setState(AteneaState.PROCESSING);
					setTxtEstadoDelSistema(atenea.getStateText());
					atenea.getMicrophone().close();
					new Thread(new RecognizeThread(atenea)).start();
				}
			}
		});
		
		lblEstado = new JLabel("Estado del sistema:");
		
		txtEstado = new JTextField(atenea.getStateText());
		txtEstado.setEditable(false);
		txtEstado.setColumns(10);
		
		lblEntradaTexto = new JLabel("Entrada de texto:");
		
		txtEntradaTexto = new JTextArea();
		txtEntradaTexto.setBorder(UIManager.getBorder("TextField.border"));
		txtEntradaTexto.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		btnEnviar = new JButton("Enviar");
		
		lblSalida = new JLabel("Salida:");
		
		txtSalida = new JTextArea();
		txtSalida.setBackground(UIManager.getColor("TextField.disabledBackground"));
		txtSalida.setBorder(UIManager.getBorder("TextField.border"));
		txtSalida.setEditable(false);
		txtSalida.setFont(new Font("Tahoma", Font.PLAIN, 11));
		
		btnValorarBien = new JButton("Ok");
		
		btnValorarMal = new JButton("Mal");
		
		lblTiempoDeRespuesta = new JLabel("Tiempo de respuesta:");
		
		txtTiempoDeRespuesta = new JTextField();
		txtTiempoDeRespuesta.setEditable(false);
		txtTiempoDeRespuesta.setColumns(10);
		
		lblMetaData = new JLabel("Meta-data:");
		
		txtMetaData = new JTextField();
		txtMetaData.setEditable(false);
		txtMetaData.setColumns(10);
		
		btnNuevoTermino = new JButton("Nuevo t\u00E9rmino");
		
		btnNuevaAccion = new JButton("Nueva acci\u00F3n");
		
		btnValorarRegular = new JButton("Reg");
		
		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(txtEstado, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
								.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
									.addComponent(txtEntradaAudio, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnGrabarDetener))
								.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
									.addComponent(txtEntradaTexto, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnEnviar, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(txtSalida, GroupLayout.PREFERRED_SIZE, 289, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnValorarBien)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnValorarRegular)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnValorarMal))
								.addGroup(groupLayout.createSequentialGroup()
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblTiempoDeRespuesta)
										.addComponent(txtTiempoDeRespuesta, 104, 104, 104))
									.addGap(27)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(lblMetaData)
										.addComponent(txtMetaData, GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE))
									.addGap(4)))
							.addGap(8))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnNuevoTermino)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNuevaAccion)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblEstado)
							.addContainerGap(364, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblEntradaAudio)
							.addContainerGap(371, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblEntradaTexto)
							.addContainerGap(371, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblSalida)
							.addContainerGap(425, Short.MAX_VALUE))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblEstado)
					.addGap(5)
					.addComponent(txtEstado, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblEntradaAudio)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnGrabarDetener, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtEntradaAudio, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblEntradaTexto)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnEnviar, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtEntradaTexto, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblSalida)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnValorarMal, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnValorarBien, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnValorarRegular, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
						.addComponent(txtSalida, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTiempoDeRespuesta)
						.addComponent(lblMetaData))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtTiempoDeRespuesta, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtMetaData, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNuevaAccion, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
						.addComponent(btnNuevoTermino, GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE))
					.addContainerGap())
		);
		this.getContentPane().setLayout(groupLayout);
		this.setVisible(true);
	}

	public void setTxtEstadoDelSistema(String text) {
		txtEstado.setText(text + "...");
	}
	
	public void setTxtTiempoDeRespuesta(String text) {
		txtTiempoDeRespuesta.setText(text);
	}
	
	public void setTxtMetaData(String text) {
		txtMetaData.setText(text);
	}
	
	public void setTxtEntradaAudio(String text) {
		txtEntradaAudio.setText(text);
	}
	
	public void setTxtSalida(String text) {
		txtSalida.setText(text);
	}
	
	public String getTxtSalida() {
		return txtSalida.getText();
	}

}