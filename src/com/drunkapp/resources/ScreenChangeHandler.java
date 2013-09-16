package com.drunkapp.resources;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;


/**
 * Registers a broadcast receiver that will listen for changes in the screen
 * (i.e. when the screen is on or off) and will start up and stop
 * ActivityHandlerThreads as necessary.
 * 
 * The reason behind having this class is to reduce the amount of processing
 * required by the phone while it is inactive.
 * 
 * @author Liam
 * 
 */
public class ScreenChangeHandler {

	/**
	 * ActivityHandlerThread to start and stop.
	 */
	private ActivityHandlerThread aht;

	/**
	 * Context of the application.
	 */
	private Activity activity;

	/**
	 * Broadcast receiver that will be registered and unregistered.
	 */
	private BroadcastReceiver activityReceiver;

	/**
	 * Parameterised constructor.
	 * 
	 * @param a	A new or existing thread to start and stop.
	 * @param act The Activity that is using this class.
	 */
	public ScreenChangeHandler(ActivityHandlerThread a, Activity act) {
		aht = a;
		activity = act;
	}

	/**
	 * Starts up the broadcast receiver that will listen for changes to the
	 * screen.
	 * 
	 * When the screen is turned off, any ActivityHandlerThreads will be
	 * stopped.
	 * 
	 * When the screen is turned on, a new thread will begin.
	 */
	public void startScreenChangeListener() {
		activityReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

					// Starting up the thread to monitor tasks on the phone.
					aht = new ActivityHandlerThread(activity);
					aht.start();
				} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
					// Stopping the thread that monitors tasks
					aht.cancel();
				}
			}
		};

		IntentFilter intentFilter = new IntentFilter("");
		intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
		intentFilter.addAction(Intent.ACTION_SCREEN_ON);
		activity.registerReceiver(activityReceiver, intentFilter);
	}

	/**
	 * Unregisters the broadcast receiver. Should only be done when the
	 * application is destroyed.
	 */
	public void stopScreenChangeListener() {
		activity.unregisterReceiver(activityReceiver);
	}
}
