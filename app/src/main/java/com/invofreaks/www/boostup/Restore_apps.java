package com.invofreaks.www.boostup;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by goura on 05-10-2015.
 */
public class Restore_apps extends Fragment {

    Context context;
    SwipeRefreshLayout Refresh;

    String copyDestDir;
    File destinationDir;
    Activity currActivity;
    ListView installed_apps;
    TextView ShowBanner;
    String pname,aname;
    ArrayList<String> FilesInFolder;

    public Restore_apps(String pname,String aname){

        this.pname = pname;
        this.aname = aname;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        View v = inflater.inflate(R.layout.restore_apps, container, false);
        currActivity = getActivity();
        Refresh = (SwipeRefreshLayout) v.findViewById(R.id.pullRefresh);
        ShowBanner = (TextView) v.findViewById(R.id.banner);
        installed_apps = (ListView) v.findViewById(R.id.restoreAppsList);

        installed_apps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String appName = String.valueOf((installed_apps.getItemAtPosition(position)));
                String restore = copyDestDir + File.separator + appName;
                File backupDir = new File(copyDestDir);

                RestoreDialog restoreDialogPass = new RestoreDialog(restore,appName,backupDir);
                restoreDialogPass.show(getActivity().getFragmentManager(), "Options: ");

                // Get instance of Vibrator from current Context
                Vibrator mVibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                mVibrator.vibrate(80);

            }
        });

        Refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new pullDownRefrest().execute();
            }
        });


        restoreFiles();

        return v;
    }


    //////////////////////////////Restore /////////////////// T1 ////////////////////////

    public void restoreFiles() {

        File backupDir = new File(Environment.getExternalStorageDirectory() + File.separator + "BoostBackup");
        copyDestDir = Environment.getExternalStorageDirectory() + File.separator + "BoostBackup";
        destinationDir = new File(copyDestDir);

        File[] check = backupDir.listFiles();

        if (!backupDir.exists() && !backupDir.isDirectory()) {
            Log.w("Restore Dir: ", "Dir not found");
        } else {

            ShowBanner.setVisibility(getView().GONE);
            FilesInFolder = GetFiles(copyDestDir);
            ArrayAdapter adapter = new ArrayAdapter<String>(context.getApplicationContext(), R.layout.restore_list_row_layout, FilesInFolder);
            installed_apps.setAdapter(adapter);

        }


    }
    ////////////////////////////////////////////////////////////////////////////////////


    private ArrayList<String> GetFiles(String path) {
        ArrayList<String> arr2 = new ArrayList<String>();
        File file = new File(path);
        File[] allfiles = file.listFiles();

        if (allfiles.length == 0) {

            return null;
        } else {
            for (int i = 0; i < allfiles.length; i++) {
                arr2.add(allfiles[i].getName());
            }
        }
        return arr2;
    }

    ///Pull down refresh class ///////////////////////////////////////////////////////////////////////////////
    private class pullDownRefrest extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    restoreFiles();
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

