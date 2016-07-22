package uet.jcia.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    
    public static Object deepClone(Object object) {
        try {
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ObjectOutputStream oos = new ObjectOutputStream(baos);
          oos.writeObject(object);
          ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
          ObjectInputStream ois = new ObjectInputStream(bais);
          return ois.readObject();
        }
        catch (Exception e) {
          e.printStackTrace();
          return null;
        }
      }
}
