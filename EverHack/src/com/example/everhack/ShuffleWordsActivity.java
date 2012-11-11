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
    @Override
    public void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuffle_words);
        nm = new NoteMixer();
       
        String[] ori = nm.getOriginal();
		String[] emb = nm.getMixed();
		String s = ori[0];
		for(int i=1; i< ori.length; i++)
			s += " "+ori[i];
		s+="\n" + emb[0];
		for(int i=1; i< emb.length; i++)
			s += " "+emb[i];
		
		((TextView)findViewById(R.id.textView1)).setText(s);
		
        final Button bot1 = (Button) findViewById(R.id.button1);

        bot1.setText("texto");
        bot1.setOnClickListener(new View.OnClickListener() {
    		
    		public void onClick(View v) {
    			String resultado = (String) ((TextView)findViewById(R.id.textView1)).getText();
    			String[] respostas = Util.removeEspacos(Util.removeEnter(resultado).split(" "));
    			String[] emb = nm.getMixed();
    			
    			boolean igual = true;
    			for(int i=0; i<respostas.length; i++)
    				if(!respostas[i].equalsIgnoreCase(emb[i]))
    					igual = false;
    			
    			if(igual)
    				((TextView)findViewById(R.id.textView1)).setText("CORRETO!");
    			else
    				((TextView)findViewById(R.id.textView1)).setText("ERRADO!");
    				
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
    	String[] original;
    	String[] embaralhada;
    	public NoteMixer(){
    		original = EverHackGamesActivity.getRandomPhrase();
    		//original = new String[]{"palavra1","palavra2","palavra3"};
    		embaralhada = embaralhar(original);
    	}
    	public String[] getOriginal(){
    		return original;   
    	}
    	public String[] getMixed(){
    		return embaralhada;   
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
