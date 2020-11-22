package org.ugomes.controllers.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.ugomes.configs.CloudProperties;
import org.ugomes.controllers.CloudDirectoryController;
import org.ugomes.helpers.FileHelpers;
import org.ugomes.models.MoveForm;
import org.ugomes.models.RenameForm;

import org.ugomes.models.rest_response.RestResponse;

@Path("/cloud_files")
@Produces(MediaType.APPLICATION_JSON)
public class CloudRestController {
    private CloudDirectoryController cloudDirectoryController = new CloudDirectoryController();
    
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public RestResponse uploadFile(@MultipartForm MultipartFormDataInput upload) {
        String fileName = "";
        Map<String, List<InputPart>> uploadForm = upload.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");
        String dirToUpload = FileHelpers.getDirNameFromMultipartFormData(uploadForm);
        

        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                InputStream inputStream = inputPart.getBody(InputStream.class,null);
                byte [] bytes = IOUtils.toByteArray(inputStream);
                fileName = CloudProperties.dir + "/" + dirToUpload + "/" + FileHelpers.getFileName(header);

                if(!dirToUpload.isBlank() && !(new File(fileName)).exists()) {
                    FileHelpers.writeFile(bytes,fileName);
                    return new RestResponse(true);
                } else {
                    return new RestResponse(false);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new RestResponse(false);
            }
        }

        return new RestResponse(false);
    }

    @PUT
    @Path("/rename")
    public String renameFile(@MultipartForm RenameForm renameFormData) throws IOException {
        File original = new File(CloudProperties.dir + renameFormData.fileDir);
        File renamed = new File(CloudProperties.dir + renameFormData.newName);

        if (renamed.exists())
            throw new java.io.IOException("file exists");

        boolean success = original.renameTo(renamed);

        if (!success) {
            return "not renamed";
        }else
            return "success";
    }

    @PUT
    @Path("/move")
    public String moveFile(@MultipartForm MoveForm moveFormData) throws IOException {
        File original = new File(CloudProperties.dir + moveFormData.currDir + moveFormData.fileName);
        File dest = new File(CloudProperties.dir + moveFormData.newDir + moveFormData.fileName);

        if (dest.exists())
            throw new java.io.IOException("file exists");

        boolean success = original.renameTo(dest);

        if (!success) {
            return "not moved";
        }else
            return "success";
    }

    @DELETE
    @Path("/delete/{pathDirToDelete}")
    public Map<String, String> deleteFile(@PathParam String pathDirToDelete) {
        System.out.println(pathDirToDelete);
        Map<String, String> returnData = new HashMap<>();
        try {
            cloudDirectoryController.deleteFile(pathDirToDelete);
            returnData.put("status", "true");
        } catch (Exception e) {
            returnData.put("status", "false");
        }
        return returnData;
    }
}
