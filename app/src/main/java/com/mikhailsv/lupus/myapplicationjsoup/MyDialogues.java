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

        final AlertDialog.Builder myBuilder = new AlertDialog.Builder(context);
        myBuilder.setTitle("Select Cafe");
        myBuilder.setCancelable(false);


        myBuilder.setItems(R.array.cafe_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selectedCafe = Consts.ARM_URL;

                switch (which){
                    case 0 :
                        selectedCafe = Consts.ARM_URL;
                        break;
                    case 1 :
                        selectedCafe = Consts.BERG_URL;
                        break;
                    case 2 :
                        selectedCafe = Consts.BOT_GARD_URL;
                        break;
                    case 3 :
                        selectedCafe = Consts.BUC_URL;
                        break;
                    case 4 :
                        selectedCafe = Consts.CAFE_ALEX_URL;
                        break;
                    case 5 :
                        selectedCafe = Consts.CAFE_MITTEL_URL;
                        break;
                    case 6 :
                        selectedCafe = Consts.CFEL_URL;
                        break;
                    case 7 :
                        selectedCafe = Consts.JUNGIUS_URL;
                        break;
                    case 8 :
                        selectedCafe = Consts.CAMP_URL;
                        break;
                    case 9 :
                        selectedCafe = Consts.FINK_URL;
                        break;
                    case 10 :
                        selectedCafe = Consts.GEO_URL;
                        break;
                    case 11 :
                        selectedCafe = Consts.HCU_URL;
                        break;
                    case 12 :
                        selectedCafe = Consts.HAR_URL;
                        break;
                    case 13 :
                        selectedCafe = Consts.STELL_URL;
                        break;
                    case 14 :
                        selectedCafe = Consts.STUD_URL;
                        break;
                    case 15 :
                        selectedCafe = Consts.UEBER_URL;
                        break;

                }

                editor.putString("cafeMensa", selectedCafe );
                editor.putString("URL", Consts.MENU_URL + language + selectedCafe + day);
                editor.commit();
                mp.execute();

            }
        });

        final AlertDialog alert11 = myBuilder.create();
        alert11.show();

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
