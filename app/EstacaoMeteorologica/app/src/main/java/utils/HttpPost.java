package utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class HttpPost extends AsyncTask<String, Void, Boolean> {

    private String url;

    public HttpPost(String urlEndpoint){
        this.url = urlEndpoint;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String json = params[0];

        try{
            URL url = new URL(this.url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            Log.i("JSON", json);
            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            //os.writeBytes(URLEncoder.encode(json, "UTF-8"));
            os.writeBytes(json);

            os.flush();
            os.close();

            Log.i("STATUS", String.valueOf(conn.getResponseCode()));
            Log.i("MSG" , conn.getResponseMessage());

            String response = new Scanner(conn.getInputStream()).next();
            Log.i("RESPONSE", response);

            conn.disconnect();
            return true;
        }
        catch (Exception ex){
            Log.e("ERROR", ex.getMessage());
            return false;
        }
    }
}
