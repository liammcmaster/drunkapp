package com.drunkapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.drunkapp.resources.ActivityHandlerThread;
import com.drunkapp.resources.ScreenChangeHandler;


/**
 * Base activity for the application.
 * 
 * @author Liam
 *
 */
public class BaseActivity extends Activity {

	/**
	 * Thread that checks running activities against the list of allowed
	 * applications.
	 */
	private ActivityHandlerThread aht;

	/**
	 * Resource for managing changes to the screen (on or off).
	 */
	private ScreenChangeHandler sch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_base);
	}

	public void onSettingsClick(View v) {
		startActivity(new Intent(getApplicationContext(),
				com.drunkapp.settings.Settings.class));
	}

	public void onStartClick(View v) {
		aht = new ActivityHandlerThread(this);
		aht.start();

		// Starts a receiver that waits for changes to the screen (on or off)
		sch = new ScreenChangeHandler(aht, this);
		sch.startScreenChangeListener();
		
		// Inform user that it the thread has now started.
		Toast.makeText(this, "Your apps are safe, go get hammered!", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		aht.cancel();
		sch.stopScreenChangeListener();
	}
}
