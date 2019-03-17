package com.mikhailsv.lupus.myapplicationjsoup


// Editing text to be displayed
fun displayText(myString1: String): String = myString1.replace("\\d".toRegex(), "").replace("\\(.*?\\)".toRegex(), "").replace(" ,".toRegex(), ",")
            .replace("\\.".toRegex(), "")


//Editing string for to be used in search query
fun searchText(myString2: String): String = myString2.replace(",".toRegex(), "").replace("\\s".toRegex(), "_").replace("\\d".toRegex(), "")
            .replace("\\(".toRegex(), "").replace("\\)".toRegex(), "").replace("-".toRegex(), "")
            .replace("\\.".toRegex(), "")

