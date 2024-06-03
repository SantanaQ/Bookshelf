package helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.jose4j.base64url.Base64;
import org.primefaces.shaded.commons.io.IOUtils;

import objects.Item;

public class DataHelper {
	
	public List<String> stringValuesToList(String s){
		List<String> categories = Arrays.asList(s.split(","));
		return categories;
	}
	
	public boolean containsVal(String[] arr, String val) {
		for(String s: arr) {
			if(s.equals(val)) {
				return true;
			}
		}
		return false;
	}
	
	public String streamToB64Convert(InputStream stream) {
		String base64 = "";
		try {
			byte[] bytes = stream.readAllBytes();
			base64 = Base64.encode(bytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return base64;
	}
	
	public int findVal(List<Item> list, Item target) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).equals(target)) {
				return i;
			}
		}
		return -1;
	}

}
