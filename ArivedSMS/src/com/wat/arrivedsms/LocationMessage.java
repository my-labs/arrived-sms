package com.wat.arrivedsms;



import com.wat.arrivedsms.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LocationMessage extends Activity {
	
	private EditText input;
	private Button getInfo;

	String number;
	String numberK="663992176";
	String numberM="512238500";
	String text="Dotar³em!";
	Double latDefin=52.187;
	Double longDefin=21.557;
	Integer count;
	PendingIntent pendingIntent;

	Button btnSignIn,btnSignUp;
    LoginDataBaseAdapter loginDataBaseAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		count=0;
		
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    LocationListener ll = new mylocationlistener();
	    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
	
		 input = (EditText)findViewById(R.id.number);
		 getInfo = (Button)findViewById(R.id.button1);
		 getInfo.setOnClickListener(new OnClickListener() {

	        @Override
	        public void onClick(View v) {
	        	 Intent i = new Intent(getApplicationContext(), MapShow.class);
         		 startActivity(i);

	        }
	    });
	}
	private class mylocationlistener implements LocationListener {
		   @Override
		    public void onLocationChanged(Location location) {
		        
			   if(count==0){
				  
				   double distance=distancee(latDefin,longDefin,location.getLatitude(),location.getLongitude());
		        	if(distance< 50){
		        		 count=1;
		        		
		        		try {
		 					SmsManager smsManager = SmsManager.getDefault();
		 					smsManager.sendTextMessage(numberK, null, text, null, null);
		 					Toast.makeText(getApplicationContext(), "SMS Sent!",
		 								Toast.LENGTH_SHORT).show();
		 					
		 				  } catch (Exception e) {
		 					Toast.makeText(getApplicationContext(),
		 						"SMS faild, please try again later!",
		 						Toast.LENGTH_LONG).show();
		 					e.printStackTrace();
		 				  }
		        	}
//		        	else if (distance>50){
//		        		count=0;
//		        	}
		        	
			   }
		   }
		     
		   @Override
		    public void onProviderDisabled(String provider) {
		    }
		    @Override
		    public void onProviderEnabled(String provider) {
		    }
		    @Override
		    public void onStatusChanged(String provider, int status, Bundle extras) {
		    }
	}	
	
	// Zwraca odleg³oœæ w metrach
	private double distancee(double lat1, double lon1, double lat2, double lon2) {
			  double theta = lon1 - lon2;
			  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
			  dist = Math.acos(dist);
			  dist = rad2deg(dist);
			  dist = dist * 60 * 1.1515;
			  dist = dist * 1609.344;
			  return dist;
			  
	}
			
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
		}
	
	private double rad2deg(double rad) {
			  return (rad * 180 / Math.PI);
			}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	
}

