package com.mikhailsv.lupus.myapplicationjsoup

import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WebSearchTextFormattingTest {
    var dish:Dish?=null

    @Before
    fun initialise() {
        dish = Dish("first dish", "Chicken breast with potato",
                "1.2", 4f,"2")
    }


    @Test
    fun webSearchTextTest()
    {
        assertEquals("Chicken+breast+with+potato", dish?.searchTextWeb())
    }
}