package com.mikhailsv.lupus.myapplicationjsoup;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codemybrainsout.ratingdialog.RatingDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Objects;

import static android.view.Window.FEATURE_NO_TITLE;


@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView dishDescription1;
    private TextView dishDescription2;
    private TextView dishDescription3;
    private TextView dishDescription4;
    private TextView dishDescription5;
    private TextView textViewDate;
    private TextView textPrice1;
    private TextView textPrice2;
    private TextView textPrice3;
    private TextView textPrice4;
    private TextView textPrice5;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView4;
    private ImageView imageView5;
    private ImageView imageView1Huge;
    private ImageView imageView2Huge;
    private ImageView imageView3Huge;
    private ImageView imageView4Huge;
    private ImageView imageView5Huge;
    private ImageView tomorrow_btn;
    private ImageView today_btn;
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
    private String myurl;
    private String language;
    private String day;
    private String cafeMensa;
    private SharedPreferences.Editor editor;
    String updateAlert;
    //increment was used for functionality that has been cut off. I left it here for future purposes
    private final int[] increment = {0, 0, 0, 0};
    private SharedPreferences sharedPref;
    private MyParser mp;
    private ConstraintLayout constraintLayout;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    //Selecting between Mensa and Cafe menus
    @SuppressLint("ApplySharedPref")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle item selection
        switch (item.getItemId()) {

            case R.id.uploadPhoto:
                Intent photoIntent = new Intent(this, PhotoActivity.class);
                startActivity(photoIntent);
                return true;
            case R.id.deBtn:
                sharedPref = getPreferences(MODE_PRIVATE);
                editor = sharedPref.edit();
                editor.putString("language", Consts.LANGUAGE_DE);
                editor.commit();
                mp = new MyParser();
                mp.execute(increment);
                return true;
            case R.id.enBtn:
                sharedPref = getPreferences(MODE_PRIVATE);
                editor = sharedPref.edit();
                editor.putString("language", Consts.LANGUAGE_EN);
                editor.commit();
                mp = new MyParser();
                mp.execute(increment);
                return true;
            case R.id.feedback:
                startActivity(new Intent(this, Feedback.class));
                return true;
            case R.id.menu_info:
                // Showing "About app" dialog
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("Information about the app");
                final SpannableString s =
                        new SpannableString(getApplicationContext().getText(R.string.about_message));
                Linkify.addLinks(s, Linkify.EMAIL_ADDRESSES);
                builder1.setMessage(s);

                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                ((TextView)alert11.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getPreferences(MODE_PRIVATE);
        Boolean ratingDialogPreferences = sharedPref.getBoolean("rating_dialog", false);

        if (!ratingDialogPreferences) {
            final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                    .threshold(3)
                    .session(5)
                    .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                        @Override
                        public void onFormSubmitted(String feedback) {
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("feedback").child("feedback").push().setValue(feedback);
                        }
                    }).build();

            ratingDialog.show();
            editor = sharedPref.edit();
            editor.putBoolean("rating_dialog", true);
            editor.commit();
        }


        constraintLayout = findViewById(R.id.constraintLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Spinner spinner = findViewById(R.id.spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dishDescription1.setText(null);
                dishDescription2.setText(null);
                dishDescription3.setText(null);
                dishDescription4.setText(null);
                dishDescription5.setText(null);
                textView1.setText(null);
                textView2.setText(null);
                textView3.setText(null);
                textView4.setText(null);
                textView5.setText(null);
                textPrice1.setText(null);
                textPrice2.setText(null);
                textPrice3.setText(null);
                textPrice4.setText(null);
                textPrice5.setText(null);
                textVotes1.setText(null);
                textVotes2.setText(null);
                textVotes3.setText(null);
                textVotes4.setText(null);
                textVotes5.setText(null);
                if (position == 0)
                {
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
                    cafeMensa = Consts.MENSA_URL;
                    myurl = Consts.MENU_URL + language + cafeMensa + day;
                    editor.putString("URL", myurl);
                    editor.commit();

                }
                else
                {
                    sharedPref = getPreferences(MODE_PRIVATE);
                    editor = sharedPref.edit();
                    language = sharedPref.getString("language", Consts.LANGUAGE_DE);
                    day = sharedPref.getString("day", Consts.DAY_TODAY);
                    editor.putString("cafeMensa", Consts.CAFE_URL);
                    cafeMensa = Consts.CAFE_URL;
                    myurl = Consts.MENU_URL + language + cafeMensa + day;
                    editor.putString("URL", myurl);
                    editor.commit();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Showing welcome toast message from the firebase database
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("message").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String toastMessage = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                if (!toastMessage.equals("null"))
                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




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
        imageView1Huge = findViewById(R.id.imageView1Huge);
        imageView2Huge = findViewById(R.id.imageView2Huge);
        imageView3Huge = findViewById(R.id.imageView3Huge);
        imageView4Huge = findViewById(R.id.imageView4Huge);
        imageView5Huge = findViewById(R.id.imageView5Huge);
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

        tomorrow_btn.setOnClickListener(this);
        today_btn.setOnClickListener(this);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView1Huge.setOnClickListener(this);
        imageView2Huge.setOnClickListener(this);
        imageView3Huge.setOnClickListener(this);
        imageView4Huge.setOnClickListener(this);
        imageView5Huge.setOnClickListener(this);

        //Default url for Mensa menu
        sharedPref = getPreferences(MODE_PRIVATE);
        language = sharedPref.getString("language", Consts.LANGUAGE_DE);
        day = sharedPref.getString("day", Consts.DAY_TODAY);
        cafeMensa = sharedPref.getString("cafeMensa", Consts.MENSA_URL);
       /* if (cafeMensa.equals(Consts.MENSA_URL))
            Objects.requireNonNull(getSupportActionBar()).setTitle("HAW Mensa");
        else
            Objects.requireNonNull(getSupportActionBar()).setTitle("HAW Cafe");*/
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

        MyParser mp = new MyParser();
        mp.execute(increment);


    }


    @Override
    protected void onResume() {
        super.onResume();
        mp = new MyParser();
        mp.execute(increment);
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tomorrow_btn:
                sharedPref = getPreferences(MODE_PRIVATE);
                editor = sharedPref.edit();
                editor.putString("day", Consts.DAY_TOMORROW);
                editor.commit();
                tomorrow_btn.setVisibility(View.GONE);
                today_btn.setVisibility(View.VISIBLE);
                MyParser mp = new MyParser();
                mp.execute(increment);
                break;
            case R.id.today_btn:
                sharedPref = getPreferences(MODE_PRIVATE);
                editor = sharedPref.edit();
                editor.putString("day", Consts.DAY_TODAY);
                editor.commit();
                today_btn.setVisibility(View.GONE);
                tomorrow_btn.setVisibility(View.VISIBLE);
                mp = new MyParser();
                mp.execute(increment);
                break;
            case R.id.imageView1:
                //imageView1Huge.setVisibility(View.VISIBLE);
                createPictureDialog(imageView1Huge);
                break;
            case R.id.imageView2:
                //imageView2Huge.setVisibility(View.VISIBLE);
                createPictureDialog(imageView2Huge);
                break;
            case R.id.imageView3:
                //imageView3Huge.setVisibility(View.VISIBLE);
                createPictureDialog(imageView3Huge);
                break;
            case R.id.imageView4:
                //imageView4Huge.setVisibility(View.VISIBLE);
                createPictureDialog(imageView4Huge);
                break;
            case R.id.imageView5:
                //imageView5Huge.setVisibility(View.VISIBLE);
                createPictureDialog(imageView5Huge);
                break;
            case R.id.imageView1Huge:
                imageView1Huge.setVisibility(View.INVISIBLE);
                break;
            case R.id.imageView2Huge:
                imageView2Huge.setVisibility(View.INVISIBLE);
                break;
            case R.id.imageView3Huge:
                imageView3Huge.setVisibility(View.INVISIBLE);
                break;
            case R.id.imageView4Huge:
                imageView4Huge.setVisibility(View.INVISIBLE);
                break;
            case R.id.imageView5Huge:
                imageView5Huge.setVisibility(View.INVISIBLE);
                break;
        }
    }


    @SuppressLint("StaticFieldLeak")
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

        /**
         * Loading an image from web to an ImageView using Picasso
         * @param url url address of image
         * @param imageViewSmall thumb ImageView
         * @param imageViewBig full size ImageView
         */
        void loadImg(String url, final ImageView imageViewSmall, ImageView imageViewBig, final TextView dishDescription)
        {

            // Setting the icon invisible if there is no dish description
            if (dishDescription.getText().toString().equals(""))
                imageViewSmall.setVisibility(View.INVISIBLE);
            else
                imageViewSmall.setVisibility(View.VISIBLE);

            // Loading the image with Picasso
            Picasso.with(MainActivity.this).load(Consts.CLOUDINARY_URL + url).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(imageViewSmall,
                    new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            String dishText = dishDescription.getText().toString().toLowerCase();

                            if (dishText.contains("noodles") || dishText.contains("nudeln"))
                                imageViewSmall.setBackgroundResource(R.drawable.noodles);
                            else if (dishText.contains("chicken") || dishText.contains("hänchen"))
                                imageViewSmall.setBackgroundResource(R.drawable.chicken);
                            else if (dishText.contains("fish") || dishText.contains("fisch"))
                                imageViewSmall.setBackgroundResource(R.drawable.fish);
                            else if (dishText.contains("pollock") || dishText.contains("seelachs"))
                                imageViewSmall.setBackgroundResource(R.drawable.fish);
                            else if (dishText.contains("pork") || dishText.contains("schweine"))
                                imageViewSmall.setBackgroundResource(R.drawable.pork);
                            else if (dishText.contains("roll") || dishText.contains("tasche"))
                                imageViewSmall.setBackgroundResource(R.drawable.roll);
                            else if (dishText.contains("burger"))
                                imageViewSmall.setBackgroundResource(R.drawable.burger);
                            else if (dishText.contains("wrap"))
                                imageViewSmall.setBackgroundResource(R.drawable.roll);
                            else if (dishText.contains("spaghetti"))
                                imageViewSmall.setBackgroundResource(R.drawable.spaghetti);
                            else if (!dishText.equals(""))
                                imageViewSmall.setBackgroundResource(R.drawable.plate);
                            }
                    });


            Picasso.with(MainActivity.this).load(Consts.CLOUDINARY_URL + url).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(imageViewBig);

        }


        @SuppressLint("ApplySharedPref")
        @Override
        protected Void doInBackground(int[]... param) {
            int[] increment = param[0];
            Document doc = null;
            Document pic = null;



            try {
                //Intent intent = getIntent();
                //myurl = intent.getExtras().getString("myurl", myurl);
                myurl = sharedPref.getString("URL", myurl);
                day = sharedPref.getString("day", Consts.DAY_TODAY);
                language = sharedPref.getString("language",Consts.LANGUAGE_DE );
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
            } catch (IOException ignored) {

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
                } catch (Exception ignored) {
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
                    Log.wtf("mytag", "DISPLAY " + displaytextDish1);
                    Log.wtf("mytag", "SEARCH " + searchtextDish1);
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


        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onPostExecute(Void result) {
            textVotes1.setText("");
            textVotes2.setText("");
            textVotes3.setText("");
            textVotes4.setText("");
            textVotes5.setText("");
            ratingBar1.setRating(0f);
            ratingBar2.setRating(0f);
            ratingBar3.setRating(0f);
            ratingBar4.setRating(0f);
            ratingBar5.setRating(0f);
            textPrice1.setText("");
            textPrice2.setText("");
            textPrice3.setText("");
            textPrice4.setText("");
            textPrice5.setText("");

            if (text1 != null) {
                textView1.setText(text1.text());
                textPrice1.setText(price1.text());
            }
            else
                textView1.setText("");
            if (text2 != null) {
                textView2.setText(text2.text());
                textPrice2.setText(price2.text());
            }
            else
                textView2.setText("");
            if (text3 != null) {
                textView3.setText(text3.text());
                textPrice3.setText(price3.text());
            }
            else
                textView3.setText("");
            if (text4 != null) {
                textView4.setText(text4.text());
                textPrice4.setText(price4.text());
            }
            else
                textView4.setText("");
            if (text5 != null) {
                textView5.setText(text5.text());
                textPrice5.setText(price5.text());
            }
            else
                textView5.setText("");

            if (displaytextDish1 != null) {
                dishDescription1.setText(displaytextDish1);
            } else
                dishDescription1.setText("");
            if (displaytextDish2 != null) {
                dishDescription2.setText(displaytextDish2);
            }
            else
                dishDescription2.setText("");
            if (displaytextDish3 != null) {
                dishDescription3.setText(displaytextDish3);
            }
            else
                dishDescription3.setText("");
            if (displaytextDish4 != null) {
                dishDescription4.setText(displaytextDish4);
            }
            else
                dishDescription4.setText("");
            if (displaytextDish5 != null) {
                dishDescription5.setText(displaytextDish5);
            }
            else
                dishDescription5.setText("");
            try{
                textViewDate.setText(textDate.text());
                } catch (Exception e) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setTitle("Error");
                builder1.setMessage("Cannot load the menu");
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
                e.printStackTrace();
                e.printStackTrace();

            }

            SharedPreferences sharedPref = getSharedPreferences("dishPref", MODE_PRIVATE);
            //Setting images using Picasso library
            //Loading a picture for the first dish
            loadImg(searchtextDish1 + ".jpg", imageView1 , imageView1Huge, dishDescription1 );
            //Loading a picture for the second dish
            Log.wtf("mytag", "SEARCH 2 " +searchtextDish2);
            loadImg(searchtextDish2 + ".jpg", imageView2 , imageView2Huge, dishDescription2 );
            //Loading a picture for the third dish
            loadImg(searchtextDish3 + ".jpg", imageView3 , imageView3Huge, dishDescription3 );
            //Loading a picture for the fourth dish
            loadImg(searchtextDish4 + ".jpg", imageView4 , imageView4Huge, dishDescription4 );
            //Loading a picture for the fifth dish
            loadImg(searchtextDish5 + ".jpg", imageView5 , imageView5Huge, dishDescription5 );
            //Accessing firebase database for rating
            mDatabase = FirebaseDatabase.getInstance().getReference();
            final String key1 = sharedPref.getString("dish1", "");
            final String key2 = sharedPref.getString("dish2", "");
            final String key3 = sharedPref.getString("dish3", "");
            final String key4 = sharedPref.getString("dish4", "");
            final String key5 = sharedPref.getString("dish5", "");


            //making the rating bar invisible if there is no dish for today
            ratingBar1.setVisibility(View.VISIBLE);
            try {
                textDish1.text();
            } catch (Exception e) {
                ratingBar1.setVisibility(View.INVISIBLE);
            }
            ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @SuppressLint("SetTextI18n")
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
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        int total = 0, count = 0;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            int rating = child.getValue(Integer.class);
                            total = total + rating;
                            count = count + 1;
                        }
                        ratingBar1.setRating(total / count);
                        String votes;
                        if (count == 1) votes = " vote";
                        else votes = " votes";
                        textVotes1.setText(String.valueOf(count) + votes);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            ratingBar2.setVisibility(View.VISIBLE);
            try {
                textDish2.text();
            } catch (Exception e) {
                ratingBar2.setVisibility(View.INVISIBLE);
            }
            ratingBar2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @SuppressLint("SetTextI18n")
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
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        int total = 0, count = 0;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            int rating = child.getValue(Integer.class);
                            total = total + rating;
                            count = count + 1;
                        }
                        ratingBar2.setRating(total / count);
                        String votes;
                        if (count == 1) votes = " vote";
                        else votes = " votes";
                        textVotes2.setText(String.valueOf(count) + votes);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            ratingBar3.setVisibility(View.VISIBLE);
            try {
                textDish3.text();
            } catch (Exception e) {
                ratingBar3.setVisibility(View.INVISIBLE);
            }
            ratingBar3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @SuppressLint("SetTextI18n")
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
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        int total = 0, count = 0;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            int rating = child.getValue(Integer.class);
                            total = total + rating;
                            count = count + 1;
                        }
                        ratingBar3.setRating(total / count);
                        String votes;
                        if (count == 1) votes = " vote";
                        else votes = " votes";
                        textVotes3.setText(String.valueOf(count) + votes);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            ratingBar4.setVisibility(View.VISIBLE);
            try {
                textDish4.text();
            } catch (Exception e) {
                ratingBar4.setVisibility(View.INVISIBLE);
            }
            ratingBar4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @SuppressLint("SetTextI18n")
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
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        int total = 0, count = 0;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            int rating = child.getValue(Integer.class);
                            total = total + rating;
                            count = count + 1;
                        }
                        ratingBar4.setRating(total / count);
                        String votes;
                        if (count == 1) votes = " vote";
                        else votes = " votes";
                        textVotes4.setText(String.valueOf(count) + votes);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            ratingBar5.setVisibility(View.VISIBLE);
            try {
                textDish5.text();
            } catch (Exception e) {
                ratingBar5.setVisibility(View.INVISIBLE);
            }
            ratingBar5.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @SuppressLint("SetTextI18n")
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
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        int total = 0, count = 0;
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            int rating = child.getValue(Integer.class);
                            total = total + rating;
                            count = count + 1;
                        }
                        ratingBar5.setRating(total / count);
                        String votes;
                        if (count == 1) votes = " vote";
                        else votes = " votes";
                        textVotes5.setText(String.valueOf(count) + votes);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });



        }


    }

    /**
     * Method increasing the votes number when user taps on the rating bar
     *
     * @param textView TextView with number of votes for the current dish
     */
    @SuppressLint("SetTextI18n")
    private void votesUpdate(TextView textView) {
        String[] votes = textView.getText().toString().split(" ");
        int votesInt = Integer.valueOf(votes[0]);
        votesInt++;
        textView.setText("" + votesInt + " votes");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp!=null)
        mp.cancel(true);
    }

   private void createPictureDialog(ImageView imageView) {

       ImageView myImageView = new ImageView(this);
       try {
           Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
           myImageView.setImageBitmap(bitmap);
           myImageView.setPadding(0, 60,0 ,60 );
           AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
           builder1.setView(myImageView);
           builder1.setCancelable(true);
           AlertDialog alert11 = builder1.create();
           alert11.requestWindowFeature(FEATURE_NO_TITLE);
           alert11.show();
       } catch (NullPointerException e)
       {}
   }

}







