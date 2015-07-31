package com.sunshine.android.personalfinance;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

public class UserRegisterActivity extends SingleFragmentActivity {

	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new UserRegisterFragment();
	}

}
