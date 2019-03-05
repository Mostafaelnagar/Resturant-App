package com.example.mostafa.eatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mostafa.eatit.Model.User;
import com.example.mostafa.eatit.R;
import com.example.mostafa.eatit.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

import static com.example.mostafa.eatit.R.id.editPhone;

public class Main2Activity extends AppCompatActivity {
    Button btnSignUp, btnSignIn;
    TextView txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        txtSlogan = (TextView) findViewById(R.id.txtSlogan);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/Nabila.ttf");
        txtSlogan.setTypeface(typeface);
        Paper.init(this);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this, SignIn.class));
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main2Activity.this, SignUp.class));

            }
        });
        String user=Paper.book().read(Common.USER_KEY);
        String pwd=Paper.book().read(Common.PD_KEY);
        if (user !=null && pwd !=null)
        {
            if (!user.isEmpty() && !pwd.isEmpty())
                login(user,pwd);
        }
    }

    private void login(final String phone, final String pwd) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference Table_user = database.getReference("user");//name of json in firebase
        if (Common.isConnected(getBaseContext())) {


            Table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Check if user is not exist in database
                    if (dataSnapshot.child(phone).exists()) {

                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);
                        if (user.getPassword().equals(pwd)) {
                            Intent intent = new Intent(Main2Activity.this, Home.class);
                            Common.currentUser = user;
                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(Main2Activity.this, "Wrong", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(Main2Activity.this, "User Not exist", Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(Main2Activity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
