package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.TILE_SIZE;
import static ca.mcgill.ecse211.project.Resources.odometer;

import lejos.hardware.Button;

/**
 * The main driver class for the lab.
 */
public class Main {


  public static int buttonChoice;
  
  public static int map;

  /**
   * The main entry point.
   * 
   * @param args 
   */
  public static void main(String[] args) {
    new Thread(odometer).start();
    // we want to move by x and y in x and y directions respectively
    map = 0;
    
    // robot localizes itself and moves to origin 
    localize();
    odometer.setXyt(TILE_SIZE, TILE_SIZE, 0);

    new Thread(new Display()).start();
    
    //TODO: Implement Rescue
    
    System.exit(0);
  }
  
  /**
   * This method waits for the user to press enter.
   * As soon as enter is pressed, the robot localizes itself
   * and moves to the origin.
   */
  public static void localize() {
    int buttonChoice;
    Display.showText("Press enter");
    do {
      buttonChoice = Button.waitForAnyPress(); // left or right press
    } while (buttonChoice != Button.ID_ENTER);
    
    //TODO: Start localization
  }
  
  /**
   * Sleeps this thread for a given duration.
   * 
   * @param duration Time in milliseconds to sleep for.
   */
  public static void sleepFor(long duration) {
    try {
      Thread.sleep(duration);
    } catch (InterruptedException e) {
      // There is nothing to be done here
    }
  }
  
}
