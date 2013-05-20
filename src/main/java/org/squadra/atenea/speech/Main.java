/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.squadra.atenea.speech;

import javax.swing.UIManager;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.squadra.atenea.speech.gui.MainGUI;

/**
 * Es el main del cliente, desde aqui se inicia la interfaz grafica
 * @author User
 */
public class Main {

    public static void main(String args[]) {
        
    	//inicia el contexto para solicitar objetos configurados por spring
    	ClassPathXmlApplicationContext context 
        = new ClassPathXmlApplicationContext(new String[] {"client-beans.xml"});

    	try {
    		
    		//configura el look de la interfaz
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            //solicita MainGui al contexto y lo configura e inicia
       	 	MainGUI gui  = (MainGUI)context.getBean("mainguibean");
            gui.setVisible(true);
            gui.setLocationRelativeTo(null);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
