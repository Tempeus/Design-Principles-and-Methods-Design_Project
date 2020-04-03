package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.BASE_WIDTH;
import static ca.mcgill.ecse211.project.Resources.TILE_SIZE;
import static ca.mcgill.ecse211.project.Resources.WHEEL_RAD;
import static ca.mcgill.ecse211.project.Resources.leftMotor;
import static ca.mcgill.ecse211.project.Resources.leftColorSensor;
import static ca.mcgill.ecse211.project.Resources.rightColorSensor;
import static ca.mcgill.ecse211.project.Resources.odometer;
import static ca.mcgill.ecse211.project.Resources.ROTATE_SPEED;
import static ca.mcgill.ecse211.project.Resources.rightMotor;
import static ca.mcgill.ecse211.project.Resources.LIGHTSENSOR_OFFSET;

import lejos.hardware.Sound;

/**
 * The class will help the robot to localize on the playing field using light sensors attached to the robot.
 * @author Kevin
 *
 */
public class LightLocalizer implements Runnable {
  
  //Array used to store the angles of the black lines
  private static double[] angles = {0, 0, 0, 0};
  
  //Array used to store angle differences.
  private static double[] anglesdiff = {0, 0, 0, 0};
  
  //To detect how many points it detected
  private static int angleIndex = 0;
  
  //Initial red value of board
  private static double initialRedValue;
  
  //Variable used to get the red value
  private static float[] leftRgbArr = new float[1];
  private static float[] rightRgbArr = new float[1];
  
  //Threshold to detect lines
  private static int rgbThres = 11;
  
  /**
   * Mode that describes the state of color sensors
   */
  public static boolean on_mode = true;
  /**
   * red value variables used to assigned the output of each light sensor
   */
  public static float leftRedVal, rightRedVal;

  
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
    
    //Move back offset/3 since the light sensors detected the line in a 45 degree angle, therefore the robot is offset/3 away from the line
    leftMotor.rotate(Navigation.convertDistance(- LIGHTSENSOR_OFFSET / 3), true);
    rightMotor.rotate(Navigation.convertDistance(- LIGHTSENSOR_OFFSET / 3), false);
    
    while (angleIndex < 4) {
      //When it hits a black line, record the angle
      if (Math.abs(leftRedVal - initialRedValue) > rgbThres) {
        angles[angleIndex] = odometer.getXyt()[2];
        Main.sleepFor(200);
        angleIndex++;
        Sound.beep();
      }
    }
    
    leftMotor.stop(true);
    rightMotor.stop(false);
    anglesdiff[0] = cal_angle_diff(angles[0],angles[1]);
    anglesdiff[1] = cal_angle_diff(angles[1],angles[2]);
    anglesdiff[2] = cal_angle_diff(angles[2],angles[3]);
    anglesdiff[3] = cal_angle_diff(angles[3],angles[0]);
    int i = 1;
    double min = anglesdiff[0];
    int mincount = 0;
    while (i < 4) {
      if (min > anglesdiff[i]) {
        min = anglesdiff[i];
        mincount = i;
      }
      i++;
    }
    
    double anglesdiff_half = min / 2;
    Navigation.turnTo(angles[mincount] - anglesdiff_half + 180);
    
    while (Math.abs(getRed() - initialRedValue) < rgbThres) {
      forward();
    }
    
    stopMotors();
    moveBackFor(0.5577427822); //move back for 17cm which is 0.5577427822 tilesize.
    turnleftBy(44);
    odometer.setXyt(TILE_SIZE, TILE_SIZE, 0);
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
   * continuously turn left.
   */  
  public static void turnLeft() {
    rightMotor.forward();
    leftMotor.backward();
    rightMotor.setSpeed(40);
    leftMotor.setSpeed(40);
  }  
  
  /**
   * continuously turn right.
   */
  public static void turnRight() {
    rightMotor.backward();
    leftMotor.forward();
    rightMotor.setSpeed(40);
    leftMotor.setSpeed(40);
  }
  
  /**
   * Robot goes straight forward.
   */
  public void forward() {
    rightMotor.forward();
    leftMotor.forward();
    rightMotor.setSpeed(40);
    leftMotor.setSpeed(40);
  }
  
  /**
   * Robot goes straight backward.
   */
  public void backward() {
    rightMotor.backward();
    leftMotor.backward();
    rightMotor.setSpeed(40);
    leftMotor.setSpeed(40);
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
   * Sets the acceleration of both motors.
   * 
   * @param acceleration the acceleration in degrees per second squared
   */
  public static void setAcceleration(int acceleration) {
    leftMotor.setAcceleration(acceleration);
    rightMotor.setAcceleration(acceleration);
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
  * stop both motors.
  */
  public static void stopMotors() {
    leftMotor.stop(true);
    rightMotor.stop(false);
  }
  
  /**
   * This method is used for the robot to adjust itself when it detect itself travelling not in a straight line.
   */
  public static void lightAdjustment() {
    //if left sensor rang first, you must turn right depending on how long the delay is
    //if right sensor rang first, you must turn left depending on how long the delay is
  }


}