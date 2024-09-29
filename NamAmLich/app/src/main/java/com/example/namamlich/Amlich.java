package com.example.namamlich;

public class Amlich {
    private String[] can = {"Canh", "Tân", "Nhâm", "Quý", "Giáp",
    "Ất", "Bính", "Đinh", "Mậu", "Kỷ"};
    private String[] chi = {"Thân", "Dậu", "Tuất", "Hợi", "Tý", "Sửu", "Dần", "Mẹo"
    , "Thìn", "Tỵ", "Ngọ", "Mùi"};
    private int namDL;
    public Amlich(int nam) {namDL = nam;}
    public String getNamAL() {
        String can = this.can[namDL % 10];
        String chi = this.chi[namDL % 12];
        return can + " " + chi;
    }
}
