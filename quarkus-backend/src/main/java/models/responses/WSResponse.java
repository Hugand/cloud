package models.responses;

public class WSResponse {
    public String type;
    public String desc;

    public WSResponse() {}
    public WSResponse(String type) {
        this.type = type;
        this.desc = "";
    }
    public WSResponse(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
