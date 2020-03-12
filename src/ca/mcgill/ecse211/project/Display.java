package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.lcd;
import static ca.mcgill.ecse211.project.Resources.odometer;

import java.text.DecimalFormat;

/**
 * This class is used to display the content of the odometer variables
 * such as: X, Y, Theta and Ultrasonic Sensor Distance
 * @author Kevin
 *
 */
public class Display implements Runnable {
  
  // Refresh period of the display
  private static final long DISPLAY_PERIOD = 25;
  
  // Timeout value
  private long timeout = Long.MAX_VALUE;
  
  private double[] position;
  private int mapChoice;

  /**
   * The main entry point
   */
  @Override
  public void run() { 
  
    lcd.clear();
  
    long updateStart;
    long updateEnd;

    long startTime = System.currentTimeMillis();
    do {
      updateStart = System.currentTimeMillis();
      
      // get US sensor reading
      //double reading = UltrasonicLocalizer.Localizer.getUSDistance();
      position = odometer.getXyt();
      mapChoice = Main.map;
      // Print US reading and Odometer theta reading
      DecimalFormat numberFormat = new DecimalFormat("######0.00");
      lcd.drawString("X: " + numberFormat.format(position[0]), 0, 0);
      lcd.drawString("Y: " + numberFormat.format(position[1]), 0, 1);
      lcd.drawString("T: " + numberFormat.format(position[2]), 0, 2);
      
      // shows where we want to go
      lcd.drawString("map: " + numberFormat.format(mapChoice), 0, 3);
      
      // this ensures that the data is updated only once every period
      updateEnd = System.currentTimeMillis();
      if (updateEnd - updateStart < DISPLAY_PERIOD) {
        Main.sleepFor(DISPLAY_PERIOD - (updateEnd - updateStart));
      }
    } while ((updateEnd - startTime) <= timeout);  
  }
  
  /**
   * Shows the text on the LCD, line by line.
   * 
   * @param strings comma-separated list of strings, one per line
   */
  public static void showText(String... strings) {
    lcd.clear();
    for (int i = 0; i < strings.length; i++) {
      lcd.drawString(strings[i], 0, i);
    }
  }
  
}
