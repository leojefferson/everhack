package com.example.everhack;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MemoryGameActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_memory_game, menu);
        return true;
    }
}
