package com.sunshine.android.personal.fragment;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import com.sunshine.android.personalfinance.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CrimeCameraFragment extends Fragment {

	private static final String TAG = "CrimeCameraFragment";
	
	public static final String EXTRA_PHOTO_FILENAME = 
			"com.xiamu.android.CrimeCameraFragment.photo_filename";
	private View mProgressContainer;
	
	private Camera mCamera;
	private SurfaceView mSurfaceView;
	//将列表中的预览尺寸与传入sufaceChanged()方法的surface的宽和高进行，找到最佳的大小
	private Size getBestSupportedSize(List<Size> sizes, int width, int height){
		Size bestSize = sizes.get(0);
		int largestArea = bestSize.width * bestSize.height;
		for(Size s : sizes){
			int area = s.width * s.height;
			if(area > largestArea){
				bestSize = s;
				largestArea = area;
			}
		}
		return bestSize;
	}
	//在相机捕获图像时被调用，显示接口显示条
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		
		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			mProgressContainer.setVisibility(View.VISIBLE);
			
		}
	};
	//在原始图像数据可用时被
	private Camera.PictureCallback mJeCallback = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			//创建文件名                     全局唯一标识符，的一个自动生成主键的方法
			String filename = UUID.randomUUID().toString() + ".jpg";
			//保存文件
			FileOutputStream os =null;
			boolean success = true;
			
			try {
				os = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				os.write(data);
			} catch (Exception e) {
				// TODO: handle exception
				success = false;
			}finally{
				try {
					if(os != null)
						os.close();
				} catch (Exception e) {
					// TODO: handle exception
					Log.i(TAG, "Error closing file" + filename, e);
					success = false;
				}
			}
					//如果数据保存成功，则返回数据
					if(success){
						Log.i(TAG, "JPEG saved at" + filename);
						Intent i = new Intent();
						i.putExtra(EXTRA_PHOTO_FILENAME, filename);
						getActivity().setResult(Activity.RESULT_OK,i);
					}else{
						getActivity().setResult(Activity.RESULT_CANCELED);
					}
				getActivity().finish();
		}
	};
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_crime_camera, container, false);
		mProgressContainer = v.findViewById(R.id.crime_camera_progressContainer);
		//设置进度条不可见状态,只是变透明了
		mProgressContainer.setVisibility(View.INVISIBLE);
		
		Button takePictureButton = (Button) v.findViewById(R.id.crime_camera_takePictureButton);
		takePictureButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//activity退出栈，但不销毁。返回到列表明细界面
				if(mCamera != null){
					mCamera.takePicture(mShutterCallback, null, mJeCallback);
				}
			}
		});
		mSurfaceView = (SurfaceView) v.findViewById(R.id.crime_camera_surfaceView);
		//在SurfaceView中一般通过使用SurfaceHolder类来控制Canvas在其surface上的操作
		SurfaceHolder holder = mSurfaceView.getHolder();
		//为了解决兼容性问题
		//SURFACE_TYPE_PUSH_BUFFERS表明该Surface不包含原生数据，Surface用到的数据由其他对象提供，
		//在Camera图像预览中就使用该类型的Surface，有Camera负责提供给预览Surface数据，这样图像预览会比较流畅。		
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		//响应surface的生命周期
		//为SurfaceHolder添加一个SurfaceHolder.Callback回调接口。
		holder.addCallback(new SurfaceHolder.Callback() {
			
			//当surface对象在将要销毁前，该方法会被立即调用。
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				if( mCamera != null){
					mCamera.stopPreview();
				}				
			}
			
			//当surface对象创建后，该方法就会被立即调用。
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				try {
					if(mCamera != null){
						//连接CamerayuSurface
						mCamera.setPreviewDisplay(holder);
					}
				} catch (IOException e) {
					// TODO: handle exception
					Log.d(TAG, "Error setting up preview display", e);
				}
			}
			
			//当surface发生任何结构性的变化时（格式或者大小），该方法就会被立即调用。
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				if(mCamera == null) return;
				
				//surface尺寸大小改变时，升级相片的预览尺寸
				//获取系统支持的相机预览储存列表
				Camera.Parameters parameters = mCamera.getParameters();
				//Size s= null;
				//得到最大数目像素的尺寸
				Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
				parameters.setPictureSize(s.width, s.height);//设置预览帧数据的尺寸
				s = getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
				parameters.setPictureSize(s.width, s.height);
				mCamera.setParameters(parameters);
				try {
					//用来在Surface上绘制帧
					mCamera.startPreview();
				} catch (Exception e) {
					// TODO: handle exception
					Log.d(TAG, "could not start preview", e);
					mCamera = null;
				}
				
			}
		});
		
		return v;
	}
	@TargetApi(9)
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			mCamera =Camera.open(0);
			
		}else{
			mCamera = Camera.open();
		}
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//调用相机相关代码时，都应检查实例是否存在
		if(mCamera != null){
			mCamera.release();
			mCamera = null;
		}
	}
}
