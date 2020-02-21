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

public class Navigation {
  
  private static double delta_x;
  private static double delta_y;
  
  /**
   * Makes the robot travel to the point (x,y).
   * @param x the x coordinate we want to reach
   * @param y the y coordinate we want to reach
   */
  public static void travelTo(double x, double y) {
    rightMotor.setAcceleration(ACCELERATION);
    leftMotor.setAcceleration(ACCELERATION);
    // we want to go to (x,y)
    // we first need to know where we are
    // we convert the odometer reading to feets
    double x_current = odometer.getX() / TILE_SIZE;
    double y_current = odometer.getY() / TILE_SIZE;
    //odometer.setXyt(x_current * TILE_SIZE, y_current * TILE_SIZE, odometer.getTheta() % 360);
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
    // now we need to calculate the distance we have to move
    // distance is in feet (same as tile size)
    double distance = Math.hypot(delta_x, delta_y);
    // we move by this distance
    moveStraightFor(distance);
    
  }
  
  /**
   * Orients the robot to the desired angle.
   * 
   * @param target the angle we want to turn to
   */
  public static void turnTo(double target) {
    double current_theta = odometer.getTheta();
    
    // we calculate the angle we would turn clockwise and anticlockwise
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
   * Turns the robot anticlockwise by an angle 'angle'.
   * 
   * @param angle the angle we are turning by anticlockwise
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
  
}
