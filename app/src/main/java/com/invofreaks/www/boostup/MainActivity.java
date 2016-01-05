package com.invofreaks.www.boostup;
//For any yo yo problem :P: http://stackoverflow.com/questions/15517878/how-to-check-internal-memory-storage-count-in-android

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class MainActivity extends FragmentActivity {

    ProgressBar pgUsage;
    TextView occupiedSpaceText;
    TextView freeSpaceText;
    SearchView searchList;
    PagerTitleStrip pagerTitleStrip;

    static String PakageName;
    static String AppName;

    SharedPreferences appPreference;
    boolean isAppInstalled = false;
    boolean doubleBackToExitPressedOnce = false;

    final float totalSpace = DeviceMemory.getInternalStorageSpace() ;
    final float occupiedSpace = DeviceMemory.getInternalUsedSpace() ;
    final float freeSpace = DeviceMemory.getInternalFreeSpace() ;
    final DecimalFormat outputFormat = new DecimalFormat("#.##");
    static float totalCache;

    ViewPager viewPager = null;
    DrawerLayout drawerLayout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        occupiedSpaceText = (TextView) findViewById(R.id.totalSpace);
        freeSpaceText = (TextView) findViewById(R.id.freeSpace);

        viewPager = (ViewPager) findViewById(R.id.Vpager);
        pagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pagerTitle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new MypagerClass(fragmentManager));


        pgUsage = (ProgressBar) findViewById(R.id.progressBar);
        pgUsage.setMax((int) totalSpace);
        pgUsage.setProgress((int) occupiedSpace);

        if (null != occupiedSpaceText) {
            occupiedSpaceText.setText(outputFormat.format(occupiedSpace) + " MB");
        }

        if (null != freeSpaceText) {
            freeSpaceText.setText(outputFormat.format(freeSpace) + " MB");
        }


        //Fab for search
        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
           // @Override
           // public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                //Clear cache ...... BOOST
                //deleteCache(getApplicationContext());
         //   }
       // });


        ////////////////////
        createShortcut();
        NavDrawerList();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void NavDrawerList() {

        String[] navListItems = getResources().getStringArray(R.array.nav_options);
        int[] img = {R.drawable.ic_get_app_white_36dp, R.drawable.ic_autorenew_white_36dp,
                R.drawable.ic_build_white_36dp, R.drawable.ic_feedback_white_36dp};

        final ArrayList<String> NavList = new ArrayList<String>();
        for (int i = 0; i < navListItems.length; ++i) {
            NavList.add(navListItems[i]);
        }

    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.invofreaks.www.boostup/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.invofreaks.www.boostup/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    private class MypagerClass extends FragmentStatePagerAdapter {

        Fragment fragment = null;

        public MypagerClass(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                fragment = new Installed_apps();
            }

            if (position == 1) {
                fragment = new System_apps();
            }

            if (position == 2) {
                fragment = new Restore_apps(null, null);
            }

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if (position == 0) {
                return "Downloaded Apps";
            }
            if (position == 1) {
                return "System Apps";
            }
            if (position == 2) {
                return "Restore Apps";
            }

            return null;
        }

        @Override
        public int getCount() {

            return 3;
        }
    }


    ///////////////////////Creating shortcut ///////////////////working//////////// check on diff phn /////////
    private void createShortcut() {

        appPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isAppInstalled = appPreference.getBoolean("isAppInstalled", false);

        if (isAppInstalled == false) {
            Intent shortcutIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
            shortcutIntent.setAction(Intent.ACTION_MAIN);

            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Boost up");
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.boostup_icn_aa));
            intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
            getApplicationContext().sendBroadcast(intent);

            SharedPreferences.Editor editor = appPreference.edit();
            editor.putBoolean("isAppInstalled", true);
            editor.commit();
        }


    }

    //Back dialog choice//////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    //////////////////////////////////////////////////////////////////////////

    public static class DeviceMemory {

        public static float getInternalStorageSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            //StatFs statFs = new StatFs("/data");
            float total = ((float) statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
            return total;
        }

        public static float getInternalFreeSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            //StatFs statFs = new StatFs("/data");
            float free = ((float) statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
            return free;
        }

        public static float getInternalUsedSpace() {
            StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            //StatFs statFs = new StatFs("/data");
            float total = ((float) statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
            float free = ((float) statFs.getAvailableBlocks() * statFs.getBlockSize()) / 1048576;
            float busy = total - free;
            return busy;
        }
        ////////////////////////////////////////////////////////////////////////////////////
    }


}



