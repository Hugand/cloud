package models;

public class CloudFile {
    private final String file_name;
    private final String file_size;
    private final String file_created_at;
    private final String file_type;
    private final String type;

    public CloudFile(
            String file_name,
            String file_size,
            String file_created_at,
            String type,
            String file_type) {
        this.file_name = file_name;
        this.file_size = file_size;
        this.file_created_at = file_created_at;
        this.type = type;
        this.file_type = file_type;
    }

    public CloudFile(
            String file_name,
            String file_size,
            String file_created_at,
            String type) {
        this.file_name = file_name;
        this.file_size = file_size;
        this.file_created_at = file_created_at;
        this.type = type;
        this.file_type = "";
    }

    public String getCreatedAt() { return this.file_created_at; }
    public String getName() { return this.file_name; }
}
