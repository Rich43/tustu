package com.sun.xml.internal.ws.addressing;

import com.sun.xml.internal.ws.api.model.CheckedException;
import com.sun.xml.internal.ws.api.model.JavaMethod;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAPNamespaceConstants;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/WsaActionUtil.class */
public class WsaActionUtil {
    private static final Logger LOGGER = Logger.getLogger(WsaActionUtil.class.getName());

    public static final String getDefaultFaultAction(JavaMethod method, CheckedException ce) {
        String tns = method.getOwner().getTargetNamespace();
        String delim = getDelimiter(tns);
        if (tns.endsWith(delim)) {
            tns = tns.substring(0, tns.length() - 1);
        }
        return tns + delim + method.getOwner().getPortTypeName().getLocalPart() + delim + method.getOperationName() + delim + SOAPNamespaceConstants.TAG_FAULT + delim + ce.getExceptionClass().getSimpleName();
    }

    private static String getDelimiter(String tns) {
        String delim = "/";
        try {
            URI uri = new URI(tns);
            if (uri.getScheme() != null) {
                if (uri.getScheme().equalsIgnoreCase("urn")) {
                    delim = CallSiteDescriptor.TOKEN_DELIMITER;
                }
            }
        } catch (URISyntaxException e2) {
            LOGGER.warning("TargetNamespace of WebService is not a valid URI");
        }
        return delim;
    }
}
