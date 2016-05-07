package com.example.tiferet.polerbear.Repository.Local;

import java.util.List;

/**
 * Created by TIFERET on 27-Mar-16.
 */
public class Trick {
    int trickId;
    String trickName;
    int trickLevel;
    List<Trick> required;
    String picRef;

    public Trick(int trickId, String trickName, int trickLevel, List<Trick> required, String picRef) {
        this.trickId = trickId;
        this.trickName = trickName;
        this.trickLevel = trickLevel;
        this.required = required;
        this.picRef = picRef;
    }

    public int getTrickId() {
        return trickId;
    }

    public void setTrickId(int trickId) {
        this.trickId = trickId;
    }

    public String getTrickName() {
        return trickName;
    }

    public void setTrickName(String trickName) {
        this.trickName = trickName;
    }

    public int getTrickLevel() {
        return trickLevel;
    }

    public void setTrickLevel(int trickLevel) {
        this.trickLevel = trickLevel;
    }

    public List<Trick> getRequired() {
        return required;
    }

    public void setRequired(List<Trick> required) {
        this.required = required;
    }

    public String getPicRef() {
        return picRef;
    }

    public void setPicRef(String picRef) {
        this.picRef = picRef;
    }
}