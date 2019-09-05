package com.example.myspeed;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.myspeed.base.MyFragmentManager;
import com.example.myspeed.download.DownloadFragment;
import com.example.myspeed.progress.ProgressFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransitionImpl;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    private ProgressFragment progressFragment=ProgressFragment.getInstance();
    private DownloadFragment downloadFragment=new DownloadFragment();
    private MarketFragment marketFragment=new MarketFragment();
    private Fragment[] fragments={downloadFragment,marketFragment};

    public FragmentManager fm=this.getSupportFragmentManager();
    public static MyFragmentManager myFm=new MyFragmentManager();

    private Menu menu;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment=null;
            switch (item.getItemId()) {
                case R.id.navigation_download:
                    fragment=fragments[0];
                    if (!fragment.isAdded()){

                        myFm.push(fragment,fm);

                    }else {
                        myFm.top(fragment,fm);

                    }

                    return true;
                case R.id.navigation_market:
                    fragment=fragments[1];
                    if (!fragment.isAdded()){
                        myFm.push(fragment,fm);

                    }else {
                        myFm.top(fragment,fm);
                    }
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }

            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myFm.push(downloadFragment,fm);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        verifyStoragePermissions(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.progress_menu:
                if (!progressFragment.isAdded()){
                    myFm.push(progressFragment,fm);

                }else {
                    myFm.top(progressFragment,fm);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void verifyStoragePermissions(Activity activity) {
        try {

            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
