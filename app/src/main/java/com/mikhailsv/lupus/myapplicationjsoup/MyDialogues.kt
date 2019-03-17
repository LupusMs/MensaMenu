package com.mikhailsv.lupus.myapplicationjsoup

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference

/**
 * Calling Alert Dialog with cafe selection list
 */
fun cafeSelectionDialog(mp: MainActivity.MyParser, editor: SharedPreferences.Editor, language: String, day: String, context: Context) {

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
fun ratingDialog(context:Context, mDatabase: DatabaseReference, rating:Float, oldRating:Float, key:String, ratingBar: RatingBar, textVotes:TextView) {

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


