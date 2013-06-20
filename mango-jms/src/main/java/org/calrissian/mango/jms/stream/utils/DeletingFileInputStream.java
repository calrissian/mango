package org.calrissian.mango.jms.stream.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DeletingFileInputStream extends FileInputStream {

    private boolean deleteOnClose = false;

    private File file;

    public DeletingFileInputStream(File file, boolean deleteOnClose)
            throws FileNotFoundException {
        super(file);
        this.file = file;
        this.deleteOnClose = deleteOnClose;
    }

    public boolean isDeleteOnClose() {
        return deleteOnClose;
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (deleteOnClose && file != null) {
            file.delete();
        }
    }

    public File getFile() {
        return file;
    }
}
