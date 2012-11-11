package com.example.everhack;

import java.util.ArrayList;
import java.util.List;

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
		result += "\n";
		return result;
	}

	public static List<String> textToFrases(String s) {
		List<String> l = new ArrayList<String>();
		String nova = "";
		for(int i=0; i< s.length(); i++){
			if(s.charAt(i) == '\n'){
				l.add(nova);
				nova = "";
			}
			else{
				nova += nova.charAt(i);
			}
		}
		return l;
	}

	public static String[] fraseToArray(String string) {
		return string.split(" ");
	}
}
