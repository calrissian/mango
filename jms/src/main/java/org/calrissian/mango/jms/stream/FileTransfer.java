package org.calrissian.mango.jms.stream;


import java.io.InputStream;

public interface FileTransfer {


    public InputStream receiveStream(String url) throws JmsFileTransferException;

}
