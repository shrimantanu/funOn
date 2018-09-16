package com.funon.com.funon.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.funon.com.funon.Adapter.RecentHistoryAdapter;
import com.funon.com.funon.Adapter.VenueAdapter;
import com.funon.com.funon.Adapter.vAdapter;
import com.funon.com.funon.Adapter.venuePost;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class search extends Fragment  {
    LinearLayout lv;
    //ustomSearchableSpinner sp_delivery;
    public boolean loader=false,addtoRecent=true;
   // List<DistributorModel> distributorlist=new ArrayList<>();
    public RecyclerView recyclerView,recent_searches;
    private ArrayAdapter<String> dadapter;
    vAdapter madapter;
    private static final int REQUEST_CODE = 1234;
    double estimate,ptr,qty;
    String responseStatus="";
    InputMethodManager inputMethodManager;
    String Currentquery="";
    long delays = 1000; // 1 seconds after user stops typing
    long last_text_edits = 0;
    int from=0; Handler handler = new Handler();
    Runnable runnable;

    String scannedObject, distributorId, distributorName, distributorOwnerName, distributoraddress1, distributorMobile, distributorMinOrderAmount, did, selectedDeliveryCode;
    int lFlags ;//= pActivity.getWindow().getDecorView().getSystemUiVisibility();
    public Boolean b=true;
    SessionManager manager;
    public Button filter;

    public Boolean stock_flag=false,scheme_flag=false;
    public Switch exclude_shortage_item;
    ArrayList<String> matches;
    RecentHistoryAdapter recent_adapter;
    List<VenueAdapter> modellist=new ArrayList<>();
    TextView bak;
    public android.support.v7.widget.SearchView searchView;
    //SearchAdapter searchAdapter;
    public static Boolean  isVoice=false;
    public ArrayList<String> recent_list=new ArrayList<>();

    RelativeLayout relativeLayout;
    ProgressBar progressBar;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public String dName="",dId="",vendor_code="";
    private ArrayList<String> Distributor_Value, Distributor_Code, spinnerDistributorValue, spinnerDistributorCode;


    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView=inflater.inflate(R.layout.fragment_search,container,false);
      /*  ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Preferred Chemists");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(null);*/
        manager=new SessionManager();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("venuelist");
        relativeLayout=(RelativeLayout)rootView.findViewById(R.id.relative);
        lv=(LinearLayout)rootView.findViewById(R.id.loading);
        progressBar=(ProgressBar)rootView.findViewById(R.id.progressbar);
        exclude_shortage_item=(Switch)rootView.findViewById(R.id.exshortage_item);
       // sp_delivery = rootView.findViewById(R.id.sp_distributor);
        spinnerDistributorValue = new ArrayList<String>();
        spinnerDistributorCode = new ArrayList<String>();
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
     //   recyclerView.setItemAnimator(new SlideInRight());
        filter=(Button)rootView.findViewById(R.id.selec_dist);
     /*   filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CustomSearchableDialog(getActivity(),SearchMedicines.this).show();
            }
        });
*/ myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                venuePost post = dataSnapshot.getValue(venuePost.class);

                modellist.addAll(post.getVenueadapterlist());
                madapter.setBackup();

                //         List<venuePost> keys=(List<venuePost>) dataSnapshot.getChildren().iterator().next().getValue();

                 /* for(int h=0;h<keys.size();h++) {
                    if(keys.get(h).getUserid().equals(FirebaseAuth.getInstance().getUid()))
                      modellist.addAll(keys.get(h).getVenueadapterlist());
                      }*/

                madapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        final LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recent_searches = (RecyclerView) rootView.findViewById(R.id.recent_searches);
        recent_searches.setLayoutManager(layoutManager);
        final String recent=manager.getPrefs(getActivity(),"recent_searches");
        if(recent.isEmpty())
            recent_searches.setVisibility(View.GONE);
        else {
            recent_searches.setVisibility(View.VISIBLE);
            recyclerView.setPadding(0,110,0,0);
        }
        Log.e("recent:",recent);
        if(!recent.isEmpty())
            recent_list.addAll(Arrays.asList(recent.split("@")));
        for(int g=0;g<recent_list.size();g++){
            Log.e("recent:","@@@@"+recent_list.get(g)+"@@@@@");

            if(recent_list.get(g).equals(" ")||recent_list.get(g).equals(""))
            {recent_list.remove(g);
                g--;
            }
            //kkk
        }


        Log.e("recent:",recent_list.size()+"");
        recent_adapter=new RecentHistoryAdapter(search.this,recent_list);
        if(recent_list.size()==0)
            recent_searches.setPadding(0,0,0,0);
        recent_searches.setAdapter(recent_adapter);
        recent_adapter.notifyDataSetChanged();
        Window window = getActivity().getWindow();
        lFlags = getActivity().getWindow().getDecorView().getSystemUiVisibility();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.white));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        //selected dist..........
        dId=manager.getPrefs(getActivity(),"selectedDID");

        dName=manager.getPrefs(getActivity(),"selectedDName");

        vendor_code=manager.getPrefs(getActivity(),"selectedVendorCode");

        if(!manager.getPrefs(getActivity(),"selectedStockFlag").isEmpty()) {
            if (manager.getPrefs(getActivity(), "selectedStockFlag").contains("true")) {
                stock_flag = true;
            } else stock_flag = false;
        }
        if(!manager.getPrefs(getActivity(),"selectedSchemeFlag").isEmpty()) {
            if (manager.getPrefs(getActivity(), "selectedSchemeFlag").contains("true")) {
                scheme_flag = true;
            } else scheme_flag = false;
        }

        /*if(!dName.isEmpty())
          //  filter.setText(""+dName);
*/
        //manager.setPrefs(getActivity(),"selectedSchemeFlag");


        //Voice Search....................
filter.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        show();
    }
});


        final Button speakButton = (Button) rootView.findViewById(R.id.speakbutton);
        Boolean bp= SpeechRecognizer.isRecognitionAvailable(getActivity());
        if(!bp)
            speakButton.setVisibility(View.GONE);

        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                speakButtonClicked(rootView);

            }
        });

        // wordsList = (ListView) rootView.findViewById(R.id.list);

        // Disable button if no recognition service is present
        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0)
        {
            speakButton.setEnabled(false);
            speakButton.setText("Recognizer not present");
        }


        final Fragment fragment=this;


        madapter=new vAdapter(getActivity() ,null);
        bak=(TextView) rootView.findViewById(R.id.bak);
        //total=(TextView) rootView.findViewById(R.id.total);
        searchView=(SearchView) rootView.findViewById(R.id.search_ch);
        searchView.performClick();
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        bak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
                final InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    if (getActivity().getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                }            }
        });
    /*    exclude_shortage_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    madapter.ExcludeShortageItems(true);

                }
                else{
                    madapter.ExcludeShortageItems(false);
                }
            }
        });*/
        final Handler mHandler=new Handler();

        inputMethodManager =
                (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(
                searchView.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
        searchView.requestFocus();

        ((InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).
                toggleSoftInput(InputMethodManager.SHOW_FORCED,
                        InputMethodManager.HIDE_IMPLICIT_ONLY);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                modellist.clear();
                Log.e("fghjkl;",did+"");
                b=true;
                loader=false;
                if(!query.isEmpty()) {
                    madapter=new vAdapter(getActivity() ,modellist);
                    recyclerView.setAdapter(madapter);
                   // new FetchMedicines(0, query).execute();

                }else{
                    modellist.clear();
                    madapter.notifyDataSetChanged();
                }


                if(addtoRecent)
                    if(!query.isEmpty())
                        if(!recent_list.contains(query)) {

                            manager.setPrefs(getContext(), "recent_searches",   query + "@"+manager.getPrefs(getContext(), "recent_searches"));


                            recent_list.clear();
                            recent_adapter.notifyDataSetChanged();
                            recent_searches.getRecycledViewPool().clear();
                            String recent = manager.getPrefs(getActivity(), "recent_searches");
                            recent_list.addAll(Arrays.asList(recent.split("@")));
                            Log.e("search:",recent);
                            for(int g=0;g<recent_list.size();g++){
                                Log.e("recent:","@@@@"+recent_list.get(g)+"@@@@@");

                                if(recent_list.get(g).equals(" "))
                                {recent_list.remove(g);
                                    g--;
                                }
                            }

                            if (recent_list.size() >= 1){
                                recent_searches.setVisibility(View.VISIBLE);
                                recyclerView.setPadding(0,110,0,60);
                            }
                            if(recent_list.size()>10) {
                                recent_list.remove(recent_list.size()-1);
                                recent_adapter.notifyItemRemoved(0);
                                recent_adapter.notifyItemRangeChanged(1,recent_list.size());
                                manager.setPrefs(getContext(), "recent_searches","");
                                for (int k=recent_list.size()-1;k>=0;k--){
                                    manager.setPrefs(getContext(), "recent_searches",  recent_list.get(k)+"@" + manager.getPrefs(getContext(), "recent_searches") );
                                }
                            }
                            //  recent_adapter.notifyItemRangeChanged(0,recent_list.size());
                            recent_searches.getRecycledViewPool().clear();
                            recent_adapter = new RecentHistoryAdapter(search.this, recent_list);
                            recent_searches.setAdapter(recent_adapter);
                            recent_adapter.notifyDataSetChanged();
                            recent_adapter.notifyItemRangeChanged(0,recent_list.size());


                        }
                addtoRecent = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {

                handler.removeCallbacks(runnable);

                Log.e("asdfghjkl",did+"");


                if(!isVoice){
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            modellist.clear();
                            b=true;
                            loader=false;
                            if(!newText.isEmpty()) {
                                madapter=new vAdapter(getActivity() ,modellist);
                                recyclerView.setAdapter(madapter);
                               // new FetchMedicines(0, newText).execute();
                            }else{
                                modellist.clear();
                                madapter.notifyDataSetChanged();
                            }


                            // Your code here.
                        }
                    };
                    handler.postDelayed(runnable, 1000);
                }
                isVoice=false;

                return false;
            }
        });

//        ac.setDisplayShowTitleEnabled(true);
        // actionBar.setIcon(R.drawable.ic_action_search);

        madapter=new vAdapter(getActivity() ,modellist);//,stock_flag,scheme_flag,dId,vendor_code,SearchMedicines.this);
        recyclerView.setAdapter(madapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (inputMethodManager.isActive()) {
                    if (getActivity().getCurrentFocus() != null) {
                        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                }

                if (!recyclerView.canScrollVertically(1) && b && loader) {
                    from=modellist.size();
                    from+=20;
                    recyclerView.setPadding(0,0,0,70);
                    lv.setVisibility(View.VISIBLE);
                    loader=false;
                  //  new FetchMedicines(from,searchView.getQuery().toString()).execute();
                }
                else if(b==false){
                    lv.setVisibility(View.GONE);
                    recyclerView.setPadding(0,0,0,0);

                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy<0){
                    lv.setVisibility(View.GONE);

                }
                else
                {

                }
            }
        });
        LayoutInflater inflator = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.search_codex, null);
        /* ac.setCustomView(v);*/
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    // Scrolling up
                    recent_searches.animate().translationY(-recent_searches.getHeight()).setDuration(300);
                    //  recyclerView.setPadding(0,0,0,60);


                } else {
                    // Scrolling down


                    recent_searches.animate().translationY(0).setDuration(300);

                }
            }


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int first=layoutManager.findFirstVisibleItemPosition();

//                Log.e("SCROLL CHANGED",first+"         @@@@@@@@@@@@@    "+ recent_list.size()   +recent_list.get(0));
                if(first<1&&recent_list.size()>0){
                    recyclerView.setPadding(0,110,0,60);
                }
                else  if(recent_list.size()==0)
                {
                    recyclerView.setPadding(0,0,0,60);
                }



            }


        });


        return rootView;
    }


  /*  private Runnable input_finish_checkers = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edits + delays - 500)) {

                modellist.clear();
                from=0;
                b=true;
                last_text_edits = System.currentTimeMillis();
                handlers.postDelayed(input_finish_checkers, delays);
                //pagesearch=searchView.getQuery().toString();
                new InvoiceGetter(0, searchView.getQuery().toString()).execute((Void[]) null);
                handlers.removeCallbacks(input_finish_checkers);
            }else{

            }
        }
    };
*/



   /* public void fetchMedicines(int from, String srch) {
        try {
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new GetInterceptor(getActivity())).build();
            String distid=dId;
            if(dId.equals("")||dId.equals("0")){
                distid=null;
            }
            Request request = new Request.Builder()
                    .url(getString(R.string.medicine_url)
                            +"query="+srch
                            +"&did="+distid//+manager.getPrefs(getActivity(),"aid")
                            +"&from="+from
                            +"&vendor_code="+vendor_code
                    )
                    .build();
            Log.e("Book", "medicine: " + request.url() + request.headers());
            try {

                Response response = client.newCall(request).execute();
                //for (int i = 0; i < response.body().toString().length(); i++) {

                final String myResponse = response.body().string();
                //Log.i("Response", "run: " + myResponse);
                Log.e("Response", "run: " + myResponse);
                JSONObject result = new JSONObject(myResponse);
                //JSONObject itemObject = result.getJSONObject("data");
                //total=Integer.parseInt(itemObject.getString("last_page"));
                JSONArray medicineArray = result.getJSONArray("data");
                if(medicineArray.length()<20)
                    b=false;
                responseStatus = result.getString("status");
                Log.i("Response", "run: " + responseStatus+",length "+response.body().toString().length());
                if (responseStatus.contains("true")) {
                    //  Log.i("TAG",""+medicineArray.length()+","+total);
                    //length=chemistArray.length();
                    //arrayinitialize(length);
                    if(medicineArray.length()>=1){

                        for(int x=0;x<medicineArray.length();x++){
                            JSONObject deliveryData=medicineArray.getJSONObject(x);
                            MedicineModel model = new MedicineModel();
                            if (deliveryData.has("brand")) {
                                if (!(deliveryData.isNull("brand"))) {
                                    String d=deliveryData.getString("brand");
                                    int por=d.indexOf(srch);
                                    por++;

                                    Log.e("por",por+"d       "+d);
                                    model.setMedname(d);
                                    // model.setMedname(d.substring(0,por)+"<u>"+d.substring(por,por+srch.length())+"</u>"+d.substring(por+srch.length()));
                                } else {
                                    model.setMedname("");
                                }
                            } else {
                                model.setMedname("");
                            }
                            if (deliveryData.has("marketing_co")) {
                                if (!(deliveryData.isNull("marketing_co"))) {
                                    model.setBrand(deliveryData.getString("marketing_co"));
                                } else {
                                    model.setBrand("");
                                }
                            } else {
                                model.setBrand("");
                            }
                            if (deliveryData.has("mrp")) {
                                if (!(deliveryData.isNull("mrp"))) {
                                    model.setMrp(deliveryData.getString("mrp"));
                                } else {
                                    model.setMrp("0");
                                }
                            } else {
                                model.setMrp("0");
                            }
                            if (deliveryData.has("ptr")) {
                                if (!(deliveryData.isNull("ptr"))) {
                                    model.setPtr(deliveryData.getString("ptr"));
                                } else {
                                    model.setPtr("0");
                                }
                            } else {
                                model.setPtr("0");
                            }
                            if (deliveryData.has("packaging")) {
                                if (!(deliveryData.isNull("packaging"))) {
                                    model.setPkg(deliveryData.getString("packaging"));
                                } else {
                                    model.setPkg("");
                                }
                            } else {
                                model.setPkg("");
                            }
                            if (deliveryData.has("inv")) {
                                if (!(deliveryData.isNull("inv"))) {
                                    model.setStock(deliveryData.getString("inv"));
                                } else {
                                    model.setStock("");
                                }
                            } else {
                                model.setStock("");
                            }
                            if (deliveryData.has("minQty")) {
                                if (!(deliveryData.isNull("minQty"))) {
                                    model.setMinq(deliveryData.getString("minQty"));
                                } else {
                                    model.setMinq("1");
                                }
                            } else {
                                model.setMinq("1");
                            }
                            if (deliveryData.has("maxQty")) {
                                if (!(deliveryData.isNull("maxQty"))) {
                                    model.setMaxq(deliveryData.getString("maxQty"));
                                } else {
                                    model.setMaxq("100");
                                }
                            } else {
                                model.setMaxq("100");
                            }
                            if (deliveryData.has("stepQty")) {
                                if (!(deliveryData.isNull("stepQty"))) {
                                    model.setStepq(deliveryData.getString("stepQty"));
                                } else {
                                    model.setStepq("1");
                                }
                            } else {
                                model.setStepq("1");
                            }
                            if (deliveryData.has("scheme")) {
                                if (!(deliveryData.isNull("scheme"))) {
                                    model.setScheme(deliveryData.getString("scheme"));
                                } else {
                                    model.setScheme("");
                                }
                            } else {
                                model.setScheme("");
                            }
                            if (deliveryData.has("item_code")) {
                                if (!(deliveryData.isNull("item_code"))) {
                                    model.setItemcode(deliveryData.getString("item_code"));
                                } else {
                                    model.setItemcode("");
                                }
                            } else {
                                model.setItemcode("");
                            }
                            if (deliveryData.has("getInv")) {
                                if (!(deliveryData.isNull("getInv"))) {
                                    model.setInv(deliveryData.getString("getInv"));
                                } else {
                                    model.setInv("");
                                }
                            } else {
                                model.setInv("");
                            }
                            if (deliveryData.has("uom_name")) {
                                if (!(deliveryData.isNull("uom_name"))) {
                                    model.setUom_name(deliveryData.getString("uom_name"));
                                } else {
                                    model.setUom_name("");
                                }
                            } else {
                                model.setUom_name("");
                            }
                            if (deliveryData.has("uom_quantity")) {
                                if (!(deliveryData.isNull("uom_quantity"))) {
                                    model.setUom_quantity(deliveryData.getString("uom_quantity"));
                                } else {
                                    model.setUom_quantity("");
                                }
                            } else {
                                model.setUom_quantity("");
                            }
                            if (deliveryData.has("form")) {
                                if (!(deliveryData.isNull("form"))) {
                                    model.setForm(deliveryData.getString("form"));
                                } else {
                                    model.setForm("");
                                }
                            } else {
                                model.setForm("");
                            }
                            if (deliveryData.has("did")) {
                                if (!(deliveryData.isNull("did"))) {
                                    model.setDid(deliveryData.getString("did"));
                                } else {
                                    model.setDid("");
                                }
                            } else {
                                model.setDid("");
                            }

                            if (deliveryData.has("pack")) {
                                if (!(deliveryData.isNull("pack"))) {
                                    model.setPack(deliveryData.getString("pack"));
                                } else {
                                    model.setPack("");
                                }
                            } else {
                                model.setPack("");
                            }


                            if (deliveryData.has("drug_master_id")) {
                                if (!(deliveryData.isNull("drug_master_id"))) {
                                    model.setDrugmasterid(deliveryData.getString("drug_master_id"));
                                } else {
                                    model.setDrugmasterid("");
                                }
                            } else {
                                model.setDrugmasterid("");
                            }
                            model.setBtnstatus(0);
                            if(model.getDid().isEmpty()){
                                if (deliveryData.has("id")) {
                                    if (!(deliveryData.isNull("id"))) {
                                        model.setDrugmasterid(deliveryData.getString("id"));
                                    } else {
                                        model.setDrugmasterid("");
                                    }
                                } else {
                                    model.setDrugmasterid("");
                                }

                            }

                            modellist.add(model);
                            if(x==medicineArray.length()-1)
                                Log.i("Data","Medicines name stored");
                        }

                    }
                } else {


                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    alertDialog.setMessage("Kuch nhi hua yr");
                    alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();

                }
            }catch (JSONException e){

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

*/

    /*class FetchMedicines extends AsyncTask<Void, Void, Void> {

        int from;String srch;
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        FetchMedicines(int from, String srch){
            this.srch=srch;
            this.from=from;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(from==0)
                progressBar.setVisibility(View.VISIBLE);

        }
        protected void onPostExecute(Void param) {
            try{
                progressBar.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);
                loader=true;
                //current
                madapter.notifyDataSetChanged();

                madapter.ExcludeShortageItems(exclude_shortage_item.isChecked());
                Currentquery=srch;
                Log.i("ListSize",""+modellist.size());
            }catch (Exception e){

            }
        }

        protected Void doInBackground(Void... param) {

            fetchMedicines(from,srch);
            return null;
        }


    }
*/
    //Voice Search......................................

    //  private ListView wordsList;

    /**
     * Called with the activity is first created.
     */

    /**
     * Handle the action of the button being clicked
     */
    public void speakButtonClicked(View v)
    {


        startVoiceRecognitionActivity();

    }

    /**
     * Fire an intent to start the voice recognition activity.
     */
    private void startVoiceRecognitionActivity()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).actionBar.hide();


    }

    /**
     * Handle the results from the voice recognition activity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            matches= data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            modellist.clear();
            isVoice=true;
            searchView.setQuery(matches.get(0)+"",false);
       //     new FetchMedicines(0,matches.get(0)).execute();

           /* wordsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                    matches));*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //History Last five..................

    public void addhistory(){


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) getActivity()).showActionBar(0,null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.setStatusBarColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
            window.getDecorView().setSystemUiVisibility(lFlags);
            Log.e("flag",lFlags+"");
        }

    }
    public void show() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_dialog);
        RecyclerView recyclerView;
        TextView clear=(TextView)dialog.findViewById(R.id.clearfilter);
       // im=(ImageView)dialog.findViewById(R.id.upload);
       // final EditText vname=(EditText)dialog.findViewById(R.id.Vname);
        final CheckBox t0=(CheckBox)dialog.findViewById(R.id.t0);
        final CheckBox t1=(CheckBox)dialog.findViewById(R.id.t1);
        final CheckBox t2=(CheckBox)dialog.findViewById(R.id.t2);
        final CheckBox t3=(CheckBox)dialog.findViewById(R.id.t3);
        final CheckBox t4=(CheckBox)dialog.findViewById(R.id.t4);
        final CheckBox t7=(CheckBox)dialog.findViewById(R.id.t7);
        final CheckBox t8=(CheckBox)dialog.findViewById(R.id.t8);
        final CheckBox t9=(CheckBox)dialog.findViewById(R.id.t9);

        final RadioButton r0=(RadioButton)dialog.findViewById(R.id.par0);

        RadioButton r1=(RadioButton)dialog.findViewById(R.id.par1);

     /*   final EditText vloc=(EditText)dialog.findViewById(R.id.VLoc);
*/

clear.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        manager.setPrefs(getContext(),"vbirthday",true);

        manager.setPrefs(getContext(),"vtheamed",true);

        manager.setPrefs(getContext(),"vpool",true);

        manager.setPrefs(getContext(),"vcocktail",true);

        manager.setPrefs(getContext(),"vmarriage",true);

        manager.setPrefs(getContext(),"vchair",true);

        manager.setPrefs(getContext(),"vtable",true);
        manager.setPrefs(getContext(),"vorgini",true);
        manager.setPrefs(getContext(),"vparking",true);

        madapter.clearFilter();
        dialog.dismiss();
    }
});

        TextView can = (TextView) dialog.findViewById(R.id.backto);

        TextView add = (TextView) dialog.findViewById(R.id.addv);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             manager.setPrefs(getContext(),"vbirthday",t0.isChecked());

                manager.setPrefs(getContext(),"vtheamed",t1.isChecked());

                manager.setPrefs(getContext(),"vpool",t2.isChecked());

                manager.setPrefs(getContext(),"vcocktail",t3.isChecked());

                manager.setPrefs(getContext(),"vmarriage",t4.isChecked());

                manager.setPrefs(getContext(),"vchair",t7.isChecked());

                manager.setPrefs(getContext(),"vtable",t8.isChecked());
                manager.setPrefs(getContext(),"vorgini",t9.isChecked());
                manager.setPrefs(getContext(),"vparking",r0.isChecked());
                madapter.filterList();
           /*     VenueAdapter venueAdapter=new VenueAdapter();
                venueAdapter.setVenuelocation(vloc.getText().toString());
                venueAdapter.setVenuename(vname.getText().toString());
                venueAdapter.setVid(FirebaseAuth.getInstance().getUid());
                venueAdapter.setBirthday(t0.isChecked());
                venueAdapter.setTheamed(t1.isChecked());
                venueAdapter.setPool(t2.isChecked());
                venueAdapter.setCocktail(t3.isChecked());
                venueAdapter.setMarriage(t4.isChecked());
                venueAdapter.setChair(t7.isChecked());
                venueAdapter.setTable(t8.isChecked());
                venueAdapter.setOrganizerliable(t9.isChecked());
                venueAdapter.setParking(r0.isChecked());*/
               /* modellist.add(venueAdapter);
                venuePost b=new venuePost();
                b.setUserid(FirebaseAuth.getInstance().getUid());
                b.setVenueadapterlist(modellist);
                myRef.setValue(b);*/
               dialog.dismiss();
            }
        });

        /*im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
            }
        });*/





        dialog.show();



        can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }
    public void addCurrentToRecent(){
        if(addtoRecent)
            if(!Currentquery.isEmpty())
                if(!recent_list.contains(Currentquery)) {



                    manager.setPrefs(getContext(), "recent_searches",   Currentquery + "@"+manager.getPrefs(getActivity(), "recent_searches"));


                    recent_list.clear();
                    recent_adapter.notifyDataSetChanged();
                    recent_searches.getRecycledViewPool().clear();
                    String recent = manager.getPrefs(getActivity(), "recent_searches");
                    recent_list.addAll(Arrays.asList(recent.split("@")));
                    Log.e("search:",recent);
                    for(int g=0;g<recent_list.size();g++){
                        Log.e("recent:","@@@@"+recent_list.get(g)+"@@@@@");

                        if(recent_list.get(g).equals(" "))
                        {recent_list.remove(g);
                            g--;
                        }
                    }

                    if (recent_list.size() >= 1){
                        recent_searches.setVisibility(View.VISIBLE);
                        recyclerView.setPadding(0,110,0,60);
                    }
                    if(recent_list.size()>10) {
                        recent_list.remove(recent_list.size()-1);
                        recent_adapter.notifyItemRemoved(0);
                        recent_adapter.notifyItemRangeChanged(1,recent_list.size());
                        manager.setPrefs(getContext(), "recent_searches","");
                        for (int k=recent_list.size()-1;k>=0;k--){
                            manager.setPrefs(getContext(), "recent_searches",  recent_list.get(k)+"@" + manager.getPrefs(getContext(), "recent_searches") );
                        }
                    }
                    //  recent_adapter.notifyItemRangeChanged(0,recent_list.size());
                    recent_searches.getRecycledViewPool().clear();
                    recent_adapter = new RecentHistoryAdapter(search.this, recent_list);
                    recent_searches.setAdapter(recent_adapter);
                    recent_adapter.notifyDataSetChanged();
                    recent_adapter.notifyItemRangeChanged(0,recent_list.size());


                }
        addtoRecent = true;
    }
}







