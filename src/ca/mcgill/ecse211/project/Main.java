package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.odometer;

/**
 * The main driver class for the project.
 * This class is essentially the brain of the robot as well as managing all the peripheral threads.
 * 
 * This class will first run the odometer thread as well as the display thread and localize the robot.
 * Afterwards, it will set all the initial variables, such as the location of the tunnels by using {@code SetCoordinates}.
 * The robot will then proceed towards the tunnel and cross it by using {@code goPastBridge()}.
 * After successfully going pass the tunnel, it will initiate its searching and navigation phase.
 * 
 * In this phase, the robot will detect all surrounding objects around the robot using {@code UltrasonicAvoidance.detectObstacles()} and will navigate towards the nearest object.
 * After reaching the object, it will analyze if the object is a wall or the objective cart.
 * Once the robot detects the objective cart, it will be in the rescue phase.
 * 
 * In this phase, the robot will rotate itself 180 degrees and capture the cart with its claws.
 * Afterwards, the robot will lock its claws, preventing the cart from escaping. 
 * The robot will then navigate itself back to base while avoiding all obstacles.
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
    
    // robot localizes itself and moves to origin 
    UltrasonicLocalizeSensor.start();
    ultrasoniclocalize.stopThread(UltrasonicLocalizeSensor);
    
    LightLocalizeSensor.start();
    lightlocalize.localize();

    new Thread(new Display()).start();
    
    // TODO: Set the parameters of the field
    SetCoordinates();

    // TODO: Navigate past the bridge
    Navigation.goPastBridge();
    
    // TODO: Navigation and obstacle detection and avoidance
    UltrasonicAvoidance.detectObstacles(); 
    
    System.exit(0);
  }
  
  /**
   * This method use IsTeamRed in WifiSetup class to determine the team of the robot and set the coordinates of 
   * the bridge, search zone and starting point accordingly.
   */
  public static void SetCoordinates (){
    
    if (WifiSetup.IsTeamRed()) {
      //Set field parameters for red team. Including the coordinates of the bridge, search zone and starting point etc.
    }
    
    else {
      //Set field parameters for green team. Including the coordinates of the bridge, search zone and starting point etc.
    }    
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