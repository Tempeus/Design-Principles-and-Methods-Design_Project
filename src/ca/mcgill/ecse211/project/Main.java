package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.TILE_SIZE;
import static ca.mcgill.ecse211.project.Resources.odometer;

import lejos.hardware.Button;

/**
 * The main driver class for the lab.
 */
public class Main {

  /**
   * The main entry point.
   * 
   * @param args not used
   */
  public static int buttonChoice;
  
  public static int map;

  /**
   * Main method of our Lab Code, runs the solution.
   * 
   * @param args Default input argument for main method.
   */
  public static void main(String[] args) {
    new Thread(odometer).start();
    // we want to move by x and y in x and y directions respectively
    map = 0;
    
    // robot localizes itself and moves to origin 
    localize();
    odometer.setXyt(TILE_SIZE, TILE_SIZE, 0);

    new Thread(new Display()).start();




    while (Button.waitForAnyPress() != Button.ID_ESCAPE) {
      buttonChoice = Button.waitForAnyPress();
      if (buttonChoice == Button.ID_UP) {
        map = 1;
      }
      if (buttonChoice == Button.ID_RIGHT) {
        map = 2;
      }
      if (buttonChoice == Button.ID_DOWN) {
        map = 3;
      }
      if (buttonChoice == Button.ID_LEFT) {
        map = 4;
      }

      if (buttonChoice == Button.ID_ENTER) {
        if (map == 1) {
          Navigation.travelTo(1, 7);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(4, 4);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(7, 7);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(7, 4);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(4, 1);
        }

        if (map == 2) {
          Navigation.travelTo(4, 4);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(1, 7);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(7, 7);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(7, 4);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(4, 1);
        }

        if (map == 3) {
          Navigation.travelTo(4, 1);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(7, 4);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(7, 7);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(1, 7);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(4, 4);
        }

        if (map == 4) {
          Navigation.travelTo(1, 4);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(4, 7);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(4, 1);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(7, 4);
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          Navigation.travelTo(7, 7);
        }
      }
      
    } 
    
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
    
    new Thread(new UltrasonicLocalizer()).start();  
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
