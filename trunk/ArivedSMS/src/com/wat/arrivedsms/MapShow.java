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
	public static Address address;

	double markerLatitude;
	double markerLongitude;
	public LatLng markerLatLngPosition;
	public String search;
	private GoogleMap googleMap;
	
	//Aktywnosc odpowiadajacac za google maps
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapshow);

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener llistener = new mylocationlistener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, llistener);

		// Wyszukiwanie lokalizacji (Search Box na mapie google)
		
		mapSearchBox = (EditText) findViewById(R.id.mapSearchBox);
		Button mapSearch = (Button) findViewById(R.id.search);
		Button mapViewType = (Button) findViewById(R.id.mapTypeChange);

		inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
		mapSearch.setOnClickListener(new OnClickListener() { 										//Obsluga klikniecia przycisku mapSearch
			@Override
			public void onClick(View v) {
				inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS); //Ukrycie klawiatury
				if(mapSearchBox.getText().toString().trim().length()<=0){								//Zabezpieczenie przed pozostawieniem
																										//pustego Search Box'a
					Toast.makeText(MapShow.this, "Popraw puste pole!", Toast.LENGTH_SHORT).show();		//Nie wpisano lokalizacji do wyszukiwania
																										//wiec wyswietl komunikat
				}
				else{
					search = mapSearchBox.getText().toString();  //Pobranie lokalizacji wprowadzonej do Search Box'a 
					try {
						Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault()); // Utworzenie obiektu do konwersji
						List<Address> nazwy = geocoder.getFromLocationName(search, 1);				// polo¿enia na wspolrzedne oraz
																										// listy przechowujacej koordynanty
						if (nazwy.size() == 0) {														// otrzymane z wporwadzonej nazwy
							System.out.println("Error");	
						}

						address = nazwy.get(0);														//Pobranie wyszukiwanie adresu
						LatLng searchedLocation = new LatLng(address.getLatitude(), address.getLongitude()); //Stworzenie zmiennej
																											 //przechowujacej lokalizacje
						googleMap.moveCamera(CameraUpdateFactory.newLatLng(searchedLocation));			//Przesuniecie widoku mapy na
																										//wyszukiwana lokalizacje
						googleMap.animateCamera(CameraUpdateFactory.zoomTo(16));						//Przybli¿enie ustawionej mapy
						} 
					catch (Exception e) {																//Obsluga wyjatkow
						Log.e("", "Wystapil blad ! ", e);
					}
				}
			}
		});

		mapViewType.setOnClickListener(new OnClickListener() {	//Obsluga zdarzenia przycisku
			int mapFlag=0;										//Parametr pomocniczy
			@Override
			public void onClick(View v) {
				if(mapFlag == 0){										//Sprawdzenie wartosci mapFlag
					googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); //Jezeli wartosc 0 ustawia sie
					mapFlag = 1;										//widok z satelity i mapFlag
				}														//ustawiany jest na 1
				else if(mapFlag == 1){									//Sprawdzenie wartosci mapFlag
					googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);	//Jezeli wartosc 1 ustawia sie
					mapFlag = 0;										//widok standardowy i mapFlag
				}														//ustawiany jest na 0
			}
		});

		googleMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();

		// Pobranie aktualnej lokalizacji

		//			Jezeli aktualna pozycja  jest pusta pobierz ostatnia zapamietana lokalizacje
		if(LocationMessage.locationMapView == null  ){

			Toast.makeText(MapShow.this, "Poczekaj na pobranie aktualnej lokalizacji.",Toast.LENGTH_SHORT).show();
		}
		else{
			currentLocationMapShow = new LatLng(LocationMessage.locationMapView.getLatitude(),LocationMessage.locationMapView.getLongitude());
		}

		//Przesuniecie na aktualna lokalizacje
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocationMapShow));
		// Zbli¿enie wyswietlanej mapy
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
		googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {	//Obsluga eventu dlugiego
			@Override																	//przytrzymania mapy palcem
			public void onMapLongClick(LatLng point) {		
				marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title("New Marker");	//Marker przetrzymujacy 
				markerLatitude = point.latitude;																		//wskzana pozycje i przypisanie			
				markerLongitude = point.longitude;																		//wspolrzednych
				googleMap.addMarker(marker);																			//Dodanie markera na mapie
				mapShowed = "true";																						//Parametr pomocniczy
				Toast.makeText(MapShow.this, "Wskazano lokalizacje!",Toast.LENGTH_LONG ).show();						//komunikat potwierdzajacy
				final Dialog dialog = new Dialog(MapShow.this);															//Stworzenie dialogu
				dialog.setContentView(R.layout.dialog);																	//zatwierdzajacego wskazana	
				dialog.setTitle("Wybrano lokalizacje");																	//lokalizacje
				TextView text = (TextView) dialog.findViewById(R.id.text);												//Zapytanie o poprawnosc
				text.setText("Lokalizacja docelowa jest poprawna?");													//wskazanej lokalizacji					
				Button buttonYes = (Button) dialog.findViewById(R.id.dialogButtonYes);									//Przygotowanie buttonów
				Button buttonNo = (Button) dialog.findViewById(R.id.dialogButtonNo);
				dialog.show();																							//Wyswietlenie dialogu
				buttonNo.setOnClickListener(new OnClickListener() {														//Listener przycisku nie
					@Override
					public void onClick(View v) {
						googleMap.clear();																				//Skasowanie markera z mapy
						dialog.dismiss();																				//Ukrycie okna dialogowego
						Toast.makeText(MapShow.this, "Zmien lokalizacje",Toast.LENGTH_LONG ).show();					//Komunikat o zmianie lokalizacji
					}
				});
				buttonYes.setOnClickListener(new OnClickListener() {													//Listener przycisku tak
					@Override	
					public void onClick(View v) {
						dialog.dismiss();																				//Ukrycie okna dialogowego
						finish();																						//Zakoñczenie aktywnosci
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
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}
	}
}