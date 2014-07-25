package com.dailyexpense.activity;

import java.security.acl.LastOwnerException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;

import com.dailyexpense.adapters.ExpandListAdapter;
import com.dailyexpense.database.DatabaseAdapterClass;
import com.dailyexpense.model.Expense;
import com.example.dailyexpense.R;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class ExpenseListActivity extends Activity {

	ExpandListAdapter listAdapter;
	 ExpandableListView expListView;
	 List<String[]> listDataHeader;
	 TreeMap<String, List<String[]>> listDataChild;
	 
	 DatabaseAdapterClass dbAdapter;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_list);
		
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		dbAdapter = new DatabaseAdapterClass(this);
		listDataHeader = new ArrayList<String[]>();
		listDataChild = new TreeMap<String, List<String[]>>();
		listDataChild.descendingKeySet();
		prepareListData();

		listAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);

		// setting list adapter
		expListView.setAdapter(listAdapter);
		
		expListView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				return false;
			}
		});
	
	
	expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

		public void onGroupCollapse(int groupPosition) {
			Toast.makeText(getApplicationContext(),
					listDataHeader.get(groupPosition) + " Collapsed",
					Toast.LENGTH_SHORT).show();

		}
	});

	// Listview on child click listener
	expListView.setOnChildClickListener(new OnChildClickListener() {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(),""+listDataHeader.get(groupPosition), Toast.LENGTH_SHORT).show();
			return false;
		}
	});
}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.expense_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void prepareListData() {
		HashMap<String, List<Expense>> hashmap= new HashMap<String, List<Expense>>();
		
		Cursor cursor = dbAdapter.fetchData();
		if (cursor.moveToFirst()) {
            do {
                Expense expense = new Expense(); 
                expense.setDate(cursor.getString(1));
                expense.setAmount(Double.parseDouble(cursor.getString(2)));
                expense.setSpentOn(cursor.getString(3));
                expense.setNote(cursor.getString(4));
                
               List<Expense> expenseList = hashmap.get(expense.getDate()); 
    			if( expenseList == null ) expenseList = new ArrayList<Expense>();
    			expenseList.add(expense);
    			hashmap.put(expense.getDate(), expenseList);
             
            } while (cursor.moveToNext());
        }
		
		
		for (Map.Entry<String, List<Expense>> entry : hashmap.entrySet()) {
            String key = entry.getKey();
            List<Expense> values = entry.getValue();
            List<String[]> childData = new ArrayList<String[]>();
            double totalAmount = 0;
            for (Expense e : values) {
    			totalAmount += e.getAmount();
    			String[] childDataItem = {e.getSpentOn(),""+e.getAmount()};
    			childData.add(childDataItem);
			}
            String[] headerString = {key,""+totalAmount};
            listDataHeader.add(headerString);
            listDataChild.put(key,childData);
        }

	}

	

}
