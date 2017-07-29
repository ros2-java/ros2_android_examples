/* Copyright 2017 Esteve Fernandez <esteve@apache.org>
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

import java.util.concurrent.TimeUnit;

import org.ros2.rcljava.node.BaseComposableNode;
import org.ros2.rcljava.publisher.Publisher;
import org.ros2.rcljava.timer.WallTimer;

public class TalkerNode extends BaseComposableNode {
  private final String topic;

  private Publisher<std_msgs.msg.String> publisher;

  private int count;

  private WallTimer timer;

  public TalkerNode(final String name, final String topic) {
    super(name);
    this.topic = topic;
    this.publisher = this.node.<std_msgs.msg.String>createPublisher(
        std_msgs.msg.String.class, this.topic);
  }

  public void start() {
    if (this.timer != null) {
      this.timer.cancel();
    }
    this.count = 0;
    this.timer = node.createTimer(500, TimeUnit.MILLISECONDS, this ::onTimer);
  }

  private void onTimer() {
    std_msgs.msg.String msg = new std_msgs.msg.String();
    msg.setData("Hello ROS2 from Android: " + this.count);
    this.count++;
    this.publisher.publish(msg);
  }

  public void stop() {
    if (this.timer != null) {
      this.timer.cancel();
    }
  }
}
