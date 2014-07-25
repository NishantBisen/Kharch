package com.dailyexpense.model;

import java.util.Date;

public class Expense {
	
	private String date;
	private double amount;
	private String spentOn;
	private String note;
	
	public Expense() {
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getSpentOn() {
		return spentOn;
	}
	public void setSpentOn(String spentOn) {
		this.spentOn = spentOn;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	

}
