package com.example.everhack;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class GamesMenuActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_menu);
        final Button bot1 = (Button) findViewById(R.id.button1);
        final Button bot2 = (Button) findViewById(R.id.button2);

        bot1.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MemoryGameActivity.class));
			}
		});
        bot2.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ShuffleWordsActivity.class));
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_games_menu, menu);
        return true;
    }
}
