//This is the core structure of the Robot's code

//This is the the necessary library to import
//Plugin, Implementation

package frc.robot;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.math.filter.SlewRateLimiter;


public class Robot extends TimedRobot {
  // Inherant the motor object.

  // We control the vehicle using Motor

  //Access the indivuduals motor we have to inherant it from this PWMSparkMax.
  private final PWMSparkMax m_left1Motor = new PWMSparkMax(0);
  private final PWMSparkMax m_left2Motor = new PWMSparkMax(1);
  private final PWMSparkMax m_right1Motor = new PWMSparkMax(2);
  private final PWMSparkMax m_right2Motor = new PWMSparkMax(3);

  //This is to organize which motor is on which side.
  private final MotorControllerGroup m_left = new MotorControllerGroup(m_left1Motor, m_left2Motor);
  private final MotorControllerGroup m_right = new MotorControllerGroup(m_right1Motor, m_right2Motor);
  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_left, m_right);

  //Access to the player Input which is the joystick.
  private final Joystick m_stick = new Joystick(0);

  //For the system 
  private long autoStartTime = 0;
  private boolean rightButtonPrevValue = false;
  private boolean scoreRightCommandRunning = false;
  private long commandStartTime = 0;

  @Override
  public void robotInit() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    m_right.setInverted(true);
  }

  @Override
  //This functio is called when the robot is turn on.
  public void autonomousInit() {
    autoStartTime = System.currentTimeMillis();
  }

  @Override
  // This is the first phase of the autmomous driving
  public void autonomousPeriodic() {
    
    long autoTime = System.currentTimeMillis() - autoStartTime;

    if(autoTime < 300) {
      m_robotDrive.arcadeDrive(-0.5, 0);
    }
    else if (autoTime < 500) {
      m_robotDrive.arcadeDrive(-1, 0);
    }
    else if (autoTime < 2100) {
        m_robotDrive.arcadeDrive(0.7, 0);
    }
    else if (autoTime < 3050) {
      m_robotDrive.arcadeDrive(0, 0.7);
    }
    else {
      m_robotDrive.arcadeDrive(0, 0);
    }
    
  }

  @Override
  //This is the phase when the robot is being controlled by the driver.
  public void teleopPeriodic() {
    // Drive with arcade drive.
    // That means that the Y axis drives forward
    // and backward, and the X turns left and right.

    //Robots  speed;
    double speed = m_stick.getY() * 0.8;
    //How much rotation speed;
    double turn  = m_stick.getX() * 0.65;

    //Boosting boolean;
    boolean boost = m_stick.getRawButton(1);
    
    //Preset
    boolean shootRight = m_stick.getRawButton(4);
    boolean shootleft = m_stick.getRawButton(3);

    
    if(shootRight && !rightButtonPrevValue) {
      // Start the command
      if (!scoreRightCommandRunning) {
        scoreRightCommandRunning = true;
        commandStartTime = System.currentTimeMillis();
      }
    }
    rightButtonPrevValue = shootRight;

    if (scoreRightCommandRunning) {
      long curTime = System.currentTimeMillis() - commandStartTime;

      if(curTime < 500) {
        m_robotDrive.arcadeDrive(-0.8, 0);
      }
      else if(curTime < 1200) {
        m_robotDrive.arcadeDrive(0, 0.7);
      }
      else if(curTime < 1500) {
        m_robotDrive.arcadeDrive(1, 0);
      }
      else {
        scoreRightCommandRunning = false;
      }
      return;
    }

    //if the boost button is pressed then the robot will be boosted up.
    if(boost) {
      speed *= 1.5;
      turn *= 1.0;
    }

    //This is the direct command to tell the computer how much speed it is and turn it 
    m_robotDrive.arcadeDrive(speed, turn);
  }
}
