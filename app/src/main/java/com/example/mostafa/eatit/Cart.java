package com.example.mostafa.eatit;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafa.eatit.Databases.Database;
import com.example.mostafa.eatit.Model.MyResponse;
import com.example.mostafa.eatit.Model.Notification;
import com.example.mostafa.eatit.Model.Order;
import com.example.mostafa.eatit.Model.Request;
import com.example.mostafa.eatit.Model.Sender;
import com.example.mostafa.eatit.Model.Token;
import com.example.mostafa.eatit.Remote.APIService;
import com.example.mostafa.eatit.ViewHolder.CartAdapter;
import com.example.mostafa.eatit.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference request;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView txtTotalPrice;
    FButton btnPlaceOrder;
    List<Order> orders = new ArrayList<>();
    CartAdapter cartAdapter;
    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //init service
        mService = Common.getFCMService();
        database = FirebaseDatabase.getInstance();
        request = database.getReference("Requests");
        recyclerView = (RecyclerView) findViewById(R.id.listcart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnPlaceOrder = (FButton) findViewById(R.id.btnPlaceOfOrder);
        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orders.size() > 0)
                    showAlertDialog();
                else
                    Toast.makeText(Cart.this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            }
        });
        loadListFood();

    }

    public void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One more step");
        alertDialog.setMessage("Enter your address");
        LayoutInflater inflater = this.getLayoutInflater();
        View order_address_comment = inflater.inflate(R.layout.order_comment_layout, null);
        final MaterialEditText edt_address = (MaterialEditText) order_address_comment.findViewById(R.id.editaddress);
        final MaterialEditText edt_Comment = (MaterialEditText) order_address_comment.findViewById(R.id.editcomment);
        alertDialog.setView(order_address_comment);
        alertDialog.setIcon(R.drawable.shopping);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Request re = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edt_address.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        "0",
                        edt_Comment.getText().toString(),
                        orders);


                //Send Cart to FireBase
                String order_number = String.valueOf(System.currentTimeMillis());
                request.child(order_number).setValue(re);
                //Delete Cart
                new Database(getBaseContext()).cleanCart();
                sendNotificationOrder(order_number);
//                Toast.makeText(Cart.this, "Thank You", Toast.LENGTH_SHORT).show();
//                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void sendNotificationOrder(final String order_number) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = reference.orderByChild("isServerToken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot PostSnapshot : dataSnapshot.getChildren()) {
                    Token ServerToken = PostSnapshot.getValue(Token.class);
//create to send notifi
                    Notification notification = new Notification("Eat", "You have new order" + order_number);
                    Sender content = new Sender(ServerToken.getToken(), notification);
                    mService.sendNotification(content).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success == 1) {
                                    Toast.makeText(Cart.this, "Thank you ,Order placed", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(Cart.this, "Failed!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Log.i("ERROR", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void loadListFood() {
        orders = new Database(this).getCarts();
        cartAdapter = new CartAdapter(orders, this);
        cartAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(cartAdapter);

        int Total = 0;
        for (Order order : orders)
            Total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));

        Locale locale = new Locale("en", "us");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(fmt.format(Total));

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.Delete))
            deleteCart(item.getOrder());
        return true;
    }

    private void deleteCart(int position) {
        //we will remove item from listOrder by position
        orders.remove(position);
        //we will delete all old data from Sqlite
        new Database(this).cleanCart();
        //we will add new data from listOrders to Sqlite
        for (Order item : orders)
            new Database(this).addToCart(item);
        loadListFood();
    }

}
