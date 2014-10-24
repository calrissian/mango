/*
 * Copyright (C) 2013 The Calrissian Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.calrissian.mango.jms.connectionfac;

import org.calrissian.mango.jms.connectionfac.decorator.MessageListenerDecorator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import static org.calrissian.mango.jms.connectionfac.SingleDestinationUtils.postReceiveMessage;

/**
 * Class SingleDestinationMessageListener
 * Date: Nov 30, 2011
 * Time: 5:07:28 PM
 */
@Deprecated
class SingleDestinationMessageListener extends MessageListenerDecorator {

    public SingleDestinationMessageListener(MessageListener messageListener) {
        super(messageListener);
    }

    @Override
    public void onMessage(Message message) {
        try {
            postReceiveMessage(message);
        } catch (JMSException e) {
            //not sure what to do here
        }
        super.onMessage(message);
    }
}
