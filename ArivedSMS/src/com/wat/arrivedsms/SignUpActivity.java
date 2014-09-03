package com.wat.arrivedsms;

import java.util.ArrayList;

import com.wat.arrivedsms.R;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity  {

	EditText editTextUserName, editTextPassword;
	Button buttonAccCreate;
	LoginDataBaseAdapter loginDataBaseAdapter;
	GestureLibrary bibliotekaGestow;
	GestureOverlayView gestView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		loginDataBaseAdapter= new LoginDataBaseAdapter(this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();

		editTextUserName=(EditText)findViewById(R.id.editUserNameLogin);
		editTextUserName.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		editTextPassword=(EditText)findViewById(R.id.editPasswordLogin);
		editTextPassword.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		buttonAccCreate=(Button)findViewById(R.id.buttonReg);
		buttonAccCreate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String userName=editTextUserName.getText().toString();
				String password=editTextPassword.getText().toString();

				if(userName.equals("")||password.equals(""))
				{
					Toast.makeText(getApplicationContext(), "Zosta³y puste pola!", Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					loginDataBaseAdapter.insertEntry(userName, password);
					Toast.makeText(getApplicationContext(), "Account is created", Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

}
