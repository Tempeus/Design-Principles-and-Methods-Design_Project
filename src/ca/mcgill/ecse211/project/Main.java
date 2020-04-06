package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.odometer;

/**
 * The main driver class for the project.
 */
public class Main {
 
  private static UltrasonicLocalize ultrasoniclocalize = new UltrasonicLocalize();
  private static Thread UltrasonicLocalizeSensor = new Thread(ultrasoniclocalize);
  private static LightLocalizer lightlocalize = new LightLocalizer();
  private static Thread LightLocalizeSensor = new Thread(lightlocalize);

  /**
   * Main method of our project.
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
    Navigation.goPastBridge();
    
    // TODO: Navigation and obstacle detection and avoidance
    UltrasonicAvoidance.detectObstacles();
    
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
}