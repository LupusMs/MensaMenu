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
import android.widget.TextView;

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
    public TextView textView6;
    public TextView textView7;
    public TextView textView8;
    public TextView textViewDate;
    private TextView textPrice1;
    private TextView textPrice2;
    private TextView textPrice3;
    private TextView textPrice4;
    public ImageView imageView5;
    public ImageView imageView6;
    public ImageView imageView7;
    public ImageView imageView8;
    private ImageView tomorrow_btn;
    private ImageView today_btn;
    public Button buttonEn;
    public Button buttonDe;
    public String link5;
    String link6;
    String link7;
    String link8;
    int i;
    String myurl;
    String language;
    String day;
    String cafeMensa;
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
        textView6 = findViewById(R.id.textView6);
        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);
        textViewDate = findViewById(R.id.textViewDate);
        textPrice1 = findViewById(R.id.textPrice1);
        textPrice2 = findViewById(R.id.textPrice2);
        textPrice3 = findViewById(R.id.textPrice3);
        textPrice4 = findViewById(R.id.textPrice4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        buttonDe = findViewById(R.id.buttonDe);
        buttonEn = findViewById(R.id.buttonEn);
        tomorrow_btn = findViewById(R.id.tomorrow_btn);
        today_btn = findViewById(R.id.today_btn);

        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);
        imageView7.setOnClickListener(this);
        imageView8.setOnClickListener(this);
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
            case R.id.imageView5:
                MyParser mp = new MyParser();
                increment[0] = increment[0] + 1;
                mp.execute(increment);
                break;

            case R.id.imageView6:
                mp = new MyParser();
                increment[1] = increment[1] + 1;
                mp.execute(increment);
                break;
            case R.id.imageView7:
                mp = new MyParser();
                increment[2] = increment[2] + 1;
                mp.execute(increment);
                break;
            case R.id.imageView8:
                mp = new MyParser();
                increment[3] = increment[3] + 1;
                mp.execute(increment);
                break;
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
                recreate();
                break;

            case R.id.today_btn:
                sharedPref = getPreferences(MODE_PRIVATE);
                editor = sharedPref.edit();
                editor.putString("day", Consts.DAY_TODAY);
                editor.commit();
                recreate();
                break;


        }
    }


    class MyParser extends AsyncTask<int[], Void, Void> {


        Element text1;
        Element text2;
        Element text3;
        Element text4;
        Element text5;
        Element text6;
        Element text7;
        Element text8;
        Element textDate;
        Element img;
        Element price1;
        Element price2;
        Element price3;
        Element price4;
        String searchtext5, displaytext5;
        String searchtext6, displaytext6;
        String searchtext7, displaytext7;
        String searchtext8, displaytext8;


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
                    text5 = doc.select(".dish-description").get(0);
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

                changeDisplayText mychangeDisplayText = new changeDisplayText();

                try {

                    text5 = doc.select(".dish-description").get(0);


                    //Editing Strings for displaying and for search requests
                    displaytext5 = mychangeDisplayText.displayText(text5.text());
                    searchtext5 = mychangeDisplayText.searchText(text5.text());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    text6 = doc.select(".dish-description").get(1);
                    displaytext6 = mychangeDisplayText.displayText(text6.text());
                    searchtext6 = mychangeDisplayText.searchText(text6.text());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    text7 = doc.select(".dish-description").get(2);
                    displaytext7 = mychangeDisplayText.displayText(text7.text());
                    searchtext7 = mychangeDisplayText.searchText(text7.text());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    text8 = doc.select(".dish-description").get(3);
                    displaytext8 = mychangeDisplayText.displayText(text8.text());
                    searchtext8 = mychangeDisplayText.searchText(text8.text());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            try {
                pic = Jsoup.connect(Consts.SEARCH_ENGINE_URL + searchtext5).get();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (pic != null) {
                img = pic.select("img").get(1 + increment[0]);
                link5 = img.attr("abs:src");
            }

            try {
                pic = Jsoup.connect(Consts.SEARCH_ENGINE_URL + searchtext6).get();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (pic != null) {
                img = pic.select("img").get(1 + increment[1]);
                link6 = img.attr("abs:src");
            }

            try {
                pic = Jsoup.connect(Consts.SEARCH_ENGINE_URL + searchtext7).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (pic != null) {
                img = pic.select("img").get(1 + increment[2]);
                link7 = img.attr("abs:src");
            }

            try {
                pic = Jsoup.connect(Consts.SEARCH_ENGINE_URL + searchtext8).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (pic != null) {
                img = pic.select("img").get(1 + increment[3]);
                link8 = img.attr("abs:src");
            }

            SharedPreferences sharedPref = getSharedPreferences("dishPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("dish1", searchtext5);
            editor.putString("dish2", searchtext6);
            editor.putString("dish3", searchtext7);
            editor.putString("dish4", searchtext8);
            editor.putString("dish11", displaytext5);
            editor.putString("dish22", displaytext6);
            editor.putString("dish33", displaytext7);
            editor.putString("dish44", displaytext8);

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
            if (displaytext5 != null) {
                textView5.setText(displaytext5);
            }
            if (displaytext6 != null) {
                textView6.setText(displaytext6);
            }
            if (displaytext7 != null) {
                textView7.setText(displaytext7);
            }
            if (displaytext8 != null) {
                textView8.setText(displaytext8);
            }
            textViewDate.setText(textDate.text());
            if (link5.equals(link6)) {
            } else {

                //Setting images using Picasso library
                if (displaytext5 != null && !link5.equals("")) {
                    Picasso.with(MainActivity.this).load(link5).into(imageView5);
                }
                if (displaytext6 != null && !link6.equals("")) {
                    Picasso.with(MainActivity.this).load(link6).into(imageView6);
                }
                if (displaytext7 != null && !link7.equals("")) {
                    Picasso.with(MainActivity.this).load(link7).into(imageView7);
                }

                if (displaytext8 != null && !link8.equals("")) {
                    Picasso.with(MainActivity.this).load(link8).into(imageView8);
                }


            }


        }

    }


}







