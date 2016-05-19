/*
 * Copyright (C) 2016 The Calrissian Authors
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
package org.calrissian.mango.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Overloaded {@link FileInputStream} that will optionally delete the file when the input stream is closed.
 */
public class DeletingFileInputStream extends FileInputStream {

    private final boolean deleteOnClose;
    private final File file;

    public DeletingFileInputStream(String name, boolean deleteOnClose) throws FileNotFoundException {
        this((name != null ? new File(name) : null), deleteOnClose);
    }

    public DeletingFileInputStream(File file, boolean deleteOnClose)
            throws FileNotFoundException {
        super(file);
        this.file = file;
        this.deleteOnClose = deleteOnClose;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        super.close();
        if (deleteOnClose && file != null) {
            file.delete();
        }
    }
}
