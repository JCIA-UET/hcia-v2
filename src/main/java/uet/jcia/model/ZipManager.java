package uet.jcia.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipManager {

    private List<String> fileList;
    private String source;
    
    public ZipManager() {
        fileList = new ArrayList<>();
    }
    
    public String compress(String sourceFolder) {
        this.source = sourceFolder;
        scan(new File(sourceFolder));
        
        byte[] buff = new byte[1024];
        
        String outputZip = source + ".zip";
        try {
            FileOutputStream fos = new FileOutputStream(outputZip);
            ZipOutputStream zos = new ZipOutputStream(fos);
            
            for (String entryName : fileList) {
                ZipEntry entry = new ZipEntry(entryName);
                zos.putNextEntry(entry);
                
                FileInputStream fis =
                        new FileInputStream(source + File.separator + entryName);
                
                int len;
                while ((len = fis.read(buff)) > 0) {
                    zos.write(buff, 0, len);
                }
                
                fis.close();
            }
            
            zos.closeEntry();
            zos.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public void scan(File f) {
        if (f.isFile()) {
            fileList.add(getFileEntryName(f.getAbsolutePath()));
            
        } else if (f.isDirectory()){
            String[] childs = f.list();
            for (String c : childs) {
                scan(new File(f, c));
            }
        }
    }
    
    private String getFileEntryName(String fullPath) {
        return fullPath.substring(source.length()+1, fullPath.length());
    }
    
    public List<String> getFileList() {
        return fileList;
    }
}
