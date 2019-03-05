package com.example.mostafa.eatit;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mostafa.eatit.Model.User;
import com.example.mostafa.eatit.common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {
    MaterialEditText editPhone, editPassword, editName;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        btnSignUp = (Button) findViewById(R.id.btnSignUP);
        editPhone = (MaterialEditText) findViewById(R.id.editPhone);
        editPassword = (MaterialEditText) findViewById(R.id.editPassword);
        editName = (MaterialEditText) findViewById(R.id.editName);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference Table_user = database.getReference("user");//name of json in firebase
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isConnected(getBaseContext())) {
                    final ProgressDialog dialog = new ProgressDialog(SignUp.this);
                    dialog.setMessage("Loading...");
                    dialog.show();
                    Table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //Check if user already exist
                            if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                                dialog.dismiss();
                                Toast.makeText(SignUp.this, " Your Phone is already exist", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                User user = new User(editName.getText().toString(), editPassword.getText().toString());
                                Table_user.child(editPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, " Created", Toast.LENGTH_SHORT).show();
                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else {
                    Toast.makeText(SignUp.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        //init FireBase
    }
}
