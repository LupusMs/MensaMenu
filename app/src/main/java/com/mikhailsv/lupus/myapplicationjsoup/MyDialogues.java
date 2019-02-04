package com.mikhailsv.lupus.myapplicationjsoup;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

public class MyDialogues {


    public static void cafeSelectionDialog(final MainActivity.MyParser mp, final SharedPreferences.Editor editor, final String language,
                                           final String day, final Context context){

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View dialogView = inflater.inflate(R.layout.dialog_cafe_selection, null);
        myBuilder.setView(dialogView);
        myBuilder.setCancelable(false);
        final AlertDialog alert11 = myBuilder.create();
        alert11.show();

        TextView cafeName2 = dialogView.findViewById(R.id.textCafeName2);

        cafeName2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.BERG_URL);
                String cafeMensa = Consts.BERG_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();
                DialogInterface dialog;
                alert11.cancel();
            }
        });


        myBuilder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
        myBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

    }


    public static void ratingDialog(final Context context, final DatabaseReference mDatabase, final float rating, final float oldRating, final String key, final RatingBar ratingBar, final TextView textVotes){

        AlertDialog.Builder myBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        //myBuilder.setMessage("Your vote is\n" + (int)rating );

        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        TextView textView = dialogView.findViewById(R.id.textVoteDialog);
        textView.setText("" + (int)rating);
        myBuilder.setView(dialogView);


        myBuilder.setCancelable(false);

        myBuilder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(context, "Vote accepted", Toast.LENGTH_LONG).show();
                        mDatabase.child(key).child("Rating").push().setValue(rating);
                        ratingBar.setIsIndicator(true);
                        if (textVotes.getText().equals("")) textVotes.setText("1 vote");
                        else votesUpdate(textVotes);
                        dialog.cancel();
                    }
                });
        myBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ratingBar.setRating(oldRating);
                dialog.cancel();
            }
        });
        AlertDialog alert11 = myBuilder.create();
        alert11.show();
    }

    /**
     * Method increasing the votes number when user taps on the rating bar
     *
     * @param textView TextView with number of votes for the current dish
     */
    @SuppressLint("SetTextI18n")
    private static void votesUpdate(TextView textView) {
        String[] votes = textView.getText().toString().split(" ");
        int votesInt = Integer.valueOf(votes[0]);
        votesInt++;
        textView.setText("" + votesInt + " votes");
    }

}
