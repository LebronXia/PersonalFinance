package com.sunshine.android.personal.fragment;




import java.util.Calendar;

import com.sunshine.android.personalfinance.AccountActivity;
import com.sunshine.android.personalfinance.CrimeCameraActivity;

import com.sunshine.android.personalfinance.MainActivity;
import com.sunshine.android.personalfinance.R;
import com.sunshine.android.personalfinance.dabase.DBHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 
 * @author wuwenjie
 * @description 浠婃棩
 */
public class ExpendFragment extends Fragment implements OnClickListener{
	private Spinner expand_type;   //选择支出类型
	private Spinner count_type;    	//账目类型
	private EditText money;				//花费的
	private EditText beizhu;		//备注
	private TextView dateView;		//录入时间
	private String filename;
	private AlertDialog alert;		//对话框
	private DatePicker datePicker; 		//日期选择控件
	private ImageButton mPhotoButton;
	private Button chooseData;			//选择时间按钮
	private Button end_ok;               //提交按钮
	private Button end_cancle;			//取消按钮
	private static final String[] expandType = 
		{"生活用品","水电煤气费","汽油费","吃饭","其他"};
	private static final int REQUEST_PHOTO = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_account,container, false);
		money = (EditText) v.findViewById(R.id.money);
		beizhu = (EditText) v.findViewById(R.id.beizhu);
		chooseData = (Button) v.findViewById(R.id.chooseData);
		chooseData.setOnClickListener(this);
		dateView = (TextView) v.findViewById(R.id.dataView);
		end_ok = (Button) v.findViewById(R.id.end_ok);
		end_ok.setOnClickListener(this);
		end_cancle = (Button) v.findViewById(R.id.end_cancel);
		end_cancle.setOnClickListener(this);
		
		//账目类型
		count_type = (Spinner) v.findViewById(R.id.counttype);
		count_type.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,
					new String[]{"现金支付","信用卡"}));
		mPhotoButton = (ImageButton) v.findViewById(R.id.crime_imageButton);
		mPhotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
				startActivityForResult(i, REQUEST_PHOTO);
			}
		});
		//初始化时间
		iniDate();
		//支付类型的选择
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,expandType);
		adapter.setDropDownViewResource(android.R.layout. simple_spinner_dropdown_item);
		expand_type = (Spinner) v.findViewById(R.id.chooseType);
		expand_type.setAdapter(adapter);
		
		//创建日期对话框
		Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater()
				.inflate(R.layout.dialog_date, null);
		datePicker = (DatePicker) view.findViewById(R.id.dialog_date_datePicker);
		builder.setTitle("请选择日期");
		builder.setView(view);
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String date = String.valueOf(datePicker.getYear()) + "-"
						+ String.valueOf(datePicker.getMonth() + 1) + "-"
						+ String.valueOf(datePicker.getDayOfMonth());
				dateView.setText(date);
			}
		});
		alert = builder.create();

		
		//如果对于不带相机的设备，拍照按钮应该禁用
				PackageManager pm = getActivity().getPackageManager();
				//后置摄像机
				boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)||
						//前置摄像机
						pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)||
						//版本号小于9
						Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD||
						//
						Camera.getNumberOfCameras() > 0;
						if(!hasACamera){
							mPhotoButton.setEnabled(false);
						}
				
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		double sum ;
		switch (v.getId()) {
		case R.id.chooseData:
			alert.show();			
			break;
		case R.id.end_ok:
			DBHelper database = new DBHelper(getActivity());
			Cursor cursor = database.getWritableDatabase().rawQuery("select * from tb2_account", null);
			if(cursor.getCount() == 0){
			sum = Integer.parseInt(money.getText().toString().trim());	
			addcount(database,sum);
			}else{
				 sum = 0;
				if(cursor.moveToLast()){
					sum = cursor.getDouble(cursor.getColumnIndex("sum"));
				}
					sum = sum + Integer.parseInt(money.getText().toString()
							.trim());
					addcount(database,sum);
			
			}
			break;
			case R.id.end_cancel:
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
			break;

		default:
			break;
		}
}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode != Activity.RESULT_OK) return;
		
		if(requestCode == REQUEST_PHOTO){
			filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
			if(filename != null){
				Log.i("ExpendFragment", "filename" + filename);
			}
		}
	}

	//添加数据库
	private void addcount(DBHelper database,double sum){
		database.getWritableDatabase().execSQL("insert into tb2_account values (null,?,?,?,?,?,?,?,?)",
				new Object[]{expand_type.getSelectedItem().toString().trim(),
				"支出", 
				money.getText().toString().trim(),
				dateView.getText().toString(),
				count_type.getSelectedItem().toString(),
				beizhu.getText().toString(),
				sum,
				filename});
		
		Toast.makeText(getActivity(), "保存成功", 1).show();
		money.setText("");
		beizhu.setText("");
	}
	private void iniDate(){
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		dateView.setText(year + "-" + month + "-" + day);		
	}

	
}
