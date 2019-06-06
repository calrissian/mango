/*
 * Copyright (C) 2019 The Calrissian Authors
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
package org.calrissian.mango.types.encoders;

public class AliasConstants {
    private AliasConstants() { }

    //Java types
    public static final String BOOLEAN_ALIAS = "boolean";
    public static final String BYTE_ALIAS = "byte";
    public static final String DATE_ALIAS = "date";
    public static final String INSTANT_ALIAS = "instant";
    public static final String DOUBLE_ALIAS = "double";
    public static final String FLOAT_ALIAS = "float";
    public static final String INTEGER_ALIAS = "integer";
    public static final String LONG_ALIAS = "long";
    public static final String STRING_ALIAS = "string";
    public static final String URI_ALIAS = "uri";
    public static final String BIGINTEGER_ALIAS = "bigint";
    public static final String BIGDECIMAL_ALIAS = "bigdec";
    public static final String INET4_ALIAS = "inet4";
    public static final String INET6_ALIAS = "inet6";

    //Mango types
    public static final String ENTITY_IDENTIFIER_ALIAS = "entityId";
    public static final String EVENT_IDENTIFIER_ALIAS = "eventId";
    public static final String IPV4_ALIAS = "ipv4";
    public static final String IPV6_ALIAS = "ipv6";

    //Guava types
    public static final String UNSIGNEDINTEGER_ALIAS = "uint";
    public static final String UNSIGNEDLONG_ALIAS = "ulong";
}
