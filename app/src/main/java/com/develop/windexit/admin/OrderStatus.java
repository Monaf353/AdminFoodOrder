package com.develop.windexit.admin;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.develop.windexit.admin.Common.Common;
import com.develop.windexit.admin.Common.Request;
import com.develop.windexit.admin.Model.MyResponse;
import com.develop.windexit.admin.Model.Notification;
import com.develop.windexit.admin.Model.Sender;
import com.develop.windexit.admin.Model.Token;
import com.develop.windexit.admin.Remote.APIService;
import com.develop.windexit.admin.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    MaterialSpinner spinnerStatus, SpinnerShiper;
    MaterialEditText edtComment;
    TextView edtStartDate, edtEndDate;
    Button selectDateStart, selectDateEnd;
    APIService mService;

    TextView textView, today_order;
    long size, size2;
    SwipeRefreshLayout swipeRefreshLayout;
    Spinner spinner;
    int mYear, mMonth, mDay;
    String startDate, endDate;

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


        setContentView(R.layout.activity_order_status);

        textView = findViewById(R.id.totalCount);
        today_order = findViewById(R.id.todayOrderCount);

        //view
        swipeRefreshLayout = findViewById(R.id.swipe_layout);


        /* toolbar =  findViewById(R.id.sylBack);
        setSupportActionBar(toolbar);*/
//        getSupportActionBar().setTitle("ORDERS");
//        setTitleColor(Color.WHITE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Orders        ");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Init Service
        mService = Common.getFCMService();
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Request");

        recyclerView = findViewById(R.id.listOrders);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(), R.anim.layout_bottom_up);
        recyclerView.setLayoutAnimation(controller);

        requests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                size = dataSnapshot.getChildrenCount();
                textView.setText("Total Order : " + (int) size);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        requests.orderByChild("date")
                .equalTo(Common.date())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        size2 = dataSnapshot.getChildrenCount();
                        today_order.setText("Today order : " + size2);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        // toolbar.setTitleTextColor(Color.WHITE);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(OrderStatus.this,
                R.layout.custom_layout_item,
                getResources().getStringArray(R.array.oder_view));

        myAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equals("Today Orders")) {

                    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark,
                            android.R.color.holo_orange_dark,
                            android.R.color.holo_blue_dark);

                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            todayOrder();
                        }
                    });

                    //Default, load for first time
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            todayOrder();
                        }
                    });

                    //Toast.makeText(OrderStatus.this,"All work",Toast.LENGTH_SHORT).show();
                } else if (spinner.getSelectedItem().toString().equals("All Orders")) {


                    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark,
                            android.R.color.holo_orange_dark,
                            android.R.color.holo_blue_dark);

                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            loadOrders();
                        }
                    });

                    //Default, load for first time
                    swipeRefreshLayout.post(new Runnable() {
                        @Override
                        public void run() {
                            loadOrders();
                        }
                    });

                } else if (spinner.getSelectedItem().toString().equals("Date Picker")) {

                    showDatePickerDialog();
                }
               /* else {
                    Toast.makeText(OrderStatus.this,"Date Picker",Toast.LENGTH_SHORT).show();
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void showDatePickerDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatus.this);
        alertDialog.setTitle("Date Picker");
        alertDialog.setMessage("Please select start date and end date");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_date = inflater.inflate(R.layout.order_date_picker, null);

        edtStartDate = add_date.findViewById(R.id.edtStartDate);
        selectDateStart = add_date.findViewById(R.id.btnStartDate);
        selectDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderStatus.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                edtStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                startDate = String.valueOf(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        edtEndDate = add_date.findViewById(R.id.edtEndDate);
        selectDateEnd = add_date.findViewById(R.id.btnEndDate);
        selectDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(OrderStatus.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                edtEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                endDate = String.valueOf(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        alertDialog.setView(add_date);

        //alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        //Set button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_green_dark,
                        android.R.color.holo_orange_dark,
                        android.R.color.holo_blue_dark);

                swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadOrdersDate(startDate, endDate);
                    }
                });

                //Default, load for first time
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        loadOrdersDate(startDate, endDate);
                    }
                });


            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();


    }

    private void loadOrdersDate(String startDate, String endDate) {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("date").startAt(startDate).endAt(endDate).limitToLast(5)) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, final Request model, final int position) {

                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                // viewHolder.txtorderDate.setText(Common.getDate(Long.parseLong(adapter.getRef(position).getKey())));

                viewHolder.txtorderDate.setText(model.getDate());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtComment.setText(model.getComment());
                viewHolder.txtOrderPhone.setText(model.getPhone());

                viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(), adapter.getItem(position));
                    }
                });

              /*  viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(adapter.getRef(position).getKey());
                    }
                });*/

                viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent orderDetail = new Intent(OrderStatus.this, OrderDetail.class);
                        Common.currentRequest = model;
                        orderDetail.putExtra("OrderId", adapter.getRef(position).getKey());
                        startActivity(orderDetail);
                    }
                });


            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void todayOrder() {

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("date").equalTo(Common.date())) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, final Request model, final int position) {

                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                // viewHolder.txtorderDate.setText(Common.getDate(Long.parseLong(adapter.getRef(position).getKey())));

                viewHolder.txtorderDate.setText(model.getDate());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtComment.setText(model.getComment());
                viewHolder.txtOrderPhone.setText(model.getPhone());

                viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(), adapter.getItem(position));
                    }
                });

              /*  viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(adapter.getRef(position).getKey());
                    }
                });*/

                viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent orderDetail = new Intent(OrderStatus.this, OrderDetail.class);
                        Common.currentRequest = model;
                        orderDetail.putExtra("OrderId", adapter.getRef(position).getKey());
                        startActivity(orderDetail);
                    }
                });




               /* viewHolder.setItemCliclListener(new ItemCliclListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if(!isLongClick) {
                            Intent intent = new Intent(OrderStatus.this, MapsActivityForOrder.class);
                            Common.currentRequest = model;
                            startActivity(intent);
                            }
                        *//*
                        I will comment this line because is we use Long click
                        else {
                            Intent orderDetail = new Intent(OrderStatus.this, OrderDetail.class);
                            Common.currentRequest = model;
                            orderDetail.putExtra("OrderId",adapter.getRef(position).getKey());
                            startActivity(orderDetail);
                        }*//*
                    }
                });*/

            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }


    private void loadOrders() {
        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, final Request model, final int position) {

                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtorderDate.setText(model.getDate());

                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtComment.setText(model.getComment());
                viewHolder.txtOrderPhone.setText(model.getPhone());

                viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(), adapter.getItem(position));
                    }
                });

              /*  viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(adapter.getRef(position).getKey());
                    }
                });*/

                viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent orderDetail = new Intent(OrderStatus.this, OrderDetail.class);
                        Common.currentRequest = model;
                        orderDetail.putExtra("OrderId", adapter.getRef(position).getKey());
                        startActivity(orderDetail);
                    }
                });


            }
        };

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }

    //Press ctrl + o
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle().equals(Common.DELETE)) {
            deleteOrder(adapter.getRef(item.getOrder()).getKey());
        }
        return super.onContextItemSelected(item);
    }


    private void deleteOrder(final String key) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to remove order?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requests.child(key).removeValue();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(OrderStatus.this, "Order deleted !!", Toast.LENGTH_SHORT).show();
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


    private void showUpdateDialog(String key, final Request item) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(OrderStatus.this);
        alert.setTitle("Update Order");
        alert.setMessage("Please choose status");

        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout, null);

        spinnerStatus = (MaterialSpinner) view.findViewById(R.id.statusSpinner);
        spinnerStatus.setItems("PLACED", "BEING PROCESSED", "COMPLETE");

        edtComment = (MaterialEditText) view.findViewById(R.id.edtComment);
        // edtComment.setText(edtComment.getText().toString());


        alert.setView(view);
        final String localKey = key;
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                item.setStatus(String.valueOf(spinnerStatus.getSelectedIndex()));
                item.setComment(edtComment.getText().toString());

                requests.child(localKey).setValue(item);
                adapter.notifyDataSetChanged();
                sendOrderStatusToUser(localKey, item);

            }
        });

        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();

    }


    //use client send order
    private void sendOrderStatusToUser(final String key, final Request item) {

        DatabaseReference tokens = database.getReference("Tokens");
        tokens.child(item.getPhone())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Token token = dataSnapshot.getValue(Token.class);
                        Notification notification = new Notification("Admin", "Your order " + key + " was updated");
                        Sender content = new Sender(token.getToken(), notification);
                        mService.sendNotification(content)
                                .enqueue(new Callback<MyResponse>() {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                        if (response.body().success == 1) {
                                            Toast.makeText(OrderStatus.this, "Order was Updated", Toast.LENGTH_LONG).show();
                                            //finish();
                                        } else {
                                            Toast.makeText(OrderStatus.this, " Order was updated but Failed", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t) {
                                        Log.e("Error", t.getMessage());
                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }


}


