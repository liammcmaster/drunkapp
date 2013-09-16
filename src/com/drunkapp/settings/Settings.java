package com.drunkapp.settings;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.drunkapp.R;

/**
 * Activity that displays and manages the selection of applications that are
 * allowed to run within the KidSafe launcher. It displays a list of installed
 * applications with check boxes.
 * 
 * @author Liam
 * 
 */
public class Settings extends Activity {

	/**
	 * Applications to be displayed.
	 */
	private ArrayList<RunnableApp> apps;

	/**
	 * RunnableAppHandler for fetching applications from shared file.
	 */
	private RunnableAppHandler appHandler;

	/**
	 * Adapter that acts as a link between the ListView and the ArrayList of
	 * applications.
	 */
	private AppListAdapter appAdapter;

	/**
	 * ListView of applications that is used dynamically.
	 */
	private ListView appsListView;

	/**
	 * Sets the list layout for the KidSafe launcher based on the applications
	 * stored in a shared file.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_settings);

		new PopulateListTask().execute(this);
	}

	/**
	 * Once this activity loses focus, save the applications.
	 */
	@Override
	public void onPause() {
		super.onPause();
		appHandler.writeAppsToFile(appAdapter.getAllApps());
	}

	/**
	 * Prevents the settings menu from being displayed.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	/**
	 * Asynchronous task that populates the application settings list in the
	 * background to avoid lag in the system.
	 * 
	 * @author Liam
	 * 
	 */
	private class PopulateListTask extends
			AsyncTask<Activity, RunnableApp, Integer> {

		/**
		 * Instantiates all the UI components
		 */
		@Override
		protected void onPreExecute() {
			apps = new ArrayList<RunnableApp>();
			appHandler = new RunnableAppHandler(getApplicationContext());
			appsListView = (ListView) findViewById(R.id.list);
		}

		/**
		 * Loops through the applications that are installed on the system and
		 * adds them one by one to the ListView by using the publishProgress()
		 * method.
		 */
		@Override
		protected Integer doInBackground(Activity... params) {
			appAdapter = new AppListAdapter(params[0], R.layout.special_list,
					apps);

			for (RunnableApp a : appHandler.getAllApps()) {
				publishProgress(a);
			}

			return null;
		}

		/**
		 * Updates the ListView with the newly fetched applications.
		 */
		@Override
		protected void onProgressUpdate(RunnableApp... newApps) {

			for (RunnableApp a : newApps) {
				appAdapter.add(a);
				appsListView.setAdapter(appAdapter);
			}
		}
	}
}