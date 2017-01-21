package com.example.aaron.leveler;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor tilt;
    private TextView top;
    private TextView bottom;
    private TextView left;
    private TextView right;
    private TextView levelLabel;
    private int screenRotation;
    private SurfaceView surfaceView;
    private boolean mIsHorizontal, mIsVertical,mIsFlat;
    private ImageView levelImage;
    private SeekBar levelDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        top     = (TextView)findViewById(R.id.topText);
        bottom  = (TextView)findViewById(R.id.bottomText);
        left    = (TextView)findViewById(R.id.leftText);
        right   = (TextView)findViewById(R.id.rightText);
        levelLabel = (TextView)findViewById(R.id.level_label);
        levelDisplay =(SeekBar)findViewById(R.id.seek);
        levelDisplay.setProgressDrawable(new ColorDrawable(Color.argb(0,0,0,255)));
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mIsHorizontal = false;
        mIsVertical = false;
        mIsFlat = true;
        tilt = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //levelImage = (ImageView)findViewById(R.id.level_image);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        screenRotation =((WindowManager)
                getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        // 3 values, one for each axis.
        float xTilt = event.values[0];
        float yTilt = event.values[1];
        float zTilt = event.values[2];
        // Do something with this sensor value.
        showTilt(xTilt, yTilt, zTilt);
        Log.d("Sensor Changed", String.format("x = %8.6f,  y = %8.6f,  z = %8.6f",xTilt, yTilt, zTilt));
    }

    public void showTilt(float hTilt, float vTilt,float zTilt) {
        int red = Math.round(Math.abs(vTilt) * 25.5f);
        int blue = Math.round(Math.abs(hTilt) * 25.5f);
            top.setTextColor(Color.BLACK);
            bottom.setTextColor(Color.BLACK);
            left.setTextColor(Color.BLACK);
            right.setTextColor(Color.BLACK);
        levelLabel.setTextColor(Color.BLACK);
        if (mIsFlat) {
            levelDisplay.setProgress(((int)-hTilt*10)+50);
            if (red <= 20 && blue <= 20) {
                levelLabel.setText("LEVEL!");
                //levelImage.setVisibility(View.VISIBLE);
                //surfaceView.setBackgroundColor(Color.rgb(red, 0, blue));
            } else {
                //surfaceView.setBackgroundColor(Color.rgb(red, 0, blue));
                levelLabel.setText("");
                //levelImage.setVisibility(View.INVISIBLE);
            }
        } else if (mIsHorizontal) {
            levelDisplay.setProgress(((int) -vTilt * 10) + 50);
            if (vTilt < 0.5 && vTilt>-0.5) {
                levelLabel.setText("LEVEL!");
                //levelImage.setVisibility(View.VISIBLE);
                //surfaceView.setBackgroundColor(Color.rgb(red, 0, blue));
            } else {
                //surfaceView.setBackgroundColor(Color.rgb(red, 0, blue));
                levelLabel.setText("");
                //levelImage.setVisibility(View.INVISIBLE);
            }
        } else if (mIsVertical) {
            levelDisplay.setProgress(((int)-zTilt*10)+50);
            if (zTilt < 0.5 && zTilt > -0.5) {
                levelLabel.setText("LEVEL!");
                //levelImage.setVisibility(View.VISIBLE);
                //surfaceView.setBackgroundColor(Color.rgb(red, 0, blue));
            }
            else {
                //surfaceView.setBackgroundColor(Color.rgb(red, 0, blue));
                levelLabel.setText("");
                //levelImage.setVisibility(View.INVISIBLE);
            }
        }
        if (mIsFlat) {
            if (screenRotation == 0 || screenRotation == 2) {
                top.setText("" + vTilt);
                bottom.setText("" + -vTilt);
                left.setText("" + -hTilt);
                right.setText("" + hTilt);
            } else if (screenRotation == 1) {
                left.setText("" /*+ vTilt*/);
                right.setText("" /*+ -vTilt*/);
                top.setText("" /*+hTilt*/);
                bottom.setText("" /*+ -hTilt*/);
            } else {
                left.setText("" /*+-vTilt*/);
                right.setText("" /*+ vTilt*/);
                top.setText("" /*+ -hTilt*/);
                bottom.setText("" /*+ hTilt*/);
            }
        } else if (mIsHorizontal) {
            if (screenRotation == 0 || screenRotation == 2) {
                top.setText("");
                bottom.setText("");
                left.setText(""/* + -hTilt*/);
                right.setText(""/* + hTilt*/);
            } else if (screenRotation == 1) {
                left.setText("" /*+ vTilt*/);
                right.setText("" /*+ -vTilt*/);
                top.setText("");
                bottom.setText("");
            } else {
                left.setText(""/* + -vTilt*/);
                right.setText(""/* + vTilt*/);
                top.setText("");
                bottom.setText("");
            }
        } else if (mIsVertical) {
            if (screenRotation == 0 || screenRotation == 2) {
                top.setText("" /*+ (10-vTilt)*/);
                bottom.setText(""/* + (-vTilt+10)*/);
                left.setText("");
                right.setText("");
            } else if (screenRotation == 1) {
                left.setText("");
                right.setText("");
                top.setText("" /*+ (10-hTilt)*/);
                bottom.setText(""/* + (-hTilt+10)*/);
            } else {
                left.setText("");
                right.setText("");
                top.setText("" /*+ (-hTilt-10)*/);
                bottom.setText(""/* + (10+hTilt)*/);
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, tilt,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id) {
            case R.id.action_settings:
                //// TODO: 9/23/2015
                break;
            case R.id.crossed_arrow:
                mIsFlat = true;
                mIsHorizontal = false;
                mIsVertical = false;
                return true;
            case R.id.horizontal_arrow:
                mIsFlat = false;
                mIsHorizontal = true;
                mIsVertical = false;
                return true;
            case R.id.vertical_arrow:
                mIsFlat = false;
                mIsHorizontal = false;
                mIsVertical = true;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
