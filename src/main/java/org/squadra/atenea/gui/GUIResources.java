package org.squadra.atenea.gui;

import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

/**
 * Hoja de recursos utilizados para la interfaz de usuario.
 * Contiene las rutas relativas de la imagenes, audios, etc.
 * @author Leandro Morrone
 *
 */
public class GUIResources {

	private static final String BASE_PATH = "./src/main/resources/";
	
	public static class Images {

		private static final String IMAGES_PATH = BASE_PATH + "images/";
		
		public static final Image ateneaIcon = 
				Toolkit.getDefaultToolkit().getImage(IMAGES_PATH + "icon.png");
		
		public static class Backgrounds {
			
			private static final String BG_PATH = IMAGES_PATH + "backgrounds/";
			
			public static final ImageIcon main = new ImageIcon(BG_PATH + "main_background.png");
		}
		
		public static class MainButton {
			
			private static final String BUTTON_PATH = IMAGES_PATH + "main_button/";
			
			public static final ImageIcon red = new ImageIcon(BUTTON_PATH + "main_red.png");
			public static final ImageIcon green = new ImageIcon(BUTTON_PATH + "main_green.png");
			public static final ImageIcon orange = new ImageIcon(BUTTON_PATH + "main_orange.png");
			public static final ImageIcon blue = new ImageIcon(BUTTON_PATH + "main_blue.png");
			public static final ImageIcon yellow = new ImageIcon(BUTTON_PATH + "main_yellow.png");
			public static final ImageIcon light_red = new ImageIcon(BUTTON_PATH + "main_light_red.png");
			public static final ImageIcon light_green = new ImageIcon(BUTTON_PATH + "main_light_green.png");
			public static final ImageIcon light_orange = new ImageIcon(BUTTON_PATH + "main_light_orange.png");
			public static final ImageIcon light_blue = new ImageIcon(BUTTON_PATH + "main_light_blue.png");
			public static final ImageIcon light_yellow = new ImageIcon(BUTTON_PATH + "main_light_yellow.png");
			
			public static ImageIcon getByColor(Colors color) {
				switch (color) {
					case RED: return red;
					case GREEN: return green;
					case ORANGE: return orange;
					case BLUE: return blue;
					case YELLOW: return yellow;
					default: return green;
				}
			}
			public static ImageIcon getByLightColor(Colors color) {
				switch (color) {
					case RED: return light_red;
					case GREEN: return light_green;
					case ORANGE: return light_orange;
					case BLUE: return light_blue;
					case YELLOW: return light_yellow;
					default: return light_green;
				}
			}
		}
		
		public static class CloseButton {
			
			private static final String BUTTON_PATH = IMAGES_PATH + "close_button/";
			
			public static final ImageIcon red = new ImageIcon(BUTTON_PATH + "close_red.png");
			public static final ImageIcon green = new ImageIcon(BUTTON_PATH + "close_green.png");
			public static final ImageIcon orange = new ImageIcon(BUTTON_PATH + "close_orange.png");
			public static final ImageIcon blue = new ImageIcon(BUTTON_PATH + "close_blue.png");
			public static final ImageIcon yellow = new ImageIcon(BUTTON_PATH + "close_yellow.png");
			public static final ImageIcon grey = new ImageIcon(BUTTON_PATH + "close_grey.png");
			
			public static ImageIcon getByColor(Colors color) {
				switch (color) {
					case RED: return red;
					case GREEN: return green;
					case ORANGE: return orange;
					case BLUE: return blue;
					case YELLOW: return yellow;
					case GREY: return grey;
					default: return grey;
				}
			}
		}
		
		public static class MinimizeButton {
			
			private static final String BUTTON_PATH = IMAGES_PATH + "minimize_button/";
			
			public static final ImageIcon red = new ImageIcon(BUTTON_PATH + "minimize_red.png");
			public static final ImageIcon green = new ImageIcon(BUTTON_PATH + "minimize_green.png");
			public static final ImageIcon orange = new ImageIcon(BUTTON_PATH + "minimize_orange.png");
			public static final ImageIcon blue = new ImageIcon(BUTTON_PATH + "minimize_blue.png");
			public static final ImageIcon yellow = new ImageIcon(BUTTON_PATH + "minimize_yellow.png");
			public static final ImageIcon grey = new ImageIcon(BUTTON_PATH + "minimize_grey.png");
			
			public static ImageIcon getByColor(Colors color) {
				switch (color) {
					case RED: return red;
					case GREEN: return green;
					case ORANGE: return orange;
					case BLUE: return blue;
					case YELLOW: return yellow;
					case GREY: return grey;
					default: return grey;
				}
			}
		}
		
		public static class SettingButton {
			
			private static final String BUTTON_PATH = IMAGES_PATH + "setting_button/";
			
			public static final ImageIcon red = new ImageIcon(BUTTON_PATH + "setting_red.png");
			public static final ImageIcon green = new ImageIcon(BUTTON_PATH + "setting_green.png");
			public static final ImageIcon orange = new ImageIcon(BUTTON_PATH + "setting_orange.png");
			public static final ImageIcon blue = new ImageIcon(BUTTON_PATH + "setting_blue.png");
			public static final ImageIcon yellow = new ImageIcon(BUTTON_PATH + "setting_yellow.png");
			public static final ImageIcon grey = new ImageIcon(BUTTON_PATH + "setting_grey.png");
			
			public static ImageIcon getByColor(Colors color) {
				switch (color) {
					case RED: return red;
					case GREEN: return green;
					case ORANGE: return orange;
					case BLUE: return blue;
					case YELLOW: return yellow;
					case GREY: return grey;
					default: return grey;
				}
			}
		}
		
		public static class HelpButton {
			
			private static final String BUTTON_PATH = IMAGES_PATH + "help_button/";
			
			public static final ImageIcon red = new ImageIcon(BUTTON_PATH + "help_red.png");
			public static final ImageIcon green = new ImageIcon(BUTTON_PATH + "help_green.png");
			public static final ImageIcon orange = new ImageIcon(BUTTON_PATH + "help_orange.png");
			public static final ImageIcon blue = new ImageIcon(BUTTON_PATH + "help_blue.png");
			public static final ImageIcon yellow = new ImageIcon(BUTTON_PATH + "help_yellow.png");
			public static final ImageIcon grey = new ImageIcon(BUTTON_PATH + "help_grey.png");
			
			public static ImageIcon getByColor(Colors color) {
				switch (color) {
					case RED: return red;
					case GREEN: return green;
					case ORANGE: return orange;
					case BLUE: return blue;
					case YELLOW: return yellow;
					case GREY: return grey;
					default: return grey;
				}
			}
		}
		
		public static class ActionsButton {
			
			private static final String BUTTON_PATH = IMAGES_PATH + "actions_button/";
			
			public static final ImageIcon grey = new ImageIcon(BUTTON_PATH + "actions_grey.png");
			public static final ImageIcon light_grey = new ImageIcon(BUTTON_PATH + "actions_light_grey.png");
		}
		
		public static class RateButton {
					
			private static final String BUTTON_PATH = IMAGES_PATH + "rate_button/";
			
			public static final ImageIcon grey = new ImageIcon(BUTTON_PATH + "rate_grey.png");
			public static final ImageIcon light_grey = new ImageIcon(BUTTON_PATH + "rate_light_grey.png");
		}
		
		public static class HistoryButton {
			
			private static final String BUTTON_PATH = IMAGES_PATH + "history_button/";
			
			public static final ImageIcon grey = new ImageIcon(BUTTON_PATH + "history_grey.png");
			public static final ImageIcon light_grey = new ImageIcon(BUTTON_PATH + "history_light_grey.png");
		}
		
		public static class InputButton {
			
			private static final String BUTTON_PATH = IMAGES_PATH + "input_button/";
			
			public static final ImageIcon grey = new ImageIcon(BUTTON_PATH + "input_grey.png");
			public static final ImageIcon light_grey = new ImageIcon(BUTTON_PATH + "input_light_grey.png");
		}
		
	}
	
	public static enum Colors {
		GREEN, RED, BLUE, ORANGE, YELLOW, GREY
	}
	
}
