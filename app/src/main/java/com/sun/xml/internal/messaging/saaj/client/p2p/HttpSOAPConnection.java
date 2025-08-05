package com.sun.xml.internal.messaging.saaj.client.p2p;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.util.Base64;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.internal.messaging.saaj.util.ParseUtil;
import com.sun.xml.internal.messaging.saaj.util.SAAJUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeader;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/client/p2p/HttpSOAPConnection.class */
class HttpSOAPConnection extends SOAPConnection {
    public static final String vmVendor = SAAJUtil.getSystemProperty("java.vendor.url");
    private static final String sunVmVendor = "http://java.sun.com/";
    private static final String ibmVmVendor = "http://www.ibm.com/";
    private static final boolean isSunVM;
    private static final boolean isIBMVM;
    private static final String JAXM_URLENDPOINT = "javax.xml.messaging.URLEndpoint";
    protected static final Logger log;
    MessageFactory messageFactory;
    boolean closed = false;
    private static final String SSL_PKG;
    private static final String SSL_PROVIDER;
    private static final int dL = 0;

    static {
        isSunVM = sunVmVendor.equals(vmVendor);
        isIBMVM = ibmVmVendor.equals(vmVendor);
        log = Logger.getLogger(LogDomainConstants.HTTP_CONN_DOMAIN, "com.sun.xml.internal.messaging.saaj.client.p2p.LocalStrings");
        if (isIBMVM) {
            SSL_PKG = "com.ibm.net.ssl.internal.www.protocol";
            SSL_PROVIDER = "com.ibm.net.ssl.internal.ssl.Provider";
        } else {
            SSL_PKG = "com.sun.net.ssl.internal.www.protocol";
            SSL_PROVIDER = "com.sun.net.ssl.internal.ssl.Provider";
        }
    }

    public HttpSOAPConnection() throws SOAPException {
        this.messageFactory = null;
        try {
            this.messageFactory = MessageFactory.newInstance(SOAPConstants.DYNAMIC_SOAP_PROTOCOL);
        } catch (Exception ex) {
            log.log(Level.SEVERE, "SAAJ0001.p2p.cannot.create.msg.factory", (Throwable) ex);
            throw new SOAPExceptionImpl("Unable to create message factory", ex);
        } catch (NoSuchMethodError e2) {
            this.messageFactory = MessageFactory.newInstance();
        }
    }

    @Override // javax.xml.soap.SOAPConnection
    public void close() throws SOAPException {
        if (this.closed) {
            log.severe("SAAJ0002.p2p.close.already.closed.conn");
            throw new SOAPExceptionImpl("Connection already closed");
        }
        this.messageFactory = null;
        this.closed = true;
    }

    @Override // javax.xml.soap.SOAPConnection
    public SOAPMessage call(SOAPMessage message, Object endPoint) throws SOAPException {
        if (this.closed) {
            log.severe("SAAJ0003.p2p.call.already.closed.conn");
            throw new SOAPExceptionImpl("Connection is closed");
        }
        Class urlEndpointClass = null;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            if (loader != null) {
                urlEndpointClass = loader.loadClass(JAXM_URLENDPOINT);
            } else {
                urlEndpointClass = Class.forName(JAXM_URLENDPOINT);
            }
        } catch (ClassNotFoundException e2) {
            if (log.isLoggable(Level.FINEST)) {
                log.finest("SAAJ0090.p2p.endpoint.available.only.for.JAXM");
            }
        }
        if (urlEndpointClass != null && urlEndpointClass.isInstance(endPoint)) {
            try {
                Method m2 = urlEndpointClass.getMethod("getURL", (Class[]) null);
                String url = (String) m2.invoke(endPoint, (Object[]) null);
                try {
                    endPoint = new URL(url);
                } catch (MalformedURLException mex) {
                    log.log(Level.SEVERE, "SAAJ0005.p2p.", (Throwable) mex);
                    throw new SOAPExceptionImpl("Bad URL: " + mex.getMessage());
                }
            } catch (Exception ex) {
                log.log(Level.SEVERE, "SAAJ0004.p2p.internal.err", (Throwable) ex);
                throw new SOAPExceptionImpl("Internal error: " + ex.getMessage());
            }
        }
        if (endPoint instanceof String) {
            try {
                endPoint = new URL((String) endPoint);
            } catch (MalformedURLException mex2) {
                log.log(Level.SEVERE, "SAAJ0006.p2p.bad.URL", (Throwable) mex2);
                throw new SOAPExceptionImpl("Bad URL: " + mex2.getMessage());
            }
        }
        if (endPoint instanceof URL) {
            try {
                SOAPMessage response = post(message, (URL) endPoint);
                return response;
            } catch (Exception ex2) {
                throw new SOAPExceptionImpl(ex2);
            }
        }
        log.severe("SAAJ0007.p2p.bad.endPoint.type");
        throw new SOAPExceptionImpl("Bad endPoint type " + endPoint);
    }

    SOAPMessage post(SOAPMessage message, URL endPoint) throws SOAPException, IOException {
        int responseCode;
        InputStream inputStream;
        boolean isFailure = false;
        try {
            if (endPoint.getProtocol().equals("https")) {
                initHttps();
            }
            URI uri = new URI(endPoint.toString());
            String userInfo = uri.getRawUserInfo();
            if (!endPoint.getProtocol().equalsIgnoreCase("http") && !endPoint.getProtocol().equalsIgnoreCase("https")) {
                log.severe("SAAJ0052.p2p.protocol.mustbe.http.or.https");
                throw new IllegalArgumentException("Protocol " + endPoint.getProtocol() + " not supported in URL " + ((Object) endPoint));
            }
            HttpURLConnection httpConnection = createConnection(endPoint);
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setUseCaches(false);
            httpConnection.setInstanceFollowRedirects(true);
            if (message.saveRequired()) {
                message.saveChanges();
            }
            MimeHeaders headers = message.getMimeHeaders();
            Iterator it = headers.getAllHeaders();
            boolean hasAuth = false;
            while (it.hasNext()) {
                MimeHeader header = (MimeHeader) it.next();
                String[] values = headers.getHeader(header.getName());
                if (values.length == 1) {
                    httpConnection.setRequestProperty(header.getName(), header.getValue());
                } else {
                    StringBuffer concat = new StringBuffer();
                    for (int i2 = 0; i2 < values.length; i2++) {
                        if (i2 != 0) {
                            concat.append(',');
                        }
                        concat.append(values[i2]);
                    }
                    httpConnection.setRequestProperty(header.getName(), concat.toString());
                }
                if ("Authorization".equals(header.getName())) {
                    hasAuth = true;
                    if (log.isLoggable(Level.FINE)) {
                        log.fine("SAAJ0091.p2p.https.auth.in.POST.true");
                    }
                }
            }
            if (!hasAuth && userInfo != null) {
                initAuthUserInfo(httpConnection, userInfo);
            }
            OutputStream out = httpConnection.getOutputStream();
            try {
                message.writeTo(out);
                out.flush();
                out.close();
                httpConnection.connect();
                try {
                    responseCode = httpConnection.getResponseCode();
                    if (responseCode == 500) {
                        isFailure = true;
                    } else if (responseCode / 100 != 2) {
                        log.log(Level.SEVERE, "SAAJ0008.p2p.bad.response", (Object[]) new String[]{httpConnection.getResponseMessage()});
                        throw new SOAPExceptionImpl("Bad response: (" + responseCode + httpConnection.getResponseMessage());
                    }
                } catch (IOException e2) {
                    responseCode = httpConnection.getResponseCode();
                    if (responseCode == 500) {
                        isFailure = true;
                    } else {
                        throw e2;
                    }
                }
                SOAPMessage response = null;
                InputStream httpIn = null;
                if (responseCode == 200 || isFailure) {
                    try {
                        try {
                            try {
                                MimeHeaders headers2 = new MimeHeaders();
                                int i3 = 1;
                                while (true) {
                                    String key = httpConnection.getHeaderFieldKey(i3);
                                    String value = httpConnection.getHeaderField(i3);
                                    if (key == null && value == null) {
                                        break;
                                    }
                                    if (key != null) {
                                        StringTokenizer values2 = new StringTokenizer(value, ",");
                                        while (values2.hasMoreTokens()) {
                                            headers2.addHeader(key, values2.nextToken().trim());
                                        }
                                    }
                                    i3++;
                                }
                                if (isFailure) {
                                    inputStream = httpConnection.getErrorStream();
                                } else {
                                    inputStream = httpConnection.getInputStream();
                                }
                                httpIn = inputStream;
                                byte[] bytes = readFully(httpIn);
                                int length = httpConnection.getContentLength() == -1 ? bytes.length : httpConnection.getContentLength();
                                if (length == 0) {
                                    response = null;
                                    log.warning("SAAJ0014.p2p.content.zero");
                                } else {
                                    ByteInputStream in = new ByteInputStream(bytes, length);
                                    response = this.messageFactory.createMessage(headers2, in);
                                }
                            } catch (Exception ex) {
                                log.log(Level.SEVERE, "SAAJ0010.p2p.cannot.read.resp", (Throwable) ex);
                                throw new SOAPExceptionImpl("Unable to read response: " + ex.getMessage());
                            }
                        } catch (SOAPException ex2) {
                            throw ex2;
                        }
                    } finally {
                        if (httpIn != null) {
                            httpIn.close();
                        }
                        httpConnection.disconnect();
                    }
                }
                return response;
            } catch (Throwable th) {
                out.close();
                throw th;
            }
        } catch (SOAPException ex3) {
            throw ex3;
        } catch (Exception ex4) {
            log.severe("SAAJ0009.p2p.msg.send.failed");
            throw new SOAPExceptionImpl("Message send failed", ex4);
        }
    }

    @Override // javax.xml.soap.SOAPConnection
    public SOAPMessage get(Object endPoint) throws SOAPException {
        if (this.closed) {
            log.severe("SAAJ0011.p2p.get.already.closed.conn");
            throw new SOAPExceptionImpl("Connection is closed");
        }
        Class urlEndpointClass = null;
        try {
            urlEndpointClass = Class.forName(JAXM_URLENDPOINT);
        } catch (Exception e2) {
        }
        if (urlEndpointClass != null && urlEndpointClass.isInstance(endPoint)) {
            try {
                Method m2 = urlEndpointClass.getMethod("getURL", (Class[]) null);
                String url = (String) m2.invoke(endPoint, (Object[]) null);
                try {
                    endPoint = new URL(url);
                } catch (MalformedURLException mex) {
                    log.severe("SAAJ0005.p2p.");
                    throw new SOAPExceptionImpl("Bad URL: " + mex.getMessage());
                }
            } catch (Exception ex) {
                log.severe("SAAJ0004.p2p.internal.err");
                throw new SOAPExceptionImpl("Internal error: " + ex.getMessage());
            }
        }
        if (endPoint instanceof String) {
            try {
                endPoint = new URL((String) endPoint);
            } catch (MalformedURLException mex2) {
                log.severe("SAAJ0006.p2p.bad.URL");
                throw new SOAPExceptionImpl("Bad URL: " + mex2.getMessage());
            }
        }
        if (endPoint instanceof URL) {
            try {
                SOAPMessage response = doGet((URL) endPoint);
                return response;
            } catch (Exception ex2) {
                throw new SOAPExceptionImpl(ex2);
            }
        }
        throw new SOAPExceptionImpl("Bad endPoint type " + endPoint);
    }

    SOAPMessage doGet(URL endPoint) throws SOAPException, IOException {
        int responseCode;
        InputStream inputStream;
        boolean isFailure = false;
        try {
            if (endPoint.getProtocol().equals("https")) {
                initHttps();
            }
            URI uri = new URI(endPoint.toString());
            uri.getRawUserInfo();
            if (!endPoint.getProtocol().equalsIgnoreCase("http") && !endPoint.getProtocol().equalsIgnoreCase("https")) {
                log.severe("SAAJ0052.p2p.protocol.mustbe.http.or.https");
                throw new IllegalArgumentException("Protocol " + endPoint.getProtocol() + " not supported in URL " + ((Object) endPoint));
            }
            HttpURLConnection httpConnection = createConnection(endPoint);
            httpConnection.setRequestMethod("GET");
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setUseCaches(false);
            HttpURLConnection.setFollowRedirects(true);
            httpConnection.connect();
            try {
                responseCode = httpConnection.getResponseCode();
                if (responseCode == 500) {
                    isFailure = true;
                } else if (responseCode / 100 != 2) {
                    log.log(Level.SEVERE, "SAAJ0008.p2p.bad.response", (Object[]) new String[]{httpConnection.getResponseMessage()});
                    throw new SOAPExceptionImpl("Bad response: (" + responseCode + httpConnection.getResponseMessage());
                }
            } catch (IOException e2) {
                responseCode = httpConnection.getResponseCode();
                if (responseCode == 500) {
                    isFailure = true;
                } else {
                    throw e2;
                }
            }
            SOAPMessage response = null;
            InputStream httpIn = null;
            if (responseCode == 200 || isFailure) {
                try {
                    try {
                        MimeHeaders headers = new MimeHeaders();
                        int i2 = 1;
                        while (true) {
                            String key = httpConnection.getHeaderFieldKey(i2);
                            String value = httpConnection.getHeaderField(i2);
                            if (key == null && value == null) {
                                break;
                            }
                            if (key != null) {
                                StringTokenizer values = new StringTokenizer(value, ",");
                                while (values.hasMoreTokens()) {
                                    headers.addHeader(key, values.nextToken().trim());
                                }
                            }
                            i2++;
                        }
                        if (isFailure) {
                            inputStream = httpConnection.getErrorStream();
                        } else {
                            inputStream = httpConnection.getInputStream();
                        }
                        httpIn = inputStream;
                        if (httpIn == null || httpConnection.getContentLength() == 0 || httpIn.available() == 0) {
                            response = null;
                            log.warning("SAAJ0014.p2p.content.zero");
                        } else {
                            response = this.messageFactory.createMessage(headers, httpIn);
                        }
                    } catch (SOAPException ex) {
                        throw ex;
                    } catch (Exception ex2) {
                        log.log(Level.SEVERE, "SAAJ0010.p2p.cannot.read.resp", (Throwable) ex2);
                        throw new SOAPExceptionImpl("Unable to read response: " + ex2.getMessage());
                    }
                } finally {
                    if (httpIn != null) {
                        httpIn.close();
                    }
                    httpConnection.disconnect();
                }
            }
            return response;
        } catch (SOAPException ex3) {
            throw ex3;
        } catch (Exception ex4) {
            log.severe("SAAJ0012.p2p.get.failed");
            throw new SOAPExceptionImpl("Get failed", ex4);
        }
    }

    private byte[] readFully(InputStream istream) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        while (true) {
            int num = istream.read(buf);
            if (num != -1) {
                bout.write(buf, 0, num);
            } else {
                byte[] ret = bout.toByteArray();
                return ret;
            }
        }
    }

    private void initHttps() {
        String pkgs;
        String pkgs2 = SAAJUtil.getSystemProperty("java.protocol.handler.pkgs");
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "SAAJ0053.p2p.providers", (Object[]) new String[]{pkgs2});
        }
        if (pkgs2 == null || pkgs2.indexOf(SSL_PKG) < 0) {
            if (pkgs2 == null) {
                pkgs = SSL_PKG;
            } else {
                pkgs = pkgs2 + CallSiteDescriptor.OPERATOR_DELIMITER + SSL_PKG;
            }
            System.setProperty("java.protocol.handler.pkgs", pkgs);
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "SAAJ0054.p2p.set.providers", (Object[]) new String[]{pkgs});
            }
            try {
                Class c2 = Class.forName(SSL_PROVIDER);
                Provider p2 = (Provider) c2.newInstance();
                Security.addProvider(p2);
                if (log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE, "SAAJ0055.p2p.added.ssl.provider", (Object[]) new String[]{SSL_PROVIDER});
                }
            } catch (Exception e2) {
            }
        }
    }

    private void initAuthUserInfo(HttpURLConnection conn, String userInfo) {
        String user;
        String password;
        if (userInfo != null) {
            int delimiter = userInfo.indexOf(58);
            if (delimiter == -1) {
                user = ParseUtil.decode(userInfo);
                password = null;
            } else {
                user = ParseUtil.decode(userInfo.substring(0, delimiter));
                password = ParseUtil.decode(userInfo.substring(delimiter + 1));
            }
            String plain = user + CallSiteDescriptor.TOKEN_DELIMITER;
            byte[] nameBytes = plain.getBytes();
            byte[] passwdBytes = password.getBytes();
            byte[] concat = new byte[nameBytes.length + passwdBytes.length];
            System.arraycopy(nameBytes, 0, concat, 0, nameBytes.length);
            System.arraycopy(passwdBytes, 0, concat, nameBytes.length, passwdBytes.length);
            String auth = "Basic " + new String(Base64.encode(concat));
            conn.setRequestProperty("Authorization", auth);
        }
    }

    private void d(String s2) {
        log.log(Level.SEVERE, "SAAJ0013.p2p.HttpSOAPConnection", (Object[]) new String[]{s2});
        System.err.println("HttpSOAPConnection: " + s2);
    }

    private HttpURLConnection createConnection(URL endpoint) throws IOException {
        return (HttpURLConnection) endpoint.openConnection();
    }
}
