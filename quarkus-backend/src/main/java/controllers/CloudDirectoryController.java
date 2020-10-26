package org.ugomes.controllers;

import java.util.Map;
import java.util.ArrayList;
import java.io.File;
import org.ugomes.configs.CloudProperties;
// import org.eclipse.microprofile.config.inject.ConfigProperty;
import javax.inject.Inject;

public class CloudDirectoryController {
    public CloudDirectoryController() { 
    }

    @Inject
    CloudProperties cloud = new CloudProperties();

    public String[] getFilesList() {
        File cloudDirFiles = new File(CloudProperties.dir);

        String[] pathnames = cloudDirFiles.list();

        // for (String pathname : pathnames) {
        //     // Print the names of files and directories
        //     System.out.println(pathname);
        // }

        // HashMap<String, String> map = new HashMap<>();
        // map.put("key", "value");
        // map.put("foo", "bar");
        // map.put("aa", CLOUD_DIR);
        

        return pathnames;
    }
}
