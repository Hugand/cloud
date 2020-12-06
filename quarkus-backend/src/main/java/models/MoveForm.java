package org.ugomes.models;

import java.io.InputStream;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.File;  // Import the File class
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;


public class MoveForm {
    @FormParam("currDir")
    public String currDir;
    
    @FormParam("fileName")
    public String fileName;
    
    @FormParam("newDir")
    public String newDir;

    public void print() {
        System.out.println(fileName + " " + currDir + " " + newDir);
        System.out.println(this.getFileName());
        // System.out.println(this.getFileName() + " " + this.getCurrDir() + " " + this.getNewDir());
    }

    public String getCurrDir() {
        try {
            return URLDecoder.decode(this.currDir, StandardCharsets.UTF_8.toString());
        }catch(UnsupportedEncodingException e) {
            System.err.println(e);
        }

        return "";
    }

    public String getNewDir() {
        try {
            return URLDecoder.decode(this.newDir, StandardCharsets.UTF_8.toString());
        }catch(UnsupportedEncodingException e) {
            System.err.println(e);
        }

        return "";
    }

    public String getFileName() {
        try {
            return URLDecoder.decode(this.fileName, StandardCharsets.UTF_8.toString());
        }catch(UnsupportedEncodingException e) {
            System.err.println(e);
        }

        return "";
    }
}