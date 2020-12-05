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

                if(!(new File(fileName)).exists()) {
                    FileHelpers.writeFile(bytes,fileName);
                    return new RestResponse("success");
                } else {
                    return new RestResponse("error", "FILE_ALREADY_EXISTS");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return new RestResponse("failed");
            }
        }

        return new RestResponse("failed");
    }

    @PUT
    @Path("/rename")
    public RestResponse renameFile(@MultipartForm RenameForm renameFormData) {
        File original = new File(CloudProperties.dir + renameFormData.getDecodedFileDir() + renameFormData.getDecodedPrevName());
        File renamed = new File(CloudProperties.dir + renameFormData.getDecodedFileDir() + renameFormData.getDecodedNewName());

        System.out.println(renameFormData.getAsString());
        System.out.println(CloudProperties.dir + renameFormData.getDecodedFileDir() + renameFormData.getDecodedPrevName());
        System.out.println(CloudProperties.dir + renameFormData.getDecodedFileDir() + renameFormData.getDecodedNewName());
        System.out.println(original);
        System.out.println(renamed);
        

        if (renamed.exists())
            return new RestResponse("error", "FILE_ALREADY_EXISTS");

        boolean success = original.renameTo(renamed);

        if (!success) {
            return new RestResponse("failed");
        }else
            return new RestResponse("success");
    }

    @PUT
    @Path("/move")
    public RestResponse moveFile(@MultipartForm MoveForm moveFormData) throws IOException {
        File original = new File(CloudProperties.dir + moveFormData.currDir + moveFormData.fileName);
        File dest = new File(CloudProperties.dir + moveFormData.newDir + moveFormData.fileName);

        if (dest.exists())
            return new RestResponse("error", "FILE_ALREADY_EXISTS");

        boolean success = original.renameTo(dest);

        if (!success) {
            return new RestResponse("failed");
        }else
            return new RestResponse("success");
    }

    @DELETE
    @Path("/delete/{pathDirToDelete}")
    public RestResponse deleteFile(@PathParam String pathDirToDelete) {
        if(!pathDirToDelete.isBlank())
            try {
                cloudDirectoryController.deleteFile(pathDirToDelete);
                return new RestResponse("success");
            } catch (Exception e) {
                return new RestResponse("failed");
            }
        else
            return new RestResponse("error", "PATH_INVALID");
    }
}
