package models.rest_response;

import java.util.HashSet;
import java.util.Set;
import models.rest_response.RestResponse;

public class GetFoldersInDirResponse extends RestResponse {
    public Set<String> folderList;

    public GetFoldersInDirResponse(String status) {
        this.status = status;
        this.folderList = new HashSet<>();
    }

    public GetFoldersInDirResponse(String status, Set<String> folderList) {
        this.status = status;
        this.folderList = folderList;
    }

    public GetFoldersInDirResponse(String status, String desc) {
        this.status = status;
        this.desc = desc;
        this.folderList = new HashSet<>();
    }

    public GetFoldersInDirResponse(String status, String desc, Set<String> folderList) {
        this.status = status;
        this.desc = desc;
        this.folderList = folderList;
    }

    public void getAsString() {
        System.out.println(this.status + " " + this.desc + " " + this.folderList.size());
    }
}
