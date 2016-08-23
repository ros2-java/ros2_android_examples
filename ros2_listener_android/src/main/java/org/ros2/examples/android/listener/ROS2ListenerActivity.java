package org.ros2.examples.android.listener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.os.AsyncTask;
import android.text.method.ScrollingMovementMethod;

import org.ros2.rcljava.Consumer;
import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.Node;
import org.ros2.rcljava.Subscription;

public class ROS2ListenerActivity extends Activity
{

    private Listener listener;

    private TextView listenerView;

    private class Listener extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... text) {
            Node node = RCLJava.createNode("listener");
            Subscription<std_msgs.msg.String> chatter_sub = node.<std_msgs.msg.String>createSubscription(
                std_msgs.msg.String.class, "chatter",
                new Consumer<std_msgs.msg.String>() {
                    @Override
                    public void accept(std_msgs.msg.String msg) {
                        publishProgress("I heard: " + msg.getData());
                    }
                }
            );

            while(!isCancelled() && RCLJava.ok()) {
                RCLJava.spinOnce(node);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        protected void onProgressUpdate(String... progress) {
            listenerView.append(progress[0] + "\r\n");
       }
    }

    private static String logtag = "ROS2ListenerActivity";//for use as the tag when logging

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button buttonStart = (Button)findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(startListener); // Register the onClick listener with the implementation above

        Button buttonStop = (Button)findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(stopListener); // Register the onClick listener with the implementation above
        buttonStop.setEnabled(false);

        listenerView = (TextView)findViewById(R.id.listenerView);
        listenerView.setMovementMethod(new ScrollingMovementMethod());
        RCLJava.rclJavaInit();
    }

    //Create an anonymous implementation of OnClickListener
    private OnClickListener startListener = new OnClickListener() {
        public void onClick(View v) {
            Log.d(logtag,"onClick() called - start button");
            Toast.makeText(ROS2ListenerActivity.this, "The Start button was clicked.", Toast.LENGTH_LONG).show();
            Log.d(logtag,"onClick() ended - start button");
            Button buttonStart = (Button)findViewById(R.id.buttonStart);
            Button buttonStop = (Button)findViewById(R.id.buttonStop);
            buttonStart.setEnabled(false);
            buttonStop.setEnabled(true);
            listener = new Listener();
            listener.execute("Hello ROS2 from Android");
        }
    };

    // Create an anonymous implementation of OnClickListener
    private OnClickListener stopListener = new OnClickListener() {
        public void onClick(View v) {
            Log.d(logtag,"onClick() called - stop button");
            Toast.makeText(ROS2ListenerActivity.this, "The Stop button was clicked.", Toast.LENGTH_LONG).show();
            listener.cancel(true);
            Button buttonStart = (Button)findViewById(R.id.buttonStart);
            Button buttonStop = (Button)findViewById(R.id.buttonStop);
            buttonStart.setEnabled(true);
            buttonStop.setEnabled(false);
            Log.d(logtag,"onClick() ended - stop button");
        }
    };


    @Override
    protected void onStart() {//activity is started and visible to the user
        Log.d(logtag,"onStart() called");
        super.onStart();
    }
    @Override
    protected void onResume() {//activity was resumed and is visible again
        Log.d(logtag,"onResume() called");
        super.onResume();

    }
    @Override
    protected void onPause() { //device goes to sleep or another activity appears
        Log.d(logtag,"onPause() called");//another activity is currently running (or user has pressed Home)
        super.onPause();

    }
    @Override
    protected void onStop() { //the activity is not visible anymore
        Log.d(logtag,"onStop() called");
        super.onStop();

    }
    @Override
    protected void onDestroy() {//android has killed this activity
        Log.d(logtag,"onDestroy() called");
        super.onDestroy();
    }
}
