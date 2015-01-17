package com.example.golergka.playimtest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONArray;
import org.json.JSONException;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Getting the tilt data
        final SensorManager sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        final float[] acceleration  = new float[3];

        sensorManager.registerListener(
                new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        System.arraycopy(event.values, 0, acceleration, 0, 3);
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
                },
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
        );

        // Creating the web view
        final WebView mainWebView = (WebView) findViewById(R.id.webView);
        mainWebView.getSettings().setJavaScriptEnabled(true);
        mainWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mainWebView.loadUrl("http://golergka.github.io/tiltAppServer/");

        // Setting up the API
        final Object orientationProvider = new Object() {
            @JavascriptInterface
            public String getOrientation() throws JSONException {
                return new JSONArray(acceleration).toString();
            }
        };
        mainWebView.addJavascriptInterface(orientationProvider, "OrientationProvider");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
