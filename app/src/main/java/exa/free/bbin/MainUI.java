package exa.free.bbin;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainUI extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    private long lastPressedTime;
    private static final int PERIOD = 3000;
    NavigationView navigationView;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_ui);

        context = getApplicationContext();

        MobileAds.initialize(this);

        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mAdView.loadAd(new AdRequest.Builder().build());
            }
        });

        if(savedInstanceState == null){
            MenuItem selected = navigationView.getMenu().findItem(R.id.method1);
            selected.setCheckable(true);
            selected.setChecked(true);
            newFragment(0);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
            if(drawer.isDrawerOpen(GravityCompat.START)){
                switch(event.getAction()){
                    case KeyEvent.ACTION_DOWN:
                        if(event.getDownTime() - lastPressedTime < PERIOD){
                            finish();
                        }else{
                            Toast.makeText(context, R.string.press_again_to_exit, Toast.LENGTH_SHORT).show();
                            lastPressedTime = event.getEventTime();
                        }
                        return true;
                }
            }else if(!drawer.isDrawerOpen(GravityCompat.START)){
                drawer.openDrawer(GravityCompat.START);
            }
        }
        return false;
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.method1) {
            MenuItem selected = navigationView.getMenu().findItem(R.id.method1);
            selected.setCheckable(true);
            selected.setChecked(true);
            newFragment(0);
        }else if(id == R.id.method2){
            MenuItem selected = navigationView.getMenu().findItem(R.id.method2);
            selected.setCheckable(true);
            selected.setChecked(true);
            newFragment(1);
        }else if(id == R.id.settings){
            MenuItem selected = navigationView.getMenu().findItem(R.id.settings);
            selected.setCheckable(true);
            selected.setChecked(true);
            newFragment(2);
        }else if(id == R.id.about){
            MenuItem selected = navigationView.getMenu().findItem(R.id.about);
            selected.setCheckable(true);
            selected.setChecked(true);
            newFragment(3);
        }else if(id == R.id.about_busybox){
            MenuItem selected = navigationView.getMenu().findItem(R.id.about_busybox);
            selected.setCheckable(true);
            selected.setChecked(true);
            newFragment(4);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void newFragment(int position){

        Fragment fragment;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        switch(position){

            case 0:
                fragment = new Method1();
                fragmentTransaction.replace(R.id.fragmentHolder, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case 1:
                fragment = new Method2();
                fragmentTransaction.replace(R.id.fragmentHolder, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case 2:
                fragment = new Settings();
                fragmentTransaction.replace(R.id.fragmentHolder, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case 3:
                fragment = new About();
                fragmentTransaction.replace(R.id.fragmentHolder, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case 4:
                fragment = new AboutBusybox();
                fragmentTransaction.replace(R.id.fragmentHolder, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }
    }
}
