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
package org.calrissian.mango.jms.stream.utils;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReflectionUtils {

    protected ReflectionUtils() {

    }

    public static Method findMethod(Class clazz, String name) {
        return findMethod(clazz, name, new Class[0]);
    }

    public static Method findMethod(Class clazz, String name,
                                    Class paramTypes[]) {
        if (clazz == null)
            throw new IllegalArgumentException("Class must not be null");
        if (name == null)
            throw new IllegalArgumentException("Method name must not be null");
        for (Class searchType = clazz; !(java.lang.Object.class)
                .equals(searchType)
                && searchType != null; searchType = searchType.getSuperclass()) {
            Method methods[] = searchType.isInterface() ? searchType
                    .getMethods() : searchType.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if (name.equals(method.getName())
                        && (paramTypes == null || Arrays.equals(paramTypes,
                        method.getParameterTypes())))
                    return method;
            }

        }

        return null;
    }

    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, null);
    }

    public static Object invokeMethod(Method method, Object target,
                                      Object args[]) {
        try {
            return method.invoke(target, args);
        } catch (Exception e) {
            // TODO: Need to handle exceptions better
            throw new IllegalStateException(e);
        }
    }
}
