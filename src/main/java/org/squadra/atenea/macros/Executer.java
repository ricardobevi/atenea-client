package org.squadra.atenea.macros;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_32F;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvMinMaxLoc;
import static com.googlecode.javacv.cpp.opencv_core.cvRectangle;
import static com.googlecode.javacv.cpp.opencv_core.cvReleaseImage;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_TM_SQDIFF;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvMatchTemplate;
import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class Executer {
	String[] actions;
	Robot robot;

	public Executer(String[] actions) {
		try {
			robot = new Robot();
		} catch (AWTException e) {
		}
		this.actions = actions;
	}

	public static void clickIn(int x, int y, String typeOfClick) {
		try {
			Robot myRobot = new Robot();
			myRobot.mouseMove(x, y);
			if (typeOfClick != "Click") {
				myRobot.mousePress(InputEvent.BUTTON1_MASK);
				myRobot.mouseRelease(InputEvent.BUTTON1_MASK);
			}

		} catch (Exception e) {
		}
	}

	/*
	 * Ejecuta las acciones indicadas
	 */
	public void execute() {
		for (String name : actions) {
			File archivo = new File("images" + File.separatorChar + name + ".txt");
			// Leo cada linea del archivo de iconos para hacer click y
			// los ejecuto
			FileReader fr;
			try {
				fr = new FileReader(archivo);
				BufferedReader br = new BufferedReader(fr);
				String clickType;
				while ((clickType = br.readLine()) != null) {
					String icon = br.readLine();
					System.out.println("Searching: " + icon);
					executeIcon(icon, clickType);
				}
			} catch (Exception e) {
			}
		}
	}

	/*
	 * Metodo para hacer click en un icono
	 * 
	 * @param iconFileName path al icono a ejecutar
	 */
	public void executeIcon(String iconFileName, String clickType) throws HeadlessException, IOException {
		try {
			// Busco el icono
			int[] aux = searchIcon(iconFileName);
			// Muevo el mouse a la posicion indicada
			robot.mouseMove(aux[0], aux[1]);
			System.out.print("Executing " + clickType + " in: " + aux[0] + ", " + aux[1] + "\n");

			Thread.currentThread();
			if (clickType.equals("Click")) {
				simpleClick();
				// Espero medio segundo antes de hacer otro click
				Thread.sleep(500);
			} else {
				doubleClick();
				// Espero dos segundos antes de hacer otro click, porque puedo
				// estar abriendo un programa
				Thread.sleep(2000);
			}

		} catch (Exception e1) {
		}
	}

	/*
	 * Hace click donde se encuentra el cursor
	 */
	private void simpleClick() {
		robot.mousePress(InputEvent.BUTTON1_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_MASK);
	}

	/*
	 * Hace doble click donde se encuentra el cursor
	 */
	private void doubleClick() {
		simpleClick();
		simpleClick();
	}

	/*
	 * Metodo que busca el icono en la pantalla
	 * 
	 * @param iconFileName path del icono a buscar
	 * 
	 * @return array con la posicion x, y de la pantalla donde se encuentra el
	 * icono
	 */
	private int[] searchIcon(String iconFileName) throws HeadlessException, AWTException, IOException, InterruptedException {
		String screenshootPath = "images" + File.separatorChar + "screenshot.jpg";
		// Saco un screenshot de la pantalla
		BufferedImage image2 = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		ImageIO.write(image2, "jpg", new File(screenshootPath));

		// Cargo la imagen a buscar y el screenshot a memoria
		IplImage img = cvLoadImage(screenshootPath);
		IplImage template = cvLoadImage(iconFileName);
		IplImage result = cvCreateImage(cvSize(img.width() - template.width() + 1, img.height() - template.height() + 1), IPL_DEPTH_32F, 1);

		// busco
		int method = CV_TM_SQDIFF;
		cvMatchTemplate(img, template, result, method);

		double[] min_val = new double[2];
		double[] max_val = new double[2];

		// Where are located our max and min correlation points
		CvPoint minLoc = new CvPoint();
		CvPoint maxLoc = new CvPoint();

		cvMinMaxLoc(result, min_val, max_val, minLoc, maxLoc, null);

		CvPoint point = new CvPoint();
		point.x(minLoc.x() + template.width());
		point.y(minLoc.y() + template.height());

		cvRectangle(img, minLoc, point, CvScalar.RED, 2, 8, 0);

		CvPoint point2 = new CvPoint();
		point2.x(minLoc.x() + template.width() / 2);
		point2.y(minLoc.y() + template.height() / 2);

		cvRectangle(img, point2, point2, CvScalar.GREEN, 2, 8, 0);

		try {
			ImageIO.write(img.getBufferedImage(), "jpg", new File("images" + File.separatorChar + "result" + ".jpg"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		int[] ret = new int[2];
		ret[0] = (minLoc.x() + template.width() / 2);
		ret[1] = (minLoc.y() + template.height() / 2);

		// Release
		cvReleaseImage(img);
		cvReleaseImage(template);
		cvReleaseImage(result);

		return ret;
	}
}
