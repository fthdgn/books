package tr.name.fatihdogan.books;

import java.io.File;

@SuppressWarnings({"WeakerAccess"})
public class Constants {

    //region Paths
    public static final File filesDir = BaseApplication.getInstance().getFilesDir();
    public static final String filesDirPath = filesDir.getPath();
    public static final String localBookPath = filesDirPath + File.separator + "local" + File.separator + "book";
    public static final String onlineBookPath = filesDirPath + File.separator + "online" + File.separator + "book";
    public static final String localCoverPath = filesDirPath + File.separator + "local" + File.separator + "cover";
    public static final String onlineCoverPath = filesDirPath + File.separator + "online" + File.separator + "cover";
    //endregion

}
