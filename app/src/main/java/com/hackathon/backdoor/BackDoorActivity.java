package com.hackathon.backdoor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Aaron Dishman on 1/9/17.
 */
public class BackDoorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, BackDoorFragment.newInstance(), BackDoorFragment.TAG).commit();
    }


}
