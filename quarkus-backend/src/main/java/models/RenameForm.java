package models;

import helpers.FileHelpers;

import javax.ws.rs.FormParam;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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

    public String getFileDir() {
        try {
            return URLDecoder.decode(this.fileDir, StandardCharsets.UTF_8.toString());
        }catch(UnsupportedEncodingException e) {
            System.err.println(e);
        }

        return "";
    }

    public String getPrevName() {
        try {
            return URLDecoder.decode(this.prevName, StandardCharsets.UTF_8.toString());
        }catch(UnsupportedEncodingException e) {
            System.err.println(e);
        }

        return "";
    }

    public String getNewName() {
        try {
            String decodedNewName = URLDecoder.decode(this.newName, StandardCharsets.UTF_8.toString());

            if(decodedNewName.split("[.]").length <= 1 && this.getPrevName().split("[.]").length > 1)
                return decodedNewName + "." +
                        FileHelpers.getFileExtension(this.getPrevName()); // filename + . + file_extension
            else
                return decodedNewName;
        }catch(UnsupportedEncodingException e) {
            System.err.println(e);
        }

        return "";
    }
}