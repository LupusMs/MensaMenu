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

import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {

    private List<Dish> dishes;

    public DishAdapter(List<Dish> dishes){
        this.dishes = dishes;
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

        // We also create a constructor that accepts the entire item row
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
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        if ((i % 2) == 0)
        {
            viewHolder.constraintLayoutRW.setBackgroundColor(Color.parseColor("#F2F2F2"));
        }
        else
        {
            viewHolder.constraintLayoutRW.setBackgroundColor(Color.WHITE);
        }

        Dish dish = dishes.get(i);

        // Set item views based on your views and data model
        viewHolder.dishDescriptionRW.setText(dish.displayText());
        viewHolder.dishTypeRW.setText(dish.getDishType());
        viewHolder.priceRW.setText(dish.getPrice());
        viewHolder.textVotesRW.setText(dish.getVotes());
        viewHolder.ratingBarRW.setRating(dish.getRating());
        setDishImage(dish.displayText().toLowerCase(), viewHolder.imageViewRW);
        setRating(FirebaseDatabase.getInstance().getReference(), dish.searchText(), viewHolder.ratingBarRW);
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    private void setDishImage(String dishText, ImageView imageViewSmall){


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

    private void setRating(DatabaseReference mDatabase, String dish, final RatingBar ratingBar){
        mDatabase.child(dish).child("Rating").addListenerForSingleValueEvent(new ValueEventListener() {
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
                    /*String votes;
                    if (count == 1) votes = " vote";
                    else votes = " votes";
                    textViewsVotes.get(finalI).setText(String.valueOf(count) + votes);
                    dishes.get(finalI).setVotes(votes);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
