package helpers;

import java.util.Arrays;
import java.util.List;

public class DataTransformHelper {
	
	public List<String> stringValuesToList(String s){
		List<String> categories = Arrays.asList(s.split(","));
		return categories;
	}

}
