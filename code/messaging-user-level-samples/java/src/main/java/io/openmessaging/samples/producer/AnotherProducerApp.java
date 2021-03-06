/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.openmessaging.samples.producer;

import io.openmessaging.MessagingAccessPoint;
import io.openmessaging.MessagingAccessPointFactory;
import io.openmessaging.Producer;
import io.openmessaging.SendResult;
import java.nio.charset.Charset;

public class AnotherProducerApp {
    public static void main(String[] args) {
        final MessagingAccessPoint messagingAccessPoint = MessagingAccessPointFactory
            .getMessagingAccessPoint("openmessaging:rocketmq://IP1:10911,IP2:10900/namespace");

        final Producer producer = messagingAccessPoint.createProducer();

        messagingAccessPoint.startup();
        System.out.println("MessagingAccessPoint startup OK");

        producer.startup();
        System.out.println("Producer startup OK");

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                producer.shutdown();
                messagingAccessPoint.shutdown();
            }
        }));

        SendResult result = producer.send(producer.createBytesMessageToTopic(
            "HELLO_TOPIC1", "HELLO_BODY1".getBytes(Charset.forName("UTF-8"))));

        System.out.println("Send first message to topic OK, message id is: " + result.messageId());

        producer.send(producer.createBytesMessageToTopic(
            "HELLO_TOPIC2", "HELLO_BODY2".getBytes(Charset.forName("UTF-8")))
            .putProperties("KEY1", 100)
            .putProperties("KEY2", 200L)
            .putProperties("KEY3", 3.14)
            .putProperties("KEY4", "value4")
        );

        System.out.println("Send second message to topic OK");

        producer.send(producer.createBytesMessageToQueue(
            "HELLO_QUEUE", "HELLO_BODY".getBytes(Charset.forName("UTF-8"))));

        System.out.println("send third message to queue OK");
    }
}