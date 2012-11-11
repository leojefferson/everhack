package com.example.everhack;

import org.apache.thrift.TException;

import android.os.AsyncTask;

import com.evernote.client.oauth.android.EvernoteSession;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteStore.Client;

public class NoteGetter extends AsyncTask<Void, Void, String> {
	private String guid, result;
	private final int DIALOG_PROGRESS = 101;
	private Client noteStore;
	private EvernoteSession es;
	
	public NoteGetter(String guid, Client createdNote, EvernoteSession es) {
		this.guid = guid;
		noteStore = createdNote;
		this.es = es;
	}
	@SuppressWarnings("deprecation")
    @Override
    protected void onPreExecute() {
      //showDialog(DIALOG_PROGRESS);
    }
    protected String doInBackground(Void... Void) {
    	result = null;
    	try {
			result = noteStore.getNoteContent(es.getAuthToken(), guid);
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
    	return result;
    }
    @SuppressWarnings("deprecation")
    @Override
    protected void onPostExecute(String p) {
    	//removeDialog(DIALOG_PROGRESS);
    }
	
}
