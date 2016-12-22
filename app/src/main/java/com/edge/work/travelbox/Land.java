package com.edge.work.travelbox;

/**
 * Created by Super_000 on 12/22/2016.
 */

public class Land{

    public LandElement[][] landElements = new LandElement[6][6];

    public void initLands(){
        //Get lands from server
    }

    public Boolean[][] getAvailableLand(){
        Boolean[][] availableList = new Boolean[6][6];
        //Search for available lands.
        for(int i=0;  )
        return availableList;
    }


    public class LandElement {
        private String content=null;
        private Boolean isHead=false;
        private int width=2;

        public void initContent(String con, int wid){
            content = con;
            width = wid;
        }
        public String getContent(){
            return content;
        }
        public int getWidth(){
            return width;
        }
    }
}
