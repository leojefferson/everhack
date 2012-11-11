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
				if(!nova.equalsIgnoreCase(""))
					l.add(nova);
				nova = "";
			}
			else{
				if(!nova.equals(""))
					nova+=" ";
				nova += nova.charAt(i);
			}
			if(!nova.equalsIgnoreCase(""))
				l.add(nova);
		}
		
		//l.add("Uma Nova Frase de 6 palavras");
		return l;
	}

	public static String[] fraseToArray(String string) {
		return string.split(" ");
	}

	public static String removeEnter(String s) {
		char c = s.charAt(0);
		if(c=='\n')
			c = ' ';
		String result = ""+c;
		for(int i=0; i< s.length(); i++){
			c = s.charAt(i);
			if(c=='\n')
				c = ' ';
			result += c;
		}
		return result;
	}

	public static String[] removeEspacos(String[] frase) {
		
		String[] result = new String[frase.length];
		for(int i=0; i< frase.length; i++){
			String antiga = frase[i];
			String nova = "";
			for(int j=0; j< antiga.length(); j++){
				char c = antiga.charAt(j);
				if(c != ' ')
					nova += c;
			}
			result[i] = nova;
		}
		return result;
	}
}
