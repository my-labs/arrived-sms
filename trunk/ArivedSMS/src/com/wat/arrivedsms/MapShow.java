package com.wat.arrivedsms;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.wat.arrivedsms.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

	

public class MapShow extends FragmentActivity{
	
	private GoogleMap mMap;
	//MapActivity,
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapshow);
		
		
//		SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//		mMap = supportMapFragment.getMap();
	        if (mMap== null)
	        {
	            Toast.makeText(this,"Google Maps not Available",
	                    Toast.LENGTH_LONG).show();
	        }
		
	}


	
}
