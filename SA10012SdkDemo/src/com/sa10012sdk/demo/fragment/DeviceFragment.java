package com.sa10012sdk.demo.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import com.sa10012sdk.demo.MainActivity;
import com.sa10012sdk.demo.R;
import com.sa10012sdk.demo.SearchBleDeviceActivity;
import com.sleepace.sdk.interfs.IConnectionStateCallback;
import com.sleepace.sdk.interfs.IDeviceManager;
import com.sleepace.sdk.interfs.IResultCallback;
import com.sleepace.sdk.manager.CONNECTION_STATE;
import com.sleepace.sdk.manager.CallbackData;
import com.sleepace.sdk.manager.ble.BleHelper;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceFragment extends BaseFragment {
	private Button btnConnectDevice, btnDeviceName, btnDeviceId, btnVersion, btnUpgrade;
	private TextView tvDeviceName, tvDeviceId, tvVersion;
	private boolean upgrading = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View root = inflater.inflate(R.layout.fragment_device, null);
//		LogUtil.log(TAG+" onCreateView-----------");
		findView(root);
		initListener();
		initUI();
		return root;
	}
	
	protected void findView(View root) {
		// TODO Auto-generated method stub
		super.findView(root);
		tvDeviceName = (TextView) root.findViewById(R.id.tv_device_name);
		tvDeviceId = (TextView) root.findViewById(R.id.tv_device_id);
		tvVersion = (TextView) root.findViewById(R.id.tv_device_version);
		btnConnectDevice = (Button) root.findViewById(R.id.btn_connect_device);
		btnDeviceName = (Button) root.findViewById(R.id.btn_get_device_name);
		btnDeviceId = (Button) root.findViewById(R.id.btn_get_device_id);
		btnVersion = (Button) root.findViewById(R.id.btn_device_version);
		btnUpgrade = (Button) root.findViewById(R.id.btn_upgrade_fireware);
	}


	protected void initListener() {
		// TODO Auto-generated method stub
		super.initListener();
		getDeviceHelper().addConnectionStateCallback(stateCallback);
		btnConnectDevice.setOnClickListener(this);
		btnDeviceName.setOnClickListener(this);
		btnDeviceId.setOnClickListener(this);
		btnVersion.setOnClickListener(this);
		btnUpgrade.setOnClickListener(this);
	}


	protected void initUI() {
		// TODO Auto-generated method stub
		mActivity.setTitle(R.string.device);
		tvDeviceName.setText(MainActivity.deviceName);
		tvDeviceId.setText(MainActivity.deviceId);
		tvVersion.setText(MainActivity.version);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		boolean isConnected = getDeviceHelper().isConnected();
		initPageState(isConnected);
	}
	
	private void initPageState(boolean isConnected) {
		initBtnConnectState(isConnected);
		setPageEnable(isConnected);
		if(!isConnected) {
			tvDeviceName.setText(null);
			tvDeviceId.setText(null);
			tvVersion.setText(null);
		}
	}
	
	private void initBtnConnectState(boolean isConnected) {
		if(isConnected) {
			btnConnectDevice.setText(R.string.disconnect);
			btnConnectDevice.setTag("disconnect");
		}else {
			btnConnectDevice.setText(R.string.connect_device);
			btnConnectDevice.setTag("connect");
		}
	}
	
	private void setPageEnable(boolean enable){
		btnDeviceName.setEnabled(enable);
		btnDeviceId.setEnabled(enable);
		btnVersion.setEnabled(enable);
		btnUpgrade.setEnabled(enable);
	}
	
	private IConnectionStateCallback stateCallback = new IConnectionStateCallback() {
		@Override
		public void onStateChanged(IDeviceManager manager, final CONNECTION_STATE state) {
			// TODO Auto-generated method stub
			
			if(!isAdded()){
				return;
			}
			
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					initPageState(state == CONNECTION_STATE.CONNECTED);
					
					if(state == CONNECTION_STATE.DISCONNECT){
						
						if(upgrading){
							upgrading = false;
							mActivity.hideUpgradeDialog();
							mActivity.setUpgradeProgress(0);
						}
						
					}else if(state == CONNECTION_STATE.CONNECTED){
						
						if(upgrading){
							upgrading = false;
							btnUpgrade.setEnabled(true);
							mActivity.hideUpgradeDialog();
						}
					}
				}
			});
		}
	};
	

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		getDeviceHelper().removeConnectionStateCallback(stateCallback);
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if(v == btnConnectDevice) {
			Object tag = v.getTag();
			if(tag == null || "connect".equals(tag)) {
				if(!BleHelper.getInstance(mActivity).isBluetoothOpen()) {
					Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enabler, BleHelper.REQCODE_OPEN_BT);
				}else {
					Intent intent = new Intent(mActivity, SearchBleDeviceActivity.class);
					startActivity(intent);
				}
			}else {//断开设备
				getDeviceHelper().disconnect();
			}
		}else if(v == btnUpgrade){
			FirmwareBean bean = getFirmwareBean();
			if(bean == null){
				return;
			}
			
			btnUpgrade.setEnabled(false);
			mActivity.showUpgradeDialog();
			mActivity.setUpgradeProgress(0);
			upgrading = true;
			getDeviceHelper().deviceUpgrade(bean.fileName, bean.is, new IResultCallback<Integer>() {
				@Override
				public void onResultCallback(final CallbackData<Integer> cd) {
					// TODO Auto-generated method stub
//					LogUtil.log(TAG+" upgradeDevice " + cd);
					mActivity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if(cd.isSuccess()){
								int progress =  cd.getResult();
								mActivity.setUpgradeProgress(progress);
								if(progress == 100){
									upgrading = false;
									btnUpgrade.setEnabled(true);
									mActivity.hideUpgradeDialog();
									Toast.makeText(mActivity, R.string.up_success, Toast.LENGTH_SHORT).show();
									getDeviceHelper().disconnect();
								}
							}else{
								upgrading = false;
								btnUpgrade.setEnabled(getDeviceHelper().isConnected());
								mActivity.hideUpgradeDialog();
								Toast.makeText(mActivity, R.string.up_failed, Toast.LENGTH_SHORT).show();
							}
						}
					});
				}
			});
		}else if(v == btnDeviceName){
			tvDeviceName.setText(MainActivity.deviceName);
		}else if(v == btnDeviceId){
			tvDeviceId.setText(MainActivity.deviceId);
		}else if(v == btnVersion){
			tvVersion.setText(MainActivity.version);
		}
	}
	
	class FirmwareBean{
		String fileName;
		InputStream is;
	}
	
	private FirmwareBean getFirmwareBean(){
		try {
			String fileName = "SA1001_2_20180817.0.51_beta.MVA";
			InputStream is = mActivity.getResources().getAssets().open(fileName);
			FirmwareBean bean = new FirmwareBean();
			bean.fileName = fileName;
			bean.is = is;
			return bean;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}









