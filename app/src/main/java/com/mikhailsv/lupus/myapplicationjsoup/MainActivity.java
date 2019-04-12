package com.mikhailsv.lupus.myapplicationjsoup;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import com.airbnb.lottie.LottieAnimationView;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private List<TextView> textViewsType;
    private List<TextView> textViewsDescription;
    private List<TextView> textViewsPrice;
    private List<ImageView> imageViews;
    private List<ImageView> imageViewsHuge;
    private List<RatingBar> ratingBars;
    private List<TextView> textViewsVotes;
    private List<Dish> dishes;
    private TextView textViewDate;
    private ImageView tomorrow_btn;
    private ImageView today_btn;
    private DatabaseReference mDatabase;
    private String myurl;
    private String language;
    private String day;
    private String cafeMensa;
    private String updateAlert;
    private MyParser mp;
    private ConstraintLayout constraintLayout;
    private float oldRating;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Writing data to SharedPreferences
     * @param key
     * @param value
     */
    private void writeToPrefs(String key, String value )
    {
        SharedPreferences sharedPref = getSharedPreferences(Consts.preferencesFile,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }



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
                writeToPrefs("language", Consts.LANGUAGE_DE);
                mp = new MyParser();
                mp.execute();
                return true;
            case R.id.enBtn:
                writeToPrefs("language", Consts.LANGUAGE_EN);
                mp = new MyParser();
                mp.execute();
                return true;
            case R.id.feedback:
                startActivity(new Intent(this, Feedback.class));
                return true;
            case R.id.menu_info:
               MyDialoguesKt.showAboutAppDialog(MainActivity.this);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void clearAllViews()
    {
        for ( int i = 0; i < textViewsDescription.size(); i++)
        {
            textViewsDescription.get(i).setText(null);
            textViewsType.get(i).setText(null);
            textViewsPrice.get(i).setText(null);
            textViewsVotes.get(i).setText(null);
            ratingBars.get(i).setVisibility(View.INVISIBLE);
            imageViews.get(i).setVisibility(View.INVISIBLE);
        }

    }


    @SuppressLint({"ClickableViewAccessibility", "ApplySharedPref"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.constraintLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ratingBars = new ArrayList<>();
        textViewsType = new ArrayList<>();
        textViewsDescription = new ArrayList<>();
        textViewsPrice = new ArrayList<>();
        imageViews = new ArrayList<>();
        imageViewsHuge = new ArrayList<>();
        textViewsVotes = new ArrayList<>();
        dishes = new ArrayList<>();
        textViewsType.add((TextView) findViewById(R.id.textView1));
        textViewsType.add((TextView) findViewById(R.id.textView2));
        textViewsType.add((TextView) findViewById(R.id.textView3));
        textViewsType.add((TextView) findViewById(R.id.textView4));
        textViewsType.add((TextView) findViewById(R.id.textView5));
        textViewsDescription.add((TextView) findViewById(R.id.dishDescription1));
        textViewsDescription.add((TextView) findViewById(R.id.dishDescription2));
        textViewsDescription.add((TextView) findViewById(R.id.dishDescription3));
        textViewsDescription.add((TextView) findViewById(R.id.dishDescription4));
        textViewsDescription.add((TextView) findViewById(R.id.dishDescription5));
        textViewDate = findViewById(R.id.textViewDate);
        textViewsPrice.add((TextView) findViewById(R.id.textPrice1));
        textViewsPrice.add((TextView) findViewById(R.id.textPrice2));
        textViewsPrice.add((TextView) findViewById(R.id.textPrice3));
        textViewsPrice.add((TextView) findViewById(R.id.textPrice4));
        textViewsPrice.add((TextView) findViewById(R.id.textPrice5));
        imageViews.add((ImageView) findViewById(R.id.imageView1));
        imageViews.add((ImageView) findViewById(R.id.imageView2));
        imageViews.add((ImageView) findViewById(R.id.imageView3));
        imageViews.add((ImageView) findViewById(R.id.imageView4));
        imageViews.add((ImageView) findViewById(R.id.imageView5));
        imageViewsHuge.add((ImageView) findViewById(R.id.imageView1Huge));
        imageViewsHuge.add((ImageView) findViewById(R.id.imageView2Huge));
        imageViewsHuge.add((ImageView) findViewById(R.id.imageView3Huge));
        imageViewsHuge.add((ImageView) findViewById(R.id.imageView4Huge));
        imageViewsHuge.add((ImageView) findViewById(R.id.imageView5Huge));
        tomorrow_btn = findViewById(R.id.tomorrow_btn);
        today_btn = findViewById(R.id.today_btn);



        ratingBars.add((RatingBar) findViewById(R.id.ratingBar1));
        ratingBars.add((RatingBar) findViewById(R.id.ratingBar2));
        ratingBars.add((RatingBar) findViewById(R.id.ratingBar3));
        ratingBars.add((RatingBar) findViewById(R.id.ratingBar4));
        ratingBars.add((RatingBar) findViewById(R.id.ratingBar5));
        textViewsVotes.add((TextView) findViewById(R.id.textVotes1));
        textViewsVotes.add((TextView) findViewById(R.id.textVotes2));
        textViewsVotes.add((TextView) findViewById(R.id.textVotes3));
        textViewsVotes.add((TextView) findViewById(R.id.textVotes4));
        textViewsVotes.add((TextView) findViewById(R.id.textVotes5));
        tomorrow_btn.setOnClickListener(this);
        today_btn.setOnClickListener(this);

        for (int i = 0; i < imageViews.size(); i++)
        {
            imageViews.get(i).setOnClickListener(this);
            textViewsDescription.get(i).setOnClickListener(this);
            imageViewsHuge.get(i).setOnClickListener(this);
        }

        //Asks user to rate the app
        MyDialoguesKt.showAppRatingDialog(MainActivity.this);

        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearAllViews();
                writeToPrefs("language", Consts.LANGUAGE_DE);
                writeToPrefs("day", Consts.DAY_TODAY);

                //Hiding navigation arrow
                assert day != null;
                if (day.equals(Consts.DAY_TODAY))
                    today_btn.setVisibility(View.GONE);
                else
                    tomorrow_btn.setVisibility(View.GONE);

                mp = new MyParser();
                if (position == 1) //Mensa
                {
                    writeToPrefs("cafeMensa", Consts.MENSA_URL);
                    writeToPrefs("URL", Consts.MENU_URL + language + Consts.MENSA_URL + day);
                    mp.execute();
                }
                else if (position == 2) // Cafe
                {
                    writeToPrefs("cafeMensa", Consts.CAFE_URL);
                    writeToPrefs("URL", Consts.MENU_URL + language + Consts.CAFE_URL + day);
                    mp.execute();
                }
                else if (position == 3) // Other...
                {
                    MyDialoguesKt.cafeSelectionDialog(mp, language, day, MainActivity.this);
                    spinner.setSelection(0);
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

        //Showing Animation on app starting
        final LottieAnimationView lottieAnimationView = findViewById(R.id.lottie1);
        if (lottieAnimationView!= null) {
            lottieAnimationView.setVisibility(View.VISIBLE);
            lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    lottieAnimationView.setVisibility(View.GONE);

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }



        //Default url for Mensa menu
        SharedPreferences sharedPref = getSharedPreferences(Consts.preferencesFile, MODE_PRIVATE);
        language = sharedPref.getString("language", Consts.LANGUAGE_DE);
        day = sharedPref.getString("day", Consts.DAY_TODAY);
        cafeMensa = sharedPref.getString("cafeMensa", Consts.MENSA_URL);
        myurl = Consts.MENU_URL + language + cafeMensa + day;

        //Hiding navigation arrow
        if (day.equals(Consts.DAY_TODAY))
            today_btn.setVisibility(View.GONE);
        else
            tomorrow_btn.setVisibility(View.GONE);

        MyDialoguesKt.showUpdateDialog(MainActivity.this);
        MyParser mp = new MyParser();
        mp.execute();
    }


    @SuppressLint("ApplySharedPref")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tomorrow_btn:
                writeToPrefs("day", Consts.DAY_TOMORROW);
                tomorrow_btn.setVisibility(View.GONE);
                today_btn.setVisibility(View.VISIBLE);
                MyParser mp = new MyParser();
                mp.execute();
                break;
            case R.id.today_btn:
                writeToPrefs("day", Consts.DAY_TODAY);
                today_btn.setVisibility(View.GONE);
                tomorrow_btn.setVisibility(View.VISIBLE);
                mp = new MyParser();
                mp.execute();
                break;
            case R.id.imageView1:
                MyDialoguesKt.createPictureDialog(MainActivity.this, imageViewsHuge.get(0));
                break;
            case R.id.imageView2:
                MyDialoguesKt.createPictureDialog(MainActivity.this, imageViewsHuge.get(1));
                break;
            case R.id.imageView3:
                MyDialoguesKt.createPictureDialog(MainActivity.this, imageViewsHuge.get(2));
                break;
            case R.id.imageView4:
                MyDialoguesKt.createPictureDialog(MainActivity.this, imageViewsHuge.get(3));
                break;
            case R.id.imageView5:
                MyDialoguesKt.createPictureDialog(MainActivity.this, imageViewsHuge.get(4));
                break;
            case R.id.imageView1Huge:
                imageViewsHuge.get(0).setVisibility(View.INVISIBLE);
                break;
            case R.id.imageView2Huge:
                imageViewsHuge.get(1).setVisibility(View.INVISIBLE);
                break;
            case R.id.imageView3Huge:
                imageViewsHuge.get(2).setVisibility(View.INVISIBLE);
                break;
            case R.id.imageView4Huge:
                imageViewsHuge.get(3).setVisibility(View.INVISIBLE);
                break;
            case R.id.imageView5Huge:
                imageViewsHuge.get(4).setVisibility(View.INVISIBLE);
                break;
            case R.id.dishDescription1:
                MyDialoguesKt.dishSearchDialog(dishes.get(0).searchTextWeb(), MainActivity.this);
                break;
            case R.id.dishDescription2:
                MyDialoguesKt.dishSearchDialog(dishes.get(1).searchTextWeb(), MainActivity.this);
                break;
            case R.id.dishDescription3:
                MyDialoguesKt.dishSearchDialog(dishes.get(2).searchTextWeb(), MainActivity.this);
                break;
            case R.id.dishDescription4:
                MyDialoguesKt.dishSearchDialog(dishes.get(3).searchTextWeb(), MainActivity.this);
                break;
            case R.id.dishDescription5:
                MyDialoguesKt.dishSearchDialog(dishes.get(4).searchTextWeb(), MainActivity.this);
                break;

        }
    }

    // Memory Leak is prevented with destroying the AsyncTask explicitly in OnDestroy() method
    // This internal class is parsing the dish data from the website with menu
    @SuppressLint("StaticFieldLeak")
    public class MyParser extends AsyncTask<int[], Void, Void> {

        Element textDate;
        private List<Dish> dishes = new ArrayList<>();
        private List<String> dishTypes = new ArrayList<>();
        private List<String> textsDish = new ArrayList<>();
        private List<String> prices = new ArrayList<>();


        /**
         * Loading an image from web to an ImageView using Picasso
         * @param url url address of image
         * @param imageViewSmall thumb ImageView
         * @param imageViewBig full size ImageView
         */
        void loadImg(String url, final ImageView imageViewSmall, ImageView imageViewBig, final TextView dishDescription)
        {
            imageViewSmall.setBackgroundResource(0);
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

                        //Loading images from local directory
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
                            {
                                imageViewSmall.setBackgroundResource(R.drawable.plate);
                            }
                        }
                    });

            Picasso.with(MainActivity.this).load(Consts.CLOUDINARY_URL + url).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(imageViewBig);
        }


        @SuppressLint("ApplySharedPref")
        @Override
        protected Void doInBackground(int[]... param) {

            int[] pricesElements = new int[] {0, 3, 6, 9, 10};

            Document doc = null;
            Document pic = null;
            mDatabase = mDatabase = FirebaseDatabase.getInstance().getReference();

            try {
                SharedPreferences sharedPref = getSharedPreferences(Consts.preferencesFile, MODE_PRIVATE);
                myurl = sharedPref.getString("URL", myurl);
                day = sharedPref.getString("day", Consts.DAY_TODAY);
                language = sharedPref.getString("language",Consts.LANGUAGE_DE );
                //Adjusting myurl
                myurl = myurl.replaceAll("/" + Consts.LANGUAGE_EN + "/", "/" + language + "/")
                .replaceAll("/" + Consts.LANGUAGE_DE + "/", "/" + language + "/")
                .replaceAll(Consts.DAY_TODAY, day)
                .replaceAll(Consts.DAY_TOMORROW, day);

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                doc = Jsoup.connect(myurl).get();
            } catch (IOException ignored) {
                Log.d("mytag", "JSoup Connection error");
            }
            if (doc != null) {

                int size = 5;
                if (doc.select(".dish-description").size() < 5)
                    size = doc.select(".dish-description").size();

                for (int i = 0; i < size; i++) {
                    textsDish.add(doc.select(".dish-description").get(i).text());
                    dishTypes.add(doc.select("th").get(i+4).text());
                    prices.add(doc.select("td.price").get(pricesElements[i]).text());
                }

                try {
                    textDate = doc.select(".category").get(0);
                } catch (Exception ignored) {
                    Log.d("mytag", "JSoup: date cannot be parsed");
                }
            }



            for (int i = 0; i < textsDish.size(); i++)
            {
                dishes.add(new Dish(dishTypes.get(i), textsDish.get(i), prices.get(i),0f, ""));
            }


           /* writeToPrefs(new String[]{"dish1", "dish2", "dish3", "dish4", "dish5",
                    "dish11", "dish22", "dish33", "dish44", "dish55"} , new String[]{dishes.get(0).searchText(),
                    dishes.get(1).searchText(),dishes.get(2).searchText(),dishes.get(3).searchText(),dishes.get(4).searchText(),
                    dishes.get(0).searchText(), dishes.get(1).displayText(), dishes.get(2).displayText(),
                    dishes.get(3).displayText(), dishes.get(4).displayText()} );*/
            return null;
        }


        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onPostExecute(Void result) {
            clearAllViews();

            // Filling the Menu with dishes
            for (int i = 0; i < dishes.size(); i++ )
            {


                textViewsType.get(i).setText(dishes.get(i).getDishType());
                textViewsPrice.get(i).setText(dishes.get(i).getPrice());
                textViewsDescription.get(i).setText(dishes.get(i).displayText());

                Log.wtf("mytag", "SEARCh TEXT " + dishes.get(i).searchText());
                if (!dishes.get(i).searchText().equals("")) {
                    imageViews.get(i).setVisibility(View.VISIBLE);
                    loadImg(dishes.get(i).getImage(), imageViews.get(i), imageViewsHuge.get(i),
                            textViewsDescription.get(i));
                }

                ratingBars.get(i).setRating(dishes.get(i).getRating());


                if (dishes.get(i).displayText().equals(""))
                    ratingBars.get(i).setVisibility(View.INVISIBLE);
                else
                    ratingBars.get(i).setVisibility(View.VISIBLE);

                final int finalI = i;
                ratingBars.get(i).setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        if (fromUser)
                            MyDialoguesKt.ratingDialog(MainActivity.this, mDatabase, rating,
                                    dishes.get(finalI).getRating(), dishes.get(finalI).searchText(), ratingBar, textViewsVotes.get(finalI),
                                    dishes.get(finalI));
                    }
                });
                mDatabase.child(dishes.get(finalI).searchText()).child("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                            int total = 0, count = 0;
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                int rating = child.getValue(Integer.class);
                                total += rating;
                                count++;
                            }

                            oldRating = total / count;
                            ratingBars.get(finalI).setRating(oldRating);
                            String votes;
                            if (count == 1) votes = " vote";
                            else votes = " votes";
                            textViewsVotes.get(finalI).setText(String.valueOf(count) + votes);
                            dishes.get(finalI).setVotes(votes);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });


            }

            try{
                textViewDate.setText(textDate.text());
                } catch (Exception e) {
                MyDialoguesKt.netErrorDialog(MainActivity.this);
                e.printStackTrace();
            }


        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp!=null)
        mp.cancel(true);
    }


}







