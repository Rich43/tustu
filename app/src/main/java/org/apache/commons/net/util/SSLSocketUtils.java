package org.apache.commons.net.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.net.ssl.SSLSocket;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/util/SSLSocketUtils.class */
public class SSLSocketUtils {
    private SSLSocketUtils() {
    }

    public static boolean enableEndpointNameVerification(SSLSocket socket) {
        Object sslParams;
        try {
            Class<?> cls = Class.forName("javax.net.ssl.SSLParameters");
            Method setEndpointIdentificationAlgorithm = cls.getDeclaredMethod("setEndpointIdentificationAlgorithm", String.class);
            Method getSSLParameters = SSLSocket.class.getDeclaredMethod("getSSLParameters", new Class[0]);
            Method setSSLParameters = SSLSocket.class.getDeclaredMethod("setSSLParameters", cls);
            if (setEndpointIdentificationAlgorithm == null || getSSLParameters == null || setSSLParameters == null || (sslParams = getSSLParameters.invoke(socket, new Object[0])) == null) {
                return false;
            }
            setEndpointIdentificationAlgorithm.invoke(sslParams, "HTTPS");
            setSSLParameters.invoke(socket, sslParams);
            return true;
        } catch (ClassNotFoundException e2) {
            return false;
        } catch (IllegalAccessException e3) {
            return false;
        } catch (IllegalArgumentException e4) {
            return false;
        } catch (NoSuchMethodException e5) {
            return false;
        } catch (SecurityException e6) {
            return false;
        } catch (InvocationTargetException e7) {
            return false;
        }
    }
}
