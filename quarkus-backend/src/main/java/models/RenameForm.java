package org.ugomes.models;

import java.io.InputStream;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.File;  // Import the File class
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;

public class RenameForm {
    @FormParam("fileDir")
    public String fileDir;

    @FormParam("prevName")
    public String prevName;
    
    @FormParam("newName")
    public String newName;

    public String getAsString() {
        return fileDir + " " + prevName + " " + newName;
    }

    

    public String getDecodedFileDir() {
        try {
            return URLDecoder.decode(this.fileDir, StandardCharsets.UTF_8.toString());
        }catch(UnsupportedEncodingException e) {
            System.err.println(e);
        }

        return "";
    }

    public String getDecodedPrevName() {
        try {
            return URLDecoder.decode(this.prevName, StandardCharsets.UTF_8.toString());
        }catch(UnsupportedEncodingException e) {
            System.err.println(e);
        }

        return "";
    }

    public String getDecodedNewName() {
        try {
            return URLDecoder.decode(this.newName, StandardCharsets.UTF_8.toString());
        }catch(UnsupportedEncodingException e) {
            System.err.println(e);
        }

        return "";
    }
}