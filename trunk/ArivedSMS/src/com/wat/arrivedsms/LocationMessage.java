package com.wat.arrivedsms;



import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Browser.SearchColumns;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
//import android.support.v4.widget.SearchViewCompat;
import android.telephony.SmsManager;
import android.util.Log;
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
	TextView result;
	public static Address adresWybrany;
	public static Location locationMapView;
	public static TextView resultText;
	SearchView searchContact;
	LocationManager lm;


	String numberM;
	String text="Dotar≥em!";
	Double latDefin = 0.0;
	Double longDefin = 0.0;
	double tmp = 0.0;
	Integer count;

	Button btnSignIn,btnSignUp;
	ImageView kropka_zielona,kropka_czerwona;
	LoginDataBase loginDataBaseAdapter;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		count=0;

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		LocationListener ll = new mylocationlistener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);

		kropka_zielona = (ImageView) findViewById(R.id.kropka_zielona);
		kropka_czerwona = (ImageView) findViewById(R.id.kropka_czerwona);

		//Ikona informacji pobrania aktualnego sygnalu gps
		
		if(locationMapView != null){

			kropka_czerwona.setVisibility(View.INVISIBLE);
			kropka_zielona.setVisibility(View.VISIBLE);
		}

		input = (EditText)findViewById(R.id.number);
		result = (TextView)findViewById(R.id.searchViewResult);
		resultText = (TextView)findViewById(R.id.mapResult);
		Search();
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
				}
			}
		});
	}
	private class mylocationlistener implements LocationListener {
		@Override
		public void onLocationChanged(Location  location) {
			if(location != null){
				locationMapView = location;
				//Zmiany statusu GPS
				if(locationMapView != null){							//Sprawdzenie czy zmienna przechowuje lokalizacje
					kropka_czerwona.setVisibility(View.INVISIBLE);		//Ukrycie ikony braku sygnalu satelitarnego
					kropka_zielona.setVisibility(View.VISIBLE);			//Wystwietlenie ikony poprawnego sygnalu GPS
				}
				else{
					kropka_czerwona.setVisibility(View.VISIBLE);		//Wystwietlenie ikony braku sygnalu satelitarnego
					kropka_zielona.setVisibility(View.INVISIBLE);		//Ukrycie ikony poprawnego sygnalu GPS
				}	
			}
			if(count==0){															//Sprawdzenie wartosci parametru pomocniczego
				double distance=distancee(latDefin,longDefin,location.getLatitude(),location.getLongitude());//Obliczenie odleglosci miedzy
																					//aktualnym polozeniem, a lokalizacja wskazana		
				if(distance< 50){													//Jezeli obliczona odleglosc wynosi mniej niz 50m
					count=1;														//Ustawienie parametru pomocniczego
					try {																
						SmsManager smsManager = SmsManager.getDefault();			//Wykorzystanie manager'a sms 
						smsManager.sendTextMessage(numberM, null, text, null, null);//Wyslanie zdefiniowanej wiadomosci pod odpowiedni numer
						Toast.makeText(getApplicationContext(), "SMS wyslany poprawnie!",	//Komunikat o poprawnym wyslaniu sms
								Toast.LENGTH_SHORT).show();
					} catch (Exception e) {											//Wychwycenie wyjatkow w dzialaniu aplikacji
						Toast.makeText(getApplicationContext(),						//Wyswietlenie komunikatu o nie wyslaniu wiadomosci sms
								"SMS nie zostal wyslany. Blad!",Toast.LENGTH_SHORT).show();
						e.printStackTrace();										//Zapisanie info o bledzie do strumienia bledow 
						count=0;													//systemowych
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
		double th = lon1 - lon2;
		double distance = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(th));
		distance = Math.acos(distance);
		distance = rad2deg(distance);
		distance = distance* 60 * 1.1515;
		distance = distance * 1609.344;
		return distance;

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
			showDestinationLocationName(latDefin, longDefin);
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

	public void showDestinationLocationName(double latitude,double longitude){

		try {
			Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
			List<Address> results = geocoder.getFromLocation(latitude, longitude, 3);


			if (results.size() == 0) {
				System.out.println("Error");	
			}

			adresWybrany = results.get(0);

		} catch (Exception e) {
			Log.e("", "Something went wrong: ", e);

		}	

		if(adresWybrany.getThoroughfare() == null){
			resultText.setText(""+adresWybrany.getLocality());
		}
		else if(adresWybrany.getLocality()==null){
			resultText.setText(""+adresWybrany.getThoroughfare());
		}
		else{
			resultText.setText(""+adresWybrany.getLocality()+","+adresWybrany.getThoroughfare());
		}






	}


	private void Search() {
		SearchManager searchMgr = (SearchManager) getSystemService(Context.SEARCH_SERVICE);	//Stworzenie managera wyszukiwania
		searchContact = (SearchView) findViewById(R.id.searchContact);			//Stworzenie elementu(SearchView) layoutu
		SearchableInfo searchInf = searchMgr.getSearchableInfo(getComponentName());			//Stworzenie zmiennej searchedInfo 																					
		searchContact.setSearchableInfo(searchInf);											//Ustawienie wartosci searchContact na wczesniej
																							//wyszukane infomracje

	}

	private String getNameFromContact(Intent intent) {												//Metoda pobierajaca nazwe przypisana do nr.tel
		Cursor phoneNrCsr = getContentResolver().query(intent.getData(), null, null, null, null); 	//Przygotowanie kursora

		phoneNrCsr.moveToFirst();																	//Przesuniecie wczesniej utworzonego kursora
		int contactId = phoneNrCsr.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);			//Pobranie id dla konkretnego kontaktu
		String contactName = phoneNrCsr.getString(contactId);										//Pobranie nazwy kontaktu na podstawie id
		phoneNrCsr.close();																			//Zwolnienie wykorzystywanego kursora
		return contactName;																			//ZwrÛcenie nazwy kontaktu
	}




	@Override
	protected void onNewIntent(Intent intent) {
		if (ContactsContract.Intents.SEARCH_SUGGESTION_CLICKED.equals(intent.getAction())) { //Obsluga zdarzenia polegajacego na wybraniu
																							 //Obsluga klikniecia podpowiadanego kontaktu										
			String name = getNameFromContact(intent);										 //Pobranie nazwy kontaktu dla danego intentu
			result.setText(name);															 //Ustawienie odpowiedniej nazwy do wyswietlenia
			searchContact.setQuery(name, false);
			Cursor peopleList = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, //Przygotowanie kursora i pobranie rekordu
								null, "display_name"+"='"+name+"'", null, null);			 //z wyszukiwana nazwa tak jak String Name
			peopleList.moveToFirst();  														 //Przesuniecie kursora na pierwsza pozycje
			String contactIdentyficator = peopleList.getString(peopleList.getColumnIndex(ContactsContract.Contacts._ID));// Przypisanie do
																							 //zmiennej identyfikatora w oparciu o
																							 //wczesniejszy kursor

			Cursor phoneNr = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,//Stworzenie cursora i 
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactIdentyficator,null, null);//pobranie danych
			while (phoneNr.moveToNext()) 													 // pasujacych do id, sprawdzenie czy to ostatni
			{																				 // wczytany wiersz
				numberM= phoneNr.getString(phoneNr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));//Przypisanie do zmiennej
			}																				 //globalnej wskazanego numeru telefonu
			phoneNr.close(); 																 //Zwolnienie kursora

		} else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {						 //Obluga bezposredniego wprowadzenia nr. telefonu
																							 //Obsluga bezposredniego podania numeru docelowego
			String queryNumber = intent.getStringExtra(SearchManager.QUERY);				 //Pobranie wartosci wporawdzonej prez uzytkownika
			 if(!queryNumber.matches("^([0-9]{9})|(([0-9]{3}-){2}[0-9]{3})$")){
				 Toast.makeText(getApplicationContext(), "Podano zly numer telefonu!",Toast.LENGTH_LONG).show();
				 searchContact.setQuery("", false);
				 searchContact.clearFocus();
			 }
			 else{			
				 numberM = ""+queryNumber ;													 //Przypisanie numeru do zmiennej globalnej	
				 result.setText( queryNumber );												 //Wyswietlenie odpowiedniego numeru telefonu
			 }
		}
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    String res = result.getText().toString();
	    outState.putString("listItems", res);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    String item = savedInstanceState.getString("listItems");
	    result.setText(item);
	    numberM = result.getText().toString();
	    super.onRestoreInstanceState(savedInstanceState);
	}

}



