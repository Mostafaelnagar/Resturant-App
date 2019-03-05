package com.example.mostafa.eatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mostafa.eatit.Model.User;
import com.example.mostafa.eatit.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {
    EditText editPhone, editPassword;
    Button btnSignIn;
    com.rey.material.widget.CheckBox ckRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btnSignIn = (Button) findViewById(R.id.btnSign);
        editPhone = (EditText) findViewById(R.id.editPhone);
        editPassword = (EditText) findViewById(R.id.editPassword);
        ckRemember = (com.rey.material.widget.CheckBox) findViewById(R.id.ch_remember);
        //init paper
        Paper.init(this);
        //init FireBase

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference Table_user = database.getReference("user");//name of json in firebase
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnected(getBaseContext())) {
//Save password in paper
                    if (ckRemember.isChecked())
                    {
                        Paper.book().write(Common.USER_KEY,editPhone.getText().toString());
                        Paper.book().write(Common.PD_KEY,editPassword.getText().toString());
                    }
                    final ProgressDialog dialog = new ProgressDialog(SignIn.this);
                    dialog.setMessage("Loading...");
                    dialog.show();
                    Table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Check if user is not exist in database
                            if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                                //Get user data
                                dialog.dismiss();
                                User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                                user.setPhone(editPhone.getText().toString());
                                if (user.getPassword().equals(editPassword.getText().toString())) {
                                    Intent intent = new Intent(SignIn.this, Home.class);
                                    Common.currentUser = user;
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Toast.makeText(SignIn.this, "Wrong", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                dialog.dismiss();
                                Toast.makeText(SignIn.this, "User Not exist", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Toast.makeText(SignIn.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}
