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

	private EditText input;
	private Button getInfo;

	GestureLibrary bibliotekaGestow;
	GestureOverlayView gestView;

	String number;
	String numberTmp="512238500";
	String text="Dotar�em!";
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
				Log.e("GestureSignUpActivity", "Nie wczytano biblioteki gest�w");

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
		Button loginButton=(Button)findViewById(R.id.buttonLogin);

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String userLogin=userName.getText().toString();
				String password=userPass.getText().toString();
				if(loginDataBaseAdapter.getSingleEntry(userLogin)==null){

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

					Toast.makeText(MainActivity.this, "Login lub has�o jest z�e!",Toast.LENGTH_LONG ).show();

			}
		});


		Button buttonRegg=(Button)findViewById(R.id.buttonReg);
		buttonRegg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String userNameReg=userName.getText().toString();
				String passwordReg=userPass.getText().toString();

				boolean checkAvalible=loginDataBaseAdapter.verification(userNameReg);

				if(userNameReg.equals("")||passwordReg.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Zosta�y puste pola!", Toast.LENGTH_LONG).show();


					return;
				}
				else if(checkAvalible==true){
					Toast.makeText(getApplicationContext(), "Login zaj�ty!", Toast.LENGTH_LONG).show();


					return;
				}
				else
				{
					loginDataBaseAdapter.insertEntry(userNameReg, passwordReg);
					Toast.makeText(getApplicationContext(), "Account is created", Toast.LENGTH_LONG).show();
				}
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
