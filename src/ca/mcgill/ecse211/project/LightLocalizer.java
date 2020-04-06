package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.BASE_WIDTH;
import static ca.mcgill.ecse211.project.Resources.TILE_SIZE;
import static ca.mcgill.ecse211.project.Resources.WHEEL_RAD;
import static ca.mcgill.ecse211.project.Resources.leftMotor;
import static ca.mcgill.ecse211.project.Resources.leftColorSensor;
import static ca.mcgill.ecse211.project.Resources.rightColorSensor;
import static ca.mcgill.ecse211.project.Resources.odometer;
import static ca.mcgill.ecse211.project.Resources.ROTATE_SPEED;
import static ca.mcgill.ecse211.project.Resources.ACCELERATION;
import static ca.mcgill.ecse211.project.Resources.rightMotor;
import static ca.mcgill.ecse211.project.Resources.LIGHTSENSOR_OFFSET;
import static ca.mcgill.ecse211.project.Resources.FORWARD_SPEED;

import lejos.hardware.Sound;

/**
 * The class will help the robot to localize on the playing field using two light sensors attached on both sides of the robot.
 * 
 * Additionally, the class also has methods that is responsible in calibrating the robot's orientation, making sure that it is always moving in a straight line.
 * This calibration occurs when one of the color sensors of the robot detects a line. When this occurs, the robot will make sure that the other color sensor also detects a line.
 * If it does not detect one, the robot will run the associated motor, rotating it until the color sensor detects the line.
 * Thus, the robot will be straight.
 * With this feature, it will allow us to navigate the robot back to the base with the rescue cart without the need of localizing every so often. 
 * 
 * @author Kevin
 *
 */
public class LightLocalizer implements Runnable {
  
  /**
   * Array used to store the angles of the black lines
   */
  private static double[] angles = {0, 0, 0, 0};
  
  /**
   * Array used to store angle differences.
   */
  private static double[] anglesdiff = {0, 0, 0, 0};
  
  /**
   * To detect how many points it detected
   */
  private static int angleIndex = 0;
  
  /**
   * Initial red value of board
   */
  public static double initialRedValue = 0;
  
  /**
   * Variable used to get the red value on the left light sensor
   */
  private static float[] leftRgbArr = new float[1];
  
  /**
   * Variable used to get the red value on the right light sensor
   */
  private static float[] rightRgbArr = new float[1];
  
  /**
   * Threshold to detect lines
   */
  public static int rgbThres = 11;
  
  /**
   * Mode that describes the state of color sensors
   */
  public static boolean on_mode = true;
  
  /**
   * red value variables used to assigned the output of each light sensor
   */
  public static float leftRedVal, rightRedVal;
  
  /**
   * boolean used to identify if the right light sensor detects a line
   */
  public static boolean rightLineDetect = true;
  
  /**
   * boolean used to identify if the left light sensor detected a line.
   */
  public static boolean leftLineDetect = true;
  
  /**
   * Get red values of both color sensors to continuously update the values of
   */
  @Override
  public void run() {
    on_mode = true;
    while(on_mode) {
      try {
        leftColorSensor.getRedMode().fetchSample(leftRgbArr, 0);
        rightColorSensor.getRedMode().fetchSample(rightRgbArr, 0);
        leftRedVal = leftRgbArr[0] * 100;
        rightRedVal = rightRgbArr[0] * 100;
        Thread.sleep(50);
        
      } catch(InterruptedException e) {
       if (on_mode == false)
         break;
      }      
    }
  }
  
  /**
  * Method used to locate the precise location of the initial position 
  * and adjust the robot to 0 degrees afterwards.
  */
  public void localize() {
   //Turn robot to 45 degrees
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(ROTATE_SPEED);
    initialRedValue = leftRedVal;
    Navigation.turnTo(45);
    
    //Proceed forward until the color sensors detect lines
    while(Math.abs(leftRedVal - initialRedValue) < rgbThres || Math.abs(rightRedVal - initialRedValue) < rgbThres) {
      leftMotor.forward();
      rightMotor.forward();
    }
    leftMotor.stop(true);
    rightMotor.stop(false);
    
    //Make the robot turn left until the left sensor finds it
    
    //make the robot turn right until the right sensor finds it
    
    //do calculations
    
    //turn robot back to facing north
  }
  
  /**
   * This method is responsible to find the smallest difference of two angles.
   * 
   * @param angle1     This is the first angle
   * @param angle2     This is the second angle
   * @return double    The difference of the two angles
   */
  public static double cal_angle_diff(double angle1, double angle2) {
    double a = Math.abs(angle1 - angle2);
    double b;
    if (angle1 > angle2) {
      b = 360 - angle1 + angle2;
    } else {
      b = 360 - angle2 + angle1;
    }
    if (a > b) {
      return b;
    } else {
      return a;
    }
  }
  
  /**
   *Turns right by a specified angle.
   *@param angle    the angle to turn right by
   */
  public static void turnrightBy(double angle) {
    leftMotor.rotate(convertAngle(angle), true);
    rightMotor.rotate(-convertAngle(angle), false);
  }
  
  /**
   *Turn left by a specified angle.
   *@param angle    the angle to turn left by
   */
  public static void turnleftBy(double angle) {
    leftMotor.rotate(-convertAngle(angle), true);
    rightMotor.rotate(convertAngle(angle), false);
  }
  
  /**
   * Converts input angle to the total rotation of each wheel needed to rotate the robot by that
   * angle.
   * 
   * @param angle the input angle
   * @return the wheel rotations necessary to rotate the robot by the angle
   */
  public static int convertAngle(double angle) {
    return convertDistance(Math.PI * BASE_WIDTH * angle / 360.0);
  }
  
  
  /**
   * Converts input distance to the total rotation of each wheel needed to cover that distance.
   * 
   * @param distance the input distance
   * @return the wheel rotations necessary to cover the distance
   */
  public static int convertDistance(double distance) {
    return (int) ((180.0 * distance) / (Math.PI * WHEEL_RAD));
  }
  
  /**
   * Moves the robot straight for the given distance.
   * 
   * @param distance in feet (tile sizes), may be negative
   */
  public static void moveStraightFor(double distance) {
    leftMotor.rotate(convertDistance(distance * TILE_SIZE), true);
    rightMotor.rotate(convertDistance(distance * TILE_SIZE), false);
  }
  
  /**
   * Moves the robot back for the given distance.
   * 
   * @param distance in feet (tile sizes), may be negative
   */
  public static void moveBackFor(double distance) {
    leftMotor.rotate(-convertDistance(distance * TILE_SIZE), true);
    rightMotor.rotate(-convertDistance(distance * TILE_SIZE), false);
  }
  
  /**
   * This method is ran constantly during a thread of the robot's navigation. 
   * It is used to help the robot calibrate itself to move in a straight line.
   * It is used to detect lines using the color sensor while the robot is moving.
   * Whenever one sensor detects a line, it will check if the other sensor also detects a line.
   * If it does, it will set one of the boolean variables associated to the sensors to false.
   * 
   * @return true   if all the sensors detects a line
   *         false  if one of the sensors does not detect a line.
   */
  public static boolean checkSensors() {
    //Default booleans set to true
    rightLineDetect = true;
    leftLineDetect = true;
    
    //If the left sensor detects the line first
    if (Math.abs(leftRedVal - initialRedValue) > rgbThres) {
      leftMotor.stop(true);
      rightMotor.stop(false);
      //if the right sensor does not detect a line
      if(Math.abs(rightRedVal - initialRedValue) < rgbThres) {
        rightLineDetect = false;
        return false;
      }
    }
    
    //if the right sensor detects the line first
    else if(Math.abs(rightRedVal - initialRedValue) > rgbThres) {
      leftMotor.stop(true);
      rightMotor.stop(false);
      //if the left sensor does not detect a line
      if(Math.abs(leftRedVal - initialRedValue) < rgbThres) {
        leftLineDetect = false;
        return false;
      }
    }
    return true;
  }
  
  /**
   * This method is responsible to correct the robot's trajectory by using two light sensors to detect lines on the playing field.
   * When one of the light sensors detect a line, it will make sure that the other sensor also detects the line.
   * If the other sensor does detect a line at the same time, that means the robot is moving in a straight line.
   * Other wise, the robot will stop and run the motor that is on the same side of the sensor until it detects a line.
   * This will make the robot face straight.
   * 
   * @return boolean     true if it successfully adjusted
   *                     false if it unsuccessfully adjusted
   */
  public static boolean lightAdjustment() {
    leftMotor.stop(true);
    rightMotor.stop(false);
    
    //if left sensor doesn't detect a line, 
    if(leftLineDetect == false) {
      //turn left motor until it detects a line
      while(true) {
        if(Math.abs(rightRedVal - initialRedValue) > rgbThres) {
          leftMotor.stop(true);
          rightMotor.stop(false);
          break;
        }
        leftMotor.forward();
      }
    }
    
    //if right sensor doesn't detect a line
    else if(rightLineDetect == false) {
      //turn right motor until it detects a line
      while(true) {
        if(Math.abs(leftRedVal - initialRedValue) > rgbThres) {
          leftMotor.stop(true);
          rightMotor.stop(false);
          break;
        }
        rightMotor.forward();
      }
    }
    return true;
  }
}