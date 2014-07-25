package com.dailyexpense.adapters;



import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import com.example.dailyexpense.R;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandListAdapter extends BaseExpandableListAdapter {

	Context context;
	List<String[]> listHeader;
	
	TreeMap<String, List<String[]>> listChildData;
	
	public ExpandListAdapter(Context context, List<String[]> listHeader,TreeMap<String, List<String[]>> listChildData) {
		this.context = context;
		this.listHeader = listHeader;
		this.listChildData = listChildData;
	}
	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		// TODO Auto-generated method stub
		return this.listChildData.get(this.listHeader.get(groupPosition)[0]).get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		final String[] childText = (String[]) getChild(groupPosition, childPosition);
		 
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item,null);
        }
        
        TextView itemName = (TextView) convertView.findViewById(R.id.expense_item_text);
        TextView itemAmount = (TextView) convertView.findViewById(R.id.expense_item_amount_text);
 
        itemName.setText(childText[0].toString());
        itemAmount.setText(childText[1].toString());
      
        return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		Log.v("Positoin", ""+this.listHeader.get(groupPosition));
		Log.v("Size", ""+(this.listChildData.get(this.listHeader.get(groupPosition)[0]).size()));
		return this.listChildData.get(this.listHeader.get(groupPosition)[0]).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return this.listHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return this.listHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 String[] expense = (String[]) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }
 
        TextView expenseDate = (TextView) convertView.findViewById(R.id.expense_date_text);
        TextView expenseTotalAmount = (TextView) convertView.findViewById(R.id.expense_amount_text);
        expenseDate.setTypeface(null, Typeface.BOLD);
        expenseDate.setText(expense[0]);
        expenseTotalAmount.setTypeface(null, Typeface.BOLD);
        expenseTotalAmount.setText(expense[1]);
 
 
        return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	

	
}
