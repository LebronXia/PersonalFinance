package com.sunshine.android.personalfinance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Window;
		//添加一个通用的超累，抽象
public abstract class SingleFragmentActivity extends FragmentActivity {
	//新增一个名为createFragmenr的抽象方法，我们可使用它实例化新的fragment
	protected abstract Fragment createFragment(); 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fragment);
		//获取FragmentManager本身
		FragmentManager fm = getSupportFragmentManager();
		//使用容器视图资源ID获取fragment
		Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
		
		if(fragment == null){
			//创建一个新的Fragment事务，加入一个添加操作，然后提交该事物。
			fragment = createFragment();
			fm.beginTransaction()//用beginTransaction()方法返回FragmentTransaction实例，由此得到一个队列
				.add(R.id.fragmentContainer, fragment)
				.commit();
		}
	}
}
