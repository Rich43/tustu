package com.sun.org.apache.xml.internal.security.utils.resolver.implementations;

import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverContext;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverException;
import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/resolver/implementations/ResolverDirectHTTP.class */
public class ResolverDirectHTTP extends ResourceResolverSpi {
    private static final Logger LOG = LoggerFactory.getLogger(ResolverDirectHTTP.class);
    private static final String[] properties = {"http.proxy.host", "http.proxy.port", "http.proxy.username", "http.proxy.password", "http.basic.username", "http.basic.password"};
    private static final int HttpProxyHost = 0;
    private static final int HttpProxyPort = 1;
    private static final int HttpProxyUser = 2;
    private static final int HttpProxyPass = 3;
    private static final int HttpBasicUser = 4;
    private static final int HttpBasicPass = 5;

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public boolean engineIsThreadSafe() {
        return true;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public XMLSignatureInput engineResolveURI(ResourceResolverContext resourceResolverContext) throws ResourceResolverException {
        try {
            URI newURI = getNewURI(resourceResolverContext.uriToResolve, resourceResolverContext.baseUri);
            URL url = newURI.toURL();
            URLConnection uRLConnectionOpenConnection = openConnection(url);
            String headerField = uRLConnectionOpenConnection.getHeaderField("WWW-Authenticate");
            if (headerField != null && headerField.startsWith("Basic")) {
                String strEngineGetProperty = engineGetProperty(properties[4]);
                String strEngineGetProperty2 = engineGetProperty(properties[5]);
                if (strEngineGetProperty != null && strEngineGetProperty2 != null) {
                    uRLConnectionOpenConnection = openConnection(url);
                    uRLConnectionOpenConnection.setRequestProperty("Authorization", "Basic " + XMLUtils.encodeToString((strEngineGetProperty + CallSiteDescriptor.TOKEN_DELIMITER + strEngineGetProperty2).getBytes(StandardCharsets.ISO_8859_1)));
                }
            }
            String headerField2 = uRLConnectionOpenConnection.getHeaderField("Content-Type");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Throwable th = null;
            try {
                InputStream inputStream = uRLConnectionOpenConnection.getInputStream();
                Throwable th2 = null;
                try {
                    try {
                        byte[] bArr = new byte[4096];
                        int i2 = 0;
                        while (true) {
                            int i3 = inputStream.read(bArr);
                            if (i3 < 0) {
                                break;
                            }
                            byteArrayOutputStream.write(bArr, 0, i3);
                            i2 += i3;
                        }
                        LOG.debug("Fetched {} bytes from URI {}", Integer.valueOf(i2), newURI.toString());
                        XMLSignatureInput xMLSignatureInput = new XMLSignatureInput(byteArrayOutputStream.toByteArray());
                        xMLSignatureInput.setSecureValidation(resourceResolverContext.secureValidation);
                        xMLSignatureInput.setSourceURI(newURI.toString());
                        xMLSignatureInput.setMIMEType(headerField2);
                        if (inputStream != null) {
                            if (0 != 0) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th3) {
                                    th2.addSuppressed(th3);
                                }
                            } else {
                                inputStream.close();
                            }
                        }
                        return xMLSignatureInput;
                    } catch (Throwable th4) {
                        if (inputStream != null) {
                            if (th2 != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable th5) {
                                    th2.addSuppressed(th5);
                                }
                            } else {
                                inputStream.close();
                            }
                        }
                        throw th4;
                    }
                } finally {
                }
            } finally {
                if (byteArrayOutputStream != null) {
                    if (0 != 0) {
                        try {
                            byteArrayOutputStream.close();
                        } catch (Throwable th6) {
                            th.addSuppressed(th6);
                        }
                    } else {
                        byteArrayOutputStream.close();
                    }
                }
            }
        } catch (IOException e2) {
            throw new ResourceResolverException(e2, resourceResolverContext.uriToResolve, resourceResolverContext.baseUri, "generic.EmptyMessage");
        } catch (IllegalArgumentException e3) {
            throw new ResourceResolverException(e3, resourceResolverContext.uriToResolve, resourceResolverContext.baseUri, "generic.EmptyMessage");
        } catch (MalformedURLException e4) {
            throw new ResourceResolverException(e4, resourceResolverContext.uriToResolve, resourceResolverContext.baseUri, "generic.EmptyMessage");
        } catch (URISyntaxException e5) {
            throw new ResourceResolverException(e5, resourceResolverContext.uriToResolve, resourceResolverContext.baseUri, "generic.EmptyMessage");
        }
    }

    private URLConnection openConnection(URL url) throws NumberFormatException, IOException {
        URLConnection uRLConnectionOpenConnection;
        String strEngineGetProperty = engineGetProperty(properties[0]);
        String strEngineGetProperty2 = engineGetProperty(properties[1]);
        String strEngineGetProperty3 = engineGetProperty(properties[2]);
        String strEngineGetProperty4 = engineGetProperty(properties[3]);
        Proxy proxy = null;
        if (strEngineGetProperty != null && strEngineGetProperty2 != null) {
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(strEngineGetProperty, Integer.parseInt(strEngineGetProperty2)));
        }
        if (proxy != null) {
            uRLConnectionOpenConnection = url.openConnection(proxy);
            if (strEngineGetProperty3 != null && strEngineGetProperty4 != null) {
                uRLConnectionOpenConnection.setRequestProperty("Proxy-Authorization", "Basic " + XMLUtils.encodeToString((strEngineGetProperty3 + CallSiteDescriptor.TOKEN_DELIMITER + strEngineGetProperty4).getBytes(StandardCharsets.ISO_8859_1)));
            }
        } else {
            uRLConnectionOpenConnection = url.openConnection();
        }
        return uRLConnectionOpenConnection;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public boolean engineCanResolveURI(ResourceResolverContext resourceResolverContext) {
        if (resourceResolverContext.uriToResolve == null) {
            LOG.debug("quick fail, uri == null");
            return false;
        }
        if (resourceResolverContext.uriToResolve.equals("") || resourceResolverContext.uriToResolve.charAt(0) == '#') {
            LOG.debug("quick fail for empty URIs and local ones");
            return false;
        }
        LOG.debug("I was asked whether I can resolve {}", resourceResolverContext.uriToResolve);
        if (resourceResolverContext.uriToResolve.startsWith("http:") || (resourceResolverContext.baseUri != null && resourceResolverContext.baseUri.startsWith("http:"))) {
            LOG.debug("I state that I can resolve {}", resourceResolverContext.uriToResolve);
            return true;
        }
        LOG.debug("I state that I can't resolve {}", resourceResolverContext.uriToResolve);
        return false;
    }

    @Override // com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi
    public String[] engineGetPropertyKeys() {
        return (String[]) properties.clone();
    }

    private static URI getNewURI(String str, String str2) throws URISyntaxException {
        URI uri;
        if (str2 == null || "".equals(str2)) {
            uri = new URI(str);
        } else {
            uri = new URI(str2).resolve(str);
        }
        if (uri.getFragment() != null) {
            return new URI(uri.getScheme(), uri.getSchemeSpecificPart(), null);
        }
        return uri;
    }
}
