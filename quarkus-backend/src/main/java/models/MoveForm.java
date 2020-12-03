package org.ugomes.models;

import java.io.InputStream;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.File;  // Import the File class

public class MoveForm {
    @FormParam("currDir")
    public String currDir;
    
    @FormParam("fileName")
    public String fileName;
    
    @FormParam("newDir")
    public String newDir;
}