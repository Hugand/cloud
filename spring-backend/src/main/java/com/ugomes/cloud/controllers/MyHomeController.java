package com.ugomes.cloud.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.HashMap;
import java.util.Map;
import java.io.File;  // Import the File class

@RestController
public class MyHomeController {

    @Value("${cloud_dir}")
    public String CLOUD_DIR;

    @RequestMapping("/")
    public Map<String, String> myhome(@RequestParam(value = "name", defaultValue = "World") String name){

        File f = new File(CLOUD_DIR);

        String[] pathnames = f.list();

        for (String pathname : pathnames) {
            // Print the names of files and directories
            System.out.println(pathname);
        }

        System.out.println(pathnames);

        HashMap<String, String> map = new HashMap<>();
        map.put("key", "value");
        map.put("foo", "bar");
        map.put("aa", CLOUD_DIR);
        return map;
    }
}