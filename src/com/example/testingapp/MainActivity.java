package com.example.testingapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


class GetTask extends AsyncTask<String,String,String>
{
    protected String doInBackground(String... uri) {

		
    	System.out.println("Starting request now");
    	HttpClient GET = new DefaultHttpClient();
    	String line = null;
    	StringBuilder total = new StringBuilder();
    	
    	try{
    		
    		HttpResponse response = GET.execute(new HttpGet(uri[0]));
    		BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent())); 		

    		while ((line = r.readLine()) != null) 
    		{
    			
    		   total.append(line);
    		   System.out.println(line);
    		   
    		}
    		
    		System.out.println("Success!");
    	}
    	catch (IOException e) {

    		System.out.println("Errror occurred : "+e);
    		
        }
    	
    	line = total.toString();
    	
    	return line;
    	
    }
	
    @Override
    protected void onPostExecute(String l) {
        
        System.out.println("Inside onPostExeciute");
        System.out.println("For Some reason, line is not availabel here");
    
    }
	
}
public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "com.example.testingapp.MESSAGE";
	Button mButton;
	EditText mEdit;
	TextView mView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	    mButton = (Button)findViewById(R.id.button);
	    mEdit   = (EditText)findViewById(R.id.edittext);
	    mView	=	(TextView)findViewById(R.id.viewBody);
	   
	    
	    mButton.setOnClickListener(
	        new View.OnClickListener()
	        {
	            public void onClick(View view)
	            {
	            
	            	System.out.println("Beginning Request");
	            	
	            	GetTask get = new GetTask();
	            	
	            	String x = mEdit.getText().toString().trim();
	            	
	            	try
	            	{
	            		String data = get.execute(x).get();
	            		System.out.println("Inside main thread again!");
	            		
	            		System.out.println("Data received = "+data);
	            		mView.setMovementMethod(new ScrollingMovementMethod());	
	            		mView.setText(data);
	            		

	            	}
	            	
	            	catch(Exception E)
	            	{
	            		System.out.println("error caught!");
	            	}
	                
	            }
	        });

	}
	
	public void startNewActivity(View view)
	{
		Intent intent = new Intent(this ,ViewGetData.class);
		String message = mView.getText().toString().trim();
		intent.putExtra(EXTRA_MESSAGE, message);
		startActivity(intent);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
