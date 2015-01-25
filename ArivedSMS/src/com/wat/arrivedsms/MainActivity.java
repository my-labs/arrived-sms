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
import android.text.method.PasswordTransformationMethod;
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
	LoginDataBase pomocnikBazyDanych;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		setTitle("Login");

		pomocnikBazyDanych= new LoginDataBase(this);
		pomocnikBazyDanych=pomocnikBazyDanych.open();

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
		userPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

		Button loginButton=(Button)findViewById(R.id.buttonLogin);				// Stworzenie przycisku logowania
		loginButton.setOnClickListener(new OnClickListener() {					//Listener event'ów przycisku logowania

			@Override
			public void onClick(View v) {

				String userLogin=userName.getText().toString();					//Pobranie wproawdzonego loginu
				String password=userPass.getText().toString();					//Pobranie wprowadzonego hasla uzytkownika

				if(userLogin.equals("")||password.equals("")){		//
					Toast.makeText(getApplicationContext(), "Zosta³y puste pola!", Toast.LENGTH_LONG).show(); //Komunikat o pustych polach
					return;														//Opuszczenie metody
				}
				else{
					storedPassword=pomocnikBazyDanych.getOneEntry(userLogin);//Pobranie hasla z bazy danych odpowiadajacego podanemu loginowi
				}

				if(password.equals(storedPassword)){							//Porownanie hasla wprowadzonego i pobranego z bazy danych
					Toast.makeText(MainActivity.this, "Zalogowano pomyslnie!",Toast.LENGTH_LONG ).show();//Komunikat o poprawnym zalogowaniu
					Intent i = new Intent(getApplicationContext(), LocationMessage.class); //Przygotowanie kolejnej aktywnosci
					startActivity(i);											//Uruchomienie nowej aktywnosci
				}
				else
					Toast.makeText(MainActivity.this, "Login lub has³o jest z³e!",Toast.LENGTH_LONG ).show();//Komunikat o blednym hasle

			}
		});


		Button buttonRegg=(Button)findViewById(R.id.buttonReg);								//Stworzenie przycisku rejestracji konta	
		buttonRegg.setOnClickListener(new OnClickListener() {								//Przygotowanie listener'a klikniecia
			@Override
			public void onClick(View v) {
				String userNameReg=userName.getText().toString();							//Pobranie loginu wprowadzonego do rejestracji
				String passwordReg=userPass.getText().toString();							//Pobranie hasla wprowadzonego do rejestracji
				boolean checkAvalible=pomocnikBazyDanych.verification(userNameReg);		//Operacja sprawdzenia dostepnosci
																							//wprowadzonego loginu
				if(userNameReg.equals("")||passwordReg.equals("")						//Zabezpieczenie przed pustymi polami:login i haslo
						||userNameReg.trim().length() == 0||passwordReg.trim().length() == 0)
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
					pomocnikBazyDanych.insertEntry(userNameReg, passwordReg);				//Jezeli login dostepny to wpisanie rekordu do
					Toast.makeText(getApplicationContext(), "Stworzono konto uzytkownika", Toast.LENGTH_LONG).show(); //bazy danych oraz
				}																			// wyswietlenie komunikatu
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
