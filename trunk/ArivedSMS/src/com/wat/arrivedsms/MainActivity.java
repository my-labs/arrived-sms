package com.wat.arrivedsms;

import java.util.ArrayList;

import com.wat.arrivedsms.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnGesturePerformedListener {

	
	//do wykasowania- nie uzywane
	private EditText input;
	private Button getInfo;

	GestureLibrary bibliotekaGestow;
	GestureOverlayView gestView;

	String number;
	String text="Dotar³em!";
	String storedPassword;
	Double latDefin=52.187;
	Double longDefin= 21.557;
	Integer count=0;
	PendingIntent pendingIntent;
	Button btnSignIn,btnSignUp;
	LoginDataBaseAdapter loginDataBaseAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		setTitle("Login");

		loginDataBaseAdapter= new LoginDataBaseAdapter(this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();

		bibliotekaGestow = GestureLibraries.fromRawResource(this,R.raw.gestures);

		if(bibliotekaGestow != null){
			if(!bibliotekaGestow.load()){
				Log.e("GestureSignUpActivity", "Nie wczytano biblioteki gestów");

			}
			else{
				gestView = (GestureOverlayView)findViewById(R.id.gestures);
				gestView.addOnGesturePerformedListener(this);
				gestView.setGestureVisible(false);
			}
		}

		final EditText userName=(EditText)findViewById(R.id.editUserNameLogin);
		userName.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		final EditText userPass=(EditText)findViewById(R.id.editPasswordLogin);
		userPass.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

		Button loginButton=(Button)findViewById(R.id.buttonLogin);				// Stworzenie przycisku logowania
		loginButton.setOnClickListener(new OnClickListener() {					//Listener event'ów przycisku logowania

			@Override
			public void onClick(View v) {

				String userLogin=userName.getText().toString();					//Pobranie wproawdzonego loginu
				String password=userPass.getText().toString();					//Pobranie wprowadzonego hasla uzytkownika

				if(loginDataBaseAdapter.getSingleEntry(userLogin)==null){		//
					storedPassword="admin";
				}
				else{
					storedPassword=loginDataBaseAdapter.getSingleEntry(userLogin);
				}

				if(password.equals(storedPassword)){
					Toast.makeText(MainActivity.this, "Login sucessfully!",Toast.LENGTH_LONG ).show();
					Intent i = new Intent(getApplicationContext(), LocationMessage.class);
					startActivity(i);
				}
				else
					Toast.makeText(MainActivity.this, "Login lub has³o jest z³e!",Toast.LENGTH_LONG ).show();

			}
		});


		Button buttonRegg=(Button)findViewById(R.id.buttonReg);								//Stworzenie przycisku rejestracji konta	
		buttonRegg.setOnClickListener(new OnClickListener() {								//Przygotowanie listener'a klikniecia
			@Override
			public void onClick(View v) {
				String userNameReg=userName.getText().toString();							//Pobranie loginu wprowadzonego do rejestracji
				String passwordReg=userPass.getText().toString();							//Pobranie hasla wprowadzonego do rejestracji
				boolean checkAvalible=loginDataBaseAdapter.verification(userNameReg);		//Operacja sprawdzenia dostepnosci
																							//wprowadzonego loginu
				if(userNameReg.equals("")||passwordReg.equals(""))							//Zabezpieczenie przed pustymi polami:login i haslo
				{
					Toast.makeText(getApplicationContext(), "Zosta³y puste pola!", Toast.LENGTH_LONG).show(); //Komunikat o pustych polach
					return;																	//Opuszczenie metody
				}
				else if(checkAvalible==true){												//Jezeli login niedostepny(wartosci true)
					Toast.makeText(getApplicationContext(), "Login zajêty!", Toast.LENGTH_LONG).show(); //Komunikat o zajetosci loginu
					return;																	//Opuszczenie metody
				}
				else
				{
					loginDataBaseAdapter.insertEntry(userNameReg, passwordReg);				//Jezeli login dostepny to wpisanie rekordu do
					Toast.makeText(getApplicationContext(), "Account is created", Toast.LENGTH_LONG).show(); //danych z wprowadzonym loginem 
				}																			//haslem oraz wyswietlenie komunikatu
			}
		});
	}

	@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		// TODO Auto-generated method stub
		ArrayList<Prediction> predictions = bibliotekaGestow.recognize(gesture);

		if(predictions.size()>0){
			Prediction prediction = predictions.get(0);
			if(prediction.score>1.0){
				Intent i = new Intent(getApplicationContext(), LocationMessage.class);
				startActivity(i);
			}
		}

	}

	@Override
	public void onDestroy(){
		super.onDestroy();
	}
}
