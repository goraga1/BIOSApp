package com.example.lenovo.hkgorprivateapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by GorA on 9/24/16.
 */

public class Activity2 extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity2);

        //  setupGridLayout();


    }


    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(), v.getId() + "", Toast.LENGTH_SHORT).show();

    }
}
