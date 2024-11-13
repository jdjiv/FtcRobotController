package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name = "Deep")
public class Deep extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our main motors for mecanum drive
        DcMotor LF = hardwareMap.dcMotor.get("LF");  // Left Front
        DcMotor LB = hardwareMap.dcMotor.get("LB");  // Left Back
        DcMotor RF = hardwareMap.dcMotor.get("RF");  // Right Front
        DcMotor RB = hardwareMap.dcMotor.get("RB");  // Right Back

        // Reverse the right side motors. This may be wrong for your setup.
        LF.setDirection(DcMotorSimple.Direction.REVERSE);
        LB.setDirection(DcMotorSimple.Direction.REVERSE);

        // Declare additional motors and servos for the second controller
        DcMotor OT = hardwareMap.dcMotor.get("OT");  // Other motor (e.g., shooter)
        DcMotor IT = hardwareMap.dcMotor.get("IT");  // Intake motor
        DcMotor A = hardwareMap.dcMotor.get("A");   // Arm motor

        // Declare servos
        Servo HC = hardwareMap.servo.get("HC");  // Hook/Climber servo
        Servo IP = hardwareMap.servo.get("IP");  // Intake Position servo

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // Mecanum drive control using the left and right sticks on gamepad1
            double y = -gamepad1.left_stick_y; // Forward/backward movement (reversed)
            double x = gamepad1.left_stick_x * 1.1; // Strafing left/right (with compensation)
            double rx = gamepad1.right_stick_x; // Rotation (clockwise/counterclockwise)

            // Denominator for normalization of motor power
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            // Set power for the mecanum drive motors
            LF.setPower(frontLeftPower);
            LB.setPower(backLeftPower);
            RF.setPower(frontRightPower);
            RB.setPower(backRightPower);

            // Control additional motors and servos with gamepad2

            // Control intake motor (IT)
            if (gamepad2.b) {
                IT.setPower(1);  // Intake in (full power)
            } else if (gamepad2.x) {
                IT.setPower(-1); // Intake out (reverse power)
            } else {
                IT.setPower(0);  // Stop intake
            }

            // Control OT motor using triggers
            // Left trigger controls reverse direction, right trigger controls forward direction
            double otPower = 0;
            if (gamepad2.left_trigger > 0) {
                otPower = -gamepad2.left_trigger; // Reverse direction, proportional to left trigger
            } else if (gamepad2.right_trigger > 0) {
                otPower = gamepad2.right_trigger; // Forward direction, proportional to right trigger
            }
            OT.setPower(otPower);

            // If the "A" button is pressed, set OT motor to fixed power of -0.4
            if (gamepad2.a) {
                OT.setPower(-0.4);  // Set OT motor to fixed power of -0.4
            }

            // Control arm motor (A) - Using right thumbstick for vertical control
            double armPower = -gamepad2.right_stick_y; // Reverse for correct direction
            A.setPower(armPower);

            // Control servos
            if (gamepad2.dpad_up) {
                HC.setPosition(1);  // Move HC servo to position 1 (e.g., hook/climber)
            } else if (gamepad2.dpad_down) {
                HC.setPosition(0.5);  // Move HC servo to position 0 (e.g., stowed)
            }

            // Set the IP servo to just above the ground
            if (gamepad2.right_bumper)
            IP.setPosition(0.3);  // Adjust this value if needed for your setup

            if (gamepad2.dpad_right) {
                IP.setPosition(0);  // Move intake position servo to position 0 (e.g., stowed)
            } else if (gamepad2.dpad_left) {
                IP.setPosition(1);  // Move intake position servo to position 1 (e.g., active position)
            }
        }
    }
}
