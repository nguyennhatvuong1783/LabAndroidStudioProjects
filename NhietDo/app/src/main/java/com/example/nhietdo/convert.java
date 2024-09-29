package com.example.nhietdo;

public class convert {
    private double doF=32.0, doC=0.0;
    public convert() {
    }
    public double getF() {return doF;}
    public double getC() {return doC;}
    public void setF(double doF) {this.doF = doF;}
    public void setC(double doC) {this.doC = doC;}
    public void convertFtoC() {
        doC = (doF - 32) * 5 / 9;
    }
    public void convertCtoF() {
        doF = doC * 9 / 5 + 32;
    }
}
