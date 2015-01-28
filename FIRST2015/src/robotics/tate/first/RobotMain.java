/*tate highschool code DO NOT DISTRIBUTE!!------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
//This is an instance of the Simple Robot Processing. Not all of the warned items
//below are there for a reason, but some of them are.  If using the two joysticks
// use the tankDrive and if using a gaming controller ie xbox or arcade, use arcadeDrive.
//Something I just realized now, TankDrive is going to make you use both sticks like a tank to move your robot places,
//both forwards = forwards, one forwards one backwards = spinning, both back = backwards, one forward = forward turn,
//and one backwards = backwards turn.  I must work to fix this, so I need to use that as one of my goals before we even
//get out there, it is of utmost importance if we are only going to use one joystick that we got last year.
//if we are using a gaming controller it might have to be in tank drive just to get the joystick layout correct, but
//then again it might look for a usb 2 and we not have it, so I will need to test.  Whoever my successor is should be
//able to look at my notes at the end and see what needs to be done.  I apologize now, this is going to be a lot of 
//notes, but I believe in you, look at them and see what needs to change.  Make sure you downloaded all of the 
//plugins from the site that it was necessary to use as your updating site, they are a First Partner and will get you
//the syntax and code building necessities.  Make sure that you are using Net Beans with those plugins, that is crucial to even start.
package robotics.tate.first;

//these are where the program finds the things it needs in order to run.  They are usually named similar to the object you are trying to control.
//import edu.wpi.first.wpilibj.AnalogModule;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Dashboard;
//import edu.wpi.first.wpilibj.DigitalModule;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Jaguar;
//import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Joystick;
//import edu.wpi.first.wpilibj.buttons.JoystickButton;
//import edu.wpi.first.wpilibj.SpeedController;
//import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//talon is my attempt for doing the shooter motors, the window motors.  hope it works, but it might not and therefore might end up being totally retarded.
//talon is not the correct motor controller for the window motor and we aren't using a window motor on the shooter anyways.
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotMain extends SimpleRobot {
    //RobotDrive.MotorType Jaguar;

    RobotDrive drive = new RobotDrive(1, 2);
    //private Jaguar driveSideL = new Jaguar(1);
    //private Jaguar driveSideR = new Jaguar(2);
    Joystick leftStick = new Joystick(1);
    Joystick rightStick = new Joystick(2);
    private final Jaguar motion = new Jaguar(3);
    //put the two drive motors on 1 and 2 and the shooter motors on 3
    private final Jaguar motionRight = new Jaguar(4);
    // private Victor lift = new Victor(4);  
    //put the lift for the shooter on this one, substituting the vex microcontroller as a victor, put it on 4
    private final Victor lift = new Victor(5);
    final Relay pincer = new Relay(6);
    SmartDashboard SDash = new SmartDashboard();

    public void watchdogOn() {  //code kill switch in case the robot stops responding for a certain amount of time but tries to continue functioning (ref id)
        getWatchdog().setEnabled(true);
    }

    public void watchdogOff() {  //code kill switch off ie autonomous when robot is staying still on a wait command (Not being controlled and being still)
        getWatchdog().setEnabled(false);
    }

    /**
     * This function is called once each time the robot enters autonomous mode.
     */
    public void updateDashboard() {
        SmartDashboard.putDouble("Motion Rear", motion.getSpeed());
        SmartDashboard.putDouble("Lift", lift.getPosition());
        SmartDashboard.putDouble("Motion Front", motionRight.getSpeed());
        Dashboard lowDashData = DriverStation.getInstance().getDashboardPackerLow();
        lowDashData.addCluster();
        {
            lowDashData.addCluster();
            {     //analog modules
                lowDashData.addCluster();
                {
                    for (int i = 1; i <= 8; i++) {
                        lowDashData.addFloat((float) motion.getSpeed());
                    }
                }
                lowDashData.finalizeCluster();
                lowDashData.addCluster();
                {
                    for (int i = 1; i <= 8; i++) {
                        lowDashData.addFloat((float) drive.getExpiration());
                    }
                }
                lowDashData.finalizeCluster();
            }
            lowDashData.finalizeCluster();

            /*lowDashData.addCluster();
             { //digital modules
             lowDashData.addCluster();
             {
             lowDashData.addCluster();
             {
             int module = 1;
             lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
             lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
             lowDashData.addShort(DigitalModule.getInstance(module).getAllDIO());
             lowDashData.addShort(DigitalModule.getInstance(module).getDIODirection());
             lowDashData.addCluster();
             {
             for (int i = 1; i <= 10; i++) {
             lowDashData.addByte((byte) DigitalModule.getInstance(module).getPWM(i));
             }
             }
             lowDashData.finalizeCluster();
             }
             lowDashData.finalizeCluster();
             }
             lowDashData.finalizeCluster();

             lowDashData.addCluster();
             {
             lowDashData.addCluster();
             {
             int module = 2;
             lowDashData.addByte(DigitalModule.getInstance(module).getRelayForward());
             lowDashData.addByte(DigitalModule.getInstance(module).getRelayReverse());
             lowDashData.addShort(DigitalModule.getInstance(module).getAllDIO());
             lowDashData.addShort(DigitalModule.getInstance(module).getDIODirection());
             lowDashData.addCluster();
             {
             for (int i = 1; i <= 10; i++) {
             lowDashData.addByte((byte) DigitalModule.getInstance(module).getPWM(i));
             }
             }
             lowDashData.finalizeCluster();
             }
             lowDashData.finalizeCluster();
             }
             lowDashData.finalizeCluster();

             }
             lowDashData.finalizeCluster();

             lowDashData.addByte(Solenoid.getAllFromDefaultModule());
             }
             lowDashData.finalizeCluster();*/
            lowDashData.commit();

        }
    }

    public void autonomous() {
        /**
         * Disable Watchdog during autonomous because there will be no remote
         * control usage during autonomous and therefore the robot will not stop
         * if controller communication is lost. Will look for communication when
         * Autonomous session is over.
         */
        watchdogOff();
//ref to above public void, calls that function.

        //for (int i = 0; i < 5; i++) {
        //  drive.drive(0.5, 0.0);  // drive 50% fwd 0% turn 
        //  Timer.delay(0.5);  // wait function, number is in wx.yz format. 
        //  drive.drive(0.0, 0.75); // drive 0% fwd, 75% turn 
        //motion.set(300); //300 % normal speed
        motion.set(1);
        motionRight.set(1);//300 % normal speed
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("Exception caused while Running a thread sleep 10s.");
        }
        //}
        //drive.drive(0.0, 0.0);  // drive 0% forward, 0% turn
        motion.set(0);
        motionRight.set(0);
        watchdogOn();
    }

    /**
     * This function is called once each time the robot enters/operator control.
     */
    public void operatorControl() {
        SmartDashboard.putString("Drive", drive.getDescription());
        //SmartDashboard.putNumber("DriveSideR",  driveSideR.getSpeed());
        SmartDashboard.putString("Blah", "OPControl");
        System.out.println("You are now able to control your robot");
        //SmartDashboard.putNumber(ERRORS_TO_DRIVERSTATION_PROP, ROBOT_TASK_PRIORITY);
        SmartDashboard.putDouble("Motion Rear", motion.getSpeed());
        SmartDashboard.putDouble("Lift", lift.getPosition());
        SmartDashboard.putDouble("Motion Front", motionRight.getSpeed());
        watchdogOn();

        Watchdog.getInstance().feed();
        // lift.set(Relay.Value.kOn);
        updateDashboard();

        while (isOperatorControl() && isEnabled()) {
            Watchdog.getInstance().feed();
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 0, Integer.toString((int) motion.getSpeed()));
            SmartDashboard.putDouble("Motion Rear", motion.getSpeed());
            SmartDashboard.putDouble("Lift", lift.getPosition());
            SmartDashboard.putDouble("Motion Front", motionRight.getSpeed());
            if (leftStick.getTrigger()) {
                motion.set(0.8);
                motionRight.set(1);
                // motion.startLiveWindowMode();
                motion.getSpeed();
                Watchdog.getInstance().feed();
                /**
                 * not motion for drive, motion for launcher. is being tested,
                 * but is only for Left launcher.
                 */
                //same info
                drive.tankDrive(leftStick, rightStick);
            } //else if (leftStick.getRawButton(4)){
            //motion.set(1);
            /*   }*/ else if (leftStick.getRawButton(6)) {
                motion.set(0);
                motionRight.set(0);
            } else if (leftStick.getRawButton(3)) {
                //lift.set(Relay.Value.kOn);
                //lift.set(Relay.Value.kForward);
                lift.set(0.5);
                drive.tankDrive(leftStick, rightStick);
            } else if (leftStick.getRawButton(2)) {
                //lift.set(Relay.Value.kOn);\
                //lift.set(Relay.Value.kReverse);
                lift.set(-0.2);
                drive.tankDrive(leftStick, rightStick);
            } else if (leftStick.getRawButton(10)) {
                //lift.set(Relay.Value.kOff);
                lift.set(0);
            } else if (rightStick.getRawButton(3)) {
                Watchdog.getInstance().feed();
                for (int a = 0; a < 2; a++) {
                    pincer.set(Relay.Value.kForward);
                    pincer.set(Relay.Value.kOn);
                    Watchdog.getInstance().feed();
                }
            } else if (rightStick.getRawButton(2)) {
                Watchdog.getInstance().feed();
                for (int a = 2; a < 4; a++) {
                    pincer.set(Relay.Value.kReverse);
                    pincer.set(Relay.Value.kOn);
                    Watchdog.getInstance().feed();
                }
                Watchdog.getInstance().feed();
            } else if (rightStick.getRawButton(4)) {
                for (int a = 4; a < 5; a++) {
                    pincer.set(Relay.Value.kOff);
                    Watchdog.getInstance().feed();
                }
                Watchdog.getInstance().feed();
            } else {
                drive.tankDrive(leftStick, rightStick);
            }
            // while()
        }
        // motion.set(0);
        //motionRight.set(0);
        Timer.delay(0.01);
    }

    /**
     * This function is called once each time the robot enters test mode.
     */
    public void test() {
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 0,
                "Entering Test Environment");
        DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6, 0,
                "PICK TEST IF AVAILABLE");
        if (DriverStation.getInstance().getTeamNumber() != 4179) {
            DriverStationLCD.getInstance().println(DriverStationLCD.Line.kMain6,
                    ROBOT_TASK_PRIORITY, "INCORRECT TEAM "
                    + "NUMBER SETUP!!!!!!!!!!!!!!!!!!!!!!!!");
        }
    }
}
