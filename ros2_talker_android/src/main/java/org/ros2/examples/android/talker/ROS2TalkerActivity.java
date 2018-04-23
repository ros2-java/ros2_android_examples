/* Copyright 2016-2017 Esteve Fernandez <esteve@apache.org>
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

package org.ros2.examples.android.talker;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import org.ros2.rcljava.RCLJava;

import org.ros2.android.activity.ROSActivity;

public class ROS2TalkerActivity extends ROSActivity {

  private static final String IS_WORKING = "isWorking";

  private TalkerNode talkerNode;

  private static String logtag = ROS2TalkerActivity.class.getName();

  private boolean isWorking;

  /** Called when the activity is first created. */
  @Override
  public final void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    if (savedInstanceState != null) {
      isWorking = savedInstanceState.getBoolean(IS_WORKING);
    }

    Button buttonStart = (Button)findViewById(R.id.buttonStart);
    buttonStart.setOnClickListener(startListener);

    Button buttonStop = (Button)findViewById(R.id.buttonStop);
    buttonStop.setOnClickListener(stopListener);

    RCLJava.rclJavaInit();

    talkerNode = new TalkerNode("android_talker_node", "chatter");

    changeState(isWorking);
  }

  // Create an anonymous implementation of OnClickListener
  private OnClickListener startListener = new OnClickListener() {
    public void onClick(final View view) {
      Log.d(logtag, "onClick() called - start button");
      Toast
          .makeText(ROS2TalkerActivity.this, "The Start button was clicked.",
                    Toast.LENGTH_LONG)
          .show();
      Log.d(logtag, "onClick() ended - start button");
      Button buttonStart = (Button)findViewById(R.id.buttonStart);
      Button buttonStop = (Button)findViewById(R.id.buttonStop);
      changeState(true);
    }
  };

  // Create an anonymous implementation of OnClickListener
  private OnClickListener stopListener = new OnClickListener() {
    public void onClick(final View view) {
      Log.d(logtag, "onClick() called - stop button");
      Toast
          .makeText(ROS2TalkerActivity.this, "The Stop button was clicked.",
                    Toast.LENGTH_LONG)
          .show();
      changeState(false);
      Log.d(logtag, "onClick() ended - stop button");
      talkerNode.stop();
    }
  };

  private void changeState(boolean isWorking) {
    this.isWorking = isWorking;
    Button buttonStart = (Button)findViewById(R.id.buttonStart);
    Button buttonStop = (Button)findViewById(R.id.buttonStop);
    buttonStart.setEnabled(!isWorking);
    buttonStop.setEnabled(isWorking);
    if (isWorking){
      getExecutor().addNode(talkerNode);
      talkerNode.start();
    } else {
      talkerNode.stop();
      getExecutor().removeNode(talkerNode);
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    if (outState != null) {
      outState.putBoolean(IS_WORKING, isWorking);
    }
    super.onSaveInstanceState(outState);
  }
}
