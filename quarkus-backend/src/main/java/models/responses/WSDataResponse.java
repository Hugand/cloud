package models.responses;

import models.CloudFile;
import models.CloudStorage;

import java.util.List;
import java.util.Set;

public class WSDataResponse extends WSResponse {
    public List<CloudFile> filesList;
    CloudStorage cloudStorage;

    public WSDataResponse(String type, List<CloudFile> filesList) {
        this.type = type;
        this.filesList = filesList;
    }

    public WSDataResponse(String type, List<CloudFile> filesList, CloudStorage cloudStorage) {
        this.type = type;
        this.filesList = filesList;
        this.cloudStorage = cloudStorage;
    }

    public WSDataResponse(String type, String desc, List<CloudFile> filesList) {
        this.type = type;
        this.desc = desc;
        this.filesList = filesList;
    }

    public WSDataResponse(String type, String desc, List<CloudFile> filesList, CloudStorage cloudStorage) {
        this.type = type;
        this.desc = desc;
        this.filesList = filesList;
        this.cloudStorage = cloudStorage;
    }
}
