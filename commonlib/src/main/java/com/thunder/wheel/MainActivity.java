//package com.thunder.wheel;
//
//import java.util.Calendar;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.thunder.wheel.view.NumericWheelAdapter;
//import com.thunder.wheel.view.OnWheelChangedListener;
//import com.thunder.wheel.view.OnWheelClickedListener;
//import com.thunder.wheel.view.OnWheelScrollListener;
//import com.thunder.wheel.view.WheelView;
//
//public class MainActivity extends Activity {
//	// Time changed flag
//	private boolean timeChanged = false;
//
//	// Time scrolled flag
//	private boolean timeScrolled = false;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.activity_main);
//
//		final WheelView hours = (WheelView) findViewById(R.id.hour);
//		hours.setViewAdapter(new NumericWheelAdapter(this, 0, 23));
//		hours.setCyclic(true);//����ѭ��
//
//
//		final WheelView mins = (WheelView) findViewById(R.id.mins);
//		mins.setViewAdapter(new NumericWheelAdapter(this, 0, 59, "%02d")); // ���롰 "%02d" �� �����λ��0-��00
//		mins.setCyclic(true);
//
//		TextView ok = (TextView) findViewById(R.id.ok);
//		ok.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Toast.makeText(MainActivity.this, hours.getCurrentItem()+":"+mins.getCurrentItem(), Toast.LENGTH_SHORT).show();
//			}
//		});
//		Calendar c = Calendar.getInstance();
//		int curHours = c.get(Calendar.HOUR_OF_DAY);
//		int curMinutes = c.get(Calendar.MINUTE);
//
//		addChangingListener(mins, "min");
//		addChangingListener(hours, "hour");
//
//		OnWheelChangedListener wheelListener = new OnWheelChangedListener() {
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				if (!timeScrolled) {
//					timeChanged = true;
//					Log.v("==1", hours.getCurrentItem()+":"+mins.getCurrentItem());
//
//					timeChanged = false;
//				}
//			}
//		};
//		hours.addChangingListener(wheelListener);
//		mins.addChangingListener(wheelListener);
//
//		OnWheelClickedListener click = new OnWheelClickedListener() {
//            public void onItemClicked(WheelView wheel, int itemIndex) {
//                wheel.setCurrentItem(itemIndex, true);
//            }
//        };
//        hours.addClickingListener(click);
//        mins.addClickingListener(click);
//
//		OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
//			public void onScrollingStarted(WheelView wheel) {
//				timeScrolled = true;
//			}
//			public void onScrollingFinished(WheelView wheel) {
//				timeScrolled = false;
//				timeChanged = true;
//				Log.v("==2", hours.getCurrentItem()+":"+mins.getCurrentItem());
////				picker.setCurrentHour(hours.getCurrentItem());
////				picker.setCurrentMinute(mins.getCurrentItem());
//				timeChanged = false;
//			}
//		};
//
//		hours.addScrollingListener(scrollListener);
//		mins.addScrollingListener(scrollListener);
//
//
//	}
//
//	/**
//	 * Adds changing listener for wheel that updates the wheel label
//	 * @param wheel the wheel
//	 * @param label the wheel label
//	 */
//	private void addChangingListener(final WheelView wheel, final String label) {
//		wheel.addChangingListener(new OnWheelChangedListener() {
//			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				//wheel.setLabel(newValue != 1 ? label + "s" : label);
//				//Toast.makeText(MainActivity.this, label, Toast.LENGTH_LONG).show();
//			}
//		});
//	}
//}