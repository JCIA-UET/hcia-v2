package uet.jcia.utils;

import java.security.SecureRandom;

public class RandomHelper {
	private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();
	
	public static String randomString() {
		StringBuilder sb = new StringBuilder(20);
		
		for (int i = 0; i < 20; i++) 
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		
		return sb.toString();
	}
}
