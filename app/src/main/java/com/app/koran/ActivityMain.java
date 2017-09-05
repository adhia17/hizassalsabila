package com.app.koran;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.koran.fragment.FragmentCategory;
import com.app.koran.fragment.FragmentHome;
import com.app.koran.fragment.FragmentNews;

//import com.roughike.bottombar.BottomBar;
//import com.roughike.bottombar.OnTabReselectListener;
//import com.roughike.bottombar.OnTabSelectListener;

public class ActivityMain extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

//    private TextView mTextMessage;
//    private Button Button1;
//    private Toolbar toolbar;
//    private NavigationView navigationView;
//    private DrawerLayout drawerLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BottomNavigationView buttonBar = (BottomNavigationView) findViewById(R.id.navigation);
        buttonBar.setOnNavigationItemSelectedListener(this);
        onNavigationItemSelected(buttonBar.getMenu().getItem(0));
    }

    private void displaySelectedScreen(int itemId) {

        Fragment fragment = null;
        switch (itemId) {
            case R.id.navigation_home:
                fragment = new FragmentHome();
                break;
            case R.id.navigation_news:
                fragment = new FragmentNews();
                break;
            case R.id.navigation_about:
                fragment = new FragmentCategory();
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment);
            ft.addToBackStack("tag").commit();
        }


    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    };
}
