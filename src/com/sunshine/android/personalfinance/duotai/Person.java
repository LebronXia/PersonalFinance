package com.sunshine.android.personalfinance.duotai;

public class Person {
	private String mUserId;
	private String mUserPwd;
	public Person(){
		
	}
	
	public Person(String mUserId, String mUserPwd) {
		super();
		this.mUserId = mUserId;
		this.mUserPwd = mUserPwd;
	}


	public String getUserId() {
		return mUserId;
	}

	public void setUserId(String userId) {
		mUserId = userId;
	}

	public String getUserPwd() {
		return mUserPwd;
	}

	public void setUserPwd(String userPwd) {
		mUserPwd = userPwd;
	}	
	
}
