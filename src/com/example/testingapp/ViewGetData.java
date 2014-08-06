package com.example.testingapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

class writeToDBAsync extends AsyncTask<String,String,String>
{
	@Override
	protected String doInBackground(String... data)
	{
		String x = data[0];
		String response_code;
		StringBuilder builder = new StringBuilder();
		String hardcoded_api_url = "http://www.emptybrains.net/sriduth/android/api.php?data="+"hello!";
		HttpClient send_data = new DefaultHttpClient();
		//begin API request
		try
		{
			System.out.println("Beginning request");
			HttpResponse response_holder = send_data.execute(new HttpGet(hardcoded_api_url));
			BufferedReader r = new BufferedReader(new InputStreamReader(response_holder.getEntity().getContent()));
			
			while((response_code = r.readLine()) != null)
			{
				builder.append(response_code);
			}
		}
		catch(IOException E)
		{
			System.out.println("Caught some error");
		}
		
		response_code = builder.toString();
		System.out.println("Requset has ended!");
		
		return response_code;
	}
	
	@Override
	protected void onPostExecute(String s)
	{
		System.out.println("done!");
	}
}
public class ViewGetData extends Activity {
	
	TextView GetData;
	Button WriteToDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_get_data);
		// Show the Up button in the action bar.
		setupActionBar();
		//Setup UI elements
		WriteToDB = (Button)findViewById(R.id.save_data_to_DB);
		GetData = (TextView)findViewById(R.id.ViewRequestData);
		
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		GetData.setText(message.trim());
		
		WriteToDB.setOnClickListener(
				new View.OnClickListener()
				{
					public void onClick(View view)
					{
						System.out.println("Starting request");
						writeToDBAsync writer = new writeToDBAsync();
						String message = GetData.getText().toString();
						
						try
						{
							String response_code = writer.execute(message).get();
							System.out.println("API return status  = "+response_code);
						}
						catch(Exception E)//Catch generic exception cause i dont know man, its java!
						{
							System.out.println("Caught error!");
						}
					}
				});
		
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_get_data, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
