package com.edge.work.travelbox;

public class LandElement {
    private static String content = null;
    public static Boolean isHead = false;


    public static String getContent(){
        return content;
    }
    public static void setContent(String value){
        content = value;
    }
    public static Boolean getIsHead(){
        return isHead;
    }
    public static void setIsHead(){
        isHead=true;
    }
}
