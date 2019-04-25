package com.mikhailsv.lupus.myapplicationjsoup

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.google.firebase.database.*
import com.squareup.picasso.Callback
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

/**
 * Adapter for RecyclerView
 */
class DishAdapter(private val dishes: List<Dish>, private val context: Context) : RecyclerView.Adapter<DishAdapter.ViewHolder>() {

    inner class ViewHolder
    (itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        val dishDescriptionRW: TextView
        val dishTypeRW: TextView
        val priceRW: TextView
        val textVotesRW: TextView
        val ratingBarRW: RatingBar
        val imageViewRW: ImageView
        val constraintLayoutRW: ConstraintLayout

        init {

            dishDescriptionRW = itemView.findViewById(R.id.dishDescriptionRW)
            dishTypeRW = itemView.findViewById(R.id.dishTypeRW)
            priceRW = itemView.findViewById(R.id.textPriceRW)
            textVotesRW = itemView.findViewById(R.id.textVotesRW)
            ratingBarRW = itemView.findViewById(R.id.ratingBarRW)
            imageViewRW = itemView.findViewById(R.id.imageViewRW)
            constraintLayoutRW = itemView.findViewById(R.id.constraintlayoutRW)
        }
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val context = viewGroup.context
        val inflater = LayoutInflater.from(context)

        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.recycler_view_item, viewGroup, false)

        // Return a new holder instance
        return ViewHolder(contactView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        if (i % 2 == 0) {
            viewHolder.constraintLayoutRW.setBackgroundColor(Color.parseColor("#F2F2F2"))
        } else {
            viewHolder.constraintLayoutRW.setBackgroundColor(Color.WHITE)
        }

        val dish = dishes[i]
        val mDatabase = FirebaseDatabase.getInstance().reference

        // Set item views based on views and data model
        viewHolder.dishDescriptionRW.text = dish.displayText()
        viewHolder.dishTypeRW.text = dish.dishType
        viewHolder.priceRW.text = dish.price
        viewHolder.textVotesRW.text = dish.votes
        viewHolder.ratingBarRW.rating = dish.rating



        loadCloudinaryImage(dish.searchText(), viewHolder.imageViewRW)
        setRating(mDatabase, dish,
                viewHolder.ratingBarRW, viewHolder.textVotesRW)

        viewHolder.ratingBarRW.onRatingBarChangeListener = RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            if (fromUser)
                ratingDialog(context, mDatabase, rating,
                        dish.rating, dish.searchText(), ratingBar, viewHolder.textVotesRW,
                        dish)
        }
    }

    override fun getItemCount(): Int {
        return dishes.size
    }


    private fun loadCloudinaryImage(dishText: String, imageViewSmall: ImageView) {

        Picasso.with(context).load(Consts.CLOUDINARY_URL + dishText)
                .memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                .resize(250, 140).into(imageViewSmall,
                        object : Callback {
                            override fun onSuccess() {

                            }

                            //Loading images from local directory
                            override fun onError() {
                                setDishImage(dishText.toLowerCase(), imageViewSmall)
                            }
                        })
    }

    /**
     * setting one of the default pictures if dish photo is not in cloudinary db
     * @param dishText
     * @param imageViewSmall
     */
    private fun setDishImage(dishText: String, imageViewSmall: ImageView) {


        if (dishText.contains("noodles") || dishText.contains("nudeln"))
            imageViewSmall.setBackgroundResource(R.drawable.noodles)
        else if (dishText.contains("mushroom") || dishText.contains("champignon"))
            imageViewSmall.setBackgroundResource(R.drawable.mushroom)
        else if (dishText.contains("rice") || dishText.contains("reis"))
            imageViewSmall.setBackgroundResource(R.drawable.rice)
        else if (dishText.contains("sausage") || dishText.contains("currywurst"))
            imageViewSmall.setBackgroundResource(R.drawable.currywurst)
        else if (dishText.contains("chicken") || dishText.contains("hänchen"))
            imageViewSmall.setBackgroundResource(R.drawable.chicken)
        else if (dishText.contains("fish") || dishText.contains("fisch"))
            imageViewSmall.setBackgroundResource(R.drawable.fish)
        else if (dishText.contains("pollock") || dishText.contains("seelachs"))
            imageViewSmall.setBackgroundResource(R.drawable.fish)
        else if (dishText.contains("pork") || dishText.contains("schweine"))
            imageViewSmall.setBackgroundResource(R.drawable.pork)
        else if (dishText.contains("roll") || dishText.contains("tasche"))
            imageViewSmall.setBackgroundResource(R.drawable.roll)
        else if (dishText.contains("burger"))
            imageViewSmall.setBackgroundResource(R.drawable.burger)
        else if (dishText.contains("wrap"))
            imageViewSmall.setBackgroundResource(R.drawable.roll)
        else if (dishText.contains("spaghetti"))
            imageViewSmall.setBackgroundResource(R.drawable.spaghetti)
        else if (dishText != "") {
            imageViewSmall.setBackgroundResource(R.drawable.plate)
        }
    }

    /**
     * Updating rating bar and count of the votes
     * @param mDatabase
     * @param dish
     * @param ratingBar
     * @param textVotesRW
     */
    private fun setRating(mDatabase: DatabaseReference, dish: Dish,
                          ratingBar: RatingBar, textVotesRW: TextView) {
        mDatabase.child(dish.searchText()).child("Rating").addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.value != null) {
                    var total = 0
                    var count = 0
                    for (child in dataSnapshot.children) {
                        val rating = child.getValue(Int::class.java)!!
                        total += rating
                        count++
                    }

                    val oldRating = total / count
                    ratingBar.rating = oldRating.toFloat()
                    dish.rating = oldRating.toFloat()
                    val votes: String
                    if (count == 1)
                        votes = " vote"
                    else
                        votes = " votes"
                    textVotesRW.text = count.toString() + votes
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}
