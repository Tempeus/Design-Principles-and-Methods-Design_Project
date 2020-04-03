package ca.mcgill.ecse211.project;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;

/**
 * This class is used to define static resources in one place for easy access and to avoid
 * cluttering the rest of the codebase. All resources can be imported at once like this:
 * 
 * <p>{@code import static ca.mcgill.ecse211.lab3.Resources.*;}
 */
public class Resources {
  
  /* WIFI SETUP RESOURCES*/
  
 //Set these as appropriate for your team and current situation
 /**
  * The default server IP used by the profs and TA's.
  */
 public static final String DEFAULT_SERVER_IP = "192.168.2.3";
 
 /**
  * The IP address of the server that transmits data to the robot. For the beta demo and
  * competition, replace this line with
  * 
  * <p>{@code public static final String SERVER_IP = DEFAULT_SERVER_IP;}
  */
 public static final String SERVER_IP = "192.168.2.3"; // = DEFAULT_SERVER_IP;
 
 /**
  * Your team number.
  */
 public static final int TEAM_NUMBER = 13;
 
 /** 
  * Enables printing of debug info from the WiFi class. 
  */
 public static final boolean ENABLE_DEBUG_WIFI_PRINT = true;
 
 /**
  * Enable this to attempt to receive Wi-Fi parameters at the start of the program.
  */
 public static final boolean RECEIVE_WIFI_PARAMS = true;
 
 /* PROJECT VARIABLES */
 
  /**
   * 45 Degrees
   */
  public static final double DEGREE_45 = 45.0;
  
  /**
   * 0 Degrees
   */
  public static final double DEGREE_0 = 0.0;
  
  /**
   * 180 Degrees
   */
  public static final double DEGREE_180 = 180.0;
  
  /**
   * 225 Degrees
   */
  public static final double DEGREE_225 = 225.0;
  
  /**
   * 360 Degrees
   */
  public static final double DEGREE_360 = 360.0;
  
  /**
   * Offset from the wall (cm).
   */
  public static final int BAND_CENTER = 35;

  /**
   * Width of dead band (cm).
   */
  public static final int BAND_WIDTH = 3;
  
  /**
   * The offset for the light sensor
   */
  /* TODO */
  public static final double LIGHTSENSOR_OFFSET = 10.4;
  
  /**
   * 
   */
  public static final int FILTER_OUT = 25;
  
  /* TODO TWEAK */
  /**
   * The wheel radius in centimeters.
   */
  public static final double WHEEL_RAD = 2.130;
  
  /* TODO TWEAK*/
  /**
   * The robot width in centimeters.
   */
  public static final double BASE_WIDTH = 10.15;
  
  /**
   * The speed at which the robot moves forward in degrees per second.
   */
  public static final int FORWARD_SPEED = 150;
  
  /**
   * The speed at which the robot rotates in degrees per second.
   */
  public static final int ROTATE_SPEED = 100;
  
  /**
   * The motor acceleration in degrees per second squared.
   */
  public static final int ACCELERATION = 2500;
  
  /**
   * Timeout period in milliseconds.
   */
  public static final int TIMEOUT_PERIOD = 3000;
  
  /**
   * The tile size in centimeters. Note that 30.48 cm = 1 ft.
   */
  public static final double TILE_SIZE = 30.48;
  
  /**
   * The left motor.
   */
  public static final EV3LargeRegulatedMotor leftMotor = new EV3LargeRegulatedMotor(MotorPort.A);

  /**
   * The right motor.
   */
  public static final EV3LargeRegulatedMotor rightMotor = new EV3LargeRegulatedMotor(MotorPort.D);
  
  /**
   * The ultrasonic sensor.
   */
  public static final EV3UltrasonicSensor ultraSonicSensor =
      new EV3UltrasonicSensor(LocalEV3.get().getPort("S1"));
  
  /**
   * The left color sensor.
   */
  public static final EV3ColorSensor leftColorSensor = new EV3ColorSensor(SensorPort.S2);
  
  /**
   * The right color sensor
   */
  public static final EV3ColorSensor rightColorSensor = new EV3ColorSensor(SensorPort.S3);
  
  /**
   * The color sensor in the back
   * Used to identify if the object is a wall or the objective cart
   */
  public static final EV3ColorSensor backColorSensor = new EV3ColorSensor(SensorPort.S4);

  /**
   * The limit of invalid samples that we read from the US sensor before assuming no obstacle.
   */
  public static final int INVALID_SAMPLE_LIMIT = 20;
  
  /**
   * The LCD.
   */
  public static final TextLCD lcd = LocalEV3.get().getTextLCD();
  
  /**
   * The odometer.
   */
  public static Odometer odometer = Odometer.getOdometer();
}