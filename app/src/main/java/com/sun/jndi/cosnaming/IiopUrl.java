package com.sun.jndi.cosnaming;

import com.sun.jndi.toolkit.url.UrlUtil;
import java.net.MalformedURLException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.naming.Name;
import javax.naming.NamingException;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/jndi/cosnaming/IiopUrl.class */
public final class IiopUrl {
    private static final int DEFAULT_IIOPNAME_PORT = 9999;
    private static final int DEFAULT_IIOP_PORT = 900;
    private static final String DEFAULT_HOST = "localhost";
    private Vector<Address> addresses;
    private String stringName;

    /* loaded from: rt.jar:com/sun/jndi/cosnaming/IiopUrl$Address.class */
    public static class Address {
        public int port;
        public int major;
        public int minor;
        public String host;

        public Address(String str, boolean z2) throws MalformedURLException {
            int i2;
            int i3;
            int iIndexOf;
            this.port = -1;
            if (z2 || (iIndexOf = str.indexOf(64)) < 0) {
                this.major = 1;
                this.minor = 0;
                i2 = 0;
            } else {
                int iIndexOf2 = str.indexOf(46);
                if (iIndexOf2 < 0) {
                    throw new MalformedURLException("invalid version: " + str);
                }
                try {
                    this.major = Integer.parseInt(str.substring(0, iIndexOf2));
                    this.minor = Integer.parseInt(str.substring(iIndexOf2 + 1, iIndexOf));
                    i2 = iIndexOf + 1;
                } catch (NumberFormatException e2) {
                    throw new MalformedURLException("Nonnumeric version: " + str);
                }
            }
            int iIndexOf3 = str.indexOf(47, i2);
            iIndexOf3 = iIndexOf3 < 0 ? str.length() : iIndexOf3;
            if (str.startsWith("[", i2)) {
                int iIndexOf4 = str.indexOf(93, i2 + 1);
                if (iIndexOf4 < 0 || iIndexOf4 > iIndexOf3) {
                    throw new IllegalArgumentException("IiopURL: name is an Invalid URL: " + str);
                }
                this.host = str.substring(i2, iIndexOf4 + 1);
                i3 = iIndexOf4 + 1;
            } else {
                int iIndexOf5 = str.indexOf(58, i2);
                int i4 = (iIndexOf5 < 0 || iIndexOf5 > iIndexOf3) ? iIndexOf3 : iIndexOf5;
                if (i2 < i4) {
                    this.host = str.substring(i2, i4);
                }
                i3 = i4;
            }
            if (i3 + 1 < iIndexOf3) {
                if (str.startsWith(CallSiteDescriptor.TOKEN_DELIMITER, i3)) {
                    this.port = Integer.parseInt(str.substring(i3 + 1, iIndexOf3));
                } else {
                    throw new IllegalArgumentException("IiopURL: name is an Invalid URL: " + str);
                }
            }
            if ("".equals(this.host) || this.host == null) {
                this.host = "localhost";
            }
            if (this.port == -1) {
                this.port = z2 ? 900 : IiopUrl.DEFAULT_IIOPNAME_PORT;
            }
        }
    }

    public Vector<Address> getAddresses() {
        return this.addresses;
    }

    public String getStringName() {
        return this.stringName;
    }

    public Name getCosName() throws NamingException {
        return CNCtx.parser.parse(this.stringName);
    }

    public IiopUrl(String str) throws MalformedURLException {
        boolean z2;
        int i2;
        if (str.startsWith("iiopname://")) {
            z2 = false;
            i2 = 11;
        } else if (str.startsWith("iiop://")) {
            z2 = true;
            i2 = 7;
        } else {
            throw new MalformedURLException("Invalid iiop/iiopname URL: " + str);
        }
        int iIndexOf = str.indexOf(47, i2);
        if (iIndexOf < 0) {
            iIndexOf = str.length();
            this.stringName = "";
        } else {
            this.stringName = UrlUtil.decode(str.substring(iIndexOf + 1));
        }
        this.addresses = new Vector<>(3);
        if (z2) {
            this.addresses.addElement(new Address(str.substring(i2, iIndexOf), z2));
            return;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(str.substring(i2, iIndexOf), ",");
        while (stringTokenizer.hasMoreTokens()) {
            this.addresses.addElement(new Address(stringTokenizer.nextToken(), z2));
        }
        if (this.addresses.size() == 0) {
            this.addresses.addElement(new Address("", z2));
        }
    }
}
