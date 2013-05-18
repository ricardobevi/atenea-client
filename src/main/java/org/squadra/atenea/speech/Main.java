/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.squadra.atenea.speech;

import javax.swing.UIManager;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.squadra.atenea.speech.gui.MainGUI;

/**
 *
 * @author User
 */
public class Main {

    public static void main(String args[]) {
        
    	ClassPathXmlApplicationContext context 
        = new ClassPathXmlApplicationContext(new String[] {"client-beans.xml"});

    	try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
       	 	MainGUI gui  = (MainGUI)context.getBean("mainguibean");
            
            gui.setVisible(true);
            gui.setLocationRelativeTo(null);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
