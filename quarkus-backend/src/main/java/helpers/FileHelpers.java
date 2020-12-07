package helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;

public class FileHelpers {
    static public String getDirNameFromMultipartFormData(Map<String, List<InputPart>>  uploadForm) {
        for (InputPart inputPart: uploadForm.get("dir")) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();

                return URLEncoder.encode(inputPart.getBody(String.class, null), StandardCharsets.UTF_8.toString());
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
}