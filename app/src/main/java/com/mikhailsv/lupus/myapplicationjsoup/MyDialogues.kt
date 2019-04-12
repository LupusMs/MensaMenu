package com.mikhailsv.lupus.myapplicationjsoup

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.codemybrainsout.ratingdialog.RatingDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// PATH to pics search engine
const val SEARCH_URL = "https://go.mail.ru/search_images?src=go&fr=mailru&sbmt=1553803701867&fm=1&q="

/**
 * Calls dialogue with webView that shows search results for a dish
 */
fun dishSearchDialog(dish : String, context: Context)
{
    val myBuilder = AlertDialog.Builder(context)
    myBuilder.setCancelable(true)
    val wv = WebView(context)
    wv.getSettings().setDomStorageEnabled(true)
    wv.getSettings().setAppCacheEnabled(true);
    wv.getSettings().setLoadsImagesAutomatically(true);
    wv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    wv.setWebChromeClient( WebChromeClient());
    wv.setWebViewClient( WebViewClient());
    wv.clearCache(true);
    wv.clearHistory();
    wv.getSettings().setJavaScriptEnabled(true);
    wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    wv.loadUrl(SEARCH_URL + dish)
    wv.webViewClient = object : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }
    }
    myBuilder.setView(wv)
    myBuilder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel()}
    val alert11 = myBuilder.create()
    alert11.show()

}

/**
 * Calling Alert Dialog with cafe selection list
 */
fun cafeSelectionDialog(mp: MainActivity.MyParser, language: String, day: String, context: Context) {

    val myBuilder = AlertDialog.Builder(context)
    myBuilder.setTitle("Select Cafe")
    myBuilder.setCancelable(false)


    myBuilder.setItems(R.array.cafe_array) { dialog, which ->
        var selectedCafe = Consts.ARM_URL

        when (which) {
            0 -> selectedCafe = Consts.ARM_URL
            1 -> selectedCafe = Consts.BERG_URL
            2 -> selectedCafe = Consts.BOT_GARD_URL
            3 -> selectedCafe = Consts.BUC_URL
            4 -> selectedCafe = Consts.CAFE_ALEX_URL
            5 -> selectedCafe = Consts.CAFE_MITTEL_URL
            6 -> selectedCafe = Consts.CFEL_URL
            7 -> selectedCafe = Consts.JUNGIUS_URL
            8 -> selectedCafe = Consts.CAMP_URL
            9 -> selectedCafe = Consts.FINK_URL
            10 -> selectedCafe = Consts.GEO_URL
            11 -> selectedCafe = Consts.HCU_URL
            12 -> selectedCafe = Consts.HAR_URL
            13 -> selectedCafe = Consts.STELL_URL
            14 -> selectedCafe = Consts.STUD_URL
            15 -> selectedCafe = Consts.UEBER_URL
        }

        val sPrefs = context.getSharedPreferences(Consts.preferencesFile, MODE_PRIVATE)
        val editor = sPrefs.edit()
        editor.putString("cafeMensa", selectedCafe)
        editor.putString("URL", Consts.MENU_URL + language + selectedCafe + day)
        editor.commit()
        mp.execute()
    }

    val alert11 = myBuilder.create()
    alert11.show()

}

/**
 * Function increasing the votes number when user taps on the rating bar
 *
 * @param textView TextView with number of votes for the current dish
 */
@SuppressLint("SetTextI18n")
private fun votesUpdate(textView: TextView) {
    val votes = textView.text.toString().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    var votesInt = Integer.valueOf(votes[0])
    votesInt++
    textView.text = "$votesInt votes"
}


/**
 * Calling Alert Dialog to confirm the rating change
 */
@SuppressLint("InflateParams")
fun ratingDialog(context:Context, mDatabase: DatabaseReference, rating:Float, oldRating:Float,
                 key:String, ratingBar: RatingBar, textVotes:TextView,
                 dish: Dish) {

val myBuilder = AlertDialog.Builder(context)
val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
val dialogView = inflater.inflate(R.layout.dialog_layout, null)
val textView = dialogView.findViewById<TextView>(R.id.textVoteDialog)
textView.text = "" + rating.toInt()
myBuilder.setView(dialogView)
myBuilder.setCancelable(false)
myBuilder.setPositiveButton(
"Ok"
) { dialog, id ->
    Toast.makeText(context, "Vote accepted", Toast.LENGTH_LONG).show()
    mDatabase.child(key).child("Rating").push().setValue(rating)
    dish.rating = rating
    ratingBar.setIsIndicator(true)
    if (textVotes.text == "")
        textVotes.text = "1 vote"
    else
        votesUpdate(textVotes)
    dialog.cancel()
}
myBuilder.setNegativeButton("Cancel", object: DialogInterface.OnClickListener {
override fun onClick(dialog: DialogInterface, which:Int) {
    ratingBar.rating = oldRating
dialog.cancel()
}
})
val alert11 = myBuilder.create()
alert11.show()
}

/**
 * Asks user to rate the app
 */
fun showAppRatingDialog(context: Context) {
    val sharedPref = context.getSharedPreferences(Consts.preferencesFile, Context.MODE_PRIVATE)
    val ratingDialogPreferences = sharedPref.getBoolean("rating_dialog", false)

    if (!ratingDialogPreferences) {
        val ratingDialog = RatingDialog.Builder(context)
                .threshold(3f)
                .session(5)
                .onRatingBarFormSumbit { feedback ->
                    val mDatabase = FirebaseDatabase.getInstance().reference
                    mDatabase.child("feedback").child("feedback").push().setValue(feedback)
                }.build()
        ratingDialog.show()
        val editor = sharedPref.edit()
        editor.putBoolean("rating_dialog", true)
        editor.commit()
    }
}

/**
 * Dialog can be showed after App Updates with all relevant info
 */
fun showUpdateDialog(context: Context) {

    val sharedPref = context.getSharedPreferences(Consts.preferencesFile, MODE_PRIVATE)
    val editor = sharedPref.edit()
    val updateAlert = sharedPref.getString("update1", "")
    if (updateAlert!!.equals("")) {
        val builder1 = AlertDialog.Builder(context);
        builder1.setTitle("Update")
        builder1.setMessage("Welcome to the new version!\n" +
                "This is a test message")
        builder1.setCancelable(true)

        builder1.setPositiveButton(
                "Ok", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                dialog?.cancel()
            }

        })

        val alert11 = builder1.create()
        alert11.show()
        editor.putString("update1", "done");
        editor.commit();
    }
}

/**
 * Shows dialogue with App info
 */
fun showAboutAppDialog(context: Context)
{
    // Showing "About app" dialog
    val builder1 = AlertDialog.Builder(context)
    builder1.setTitle("Information about the app")
    val s = SpannableString(context.getApplicationContext().getText(R.string.about_message))
    Linkify.addLinks(s, Linkify.EMAIL_ADDRESSES)
    builder1.setMessage(s)
    builder1.setCancelable(true)
    builder1.setPositiveButton(
            "Close"
    ) { dialog, id -> dialog.cancel() }

    val alert11 = builder1.create()
    alert11.show()
    (alert11.findViewById<View>(android.R.id.message) as TextView).movementMethod = LinkMovementMethod.getInstance()
}

/**
 * Shows error message if website cannot be parsed( No Internet connection etc.)
 */
fun netErrorDialog(context: Context)
{
    val builder1 = AlertDialog.Builder(context)
    builder1.setTitle(R.string.error)
    builder1.setMessage(R.string.load_error)
    builder1.setCancelable(true)
    builder1.setPositiveButton(
            "Ok"
    ) { dialog, id -> dialog.cancel() }

    val alert11 = builder1.create()
    alert11.show()
}


