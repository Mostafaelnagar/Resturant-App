package com.example.mostafa.eatit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.mostafa.eatit.Databases.Database;
import com.example.mostafa.eatit.InterFace.ItemClicklistener;
import com.example.mostafa.eatit.Model.Food;
import com.example.mostafa.eatit.ViewHolder.FoodViewHolder;
import com.example.mostafa.eatit.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Food_List extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference foodList;
    Database localDatabase;
    RecyclerView recycler_food;
    RecyclerView.LayoutManager layoutManager;
    String CatId = "";
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    //SearchFuction
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food__list);
        //init database
        localDatabase = new Database(this);
        //init firebase
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");
        recycler_food = (RecyclerView) findViewById(R.id.recycler_food);
        recycler_food.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_food.setLayoutManager(layoutManager);
        //Get Intent Here
        if (getIntent() != null) {
            CatId = getIntent().getStringExtra("catId");
            if (!CatId.isEmpty() && CatId != null) {
                if (Common.isConnected(getBaseContext()))
                    loadListFood(CatId);
                else {
                    Toast.makeText(Food_List.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        loadSuggest();
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//when user type their
                List<String> suggest = new ArrayList<String>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }

                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //when Search Bar is close
                //Restore original adapter
                if (!enabled)
                    recycler_food.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });


    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("Name").equalTo(text.toString())) {
            @Override
            protected void populateViewHolder(final FoodViewHolder viewHolder, final Food model, final int position) {
                viewHolder.food_Name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_Image);
                //Add Favo
                if (localDatabase.isFavorites(adapter.getRef(position).getKey()))
                    viewHolder.fav_Image.setImageResource(R.drawable.ic_favorite_black_24dp);

                //Change state of favo
                viewHolder.fav_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!localDatabase.isFavorites(adapter.getRef(position).getKey()))
                        {
                            localDatabase.addFavorites(adapter.getRef(position).getKey());
                            viewHolder.fav_Image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(Food_List.this, ""+model.getName()+"IS ADD TO FAVORITE", Toast.LENGTH_SHORT).show();
                        }else
                        {
                            localDatabase.removeFromFavorites(adapter.getRef(position).getKey());
                            viewHolder.fav_Image.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                            Toast.makeText(Food_List.this, ""+model.getName()+"IS REMOVE FAVORITE", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                final Food local = model;
                viewHolder.setItemClicklistener(new ItemClicklistener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(Food_List.this, FoodDetail.class);
                        intent.putExtra("FoodId", searchAdapter.getRef(position).getKey());
                        startActivity(intent);

                    }
                });
            }
        };
        recycler_food.setAdapter(searchAdapter);
    }


    private void loadSuggest() {
        foodList.orderByChild("MenuId").equalTo(CatId)
                .addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Food item = postSnapshot.getValue(Food.class);
                            suggestList.add(item.getName());
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void loadListFood(String catId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("menuId").equalTo(CatId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.food_Name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_Image);
                final Food local = model;
                viewHolder.setItemClicklistener(new ItemClicklistener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(Food_List.this, FoodDetail.class);
                        intent.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(intent);

                    }
                });
            }
        };
        recycler_food.setAdapter(adapter);
    }
}
