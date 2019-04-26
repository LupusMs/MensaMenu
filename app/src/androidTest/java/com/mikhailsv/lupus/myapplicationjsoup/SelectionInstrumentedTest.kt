package com.mikhailsv.lupus.myapplicationjsoup

import android.view.View
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.core.AllOf.allOf
import org.hamcrest.core.IsNot.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class SelectionInstrumentedTest {

    @get:Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)

    //Check if any menu is displayed for particular cafe

    @Test
    fun clickTest0() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Armgartstraße")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest1() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Bergedorf")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest2() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Botanischer-Garten")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest3() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Bucerius-Law-School")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest4() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Cafe Alexanderstraße")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest5() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Café (am Mittelweg)")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest6() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Café CFEL")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest7() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Cafe Jungiusstraße")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest8() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Campus")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest9() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Finkenau")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest10() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Geomatikum")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest11() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("HCU")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest12() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Harburg")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest13() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Stellingen")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest14() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Studierendenhaus")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }

    @Test
    fun clickTest15() {
        onView(withId(R.id.spinner)).perform(click())
        onData(allOf(`is`(instanceOf<Any>(String::class.java)), `is`("Other"))).perform(click())
        onView(withText("Überseering")).perform(click())
        onView(withId(R.id.dishDescriptionRW)).check(matches(not<View>(withText(""))))
    }


}
