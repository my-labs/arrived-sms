package com.wat.arrivedsms;


import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;




@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapShow extends FragmentActivity{

	MapView mapView;
	MapController mController;
	LatLng currentLocationMapShow;
	EditText mapSearchBox;

	InputMethodManager inputManager;
	public static MarkerOptions marker;
	public static String mapShowed;
	public LatLng markerLatLngPosition;
	public String search;
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

		//Wyszukiwarka

		mapSearchBox = (EditText) findViewById(R.id.mapSearchBox);
		Button mapSearch = (Button) findViewById(R.id.search);
		Button mapViewType = (Button) findViewById(R.id.mapTypeChange);

		inputManager = (InputMethodManager)
				getSystemService(Context.INPUT_METHOD_SERVICE); 



		mapSearch.setOnClickListener(new OnClickListener() {



			@Override
			public void onClick(View v) {

				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

				if(mapSearchBox.getText().toString().trim().length()<=0){
					//Nie wpisano lokalizacji do wyszukiwania wiec nie rob nic
					Toast.makeText(MapShow.this, "Popraw puste pole!", Toast.LENGTH_SHORT).show();
				}
				else{
					search = mapSearchBox.getText().toString();
					Address address;
					try {
						Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
						List<Address> results = geocoder.getFromLocationName(search, 1);

						if (results.size() == 0) {
							System.out.println("Error");	
						}

						address = results.get(0);
						LatLng searchedLocation = new LatLng(address.getLatitude(), address.getLongitude());

						//Przesuniecie na wyszukiwana lokalizacje
						googleMap.moveCamera(CameraUpdateFactory.newLatLng(searchedLocation));
						// Zoom mapy
						googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));


					} catch (Exception e) {
						Log.e("", "Something went wrong: ", e);

					}
				}
			}
		});

		mapViewType.setOnClickListener(new OnClickListener() {
			int mapFlag=0;
			@Override
			public void onClick(View v) {
				if(mapFlag == 0){
					googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					mapFlag = 1;
				}
				else if(mapFlag == 1){
					googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					mapFlag = 0;
				}
			}
		});




		googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();

		//Pobranie aktualnej lokalizacji

		//			Jezeli aktualna pozycja  jest pusta pobierz ostatnia zapamietana lokalizacje
		if(LocationMessage.locationMapView == null  ){

			Toast.makeText(MapShow.this, "Poczekaj na pobranie aktualnej lokalizacji.",Toast.LENGTH_SHORT).show();
			//			Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			//			currentLocationMapShow = new LatLng(location.getLatitude(),location.getLongitude());   
		}
		else{
			currentLocationMapShow = new LatLng(LocationMessage.locationMapView.getLatitude(),LocationMessage.locationMapView.getLongitude());
		}

		//Pobranie ostatniej znanej lokalizacji


		//		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		//		LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());      


		//Przesuniecie na aktualna lokalizacje
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocationMapShow));
		// Zoom mapy
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));

		googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng point) {
				marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title("New Marker");
				googleMap.addMarker(marker);
				mapShowed = "true";
				Toast.makeText(MapShow.this, "Wskazano lokalizacje!",Toast.LENGTH_LONG ).show();

				final Dialog dialog = new Dialog(MapShow.this);
				dialog.setContentView(R.layout.dialog);
				dialog.setTitle("Wybrano lokalizacje");

				// Ustawienie wartosci dla komponentow dialogu (text)
				TextView text = (TextView) dialog.findViewById(R.id.text);
				text.setText("Lokalizacja docelowa jest poprawna?");

				Button buttonYes = (Button) dialog.findViewById(R.id.dialogButtonYes);
				Button buttonNo = (Button) dialog.findViewById(R.id.dialogButtonNo);

				dialog.show();
				buttonNo.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						googleMap.clear();
						dialog.dismiss();
						Toast.makeText(MapShow.this, "Zmien lokalizacje",Toast.LENGTH_LONG ).show();

					}
				});

				buttonYes.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
						finish();
					}
				});

			}
		}); 

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