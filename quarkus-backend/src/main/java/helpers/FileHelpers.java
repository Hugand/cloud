package helpers;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.ws.rs.core.MultivaluedMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class FileHelpers {
    static public String getDirNameFromMultipartFormData(Map<String, List<InputPart>>  uploadForm) {
        for (InputPart inputPart: uploadForm.get("dir")) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();

                return URLEncoder.encode(inputPart.getBody(String.class, null), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
    
    //get uploaded filename, is there a easy way in RESTEasy?
    static public String getFileName(MultivaluedMap<String, String> header) {
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        
        for (String filename : contentDisposition) {
            System.out.println(filename);
            if ((filename.trim().startsWith("filename"))) {

                String[] name = filename.split("=");

                try {
                    return URLEncoder.encode(name[1].trim().replaceAll("\"", ""), StandardCharsets.UTF_8.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return "unknown";
    }

    //save to somewhere
    static public boolean writeFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);
        boolean fileCreateResult = false;

        if (!file.exists()) {
            fileCreateResult = file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();

        return fileCreateResult;
    }

    static public String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOfDot = name.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // empty extension
        }

        // +1 to return a substring without the . (dot)
        return name.substring(lastIndexOfDot + 1);
    }

    static public String getFileExtension(String name) {
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

    public static String getFileSize(File file) {
        if(file.length() < 1024)
            return getFileSizeBytes(file.length()) + "B";
        else if(getFileSizeKiloBytes(file.length()) < 1024)
            return getFileSizeKiloBytes(file.length()) + "KB";
        else if(getFileSizeMegaBytes(file.length()) < 1024)
            return getFileSizeMegaBytes(file.length()) + "MB";
        else
            return getFileSizeGigaBytes(file.length()) + "GB";
    }

    public static String getFileSize(long byteSize) {
        if(byteSize < 1024)
            return getFileSizeBytes(byteSize) + "B";
        else if(getFileSizeKiloBytes(byteSize) < 1024)
            return getFileSizeKiloBytes(byteSize) + "KB";
        else if(getFileSizeMegaBytes(byteSize) < 1024)
            return getFileSizeMegaBytes(byteSize) + "MB";
        else
            return getFileSizeGigaBytes(byteSize) + "GB";
    }


    public static String getType(File file) {
        return file.isDirectory() ? "folder" : "file";
    }

    public static String getFileCategory(File file) {
        String fileExtension = FileHelpers.getFileExtension(file);

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
}