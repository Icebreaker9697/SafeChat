package hello;

import java.util.Base64;

public class URLEncoder {
	public static String encodeToString(byte[] arr) {
		String res = Base64.getEncoder().encodeToString(arr);
		res = res.replace('+', '-');
		res = res.replace('/', '_');
		return res;
	}
	
	public static byte[] decodeFromString(String dat) {
		String tmp = dat.replace('_', '/');
		tmp = tmp.replace('-', '+');
		return Base64.getDecoder().decode(tmp);
	}
}
