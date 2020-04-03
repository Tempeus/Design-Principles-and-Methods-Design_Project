package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.BASE_WIDTH;
import static ca.mcgill.ecse211.project.Resources.TILE_SIZE;
import static ca.mcgill.ecse211.project.Resources.WHEEL_RAD;
import static ca.mcgill.ecse211.project.Resources.leftMotor;
//import static ca.mcgill.ecse211.project.Resources.lightSensor;
import static ca.mcgill.ecse211.project.Resources.odometer;
import static ca.mcgill.ecse211.project.Resources.rightMotor;

import lejos.hardware.Sound;

/**
 * The class will help the robot to localize on the playing field using light sensors attached to the robot.
 * @author Kevin
 *
 */
public class LightLocalizer {
  //Array used to store the angles of the black lines
  private static double[] angles = {0, 0, 0, 0};
  
  //Array used to store angle differences.
  private static double[] anglesdiff = {0, 0, 0, 0};
  
  //To detect how many points it detected
  private static int angleIndex = 0;
  
  //Initial red value of board
  private static double initialRedValue;
  
  //Variable used to get the red value
  private static float[] rgbArr = new float[1];
  
  //Threshold to detect lines
  private static int rgbThres = 11;

  /*
   * Get the red value from the color sensor
   * @return rgbArr[0] * 100
   */
  private static float getRed() {
//    lightsSensor.getRedMode().fetchSample(rgbArr, 0);
    return rgbArr[0] * 100;


  }
  /**
  * Method used to locate the precise location of the initial position 
  * and adjust the robot to 0 degrees afterwards.
  */

  public void localize() {
    leftMotor.setSpeed(40);
    rightMotor.setSpeed(40);
    leftMotor.backward();
    rightMotor.forward();
    angleIndex = 0;
    
    //get initial red value and then compare it to when it's reading a black line
    initialRedValue = getRed(); 
    while (angleIndex < 4) {
      //When it hits a black line, record the angle
      if (Math.abs(getRed() - initialRedValue) > rgbThres) {
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
   * Method used to localize the robot when there are no walls around.
   * It will localize itself to a precise location using the x and y coordinates
   * @param target_x   The x-coordinate of the target
   * @param target_y   The y-coordinate of the target
   */
  public static void localize_not_corner(int target_x, int target_y) {
    leftMotor.setSpeed(50);
    rightMotor.setSpeed(50);
    leftMotor.backward();
    rightMotor.forward();
    angleIndex = 0;
    
    //get initial red value and then compare it to when it's reading a black line
    initialRedValue = getRed();
    
    
    while (angleIndex < 4) {
      //When it hits a black line, record the angle
      if (Math.abs(getRed() - initialRedValue) > rgbThres) {
        angles[angleIndex] = odometer.getXyt()[2];
        angleIndex++;
        Sound.beep();
        Main.sleepFor(200);
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
    
    if (angles[mincount] > 0 && angles[mincount] <= 90) {   
      // target point is in 1st quadrant
      double deltay = -17 * Math.cos(cal_angle_diff(angles[mincount], 
          angles[(mincount + 2) % 4] / 2));
      double deltax = -17 * Math.cos(cal_angle_diff(angles[mincount + 1], 
          angles[(mincount + 3) % 4] / 2));
      
      //update dx dy 
      odometer.update(deltax, deltax, 0);
      
      //travel to nearest point
      Navigation.travelTo(target_x, target_y);
      while (Math.abs(getRed() - initialRedValue) < rgbThres) {
        turnLeft();
      }
      stopMotors();
      
      //update theta =0
      odometer.setTheta(0);
    } else if (angles[mincount] > 180 && angles[mincount] <= 270) {      
      // target point is in 3rd quadrant
      double deltay = 17 * Math.cos(cal_angle_diff(angles[mincount], 
          angles[(mincount + 2) % 4] / 2));
      double deltax = 17 * Math.cos(cal_angle_diff(angles[mincount + 1],
          angles[(mincount + 3) % 4] / 2));
      //update dx dy 
      odometer.update(deltax, deltax, 0);
      
      //travel to nearest point
      Navigation.travelTo(target_x, target_y);
      while (Math.abs(getRed() - initialRedValue) < rgbThres) {
        turnLeft();
      }
      stopMotors();
      //update theta = 180
      odometer.setTheta(180);
    } else if (angles[mincount] > 270 && angles[mincount] <= 360) {     
      // target point is in 2nd quadrant
      double deltax = 17 * Math.cos(cal_angle_diff(angles[mincount], 
          angles[(mincount + 2) % 4] / 2));
      double deltay = -17 * Math.cos(cal_angle_diff(angles[mincount + 1], 
          angles[(mincount + 3) % 4] / 2));
      //update x y 
      odometer.update(deltax, deltax, 0);
      
      //travel to nearest point
      Navigation.travelTo(target_x, target_y);
      while (Math.abs(getRed() - initialRedValue) < rgbThres) {
        turnRight();
      }
      stopMotors();
      //update theta = 0
      odometer.setTheta(0);
    } else if (angles[mincount] > 90 && angles[mincount] <= 180) {  
      // target point is in 4th quadrant
      double deltax = -17 * Math.cos(cal_angle_diff(angles[mincount], 
          angles[(mincount + 2) % 4] / 2));
      double deltay = 17 * Math.cos(cal_angle_diff(angles[mincount + 1], 
          angles[(mincount + 3) % 4] / 2));
      //update x y 
      odometer.update(deltax, deltax, 0);
      
      //travel to nearest point
      Navigation.travelTo(target_x, target_y);
      while (Math.abs(getRed() - initialRedValue) < rgbThres) {
        turnRight();
      }
      stopMotors();
      //update theta = 180
      odometer.setTheta(180);
    }
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