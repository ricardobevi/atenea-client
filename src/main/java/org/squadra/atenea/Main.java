/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.squadra.atenea;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Esta clase contiene solo el main del programa
 * @author Leandro Morrone
 */
public class Main {

	/**
	 * El main se encarga de instanciar el objeto Atenea utilizando spring
	 * y lanzar la interfaz principal del programa.
	 * @param args
	 * @author Leandro Morrone
	 */
    public static void main(String args[]) {

    	// Inicia el contexto para solicitar objetos configurados por spring
    	ClassPathXmlApplicationContext context = 
    			new ClassPathXmlApplicationContext(new String[] {"client-beans.xml"});
    	
    	// Instancio el programa e inyecto el cliente desde spring
		Atenea atenea = (Atenea)context.getBean("ateneabean");
		// Lanzo la interfaz principal
    	atenea.launchMainGUI();
    }
}
