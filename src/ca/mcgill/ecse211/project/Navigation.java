package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.ACCELERATION;
import static ca.mcgill.ecse211.project.Resources.BASE_WIDTH;
import static ca.mcgill.ecse211.project.Resources.FORWARD_SPEED;
import static ca.mcgill.ecse211.project.Resources.ROTATE_SPEED;
import static ca.mcgill.ecse211.project.Resources.TILE_SIZE;
import static ca.mcgill.ecse211.project.Resources.WHEEL_RAD;
import static ca.mcgill.ecse211.project.Resources.leftMotor;
import static ca.mcgill.ecse211.project.Resources.odometer;
import static ca.mcgill.ecse211.project.Resources.rightMotor;

import lejos.hardware.Sound;

/**
 * This class is used to make the robot to precisely travel to specified coordinates on the playing field.
 * In order to accurately do so, it uses the odometer.
 * 
 * One of the main aspect of the navigation mechanism is that the robot will only be traveling along the x and y axis. 
 * This feature was implemented to reduce the number of localizations needed on the playing field.
 * In order to keep the robot moving in a straight line, we will be using the lightLocalization class that will use the two light sensors to track the perpendicular lines while the robot is travelling.
 * If it was determined that the robot isn't moving in a straight line, the robot will readjust itself. 
 * @author Kevin
 *
 */
public class Navigation {
  
  /**
   *  declare values for the difference in (x, y) between destination
   *  and current location.
   */
  private static double delta_x;
  private static double delta_y;
  private static double current_x;
  private static double current_y;
  private static double target_x;
  private static double target_y;
  
  /**
   * This method makes the robot go to specified coordinates on the playing field.
   * 
   * @param x      The x coordinate of the destination
   * @param y      The y coordinate of the destination
   */
  public static void travelTo(double x, double y) {
    
    current_x = odometer.getXyt()[0];
    current_y = odometer.getXyt()[1];
    
    target_x = x;
    target_y = y;
    
    delta_x = target_x - current_x;
    delta_y = target_y - current_y;
    
    // we always start from a specific orientation
    turnTo(0);
    
    // let's say we want to move along the y-direction first 
    if(delta_y > 0) {
      // this means that we should continue going in the same y-direction
      // do nothing
    } else {
      // this means that we should go in the opposite direction
      turnByClockwise(180);
    }
    
    // now we travel
    while(delta_y != 0) {
      moveStraightFor(1);
      
      // we check if we are not moving in a straight line
      if(LightLocalizer.checkSensors() == false) {
        // we correct the path of the robot
        LightLocalizer.lightAdjustment();
      }
      // we update the value of delta_y
      delta_y = target_x - current_x;
    }
    
    // now we need to travel in the x-direction
    // we are currently pointing 0 degrees
    
    if(delta_x > 0) {
      // this means that we should turn right
      turnTo(90);
    } else {
      // this means that we should turn left
      turnTo(-90);
    }
    
    // now we need to go to the target x
    while(delta_x != 0) {
      moveStraightFor(1);
      // we check if we are not moving in a straight line
      if(LightLocalizer.checkSensors() == false) {
        // we correct the path of the robot
        LightLocalizer.lightAdjustment();
      }
      // we update the value of delta_x
      delta_x = target_x - current_x;
    }
    
  }
  
  /**
   * Makes the robot orient itself in preparation to travel to point (x, y).
   * @param x the x coordinate we want to reach
   * @param y the y coordinate we want to reach
   */
  public static void orient(double x, double y) {
    rightMotor.setAcceleration(ACCELERATION);
    leftMotor.setAcceleration(ACCELERATION);
    
    // we convert the odometer reading to feet
    double x_current = odometer.getXyt()[0] / TILE_SIZE;
    double y_current = odometer.getXyt()[1] / TILE_SIZE;
    
    // we calculate the angle we want to turn
    delta_x = x - x_current;
    delta_y = y - y_current;
    double turning_angle = Math.atan(delta_x / delta_y) * 180 / Math.PI;
    
    // we identify in which quadrant we want to go and adjust the angle we
    // want to turn accordingly
    // we are keeping theta in the range [0,360]
    if (turning_angle < 0) {
      if (delta_x < 0) {
        turnTo(360 + turning_angle);
      } else {
        turnTo(180 + turning_angle);
      }
    } else {
      if (delta_x < 0) {
        turnTo(180 + turning_angle);
      } else {
        turnTo(turning_angle);
      }
    }
    leftMotor.stop(true);
    rightMotor.stop(false);    
  }
  
  /**
   * Orients the robot to the desired angle.
   * 
   * @param target the angle we want to turn to
   */
  public static void turnTo(double target) {
    double current_theta = odometer.getXyt()[2];
    
    // we calculate the angle we would turn clockwise and anti-clockwise
    double angleTurnedByClockwise = (360 - current_theta + target) % 360;
    double angleTurnedByAntiClockwise = (360 - target + current_theta) % 360;
    
    // we turn by the smaller one in the appropriate direction
    if (angleTurnedByClockwise > angleTurnedByAntiClockwise) {
      turnByAntiClockwise(angleTurnedByAntiClockwise);
    } else {
      turnByClockwise(angleTurnedByClockwise);
    }
  }
  
  /**
   * Turns the robot clockwise by an angle 'angle'.
   * 
   * @param angle the angle we are turning by clockwise
   */
  public static void turnByClockwise(double angle) {
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(ROTATE_SPEED);
    leftMotor.rotate(convertAngle(angle), true);
    rightMotor.rotate(-convertAngle(angle), false);
  }
  
  /**
   * Turns the robot anti-clockwise by an angle 'angle'.
   * 
   * @param angle the angle we are turning by anti-clockwise
   */
  public static void turnByAntiClockwise(double angle) {
    leftMotor.setSpeed(ROTATE_SPEED);
    rightMotor.setSpeed(ROTATE_SPEED);
    leftMotor.rotate(-convertAngle(angle), true);
    rightMotor.rotate(+convertAngle(angle), false);
  }
  
  /**
   * Moves the robot straight for the given distance.
   * 
   * @param distance in feet (tile sizes), may be negative
   */
  public static void moveStraightFor(double distance) {
    leftMotor.setSpeed(FORWARD_SPEED);
    rightMotor.setSpeed(FORWARD_SPEED);
    leftMotor.rotate(convertDistance(distance * TILE_SIZE), true);
    rightMotor.rotate(convertDistance(distance * TILE_SIZE), false);
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
   * This navigation method is used to help the robot go past the bridge before initiating the search for the rescue cart.
   * It is also responsible when the robot rescued the cart and is going back to the base.
   */
  public static void goPastBridge() {
    if(UltrasonicAvoidance.cartFound == false) {
      //Coordinates for exiting the tunnel
    }
    
    else {
      //coordinates for entering the tunnel
    }
  }
}