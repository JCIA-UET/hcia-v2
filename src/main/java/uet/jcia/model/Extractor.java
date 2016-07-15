package uet.jcia.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

public class Extractor {
	public static final String OUTPUT_DIR = "src/main/resources";
	
	public void extractZipFile(ZipInputStream zis, String fileName) {
		byte[] buffer = new byte[1024];
		try {
			String fileDir = OUTPUT_DIR + File.separator + fileName;
			BufferedOutputStream bof = new BufferedOutputStream(new FileOutputStream(new File(fileDir)));
			int i = -1;
			while((i = zis.read(buffer)) != -1) {
				bof.write(buffer, 0, i);
			}
			bof.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveUploadFile(InputStream is, String fileName) {
    	byte[] buffer = new byte[1024000];
		try {
			String fileDir = OUTPUT_DIR + File.separator + fileName;
			BufferedOutputStream bof = new BufferedOutputStream(new FileOutputStream(new File(fileDir)));
			int i = -1;
			while((i = is.read(buffer)) != -1) {
				bof.write(buffer, 0, i);
			}
			bof.close();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
