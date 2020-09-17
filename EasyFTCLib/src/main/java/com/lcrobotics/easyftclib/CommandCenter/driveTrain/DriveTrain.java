package com.lcrobotics.easyftclib.CommandCenter.driveTrain;

import com.lcrobotics.easyftclib.tools.ArrayTools;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

public class DriveTrain {

    private WheelType wheelType;
    private DriveMotor[] motors;

    DriveTrain(WheelType wheelType, DcMotor leftMotor, DcMotor rightMotor){
        this.wheelType = wheelType;
        this.motors = new DriveMotor[]{new DriveMotor(leftMotor, WheelPosition.LEFT), new DriveMotor(rightMotor, WheelPosition.RIGHT)};
    }

    // Creates a 4 wheel drive train
    public DriveTrain(WheelType wheelType, DcMotor frontLeftMotor, DcMotor frontRightMotor, DcMotor BackLeftMotor, DcMotor BackRightMotor){
        // Stores what type of wheel (i.e mechanum, omni, etc
        this.wheelType = wheelType;
        // stores the motors in our DriveMotor[], this allows us to know what they are attached to
        this.motors = new DriveMotor[] {
                new DriveMotor(frontLeftMotor, WheelPosition.FRONT_LEFT),
                new DriveMotor(frontRightMotor, WheelPosition.FRONT_RIGHT),
                new DriveMotor(BackLeftMotor, WheelPosition.BACK_LEFT),
                new DriveMotor(BackRightMotor, WheelPosition.BACK_RIGHT)
        };
    }

    public DriveTrain(WheelType wheelType, DriveMotor frontLeftMotor, DriveMotor frontRightMotor, DriveMotor backLeftMotor, DriveMotor backRightMotor){
        // Stores what type of wheel (i.e mechanum, omni, etc
        this.wheelType = wheelType;

        // stores the motors in our DriveMotor[], this allows us to know what they are attached to
        this.motors = new DriveMotor[] { frontLeftMotor,  frontRightMotor,
                                         backLeftMotor,   backRightMotor};
    }

    // Given an array of DcMotors and their motor positions reset drive train
    public void setMotors(DcMotor[] motors, WheelPosition[] motorPos) throws DriveTrainException {

        int numMotors = motors.length;

        if (numMotors != motorPos.length){
            throw new DriveTrainException("setMotors must be passed two arrays of equal length");
        }

        if (numMotors != 2 && numMotors != 4){
            throw new DriveTrainException("There must be either 2 or 4 motors in array");
        }

        // In case of 2 wheel drive, make sure all motors are 2 wheel
        if (numMotors == 2){
            for (int i = 0; i < 2; i++){
                if (!is2Wheel(motorPos[i])){
                    throw new DriveTrainException("4 wheel motor found in 2 wheel array");
                }
            }
        }

        // In case of 4 wheel drive, make sure all motors are 4 wheel
        if (numMotors == 4){
            for (int i = 0; i < 4; i++){
                if (is2Wheel(motorPos[i])){
                    throw new DriveTrainException("2 wheel motor found in 4 wheel array");
                }
            }
        }

        if (ArrayTools.containsDuplicates(motorPos)){
            throw new DriveTrainException("Invalid wheel positions: duplicate wheel positions detected");
        }

        // At this point, all criteria have been satisfied
        // Iterates through and creates new drive motors
        this.motors = new DriveMotor[numMotors];
        for (int i = 0; i < numMotors; i++){
            this.motors[posIndex(motorPos[i])] = new DriveMotor(motors[i], motorPos[i]);
        }

    }

    public boolean is2Wheel(WheelPosition pos){
        return pos == WheelPosition.LEFT || pos == WheelPosition.RIGHT;
    }

    // Maps wheel positions to index of motors array
    // LEFT, RIGHT: 0, 1
    // FRONT_LEFT, FRONT_RIGHT, BACK_LEFT, BACK_RIGHT: 0, 1, 2, 3
    public int posIndex(WheelPosition pos){
        if (is2Wheel(pos)) {
            return pos.ordinal();
        }
        return pos.ordinal()-2;
    }

    public void setPower(float x, float y) throws DriveTrainException {

        if (this.motors.length == 2){
            float leftDrivePow = Range.clip(x + y, -1, 1);
            float rightDrivePow = Range.clip(x - y, -1, 1);
            motors[0].motor.setPower(leftDrivePow);
            motors[1].motor.setPower(rightDrivePow);
        }

        if (this.motors.length == 4) {
            float frontLeftDrivePow = Range.clip(x + y, -1, 1);
            float frontRightDrivePow = Range.clip(x - y, -1, 1);
            float backLeftDrivePow = Range.clip(x + y, -1, 1);
            float backRightDrivePow = Range.clip(x - y, -1, 1);
            motors[0].motor.setPower(frontLeftDrivePow);
            motors[1].motor.setPower(frontRightDrivePow);
            motors[2].motor.setPower(backLeftDrivePow);
            motors[3].motor.setPower(backRightDrivePow);
        }

        throw new DriveTrainException("this.motors must have either 2 or 4 members");
    }
}