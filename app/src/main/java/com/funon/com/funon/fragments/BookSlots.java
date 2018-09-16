package com.funon.com.funon.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.funon.com.funon.Adapter.VenueAdapter;
import com.funon.com.funon.Adapter.vAdapter;
import com.funon.com.funon.Adapter.venuePost;
import com.funon.com.funon.R;
import com.funon.com.funon.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


public class BookSlots extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
        SessionManager manager=new SessionManager();
        List<VenueAdapter> modellist=new ArrayList<>();
private RecyclerView recyclerView;
        vAdapter madapter;
        Uri selectedImageURI;
        Bitmap selectedImage;
        ImageView im;
        String realPath;
private final static int GALLERY=2;
        TextView search;
        long delays=1000;
        long last_text_edits=0;
private StorageReference mStorageRef;
        Handler handlers=new Handler();
        FirebaseDatabase database;
        DatabaseReference myRef;
        SwipeRefreshLayout mSwipeRefreshLayout;

public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.fragment_book_slots,container,false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Venues");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(null);
       /* FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);*/
        ImageView addVenue=view.findViewById(R.id.addVenue);
        database=FirebaseDatabase.getInstance();
        manager=new SessionManager();

        myRef=database.getReference("venuelist");
        modellist.clear();

        mStorageRef= FirebaseStorage.getInstance().getReference();
        // progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        /*recyclerView=(RecyclerView)view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        madapter=new vAdapter(getActivity(),modellist);
        recyclerView.setAdapter(madapter);
        recyclerView.setPadding(0,0,0,70);
        mSwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener)this);

*/
    final Calendar myCalendar = Calendar.getInstance();

    Button book=view.findViewById(R.id.book);
    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
          //  updateLabel();
        }

    };

    book.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    });
//        addVenue.setOnClickListener(new View.OnClickListener(){
//@Override
//public void onClick(View v){
//
//        }
//        });
//        Log.e("active status",""+manager.getPrefs(getActivity(),"active"));
//        Log.e("active 1"," me aa gya me");
//
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
//@Override
//public void onScrollStateChanged(RecyclerView recyclerView,int newState){
//        super.onScrollStateChanged(recyclerView,newState);
//           /*  if (!recyclerView.canScrollVertically(1) && modellist.size() < count && loader) {
//                    loader = false;
//                    recyclerView.setPadding(0, 0, 0, 70);
//                    lv.setVisibility(View.VISIBLE);
//                    new FetchOrders(modellist.size(),20).execute();
//                } else if (modellist.size() == count) {
//                    recyclerView.setPadding(0, 0, 0, 0);
//                    lv.setVisibility(View.GONE);
//                }*/
//        }
//        });
//        // new GetOrderCount().execute();
//        mSwipeRefreshLayout.post(new Runnable(){
//
//@Override
//public void run(){
//
//        mSwipeRefreshLayout.setRefreshing(false);
//
//        // Fetching data from server
//        //           loadRecyclerViewData();
//        }
//        });
//
//        Log.e("active 2"," me aa gya me");
        myRef.addValueEventListener(new ValueEventListener(){
@Override
public void onDataChange(DataSnapshot dataSnapshot){
        venuePost post=dataSnapshot.getValue(venuePost.class);

        modellist.addAll(post.getVenueadapterlist());

        //         List<venuePost> keys=(List<venuePost>) dataSnapshot.getChildren().iterator().next().getValue();

                 /* for(int h=0;h<keys.size();h++) {
                    if(keys.get(h).getUserid().equals(FirebaseAuth.getInstance().getUid()))
                      modellist.addAll(keys.get(h).getVenueadapterlist());
                      }*/

//        madapter.notifyDataSetChanged();
        }

@Override
public void onCancelled(DatabaseError error){
        // Failed to read value
        Log.w(TAG,"Failed to read value.",error.toException());
        }
        });


        return view;
        }


    @Override
    public void onRefresh() {

    }
}