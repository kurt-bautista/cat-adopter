package com.pelaez.bautista.catadopter;

/**
 * Created by AUD_SITE on 7/21/2016.
 */
public class Request {

    private String uploader;
    private String requester;

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    private String cat;

    public Request(){

    }
    public Request(String uploader, String requester, String cat){
        this.uploader=uploader;
        this.requester=requester;
        this.cat=cat;

    }





}
