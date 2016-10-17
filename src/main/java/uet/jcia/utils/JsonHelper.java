package uet.jcia.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uet.jcia.data.node.RootNode;
import uet.jcia.data.node.TreeNode;
import uet.jcia.model.CoreAPI;

public class JsonHelper {
    
    public static String toJsonString(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static RootNode convertJsonToRootNode(String szJson) {
		RootNode root = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			root = mapper.readValue(szJson, RootNode.class);
		
			return root;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    
    public static String convertNodeToJson(TreeNode node) {
    	String result = null;
    	try {
    		ObjectMapper mapper = new ObjectMapper();
			result = mapper.writeValueAsString(node);
		
			return result;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }
}
