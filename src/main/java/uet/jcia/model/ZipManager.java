package uet.jcia.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import uet.jcia.utils.Constants;
import uet.jcia.utils.Helper;

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
            return outputZip;
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
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
    
    public String unzip(String zipPath) {
        Date date = new Date();
        String dstDir =  Constants.TEMP_SOURCE_FOLDER + File.separator
                + "upload-src-" + Helper.DATE_FORMATER.format(date);
        File destDir = new File(dstDir);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        
        try {
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipPath));
            ZipEntry entry = zipIn.getNextEntry();

            while (entry != null) {
                String filePath = dstDir + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipIn, filePath);
                    
                } else {
                    File dir = new File(filePath);
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
            zipIn.close();
            return dstDir;
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[4096];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
    
    public List<String> getFileList() {
        return fileList;
    }
}
