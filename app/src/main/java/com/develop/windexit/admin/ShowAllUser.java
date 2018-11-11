package com.develop.windexit.admin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.develop.windexit.admin.Model.User;
import com.develop.windexit.admin.ViewHolder.ClientViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowAllUser extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference users;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    MaterialEditText edtAddBalance;
    private String userBalance, finalUsrbalance, sum;
    FirebaseRecyclerAdapter<User, ClientViewHolder> adapter;

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 99;

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

        setContentView(R.layout.activity_show_all_user);

         /* toolbar =  findViewById(R.id.sylBack);
        setSupportActionBar(toolbar);*/
        getSupportActionBar().setTitle("Users");
        // toolbar.setTitleTextColor(Color.WHITE);

        setTitleColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Init View
        recyclerView = findViewById(R.id.recycle_user);
        // recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_from_right);
        recyclerView.setLayoutAnimation(controller);

        //Firebase
        database = FirebaseDatabase.getInstance();
        users = database.getReference("User");

        loadAllUser();

    }

    private void loadAllUser() {
        adapter = new FirebaseRecyclerAdapter<User, ClientViewHolder>
                (User.class, R.layout.client_layout, ClientViewHolder.class, users) {
            @Override
            protected void populateViewHolder(ClientViewHolder viewHolder, final User model, final int position) {

                viewHolder.client_name.setText(model.getName());
                viewHolder.client_phone.setText(model.getPhone());
                Picasso.with(getBaseContext())
                        .load(model.getImage())
                        .into(viewHolder.client_image);

                viewHolder.btnDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showDetailsDialog(adapter.getRef(position).getKey(), model);
                    }
                });

               /* viewHolder.btnAddBalance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddBalance(adapter.getRef(position).getKey(), model);
                    }
                });*/


                viewHolder.btn_Remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeUser(adapter.getRef(position).getKey());
                    }
                });
            }


        };
        adapter.notifyDataSetChanged();//Refresh data if have data changed
        recyclerView.setAdapter(adapter);
        recyclerView.scheduleLayoutAnimation();

    }


    private void showDetailsDialog(final String key, final User model) {

        AlertDialog.Builder userDialog = new AlertDialog.Builder(ShowAllUser.this);
        userDialog.setTitle("Profile of " + model.getName());

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.user_profile, null);
        CircleImageView imageView = (CircleImageView) view.findViewById(R.id.user_image);

        Picasso.with(getBaseContext())
                .load(model.getImage())
                .into(imageView);
        TextView user_name = view.findViewById(R.id.user_name);

        TextView user_phone = view.findViewById(R.id.user_phone);

        TextView user_email = view.findViewById(R.id.user_email);
        user_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialCall(model);

            }
        });

        TextView user_homeAddress = view.findViewById(R.id.user_homeAddress);
        user_name.setText("Name: " + model.getName());
        user_phone.setText("Phone: " + model.getPhone());
        user_email.setText("Email: " + model.getEmail());
        user_homeAddress.setText("Address: " + model.getHomeAddress());
        userDialog.setView(view);
        //userDialog.setIcon(R.drawable.ic_local_shipping_black_24dp);

        userDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        userDialog.show();
    }

    private void DialCall(User model) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + model.getPhone()));

        if (ContextCompat.checkSelfPermission(ShowAllUser.this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ShowAllUser.this, new String[]{android.Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_CALL_PHONE);
        }
        else
        {
            startActivity(intent);
        }
    }

    private void removeUser(final String key) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to remove user from server?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        users.child(key)
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ShowAllUser.this, "User Remove Succeed", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ShowAllUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }


}
