package ca.mcgill.ecse211.project;

import static ca.mcgill.ecse211.project.Resources.FORWARD_SPEED;
import static ca.mcgill.ecse211.project.Resources.TILE_SIZE;
import static ca.mcgill.ecse211.project.Resources.leftClawMotor;
import static ca.mcgill.ecse211.project.Resources.rightClawMotor;
import static ca.mcgill.ecse211.project.Resources.leftMotor;
import static ca.mcgill.ecse211.project.Resources.rightMotor;

/**
 * This class is used to initiate Rescue procedures when the rescue vehicle is successfully identified
 * @author Kevin
 *
 */
public class Rescue {
  
  /**
   * The locking power of the claw motors
   */
  private static int lockPower = 100;
  
  /**
   * The claw rotating angle
   */
  private static int claw_angle = 90;

  /**
   * This method is responsible in performing the rescue of the robot.
   * The design of the robot allows it to ignore the process of attempting to find the attachment point of the rescue cart.
   * The robot is equipped with claws that is large enough and sturdy enough to capture and lock onto the cart while the robot returns to the base.
   * 
   * When the robot detects the cart, it will reverse a small distance and rotate 180 degrees. This will allow the claws to be aligned with the objective cart.
   * Afterwards, the robot will slowly move forward the same small distance and the motors of each claw will rotate 90 degrees. This will capture the rescue cart.
   * Finally, the robot will lock the motors, preventing the rescue cart to break free while the robot is returning back to base. 
   */
  public static void performRescue() {
    //Adjust the robot
    adjustRobot();
    
    //activate claws
    activateClaws();
    
    //lock the claws
    lockClaws();
  }
  
  /**
   * This method is responsible in adjusting the robot.
   * When the robot successfully identified the object as a objective cart, it must rotate itself by 180 degrees in order to have its claws facing the cart.
   * In order to prevent collision with the cart, the robot will reverse 15 cm and will go back after the rotation phase.
   */
  private static void adjustRobot() {
    //Move the robot back, to prevent collision when rotating 180 degrees
    leftMotor.setSpeed(FORWARD_SPEED);
    rightMotor.setSpeed(FORWARD_SPEED);
    leftMotor.rotate(Navigation.convertDistance(-15), true);
    rightMotor.rotate(Navigation.convertDistance(-15), false);
    
    //turn 180 degrees, so the claws is facing the objective cart
    Navigation.turnByClockwise(180);
    
    //Move the robot forward
    leftMotor.rotate(Navigation.convertDistance(-15), true);
    rightMotor.rotate(Navigation.convertDistance(-15), false);
  }
  
  /**
   * Method is used to control the motors of the claw, making them rotate 90 degrees in order to trap the rescue cart.
   */
  private static void activateClaws() {
    leftClawMotor.rotate(claw_angle);
    rightClawMotor.rotate(claw_angle);
  }
  
  /**
   * Method is used to lock the motors of the claws, preventing the rescue cart from escaping.
   */
  @SuppressWarnings("deprecation")
  private static void lockClaws() {
    leftClawMotor.lock(lockPower);
    rightClawMotor.lock(lockPower);
  }
}
