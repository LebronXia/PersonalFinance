package com.sunshine.android.personal.fragment;

import com.sunshine.android.personalfinance.MainActivity;
import com.sunshine.android.personalfinance.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;


/**
 * @date 2014/11/14
 * @author wuwenjie
 * @description ²à±ßÀ¸²Ëµ¥
 */
public class LeftFragment extends Fragment implements OnClickListener{
	private View shouYeView;
	private View discussView;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_menu, null);
		findViews(view);
		
		return view;
	}
	
	
	public void findViews(View view) {
		shouYeView = view.findViewById(R.id.tvShouye);
		//lastListView = view.findViewById(R.id.tvLastlist
		discussView = view.findViewById(R.id.tvDiscussMeeting);

		
		shouYeView.setOnClickListener(this);
		//lastListView.setOnClickListener(this);
		discussView.setOnClickListener(this);

	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Fragment newContent = null;
		String title = null;
		switch (v.getId()) {
		case R.id.tvShouye: // Ê×Ò³
			newContent = new ShouyeFragment();
			title = getString(R.string.shouye);
			break;
//		case R.id.tvLastlist:// ÊÕÈë
//			newContent = new IncomeFragment();
//			title = getString(R.string.income);
//			break;
		case R.id.tvDiscussMeeting: // Ã÷Ï¸ 
			newContent = new AccountFragment();
			title = getString(R.string.account);
			break;
		default:
			break;
		}
		if (newContent != null) {
			switchFragment(newContent, title);
		}
	}
	
	/**
	 * ÇÐ»»fragment 
	 * @param fragment
	 */
	private void switchFragment(Fragment fragment, String title) {
		if (getActivity() == null) {
			return;
		}
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchConent(fragment, title);
		}
	}
	
}
