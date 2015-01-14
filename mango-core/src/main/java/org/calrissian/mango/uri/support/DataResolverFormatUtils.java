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
package org.calrissian.mango.uri.support;

import com.google.common.base.Joiner;

import java.net.URI;
import java.net.URISyntaxException;

/*
* @deprecated
*/
@Deprecated
public class DataResolverFormatUtils {

    public static URI buildRequestURI(URI uri, String[] auths) throws URISyntaxException {

        return new URI(uri.toString() + DataResolverConstants.DELIM + Joiner.on(',').join(auths));
    }

    public static String extractTargetSystemFromUri(URI uri)
            throws URISyntaxException {

        return uri.getScheme();
    }

    public static String[] extractAuthsFromUri(URI uri) {

        return uri.toString().split(DataResolverConstants.DELIM)[1].split(",");
    }

    public static URI extractURIFromRequestURI(URI uri) throws URISyntaxException {

        return new URI(uri.toString().split(DataResolverConstants.DELIM)[0]);
    }
}
