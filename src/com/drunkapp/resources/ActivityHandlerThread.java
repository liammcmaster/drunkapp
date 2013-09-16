package com.drunkapp.resources;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.drunkapp.settings.RunnableApp;
import com.drunkapp.settings.RunnableAppHandler;

/**
 * Thread that monitors the applications that are running
 * on the device.  If the user starts an application that
 * is not permitted, it will be shut down.
 * 
 * @author Liam
 *
 */
public class ActivityHandlerThread extends Thread {
	
	/**
	 * The application context.
	 */
	private Activity activity;
	
	/**
	 * ActivityManager that is used to get the running tasks.
	 */
	private ActivityManager am;
	
	/**
	 * List of running tasks on the system.
	 */
	private List<RunningTaskInfo> tasks; 
	
	/**
	 * RunnableAppHandler for fetching applications from shared file.
	 */
	private RunnableAppHandler rah;
	
	/**
	 * Whether or not any instances of this thread can run.
	 */
	private static boolean cancelled;
	
	/**
	 * Parameterised constructor
	 * 
	 * @param a The Activity that is using this class.
	 */
	public ActivityHandlerThread(Activity a) {
		activity = a;
		am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
		rah = new RunnableAppHandler(activity);
		cancelled = false;
	}
	
	/**
	 * Main processing method that checks the current activity
	 * against allowed applications on the system.
	 */
	public void run() {
		
		/**
		 * Thread will continue to run while the DrunkApp application
		 * is still running on the system.
		 */
		while(!isCancelled()) {
			/**
			 *  Force the thread to sleep for 100 milliseconds to lighten 
			 *  the load on the processor and battery.
			 */
			try {
				sleep(100);
			} catch (InterruptedException e) {}

			tasks = null;

			try {
				tasks = am.getRunningTasks(1);
			} catch (SecurityException e) {

			}

			if (tasks != null && !tasks.isEmpty()) {
				ComponentName topActivity = tasks.get(0).topActivity;

				boolean isAllowed = true;
				
				// Check to see if current activity is allowed.
				for(RunnableApp a : rah.getBlockedApps()) {
					if(topActivity.getPackageName().equals(a.getPackageName())) {
						isAllowed = false;
					}
				}

				// Go to the home screen
				if(!isAllowed) {
					activity.getApplicationContext().startActivity(new Intent()
						.setAction(Intent.ACTION_MAIN)
						.addCategory(Intent.CATEGORY_HOME)
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
					
					// Display a message explaining why the application was blocked
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(activity, "You're too drunk to use that app mate!", Toast.LENGTH_LONG).show();
						}
					});
					
					// Allow time for changes to take place
					try {
						sleep(1000);
					} catch (InterruptedException e) {}
				}
			}
		}
	}
	
	/**
	 * 
	 * @return Whether or not the DrunkApp application is running.
	 */
	private boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * Cancels the Thread
	 */
	public void cancel() {
		cancelled = true;
	}
	
	/**
	 * This will return the fully qualified class name of the 
	 * activity that is at the top of the current task.
	 *  
	 * @return The class name of the current activity.
	 */
	public String getCurrentTopActivity() {
		try {
			tasks = am.getRunningTasks(1);
		} catch (SecurityException e) {

		}

		if (tasks != null && !tasks.isEmpty()) {
			return tasks.get(0).topActivity.getClassName();
		}
		else {
			return "";
		}
	}
}
