package mango.jms.stream;

import mango.jms.stream.domain.Request;
import mango.jms.stream.utils.DomainMessageUtils;
import org.springframework.core.task.TaskExecutor;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageListener;


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
        Request req = null;
        try {
            req = DomainMessageUtils.fromRequestMessage(request);
            taskExecutor.execute(new JmsFileSenderRunnable(req, request
                    .getJMSReplyTo()));
        } catch (Exception e1) {
            e1.printStackTrace();
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
                e.printStackTrace();
            }
        }

    }
}
