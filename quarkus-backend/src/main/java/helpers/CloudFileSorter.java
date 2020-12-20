package helpers;
import models.CloudFile;

import java.util.Comparator;

public class CloudFileSorter implements Comparator<CloudFile> {
    @Override
    public int compare(CloudFile e1, CloudFile e2)
    {
        return e1.getName().compareToIgnoreCase( e2.getName() );
    }
}