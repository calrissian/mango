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

import org.calrissian.mango.jms.connectionfac.decorator.ConnectionDecorator;

import javax.jms.*;

/**
 * Class SingleQueueConnection
 * Date: Nov 27, 2011
 * Time: 4:43:19 PM
 */
@Deprecated
class SingleQueueConnection extends ConnectionDecorator {

    private String baseQueue;

    public SingleQueueConnection(Connection connection, String baseQueue) {
        super(connection);
        this.baseQueue = baseQueue;
    }

    @Override
    public Session createSession(boolean transacted, int ackMode) throws JMSException {
        return new SingleQueueSession(super.createSession(transacted, ackMode), baseQueue);
    }

    @Override
    public ConnectionConsumer createConnectionConsumer(Destination destination, String messageSelector, ServerSessionPool serverSessionPool, int maxMessages) throws JMSException {
        if (destination instanceof Queue)
            throw new UnsupportedOperationException();

        return super.createConnectionConsumer(destination, messageSelector, serverSessionPool, maxMessages);
    }
}
