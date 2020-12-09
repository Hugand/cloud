package models;

import configs.CloudProperties;

public class CloudStorage {
    public long maxAvailableSpace = CloudProperties.MAX_AVAILABLE_SPACE; // Value in bytes
    public long currentUsedSpace;
    public long currentAvailableSpace;
    public long storageInImages = 0;
    public long storageInVideos = 0;
    public long storageInAudio = 0;
    public long storageInDocs = 0;
    public long storageInOthers = 0;

    public CloudStorage(int currentUsedSpace) {
        this.currentUsedSpace = currentUsedSpace;
        this.currentAvailableSpace = this.maxAvailableSpace - this.currentUsedSpace;
    }

    public void addToStorageInImages(int val) { this.storageInImages += val; }
    public void addToStorageInVideos(int val) { this.storageInVideos += val; }
    public void addToStorageInAudio(int val) { this.storageInAudio += val; }
    public void addToStorageInDocs(int val) { this.storageInDocs += val; }
    public void addToStorageInOthers(int val) { this.storageInOthers += val; }

    public long getStorageInImages() { return this.storageInImages; }
    public long getStorageInVideos() { return this.storageInVideos; }
    public long getStorageInAudio() { return this.storageInAudio; }
    public long getStorageInDocs() { return this.storageInDocs; }
    public long getStorageInOthers() { return this.storageInOthers; }

    public void addByCategory(String category, int val) {
        switch (category) {
            case "image":
                this.addToStorageInImages(val);
                break;
            case "video":
                this.addToStorageInVideos(val);
                break;
            case "audio":
                this.addToStorageInAudio(val);
                break;
            case "docs":
                this.addToStorageInDocs(val);
                break;
            case "other":
                this.addToStorageInOthers(val);
                break;
            default:
        }

    }

    public String getAsString() {
        return "Image: " + this.storageInImages + "\n" +
                "Video: " + this.storageInVideos + "\n" +
                "Audio: " + this.storageInAudio + "\n" +
                "Docs: " + this.storageInDocs + "\n" +
                "Others: " + this.storageInOthers + "\n";
    }
}
