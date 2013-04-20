package cic.myapps.signin.v1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;



/**
	Copyright (c) 2009 
	Author: Stefan Klumpp <stefan.klumpp@gmail.com>
	Web: http://stefanklumpp.com

	Licensed under the Apache License, Version 2.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain
	a copy of the License at
		http://www.apache.org/licenses/LICENSE-2.0
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
 **/

public class HttpClient extends AsyncTask<String, Void, JSONObject> {
	private static final String TAG = "HttpClient";
	
	private Activity activitySignin;
	
	public HttpClient(Activity activitySignin) {
		this.activitySignin = activitySignin;
	}


	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 * 
		 * (c) public domain: http://senior.ceng.metu.edu.tr/2009/praeda/2009/01/11/a-simple-restful-client-at-android/
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();		
		
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {				
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}


	/**
	 * Get json object from remote db
	 * 
	 */
	@Override
	protected JSONObject doInBackground(String... urls) {
		
		
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpPost httpPostRequest = new HttpPost(urls[0]);
			
			// Start from here
			// Set HTTP parameters
			httpPostRequest.setHeader("Accept", "application/json");
			httpPostRequest.setHeader("Content-type", "application/json");			

			long t = System.currentTimeMillis();
			HttpResponse response = (HttpResponse) httpclient.execute(httpPostRequest);
			Log.i(TAG, "HTTPResponse received in [" + (System.currentTimeMillis()-t) + "ms]");

			// Get hold of the response entity (-> the data):
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				// Read the content stream
				InputStream instream = entity.getContent();

				// convert content stream to a String
				String resultString= convertStreamToString(instream);
				
				instream.close();
				//resultString = resultString.substring(1,resultString.length()-1); // remove wrapping "[" and "]"				
								
				// Transform the String into a JSONObject
				JSONObject jsonObjRecv = new JSONObject(resultString);
							
				// Raw DEBUG output of our received JSON object:
				Log.i(TAG,"<JSONObject>\n"+jsonObjRecv.toString()+"\n</JSONObject>");

				return jsonObjRecv;
			} 

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected void onPostExecute(JSONObject jsonObjRecv) {
						
		/*
		 *  From here on do whatever you want with your JSONObject, e.g.
		 *  1) Get the value for a key: jsonObjRecv.get("key");
		 *  2) Get a nested JSONObject: jsonObjRecv.getJSONObject("key")
		 *  3) Get a nested JSONArray: jsonObjRecv.getJSONArray("key") 
		 */				
		
		String firstname = null;
		String lastname = null;
		try {
			firstname = jsonObjRecv.getString("firstname");
			lastname = jsonObjRecv.getString("lastname");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final EditText editTextFirstName = 
			(EditText) this.activitySignin.findViewById(R.id.editTextFirstName);
		editTextFirstName.setText(firstname);
		final EditText editTextLastName = 
			(EditText) this.activitySignin.findViewById(R.id.editTextLastName);
		editTextLastName.setText(lastname);
	}

}
