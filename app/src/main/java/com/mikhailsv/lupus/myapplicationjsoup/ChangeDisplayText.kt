package com.mikhailsv.lupus.myapplicationjsoup


// Editing text to be displayed
fun displayText(myString1: String): String = myString1.replace("\\d".toRegex(), "").replace("\\(.*?\\)".toRegex(), "").replace(" ,".toRegex(), ",")
            .replace("\\.".toRegex(), "")


//Editing string for to be used in DB search query
fun searchText(myString2: String): String = myString2.replace(",".toRegex(), "").replace("\\s".toRegex(), "_").replace("\\d".toRegex(), "")
            .replace("\\(".toRegex(), "").replace("\\)".toRegex(), "").replace("-".toRegex(), "")
            .replace("\\.".toRegex(), "")

// Text formatted for web search engine
fun searchTextWeb(myString3: String): String = myString3.replace(",".toRegex(), "").replace("\\s".toRegex(), "+").replace("\\d".toRegex(), "")
        .replace("\\(".toRegex(), "").replace("\\)".toRegex(), "").replace("-".toRegex(), "")
        .replace("\\.".toRegex(), "")
