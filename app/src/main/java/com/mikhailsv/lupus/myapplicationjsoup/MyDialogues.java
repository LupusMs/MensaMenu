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
                }

                editor.putString("cafeMensa", selectedCafe );
                editor.putString("URL", Consts.MENU_URL + language + selectedCafe + day);
                editor.commit();
                mp.execute();

            }
        });

        final AlertDialog alert11 = myBuilder.create();
        alert11.show();


/*

        //Armgartstraße
        TextView cafeName1 = dialogView.findViewById(R.id.textCafeName1);
        cafeName1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.ARM_URL);
                String cafeMensa = Consts.ARM_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();


            }
        });

        //Bergedorf
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

            }
        });

        //Botanischer-Garten
        TextView cafeName3 = dialogView.findViewById(R.id.textCafeName3);
        cafeName3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.BOT_GARD_URL);
                String cafeMensa = Consts.BOT_GARD_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //Bucerius-Law-School
        TextView cafeName4 = dialogView.findViewById(R.id.textCafeName4);
        cafeName4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.BUC_URL);
                String cafeMensa = Consts.BUC_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //Cafe Alexanderstraße
        TextView cafeName5 = dialogView.findViewById(R.id.textCafeName5);
        cafeName5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.CAFE_ALEX_URL);
                String cafeMensa = Consts.CAFE_ALEX_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //Café (am Mittelweg)
        TextView cafeName6 = dialogView.findViewById(R.id.textCafeName6);
        cafeName6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.CAFE_MITTEL_URL);
                String cafeMensa = Consts.CAFE_MITTEL_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //Café CFEL
        TextView cafeName7 = dialogView.findViewById(R.id.textCafeName7);
        cafeName7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.CFEL_URL);
                String cafeMensa = Consts.CFEL_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //TODO fix
        //Café dell`Arte
        TextView cafeName8 = dialogView.findViewById(R.id.textCafeName8);
        cafeName8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.BERG_URL);
                String cafeMensa = Consts.BERG_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //Cafe Jungiusstraße
        TextView cafeName9 = dialogView.findViewById(R.id.textCafeName9);
        cafeName9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.JUNGIUS_URL);
                String cafeMensa = Consts.JUNGIUS_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //TODO fix
        //Schlüters
        TextView cafeName10 = dialogView.findViewById(R.id.textCafeName10);
        cafeName10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.SCHLU_URL);
                String cafeMensa = Consts.SCHLU_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();
                DialogInterface dialog;

            }
        });

        //Campus
        TextView cafeName11 = dialogView.findViewById(R.id.textCafeName11);
        cafeName11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.CAMP_URL);
                String cafeMensa = Consts.CAMP_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //Finkenau
        TextView cafeName12 = dialogView.findViewById(R.id.textCafeName12);
        cafeName12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.FINK_URL);
                String cafeMensa = Consts.FINK_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //Geomatikum
        TextView cafeName13 = dialogView.findViewById(R.id.textCafeName13);
        cafeName13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.GEO_URL);
                String cafeMensa = Consts.GEO_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //HCU
        TextView cafeName14 = dialogView.findViewById(R.id.textCafeName14);
        cafeName14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.HCU_URL);
                String cafeMensa = Consts.HCU_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //Harburg
        TextView cafeName15 = dialogView.findViewById(R.id.textCafeName15);
        cafeName15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.HAR_URL);
                String cafeMensa = Consts.HAR_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //Stellingen
        TextView cafeName16 = dialogView.findViewById(R.id.textCafeName16);
        cafeName16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.STELL_URL);
                String cafeMensa = Consts.STELL_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //Studierendenhaus
        TextView cafeName17 = dialogView.findViewById(R.id.textCafeName17);
        cafeName17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.STUD_URL);
                String cafeMensa = Consts.STUD_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

            }
        });

        //Überseering
        TextView cafeName18 = dialogView.findViewById(R.id.textCafeName18);
        cafeName18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("cafeMensa", Consts.UEBER_URL);
                String cafeMensa = Consts.UEBER_URL;
                String myurl = Consts.MENU_URL + language + cafeMensa + day;
                editor.putString("URL", myurl);
                editor.commit();
                mp.execute();

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

        alert11.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.wtf("mytag", "cancelled");
            }
        });*/

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
