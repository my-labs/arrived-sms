package com.wat.arrivedsms;



import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class LocationMessage extends Activity {

	private EditText input;
	private Button getInfo;

	public static Location locationMapView;
	LocationManager lm;

	String numberK="663992176";
	String numberM="512238500";
	String text="Dotar≥em!";
	Double latDefin = 0.0;
	Double longDefin = 0.0;
	double tmp = 0.0;
	Integer count;

	Button btnSignIn,btnSignUp;
	ImageView kropka_zielona,kropka_czerwona;
	LoginDataBaseAdapter loginDataBaseAdapter;
	TextView result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		count=0;
		search();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener ll = new mylocationlistener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

		kropka_zielona = (ImageView) findViewById(R.id.kropka_zielona);
		kropka_czerwona = (ImageView) findViewById(R.id.kropka_czerwona);

		//Status GPS_kropka
		if(locationMapView != null){

			kropka_czerwona.setVisibility(View.INVISIBLE);
			kropka_zielona.setVisibility(View.VISIBLE);

		}

		input = (EditText)findViewById(R.id.number);
		result = (TextView)findViewById(R.id.searchResult);
		getInfo = (Button)findViewById(R.id.button1);
		getInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if((checkEnableGPS()==true) && (isConnectingToInternet()==true && locationMapView != null ) ){
					Intent i = new Intent(getApplicationContext(), MapShow.class);
					startActivity(i);
				}
				else if((checkEnableGPS()==false) || (isConnectingToInternet()==false)){
					Toast.makeText(LocationMessage.this, "Wlacz internet i lokalizacje!",Toast.LENGTH_LONG ).show();
				}
				else if(locationMapView == null){
					final Dialog dialog = new Dialog(LocationMessage.this);
					dialog.setContentView(R.layout.dialog_lokalizacja);
					dialog.setTitle("èrÛdlo poloøenia dla mapy ?");

					// Ustawienie wartosci dla komponentow dialogu (text)
					TextView text = (TextView) dialog.findViewById(R.id.text);
					text.setText("Brak aktualnej pozycji GPS. Wczytac ostatnie znane poloøenie?");

					Button buttonYes = (Button) dialog.findViewById(R.id.dialogLokalizButtonYes);
					Button buttonNo = (Button) dialog.findViewById(R.id.dialogLokalizButtonNo);

					dialog.show();
					buttonNo.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							dialog.dismiss();
							Toast.makeText(LocationMessage.this, "Czekam na sygnal GPS",Toast.LENGTH_LONG ).show();

						}
					});

					buttonYes.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();
							locationMapView = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if(locationMapView == null){
								Toast.makeText(LocationMessage.this, "Brak zapisanej lokalizacji.Poczekaj na pobranie aktualnego polozenia.", Toast.LENGTH_SHORT).show();
							}
							else{
								Intent i = new Intent(getApplicationContext(), MapShow.class);
								startActivity(i);
							}
						}
					});


					//Toast.makeText(LocationMessage.this, "Poczekaj na pobranie aktualnej lokalizacji",Toast.LENGTH_SHORT).show();
				}
			}



		});
	}
	private class mylocationlistener implements LocationListener {
		@Override
		public void onLocationChanged(Location  location) {

			if(location != null){
				locationMapView = location;
				//status GPS_kropka

				if(locationMapView != null){

					kropka_czerwona.setVisibility(View.INVISIBLE);
					kropka_zielona.setVisibility(View.VISIBLE);
				}
				else{
					
					kropka_czerwona.setVisibility(View.VISIBLE);
					kropka_zielona.setVisibility(View.INVISIBLE);
				}	

			}
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

	// Zwraca odleg≥oúÊ w metrach
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


	@Override
	protected void onRestart() {
		if(MapShow.mapShowed=="true"){
			LatLng positionLatLng = MapShow.marker.getPosition();
			latDefin = positionLatLng.latitude;
			longDefin = positionLatLng.longitude;
		}
		super.onRestart();
	}

	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) 
		{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) 
				for (int i = 0; i < info.length; i++) 
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}

		}
		return false;
	}

	private boolean checkEnableGPS(){
		String provider = Settings.Secure.getString(getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if(provider.contains("gps")){

			if(!provider.equals("")){
				//GPS Enabled
				return true;
			}


		}
		return false;
	}

	
	private void search(){
		SearchManager searchMan = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		final SearchView searchView = (SearchView) findViewById(R.id.search);
		SearchableInfo searchableInfo = (SearchableInfo) searchMan.getSearchableInfo(getComponentName());
		searchView.setSearchableInfo(searchableInfo);
	}
	
	private String getDisplayNameForContact(Intent intent) {
		Cursor phoneCursor = getContentResolver().query(intent.getData(), null, null, null, null);
		phoneCursor.moveToFirst();
		int idDisplayName = phoneCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		String name = phoneCursor.getString(idDisplayName);
		phoneCursor.close();
		return name;
	}
	
	
	@Override
	protected void onNewIntent(Intent intent) {
		if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent.getAction())) {
			//handles suggestion clicked query
			String displayName = getDisplayNameForContact(intent);
			result.setText(displayName);
		} else if(Intent.ACTION_SEARCH.equals(intent.getAction())){
			String query = intent.getStringExtra(SearchManager.QUERY);
			result.setText("should search for query: '" + query + "'...");
		}
	}
	
	



}

