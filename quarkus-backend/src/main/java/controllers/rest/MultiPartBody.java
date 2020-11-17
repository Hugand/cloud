package org.ugomes.controllers.rest;

import java.io.InputStream;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;
import java.io.File;  // Import the File class
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

public class MultiPartBody {

    @FormParam("file")
    public File file;
    // @PartType(MediaType.APPLICATION_OCTET_STREAM)
    // public InputStream file;

    // @FormParam("fileName")
    // @PartType(MediaType.TEXT_PLAIN)
    // public String fileName;
}