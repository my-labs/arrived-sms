package com.wat.arrivedsms;

import com.wat.arrivedsms.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText input;
	private Button getInfo;

	String number;
	String numberTmp="512238500";
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
		
//		loginDataBaseAdapter.insertEntry("admin", "admin");
		
		final EditText userName=(EditText)findViewById(R.id.editUserNameLogin);
		final EditText userPass=(EditText)findViewById(R.id.editPasswordLogin);
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
               
              	 Toast.makeText(MainActivity.this, "Login lub has³o jest z³e!",Toast.LENGTH_LONG ).show();
				
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
	                    Toast.makeText(getApplicationContext(), "Zosta³y puste pola!", Toast.LENGTH_LONG).show();
	                    

	                    return;
	            }
				else if(checkAvalible==true){
						Toast.makeText(getApplicationContext(), "Login zajêty!", Toast.LENGTH_LONG).show();

				
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
	public void onDestroy(){
		super.onDestroy();
	}
	
	
}
