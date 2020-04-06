package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.backColorSensor;
import static ca.mcgill.ecse211.project.Resources.ultraSonicSensor;

/**
 * This class is used to avoid obstacles on the playing field using the Ultrasonic sensor
 * @author Kevin
 *
 */
public class UltrasonicAvoidance {
  
  /**
   * A matrix that is used to store all the information on objects around the robot
   * [0] will be used to store the distance of the object
   * [1] will be used to store the bearing of the object
   */
  private static double[][] object_data;
  
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
  
  /**
   * Boolean that is used to indicate if the objective cart was found and captured
   */
  public static boolean cartFound = false;
  
  /**
   * This method will be used to detect the obstacles around the robot and precisely locate their distance and bearing.
   * This method will also use the odometer as well as the ultrasonic sensor in order to precisely identify the location of the robot.
   * 
   * The robot will rotate 360 degrees until the ultrasonic sensor detects an object. 
   * The robot will stop (sleep the motors), indicating that it detected an object within 255 cms. It will store the distance as well as the bearing into a list.
   * Afterwards, the robot will continue rotating until it detects another object and store the information into the list.
   * After the rotation phase, the robot will go through the list and pick the object that is the closest and it will go to the object.
   * 
   * In order to prevent the robot identifying the same object, we will set a limit on the distance between the object and the robot. 
   * For instance, if the object is already close to the robot when it is detecting objects, the robot will ignore it and assume that it was already identified.
   */
  public static void detectObstacles() {
    while(cartFound == false) {
      //Turn robot around 360 degrees
      
      //detect objects that are within 15cm - 255 cm 
      
      //store the detection into an array queue with the respected theta
      
      //goto the object
      goToObject();
      
      //identify if the object is a wall or cart
      if(identifyObstacle() == true) {
        //If the object is a cart, rescue it
        Rescue.performRescue();
        cartFound = true;
      }
      
      else {
        //if it is a wall, avoid it
        avoidObstacle();
      }
    }
    
    //Otherwise, go back to base
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
        return false;      
      }
      //Otherwise it is the object cart
      else{
        return true;
      }
    }
    else {
      return false;
    }
  }
  
  /**
   * This method is used after detecting all the objects in the area.
   * The robot will choose the object that it is closest to in the list.
   * 
   * The robot will then navigate to the object.
   * When the robot reaches to the obstacle, it will call the identifyObstacle() method in order to determine if the object is the objective cart or a wall.
   */
  public static void goToObject() {
    //Use Navigation method to go to object
  }
  
  /**
   * This method will make the robot navigate around obstacles in in front of the robot when it is confirmed not to be the objective cart.
   * The robot will turn 
   */
  public static void avoidObstacle() {
    //Use navigation method to avoid obstacle
    
  }
}
