package ca.mcgill.ecse211.project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.hardware.sensor.EV3ColorSensor;

/**
 * This class is used to define static resources in one place for easy access and to avoid
 * cluttering the rest of the codebase. All resources can be imported at once like this:
 * 
 * <p>{@code import static ca.mcgill.ecse211.lab3.Resources.*;}
 */
public class Resources {
  
  /**
   *  LCD Screen used for displaying information to the user.
   */
  public static final TextLCD lcd = LocalEV3.get().getTextLCD();
  
  /**
   * Ultrasonic Sensor.
   */
  public static final EV3UltrasonicSensor usSensor = new EV3UltrasonicSensor(SensorPort.S1);
  
  /**
   * Color Sensor for color detection.
   */
  public static final EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S2);
  
  /**
   * Color Sensor for localization.
   */
  public static final EV3ColorSensor lightSensor = new EV3ColorSensor(SensorPort.S4);
  /**
   * Left Motor.
   */
  public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.D);
  
  /**
   * Right motor.
   */
  public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.A);
  
  /**
   * The odometer.
   */
  public static Odometer odometer = new Odometer();
  
  /**
   * The wheel radius in centimeters.
   */
  public static final double WHEEL_RAD = 2.134;
  
  /**
   * The robot width in centimeters.
   */
  public static final double BASE_WIDTH = 15.10;
  
  /**
   * Distance between the center of rotation and the Ultrasonic sensor.
   */
  public static final double SENSOR_DISPLACEMENT = 6.0;
  
  /**
   * The speed at which the robot moves forward in degrees per second.
   */
  public static final int FORWARD_SPEED = 150;
  
  /**
   * The speed at which the robot rotates in degrees per second.
   */
  public static final int ROTATE_SPEED = 50;
  
  /**
   * The motor acceleration in degrees per second squared.
   */
  public static final int ACCELERATION = 200;
  
  /**
   * The tile size in centimeters. Note that 30.48 cm = 1 ft.
   */
  public static final double TILE_SIZE = 30.48;
  
  
  
}