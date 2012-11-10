package com.example.everhack;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class EverHackGamesActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ever_hack_games);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_ever_hack_games, menu);
        return true;
    }
}
