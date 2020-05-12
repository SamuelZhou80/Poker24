package com.samuel.main.eco;

/**
 * 车辆的行驶工况数据
 * 
 * @author Administrator
 *
 */
public class CarDriveData {
    private String timeStr;
    private double speed;
    private double prevSpeed; // 前一秒的速度
    private int act;
    private int rotateSpeed;
    private double oss;
    private double slope; // 坡度

    private double weight;

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getPrevSpeed() {
        return prevSpeed;
    }

    public void setPrevSpeed(double prevSpeed) {
        this.prevSpeed = prevSpeed;
    }

    public int getAct() {
        return act;
    }

    public void setAct(int act) {
        this.act = act;
    }

    public int getRotateSpeed() {
        return rotateSpeed;
    }

    public void setRotateSpeed(int rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }

    public double getOss() {
        return oss;
    }

    public void setOss(double oss) {
        this.oss = oss;
    }

    public double getSlope() {
        return slope;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "[timeStr=" + timeStr + ", weight=" + weight + ", speed=" + speed
                + ", prevSpeed=" + prevSpeed + ", act=" + act + ", rotateSpeed=" + rotateSpeed
                + ", oss=" + oss + ", slope=" + slope + "]";
    }

}
