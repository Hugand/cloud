package controllers;

import java.io.File;
import configs.CloudProperties;
import java.util.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.io.IOException;

public class CloudDirectoryController {
    public CloudDirectoryController() { }

    public Set<Map<String, String>> getFilesList(String subDir) throws IOException  {
        Set<Map<String, String>> fileList = new HashSet<>();
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(CloudProperties.dir + subDir))) {
	        for (Path path : stream) {
                Map<String, String> fileData = new HashMap<>();
                BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);

                fileData.put("file_name", path.getFileName().toString());
                fileData.put("file_size", getFileSize(path.toFile()));
                fileData.put("file_created_at", attr.creationTime().toString());
                fileData.put("type", this.getFileType(path.toFile()));
                fileList.add(fileData);
	        }
	    }
	    return fileList;
    }

    public Set<String> getFoldersList(String dir) throws IOException  {
        Set<String> folderList = new HashSet<>();
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(CloudProperties.dir + dir))) {
	        for (Path path : stream) {
                String fileType = this.getFileType(path.toFile());
                
                if(fileType.equals("folder")) {
                    String folderName = path.getFileName().toString();
                    folderList.add(folderName);
                }
	        }
	    }
	    return folderList;
    }

    public boolean deleteFile(String pathDir) {
        File file = new File(CloudProperties.dir + pathDir);

        return file.delete();
    }

    private String getFileType(File file) {
        return file.isDirectory() ? "folder" : "file";
    }

	private static long getFileSizeMegaBytes(File file) {
		return (long) ((double) file.length() / (1024.0 * 1024.0));
	}
	
	private static long getFileSizeKiloBytes(File file) {
		return (long) ((double) file.length() / 1024.0);
	}

	private static long getFileSizeBytes(File file) {
		return file.length();
    }
    
    private static String getFileSize(File file) {
        if(file.length() < 1024)
            return getFileSizeBytes(file) + "B";
        else if(getFileSizeKiloBytes(file) < 1024)
            return getFileSizeKiloBytes(file) + "KB";
        else 
            return getFileSizeMegaBytes(file) + "MB";
    }
}
