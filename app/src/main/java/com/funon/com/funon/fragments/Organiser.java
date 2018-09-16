package com.funon.com.funon.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.NativeActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.funon.com.funon.Adapter.VenueAdapter;
import com.funon.com.funon.Adapter.vAdapter;
import com.funon.com.funon.MainActivity;
import com.funon.com.funon.R;
import com.funon.com.funon.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class Organiser extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SessionManager manager=new SessionManager();
    List<VenueAdapter> modellist = new ArrayList<>();
    private RecyclerView recyclerView;
    vAdapter madapter;
    Uri selectedImageURI;
    Bitmap selectedImage;
    ImageView im;
    String realPath;
    private final static int GALLERY=2;
    TextView search;
    long delays = 1000;
    long last_text_edits = 0;
    private StorageReference mStorageRef;
    Handler handlers = new Handler();
    FirebaseDatabase database;
    DatabaseReference myRef;
    SwipeRefreshLayout mSwipeRefreshLayout;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_organiser, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Venues");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setIcon(null);
       /* FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);*/
        ((MainActivity) getActivity()).showActionBar(0,null);
        ImageView addVenue=view.findViewById(R.id.addVenue);
        database = FirebaseDatabase.getInstance();
        manager = new SessionManager();

        myRef = database.getReference("venuelist");
        modellist.clear();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        // progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
      /*  recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        madapter = new vAdapter(getActivity(), modellist);
        recyclerView.setAdapter(madapter);
        recyclerView.setPadding(0, 0, 0, 70);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) this);
        addVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
        Log.e("active status", "" + manager.getPrefs(getActivity(), "active"));
        Log.e("active 1", " me aa gya me");

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
               *//* if (!recyclerView.canScrollVertically(1) && modellist.size() < count && loader) {
                    loader = false;
                    recyclerView.setPadding(0, 0, 0, 70);
                    lv.setVisibility(View.VISIBLE);
                    new FetchOrders(modellist.size(),20).execute();
                } else if (modellist.size() == count) {
                    recyclerView.setPadding(0, 0, 0, 0);
                    lv.setVisibility(View.GONE);
                }*//*
            }
        });
        // new GetOrderCount().execute();
        mSwipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                mSwipeRefreshLayout.setRefreshing(false);

                // Fetching data from server
                //           loadRecyclerViewData();
            }
        });

        Log.e("active 2", " me aa gya me");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               *//* Map<String, List<VenueAdapter>> post = (Map<String, List<VenueAdapter>>)dataSnapshot.getValue();
               List<String> keys=new ArrayList<>();
               keys.addAll(post.keySet());
               for(int g=0;g<keys.size();g++){
                  for(int h=0;h<post.get(keys.get(g)).size();h++) {
                      modellist.add(post.get(keys.get(g)).get(h));
                  }
               }
               madapter.notifyDataSetChanged();
            *//*}

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

*/
        return view;
    }

    public void show() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_venues_add);
        RecyclerView recyclerView;
        im=(ImageView)dialog.findViewById(R.id.upload);
        final EditText vname=(EditText)dialog.findViewById(R.id.Vname);

        final EditText vloc=(EditText)dialog.findViewById(R.id.VLoc);




        TextView can = (TextView) dialog.findViewById(R.id.backto);

        TextView add = (TextView) dialog.findViewById(R.id.addv);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStorageRef = FirebaseStorage.getInstance().getReference();
                //   selectedImageURI=getImageUri(getContext(),selectedImage);
                if(realPath!=null&&!vname.getText().toString().isEmpty()){

                    Uri file = Uri.fromFile(new File(realPath));
                    StorageReference riversRef = mStorageRef.child("images/"+vname.getText()+".jpg");

                    riversRef.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                }
                            });



                }
                VenueAdapter venueAdapter=new VenueAdapter();
                venueAdapter.setVenuelocation(vloc.getText().toString());
                venueAdapter.setVenuename(vname.getText().toString());
                venueAdapter.setVid(FirebaseAuth.getInstance().getUid());
                modellist.add(venueAdapter);
                Map<String,List<VenueAdapter>> gh=new HashMap<>();
                gh.put(""+FirebaseAuth.getInstance().getUid(),modellist);

                myRef.setValue(gh);
            }
        });
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
            }
        });





        dialog.show();



        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }
    @Override
    public void onRefresh() {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY && data != null) {
            try {

                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageURI);
                selectedImageURI = data.getData();
                realPath=getRealPathFromURI(selectedImageURI,getActivity());
                InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImageURI);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                if(im!=null){
                    im.setImageBitmap(selectedImage);
                }

            } catch (Exception e) {
                Log.e("exception", "" + e.getMessage());
            }
        }
    }
/*

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
*/

    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }


}