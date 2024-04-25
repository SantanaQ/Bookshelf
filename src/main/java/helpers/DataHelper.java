package helpers;

import java.util.Arrays;
import java.util.List;

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

}
