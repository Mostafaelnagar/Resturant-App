package com.example.mostafa.eatit.Services;

import android.util.Log;

import com.example.mostafa.eatit.Model.Token;
import com.example.mostafa.eatit.common.Common;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by mostafa on 1/30/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (Common.currentUser != null)
            updateTokenToFirebase(refreshedToken);
    }

    private void updateTokenToFirebase(String refreshedToken) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("Tokens");
        Token token = new Token(refreshedToken, false);//false because this from client to server
        reference.child(Common.currentUser.getPhone()).setValue(token);

    }
}
