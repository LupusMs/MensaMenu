package com.mikhailsv.lupus.myapplicationjsoup;

/**
 * Created by Lupus on 09.12.2017.
 */

//Editing string for displaying in app activity
class changeDisplayText {
    public String displayText(String myString1){

        myString1 = myString1.replaceAll("\\d","").replaceAll("\\(.*?\\)","").replaceAll(",","")
        .replaceAll("\\.", "");
        return myString1;
    }


    //Editing string for to be used in search query
    public String searchText(String myString2){
        //myString2 = myString2.split(",")[0];
        myString2 = myString2.replaceAll(",","");
        myString2 = myString2.replaceAll("\\s","_");
        myString2 = myString2.replaceAll("\\d","");
        myString2 = myString2.replaceAll("\\(","");
        myString2 = myString2.replaceAll("\\)","");
        myString2 = myString2.replaceAll("-","");
        myString2 = myString2.replaceAll("\\.","");

        return myString2;

    }

}
