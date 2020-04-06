package ca.mcgill.ecse211.project;

/**
 * This class is used to avoid obstacles on the playing field using the Ultrasonic sensor
 * @author Kevin
 *
 */
public class UltrasonicAvoidance {
  
  /**
   * This method will be used to detect the obstacles around the robot
   * and precisely locate their distance and bearing
   * 
   * This method will use the odometer as well as the ultrasonic sensor in order to precisely identify the location of the robot
   */
  public void detectObstacles() {
    //Turn robot around 360 degrees
    
    //detect objects that are within 255 cms and store the detection into an array queue with the respected theta
  }
  
  /**
   * This method is used after detecting all the objects in the area, the robot will travel to a specific object in order to
   * identify if its the rescue cart or an obstacle
   */
  public static void goToObject() {
    //pop from the queue and go to the object's direction
    
  }
  
  /**
   * This method will make the robot navigate around obstacles in in front of the robot.
   * The robot will 
   */
  public void avoidObstacle() {
    
  }
}
