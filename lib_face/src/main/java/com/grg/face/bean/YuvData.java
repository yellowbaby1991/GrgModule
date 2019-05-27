package com.grg.face.bean;

public class YuvData {

    byte[] yuvs;

    int type = 0;

    int format;

    int cameraRotate;

    int[] location;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
