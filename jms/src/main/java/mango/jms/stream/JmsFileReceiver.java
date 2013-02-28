package mango.jms.stream;

import mango.jms.stream.domain.Request;
import java.io.InputStream;

public class JmsFileReceiver extends AbstractJmsFileTransferSupport implements
        FileTransfer {

    public InputStream receiveStream(String url) throws JmsFileTransferException {
        final Request req = new Request(url);
        return receiveStream(req);
    }
}
