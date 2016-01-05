package com.invofreaks.www.boostup;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by goura on 05-10-2015.
 */
public class System_apps extends Fragment {


    Context context;
    private PackageManager packageManager;
    private List<ApplicationInfo> appList;
    private AppAdapter listadapter = null;
    MainActivity mainActivity;
    SearchView searchList;
    SwipeRefreshLayout Refresh;
    static String PakageName;
    static String AppName;
    Animation alphaSearch;
    ListView installed_apps;
    String pname,aname;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        context = getActivity();
        packageManager = context.getPackageManager();
        View v = inflater.inflate(R.layout.app_list, container, false);
        installed_apps = (ListView) v.findViewById(android.R.id.list);
        Refresh = (SwipeRefreshLayout) v.findViewById(R.id.pullRefresh);

        Restore_apps restore_apps = new Restore_apps(pname,aname);
        packageManager = context.getPackageManager();

        installed_apps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //ApplicationInfo app = appList.get(position);
                TextView packagename = (TextView) view.findViewById(R.id.app_package);
                pname = (String) packagename.getText();

                TextView appname = (TextView) view.findViewById(R.id.app_name);
                aname = (String) appname.getText();

                try {
                    //To start the selected application on selecting from the list
                    Intent intent = packageManager.getLaunchIntentForPackage(pname);
                    Intent sendStuff = new Intent(context.getApplicationContext(), MyAlert.class);
                    PakageName = pname;
                    AppName = aname;
                    if (intent != null) {

                        //startActivity(intent);
                        //Toast.makeText(getApplicationContext(),PakageName,Toast.LENGTH_LONG).show();

                        DialogUp();
                    }

                } catch (ActivityNotFoundException nf) {
                } catch (Exception e) {
                }

                // Get instance of Vibrator from current Context
                Vibrator mVibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                mVibrator.vibrate(80);
            }
        });
        new LoadApplications().execute();

        Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new pullDownRefrest().execute();
            }
        });

        return v;
    }

    //Back dialog choice//////////////////////////////////////////////////////

    // @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        AlertDialog.Builder demoBuilder = new AlertDialog.Builder(context.getApplicationContext());

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            demoBuilder.setMessage("Do you want to EXIT this applicaton ?");
            demoBuilder.setCancelable(false);
            demoBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    mainActivity.finish();
                }
            });
            demoBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                }
            });
            demoBuilder.show();

        }

        return mainActivity.onKeyDown(keyCode, event);
    }

    //////////////////////////////////////////////////////////////////////////

    private List<ApplicationInfo> checkForLunchIntent(List<ApplicationInfo> list) {

        ArrayList<ApplicationInfo> appList = new ArrayList<ApplicationInfo>();
        Collections.sort(appList, null);
        for (ApplicationInfo info : list) {


            try {

                if (packageManager.getLaunchIntentForPackage(info.packageName) != null) {

                    appList.add(info);
                    Collections.sort(appList, new ApplicationInfo.DisplayNameComparator(packageManager));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return appList;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {

            List<ApplicationInfo> applist1 = new ArrayList<>();
            appList = checkForLunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            for (ApplicationInfo applicationInfo : appList) {

                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1)

                    applist1.add(applicationInfo);
            }

            listadapter = new AppAdapter(context.getApplicationContext(), R.layout.list_items, applist1);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            installed_apps.setAdapter(listadapter);
            progress.dismiss();
            super.onPostExecute(aVoid);

        }

        @Override
        protected void onPreExecute() {

            progress = ProgressDialog.show(getActivity(), null, "Loading application info...");
            super.onPreExecute();
        }
    }

    private void DialogUp() {

        MyAlert myAlert = new MyAlert(pname,aname);
        myAlert.show(getActivity().getFragmentManager(), "Options: ");  //getFragmentManager() = internal management of the dialog, "String" workes as an unique key

    }

    ///Pull down refresh class ///////////////////////////////////////////////////////////////////////////////
    private class pullDownRefrest extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    new LoadApplications().execute();
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Refresh.setRefreshing(true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Refresh.setRefreshing(false);
        }
    }
}

