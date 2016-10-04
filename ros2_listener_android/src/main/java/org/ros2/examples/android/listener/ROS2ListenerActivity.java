/* Copyright 2016 Esteve Fernandez <esteve@apache.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ros2.examples.android.listener;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ros2.rcljava.Consumer;
import org.ros2.rcljava.Node;
import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.Subscription;

public class ROS2ListenerActivity extends Activity {

  private Listener listener;

  private TextView listenerView;

  private class Listener extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(final String... text) {
      Node node = RCLJava.createNode("listener");
      Subscription<std_msgs.msg.String> chatterSubscription = node.<
          std_msgs.msg.String>createSubscription(
            std_msgs.msg.String.class, "chatter",
            new Consumer<std_msgs.msg.String>() {

              @Override
              public void accept(final std_msgs.msg.String msg) {
                publishProgress("I heard: " + msg.getData());
              }
            }
      );

      while (!isCancelled() && RCLJava.ok()) {
        RCLJava.spinOnce(node);
      }

      return null;
    }

    @Override
    protected void onPostExecute(final String result) {
    }

    protected void onProgressUpdate(final String... progress) {
      listenerView.append(progress[0] + "\r\n");
    }
  }

  private static String logtag = "ROS2ListenerActivity";

  /** Called when the activity is first created. */
  @Override
  public final void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    Button buttonStart = (Button) findViewById(R.id.buttonStart);
    buttonStart.setOnClickListener(startListener);

    Button buttonStop = (Button) findViewById(R.id.buttonStop);
    buttonStop.setOnClickListener(stopListener);
    buttonStop.setEnabled(false);

    listenerView = (TextView) findViewById(R.id.listenerView);
    listenerView.setMovementMethod(new ScrollingMovementMethod());

    RCLJava.rclJavaInit();
  }

  //Create an anonymous implementation of OnClickListener
  private OnClickListener startListener = new OnClickListener() {
    public void onClick(final View view) {
      Log.d(logtag, "onClick() called - start button");
      Toast.makeText(ROS2ListenerActivity.this, "The Start button was clicked.",
        Toast.LENGTH_LONG).show();
      Log.d(logtag, "onClick() ended - start button");
      Button buttonStart = (Button) findViewById(R.id.buttonStart);
      Button buttonStop = (Button) findViewById(R.id.buttonStop);
      buttonStart.setEnabled(false);
      buttonStop.setEnabled(true);
      listener = new Listener();
      listener.execute("Hello ROS2 from Android");
    }
  };

  // Create an anonymous implementation of OnClickListener
  private OnClickListener stopListener = new OnClickListener() {
    public void onClick(final View view) {
      Log.d(logtag, "onClick() called - stop button");
      Toast.makeText(ROS2ListenerActivity.this, "The Stop button was clicked.",
        Toast.LENGTH_LONG).show();
      listener.cancel(true);
      Button buttonStart = (Button) findViewById(R.id.buttonStart);
      Button buttonStop = (Button) findViewById(R.id.buttonStop);
      buttonStart.setEnabled(true);
      buttonStop.setEnabled(false);
      Log.d(logtag, "onClick() ended - stop button");
    }
  };
}
