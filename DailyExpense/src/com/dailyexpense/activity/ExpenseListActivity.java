package com.dailyexpense.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;

import com.dailyexpense.adapters.ExpandListAdapter;
import com.dailyexpense.database.DatabaseAdapterClass;
import com.dailyexpense.model.Expense;
import com.example.dailyexpense.R;

public class ExpenseListActivity extends Activity {

	ExpandListAdapter listAdapter;
	ExpandableListView expListView;
	List<String[]> listDataHeader;
	TreeMap<String, List<String[]>> listDataChild;
	DatabaseAdapterClass dbAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expense_list);
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
			String[] childText = (String[]) getChild(groupPosition, childPosition);
			
			Log.v("Data 0", ""+childText[0]);
			Log.v("Data 1", ""+Integer.parseInt(childText[3]));
			
			Intent intent = new Intent(ExpenseListActivity.this, AddExpenseActivity.class);
			intent.putExtra("keyFor", "UpdateExpense");
			intent.putExtra("keySpentOn", childText[0]);
			intent.putExtra("keyAmount", childText[1]);
			intent.putExtra("keyNote", childText[2]);
			intent.putExtra("keyId", childText[3]);
			intent.putExtra("keyDate", childText[4]);
        	ExpenseListActivity.this.startActivity(intent);
			//update or delete where id is Integer.parseInt(childText[3])
			//Toast.makeText(getApplicationContext(),""+listDataHeader.get(groupPosition)[1]+" "+listDataChild.get(childPosition), Toast.LENGTH_SHORT).show();
			return false;
		}
	});
}
	public Object getChild(int groupPosition, int childPosititon) {
		// TODO Auto-generated method stub
		return this.listDataChild.get(this.listDataHeader.get(groupPosition)[0]).get(childPosititon);
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
		}else if(id == android.R.id.home){
			NavUtils.navigateUpFromSameTask(this);
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
                //Log.v("cursorposition :",""+cursor.getInt(0));
                expense.setId(cursor.getInt(0));
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
    			String[] childDataItem = {e.getSpentOn(),""+e.getAmount(),e.getNote(),""+e.getId(),e.getDate()};
    			childData.add(childDataItem);
			}
            String[] headerString = {key,""+totalAmount};
            listDataHeader.add(headerString);
            listDataChild.put(key,childData);
        }
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//prepareListData();

		//listAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);

		// setting list adapter
		//expListView.setAdapter(listAdapter);
	}
}
