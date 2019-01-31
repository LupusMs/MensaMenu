package com.mikhailsv.lupus.myapplicationjsoup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MyDialogues {

    public static void cafeSelectionDialog(ArrayList<TextView> texts, Context context){
        AlertDialog.Builder myBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        View dialogView = inflater.inflate(R.layout.dialog_cafe_selection, null);
        myBuilder.setView(dialogView);

        myBuilder.setCancelable(false);

        myBuilder.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
        myBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert11 = myBuilder.create();
        alert11.show();
    }
}
