package org.ugomes.controllers.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;

import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
// import java.nio.file.Path;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import javax.ws.rs.FormParam;

import org.ugomes.configs.CloudProperties;
import org.ugomes.controllers.CloudDirectoryController;
import org.ugomes.controllers.rest.MultiPartBody;
import org.ugomes.controllers.rest.RenameForm;
import org.ugomes.controllers.rest.MoveForm;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.ugomes.models.CloudFile;

import java.io.InputStream;
import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;


@Path("/cloud_files")
@Produces(MediaType.APPLICATION_JSON)
public class CloudRestController {
    private CloudDirectoryController cloudDirectoryController = new CloudDirectoryController();
    
    @POST
    @Path("/upload/{pathToDir}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadFile(@MultipartForm MultipartFormDataInput upload) {
        String fileName = "";
        

        Map<String, List<InputPart>> uploadForm = upload.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");
        String dirToUpload = this.getDirName(uploadForm);


        for (InputPart inputPart : inputParts) {
         try {
            MultivaluedMap<String, String> header = inputPart.getHeaders();
            fileName = getFileName(header);

            //convert the uploaded file to inputstream
            InputStream inputStream = inputPart.getBody(InputStream.class,null);

            byte [] bytes = IOUtils.toByteArray(inputStream);
                
            //constructs upload file path
            fileName = CloudProperties.dir + "/" + dirToUpload + "/" + fileName;
                
            writeFile(bytes,fileName);
                
            System.out.println("Done");

          } catch (IOException e) {
            e.printStackTrace();
          }

        }
        
        return "done";
    }

    @PUT
    @Path("/rename")
    public String renameFile(@MultipartForm RenameForm renameFormData) throws IOException {
        // File (or directory) with old name
        File original = new File(CloudProperties.dir + renameFormData.fileDir);

        // File (or directory) with new name
        File renamed = new File(CloudProperties.dir + renameFormData.newName);

        if (renamed.exists())
            throw new java.io.IOException("file exists");

        // Rename file (or directory)
        boolean success = original.renameTo(renamed);

        if (!success) {
            return "not renamed";
        }else
            return "success";
    }

    @PUT
    @Path("/move")
    public String moveFile(@MultipartForm MoveForm moveFormData) throws IOException {
        // File (or directory) with old name
        File original = new File(CloudProperties.dir + moveFormData.currDir + moveFormData.fileName);

        // File (or directory) with new name
        File dest = new File(CloudProperties.dir + moveFormData.newDir + moveFormData.fileName);

        if (dest.exists())
            throw new java.io.IOException("file exists");

        // Rename file (or directory)
        boolean success = original.renameTo(dest);

        if (!success) {
            return "not moved";
        }else
            return "success";
    }

    @DELETE
    @Path("/{pathDirToDelete}")
    public Map<String, String> deleteFile(@PathParam String pathDirToDelete) {
        Map<String, String> returnData = new HashMap<>();
        try {
            cloudDirectoryController.deleteFile(pathDirToDelete);
            returnData.put("status", "true");
        } catch (Exception e) {
            returnData.put("status", "false");
        }
        return returnData;
    }

    private String getDirName(Map<String, List<InputPart>>  uploadForm) {

        for (InputPart inputPart: uploadForm.get("dir")) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String dirToUpload = inputPart.getBody(String.class, null);

                return dirToUpload;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    
    //get uploaded filename, is there a easy way in RESTEasy?
    private String getFileName(MultivaluedMap<String, String> header) {

        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        
        for (String filename : contentDisposition) {
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");
                
                String finalFileName = name[1].trim().replaceAll("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    //save to somewhere
    private void writeFile(byte[] content, String filename) throws IOException {

        File file = new File(filename);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();

    }
}
