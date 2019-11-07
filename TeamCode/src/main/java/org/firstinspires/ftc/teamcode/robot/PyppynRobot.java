package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

import static java.lang.Thread.sleep;

public class PyppynRobot implements Robot {

    public static final double JOYSTICK_THRESHOLD = 0.13;
    public static final double JOYSTICK_THRESHOLD_SQUARED = Math.pow(JOYSTICK_THRESHOLD, 2);
    public static final double MAX_LIFT_SPEED = 0.5;
    public static final double MAX_DRIVE_SPEED = 0.8;
    public static final double MIN_DRIVE_SPEED = -0.8;
    public static final double SLOW_MODE_SPEED = 0.3;
    public static final double SPIN_SPEED = 0.5;
    public static final double SLOW_MODE_SPIN_SPEED = 0.3;
    public static final double MAX_CLAW_SPEED = 0.3;
    public static final BNO055IMU.AngleUnit INTERNAL_ANGLE_UNIT = BNO055IMU.AngleUnit.RADIANS;
    public static final AngleUnit REPORTING_ANGLE_UNIT = AngleUnit.RADIANS;
    public static final double WHEEL_DIAMETER = 2.75;
    public static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;

    HardwareMap hardwareMap;

    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;

    public DcMotor lift;

    public DcMotor claw;
    public DcMotor leftSpinner;
    public DcMotor rightSpinner;

    public Servo clawServo;

    public BNO055IMU imu;

    private double referenceAngle = 0.0;

    private boolean clawOpen = false;

    public PyppynRobot(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;

        frontLeft = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft = hardwareMap.get(DcMotor.class, "back_left");
        backRight = hardwareMap.get(DcMotor.class, "back_right");

        lift = hardwareMap.get(DcMotor.class, "lift");

        claw = hardwareMap.get(DcMotor.class, "claw");

        leftSpinner = hardwareMap.get(DcMotor.class, "left_spinner");
        rightSpinner = hardwareMap.get(DcMotor.class, "right_spinner");


        clawServo = hardwareMap.get(Servo.class, "claw_servo");

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.FORWARD);
        backRight.setDirection(DcMotorSimple.Direction.FORWARD);

        lift.setDirection(DcMotorSimple.Direction.FORWARD);

        leftSpinner.setDirection(DcMotorSimple.Direction.FORWARD);
        rightSpinner.setDirection(DcMotorSimple.Direction.REVERSE);

        claw.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        claw.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftSpinner.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightSpinner.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        clawServo.setPosition(0.62);

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = INTERNAL_ANGLE_UNIT;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu.initialize(parameters);

        this.introduceSelf(telemetry);
    }

    public void calibrateIMU() throws InterruptedException {
        while (!imu.isSystemCalibrated()) {
            sleep(50);
        }
        referenceAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, REPORTING_ANGLE_UNIT).firstAngle;
    }

    public boolean isClawOpen() {
        return clawOpen;
    }

    public void setClawOpen(boolean clawOpen) {
        if (clawOpen) {
            clawServo.setPosition(0.92);
        } else {
            clawServo.setPosition(0.76);
        }

        this.clawOpen = clawOpen;
    }

    public void rotateClockwise(double power) {
        frontLeft.setPower(power);
        frontRight.setPower(power);
        backLeft.setPower(power);
        backRight.setPower(power);
    }

    public void rotateCounterclockwise(double power) {
        frontLeft.setPower(-power);
        frontRight.setPower(-power);
        backLeft.setPower(-power);
        backRight.setPower(-power);
    }

    public void slowRotateClockwise(double power) {
        frontLeft.setPower(power);
        backRight.setPower(power);
    }

    public void slowRotateCounterclockwise(double power) {
        frontLeft.setPower(-power);
        backRight.setPower(-power);
    }

    public void strafeRight(double power) {
        frontLeft.setPower(power);
        backLeft.setPower(-power);
        frontRight.setPower(power);
        backRight.setPower(-power);
    }

    public void strafeLeft(double power) {
        frontLeft.setPower(-power);
        backLeft.setPower(power);
        frontRight.setPower(-power);
        backRight.setPower(power);
    }

    public void straightDrive(double leftPower, double rightPower) {
        frontLeft.setPower(leftPower);
        backLeft.setPower(leftPower);
        frontRight.setPower(-rightPower);
        backRight.setPower(-rightPower);
    }

    public void lift(double power) {
        lift.setPower(power);
    }

    public void stop() {
        frontLeft.setPower(0.0);
        backLeft.setPower(0.0);
        frontRight.setPower(0.0);
        backRight.setPower(0.0);
    }

    public void moveClaw(double power) {
        claw.setPower(power);
    }

    public void nomNomNom(double power) {
        leftSpinner.setPower(power);
        rightSpinner.setPower(power);
    }

    public void rotateXTicks(double speed, int ticks, double timeoutS, boolean opModeIsActive) {
        // Determine new target position, and pass to motor controller
        int frontLeftTarget = frontLeft.getCurrentPosition() + ticks;
        int backLeftTarget = backLeft.getCurrentPosition() + ticks;
        int frontRightTarget = frontLeft.getCurrentPosition() + ticks;
        int backRightTarget = backLeft.getCurrentPosition() + ticks;

        frontLeft.setTargetPosition(frontLeftTarget);
        backLeft.setTargetPosition(backLeftTarget);
        frontRight.setTargetPosition(frontRightTarget);
        backRight.setTargetPosition(backRightTarget);

        // Turn On RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        ElapsedTime runtime = new ElapsedTime();
        runtime.reset();

        backLeft.setPower(Math.abs(speed));
        backRight.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.

        while (opModeIsActive && (runtime.seconds() < timeoutS) && (frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy())) {

        }

        // Stop all motion;
        stop();

        // Turn off RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public void rotateXTicks(double speed, int ticks, double timeoutS, boolean opModeIsActive, Runnable doWhileRunning) {
        // Determine new target position, and pass to motor controller
        int frontLeftTarget = frontLeft.getCurrentPosition() + ticks;
        int backLeftTarget = backLeft.getCurrentPosition() + ticks;
        int frontRightTarget = frontLeft.getCurrentPosition() + ticks;
        int backRightTarget = backLeft.getCurrentPosition() + ticks;

        frontLeft.setTargetPosition(frontLeftTarget);
        backLeft.setTargetPosition(backLeftTarget);
        frontRight.setTargetPosition(frontRightTarget);
        backRight.setTargetPosition(backRightTarget);

        // Turn On RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        ElapsedTime runtime = new ElapsedTime();
        runtime.reset();

        backLeft.setPower(Math.abs(speed));
        backRight.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.

        while (opModeIsActive && (runtime.seconds() < timeoutS) && (frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy())) {
            doWhileRunning.run();
        }

        // Stop all motion;
        stop();

        // Turn off RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void driveXTicks(double speed, int leftTicks, int rightTicks, double timeoutS, boolean opModeIsActive) {
        // Determine new target position, and pass to motor controller
        int frontLeftTarget = frontLeft.getCurrentPosition() + leftTicks;
        int backLeftTarget = backLeft.getCurrentPosition() + leftTicks;
        int frontRightTarget = frontLeft.getCurrentPosition() - rightTicks;
        int backRightTarget = backLeft.getCurrentPosition() - rightTicks;

        frontLeft.setTargetPosition(frontLeftTarget);
        backLeft.setTargetPosition(backLeftTarget);
        frontRight.setTargetPosition(frontRightTarget);
        backRight.setTargetPosition(backRightTarget);

        // Turn On RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        ElapsedTime runtime = new ElapsedTime();
        runtime.reset();

        backLeft.setPower(Math.abs(speed));
        backRight.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.

        while (opModeIsActive && (runtime.seconds() < timeoutS) && (frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy())) {

        }

        // Stop all motion;
        stop();

        // Turn off RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }


    public void driveXTicks(double speed, int leftTicks, int rightTicks, double timeoutS, boolean opModeIsActive, Runnable doWhileRunning) {
        // Determine new target position, and pass to motor controller
        int frontLeftTarget = frontLeft.getCurrentPosition() + leftTicks;
        int backLeftTarget = backLeft.getCurrentPosition() + leftTicks;
        int frontRightTarget = frontLeft.getCurrentPosition() - rightTicks;
        int backRightTarget = backLeft.getCurrentPosition() - rightTicks;

        frontLeft.setTargetPosition(frontLeftTarget);
        backLeft.setTargetPosition(backLeftTarget);
        frontRight.setTargetPosition(frontRightTarget);
        backRight.setTargetPosition(backRightTarget);

        // Turn On RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // reset the timeout time and start motion.
        ElapsedTime runtime = new ElapsedTime();
        runtime.reset();

        backLeft.setPower(Math.abs(speed));
        backRight.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.

        while (opModeIsActive && (runtime.seconds() < timeoutS) && (frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy())) {
            doWhileRunning.run();
        }

        // Stop all motion;
        stop();

        // Turn off RUN_TO_POSITION
        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }



    @Override
    public String getName() {
        return "Pyppyn";
    }

    @Override
    public void introduceSelf(Telemetry telemetry) {
        telemetry.addData("Hello!", this.getName() + " reporting for duty!");
        telemetry.update();
    }
}
