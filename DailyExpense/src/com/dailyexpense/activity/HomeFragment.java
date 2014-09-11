package com.dailyexpense.activity;


import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.dailyexpense.database.DatabaseAdapterClass;
import com.dailyexpense.model.Expense;
import com.example.dailyexpense.R;
import com.google.common.collect.Sets.SetView;

import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
 
public class HomeFragment extends Fragment {
	
	private TextView monthLabel;
	private TextView monthAmount;
	private TextView noExpenseAdded;
	private TextView addExpenseText;
	
	private int year,day,month;
	private  DatabaseAdapterClass dbAdapter;
	private  String currentMonth;
    public HomeFragment(){}
     
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        
        monthLabel = (TextView) rootView.findViewById(R.id.month_textView);
        monthAmount = (TextView) rootView.findViewById(R.id.montlyAamount_textView);
                noExpenseAdded = (TextView) rootView.findViewById(R.id.noExpenseAdded_TextView);
        addExpenseText = (TextView) rootView.findViewById(R.id.tapToAdd_TextView);
        currentMonth = getMonth(getTodaysDate());
        dbAdapter = new DatabaseAdapterClass(getActivity());
        prepareData(currentMonth);
        
        return rootView;
    }
    
    private void prepareData(String month) {
    	Cursor cursor = dbAdapter.fetchDataWhere(month);
    	double totalAmount = 0;
    	if(cursor.getCount() > 0){
    		noExpenseAdded.setText("");
    		addExpenseText.setText("");
		if (cursor.moveToFirst()) {
            do {
            	totalAmount += Double.parseDouble(cursor.getString(2)); 
            } while (cursor.moveToNext());
        }
		monthLabel.setText(month);
		monthAmount.setText(""+totalAmount+" \u20B9");
    	}else{
    		monthLabel.setText("");
    		monthAmount.setText("");
    		Log.v("HomeFragment","No tables");
    	}
	}

	public int getTodaysDate(){
    	Calendar today = Calendar.getInstance();
    	//year = today.get(Calendar.YEAR);
    	month = today.get(Calendar.MONTH);
    	//day = today.get(Calendar.DAY_OF_MONTH);	
    	return month;
    }
    
    public String getMonth(int month) {
	    return new DateFormatSymbols().getMonths()[month];
	}
    
    @Override
    public void onResume() {
    	// TODO Auto-generated method stub
    	prepareData(currentMonth);
    	super.onResume();
    	
    }
}