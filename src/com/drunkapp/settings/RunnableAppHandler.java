package com.drunkapp.settings;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

/**
 * Class that handles the applications that have been selected as allowable by
 * the parent. It will access the applications from a shared file, which enables
 * use by any of the classes within this application.
 * 
 * @author Liam
 * 
 */
public class RunnableAppHandler {

	/**
	 * Name of the shared file.
	 */
	private static final String FILE_NAME = "kidsafe_runnable_apps";
	
	/**
	 * The application context.
	 */
	private Context context;


	/**
	 * Constructor that takes an application context and creates the file used
	 * to store application information.
	 * 
	 * @param c Application context
	 */
	public RunnableAppHandler(Context c) {
		context = c;		
		
		if (!fileExists()) {
			writeAppsToFile(getSystemApps());
		}
	}

	/**
	 * Returns all applications that are runnable on the system i.e. they have
	 * an icon in the application drawer. This will pull all of the application
	 * information from the shared file.
	 * 
	 * @return ArrayList of runnable applications
	 */
	public ArrayList<RunnableApp> getAllApps() {
		ArrayList<RunnableApp> apps = new ArrayList<RunnableApp>();
		apps.addAll(getAppsFromFile());
		apps.addAll(getNewApps());
		
		return apps;
	}

	/**
	 * Returns all applications that have been selected by the user to be blocked.
	 * This will pull the application information only for allowed applications 
	 * from the shared file.
	 * 
	 * @return ArrayList of blocked applications
	 */
	public ArrayList<RunnableApp> getBlockedApps() {
		ArrayList<RunnableApp> allApps = new ArrayList<RunnableApp>();
		ArrayList<RunnableApp> blockedApps = new ArrayList<RunnableApp>();

		allApps = getAppsFromFile();
		
		for(RunnableApp a : allApps) {
			if(a.isSelected()) {
				blockedApps.add(a);
			}
		}
		return blockedApps;
	}

	/**
	 * Writes all of the given applications to the shared file.
	 * 
	 * @param apps ArrayList of allowed applications to be stored
	 */
	public void writeAppsToFile(ArrayList<RunnableApp> apps) {
		try {
			
			FileOutputStream fos = context.openFileOutput(FILE_NAME,
					Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			for (RunnableApp app : apps) {
				oos.writeObject(app);
			}

			oos.close();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks whether or not the shared file exists in memory.
	 * 
	 * @return Whether or not the file exists
	 */
	private boolean fileExists() {
		try {
			FileInputStream in = context.openFileInput(FILE_NAME);
			in.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Gets all of the application information that is stored on 
	 * the shared file.
	 * 
	 * @return ArrayList of the applications on the file
	 */
	private ArrayList<RunnableApp> getAppsFromFile() {
		ArrayList<RunnableApp> apps = new ArrayList<RunnableApp>();

		if (fileExists()) {
			try {
				FileInputStream fis = context.openFileInput(FILE_NAME);
				ObjectInputStream ois = new ObjectInputStream(fis);

				RunnableApp a = new RunnableApp((RunnableApp) ois.readObject());
				while (a != null) {
					apps.add(a);
					a = new RunnableApp((RunnableApp) ois.readObject());
				}
				ois.close();
				fis.close();

			} catch (EOFException e) {		
				// Return the applications, this exception will be thrown
				return apps;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return apps;
	}
	
	/**
	 * Get all of the applications that can be launched from the 
	 * Android system i.e. those which sit in the application drawer.
	 * 
	 * @return ArrayList of system applications
	 */
	private ArrayList<RunnableApp> getSystemApps() {
		ArrayList<RunnableApp> apps = new ArrayList<RunnableApp>();		
		TreeSet<RunnableApp> sorted = new TreeSet<RunnableApp>();

		final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null)
				.addCategory(Intent.CATEGORY_LAUNCHER);
		final List<ResolveInfo> appInfo = context.getPackageManager()
				.queryIntentActivities(mainIntent, 0);

		for(ResolveInfo a : appInfo) {
			sorted.add(new RunnableApp(a.loadLabel(context
					.getPackageManager()), false,
					a.activityInfo.packageName,
					a.activityInfo.name));
		}
		
		apps.addAll(sorted);
		
		return apps;
	}

	/**
	 * Gets any new applications that have been installed
	 * 
	 * @return ArrayList of new applications
	 */
	private ArrayList<RunnableApp> getNewApps() {
		ArrayList<RunnableApp> systemApps = new ArrayList<RunnableApp>();
		ArrayList<RunnableApp> apps = new ArrayList<RunnableApp>();
		ArrayList<RunnableApp> newApps = new ArrayList<RunnableApp>();
		
		systemApps = getSystemApps();
		
		apps.addAll(getAppsFromFile());
		for(RunnableApp a : systemApps) {
			boolean isNew = true;
			
			for(RunnableApp b : apps) {
				if(a.equals(b))
					isNew = false;
			}
			if(isNew) {
				newApps.add(a);
			}
		}
		
		return newApps;		
	}
}