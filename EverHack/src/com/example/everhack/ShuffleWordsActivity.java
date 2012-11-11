package com.example.everhack;

import java.util.Random;

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

	NoteMixer nm;
	String[] original ;
	String[] embaralhado;
    @Override
    public void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffle_words);
        nm = new NoteMixer();
       
        final Button bot1 = (Button) findViewById(R.id.button1);

        bot1.setText("texto");
        bot1.setOnClickListener(new View.OnClickListener() {
    		
    		public void onClick(View v) {
    			String s = original[0];
    			for(int i=1; i< original.length; i++)
    				s += original[i];
    			s+="\n" + embaralhado[0];
    			for(int i=1; i< embaralhado.length; i++)
    				s += embaralhado[i];
    			
    			((TextView)findViewById(R.id.textView1)).setText(s);
//    			((TextView)findViewById(R.id.textView1)).setVisibility(TextView.INVISIBLE);
    		}
    	});
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_shuffle_words, menu);
        return true;
    }
    
    public class NoteMixer{
    	public NoteMixer(){
    		inicio();
    	}

    	
    	public void inicio(){
    		
    		original = EverHackGamesActivity.getRandomPhrase();
    		embaralhado = embaralhar(original);    		
    		
    	}
    	private String[] embaralhar(String[] pieces){
    		String[] embaralhado = new String[pieces.length];
    		embaralhado = pieces.clone();
    	
    		for(int i=0; i<pieces.length; i++){
    			int j = new Random().nextInt(pieces.length);
    			String tmp = embaralhado[i];
    			embaralhado[i] = embaralhado[j];
    			embaralhado[j] = tmp;
    		}
    		
    		return embaralhado;
    		
    	}
    }   
    
}
