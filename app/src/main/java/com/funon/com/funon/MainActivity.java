package com.funon.com.funon;

import android.app.NativeActivity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.funon.com.funon.fragments.BookSlots;
import com.funon.com.funon.fragments.Organiser;
import com.funon.com.funon.fragments.Venues;
import com.funon.com.funon.fragments.homeFragment;
import com.funon.com.funon.fragments.search;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView headerUsername, headerMobile;
    NetworkChangeReceiver conectivity_reciever;
    public ActionBar actionBar;
    ImageButton searchn,history;
    SessionManager sessionManager=new SessionManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_navigation);

        String st=sessionManager.getPrefs(MainActivity.this,"isloggedin");
        if(st!=null)
            if(st.equals("true")) {

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.addDrawerListener(toggle);
                toggle.syncState();


                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                navigationView.setNavigationItemSelectedListener(this);
                Menu nav_Menu = navigationView.getMenu();

                View header = navigationView.getHeaderView(0);
                headerUsername = (TextView) header.findViewById(R.id.header_username);
                headerMobile = (TextView) header.findViewById(R.id.header_mobile);


                conectivity_reciever = new NetworkChangeReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                    }
                };

                String user = "Bhagvaan das";//manager.getPrefs(NavigationActivity.this, "username");
                String phone = " fasgdsg";//manager.getPrefs(NavigationActivity.this, "mobile");

                headerUsername.setText(user);
                headerMobile.setText(phone);
                FragmentManager fm = getSupportFragmentManager();
               fm.beginTransaction().replace(R.id.frame, new Venues()).commit();
            }
            else
            {
                Intent intent = new Intent(MainActivity.this, log.class);
                startActivity(intent);
            }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            registerReceiver(conectivity_reciever, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }


    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
          /*  startActivity(new Intent(NavigationActivity.this,NavigationActivity.class));
            finish();*/
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                unregisterReceiver(conectivity_reciever);}
        }
        catch (Exception e){

        }

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment=null;
        FragmentManager fm = getSupportFragmentManager();
    /*    if(manager.getPrefs(MainActivity.this,"active").equals("2")||
                manager.getPrefs(MainActivity.this,"active").equals("0"))
            manager.setPrefs(MainActivity.this,"checkonboard",false);
*/

        if (id == R.id.nav_dashboard) {
        /**///    if(sessionManager.getPrefs(NavigationActivity.this,"active").equals("1")) {
       /*         fragment = new OrderTabbedFragment();
                fm.beginTransaction().replace(R.id.frame, fragment).commit();
       */    /* }else if(manager.getPrefs(NavigationActivity.this,"active").equals("2")||
                    manager.getPrefs(NavigationActivity.this,"active").equals("0"))
                new CheckUserActiveOrNot().execute();
*/
        } else if (id == R.id.nav_home) {
            fragment = new homeFragment();
            fm.beginTransaction().replace(R.id.frame, fragment).commit();

        }/* else if (id == R.id.nav_activechem) {
            //Active Channel Fragment
            if(manager.getPrefs(NavigationActivity.this,"active").equals("1")) {
                fragment = new ActiveChannelDashboard();
                fm.beginTransaction().replace(R.id.frame, fragment).commit();
            }else if(manager.getPrefs(NavigationActivity.this,"active").equals("2")||
                    manager.getPrefs(NavigationActivity.this,"active").equals("0"))
                new CheckUserActiveOrNot().execute();

        } else if (id == R.id.nav_placeorder) {
            //place order fragment
            fragment = new ShortageFragment1();
            fm.beginTransaction().replace(R.id.frame, fragment).commit();
        }*/ else if (id == R.id.nav_ordermanage) {
            //OrderManagement fragment
               fragment = new Organiser();
                fm.beginTransaction().replace(R.id.frame, fragment).commit();

        } else if (id == R.id.nav_invoicelist) {
            //Invoice List Fragment
                fragment = new BookSlots();
                fm.beginTransaction().replace(R.id.frame, fragment).commit();

        }
        /*
        else if (id == R.id.nav_distributor) {
            fragment = new FragmentDistributorMapping();
            fm.beginTransaction().replace(R.id.frame, fragment).commit();

        } else if(id==R.id.nav_terms){
            fragment = new fragment_terms_conditions();
            fm.beginTransaction().replace(R.id.frame, fragment).commit();
        }else if(id==R.id.nav_about){
            fragment = new AboutUsFragment();
            fm.beginTransaction().replace(R.id.frame, fragment).commit();
        }
        else if(id==R.id.nav_privacy){
            fragment = new FragmentPrivacy();
            fm.beginTransaction().replace(R.id.frame, fragment).commit();
        }
        else if(id==R.id.nav_contact){
            Intent i=new Intent(NavigationActivity.this,WebActivity.class);
            startActivity(i);
        }
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showActionBar(int ac, final Bundle bundle){

        if(ac==0) {
            final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                    R.layout.actionbar_shortage,
                    null);
            history = (ImageButton) actionBarLayout.findViewById(R.id.history);

            //checkout = (ImageButton) actionBarLayout.findViewById(R.id.checkout);

            // Set up your ActionBar
            actionBar = getSupportActionBar();
            actionBar.show();
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarLayout);

            searchn = (ImageButton) actionBarLayout.findViewById(R.id.search);
            searchn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionBar.hide();

                    final search fragment = new search();
                    fragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, "generatebatch");
                    fragmentTransaction.commitAllowingStateLoss();
                }
            });

            /*checkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                *//*actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayShowCustomEnabled(false);*//*
                    final PreviewFragment fragment = new PreviewFragment();
                    fragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, "generatebatch");
                    fragmentTransaction.commitAllowingStateLoss();

                }
            });*/


            history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actionBar.hide();
                  //  final OrderHistory fragment = new OrderHistory();
                  /*  fragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = ((FragmentActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment, "generatebatch");
                    fragmentTransaction.commitAllowingStateLoss();*/
                }
            });
        }
        /*else if(ac==1){
            final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
                    R.layout.actionbar_home,
                    null);
            ImageButton cart=(ImageButton)actionBarLayout.findViewById(R.id.cart);
            final ImageButton noti=(ImageButton)actionBarLayout.findViewById(R.id.noti);
            new_noti=(Button)actionBarLayout.findViewById(R.id.new_noti);

            int unseen=checkNoti();
            if(unseen>0){
                new_noti.setVisibility(View.VISIBLE);
                new_noti.setText(unseen+"");
            }
            else
                new_noti.setVisibility(View.GONE);
            actionBar = getSupportActionBar();
            actionBar.show();
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbarback));
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarLayout);
            cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Fragment fragment = new ShortageFragment1();
                    FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment).commit();
                }
            });
            noti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Fragment fragment = new notification();
                    FragmentTransaction fragmentTransaction =getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.frame, fragment).commit();
                }
            });
            new_noti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noti.performClick();
                }
            });
        }*/

    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
