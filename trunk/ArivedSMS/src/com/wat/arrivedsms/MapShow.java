package com.wat.arrivedsms;


import android.annotation.TargetApi;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.maps.MapController;




@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapShow extends FragmentActivity{

	MapView mapView;
	MapController mController;
	LatLng currentLocation;

	//	private static  LatLng currentLocation = new LatLng(52.187 , 21.557) ;
	private GoogleMap googleMap;
	//MapActivity,
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapshow);

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener llistener = new mylocationlistener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, llistener);

		googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
		//Pobranie aktualnej lokalizacji
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());      
		//Przesuniecie na aktualna lokalizacje
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		// Zoom mapy
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));


	}
	private class mylocationlistener implements LocationListener {
		@Override
		public void onLocationChanged(Location location) {

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}
	}
}
