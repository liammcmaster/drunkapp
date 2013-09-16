package com.drunkapp.settings;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.drunkapp.R;

/**
 * Extends the AppAdapter class for use with a listView.
 * 
 * @author Liam
 *
 */
public class AppListAdapter extends ArrayAdapter<RunnableApp> {

	/**
	 * Array list of runnable applications
	 */
	private ArrayList<RunnableApp> apps;
	
	/**
	 * Activity that is using this adapter
	 */
	private Activity activity;
	
	/**
	 * Calls super constructor
	 * 
	 * @param a Parent activity that is using this resource
	 * @param listItemViewResourceId Resource id of the grid item view
	 * @param items ArrayList of items to be displayed
	 */
	public AppListAdapter(Activity a, int listItemViewResourceId, ArrayList<RunnableApp> items) {
		super(a, listItemViewResourceId, items);
		
		apps = new ArrayList<RunnableApp>();
		apps = items;
		activity = a;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			row = inflater.inflate(R.layout.special_list, parent, false);
		}

		// Set the text 
		TextView listItem = (TextView) row.findViewById(R.id.list_text);
		listItem.setText(apps.get(position).getLabel());

		// Set the checkbox (checked or unchecked)
		final CheckBox cbx = (CheckBox) row.findViewById(R.id.list_checkbox);
		cbx.setChecked(apps.get(position).isSelected());
		
		// Set on click functionality - check the CheckBox
		row.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cbx.setChecked(!cbx.isChecked());
				apps.set(position, apps.get(position).setSelected(cbx.isChecked()));
			}
		});
		
		return row;
	}

	/**
	 * Used to obtain an array list of the applications that have been selected
	 * as allowed by the parent.
	 * 
	 * @return ArrayList of allowed applications
	 */
	public ArrayList<RunnableApp> getAllApps() {
		return apps;
	}
	
	@Override
	public int getCount() {
		return apps.size();
	}
	
	@Override
	public RunnableApp getItem(int position) {
		return apps.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
}
