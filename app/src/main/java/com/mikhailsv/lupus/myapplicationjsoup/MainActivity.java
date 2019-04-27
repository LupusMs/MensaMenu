package com.mikhailsv.lupus.myapplicationjsoup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
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
    private ImageView imageViewNoMenu;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
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


    @SuppressLint({"ClickableViewAccessibility", "ApplySharedPref"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.constraintLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        tomorrow_btn = findViewById(R.id.tomorrow_btn);
        today_btn = findViewById(R.id.today_btn);
        textViewDate = findViewById(R.id.textViewDate);
        imageViewNoMenu = findViewById(R.id.imageViewNoMenu);
        tomorrow_btn.setOnClickListener(this);
        today_btn.setOnClickListener(this);

        imageViewNoMenu.setVisibility(View.GONE);

        //Asks user to rate the app
        MyDialoguesKt.showAppRatingDialog(MainActivity.this);

        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

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

        //MyDialoguesKt.showUpdateDialog(MainActivity.this);
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

        }
    }

    // Memory Leak is prevented with destroying the AsyncTask explicitly in OnDestroy() method
    // This internal class is parsing the dish data from the website with menu
    @SuppressLint("StaticFieldLeak")
    public class MyParser extends AsyncTask<int[], Void, Void> {

        Element textDate;
        private List<String> dishTypes = new ArrayList<>();
        private List<String> textsDish = new ArrayList<>();
        private List<String> prices = new ArrayList<>();


        @SuppressLint("ApplySharedPref")
        @Override
        protected Void doInBackground(int[]... param) {

            dishes = new ArrayList<>();

            int[] pricesElements = new int[] {0, 3, 6, 9, 10};

            Document doc = null;
            Document pic = null;
            mDatabase = FirebaseDatabase.getInstance().getReference();

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
                writeToPrefs("dish" + (i+1) + "" + (i+1), dishes.get(i).displayText());
                writeToPrefs("dish" + (i+1), dishes.get(i).searchText());
            }
            return null;
        }


        @SuppressLint("ClickableViewAccessibility")
        @Override
        protected void onPostExecute(Void result) {

            imageViewNoMenu.setVisibility(View.GONE);

            try{
                textViewDate.setText(textDate.text());
                } catch (Exception e) {
                MyDialoguesKt.netErrorDialog(MainActivity.this);
                e.printStackTrace();
            }

            //Create RecycklerView
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

            // Create adapter passing in the sample user data
            DishAdapter adapter = new DishAdapter(dishes, MainActivity.this);
            // Attach the adapter to the recyclerview to populate items
            recyclerView.setAdapter(adapter);
            // Set layout manager to position the items
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


            // If today Menu is empty
            if (dishes.size() == 0){
                //Show message
                imageViewNoMenu.setVisibility(View.VISIBLE);
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







