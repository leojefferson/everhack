package com.example.everhack;

import android.util.Log;

public class Util {
	public static String  htmlToPlain(String s){
		String result = "";
		
		for(int i=0; i< s.length(); i++){
			if(s.charAt(i) == '<'){
				result += "\n";
				i++;
				while (i<s.length() && s.charAt(i) != '>')
					i++;	
				//i++;
			}
			else if(i<s.length() && s.charAt(i) != '\n' &&
					(!((s.substring(i, i+2)).equalsIgnoreCase("  ")
					&& i<s.length()-2)))
				result += s.charAt(i);
		}
		return result;
	}
}
