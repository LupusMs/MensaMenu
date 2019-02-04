package com.mikhailsv.lupus.myapplicationjsoup;

final class Consts {
    public static final String MENU_URL = "http://speiseplan.studierendenwerk-hamburg.de/";
    public static final String CLOUDINARY_URL = "https://res.cloudinary.com/hawmenu/image/upload/";
    public static final String LANGUAGE_EN = "en";
    public static final String LANGUAGE_DE = "de";

    //HAW
    public static final String CAFE_URL = "/531/2019";
    public static final String MENSA_URL = "/530/2019";

    public static final String ARM_URL = "/590/2019";
    public static final String BERG_URL = "/520/2019";
    public static final String DAY_TODAY = "/0/";
    public static final String DAY_TOMORROW = "/99/";

    public static final int[] increment = {0, 0, 0, 0};

    private Consts(){
        throw new AssertionError();
    }

}
