package com.example.bt05;

public class Music {
    private String name, location;
    private int btnPlay;

    public Music(String name, String location, int btnPlay) {
        this.name = name;
        this.location = location;
        this.btnPlay = btnPlay;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public int getBtnPlay() {
        return btnPlay;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setBtnPlay(int btnPlay) {
        this.btnPlay = btnPlay;
    }

}
