package com.sun.xml.internal.ws.policy.jaxws;

import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken;
import com.sun.xml.internal.ws.resources.PolicyMessages;
import java.util.HashSet;
import java.util.Set;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/SafePolicyReader.class */
public class SafePolicyReader {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) SafePolicyReader.class);
    private final Set<String> urlsRead = new HashSet();
    private final Set<String> qualifiedPolicyUris = new HashSet();

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/SafePolicyReader$PolicyRecord.class */
    public final class PolicyRecord {
        PolicyRecord next;
        PolicySourceModel policyModel;
        Set<String> unresolvedURIs;
        private String uri;

        PolicyRecord() {
        }

        PolicyRecord insert(PolicyRecord insertedRec) {
            if (null == insertedRec.unresolvedURIs || insertedRec.unresolvedURIs.isEmpty()) {
                insertedRec.next = this;
                return insertedRec;
            }
            PolicyRecord oneBeforeCurrent = null;
            PolicyRecord policyRecord = this;
            while (true) {
                PolicyRecord current = policyRecord;
                if (null != current.next) {
                    if (null != current.unresolvedURIs && current.unresolvedURIs.contains(insertedRec.uri)) {
                        if (null == oneBeforeCurrent) {
                            insertedRec.next = current;
                            return insertedRec;
                        }
                        oneBeforeCurrent.next = insertedRec;
                        insertedRec.next = current;
                        return this;
                    }
                    if (insertedRec.unresolvedURIs.remove(current.uri) && insertedRec.unresolvedURIs.isEmpty()) {
                        insertedRec.next = current.next;
                        current.next = insertedRec;
                        return this;
                    }
                    oneBeforeCurrent = current;
                    policyRecord = current.next;
                } else {
                    insertedRec.next = null;
                    current.next = insertedRec;
                    return this;
                }
            }
        }

        public void setUri(String uri, String id) throws PolicyException {
            if (SafePolicyReader.this.qualifiedPolicyUris.contains(uri)) {
                throw ((PolicyException) SafePolicyReader.LOGGER.logSevereException(new PolicyException(PolicyMessages.WSP_1020_DUPLICATE_ID(id))));
            }
            this.uri = uri;
            SafePolicyReader.this.qualifiedPolicyUris.add(uri);
        }

        public String getUri() {
            return this.uri;
        }

        public String toString() {
            String result = this.uri;
            if (null != this.next) {
                result = result + "->" + this.next.toString();
            }
            return result;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:80:0x038e A[Catch: Exception -> 0x03df, TryCatch #0 {Exception -> 0x03df, blocks: (B:9:0x002d, B:10:0x0033, B:11:0x0070, B:15:0x0088, B:17:0x0094, B:18:0x0097, B:20:0x00b2, B:23:0x00e6, B:24:0x0131, B:29:0x0154, B:31:0x0167, B:33:0x0173, B:34:0x017f, B:35:0x0196, B:37:0x01a6, B:56:0x02ac, B:40:0x01bb, B:42:0x01c7, B:49:0x0216, B:51:0x0242, B:53:0x0259, B:55:0x0274, B:52:0x0251, B:44:0x01d7, B:46:0x01f3, B:48:0x020a, B:47:0x0202, B:57:0x02b2, B:22:0x00bf, B:58:0x02c6, B:60:0x02d8, B:61:0x02db, B:65:0x030a, B:64:0x02f3, B:66:0x031e, B:67:0x032c, B:69:0x0347, B:73:0x0355, B:74:0x035c, B:78:0x036c, B:80:0x038e, B:81:0x03bc, B:83:0x03c7), top: B:90:0x002d }] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x03bc A[Catch: Exception -> 0x03df, TryCatch #0 {Exception -> 0x03df, blocks: (B:9:0x002d, B:10:0x0033, B:11:0x0070, B:15:0x0088, B:17:0x0094, B:18:0x0097, B:20:0x00b2, B:23:0x00e6, B:24:0x0131, B:29:0x0154, B:31:0x0167, B:33:0x0173, B:34:0x017f, B:35:0x0196, B:37:0x01a6, B:56:0x02ac, B:40:0x01bb, B:42:0x01c7, B:49:0x0216, B:51:0x0242, B:53:0x0259, B:55:0x0274, B:52:0x0251, B:44:0x01d7, B:46:0x01f3, B:48:0x020a, B:47:0x0202, B:57:0x02b2, B:22:0x00bf, B:58:0x02c6, B:60:0x02d8, B:61:0x02db, B:65:0x030a, B:64:0x02f3, B:66:0x031e, B:67:0x032c, B:69:0x0347, B:73:0x0355, B:74:0x035c, B:78:0x036c, B:80:0x038e, B:81:0x03bc, B:83:0x03c7), top: B:90:0x002d }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.sun.xml.internal.ws.policy.jaxws.SafePolicyReader.PolicyRecord readPolicyElement(javax.xml.stream.XMLStreamReader r7, java.lang.String r8) {
        /*
            Method dump skipped, instructions count: 1033
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.ws.policy.jaxws.SafePolicyReader.readPolicyElement(javax.xml.stream.XMLStreamReader, java.lang.String):com.sun.xml.internal.ws.policy.jaxws.SafePolicyReader$PolicyRecord");
    }

    public Set<String> getUrlsRead() {
        return this.urlsRead;
    }

    public String readPolicyReferenceElement(XMLStreamReader reader) {
        try {
            if (NamespaceVersion.resolveAsToken(reader.getName()) == XmlToken.PolicyReference) {
                for (int i2 = 0; i2 < reader.getAttributeCount(); i2++) {
                    if (XmlToken.resolveToken(reader.getAttributeName(i2).getLocalPart()) == XmlToken.Uri) {
                        String uriValue = reader.getAttributeValue(i2);
                        reader.next();
                        return uriValue;
                    }
                }
            }
            reader.next();
            return null;
        } catch (XMLStreamException e2) {
            throw ((WebServiceException) LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1001_XML_EXCEPTION_WHEN_PROCESSING_POLICY_REFERENCE(), e2)));
        }
    }

    public static String relativeToAbsoluteUrl(String relativeUri, String baseUri) {
        if ('#' != relativeUri.charAt(0)) {
            return relativeUri;
        }
        return null == baseUri ? relativeUri : baseUri + relativeUri;
    }
}
