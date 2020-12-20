package controllers;

import com.google.gson.Gson;
import configs.CloudProperties;
import controllers.CloudDirectoryController;
import helpers.FileHelpers;
import helpers.StorageHelpers;
import io.smallrye.mutiny.Uni;
import models.CloudStorage;
import models.CreateDirForm;
import models.MoveForm;
import models.RenameForm;
import models.responses.GetFoldersInDirResponse;
import models.responses.RestResponse;
import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Path("/cloud_files")
@Produces(MediaType.APPLICATION_JSON)
public class CloudRestController {
    private final CloudDirectoryController cloudDirectoryController = new CloudDirectoryController();
    private final Gson gson = new Gson();

    public CloudRestController() {
	    System.out.println("Rest controller running.....");
    }

    @GET
    @Path("/rest_test")
    public String restResponse() {
	    return CloudProperties.DIR + "dadas -- " + (new File(CloudProperties.DIR + "./dadas")).exists();
    }
	    

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
    @GET
    @Path("/download/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@PathParam String fileName) {
        File fileToDownload = new File(CloudProperties.DIR + fileName);
        if(fileToDownload.exists())
            return Response.status(200).entity(fileToDownload).build();
        else
            return Response.status(422).build();
    }

    //    @Produces(MediaType.APPLICATION_JSON)
    @POST
    @Path("/mkdir")
    public RestResponse mkdir(String jsonData) {
	   System.out.println(jsonData);
        CreateDirForm cdf = gson.fromJson(jsonData, CreateDirForm.class);
        File newDir = new File(CloudProperties.DIR + cdf.getDirPathName());

        if (newDir.exists())
            return new RestResponse("error" + CloudProperties.DIR + cdf.getDirPathName(), "FILE_ALREADY_EXISTS");

        boolean isSuccessful = newDir.mkdir();
        return new RestResponse(isSuccessful ? "success" : "failed");
    }

    @GET
    @Path("/getSpace")
    public CloudStorage getSpace() {
        return cloudDirectoryController.getAvailableSpaceInBytes();
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public RestResponse uploadFile(@MultipartForm MultipartFormDataInput upload) {
        String dirName = "";
        Map<String, List<InputPart>> uploadForm = upload.getFormDataMap();
        List<InputPart> inputParts = uploadForm.get("file");
        String dirToUpload = URLDecoder.decode(
                FileHelpers.getDirNameFromMultipartFormData(uploadForm), StandardCharsets.UTF_8);

        for (InputPart inputPart : inputParts) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                InputStream inputStream = inputPart.getBody(InputStream.class,null);
                byte [] bytes = IOUtils.toByteArray(inputStream);
                String fileName = URLDecoder.decode(FileHelpers.getFileName(header), StandardCharsets.UTF_8);
                dirName = CloudProperties.DIR + dirToUpload + fileName;

                if(!(new File(dirName)).exists()) {
                    System.out.println(bytes.length);
                    if(bytes.length < StorageHelpers.getCurrentAvailableSpaceInBytes()) {
                        boolean createFileResult = FileHelpers.writeFile(bytes,dirName);
                        return new RestResponse(createFileResult ? "success" : "failed");
                    } else {
                        return new RestResponse("error", "NOT_ENOUGH_SPACE");
                    }
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
        String originalFileDir = CloudProperties.DIR + renameFormData.getFileDir() + renameFormData.getPrevName();
        String newFileDir = CloudProperties.DIR + renameFormData.getFileDir() + renameFormData.getNewName();
        File original = new File(originalFileDir);
        File renamed = new File(newFileDir);

        if(!original.exists())
            return new RestResponse("error", "FILE_DOESNT_EXIST");

        if(renamed.exists())
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
        File original = new File(CloudProperties.DIR + moveFormData.getCurrDir() + moveFormData.getFileName());
        File dest = new File(CloudProperties.DIR + moveFormData.getNewDir() + moveFormData.getFileName());

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
                boolean isSuccessful = cloudDirectoryController.deleteFile(pathDirToDelete);
                return new RestResponse(isSuccessful ? "success" : "failed");
            } catch (Exception e) {
                return new RestResponse("failed");
            }
        else
            return new RestResponse("error", "PATH_INVALID");
    }
}
