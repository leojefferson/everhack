package com.example.everhack;

import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

import com.evernote.client.oauth.android.AuthenticationResult;
import com.evernote.client.oauth.android.EvernoteSession;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.NoteStore.Client;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GamesMenuActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_menu);
        final Button bot1 = (Button) findViewById(R.id.button1);
        final Button bot2 = (Button) findViewById(R.id.button2);
        final Button bot3 = (Button) findViewById(R.id.button3);
        final TextView txt = (TextView) findViewById(R.id.login);
        
        List<Note> list = EverHackGamesActivity.notesList;
        
        String campodenotas = ""+list.size();
        
        int i=0;
        if(list != null)
	        for(Note n : list){
	        	i++;
	        	String s1 = n.getContent();
	        	String s = Util.htmlToPlain(s1);	        	
	        	
	        	campodenotas += i + s + "\n";
	        }
        //txt.setText(campodenotas);
        txt.setText("");

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
