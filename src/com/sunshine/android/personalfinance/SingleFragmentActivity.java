package com.sunshine.android.personalfinance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
		//���һ��ͨ�õĳ��ۣ�����
public abstract class SingleFragmentActivity extends FragmentActivity {
	//����һ����ΪcreateFragmenr�ĳ��󷽷������ǿ�ʹ����ʵ�����µ�fragment
	protected abstract Fragment createFragment(); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fragment);
		//��ȡFragmentManager����
		FragmentManager fm = getSupportFragmentManager();
		//ʹ��������ͼ��ԴID��ȡfragment
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		if(fragment == null){
			//����һ���µ�Fragment���񣬼���һ����Ӳ�����Ȼ���ύ�����
			fragment = createFragment();
			fm.beginTransaction()//��beginTransaction()��������FragmentTransactionʵ�����ɴ˵õ�һ������
				.add(R.id.fragmentContainer, fragment)
				.commit();
		}
	}
}
