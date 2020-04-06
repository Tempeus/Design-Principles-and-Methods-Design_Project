package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.ACCELERATION;
import static ca.mcgill.ecse211.project.Resources.BASE_WIDTH;
import static ca.mcgill.ecse211.project.Resources.FORWARD_SPEED;
import static ca.mcgill.ecse211.project.Resources.ROTATE_SPEED;
import static ca.mcgill.ecse211.project.Resources.SENSOR_DISPLACEMENT;
import static ca.mcgill.ecse211.project.Resources.WHEEL_RAD;
import static ca.mcgill.ecse211.project.Resources.leftMotor;
import static ca.mcgill.ecse211.project.Resources.rightMotor;
import static ca.mcgill.ecse211.project.Resources.ultraSonicSensor;
import lejos.hardware.Button;

/**
 * This class is used to localize the robot using the Ultrasonic Sensor at the beginning of the competition.
 */
public class UltrasonicLocalize implements Runnable {
  
  /**
   *  Float array used to store readings from the Ultrasonic sensor.
   */
  private static float[] usData = new float[ultraSonicSensor.sampleSize()];
  
  /**
   * Integer indicating the angle intervals to scan in during phase one of localization.
   */
  private static final int phaseOneScanAngle = 15;
  
  /**
   * Integer indicating the angle intervals to scan in during phase two of localization.
   */
  private static final int phaseTwoScanAngle = 3;
  
  /**
   * Executes the ultrasonic localization, situating the robot towards a
   * predefined 0 degrees direction, and moving the robot towards the predefined
   * origin point.
   */
  @Override
  public void run() {
    
    // Set rotational speed and acceleration.
    setSpeed(ROTATE_SPEED);
    setAcceleration(ACCELERATION);
    
    // Scan 360 degrees around the robot for the distance from the wall.
    double[] ultrasonicSensorData = new double[360 / phaseOneScanAngle];
    for (int i = 0; i < (360 / phaseOneScanAngle); i++) {
      ultrasonicSensorData[i] = avgReading();
      turnBy(phaseOneScanAngle);
      
      // Sleep the thread for 300 milliseconds to give the sensor time;
      try {
        Thread.sleep(300);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
    // locate and turn towards the smallest reading from the wall.
    int minPos = getMin(ultrasonicSensorData);
    turnBy(phaseOneScanAngle * minPos - phaseOneScanAngle);
    
    // Scan in a fan 2x the size of phaseOneAngle 
    // around the wall to improve the accuracy of our angle.
    double[] sensorDataPhaseTwo = new double[(2 * phaseOneScanAngle) / phaseTwoScanAngle];
    for (int i = 0; i < ((2 * phaseOneScanAngle) / phaseTwoScanAngle); i++) {
      sensorDataPhaseTwo[i] = avgReading();
      turnBy(phaseTwoScanAngle);
      
      // Sleep the thread for 300 milliseconds to give the sensor time;
      try {
        Thread.sleep(300);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
    // Rotate back towards the wall after fanning out.
    int minPosPhaseTwo = getMin(sensorDataPhaseTwo);
    // We subtract 1 from the length to line up with the value of minPosPhaseTwo
    turnBy(((sensorDataPhaseTwo.length - 1) - minPosPhaseTwo) * -phaseTwoScanAngle);
    
    // Determine which wall was discovered and turn the robot to face 0 degrees.
    turnBy(90);
    if (avgReading() < 200) {
      turnBy(90);
    }
    
    // Call the method to move the robot to the origin.
    goToOrigin();
  }
  
  
  /**
   * Once localized to 0 degrees, this function will move the robot
   * to the origin.
   */
  public static void goToOrigin() {
    
    // Rotate 90 degrees to the left and measure the distance from the wall.
    turnBy(-90);
    double distance = avgReading();
    
    // Turn 180 degrees to face the direction we need to move in.
    turnBy(180);
    
    // Move towards the origin in the x-direction, using the distance measured earlier.
    setSpeed(FORWARD_SPEED);
    leftMotor.rotate(convertDistance(distance - SENSOR_DISPLACEMENT), true);
    rightMotor.rotate(convertDistance(distance - SENSOR_DISPLACEMENT), false);
    
    // Rotate 90 degrees right to face the second wall.
    setSpeed(ROTATE_SPEED);
    setAcceleration(ACCELERATION);
    turnBy(90);
    
    // measure the distance from the wall, and turn 180 degrees to face the positive y-direction.
    distance = avgReading();
    turnBy(180);
    
    // Move towards the origin in the y-direction, using the distance measured earlier.
    setSpeed(FORWARD_SPEED);
    setAcceleration(ACCELERATION);
    leftMotor.rotate(convertDistance(distance - SENSOR_DISPLACEMENT), true);
    rightMotor.rotate(convertDistance(distance - SENSOR_DISPLACEMENT), false);
  }
  
  /**
   * Gets the most recent reading from the ultrasonic sensor.
   * 
   * @return Ultrasonic sensor reading in cm
   */
  public static double getUSdistance() {
    ultraSonicSensor.fetchSample(usData, 0);
    // extract from buffer, convert to cm, cast to int, and filter
    return (usData[0] * 100.0);
  }
  
  /**
   * Turns the robot by a specified angle. Note that this method is different from
   * {@code Navigation.turnTo()}. For example, if the robot is facing 90 degrees, calling
   * {@code turnBy(90)} will make the robot turn to 180 degrees, but calling
   * {@code Navigation.turnTo(90)} should do nothing (since the robot is already at 90 degrees).
   * 
   * @param angle the angle by which to turn, in degrees
   */
  public static void turnBy(double angle) {
    leftMotor.rotate(convertAngle(angle), true);
    rightMotor.rotate(-convertAngle(angle), false);
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
   * Stops both motors.
   */
  public static void stopMotors() {
    leftMotor.stop();
    rightMotor.stop();
  }
  
  /**
   * Sets the speed of both motors to the same values.
   * 
   * @param speed the speed in degrees per second
   */
  public static void setSpeed(int speed) {
    setSpeeds(speed, speed);
  }
  
  /**
   * Sets the speed of both motors to different values.
   * 
   * @param leftSpeed the speed of the left motor in degrees per second
   * @param rightSpeed the speed of the right motor in degrees per second
   */
  public static void setSpeeds(int leftSpeed, int rightSpeed) {
    leftMotor.setSpeed(leftSpeed);
    rightMotor.setSpeed(rightSpeed);
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
   * Rotates robot clockwise.
   */
  public static void rotateRobot() {
    setSpeeds(ROTATE_SPEED, ROTATE_SPEED);
    rightMotor.backward();
    leftMotor.forward();
  }
  
  /**
   * Returns the position of the smallest double in a given array.
   * 
   * @param arr An array of doubles
   * @return the array position of the smallest double
   */
  public static int getMin(double[] arr) {
    int pos = 0;
    double currData = Double.MAX_VALUE;
    for (int i = 0; i < arr.length; i++) {
      if (arr[i] < currData) {
        currData = arr[i];
        pos = i;
      }
    }
    return pos;
  }
  
  /**
   * Gets 10 readings from the ultrasonic sensor then returns the average.
   * 
   * @return an average of the last 10 readings from the Ultrasonic Sensor
   */
  public static double avgReading() {
    double distanceEstimate = 0;
    for (int i = 0; i < 10; i++) {
      distanceEstimate += getUSdistance();
    }
    return distanceEstimate /= 10.0;
  }
  
  /**
   * Stops the provided thread by interrupting it
   * @param ultrasonicLocalizeSensor    The thread for the ultrasonicSensor thread
   */
  public void stopThread(Thread ultrasonicLocalizeSensor) {
    ultrasonicLocalizeSensor.interrupt();
  }
}