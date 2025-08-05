package com.sun.corba.se.impl.naming.namingutil;

import com.sun.corba.se.impl.logging.NamingSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import java.util.ArrayList;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/corba/se/impl/naming/namingutil/CorbalocURL.class */
public class CorbalocURL extends INSURLBase {
    static NamingSystemException wrapper = NamingSystemException.get(CORBALogDomains.NAMING_READ);

    public CorbalocURL(String str) {
        String strCleanEscapes = str;
        if (strCleanEscapes != null) {
            try {
                strCleanEscapes = Utility.cleanEscapes(strCleanEscapes);
            } catch (Exception e2) {
                badAddress(e2);
            }
            int iIndexOf = strCleanEscapes.indexOf(47);
            iIndexOf = iIndexOf == -1 ? strCleanEscapes.length() : iIndexOf;
            if (iIndexOf == 0) {
                badAddress(null);
            }
            StringTokenizer stringTokenizer = new StringTokenizer(strCleanEscapes.substring(0, iIndexOf), ",");
            while (stringTokenizer.hasMoreTokens()) {
                String strNextToken = stringTokenizer.nextToken();
                IIOPEndpointInfo iIOPEndpointInfoHandleColon = null;
                if (strNextToken.startsWith("iiop:")) {
                    iIOPEndpointInfoHandleColon = handleIIOPColon(strNextToken);
                } else if (strNextToken.startsWith("rir:")) {
                    handleRIRColon(strNextToken);
                    this.rirFlag = true;
                } else if (strNextToken.startsWith(CallSiteDescriptor.TOKEN_DELIMITER)) {
                    iIOPEndpointInfoHandleColon = handleColon(strNextToken);
                } else {
                    badAddress(null);
                }
                if (!this.rirFlag) {
                    if (this.theEndpointInfo == null) {
                        this.theEndpointInfo = new ArrayList();
                    }
                    this.theEndpointInfo.add(iIOPEndpointInfoHandleColon);
                }
            }
            if (strCleanEscapes.length() > iIndexOf + 1) {
                this.theKeyString = strCleanEscapes.substring(iIndexOf + 1);
            }
        }
    }

    private void badAddress(Throwable th) {
        throw wrapper.insBadAddress(th);
    }

    private IIOPEndpointInfo handleIIOPColon(String str) {
        return handleColon(str.substring(4));
    }

    private IIOPEndpointInfo handleColon(String str) {
        String strSubstring = str.substring(1);
        String strNextToken = strSubstring;
        StringTokenizer stringTokenizer = new StringTokenizer(strSubstring, "@");
        IIOPEndpointInfo iIOPEndpointInfo = new IIOPEndpointInfo();
        int iCountTokens = stringTokenizer.countTokens();
        if (iCountTokens == 0 || iCountTokens > 2) {
            badAddress(null);
        }
        if (iCountTokens == 2) {
            String strNextToken2 = stringTokenizer.nextToken();
            int iIndexOf = strNextToken2.indexOf(46);
            if (iIndexOf == -1) {
                badAddress(null);
            }
            try {
                iIOPEndpointInfo.setVersion(Integer.parseInt(strNextToken2.substring(0, iIndexOf)), Integer.parseInt(strNextToken2.substring(iIndexOf + 1)));
                strNextToken = stringTokenizer.nextToken();
            } catch (Throwable th) {
                badAddress(th);
            }
        }
        try {
        } catch (Throwable th2) {
            badAddress(th2);
        }
        if (strNextToken.indexOf(91) != -1) {
            String iPV6Port = getIPV6Port(strNextToken);
            if (iPV6Port != null) {
                iIOPEndpointInfo.setPort(Integer.parseInt(iPV6Port));
            }
            iIOPEndpointInfo.setHost(getIPV6Host(strNextToken));
            return iIOPEndpointInfo;
        }
        StringTokenizer stringTokenizer2 = new StringTokenizer(strNextToken, CallSiteDescriptor.TOKEN_DELIMITER);
        if (stringTokenizer2.countTokens() == 2) {
            iIOPEndpointInfo.setHost(stringTokenizer2.nextToken());
            iIOPEndpointInfo.setPort(Integer.parseInt(stringTokenizer2.nextToken()));
        } else if (strNextToken != null && strNextToken.length() != 0) {
            iIOPEndpointInfo.setHost(strNextToken);
        }
        Utility.validateGIOPVersion(iIOPEndpointInfo);
        return iIOPEndpointInfo;
    }

    private void handleRIRColon(String str) {
        if (str.length() != 4) {
            badAddress(null);
        }
    }

    private String getIPV6Port(String str) {
        int iIndexOf = str.indexOf(93);
        if (iIndexOf + 1 != str.length()) {
            if (str.charAt(iIndexOf + 1) != ':') {
                throw new RuntimeException("Host and Port is not separated by ':'");
            }
            return str.substring(iIndexOf + 2);
        }
        return null;
    }

    private String getIPV6Host(String str) {
        return str.substring(1, str.indexOf(93));
    }

    @Override // com.sun.corba.se.impl.naming.namingutil.INSURLBase, com.sun.corba.se.impl.naming.namingutil.INSURL
    public boolean isCorbanameURL() {
        return false;
    }
}
