package com.mikhailsv.lupus.myapplicationjsoup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for RecyclerView
 */
public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {

    private List<Dish> dishes;
    private Context context;

    public DishAdapter(List<Dish> dishes, Context context){
        this.dishes = dishes;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView dishDescriptionRW;
        private TextView dishTypeRW;
        private TextView priceRW;
        private TextView textVotesRW;
        private RatingBar ratingBarRW;
        private ImageView imageViewRW;
        private ConstraintLayout constraintLayoutRW;

        // constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            dishDescriptionRW = itemView.findViewById(R.id.dishDescriptionRW);
            dishTypeRW = itemView.findViewById(R.id.dishTypeRW);
            priceRW = itemView.findViewById(R.id.textPriceRW);
            textVotesRW = itemView.findViewById(R.id.textVotesRW);
            ratingBarRW = itemView.findViewById(R.id.ratingBarRW);
            imageViewRW =itemView.findViewById(R.id.imageViewRW);
            constraintLayoutRW =itemView.findViewById(R.id.constraintlayoutRW);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.recycler_view_item, viewGroup, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        if ((i % 2) == 0)
        {
            viewHolder.constraintLayoutRW.setBackgroundColor(Color.parseColor("#F2F2F2"));
        }
        else
        {
            viewHolder.constraintLayoutRW.setBackgroundColor(Color.WHITE);
        }

        final Dish dish = dishes.get(i);
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set item views based on views and data model
        viewHolder.dishDescriptionRW.setText(dish.displayText());
        viewHolder.dishTypeRW.setText(dish.getDishType());
        viewHolder.priceRW.setText(dish.getPrice());
        viewHolder.textVotesRW.setText(dish.getVotes());
        viewHolder.ratingBarRW.setRating(dish.getRating());



        loadCloudinaryImage(dish.searchText(), viewHolder.imageViewRW);
        setRating(mDatabase, dish,
                viewHolder.ratingBarRW, viewHolder.textVotesRW);

        viewHolder.ratingBarRW.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser)
                    MyDialoguesKt.ratingDialog(context, mDatabase, rating,
                            dish.getRating(), dish.searchText(), ratingBar, viewHolder.textVotesRW,
                            dish);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }




    private void loadCloudinaryImage(final String dishText, final ImageView imageViewSmall){

        Picasso.with(context).load(Consts.CLOUDINARY_URL + dishText)
                .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                .resize(250,140).into(imageViewSmall,
                new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    //Loading images from local directory
                    @Override
                    public void onError() {
                        setDishImage(dishText.toLowerCase(), imageViewSmall);
                    }
                });
    }

    /**
     * setting one of the default pictures if dish photo is not in cloudinary db
     * @param dishText
     * @param imageViewSmall
     */
    private void setDishImage(String dishText, ImageView imageViewSmall){


        if (dishText.contains("noodles") || dishText.contains("nudeln"))
            imageViewSmall.setBackgroundResource(R.drawable.noodles);
        else if (dishText.contains("mushroom") || dishText.contains("champignon"))
            imageViewSmall.setBackgroundResource(R.drawable.mushroom);
        else if (dishText.contains("rice") || dishText.contains("reis"))
            imageViewSmall.setBackgroundResource(R.drawable.rice);
        else if (dishText.contains("sausage") || dishText.contains("currywurst"))
            imageViewSmall.setBackgroundResource(R.drawable.currywurst);
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

    /**
     * Updating rating bar and count of the votes
     * @param mDatabase
     * @param dish
     * @param ratingBar
     * @param textVotesRW
     */
    private void setRating(DatabaseReference mDatabase, final Dish dish,
                           final RatingBar ratingBar, final TextView textVotesRW){
        mDatabase.child(dish.searchText()).child("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
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

                    int oldRating = total / count;
                    ratingBar.setRating(oldRating);
                    dish.setRating(oldRating);
                    String votes;
                    if (count == 1) votes = " vote";
                    else votes = " votes";
                    textVotesRW.setText(String.valueOf(count) + votes);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
