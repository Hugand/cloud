package org.ugomes.controllers.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
import org.ugomes.models.rest_response.GetFoldersInDirResponse;
import org.ugomes.models.rest_response.RestResponse;
import java.util.Set;

@Path("/cloud_files")
@Produces(MediaType.APPLICATION_JSON)
public class CloudRestController {
    private CloudDirectoryController cloudDirectoryController = new CloudDirectoryController();
    
    @GET
    @Path("/getFoldersInDir/{dir}")
    public GetFoldersInDirResponse getFoldersInDir(@PathParam String dir) {
        CloudDirectoryController cdc =  new CloudDirectoryController();

        try {
            Set<String> folderList = cdc.getFoldersList(dir);

            return new GetFoldersInDirResponse("success", folderList);
        } catch(IOException e) {
            System.err.println(e);
            return new GetFoldersInDirResponse("error");
        }
    }

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
        String originalFileDir = CloudProperties.dir + renameFormData.getFileDir() + renameFormData.getPrevName();
        String newFileDir = CloudProperties.dir + renameFormData.getFileDir() + renameFormData.getNewName();
        File original = new File(originalFileDir);
        File renamed = new File(newFileDir);

        if(!original.exists())
            return new RestResponse("error", "FILE_DOESNT_EXIST");

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
    public RestResponse moveFile(@MultipartForm MoveForm moveFormData) {
        moveFormData.print();
        File original = new File(CloudProperties.dir + moveFormData.getCurrDir() + moveFormData.getFileName());
        File dest = new File(CloudProperties.dir + moveFormData.getNewDir() + moveFormData.getFileName());

        if(!original.exists())
            return new RestResponse("error", "FILE_DOESNT_EXIST");

        if (dest.exists())
            return new RestResponse("error", "FILE_ALREADY_EXISTS");

        // Moves the file
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
