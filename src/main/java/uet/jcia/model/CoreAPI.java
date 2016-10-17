package uet.jcia.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uet.jcia.data.node.TreeNode;
import uet.jcia.model.parser.HbmParser;
import uet.jcia.model.parser.JavaParser;
import uet.jcia.model.parser.Parser;
import uet.jcia.utils.Constants;
import uet.jcia.utils.FileManager;
import uet.jcia.utils.Helper;
import uet.jcia.utils.HibernateHelper;
import uet.jcia.utils.ZipManager;

public class CoreAPI {
    
    private ZipManager zm = new ZipManager();
    private FileManager fm = new FileManager();
    private Parser parser = new HbmParser();
    private Inverser inverser = new Inverser();
    private MapStorage storage = new MapStorage();
    
    // mapping between temp file and extracted folder
    private static HashMap<String, String> mapper = new HashMap<>(); 
    
    public CoreAPI() {
        mapper = storage.loadMap();
    }
    
    public String parse(String uploadPath) throws Exception {
        TreeNode rootNode = null;
        String resultPath = null;

        // user uploads xml or java file
        if (uploadPath.matches(".*\\.xml") || uploadPath.matches(".*\\.java")) {
            Date date = new Date();
            String dstDir =  Constants.TEMP_SOURCE_FOLDER + File.separator
                    + "upload-src-" + Helper.DATE_FORMATER.format(date);
            File uploadedFile = new File(uploadPath);
            String dstPath = dstDir + File.separator + uploadedFile.getName();
            // copy file to generated folder
            Helper.copyFile(uploadPath, dstPath);

            // select parser's type
            if (uploadPath.matches(".*\\.xml")) {
                parser = new HbmParser();
            } else if (uploadPath.matches(".*\\.java")) {
                parser = new JavaParser();
            }
            // do parse
            rootNode = parser.parseSingleFile(uploadPath);
            // save result
            resultPath = fm.saveTempData(rootNode);
            mapper.put(resultPath, dstDir);
            
        } else if (uploadPath.matches(".*\\.zip")) { // user uploads a zip file
            String extractedFolder = zm.unzip(uploadPath);
            List<String> xmlList = new ArrayList<>();
            List<String> javaList = new ArrayList<>();
            fm.findFiles(extractedFolder, ".*\\.xml", xmlList);
            fm.findFiles(extractedFolder, ".*\\.java", javaList);
            
            if (!xmlList.isEmpty() && !javaList.isEmpty()) {
                // prevent user to upload both java and hbm files
                throw new Exception("can not parse both java and xml at the same time");
            } else if (!javaList.isEmpty()) { // use java parser
                parser = new JavaParser();
                rootNode = parser.parse(javaList);
            } else if (!xmlList.isEmpty()) { // use hbm parser
                parser = new HbmParser();
                rootNode = parser.parse(xmlList);
            }
            resultPath = fm.saveTempData(rootNode);
            mapper.put(resultPath, extractedFolder);
        }
        
        storage.saveMap(mapper);
        return resultPath;
    }
    
    public String refresh(String tempPath) {
        String sourceFolder = mapper.get(tempPath);
        TreeNode rootNode;
        String resultPath = null;
        
        List<String> xmlList = new ArrayList<>();
        fm.findFiles(
                sourceFolder, ".*\\.xml", xmlList);

        rootNode = parser.parse(xmlList);
        resultPath = fm.saveTempData(rootNode);

        mapper.put(resultPath, sourceFolder);
        storage.saveMap(mapper);
        return resultPath;
    }
    
    public TreeNode getParsedData(String tempPath) {
        TreeNode rootNode = fm.readTempData(tempPath);
        return rootNode;
    }
    
    public String download(String tempPath) {
    	System.out.println("Temp path: " + mapper.get(tempPath));
        return zm.compress(mapper.get(tempPath));
    }
    
    public String generateCreationScript(String tempPath) throws IOException {
        String extractedFolder = mapper.get(tempPath);
        List<String> javaList = new ArrayList<>();
        fm.findFiles(extractedFolder, ".*\\.java", javaList);

        for (String javaPath : javaList) {
            File javaFile = new File(javaPath);
            String parentPath = javaFile.getParentFile().getAbsolutePath();
            String fileName = javaFile.getName().replaceAll("\\.java", ".hbm.xml");
            File hbmFile = new File(parentPath + File.separator + fileName);
            if (!hbmFile.exists())
                hbmFile.createNewFile();
        }

        List<String> hbmList = new ArrayList<>();
        fm.findFiles(extractedFolder, ".*\\.hbm.xml", hbmList);
        String sql = HibernateHelper.generateDdl(hbmList); 
        System.out.println(sql);
        return sql;
    }
    
    public void updateData(TreeNode tableNode) {
        inverser.updateTable(tableNode);
    }
    
}
