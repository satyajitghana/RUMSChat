package com.msruas.debug.rumschat.landingActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.msruas.debug.rumschat.APIServices.RUMSService;
import com.msruas.debug.rumschat.InitialActivity.LoginActivity;
import com.msruas.debug.rumschat.R;
import com.msruas.debug.rumschat.fragments.ChatsFragment;
import com.msruas.debug.rumschat.fragments.GroupsFragment;
import com.msruas.debug.rumschat.model.ResponseMessage;
import com.msruas.debug.rumschat.model.User;
import com.msruas.debug.rumschat.network.RUMSAPI;
import com.msruas.debug.rumschat.utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;


public class ChatActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private SharedPreferences sharedPreferences;

    private void initSharedPreferences() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ChatActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        // Initilize Shared Preferences
        initSharedPreferences();

        //Floating Action Button
        FloatingActionButton findUsers = findViewById(R.id.fab);
        findUsers.setOnClickListener((view -> {
            startActivity(new Intent(ChatActivity.this, UsersList.class));
        }));

        //The HamBurger switch thingy
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = findViewById(R.id.view_pager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_chats);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_groups);

        String _regNo = sharedPreferences.getString(Constants.REGNO, null);
        //String _regNo = "17ETCS002159";
        // Get the Name
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        RUMSAPI rumsapi = RUMSService.getAPI();
        compositeDisposable.add(rumsapi.getUser(_regNo)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe((response) -> {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    String _name = response.getMessage();
                    //Log.d("WARNING", _name);
                    editor.putString(Constants.NAME, _name);
                    editor.apply();
                }, (error) -> {
                    if (error instanceof HttpException) {
                        Gson gson = new GsonBuilder().create();
                        try {
                            String errBody = ((HttpException)error).response().errorBody().string();
                            ResponseMessage response = gson.fromJson(errBody,ResponseMessage.class);
                            Toast.makeText(ChatActivity.this, response.getMessage(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else Toast.makeText(ChatActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                }));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment chatFragment = new ChatsFragment();
        //Bundle
        adapter.addFragment(chatFragment, "CHATS");
        adapter.addFragment(new GroupsFragment(), "GROUPS");

        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // side waale teen dots ke menu ke liye
        // getMenuInflater().inflate(R.menu.account_drawer, menu);
        return true;
    }

    // ye side waale teen dots ke menu ke liye on section
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_account_info) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}