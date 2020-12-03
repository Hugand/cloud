package org.ugomes.models;

import java.io.InputStream;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.File;  // Import the File class

public class RenameForm {
    @FormParam("fileDir")
    public String fileDir;
    
    @FormParam("newName")
    public String newName;
}