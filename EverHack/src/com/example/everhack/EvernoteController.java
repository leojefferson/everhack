package com.example.everhack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

import android.os.AsyncTask;
import android.util.Log;

import com.evernote.client.oauth.android.EvernoteSession;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.NoteStore.Client;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.example.everhack.EvernoteController.NoteGetter;

public class EvernoteController{
	
	public List<Note> notesList;
	EvernoteSession es;
	private Client noteStore;
	private String authToken;
	public EvernoteController(EvernoteSession es){
		this.es = es;
	}
	public String getNote(String guid) {
		String result;
		try {
			return (new NoteGetter().execute(guid)).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public class NoteGetter extends AsyncTask<String, Void, String> {
    	String result;
		private Object DIALOG_PROGRESS;
		private String authToken;
    	@SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
          //showDialog(com.example.everhack.EverHackGamesActivity.DIALOG_PROGRESS);
        }
        protected String doInBackground(String... noteGuid) {
        	try {
				return ((Client) noteStore).getNoteContent(authToken, noteGuid[0]);
			} catch (EDAMUserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EDAMSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (EDAMNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	return null;
        }
        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String s) {
        	s = result;
          removeDialog(DIALOG_PROGRESS);
        }
		private void removeDialog(Object dIALOG_PROGRESS2) {
			// TODO Auto-generated method stub
			
		}
    	
    }
	public class NotesReader extends AsyncTask<Void, Void, Void> {
    	@SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
          //showDialog(com.example.everhack.EverHackGamesActivity.DIALOG_PROGRESS);
        }
        protected Void doInBackground(Void... Void) {
        	getNotes();
        	return null;
        }
        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void p) {
          //removeDialog(com.example.everhack.EverHackGamesActivity.DIALOG_PROGRESS);
        }
    	
    }
	private void getNotes(){
		notesList = new ArrayList<Note>();
  		List<Notebook> notebooks = null;
  		authToken = es.getAuthToken();
  		try {
          	noteStore = es.createNoteStore();
  			notebooks  = ((Client) noteStore).listNotebooks(es.getAuthToken());
  			Log.d("Test", "gOT SOME NOTEBOOKS");
  		} catch (TTransportException e1) {
				Log.d("Test", e1.getMessage(), e1);
  		} catch (EDAMUserException e1) {
				Log.d("Test", e1.getMessage(), e1);
  		} catch (EDAMSystemException e1) {
			Log.d("Test", e1.getMessage(), e1);
  		} catch (TException e1) {
			Log.d("Test", e1.getMessage(), e1);
  		}

//          
//          if(notebooks == null)
//          	campodenotas = "NULL";
//          else
//          	campodenotas = "NOT NULL";
  		
  		
  		Log.d("Tag", "Found " + (notebooks == null ? "null" : notebooks.size()) + " notebooks");
  		if(notebooks != null)
          for(Notebook nb : notebooks){
          	NoteFilter filter = new NoteFilter();
          	filter.setNotebookGuid(nb.getGuid());
          	NoteList notelist = null;
          	
      		try {
      			if(noteStore != null){
      				notelist = ((Client) noteStore).findNotes(es.getAuthToken(), filter, 0, 1000);
      			}
  			} catch (EDAMUserException e1) {
  				Log.d("Test", e1.getMessage(), e1);
  				notelist = null;
  			} catch (EDAMSystemException e1) {
  				notelist = null;
  				Log.d("Test", e1.getMessage(), e1);
  			} catch (EDAMNotFoundException e1) {
  				notelist = null;
  				Log.d("Test", e1.getMessage(), e1);
  			} catch (TException e1) {
  				notelist = null;
  				Log.d("Test", e1.getMessage(), e1);
  			}
      		if(notelist != null){
  				List<Note> notes = notelist.getNotes();
  				Log.d("Tag", "Found " + (notebooks == null ? "null" : notes.size()) + " notes");
  	        	for(Note note : notes){
  	        		if(note != null)
  	        			notesList.add(note);       		
  	        	}
      		}
          }
    }
	public void assyncGetNotes() {
		new NotesReader().execute();
	}
}
