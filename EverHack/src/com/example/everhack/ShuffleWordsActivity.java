package com.example.everhack;

import org.w3c.dom.Text;

import android.opengl.Visibility;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShuffleWordsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffle_words);
       
        final Button bot1 = (Button) findViewById(R.id.button1);

        bot1.setText("texto");
        bot1.setOnClickListener(new View.OnClickListener() {
    		
    		public void onClick(View v) {
    			((TextView)findViewById(R.id.textView1)).setText("NOVA String");
//    			((TextView)findViewById(R.id.textView1)).setVisibility(TextView.INVISIBLE);
    		}
    	});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_shuffle_words, menu);
        return true;
    }
    
}
