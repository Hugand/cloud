package controllers;

import configs.CloudProperties;
import helpers.FileHelpers;
import models.CloudFile;
import models.CloudStorage;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class CloudDirectoryController {
    public CloudDirectoryController() { }

    public Set<CloudFile> getFilesList(String subDir) throws IOException  {
        Set<CloudFile> fileList = new HashSet<>();
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(CloudProperties.DIR + subDir))) {
	        for (Path path : stream) {
                CloudFile cloudFile;
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

                if(path.toFile().isDirectory()) {
                    cloudFile = new CloudFile(
                        path.getFileName().toString(),
                        FileHelpers.getFileSize(path.toFile()),
                        attr.creationTime().toString(),
                        FileHelpers.getType(path.toFile()),
                        "folder"
                    );
                } else {
                    cloudFile = new CloudFile(
                        path.getFileName().toString(),
                        FileHelpers.getFileSize(path.toFile()),
                        attr.creationTime().toString(),
                        FileHelpers.getType(path.toFile()),
                        FileHelpers.getFileCategory(path.toFile())
                    );
                }

                fileList.add(cloudFile);
	        }
	    }

	    return fileList;
    }

    public Set<String> getFoldersList(String dir) throws IOException  {
        Set<String> folderList = new HashSet<>();
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(CloudProperties.DIR + dir))) {
	        for (Path path : stream) {
                String fileType = FileHelpers.getType(path.toFile());
                
                if(fileType.equals("folder")) {
                    String folderName = path.getFileName().toString();
                    folderList.add(folderName);
                }
	        }
	    }
	    return folderList;
    }

    public CloudStorage getAvailableSpaceInBytes() {
        File cloudRoot = new File(CloudProperties.DIR);
        CloudStorage cloudStorage = new CloudStorage((int) FileUtils.sizeOfDirectory(cloudRoot));

        try {
            this.searchDir(cloudStorage, cloudRoot.getPath());
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }

        return cloudStorage;
    }

    public void searchDir(CloudStorage cloudStorage, String dir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
            for (Path path : stream) {
                if(path.toFile().isDirectory()) {
                    searchDir(cloudStorage, path.toFile().getPath());
                } else {
                    String fileCategory = FileHelpers.getFileCategory(path.toFile());
                    cloudStorage.addByCategory(fileCategory, (int) path.toFile().length());
                }
            }
        }

    }

    public boolean deleteFile(String pathDir) {
        File file = new File(CloudProperties.DIR + pathDir);

        return file.delete();
    }



}
