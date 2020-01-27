package com.example.playingusingmultitouch;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class MyFirstPingPongActivity extends MainMenu {
    public MyFirstPingPongSurfaceView pingPongSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pingPongSurfaceView=new MyFirstPingPongSurfaceView(this);
        setContentView(pingPongSurfaceView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

}
