package com.dailyexpense.activity;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import com.dailyexpense.database.DatabaseAdapterClass;
import com.example.dailyexpense.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class AddExpenseActivity extends Activity implements android.widget.CompoundButton.OnCheckedChangeListener{

	private EditText amountEditText,noteEditText;
	private RadioButton[] radiobutton = new RadioButton[9];
	ImageView datePickerImageView;
	Button cancelButton,saveButton;
	int year,day,month;
	//String month;
	int hour,minute,seconds;
	static final int dialogId = 1;
	TextView dateLabel;
	int[] ids = {R.id.food_radio,R.id.travel_radio,R.id.rent_radio,R.id.health_radio,R.id.gfbf_radio,R.id.leisure_radio,R.id.party_radio,R.id.other_radio,R.id.drink_radio};
	String selectedRadioButton;
	
	DatabaseAdapterClass dbAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //initialize views
        
        dateLabel = (TextView) findViewById(R.id.selected_date_label);
        datePickerImageView = (ImageView) findViewById(R.id.calendar_imageview);
        amountEditText = (EditText)findViewById(R.id.expense_amount_edittext);
        noteEditText = (EditText)findViewById(R.id.note_edittext);
        cancelButton = (Button) findViewById(R.id.cancel_expense_button);
        saveButton = (Button) findViewById(R.id.save_expense_button);
        dbAdapter = new DatabaseAdapterClass(this);
        //set todays date
        getTodaysDate();
        setDefaultValues();
        
        //initialize radio buttons
        for (int j = 0; j < ids.length; j++) {
        	radiobutton[j] = (RadioButton)findViewById(ids[j]);
        	radiobutton[j].setOnCheckedChangeListener(this);
        	if(ids[j] == R.id.other_radio){
        		
        	}
		}
        
    }
    
    private void setDefaultValues() {
		String todaysDate = getMonth(month)+"/"+day+"/"+year;
		dateLabel.setText(todaysDate);
		amountEditText.setHint("0.00");
		selectedRadioButton = "Other";
		noteEditText.setHint("Note");
	}

	public void getTodaysDate(){
    	Calendar today = Calendar.getInstance();
    	year = today.get(Calendar.YEAR);
    	month = today.get(Calendar.MONTH);
    	day = today.get(Calendar.DAY_OF_MONTH);
    	
    	hour = today.get(Calendar.HOUR_OF_DAY);
    	minute = today.get(Calendar.MINUTE);
    	seconds = today.get(Calendar.SECOND);
    }
    
    public void showDatePicker(View view){
    	showDialog(dialogId);
    }
    
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
    	// TODO Auto-generated method stub
    	switch (id) {
		case dialogId:
			return new DatePickerDialog(this, dateSetListener,year, month, day);
		}
    	return null;
    }
    
    protected DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int selectedYear, int monthOfYear,
				int dayOfMonth) {
			year = selectedYear;
			month = monthOfYear;
			day = dayOfMonth;
			
			String selectedDate = getMonth(month)+"/"+day+"/"+year;
			dateLabel.setText(selectedDate);
		}
	};


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			for (int i = 0; i < ids.length; i++) {
				if(buttonView.getId() == ids[i]){
					radiobutton[i].setChecked(true);
					selectedRadioButton = radiobutton[i].getTag().toString();
					//Log.v("selected",""+buttonView.getId());
					//Log.v("selectedq",""+radiobutton[i].getTag());
				}else{
					radiobutton[i].setChecked(false);
				}
			}
		}
		
	}
	
	public void cancelExpense(View view){
		setDefaultValues();
		showMessage("Expense Canceled");
	}
	
	public void saveExpense(View view){
		if(amountEditText.getText().toString().length() > 0 ){
			String date = dateLabel.getText().toString();
			double amount = Double.parseDouble(amountEditText.getText().toString());
			String note = noteEditText.getText().toString();
			String spenton = selectedRadioButton;
			if(noteEditText.getText().toString().length() <= 0){
				note = "none"; 
			}
			//add to data base
			
			Log.v("Amount", ""+amount);
			Log.v("Date", ""+date);
			Log.v("Spent On", ""+spenton);
			Log.v("Note", ""+note);
			long id = dbAdapter.insertData(date, amount, spenton, note);
			Intent intent  = new Intent(this, ExpenseListActivity.class);
			this.startActivity(intent);
			Log.v("Insertintodatabase", ""+id);
		}else{
			showMessage("Please enter Amount");
		}
	}
    
	public void showMessage(String string){
		Toast.makeText(this,string,Toast.LENGTH_SHORT).show();
		setDefaultValues();
	}
	
	public String getMonth(int month) {
	    return new DateFormatSymbols().getMonths()[month-1];
	}
}
