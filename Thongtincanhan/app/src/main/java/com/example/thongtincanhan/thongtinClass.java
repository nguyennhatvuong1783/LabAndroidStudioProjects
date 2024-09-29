package com.example.thongtincanhan;

public class thongtinClass {
    private String hoten, cmnd, sothich, trinhdo;
    public thongtinClass(String hoten, String cmnd,
                         String sothich, String trinhdo)
    {
        setThongtin(hoten, cmnd, sothich, trinhdo);
    }
    public void setThongtin(String hoten, String cmnd,String sothich, String trinhdo)
    {
        this.hoten = hoten;
        this.cmnd = cmnd;
        this.sothich = sothich;
        this.trinhdo = trinhdo;
    }
    @Override
    public String toString()
    {
        String s="";
        s += "Xin chào "+ hoten +", trình độ: " + trinhdo + ", sở thích: " + sothich;
        return s;
    }
}
