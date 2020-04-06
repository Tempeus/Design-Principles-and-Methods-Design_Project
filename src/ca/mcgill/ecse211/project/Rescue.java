package ca.mcgill.ecse211.project;

/**
 * This class is used to initiate Rescue procedures when the rescue vehicle is successfully identified
 * @author Kevin
 *
 */
public class Rescue {

  /**
   * This method is responsible in performing the rescue of the robot.
   * The design of the robot allows it to ignore the process of attempting to find the attachment point of the rescue cart.
   * The robot is equipped with claws that is large enough and sturdy enough to capture and lock onto the cart while the robot returns to the base.
   * 
   * When the robot detects the cart, it will reverse 5 cm and rotate 180 degrees. This will allow the claws to be aligned with the objective cart.
   * Afterwards, the robot will slowly move forward 5 cm and the motors of each claw will rotate 90 degrees. This will capture the rescue cart.
   * Finally, the robot will lock the motors, preventing the rescue cart to break free while the robot is returning back to base. 
   */
  public static void performRescue() {
    //go back a few steps
    //turn 180 degrees
    //go forward the same few steps
    //activate claws
    //lock the claws
  }
}
