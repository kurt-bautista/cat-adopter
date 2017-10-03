package com.pelaez.bautista.catadopter;

/**
 * Created by kurtv on 7/21/2016.
 *
 * Cat object
 */
public class Cat {

    private String name;
    private String uploaderID;
    private String sex;
    private boolean neutered;
    private String dateCreated;
    private String lastUpdated;

    public Cat() {

    }

    public Cat(String n, String u, String s, boolean ne, String d, String l) {
        name = n;
        uploaderID = u;
        sex = s;
        neutered = ne;
        dateCreated = d;
        lastUpdated = l;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUploaderID() {
        return uploaderID;
    }

    public void setUploaderID(String uploaderID) {
        this.uploaderID = uploaderID;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public boolean getNeutered() {
        return neutered;
    }

    public void setNeutered(boolean neutered) {
        this.neutered = neutered;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

}
