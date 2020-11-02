package org.ugomes.controllers.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import org.ugomes.controllers.CloudDirectoryController;
import org.ugomes.models.CloudFile;

@Path("/cloud_files")
@Produces(MediaType.APPLICATION_JSON)
public class CloudRestController {
    private CloudDirectoryController cloudDirectoryController = new CloudDirectoryController();
    //private Set<CloudFiles> fruits = Collections.newSetFromMap(Collections.synchronizedMap(new LinkedHashMap<>()));

    // public CloudRestController() {
    //     fruits.add(new Fruit("Apple", "Winter fruit"));
    //     fruits.add(new Fruit("Pineapple", "Tropical fruit"));
    // }

    // @GET
    // public Set<Fruit> list() {
    //     return fruits;
    // }

    // @POST
    // public Set<Fruit> add(Fruit fruit) {
    //     fruits.add(fruit);
    //     return fruits;
    // }

    @DELETE
    @Path("/{pathDirToDelete}")
    public Map<String, String> delete(@PathParam String pathDirToDelete) {
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
