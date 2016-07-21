package uet.jcia.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Helper {
    public static final DateFormat DATE_FORMATER =
            new SimpleDateFormat("yyyyMMdd-HHmmss");
    
    public static void copyFile(String srcPath, String dstPath) {
        try {
            File src = new File(srcPath);
            File dst = new File(dstPath);
            dst.getParentFile().mkdirs();
            
            Files.copy(src.toPath(), dst.toPath());
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
