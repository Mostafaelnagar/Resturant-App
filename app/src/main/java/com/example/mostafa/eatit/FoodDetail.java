package com.example.mostafa.eatit;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.Rating;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.mostafa.eatit.Databases.Database;
import com.example.mostafa.eatit.Model.Food;
import com.example.mostafa.eatit.Model.Order;
import com.example.mostafa.eatit.Model.Ratings;
import com.example.mostafa.eatit.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
//import com.stepstone.apprating.AppRatingDialog;
//import com.stepstone.apprating.listener.RatingDialogListener;
//
//import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class FoodDetail extends AppCompatActivity  {
    TextView food_Name, food_price, food_description;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart, btnrate;
    ElegantNumberButton numberButton;
    String FoodId = "";
    FirebaseDatabase database;
    DatabaseReference food;
    DatabaseReference rating_tbl;
    Food food_model;
    RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);
        database = FirebaseDatabase.getInstance();
        food = database.getReference("Foods");
        rating_tbl = database.getReference("Rating");

        numberButton = (ElegantNumberButton) findViewById(R.id.number_button);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);
       // btnrate = (FloatingActionButton) findViewById(R.id.btnrate);
        //ratingBar = (RatingBar) findViewById(R.id.ratingBar);
//        btnrate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showRatingDialog();
//
//            }
//        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).addToCart(new Order(
                        FoodId,
                        food_model.getName(),
                        numberButton.getNumber(),
                        food_model.getPrice(),
                        food_model.getDiscount()));
                Toast.makeText(FoodDetail.this, "Added To Cart", Toast.LENGTH_SHORT).show();
            }
        });
        food_description = (TextView) findViewById(R.id.food_Description);
        food_Name = (TextView) findViewById(R.id.food_Name);
        food_price = (TextView) findViewById(R.id.food_price);
        food_image = (ImageView) findViewById(R.id.img_food);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapseAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.ExpandAdapter);
        if (getIntent() != null)
            FoodId = getIntent().getStringExtra("FoodId");

        if (!FoodId.isEmpty()) {
            if (Common.isConnected(getBaseContext())) {
                getDeatailFood(FoodId);
               // getRatingsFood(FoodId);
            } else {
                Toast.makeText(FoodDetail.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }

//    private void getRatingsFood(String foodId) {
//        Query query = rating_tbl.orderByChild("foodId").equalTo(FoodId);
//        query.addValueEventListener(new ValueEventListener() {
//            int sum = 0, count = 0;
//
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Ratings item = snapshot.getValue(Ratings.class);
//                    sum += Integer.parseInt(item.getRateValue());
//                    count++;
//                }
//                if (count != 0) {
//                    float average = sum / count;
//                    ratingBar.setRating(average);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void showRatingDialog() {
//        new AppRatingDialog.Builder()
//                .setPositiveButtonText("Submit")
//                .setNegativeButtonText("Cancel")
//                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Good", "Quite Ok", "Very Good", "Excellent"))
//                .setDefaultRating(1)
//                .setTitle("Rate This Food")
//                .setDescription("Please select some stars and give your feedback")
//                .setTitleTextColor(R.color.colorPrimary)
//                .setDescriptionTextColor(R.color.colorPrimary)
//                .setHint("Please Write Your Comment Here")
//                .setHintTextColor(R.color.colorAccent)
//                .setCommentTextColor(android.R.color.white)
//                .setCommentBackgroundColor(R.color.colorPrimaryDark)
//                .setWindowAnimation(R.style.RatingDialogFadAnim)
//                .create(FoodDetail.this)
//                .show();
//
//
//    }

    private void getDeatailFood(final String foodId) {
        food.child(FoodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                food_model = dataSnapshot.getValue(Food.class);
                //Set Food Image
                Picasso.with(getBaseContext()).load(food_model.getImage()).into(food_image);
                collapsingToolbarLayout.setTitle(food_model.getName());
                food_price.setText(food_model.getPrice());
                food_Name.setText(food_model.getName());
                food_description.setText(food_model.getDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    @Override
//    public void onPositiveButtonClicked(int value, @NotNull String comments) {
//
//        final Ratings rating = new Ratings(Common.currentUser.getPhone()
//                , FoodId, String.valueOf(value), comments);
//        rating_tbl.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child(Common.currentUser.getPhone()).exists()) {
//                    //Remove old value
//                    rating_tbl.child(Common.currentUser.getPhone()).removeValue();
//                    //update with new value
//                    rating_tbl.child(Common.currentUser.getPhone()).setValue(rating);
//
//                } else {
//                    //update with new value
//                    rating_tbl.child(Common.currentUser.getPhone()).setValue(rating);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    @Override
//    public void onNegativeButtonClicked() {
//
//    }
}
