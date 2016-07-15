package uet.jcia.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Scanner {

	private Extractor ext = new Extractor();
	
	public List<String> searchXmlFile(String zipFile) {
		List<String> namesList = new ArrayList<>();
		String xmlFileDir = null;
		try {
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			
			ZipEntry ze = zis.getNextEntry();
			System.out.println("-- FINDING XML MAPPING FILE --");
			while(ze != null) {
				
				String fileName = ze.getName();
				
				if(fileName.endsWith(".xml")) {
					xmlFileDir = Extractor.OUTPUT_DIR + File.separator + fileName;
					System.out.println("-- FOUND XML FILE. EXTRACTING: " + xmlFileDir + " --");
					File xmlFile = new File(xmlFileDir);
					
					new File(xmlFile.getParent()).mkdirs();
					
					ext.extractZipFile(zis, fileName);
					namesList.add(xmlFileDir);
				}
				ze = zis.getNextEntry();
			}
			
			zis.closeEntry();
		    zis.close();
		    
		    System.out.println("-- ZIP FILE SCAN COMPLETE. --");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return namesList;
	}
}
