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
package org.calrissian.mango.jms.stream;

import org.calrissian.mango.jms.stream.domain.Request;
import org.springframework.core.task.TaskExecutor;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;

import static org.calrissian.mango.jms.stream.utils.DomainMessageUtils.fromRequestMessage;

@Deprecated
public class JmsFileSenderListener extends AbstractJmsFileTransferSupport
        implements MessageListener {

    private TaskExecutor taskExecutor;

    public TaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void onMessage(Message request) {
        Request req;
        try {
            req = fromRequestMessage(request);
            taskExecutor.execute(new JmsFileSenderRunnable(req, request
                    .getJMSReplyTo()));
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }

    }

    private class JmsFileSenderRunnable implements Runnable {

        private Request req;
        private Destination jmsReplyTo;

        JmsFileSenderRunnable(Request req, Destination jmsReplyTo) {
            this.req = req;
            this.jmsReplyTo = jmsReplyTo;
        }

        @Override
        public void run() {
            try {
                sendStream(req, jmsReplyTo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
