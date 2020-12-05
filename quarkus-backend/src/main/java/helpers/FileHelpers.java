package org.ugomes.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import java.net.URLDecoder;

public class FileHelpers {
    static public String getDirNameFromMultipartFormData(Map<String, List<InputPart>>  uploadForm) {
        for (InputPart inputPart: uploadForm.get("dir")) {
            try {
                MultivaluedMap<String, String> header = inputPart.getHeaders();
                String dirToUpload = URLDecoder.decode(inputPart.getBody(String.class, null));

                return dirToUpload;
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
                
                String finalFileName = URLDecoder.decode(name[1].trim().replaceAll("\"", ""));
                return finalFileName;
            }
        }
        return "unknown";
    }

    //save to somewhere
    static public void writeFile(byte[] content, String filename) throws IOException {

        File file = new File(filename);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();

    }
}