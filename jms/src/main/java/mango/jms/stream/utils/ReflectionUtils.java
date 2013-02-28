package mango.jms.stream.utils;

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
