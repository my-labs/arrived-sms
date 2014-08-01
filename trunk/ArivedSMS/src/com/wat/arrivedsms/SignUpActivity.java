package com.wat.arrivedsms;

import com.wat.arrivedsms.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends Activity {

	EditText editTextUserName, editTextPassword;
	Button buttonAccCreate;
	
	LoginDataBaseAdapter loginDataBaseAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		loginDataBaseAdapter= new LoginDataBaseAdapter(this);
		loginDataBaseAdapter=loginDataBaseAdapter.open();
		
		editTextUserName=(EditText)findViewById(R.id.editUserNameLogin);
		editTextPassword=(EditText)findViewById(R.id.editPasswordLogin);
		
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
