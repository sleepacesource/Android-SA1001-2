package com.sleepace.sa10012sdk.demo.fragment;

import com.sleepace.sa10012sdk.demo.BaseActivity;
import com.sleepace.sa10012sdk.demo.MainActivity;
import com.sleepace.sdk.sa10012.SA10012Helper;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment implements OnClickListener{
	
	protected String TAG = getClass().getSimpleName();
	protected MainActivity mActivity;
	private SA10012Helper mHelper;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mActivity = (MainActivity) getActivity();
		mHelper = SA10012Helper.getInstance(mActivity);
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	public SA10012Helper getDeviceHelper() {
		return mHelper;
	}

	protected void findView(View root) {
		// TODO Auto-generated method stub
	}


	protected void initListener() {
		// TODO Auto-generated method stub
	}

	protected void initUI() {
		// TODO Auto-generated method stub
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	public void registerTouchListener(BaseActivity.MyOnTouchListener myOnTouchListener) {
		mActivity.registerTouchListener(myOnTouchListener);
    }
	
    public void unregisterTouchListener(BaseActivity.MyOnTouchListener myOnTouchListener) {
    	mActivity.unregisterTouchListener(myOnTouchListener);
    }
	
}



