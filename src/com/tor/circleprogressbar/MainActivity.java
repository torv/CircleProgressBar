package com.tor.circleprogressbar;

import com.tor.circleprogressbar.view.CircleProgressBar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class MainActivity extends Activity {

	private CircleProgressBar mCircleProgressBar;
	private boolean mRunThread;
	private static float mProgress = 0;
	private Thread mUpdateProgressThread;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			mCircleProgressBar.setProgress(mProgress);
			super.handleMessage(msg);
		}
		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
	}


	private void init() {
		mCircleProgressBar = (CircleProgressBar)findViewById(R.id.cpr_progress);
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {

		mRunThread = false;
		mUpdateProgressThread.interrupt();
		mUpdateProgressThread = null;
		super.onPause();
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {

		mRunThread = true;
		
		mUpdateProgressThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(mRunThread){
					mProgress++;
					mProgress = mProgress>100?0:mProgress;
					mHandler.sendEmptyMessage(0);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		mUpdateProgressThread.setName("UpdateProgressThread");
		mUpdateProgressThread.start();
		
		super.onResume();
	}
	
}
