package com.mikhailsv.lupus.myapplicationjsoup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public TextView textView1;
    public TextView textView2;
    public TextView textView3;
    public TextView textView4;
    public TextView textView5;
    public TextView dishDescription1;
    public TextView dishDescription2;
    public TextView dishDescription3;
    public TextView dishDescription4;
    public TextView dishDescription5;
    public TextView textViewDate;
    private TextView textPrice1;
    private TextView textPrice2;
    private TextView textPrice3;
    private TextView textPrice4;
    private TextView textPrice5;
    public ImageView imageView1;
    public ImageView imageView2;
    public ImageView imageView3;
    public ImageView imageView4;
    public ImageView imageView5;
    private ImageView tomorrow_btn;
    private ImageView today_btn;
    public Button buttonEn;
    public Button buttonDe;
    private RatingBar ratingBar1;
    private RatingBar ratingBar2;
    private RatingBar ratingBar3;
    private RatingBar ratingBar4;
    private RatingBar ratingBar5;
    private DatabaseReference mDatabase;
    private TextView textVotes1;
    private TextView textVotes2;
    private TextView textVotes3;
    private TextView textVotes4;
    private TextView textVotes5;
    int i;
    String myurl;
    String language;
    String day;
    String cafeMensa;
    String updateAlert;
    int[] increment = {0, 0, 0, 0};
    SharedPreferences sharedPref;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    //Selecting between Mensa and Cafe menus
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mensa:
                sharedPref = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                language = sharedPref.getString("language", Consts.LANGUAGE_DE);
                day = sharedPref.getString("day", Consts.DAY_TODAY);

                //Hiding navigation arrow
                if (day.equals(Consts.DAY_TODAY))
                    today_btn.setVisibility(View.GONE);
                else
                    tomorrow_btn.setVisibility(View.GONE);

                editor.putString("cafeMensa", Consts.MENSA_URL);
                editor.commit();
                cafeMensa = Consts.MENSA_URL;
                myurl = Consts.MENU_URL + language + cafeMensa + day;
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("myurl", myurl);
                startActivity(intent);
                return true;
            case R.id.cafe:
                sharedPref = getPreferences(MODE_PRIVATE);
                editor = sharedPref.edit();
                language = sharedPref.getString("language", Consts.LANGUAGE_DE);
                day = sharedPref.getString("day", Consts.DAY_TODAY);
                editor.putString("cafeMensa", Consts.CAFE_URL);
                editor.commit();
                cafeMensa = Consts.CAFE_URL;
                myurl = Consts.MENU_URL + language + cafeMensa + day;
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("myurl", myurl);
                startActivity(intent);
                return true;
            case R.id.uploadPhoto:
                Intent photoIntent = new Intent(this, PhotoActivity.class);
                startActivity(photoIntent);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        dishDescription1 = findViewById(R.id.dishDescription1);
        dishDescription2 = findViewById(R.id.dishDescription2);
        dishDescription3 = findViewById(R.id.dishDescription3);
        dishDescription4 = findViewById(R.id.dishDescription4);
        dishDescription5 = findViewById(R.id.dishDescription5);
        textViewDate = findViewById(R.id.textViewDate);
        textPrice1 = findViewById(R.id.textPrice1);
        textPrice2 = findViewById(R.id.textPrice2);
        textPrice3 = findViewById(R.id.textPrice3);
        textPrice4 = findViewById(R.id.textPrice4);
        textPrice5 = findViewById(R.id.textPrice5);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        buttonDe = findViewById(R.id.buttonDe);
        buttonEn = findViewById(R.id.buttonEn);
        tomorrow_btn = findViewById(R.id.tomorrow_btn);
        today_btn = findViewById(R.id.today_btn);
        ratingBar1 = findViewById(R.id.ratingBar1);
        ratingBar1.setRating(0);
        ratingBar2 = findViewById(R.id.ratingBar2);
        ratingBar2.setRating(0);
        ratingBar3 = findViewById(R.id.ratingBar3);
        ratingBar3.setRating(0);
        ratingBar4 = findViewById(R.id.ratingBar4);
        ratingBar4.setRating(0);
        ratingBar5 = findViewById(R.id.ratingBar5);
        ratingBar5.setRating(0);
        textVotes1 = findViewById(R.id.textVotes1);
        textVotes2 = findViewById(R.id.textVotes2);
        textVotes3 = findViewById(R.id.textVotes3);
        textVotes4 = findViewById(R.id.textVotes4);
        textVotes5 = findViewById(R.id.textVotes5);

        buttonDe.setOnClickListener(this);
        buttonEn.setOnClickListener(this);
        tomorrow_btn.setOnClickListener(this);
        today_btn.setOnClickListener(this);

        //Default url for Mensa menu
        sharedPref = getPreferences(MODE_PRIVATE);
        language = sharedPref.getString("language", Consts.LANGUAGE_DE);
        day = sharedPref.getString("day", Consts.DAY_TODAY);
        cafeMensa = sharedPref.getString("cafeMensa", Consts.MENSA_URL);
        myurl = Consts.MENU_URL + language + cafeMensa + day;

        //Hiding navigation arrow
        if (day.equals(Consts.DAY_TODAY))
            today_btn.setVisibility(View.GONE);
        else
            tomorrow_btn.setVisibility(View.GONE);

        /*updateAlert = sharedPref.getString("update", "");

        if (updateAlert.equals("")){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Update");
        builder1.setMessage("Welcome to the new version!\n" +
                "- Dish rating added. Participate in mensa food rating! Every user can vote, the result are stored in cloud database and " +
                "available for all users.\n" +
                "- Photo uploads added. Take the photo of your food and it will be uploaded to cloud server and " +
                "available for all users.\n" +
                "- Photos from web search removed! Please participate and help to fill the menu with real photos.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("update", "done");
            editor.commit();
        }*/


        //Getting url from onOptionsItemSelected
        try {
            Intent intent = getIntent();
            myurl = intent.getExtras().getString("myurl", Consts.MENU_URL + language + cafeMensa + day);
        } catch (Exception e) {
            e.printStackTrace();
        }

        MyParser mp = new MyParser();
        mp.execute(increment);


    }




    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor;
        switch (view.getId()) {
            case R.id.buttonDe:
                sharedPref = getPreferences(MODE_PRIVATE);
                editor = sharedPref.edit();
                editor.putString("language", Consts.LANGUAGE_DE);
                editor.commit();
                recreate();
                break;
            case R.id.buttonEn:
                sharedPref = getPreferences(MODE_PRIVATE);
                editor = sharedPref.edit();
                editor.putString("language", Consts.LANGUAGE_EN);
                editor.commit();
                recreate();
                break;
            case R.id.tomorrow_btn:
                sharedPref = getPreferences(MODE_PRIVATE);
                editor = sharedPref.edit();
                editor.putString("day", Consts.DAY_TOMORROW);
                editor.commit();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                break;
            case R.id.today_btn:
                sharedPref = getPreferences(MODE_PRIVATE);
                editor = sharedPref.edit();
                editor.putString("day", Consts.DAY_TODAY);
                editor.commit();
                intent = getIntent();
                finish();
                startActivity(intent);
                break;
        }
    }


    class MyParser extends AsyncTask<int[], Void, Void> {
        Element text1;
        Element text2;
        Element text3;
        Element text4;
        Element text5;
        Element textDish1;
        Element textDish2;
        Element textDish3;
        Element textDish4;
        Element textDish5;
        Element textDate;
        Element price1;
        Element price2;
        Element price3;
        Element price4;
        Element price5;
        String searchtextDish1, displaytextDish1;
        String searchtextDish2, displaytextDish2;
        String searchtextDish3, displaytextDish3;
        String searchtextDish4, displaytextDish4;
        String searchtextDish5, displaytextDish5;



        @Override
        protected Void doInBackground(int[]... param) {
            int[] increment = param[0];
            Document doc = null;
            Document pic = null;
            try {
                Intent intent = getIntent();
                myurl = intent.getExtras().getString("myurl", myurl);
                //Adjusting myurl
                myurl = myurl.replaceAll("/" + Consts.LANGUAGE_EN + "/", "/" + language + "/");
                myurl = myurl.replaceAll("/" + Consts.LANGUAGE_DE + "/", "/" + language + "/");
                myurl = myurl.replaceAll(Consts.DAY_TODAY, day);
                myurl = myurl.replaceAll(Consts.DAY_TOMORROW, day);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                doc = Jsoup.connect(myurl).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (doc != null) {

                try {
                    text1 = doc.select("th").get(4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    text2 = doc.select("th").get(5);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    text3 = doc.select("th").get(6);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    text4 = doc.select("th").get(7);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    text5 = doc.select("th").get(8);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    textDish1 = doc.select(".dish-description").get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textDate = doc.select(".category").get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    price1 = doc.select("td.price").get(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    price2 = doc.select("td.price").get(3);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    price3 = doc.select("td.price").get(6);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    price4 = doc.select("td.price").get(9);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    price5 = doc.select("td.price").get(10);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                changeDisplayText mychangeDisplayText = new changeDisplayText();

                try {

                    textDish1 = doc.select(".dish-description").get(0);


                    //Editing Strings for displaying and for search requests
                    displaytextDish1 = mychangeDisplayText.displayText(textDish1.text());
                    searchtextDish1 = mychangeDisplayText.searchText(textDish1.text());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    textDish2 = doc.select(".dish-description").get(1);
                    displaytextDish2 = mychangeDisplayText.displayText(textDish2.text());
                    searchtextDish2 = mychangeDisplayText.searchText(textDish2.text());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    textDish3 = doc.select(".dish-description").get(2);
                    displaytextDish3 = mychangeDisplayText.displayText(textDish3.text());
                    searchtextDish3 = mychangeDisplayText.searchText(textDish3.text());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textDish4 = doc.select(".dish-description").get(3);
                    displaytextDish4 = mychangeDisplayText.displayText(textDish4.text());
                    searchtextDish4 = mychangeDisplayText.searchText(textDish4.text());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    textDish5 = doc.select(".dish-description").get(4);
                    displaytextDish5 = mychangeDisplayText.displayText(textDish5.text());
                    searchtextDish5 = mychangeDisplayText.searchText(textDish5.text());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            SharedPreferences sharedPref = getSharedPreferences("dishPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("dish1", searchtextDish1);
            editor.putString("dish2", searchtextDish2);
            editor.putString("dish3", searchtextDish3);
            editor.putString("dish4", searchtextDish4);
            editor.putString("dish5", searchtextDish5);
            editor.putString("dish11", displaytextDish1);
            editor.putString("dish22", displaytextDish2);
            editor.putString("dish33", displaytextDish3);
            editor.putString("dish44", displaytextDish4);
            editor.putString("dish55", displaytextDish5);

            editor.commit();
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {

            if (text1 != null) {
                textView1.setText(text1.text());
            }
            if (text2 != null) {
                textView2.setText(text2.text());
            }
            if (text3 != null) {
                textView3.setText(text3.text());
            }
            if (text4 != null) {
                textView4.setText(text4.text());
            }
            if (text5 != null) {
                textView5.setText(text5.text());
            }
            if (price1 != null) {
                textPrice1.setText(price1.text());
            }
            if (price2 != null) {
                textPrice2.setText(price2.text());
            }
            if (price3 != null) {
                textPrice3.setText(price3.text());
            }
            if (price4 != null) {
                textPrice4.setText(price4.text());
            }
            if (price5 != null) {
                textPrice5.setText(price5.text());
            }
            if (displaytextDish1 != null) {
                dishDescription1.setText(displaytextDish1);
            }
            if (displaytextDish2 != null) {
                dishDescription2.setText(displaytextDish2);
            }
            if (displaytextDish3 != null) {
                dishDescription3.setText(displaytextDish3);
            }
            if (displaytextDish4 != null) {
                dishDescription4.setText(displaytextDish4);
            }
            if (displaytextDish5 != null) {
                dishDescription5.setText(displaytextDish5);
            }
            textViewDate.setText(textDate.text());

                SharedPreferences sharedPref = getSharedPreferences("dishPref", MODE_PRIVATE);
                //Setting images using Picasso library
                //Loading a picture for the first dish
                Picasso.with(MainActivity.this).load(Consts.CLOUDINARY_URL + sharedPref.getString("dish1", "") + "al.jpg").memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(imageView1);


                //Loading a picture for the second dish
                Picasso.with(MainActivity.this).load(Consts.CLOUDINARY_URL + sharedPref.getString("dish2", "") + "al.jpg").networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView2);

                //Loading a picture for the third dish
                Picasso.with(MainActivity.this).load(Consts.CLOUDINARY_URL + sharedPref.getString("dish3", "") + "al.jpg").networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView3);
                //Loading a picture for the fourth dish
                Picasso.with(MainActivity.this).load(Consts.CLOUDINARY_URL + sharedPref.getString("dish4", "") + "al.jpg").networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView4);
                //Loading a picture for the fifth dish
                Picasso.with(MainActivity.this).load(Consts.CLOUDINARY_URL + sharedPref.getString("dish5", "") + "al.jpg").networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(imageView5);

            //Accessing firebase database for rating
            mDatabase = FirebaseDatabase.getInstance().getReference();
            final String key1 = sharedPref.getString("dish1", "");
            final String key2 = sharedPref.getString("dish2", "");
            final String key3 = sharedPref.getString("dish3", "");
            final String key4 = sharedPref.getString("dish4", "");
            final String key5 = sharedPref.getString("dish5", "");




            ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        Toast.makeText(getApplicationContext(), "Vote accepted", Toast.LENGTH_LONG).show();
                        mDatabase.child(key1).child("Rating").push().setValue(rating);
                        ratingBar1.setIsIndicator(true);
                        if (textVotes1.getText().equals("")) textVotes1.setText("1 vote");
                          else votesUpdate(textVotes1);
                    }
                }
            });
            mDatabase.child(key1).child("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        int total = 0, count = 0;
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            int rating = child.getValue(Integer.class);
                            total = total + rating;
                            count = count + 1;
                        }
                        ratingBar1.setRating(total/count);
                        String votes;
                        if (count == 1) votes = " vote"; else votes = " votes";
                        textVotes1.setText(String.valueOf(count) + votes);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });
            try{
                textDish2.text();} catch (Exception e){
                ratingBar2.setVisibility(View.INVISIBLE);}
            ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        Toast.makeText(getApplicationContext(), "Vote accepted", Toast.LENGTH_LONG).show();
                        mDatabase.child(key2).child("Rating").push().setValue(rating);
                        ratingBar2.setIsIndicator(true);
                        if (textVotes2.getText().equals("")) textVotes2.setText("1 vote");
                         else votesUpdate(textVotes2);
                    }
                }
            });
            mDatabase.child(key2).child("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        int total = 0, count = 0;
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            int rating = child.getValue(Integer.class);
                            total = total + rating;
                            count = count + 1;
                        }
                        ratingBar2.setRating(total/count);
                        String votes;
                        if (count == 1) votes = " vote"; else votes = " votes";
                        textVotes2.setText(String.valueOf(count) + votes);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });
            try{
                textDish3.text();} catch (Exception e){
            ratingBar3.setVisibility(View.INVISIBLE);}
            ratingBar3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        Toast.makeText(getApplicationContext(), "Vote accepted", Toast.LENGTH_LONG).show();
                        mDatabase.child(key3).child("Rating").push().setValue(rating);
                        ratingBar3.setIsIndicator(true);
                        if (textVotes3.getText().equals("")) textVotes3.setText("1 vote");
                         else votesUpdate(textVotes3);
                    }
                }
            });
            mDatabase.child(key3).child("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        int total = 0, count = 0;
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            int rating = child.getValue(Integer.class);
                            total = total + rating;
                            count = count + 1;
                        }
                        ratingBar3.setRating(total/count);
                        String votes;
                        if (count == 1) votes = " vote"; else votes = " votes";
                        textVotes3.setText(String.valueOf(count) + votes);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });
            try{
                textDish4.text();} catch (Exception e){
                ratingBar4.setVisibility(View.INVISIBLE);}
            ratingBar4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        Toast.makeText(getApplicationContext(), "Vote accepted", Toast.LENGTH_LONG).show();
                        mDatabase.child(key4).child("Rating").push().setValue(rating);
                        ratingBar4.setIsIndicator(true);
                        if (textVotes4.getText().equals("")) textVotes4.setText("1 vote");
                        else votesUpdate(textVotes4);
                    }
                }
            });
            mDatabase.child(key4).child("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        int total = 0, count = 0;
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            int rating = child.getValue(Integer.class);
                            total = total + rating;
                            count = count + 1;
                        }
                        ratingBar4.setRating(total/count);
                        String votes;
                        if (count == 1) votes = " vote"; else votes = " votes";
                        textVotes4.setText(String.valueOf(count) + votes);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });
            try{
                textDish5.text();} catch (Exception e){
                ratingBar5.setVisibility(View.INVISIBLE);}
            ratingBar5.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (fromUser) {
                        Toast.makeText(getApplicationContext(), "Vote accepted", Toast.LENGTH_LONG).show();
                        mDatabase.child(key5).child("Rating").push().setValue(rating);
                        ratingBar5.setIsIndicator(true);
                        if (textVotes5.getText().equals("")) textVotes5.setText("1 vote");
                        else votesUpdate(textVotes5);
                    }
                }
            });
            mDatabase.child(key5).child("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        int total = 0, count = 0;
                        for (DataSnapshot child: dataSnapshot.getChildren()) {
                            int rating = child.getValue(Integer.class);
                            total = total + rating;
                            count = count + 1;
                        }
                        ratingBar5.setRating(total/count);
                        String votes;
                        if (count == 1) votes = " vote"; else votes = " votes";
                        textVotes5.setText(String.valueOf(count) + votes);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });

        }


    }

    /**
     * Method increasing the votes number when user taps on the rating bar
     * @param textView
     * */
    public void votesUpdate(TextView textView)
    {
       String[] votes = textView.getText().toString().split(" ");
       int votesInt = Integer.valueOf(votes[0]);
       votesInt++;
       textView.setText("" + votesInt + " votes");
    }


}







