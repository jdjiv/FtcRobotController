import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name = "ParkingAuto", group = "Autonomous")
public class ParkingAuto extends OpMode {
    // Declare motor variables
    private DcMotor LF; // Left Front
    private DcMotor LB; // Left Back
    private DcMotor RF; // Right Front
    private DcMotor RB; // Right Back

    // Timer to control timing for movements
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        // Initialize motors
        LF = hardwareMap.get(DcMotor.class, "LF");
        LB = hardwareMap.get(DcMotor.class, "LB");
        RF = hardwareMap.get(DcMotor.class, "RF");
        RB = hardwareMap.get(DcMotor.class, "RB");

        // Set motor directions
        LF.setDirection(DcMotor.Direction.REVERSE);  // Reverse Left Front Motor
        LB.setDirection(DcMotor.Direction.FORWARD);  // Left Back Motor remains Forward
        RF.setDirection(DcMotor.Direction.FORWARD);  // Right Front Motor remains Forward
        RB.setDirection(DcMotor.Direction.REVERSE);  // Reverse Right Back Motor

        // Provide feedback that the motors are initialized
        telemetry.addData("Status", "Motors Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        // Start the timer for tracking the runtime
        runtime.reset();
    }

    @Override
    public void loop() {
        // Parking logic: Move forward, then strafe, then stop

        // Example: Move forward for 2 seconds (you may need to adjust the time/distance)
        if (runtime.seconds() < 2) {
            // Move forward (all motors at equal power for forward movement)
            setMotorPowers(0.5);  // Move forward with 50% power
        }
        // After moving forward, strafe to the right for 2 seconds to park (you can change direction based on your needs)
        else if (runtime.seconds() < 4) {
            // Strafe right (right wheels move forward, left wheels move backward)
            LF.setPower(-0.5);  // Left front moves backward
            LB.setPower(0.5);   // Left back moves forward
            RF.setPower(0.5);   // Right front moves forward
            RB.setPower(-0.5);  // Right back moves backward
        }
        // After strafing, stop the robot (when we reach around 4 seconds)
        else {
            // Stop all motors
            setMotorPowers(0);
            telemetry.addData("Status", "Parking complete!");
        }

        // Telemetry to display the robot's current movement state and runtime
        telemetry.addData("Runtime", runtime.seconds());
        telemetry.update();
    }

    // Helper method to set all motor powers
    private void setMotorPowers(double power) {
        LF.setPower(power);
        LB.setPower(power);
        RF.setPower(power);
        RB.setPower(power);
    }
}
