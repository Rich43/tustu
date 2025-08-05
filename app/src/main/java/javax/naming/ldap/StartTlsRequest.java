package javax.naming.ldap;

import com.sun.naming.internal.VersionHelper;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceLoader;
import javax.naming.ConfigurationException;
import javax.naming.NamingException;

/* loaded from: rt.jar:javax/naming/ldap/StartTlsRequest.class */
public class StartTlsRequest implements ExtendedRequest {
    public static final String OID = "1.3.6.1.4.1.1466.20037";
    private static final long serialVersionUID = 4441679576360753397L;

    @Override // javax.naming.ldap.ExtendedRequest
    public String getID() {
        return "1.3.6.1.4.1.1466.20037";
    }

    @Override // javax.naming.ldap.ExtendedRequest
    public byte[] getEncodedValue() {
        return null;
    }

    @Override // javax.naming.ldap.ExtendedRequest
    public ExtendedResponse createExtendedResponse(String str, byte[] bArr, int i2, int i3) throws NamingException {
        if (str != null && !str.equals("1.3.6.1.4.1.1466.20037")) {
            throw new ConfigurationException("Start TLS received the following response instead of 1.3.6.1.4.1.1466.20037: " + str);
        }
        StartTlsResponse startTlsResponse = null;
        Iterator it = ServiceLoader.load(StartTlsResponse.class, getContextClassLoader()).iterator();
        while (startTlsResponse == null && privilegedHasNext(it)) {
            startTlsResponse = (StartTlsResponse) it.next();
        }
        if (startTlsResponse != null) {
            return startTlsResponse;
        }
        try {
            return (StartTlsResponse) VersionHelper.getVersionHelper().loadClass("com.sun.jndi.ldap.ext.StartTlsResponseImpl").newInstance();
        } catch (ClassNotFoundException e2) {
            throw wrapException(e2);
        } catch (IllegalAccessException e3) {
            throw wrapException(e3);
        } catch (InstantiationException e4) {
            throw wrapException(e4);
        }
    }

    private ConfigurationException wrapException(Exception exc) {
        ConfigurationException configurationException = new ConfigurationException("Cannot load implementation of javax.naming.ldap.StartTlsResponse");
        configurationException.setRootCause(exc);
        return configurationException;
    }

    private final ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: javax.naming.ldap.StartTlsRequest.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }

    private static final boolean privilegedHasNext(final Iterator<StartTlsResponse> it) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: javax.naming.ldap.StartTlsRequest.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                return Boolean.valueOf(it.hasNext());
            }
        })).booleanValue();
    }
}
