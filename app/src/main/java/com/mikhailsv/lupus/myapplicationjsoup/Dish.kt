package com.mikhailsv.lupus.myapplicationjsoup

data class Dish(val dishType : String, val dishDescription : String,
           val price : String, var rating : Float, var votes : String) {


    // Editing text to be displayed
    fun displayText(): String = dishDescription.replace("\\d".toRegex(), "").replace("\\(.*?\\)".toRegex(), "").replace(" ,".toRegex(), ",")
            .replace("\\.".toRegex(), "")


    //Editing string for to be used in DB search query
    fun searchText(): String = dishDescription.replace(",".toRegex(), "").replace("\\s".toRegex(), "_").replace("\\d".toRegex(), "")
            .replace("\\(".toRegex(), "").replace("\\)".toRegex(), "").replace("-".toRegex(), "")
            .replace("\\.".toRegex(), "")

    // Text formatted for web search engine
    fun searchTextWeb(): String {
        val string = displayText()
        return string.replace(",".toRegex(), "").replace("\\s".toRegex(), "+").replace("\\d".toRegex(), "")
                .replace("\\(".toRegex(), "").replace("\\)".toRegex(), "").replace("-".toRegex(), "")
                .replace("\\.".toRegex(), "")
    }

    fun getImage(): String = searchText() + ".jpg"

}