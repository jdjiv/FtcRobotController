package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Strafe")
public class Strafe extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Update motor names to match your configuration (RF, LB, LF, RB)
        DcMotor LF = hardwareMap.dcMotor.get("LF");  // Left Front
        DcMotor LB = hardwareMap.dcMotor.get("LB");  // Left Back
        DcMotor RF = hardwareMap.dcMotor.get("RF");  // Right Front
        DcMotor RB = hardwareMap.dcMotor.get("RB");  // Right Back

        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        LF.setDirection(DcMotorSimple.Direction.REVERSE);
        LB.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            // Get joystick inputs for movement and rotation
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

            // Calculate the motor powers for mecanum drive
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            // Set the motor power values to the motors
            LF.setPower(frontLeftPower);
            LB.setPower(backLeftPower);
            RF.setPower(frontRightPower);
            RB.setPower(backRightPower);
        }
    }
}
