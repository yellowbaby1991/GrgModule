package com.grg.face.bean;


public class YuvData {


    /**
     * 相机yuv视频流
     */
    byte[] yuvs;

    /**
     * 相机参数format
     */
    int format;

    /**
     * 旋转角度
     */
    int cameraRotate;

    /**
     * 人脸坐标
     */
    int[] location;

    /**
     * 相机argb视频流
     */
    int[] priview;

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }

    public byte[] getYuvs() {
        return yuvs;
    }

    public void setYuvs(byte[] yuvs) {
        this.yuvs = yuvs;
    }


    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int[] getPriview() {
        return priview;
    }

    public void setPriview(int[] priview) {
        this.priview = priview;
    }

    public int getCameraRotate() {
        return cameraRotate;
    }

    public void setCameraRotate(int cameraRotate) {
        this.cameraRotate = cameraRotate;
    }
}
