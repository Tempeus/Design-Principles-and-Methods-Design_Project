package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.TILE_SIZE;
import static ca.mcgill.ecse211.project.Resources.odometer;
import static ca.mcgill.ecse211.project.Resources.backColorSensor;
import static ca.mcgill.ecse211.project.Resources.leftColorSensor;
import static ca.mcgill.ecse211.project.Resources.ultraSonicSensor;
import lejos.hardware.Button;

/**
 * The main driver class for the lab.
 */
public class Main {

  /**
   * Variable used to get the red value on the back light sensor
   */
  private static float[] backRgbArr = new float[1];
  
  /**
   * variable used to capture the direct output of the US sensor
   */
  private static float[] us_Data = new float[1];
  
  /**
   * red value variables used to assigned the output of the back light sensor
   */
  public static float backRedVal;
  
  private static UltrasonicLocalize ultrasoniclocalize = new UltrasonicLocalize();
  private static Thread UltrasonicLocalizeSensor = new Thread(ultrasoniclocalize);
  private static LightLocalizer lightlocalize = new LightLocalizer();
  private static Thread LightLocalizeSensor = new Thread(lightlocalize);
  private static Rescue rescue = new Rescue();

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
   * This method is used to identify if the object is an obstacle or the rescue cart.
   * Since the wall is taller than the objective cart, if the light sensor (that was placed just tall enough to not be able to detect an objective cart) is able to detect an object,
   * then the object would be a wall, otherwise it would be the objective cart.
   * This method will use the goToObject method in the UltrasonicAvoidance class as well as a light sensor thread for the identification process
   * @return true   if the object is the objective
   *         false   if the object is not the objective
   */
  public static boolean identifyObstacle() {
    //Use Ultrasonic sensor to detect the obstacle and making sure it's there  
    ultraSonicSensor.getDistanceMode().fetchSample(us_Data, 0);
    double distance = us_Data[0];   
    
    //filter out TODO
    if(distance <= 255) {  
      //Use light sensor to detect to see if it is a wall
      backColorSensor.getRedMode().fetchSample(backRgbArr, 0);
      backRedVal = backRgbArr[0] * 100;
      
      //if light sensor detects something, then it is a wall.
      if(Math.abs(backRedVal - LightLocalizer.initialRedValue) > LightLocalizer.rgbThres) {
        //Avoid obstacle
        UltrasonicAvoidance.avoidObstacle();
        return false;      
      }
      //Otherwise it is the object cart
      else{
        rescue.performRescue();   
        return true;
      }
    }
    else {
      return false;
    }
  }
}