package com.invofreaks.www.boostup;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

/**
 * Created by goura on 10-11-2015.
 */
public class RestoreDialog extends DialogFragment {

    String TheRestoreAPK, appname;
    File backupDir;

    public RestoreDialog(String TheRestoreAPK, String appname, File backupDir) {

        this.TheRestoreAPK = TheRestoreAPK;
        this.appname = appname;
        this.backupDir = backupDir;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Quick settings: ");
        builder.setIcon(R.drawable.ic_build_black_36dp);


        //List item onclick
        builder.setItems(R.array.restoreStr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {

                    restore();
                } else if (which == 1) {

                    shareApk();
                } else if (which == 2) {

                    deleteApk();
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
        return dialog;
    }

    private void deleteApk() {

        Uri appToDelPath = Uri.fromFile(new File(TheRestoreAPK));
        File apkToDel = new File(appToDelPath.getPath());
        apkToDel.delete();
        Toast.makeText(getActivity(),"File deleted: "+apkToDel,Toast.LENGTH_SHORT).show();

        File[]check = backupDir.listFiles();
        if (check.length == 0){ //Deleting the backup folder
            backupDir.delete();
            Toast.makeText(getActivity(),"Backup folder deleted",Toast.LENGTH_SHORT).show();

        }

    }

    private void shareApk() {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri fileData = Uri.parse(TheRestoreAPK);
        sharingIntent.setType("application/zip");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, fileData);
        startActivity(Intent.createChooser(sharingIntent, "Share APK using: "));
    }

    private void restore() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(TheRestoreAPK)), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
