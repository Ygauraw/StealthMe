// Class that handles simple string transformations not provided in Java

package com.example.stealthme;

public class StringManager
{

	public static String removeSpecialCharacters(String input)
	{
		String result = input;
        if(result.contains("-")){
            result = result.replace("-", "");
        }
        if(result.contains("(")){
            result = result.replace("(", "");
        }
        if(result.contains(")")){
            result = result.replace(")", "");
        }
        if(result.contains(".")){
            result = result.replace(".", "");
        }
        if(result.contains("/")){
            result = result.replace("/", "");
        }
        if(result.contains(",")){
            result = result.replace(",", "");
        }
        if(result.contains("#")){
            result = result.replace("#", "");
        }
        if(result.contains("*")){
            result = result.replace("*", "");
        }
        if(result.contains("+")){
            result = result.replace("+", "");
        }
        if(result.contains("N")){
            result = result.replace("N", "");
        }
        if(result.contains(";")){
            result = result.replace(";", "");
        }
        if(result.contains("%")){
            result = result.replace("%", "");
        }
        if(result.contains(" ")){
            result = result.replace(" ", "");
        }
        if(result.contains("@")){
            result = result.replace("@", "");
        }
        if(result.contains("$")){
            result = result.replace("$", "");
        }
        if(result.contains("^")){
            result = result.replace("^", "");
        }
        if(result.contains("&")){
            result = result.replace("&", "");
        }
        if(result.contains("_")){
            result = result.replace("_", "");
        }
        if(result.contains("=")){
            result = result.replace("=", "");
        }
        if(result.contains("~")){
            result = result.replace("~", "");
        }
        if(result.contains("`")){
            result = result.replace(";", "");
        }

        return result;

	}
	
}
