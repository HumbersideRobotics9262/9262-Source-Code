package frc.robot;
import java.lang.Math;
public class Arm {

    static final double l1 = 5.0;
    static final double l2 = 5.0;

    public static double forwardKinematicsX(double theta1, double theta2) 
    {
        double x = l1 * Math.cos(theta1) + l2 * Math.cos(theta1 + theta2);
        return x;
    }
    public static double forwardKinematicsY(double theta1, double theta2)
    {
        double y = l1 * Math.sin(theta2) + l2 * Math.sin(theta1 + theta2);
        return y;
    }
    

}