package com.edge.work.travelbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        StatusFragment statusF=(StatusFragment) fm.findFragmentById(R.id.status_bar);
        if(statusF==null || ! statusF.isInLayout()){
            statusF = new StatusFragment();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.status_bar, statusF)
                    .commit();
        }

        MainFragment mainF=(MainFragment) fm.findFragmentById(R.id.main_container);
        if(mainF==null || ! mainF.isInLayout()){
            mainF = new MainFragment();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.main_container, mainF)
                    .commit();
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
