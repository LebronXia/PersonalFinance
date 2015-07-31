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
	//���б��е�Ԥ���ߴ��봫��sufaceChanged()������surface�Ŀ�͸߽��У��ҵ���ѵĴ�С
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
	//���������ͼ��ʱ�����ã���ʾ�ӿ���ʾ��
	private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
		
		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			mProgressContainer.setVisibility(View.VISIBLE);
			
		}
	};
	//��ԭʼͼ�����ݿ���ʱ��
	private Camera.PictureCallback mJeCallback = new Camera.PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			// TODO Auto-generated method stub
			//�����ļ���                     ȫ��Ψһ��ʶ������һ���Զ����������ķ���
			String filename = UUID.randomUUID().toString() + ".jpg";
			//�����ļ�
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
					//������ݱ���ɹ����򷵻�����
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
		//���ý��������ɼ�״̬,ֻ�Ǳ�͸����
		mProgressContainer.setVisibility(View.INVISIBLE);
		
		Button takePictureButton = (Button) v.findViewById(R.id.crime_camera_takePictureButton);
		takePictureButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//activity�˳�ջ���������١����ص��б���ϸ����
				if(mCamera != null){
					mCamera.takePicture(mShutterCallback, null, mJeCallback);
				}
			}
		});
		mSurfaceView = (SurfaceView) v.findViewById(R.id.crime_camera_surfaceView);
		//��SurfaceView��һ��ͨ��ʹ��SurfaceHolder��������Canvas����surface�ϵĲ���
		SurfaceHolder holder = mSurfaceView.getHolder();
		//Ϊ�˽������������
		//SURFACE_TYPE_PUSH_BUFFERS������Surface������ԭ�����ݣ�Surface�õ������������������ṩ��
		//��Cameraͼ��Ԥ���о�ʹ�ø����͵�Surface����Camera�����ṩ��Ԥ��Surface���ݣ�����ͼ��Ԥ����Ƚ�������		
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		//��Ӧsurface����������
		//ΪSurfaceHolder���һ��SurfaceHolder.Callback�ص��ӿڡ�
		holder.addCallback(new SurfaceHolder.Callback() {
			
			//��surface�����ڽ�Ҫ����ǰ���÷����ᱻ�������á�
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				if( mCamera != null){
					mCamera.stopPreview();
				}				
			}
			
			//��surface���󴴽��󣬸÷����ͻᱻ�������á�
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				try {
					if(mCamera != null){
						//����CamerayuSurface
						mCamera.setPreviewDisplay(holder);
					}
				} catch (IOException e) {
					// TODO: handle exception
					Log.d(TAG, "Error setting up preview display", e);
				}
			}
			
			//��surface�����κνṹ�Եı仯ʱ����ʽ���ߴ�С�����÷����ͻᱻ�������á�
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width,
					int height) {
				// TODO Auto-generated method stub
				if(mCamera == null) return;
				
				//surface�ߴ��С�ı�ʱ��������Ƭ��Ԥ���ߴ�
				//��ȡϵͳ֧�ֵ����Ԥ�������б�
				Camera.Parameters parameters = mCamera.getParameters();
				//Size s= null;
				//�õ������Ŀ���صĳߴ�
				Size s = getBestSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
				parameters.setPictureSize(s.width, s.height);//����Ԥ��֡���ݵĳߴ�
				s = getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
				parameters.setPictureSize(s.width, s.height);
				mCamera.setParameters(parameters);
				try {
					//������Surface�ϻ���֡
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
		//���������ش���ʱ����Ӧ���ʵ���Ƿ����
		if(mCamera != null){
			mCamera.release();
			mCamera = null;
		}
	}
}
