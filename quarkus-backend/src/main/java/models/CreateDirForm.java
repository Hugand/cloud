package models;

import org.jboss.resteasy.annotations.jaxrs.FormParam;

public class CreateDirForm {
    public String dirPathName;

    public CreateDirForm(String dirPathName) {
        this.dirPathName = dirPathName;
    }

    public String getDirPathName() { return this.dirPathName; }
}
