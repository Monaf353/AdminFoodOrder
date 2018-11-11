package com.develop.windexit.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.develop.windexit.admin.Common.Common;
import com.develop.windexit.admin.Model.adminUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import info.hoang8f.widget.FButton;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    MaterialEditText mphone, mpassword;

    FirebaseDatabase db;
    DatabaseReference users;

    FButton btnSignIn;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        users = db.getReference("Admin");

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(MainActivity.this, Signin.class);
                startActivity(i);*/
                showSignInDialog();
            }
        });
    }

    private void showSignInDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sign In");
        dialog.setMessage("Please use phone number to sign in!");

        LayoutInflater inflater = LayoutInflater.from(this);
        View login_layout = inflater.inflate(R.layout.sign_in_layout, null);

        mphone = login_layout.findViewById(R.id.phoneSign);
        mpassword = login_layout.findViewById(R.id.passwordSign);

        dialog.setView(login_layout);
        dialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {

                users.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(mphone.getText().toString()).exists()) {
                            dialog.dismiss();

                            adminUser user  = dataSnapshot.child(mphone.getText().toString()).getValue(adminUser.class);
                            user.setPhone(mphone.getText().toString());
                            if (user.getPassword().equals(mpassword.getText().toString())) {
                                Intent i = new Intent(MainActivity.this, Home.class);
                                Common.currentUser = user;
                                startActivity(i);
                                finish();
                            } else
                                Toast.makeText(MainActivity.this, "Wrong password", Toast.LENGTH_LONG).show();

                        } else {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "User not exist in Database", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
