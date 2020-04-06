package ca.mcgill.ecse211.project;

import java.util.Map;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import ca.mcgill.ecse211.playingfield.Point;
import ca.mcgill.ecse211.playingfield.Region;
import ca.mcgill.ecse211.wificlient.WifiConnection;
import java.math.BigDecimal;

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
  * The IP address of the server that transmits data to the robot. 
  */
 public static final String SERVER_IP = "DEFAULT_SERVER_IP"; 
 
 /**
  * Your team number.
  */
 public static final int TEAM_NUMBER = 11;
 
 /** 
  * Enables printing of debug info from the WiFi class. 
  */
 public static final boolean ENABLE_DEBUG_WIFI_PRINT = true;
 
 /**
  * Enable this to attempt to receive Wi-Fi parameters at the start of the program.
  */
 public static final boolean RECEIVE_WIFI_PARAMS = true;
 
 /**
  * Container for the Wi-Fi parameters.
  */
 public static Map<String, Object> wifiParameters;
 
 // This static initializer MUST be declared before any Wi-Fi parameters.
 static {
   receiveWifiParameters();
 }
 
 /** Red team number. */
 public static int redTeam = getWP("RedTeam");

 /** Red team's starting corner. */
 public static int redCorner = getWP("RedCorner");

 /** Green team number. */
 public static int greenTeam = getWP("GreenTeam");

 /** Green team's starting corner. */
 public static int greenCorner = getWP("GreenCorner");

 /** The Red Zone. */
 public static Region red = makeRegion("Red");

 /** The Green Zone. */
 public static Region green = makeRegion("Green");

 /** The Island. */
 public static Region island = makeRegion("Island");

 /** The red tunnel footprint. */
 public static Region tnr = makeRegion("TNR");

 /** The green tunnel footprint. */
 public static Region tng = makeRegion("TNG");

 /** The red search zone. */
 public static Region szr = makeRegion("SZR");

 /** The green search zone. */
 public static Region szg = makeRegion("SZG");
 
 /**
  * Receives Wi-Fi parameters from the server program.
  */
 public static void receiveWifiParameters() {
   // Only initialize the parameters if needed
   if (!RECEIVE_WIFI_PARAMS || wifiParameters != null) {
     return;
   }
   System.out.println("Waiting to receive Wi-Fi parameters.");

   // Connect to server and get the data, catching any errors that might occur
   try (WifiConnection conn =
       new WifiConnection(SERVER_IP, TEAM_NUMBER, ENABLE_DEBUG_WIFI_PRINT)) {
     /*
      * getData() will connect to the server and wait until the user/TA presses the "Start" button
      * in the GUI on their laptop with the data filled in. Once it's waiting, you can kill it by
      * pressing the back/escape button on the EV3. getData() will throw exceptions if something
      * goes wrong.
      */
     wifiParameters = conn.getData();
   } catch (Exception e) {
     System.err.println("Error: " + e.getMessage());
   }
 }
 
 /**
  * Returns the Wi-Fi parameter int value associated with the given key.
  * 
  * @param key the Wi-Fi parameter key
  * @return the Wi-Fi parameter int value associated with the given key
  */
 public static int getWP(String key) {
   if (wifiParameters != null) {
     return ((BigDecimal) wifiParameters.get(key)).intValue();
   } else {
     return 0;
   }
 }
 
 /** 
  * Makes a point given a Wi-Fi parameter prefix.
  */
 public static Point makePoint(String paramPrefix) {
   return new Point(getWP(paramPrefix + "_x"), getWP(paramPrefix + "_y"));
 }
 
 /**
  * Makes a region given a Wi-Fi parameter prefix.
  */
 public static Region makeRegion(String paramPrefix) {
   return new Region(makePoint(paramPrefix + "_LL"), makePoint(paramPrefix + "_UR"));
 }
 
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
   * The right claw motor
   */
  public static final EV3LargeRegulatedMotor rightClawMotor = new EV3LargeRegulatedMotor(MotorPort.B);
  
  /**
   * the left claw motor
   */
  public static final EV3LargeRegulatedMotor leftClawMotor = new EV3LargeRegulatedMotor(MotorPort.C);
  
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
   * Distance between the center of rotation and the Ultrasonic sensor.
   */
  public static final double SENSOR_DISPLACEMENT = 6.0;
  
  /**
   * The LCD.
   */
  public static final TextLCD lcd = LocalEV3.get().getTextLCD();
  
  /**
   * The odometer.
   */
  public static Odometer odometer = Odometer.getOdometer();
}