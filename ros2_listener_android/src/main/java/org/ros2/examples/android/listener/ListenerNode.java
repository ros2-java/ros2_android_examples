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

package org.ros2.examples.android.listener;

import android.widget.TextView;

import org.ros2.rcljava.RCLJava;
import org.ros2.rcljava.node.AbstractComposableNode;
import org.ros2.rcljava.node.ComposableNode;
import org.ros2.rcljava.node.Node;
import org.ros2.rcljava.subscription.Subscription;

public class ListenerNode implements ComposableNode {
  private final String name;
  private final String topic;

  private final TextView listenerView;

  private Subscription<std_msgs.msg.String> subscriber;

  private Node node;

  public ListenerNode(final String name, final String topic, final TextView listenerView) {
    this.name = name;
    this.topic = topic;
    this.listenerView = listenerView;
    this.node = RCLJava.createNode(this.name);
    this.subscriber = this.node.<std_msgs.msg.String>createSubscription(std_msgs.msg.String.class, this.topic,
        msg -> this.listenerView.append("Hello ROS2 from Android" + msg.getData() + "\r\n"));
  }

  public Node getNode() {
    return node;
  }
}
