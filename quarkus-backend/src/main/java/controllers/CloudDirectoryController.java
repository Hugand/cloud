package controllers;

import configs.CloudProperties;
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
                        getFileSize(path.toFile()),
                        attr.creationTime().toString(),
                        this.getType(path.toFile())
                    );
                } else {
                    cloudFile = new CloudFile(
                        path.getFileName().toString(),
                        getFileSize(path.toFile()),
                        attr.creationTime().toString(),
                        this.getType(path.toFile()),
                        this.getFileCategory(path.toFile())
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
                String fileType = this.getType(path.toFile());
                
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

        System.out.println(getFileSize(FileUtils.sizeOfDirectory(cloudRoot)));
        System.out.println(cloudRoot.getName());
        System.out.println(cloudRoot.getPath());
//        System.out.println((new File(cloudRoot.getPath() + "/test")).exists());

        try {
            this.searchDir(cloudStorage, cloudRoot.getPath());

            System.out.println("Image: " + getFileSize(cloudStorage.getStorageInImages()));
            System.out.println("Video: " + getFileSize(cloudStorage.getStorageInVideos()));
            System.out.println("Audio: " + getFileSize(cloudStorage.getStorageInAudio()));
            System.out.println("Docs: " + getFileSize(cloudStorage.getStorageInDocs()));
            System.out.println("Others: " + getFileSize(cloudStorage.getStorageInOthers()));
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
                    String fileCategory = this.getFileCategory(path.toFile());
                    cloudStorage.addByCategory(fileCategory, (int) path.toFile().length());
                }
            }
        }

    }

    public boolean deleteFile(String pathDir) {
        File file = new File(CloudProperties.DIR + pathDir);

        return file.delete();
    }

    private String getType(File file) {
        return file.isDirectory() ? "folder" : "file";
    }

    private String getFileCategory(File file) {
        String fileExtension = this.getFileExtension(file);

        switch (fileExtension.toLowerCase()) {
            case "png":
            case "jpg":
            case "jpeg":
            case "svg":
            case "bmp":
            case "gif":
            case "tiff":
                return "image";

            case "webm":
            case "mpg":
            case "mp2":
            case "mpeg":
            case "mp4":
            case "m4p":
            case "m4v":
            case "avi":
            case "wmv":
            case "mov":
            case "flv":
                return "video";

            case "mp3":
            case "aac":
            case "wav":
            case "flac":
            case "alac":
            case "dsd":
            case "ogg":
                return "audio";

            case "doc":
            case "docx":
            case "html":
            case "pdf":
            case "xls":
            case "xlsx":
            case "ppt":
            case "pptx":
            case "txt":
            case "csv":
                return "docs";

            default: return "other";
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOfDot = name.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // empty extension
        }

        // +1 to return a substring without the . (dot)
        return name.substring(lastIndexOfDot + 1);
    }


    /*
        TODO: MOVE THIS TO FILE HELPERSSSS
     */
    private static long getFileSizeGigaBytes(long byteSize) {
        return (long) ((double) byteSize / (1024.0 * 1024.0 * 1024.0));
    }

    private static long getFileSizeMegaBytes(long byteSize) {
        return (long) ((double) byteSize / (1024.0 * 1024.0));
    }
	
	private static long getFileSizeKiloBytes(long byteSize) {
		return (long) ((double) byteSize / 1024.0);
	}

	private static long getFileSizeBytes(long byteSize) { return byteSize; }

    private static String getFileSize(File file) {
        if(file.length() < 1024)
            return getFileSizeBytes(file.length()) + "B";
        else if(getFileSizeKiloBytes(file.length()) < 1024)
            return getFileSizeKiloBytes(file.length()) + "KB";
        else if(getFileSizeMegaBytes(file.length()) < 1024)
            return getFileSizeMegaBytes(file.length()) + "MB";
        else
            return getFileSizeGigaBytes(file.length()) + "GB";
    }

    private static String getFileSize(long byteSize) {
        if(byteSize < 1024)
            return getFileSizeBytes(byteSize) + "B";
        else if(getFileSizeKiloBytes(byteSize) < 1024)
            return getFileSizeKiloBytes(byteSize) + "KB";
        else if(getFileSizeMegaBytes(byteSize) < 1024)
            return getFileSizeMegaBytes(byteSize) + "MB";
        else
            return getFileSizeGigaBytes(byteSize) + "GB";
    }
}
