package org.ugomes.controllers.rest;

import java.io.InputStream;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.File;  // Import the File class
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

public class RenameForm {
    @FormParam("fileDir")
    public String fileDir;
    
    @FormParam("newName")
    public String newName;
}