package org.ugomes.models.rest_response;

import java.lang.String;

public class RestResponse {
    public String status;
    public String desc = "";


    public RestResponse() {}

    public RestResponse(String status) {
        this.status = status;
    }

    public RestResponse(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}