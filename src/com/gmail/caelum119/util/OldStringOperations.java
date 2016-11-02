package com.gmail.caelum119.util;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Caelum on 8/19/14.
 */
public class OldStringOperations{
  public static Object stringToType(String typeName, String data){
    if(data.length() < 1)
      return null;
    switch(typeName){
      case ("int"):
        return Integer.parseInt(data);
      case ("long"):
        return Long.parseLong(data);
      case ("boolean"):
        return Boolean.getBoolean(data);
      case ("double"):
        return Double.valueOf(data);
      case ("byte"):
        return Byte.valueOf(data);
      case ("float"):
        return Float.valueOf(data);
      case ("class java.util.ArrayList"):

        ArrayList out = new ArrayList();
        String[] split = data.split(",");

        for(String line : split)
          out.add(line);

        return out;
      default:
        return data;

    }
  }


  public static Object stringToType(Type type, String data){
    return stringToType(type.toString(), data);
  }

  public static Object stringToType(Class type, String data){
    return stringToType(type.getName(), data);
  }

  public static String removeLastChar(String str){
    return str.substring(0, str.length() - 1);
  }

  public static String removeLastChars(String str, int count){
    return str.substring(0, str.length() - count);
  }

  public static String[] convertList(List<String> list){
    String[] output = new String[list.size()];
    for(int i = 0; i < list.size(); i++)
      output[i] = list.get(i);

    return output;
  }
}
