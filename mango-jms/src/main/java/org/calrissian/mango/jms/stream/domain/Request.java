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
package org.calrissian.mango.jms.stream.domain;

import java.io.Serializable;
import java.util.UUID;

public class Request implements Serializable {

    private static final long serialVersionUID = 3835642167224689315L;

    private String downloadUri;

    private String requestId;

    public Request(String downloadUri) {
        this(downloadUri, UUID.randomUUID().toString());
    }

    public Request(String downloadUrl, String requestId) {
        this.downloadUri = downloadUrl;
        this.requestId = requestId;
    }

    public String getDownloadUri() {
        return downloadUri;
    }

    public String getRequestId() {
        return requestId;
    }
}
