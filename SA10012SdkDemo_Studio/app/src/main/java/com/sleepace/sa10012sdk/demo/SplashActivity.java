package com.sleepace.sa10012sdk.demo;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SplashActivity extends BaseActivity {
	
	private TextView tvVer;
	private Button btnSkip, btnSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		findView();
		initListener();
		initUI();
	}
	
	
	@Override
	protected void findView() {
		// TODO Auto-generated method stub
		super.findView();
		tvVer = (TextView) findViewById(R.id.tv_ver);
		btnSkip = (Button) findViewById(R.id.btn_skip);
		btnSearch = (Button) findViewById(R.id.btn_search_device);
	}


	@Override
	protected void initListener() {
		// TODO Auto-generated method stub
		super.initListener();
		btnSkip.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
	}


	@Override
	protected void initUI() {
		// TODO Auto-generated method stub
		super.initUI();
		tvVer.setText(getAppVersionName());
	}
	
	
	private String getAppVersionName() {  
	    String versionName = "";  
	    try {  
	        // ---get the package info---  
	        PackageManager pm = getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);  
	        versionName = pi.versionName;  
	        if (versionName == null || versionName.length() <= 0) {  
	            return "";  
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();
	    }  
	    return versionName;  
	}  


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if(v == btnSkip){
//			Intent intent = new Intent(this, MainActivity.class);
//			startActivity(intent);
		}else if(v == btnSearch){
			Intent intent = new Intent(this, SearchBleDeviceActivity.class);
			startActivity(intent);
		}
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
	
}








































