package uet.jcia.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;

import uet.jcia.utils.Constants;

public class MapStorage {
	public static final String filePath = Constants.TEMP_SOURCE_FOLDER + File.separator + Constants.MAP_FILE_NAME;
	
	public void saveMap(HashMap<String, String> mapper) {
		clearMapData();
		try {
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(mapper);
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> loadMap() {
		HashMap<String, String> mapper = null;
		
		File file = new File(filePath);
		if (!file.exists()) return new HashMap<>();
		
		try {
			FileInputStream fis = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			mapper = (HashMap<String, String>) ois.readObject();
			ois.close();
			
			if (mapper == null)
				return new HashMap<>();
			else
				return mapper;
			
			//}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
		
	private void clearMapData() {
		File file = new File(filePath);
		try {
			if(file.exists()) {
				PrintWriter pw = new PrintWriter(filePath);
				pw.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
