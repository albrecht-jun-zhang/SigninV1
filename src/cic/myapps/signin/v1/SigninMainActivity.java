package cic.myapps.signin.v1;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class SigninMainActivity extends Activity {

	private static final String TAG = "MainActivity";
	private static final String URL = "http://junzone.com/loginpost/UserInfoPost.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin_main);

		// JSON object to hold the information, which is sent to the server
		JSONObject jsonObjSend = new JSONObject();

		try {
			// Add key/value pairs
			jsonObjSend.put("key_1", "value_1");
			jsonObjSend.put("key_2", "value_2");

			// Add a nested JSONObject (e.g. for header information)
			JSONObject header = new JSONObject();
			header.put("deviceType","Android"); // Device type
			header.put("deviceVersion","2.0"); // Device OS version
			header.put("language", "es-es");	// Language of the Android client
			jsonObjSend.put("header", header);
			
			// Output the JSON object we're sending to Logcat:
			Log.i(TAG, jsonObjSend.toString(2));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Send the HttpPostRequest and receive a JSONObject in return
		HttpClient httpClient = new HttpClient(this);
		String[] urls = new String[1];
		urls[0] = URL;
		
		httpClient.execute(urls);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
