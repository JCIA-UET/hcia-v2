package cuong;

import java.io.IOException;

import uet.jcia.utils.ZipManager;

public class TestZipManager {

    public static void main(String[] args) throws IOException {
        ZipManager zm = new ZipManager();
        
        String zipPath = "I:\\Workspace\\hcia-v2\\temp\\upload\\vnu.zip";
        String dst = zm.unzip(zipPath);
        System.out.println(dst);
        
        /*String source = "I:/Workspace/hcia-v2/temp/zip";
        zm.compress(source);
        System.out.println(zm.getFileList());*/
    }
}
