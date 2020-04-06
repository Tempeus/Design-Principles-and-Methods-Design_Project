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
  
  private static UltrasonicLocalize ultrasoniclocalize = new UltrasonicLocalize();
  private static Thread UltrasonicLocalizeSensor = new Thread(ultrasoniclocalize);
  private static LightLocalizer lightlocalize = new LightLocalizer();
  private static Thread LightLocalizeSensor = new Thread(lightlocalize);

  /**
   * Main method of our Lab Code, runs the solution.
   * 
   * @param args Default input argument for main method.
   */
  public static void main(String[] args) {
    new Thread(odometer).start();
        
    // TODO: set initial conditions (where the bridge is located and etc)
    
    // robot localizes itself and moves to origin 
    UltrasonicLocalizeSensor.start();
    ultrasoniclocalize.stopThread(UltrasonicLocalizeSensor);
    
    LightLocalizeSensor.start();
    lightlocalize.localize();

    new Thread(new Display()).start();

    
    // TODO: Navigate past the bridge
    
    // TODO: Navigation and obstacle detection and avoidance
    
    // TODO: Rescue Initiatiated
    
    // TODO: Navigate and avoid obstacles back to base
    
    System.exit(0);
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
  
  /**
   * This method is used to identify if the object is an obstacle or the rescue cart
   * 
   * This method will use the goToObject method in the UltrasonicAvoidance class as well as a light sensor thread for the identification process
   */
  public static void identifyObstacle() {
   //Use Ultrasonic sensor to detect the obstacle and making sure it's there
    
    //Use light sensor to detect to see if it is a wall
    //if light sensor detects something, then it is a wall
    //Otherwise it is the object cart
  }
}