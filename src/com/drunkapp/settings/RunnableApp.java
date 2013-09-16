package com.drunkapp.settings;

import android.annotation.SuppressLint;
import java.io.Serializable;
import java.util.Locale;

/**
 * Data class for application information.
 * 
 * @author Liam
 *
 */
public class RunnableApp implements Serializable, Comparable<RunnableApp> {

	/**
	 * Object ID for serialisation.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Label to be displayed in the KidSafe launcher.
	 */
	private CharSequence label;
	
	/**
	 * Is the application selected as allowed.
	 */
	private boolean selected;
	
	/**
	 * Package name for the application.
	 */
	private String packageName;
	
	/**
	 * Class name for the application.
	 */
	private String className;
	
	/**
	 * Parameterised constructor for RunnableApp class
	 * 	  
	 * @param c Label used to identify the application
	 * @param b Indicates whether an application is allowed
	 * @param p Package name of the application
	 * @param n Class name of the application
	 */
	public RunnableApp(CharSequence c, boolean b, String p, String n) {
		label = c;
		selected = b;
		packageName = p;
		className = n;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param r Application to have its information copied
	 */
	public RunnableApp(RunnableApp r) {
		label = r.label;
		selected = r.selected;
		packageName = r.packageName;
		className = r.className;
	}
	/**
	 *
	 * @return The label of the application
	 */
	public CharSequence getLabel() {
		return label;
	}
	
	/**
	 * 
	 * @return Whether the application is allowed
	 */
	public boolean isSelected() {
		return selected;
	}
	
	/**
	 * 
	 * @param b Whether the application should be allowed or not
	 * @return This RunnableApp instance so that you can stack up method calls
	 */
	public RunnableApp setSelected(Boolean b) {
		selected = b;
		return this;
	}
	
	/**
	 * 
	 * @return Package name of the application
	 */
	public String getPackageName() {
		return packageName;
	}
	
	/**
	 * 
	 * @return Class name of the application
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * 
	 * @param c The new class name for the application
	 */
	public void setClassName(String c) {
		className = c;
	}
	
	/**
	 * Compares this application against another application based
	 * on their package name and class name
	 * 
	 * @param another Another application to compare to
	 * @return Whether or not the applications are the same
	 */
	public boolean equals(RunnableApp another) {	
		if(another.packageName.equals(this.packageName) && another.className.equals(this.className)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Overriding the standard compareTo to compare based on the
	 * lower case label of the application.
	 */
	@Override
	public int compareTo(RunnableApp another) {
		return label.toString().toLowerCase().compareTo(another.label.toString().toLowerCase());
	}
}
