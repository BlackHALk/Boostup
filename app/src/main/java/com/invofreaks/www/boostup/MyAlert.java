package com.invofreaks.www.boostup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by goura on 21-09-2015.
 */
public class MyAlert extends DialogFragment {

    Installed_apps main;
    System_apps sysapp;
    String copySourceDir, copyDestDir;
    File sourceDir, destinationDir;
    Activity currActivity;
    String packagename, appname;

    public MyAlert(String packagename, String appname) {

        this.packagename = packagename;
        this.appname = appname;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        main = new Installed_apps();
        sysapp = new System_apps();
        currActivity = getActivity();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Quick settings: ");
        builder.setIcon(R.drawable.ic_build_black_36dp);


        //List item onclick
        builder.setItems(R.array.options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 4) {

                    uninstallApp();
                } else if (which == 3) {

                    new Backupanduninstall().execute();
                } else if (which == 0) {

                    clearPreferences();
                } else if (which == 1) {

                    ClearCache();
                } else if (which == 2) {

                    ForceStop();
                }
            }
        });
        //cancle button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        // dialog.getWindow().getAttributes().windowAnimations = R.anim.alert_dialog_alpha;

        return dialog;
    }

    private void ForceStop() {

        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packagename));

        startActivity(intent);

    }

    private void ClearCache() {

        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packagename));
        startActivity(intent);
    }

    private void clearPreferences() {

        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packagename));
        startActivity(intent);

    }

    private void uninstallApp() {

        Uri packageUri = Uri.parse("package:" + packagename);
        Intent uninstallIntent =
                new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
        startActivity(uninstallIntent);

    }

    public String getAnyDataDir(final Context context) throws Exception {

        return context.getPackageManager().getPackageInfo(packagename, 0).applicationInfo.sourceDir;
    }

    ////////////////////////////////back & uninstall with asynk or thread :P //////////////////////////////

    private class Backupanduninstall extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {

            //Backup in background

            //copyDestDir = "mnt/external_sd"+ File.separator + "BoostBackup";
            copyDestDir = Environment.getExternalStorageDirectory() + File.separator + "BoostBackup";
            destinationDir = new File(copyDestDir);


            try {
                copySourceDir = getAnyDataDir(getActivity());
                sourceDir = new File(copySourceDir);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!destinationDir.exists() && !destinationDir.isDirectory()) {
                // create empty directory
                if (destinationDir.mkdirs()) {

                    try {
                        //Backup copy

                        FileUtils.copyFileToDirectory(sourceDir, destinationDir);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Log.i("CreateDir: ", "App dir created,Source: " + sourceDir + "\n Destination: " + destinationDir);
                } else {

                    Log.w("CreateDir: ", "Unable to create app dir!");
                }
            } else {

                try {
                    //Backup copy
                    FileUtils.copyFileToDirectory(sourceDir, destinationDir);

                    Log.i("CreateDir: ", "App dir created,Source: " + sourceDir + "\n Destination: " + destinationDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("CreateDir: ", "App dir already exists");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.dismiss();

            currActivity.runOnUiThread(new Runnable() {    //any UI object should run on UI main thread sooooooo :P lets create on3 :P
                @Override
                public void run() {

                    Toast.makeText(currActivity, "Backup successful...", Toast.LENGTH_SHORT).show();
                }
            });

            File FromdestinationDirName = new File(destinationDir, "base.apk");
            File TodestinationDirName = new File(destinationDir, appname + ".apk");
            FromdestinationDirName.renameTo(TodestinationDirName);     //renaming apk names cozz all thhe dam apk r named tht same by default casing overlaping


            //Uninstalling after backup ///////////////////////
            Uri packageUri = Uri.parse("package:" + packagename);
            Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageUri);
            currActivity.startActivity(uninstallIntent);


        }

        @Override
        protected void onPreExecute() {

            progress = ProgressDialog.show(getActivity(), null, "Backuping data, Please wait...");
            super.onPreExecute();
        }
    }

}
