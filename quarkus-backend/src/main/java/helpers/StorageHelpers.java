package helpers;

import configs.CloudProperties;
import org.apache.commons.io.FileUtils;

import java.io.File;

public class StorageHelpers {
    public static long getCurrentUsedSpaceInBytes() {
        return FileUtils.sizeOfDirectory(new File(CloudProperties.DIR));
    }

    public static long getCurrentAvailableSpaceInBytes() {
        return CloudProperties.MAX_AVAILABLE_SPACE - getCurrentUsedSpaceInBytes();
    }
}
