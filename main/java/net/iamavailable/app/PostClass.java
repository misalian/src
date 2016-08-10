package net.iamavailable.app;

import java.io.Serializable;

/**
 * Created by Arshad on 3/21/2016.
 */
public class PostClass implements Serializable {

    String id;
    String username;
    String frontpic;
    String backpic;
    String message;
    String tstart;
    String tend;
    String date;
    String lat;
    String lang;
    String token;
    String statusmood;
    String email;
    String imenumber;
    String city;
    String numlikes;
    String numstars;
    String country;
    String likes;
    String dollars;
    String frontpicpath;

    public java.lang.String getFrontpicpath() {
        return frontpicpath;
    }

    public void setFrontpicpath(java.lang.String frontpicpath) {
        this.frontpicpath = frontpicpath;
    }

    public java.lang.String getBackpicpath() {
        return backpicpath;
    }

    public void setBackpicpath(java.lang.String backpicpath) {
        this.backpicpath = backpicpath;
    }

    String backpicpath;
    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDollars() {
        return dollars;
    }

    public void setDollars(String dollars) {
        this.dollars = dollars;
    }

    public void setNumstars(String numstars) {
        this.numstars = numstars;
    }

    public String getNumstars() {
        return numstars;
    }


    public String getNumlikes() {
        return numlikes;
    }

    public void setNumlikes(String numlikes) {
        this.numlikes = numlikes;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getImenumber() {
        return imenumber;
    }

    public void setImenumber(String imenumber) {
        this.imenumber = imenumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatusmood() {
        return statusmood;
    }

    public void setStatusmood(String statusmood) {
        this.statusmood = statusmood;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFrontpic() {
        return frontpic;
    }

    public void setFrontpic(String frontpic) {
        this.frontpic = frontpic;
    }

    public String getBackpic() {
        return backpic;
    }

    public void setBackpic(String backpic) {
        this.backpic = backpic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTstart() {
        return tstart;
    }

    public void setTstart(String tstart) {
        this.tstart = tstart;
    }

    public String getTend() {
        return tend;
    }

    public void setTend(String tend) {
        this.tend = tend;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
