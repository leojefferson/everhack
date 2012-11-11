package com.example.everhack;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.everhack.EverHackGamesActivity;
import com.example.everhack.R;
import com.example.everhack.EverHackGamesActivity.EvernoteNoteCreator;
import com.example.everhack.EverHackGamesActivity.ImageData;
import com.example.everhack.EverHackGamesActivity.ImageSelector;
import com.evernote.client.conn.mobile.FileData;
import com.evernote.client.oauth.android.EvernoteSession;
import com.evernote.client.oauth.android.EvernoteUtil;
import com.evernote.edam.error.EDAMNotFoundException;
import com.evernote.edam.error.EDAMSystemException;
import com.evernote.edam.error.EDAMUserException;
import com.evernote.edam.notestore.NoteFilter;
import com.evernote.edam.notestore.NoteList;
import com.evernote.edam.notestore.NoteStore.Client;
import com.evernote.edam.type.Note;
import com.evernote.edam.type.Notebook;
import com.evernote.edam.type.Resource;
import com.evernote.edam.type.ResourceAttributes;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.thrift.TException;
import org.apache.thrift.transport.TTransportException;

public class EverHackGamesActivity extends Activity {
    
    /***************************************************************************
     * You MUST change the following values to run this sample application.    *
     ***************************************************************************/
    
    // Your Evernote API key. See http://dev.evernote.com/documentation/cloud/
    // Please obfuscate your code to help keep these values secret.
    private static final String CONSUMER_KEY = "vsmontalvao";
    private static final String CONSUMER_SECRET = "c7f5dd7483ae5365";

    /***************************************************************************
     * Change these values as needed to use this code in your own application. *
     ***************************************************************************/

    // Name of this application, for logging
    private static final String TAG = "HelloEDAM";

    // Initial development is done on Evernote's testing service, the sandbox.
    // Change to HOST_PRODUCTION to use the Evernote production service 
    // once your code is complete, or HOST_CHINA to use the Yinxiang Biji
    // (Evernote China) production service.
    private static final String EVERNOTE_HOST = EvernoteSession.HOST_SANDBOX;

    /***************************************************************************
     * The following values are simply part of the demo application.           *
     ***************************************************************************/
    
    // Activity result request codes
    private static final int SELECT_IMAGE = 1;

    // Used to interact with the Evernote web service
    private EvernoteSession mEvernoteSession;
    
    public static EvernoteSession es;
    public static List<Note> notesList;
	private static String res;
    
    // UI elements that we update
    private Button mBtnAuth;
    private Button mBtnSave;
    private Button mBtnSelect;
    private Button BtnGame;
    private ImageView mImageView;
    private final int DIALOG_PROGRESS = 101;
    
    // The path to and MIME type of the currently selected image from the gallery
    public class ImageData {
      public Bitmap imageBitmap;
      public String filePath;
      public String mimeType;
      public String fileName;
    }

    private ImageData mImageData;
	private Button BtnNotes;
	private static Client noteStore;
	private static Note createdNote;

    /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	notesList = new ArrayList<Note>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mBtnAuth = (Button) findViewById(R.id.auth_button);
        mBtnSelect = (Button) findViewById(R.id.select_button);
        mBtnSave = (Button) findViewById(R.id.save_button);
        mImageView = (ImageView) findViewById(R.id.image);
        BtnGame = (Button) findViewById(R.id.game_button);
        BtnNotes = (Button) findViewById(R.id.notes_button);

        if (getLastNonConfigurationInstance() != null) {
          mImageData = (ImageData) getLastNonConfigurationInstance();
          mImageView.setImageBitmap(mImageData.imageBitmap);
        }

        setupSession();
        
        BtnGame.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(), GamesMenuActivity.class);
				startActivity(i);
			}
		});
        
        BtnNotes.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				new NotesReader().execute();
			}
		});
    }

    @Override
    public void onResume() {
      super.onResume();
      updateUi();
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
      return mImageData;
    }

    // using createDialog, could use Fragments instead 
    @SuppressWarnings("deprecation")
    @Override
    protected Dialog onCreateDialog(int id) {
      switch(id) {
        case DIALOG_PROGRESS:
          return new ProgressDialog(EverHackGamesActivity.this);
      }
      return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
      switch(id) {
        case DIALOG_PROGRESS:
          ((ProgressDialog)dialog).setIndeterminate(true);
          dialog.setCancelable(false);
          ((ProgressDialog) dialog).setMessage(getString(R.string.loading));
      }
    }

    /**
     * Setup the EvernoteSession used to access the Evernote API.
     */
    private void setupSession() {

      // Retrieve persisted authentication information
      mEvernoteSession = EvernoteSession.init(this, CONSUMER_KEY, CONSUMER_SECRET, EVERNOTE_HOST, null);
    }
    
    /**
     * Update the UI based on Evernote authentication state.
     */
    private void updateUi() {
      if (mEvernoteSession.isLoggedIn()) {
        mBtnAuth.setText(R.string.label_log_out);
        if(mImageData != null && !TextUtils.isEmpty(mImageData.filePath)) {
          mBtnSave.setEnabled(true);
          es = mEvernoteSession;
        } else {
          mBtnSave.setEnabled(false);
        }
        mBtnSelect.setEnabled(true);
      } else {
        mBtnAuth.setText(R.string.label_log_in);
        mBtnSave.setEnabled(false);
        mBtnSelect.setEnabled(false);
      }
    }
    
    /**
     * Called when the user taps the "Log in to Evernote" button.
     * Initiates the Evernote OAuth process, or logs out if the user is already
     * logged in.
     */
    public void startAuth(View view) {
      if (mEvernoteSession.isLoggedIn()) {
        mEvernoteSession.logOut(getApplicationContext());
      } else {
        mEvernoteSession.authenticate(this);
      }
      updateUi();
    }

    /***************************************************************************
     * The remaining code in this class simply demonstrates the use of the     *
     * Evernote API once authnetication is complete. You don't need any of it  *
     * in your application.                                                    *
     ***************************************************************************/

    /**
     * Called when the control returns from an activity that we launched.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      switch(requestCode) {
        //Update UI when oauth activity returns result
        case EvernoteSession.REQUEST_CODE_OAUTH:
          if(resultCode == Activity.RESULT_OK) {
            updateUi();
          }
          break;
        //Grab image data when picker returns result
        case SELECT_IMAGE:
          if (resultCode == Activity.RESULT_OK) {
            new ImageSelector().execute(data);
          }
          break;
      }
    }

    /**
     * Called when the user taps the "Select Image" button.
     *
     * Sends the user to the image gallery to choose an image to share.
     */
    public void startSelectImage(View view) {
      Intent intent = new Intent(Intent.ACTION_PICK,
          MediaStore.Images.Media.INTERNAL_CONTENT_URI);
      startActivityForResult(intent, SELECT_IMAGE);
    }

    /**
     * Called when the user taps the "Save Image" button.
     * 
     * You probably don't want to do this on your UI thread in the 
     * real world.
     * 
     * Saves the currently selected image to the user's Evernote account using
     * the Evernote web service API.
     * 
     * Does nothing if the Evernote API wasn't successfully initialized
     * when the activity started.
     */
    public void saveImage(View view) {
      if (mEvernoteSession.isLoggedIn() && mImageData != null && mImageData.filePath != null) {
        new EvernoteNoteCreator().execute(mImageData);
      }
    }
    public class NotesReader extends AsyncTask<Void, Void, Void> {
    	@SuppressWarnings("deprecation")
        @Override
        protected void onPreExecute() {
          showDialog(DIALOG_PROGRESS);
        }
        protected Void doInBackground(Void... Void) {
        	getNotes();
        	return null;
        }
        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void p) {
          removeDialog(DIALOG_PROGRESS);
        }
    	
    }
    

    public class EvernoteNoteCreator extends AsyncTask<ImageData, Void, Note> {     

		// using showDialog, could use Fragments instead 
    	@SuppressWarnings("deprecation")
      @Override
      protected void onPreExecute() {
        showDialog(DIALOG_PROGRESS);
      }

      @Override
      protected Note doInBackground(ImageData... imageDatas) {
        if(imageDatas == null || imageDatas.length == 0) {
          return null;
        }
        ImageData imageData = imageDatas[0];


        String f = imageData.filePath;
        try {
          // Hash the data in the image file. The hash is used to reference the
          // file in the ENML note content.
          InputStream in = new BufferedInputStream(new FileInputStream(f));
          FileData data = new FileData(EvernoteUtil.hash(in), new File(f));
          in.close();

          // Create a new Resource
          Resource resource = new Resource();
          resource.setData(data);
          resource.setMime(imageData.mimeType);
          ResourceAttributes attributes = new ResourceAttributes();
          attributes.setFileName(imageData.fileName);
          resource.setAttributes(attributes);

          // Create a new Note
          Note note = new Note();
          note.setTitle("Android test note");
          note.addToResources(resource);

          // Set the note's ENML content. Learn about ENML at
          // http://dev.evernote.com/documentation/cloud/chapters/ENML.php
          String content =
              EvernoteUtil.NOTE_PREFIX +
              "<p>This note was uploaded from Android. It contains an image.</p>" +
              EvernoteUtil.createEnMediaTag(resource) +
              EvernoteUtil.NOTE_SUFFIX;

          note.setContent(content);

          // Create the note on the server. The returned Note object
          // will contain server-generated attributes such as the note's
          // unique ID (GUID), the Resource's GUID, and the creation and update time.
          noteStore = mEvernoteSession.createNoteStore();
          createdNote = noteStore.createNote(mEvernoteSession.getAuthToken(), note);
        } catch(Exception e) {
          Log.e(TAG, getString(R.string.err_creating_note), e);
        }

        return createdNote;
      }

      // using removeDialog, could use Fragments instead 
      @SuppressWarnings("deprecation")
      @Override
      protected void onPostExecute(Note note) {
        removeDialog(DIALOG_PROGRESS);

        if (note == null) {
          Toast.makeText(getApplicationContext(), R.string.err_creating_note, Toast.LENGTH_LONG).show();
          return;
        }

        Toast.makeText(getApplicationContext(), R.string.msg_image_saved, Toast.LENGTH_LONG).show();
      }
    }

    /**
     * Called when control returns from the image gallery picker.
     * Loads the image that the user selected.

     */
    public class ImageSelector extends AsyncTask<Intent, Void, ImageData> {

      // using showDialog, could use Fragments instead 
      @SuppressWarnings("deprecation")
      @Override
      protected void onPreExecute() {
        showDialog(DIALOG_PROGRESS);
      }

      /**
       * The callback from the gallery contains a pointer into a table.
       * Look up the appropriate record and pull out the information that we need,
       * in this case, the path to the file on disk, the file name and the MIME type.
       * @param intents
       * @return
       */
      // using Display.getWidth and getHeight on older SDKs 
      @SuppressWarnings("deprecation")
      @Override
      // suppress lint check on Display.getSize(Point)
      @TargetApi(16) 
      protected ImageData doInBackground(Intent... intents) {
    	  
        if(intents == null || intents.length == 0) {
          return null;
        }

        Uri selectedImage = intents[0].getData();
        String[] queryColumns = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DISPLAY_NAME };

        Cursor cursor = null;
        ImageData image = null;
        try {
          cursor = getContentResolver().query(selectedImage, queryColumns, null, null, null);
          if(cursor.moveToFirst()) {
            image = new ImageData();
            long imageId= cursor.getLong(cursor.getColumnIndex(queryColumns[0]));

            image.filePath = cursor.getString(cursor.getColumnIndex(queryColumns[1]));
            image.mimeType = cursor.getString(cursor.getColumnIndex(queryColumns[2]));
            image.fileName = cursor.getString(cursor.getColumnIndex(queryColumns[3]));

            Uri imageUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId);
            Bitmap tempBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);

            int dimen = 0;
            int x = 0;
            int y = 0;

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
              Point size = new Point();
              getWindowManager().getDefaultDisplay().getSize(size);

              x = size.x;
              y = size.y;
            } else {
              x = getWindowManager().getDefaultDisplay().getWidth();
              y = getWindowManager().getDefaultDisplay().getHeight();
            }

            dimen = x < y ? x : y;

            image.imageBitmap = Bitmap.createScaledBitmap(tempBitmap, dimen, dimen, true);
            tempBitmap.recycle();

          }
        }catch (Exception e) {
          Log.e(TAG, "Error retrieving image");
        }finally {
          if (cursor != null) {
            cursor.close();
          }
        }
        return image;
      }

      /**
       * Sets the image to the background and enables saving it to evernote
       * @param image
       */
      // using removeDialog, could use Fragments instead 
      @SuppressWarnings("deprecation") 
      @Override
      protected void onPostExecute(ImageData image) {
        removeDialog(DIALOG_PROGRESS);

        if(image == null) {
          Toast.makeText(getApplicationContext(), R.string.err_image_selected, Toast.LENGTH_SHORT).show();
          return;
        }

        if(image.imageBitmap != null) {
          mImageView.setImageBitmap(image.imageBitmap);
        }

        if (mEvernoteSession.isLoggedIn()) {
          mBtnSave.setEnabled(true);
        }

        mImageData = image;
      }
    }
    private void getNotes(){
  		EvernoteSession es = mEvernoteSession;
  		List<Notebook> notebooks = null;
  		
  		try {
          	noteStore = es.createNoteStore();
  			notebooks  = noteStore.listNotebooks(es.getAuthToken());
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
      				notelist = noteStore.findNotes(es.getAuthToken(), filter, 0, 1000);
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
						try {
							note = noteStore.getNote(es.getAuthToken(), note.getGuid(), true, false, false, false);
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
  	        			notesList.add(note);       		
  	        	}
  	        	
      		}
          }
    }
    /*
	public static String getNote(String guid) {
		try {
			return (new NoteGetter(guid, noteStore, es).execute()).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	*/
}
