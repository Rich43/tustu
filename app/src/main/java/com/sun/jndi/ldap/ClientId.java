package com.sun.jndi.ldap;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import javax.naming.ldap.Control;
import javax.net.SocketFactory;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/jndi/ldap/ClientId.class */
class ClientId {
    private final int version;
    private final String hostname;
    private final int port;
    private final String protocol;
    private final Control[] bindCtls;
    private final OutputStream trace;
    private final String socketFactory;
    private final int myHash;
    private final int ctlHash;
    private SocketFactory factory;
    private Method sockComparator;
    private boolean isDefaultSockFactory;
    public static final boolean debug = false;

    ClientId(int i2, String str, int i3, String str2, Control[] controlArr, OutputStream outputStream, String str3) {
        this.factory = null;
        this.sockComparator = null;
        this.isDefaultSockFactory = false;
        this.version = i2;
        this.hostname = str.toLowerCase(Locale.ENGLISH);
        this.port = i3;
        this.protocol = str2;
        this.bindCtls = controlArr != null ? (Control[]) controlArr.clone() : null;
        this.trace = outputStream;
        this.socketFactory = str3;
        if (str3 != null && !str3.equals("javax.net.ssl.SSLSocketFactory")) {
            try {
                Class<?> clsLoadClass = Obj.helper.loadClass(str3);
                Class<?> cls = Class.forName(Constants.OBJECT_CLASS);
                this.sockComparator = clsLoadClass.getMethod("compare", cls, cls);
                this.factory = (SocketFactory) clsLoadClass.getMethod("getDefault", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e2) {
            }
        } else {
            this.isDefaultSockFactory = true;
        }
        int iHashCode = i2 + i3 + (outputStream != null ? outputStream.hashCode() : 0) + (this.hostname != null ? this.hostname.hashCode() : 0);
        int iHashCode2 = str2 != null ? str2.hashCode() : 0;
        int iHashCodeControls = hashCodeControls(controlArr);
        this.ctlHash = iHashCodeControls;
        this.myHash = iHashCode + iHashCode2 + iHashCodeControls;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ClientId)) {
            return false;
        }
        ClientId clientId = (ClientId) obj;
        return this.myHash == clientId.myHash && this.version == clientId.version && this.port == clientId.port && this.trace == clientId.trace && (this.hostname == clientId.hostname || (this.hostname != null && this.hostname.equals(clientId.hostname))) && ((this.protocol == clientId.protocol || (this.protocol != null && this.protocol.equals(clientId.protocol))) && this.ctlHash == clientId.ctlHash && equalsControls(this.bindCtls, clientId.bindCtls) && equalsSockFactory(clientId));
    }

    public int hashCode() {
        return this.myHash;
    }

    private static int hashCodeControls(Control[] controlArr) {
        if (controlArr == null) {
            return 0;
        }
        int iHashCode = 0;
        for (Control control : controlArr) {
            iHashCode = (iHashCode * 31) + control.getID().hashCode();
        }
        return iHashCode;
    }

    private static boolean equalsControls(Control[] controlArr, Control[] controlArr2) {
        if (controlArr == controlArr2) {
            return true;
        }
        if (controlArr == null || controlArr2 == null || controlArr.length != controlArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < controlArr.length; i2++) {
            if (!controlArr[i2].getID().equals(controlArr2[i2].getID()) || controlArr[i2].isCritical() != controlArr2[i2].isCritical() || !Arrays.equals(controlArr[i2].getEncodedValue(), controlArr2[i2].getEncodedValue())) {
                return false;
            }
        }
        return true;
    }

    private boolean equalsSockFactory(ClientId clientId) {
        if (this.isDefaultSockFactory && clientId.isDefaultSockFactory) {
            return true;
        }
        if (!clientId.isDefaultSockFactory) {
            return invokeComparator(clientId, this);
        }
        return invokeComparator(this, clientId);
    }

    private boolean invokeComparator(ClientId clientId, ClientId clientId2) {
        try {
            if (((Integer) clientId.sockComparator.invoke(clientId.factory, clientId.socketFactory, clientId2.socketFactory)).intValue() == 0) {
                return true;
            }
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    private static String toStringControls(Control[] controlArr) {
        if (controlArr == null) {
            return "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (Control control : controlArr) {
            stringBuffer.append(control.getID());
            stringBuffer.append(' ');
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return this.hostname + CallSiteDescriptor.TOKEN_DELIMITER + this.port + CallSiteDescriptor.TOKEN_DELIMITER + (this.protocol != null ? this.protocol : "") + CallSiteDescriptor.TOKEN_DELIMITER + toStringControls(this.bindCtls) + CallSiteDescriptor.TOKEN_DELIMITER + this.socketFactory;
    }
}
