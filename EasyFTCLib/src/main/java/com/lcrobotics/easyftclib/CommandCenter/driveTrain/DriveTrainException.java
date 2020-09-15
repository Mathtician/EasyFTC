package com.lcrobotics.easyftclib.CommandCenter.driveTrain;

// DriveTrain must have either 2 motors labeled LEFT and RIGHT or 4 motors with appropriate labels
// This custom exception is thrown when that isn't the case
public class DriveTrainException extends Exception {
    public DriveTrainException(String errorMessage){
        super(errorMessage);
    }
}
