package com.sun.xml.internal.ws.api.addressing;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.glassfish.gmbal.ManagedAttribute;
import com.sun.org.glassfish.gmbal.ManagedData;
import com.sun.xml.internal.ws.api.FeatureConstructor;
import java.net.URL;
import javax.xml.ws.WebServiceFeature;
import jdk.jfr.Enabled;

@ManagedData
/* loaded from: rt.jar:com/sun/xml/internal/ws/api/addressing/OneWayFeature.class */
public class OneWayFeature extends WebServiceFeature {
    public static final String ID = "http://java.sun.com/xml/ns/jaxws/addressing/oneway";
    private String messageId;
    private WSEndpointReference replyTo;
    private WSEndpointReference sslReplyTo;
    private WSEndpointReference from;
    private WSEndpointReference faultTo;
    private WSEndpointReference sslFaultTo;
    private String relatesToID;
    private boolean useAsyncWithSyncInvoke = false;

    public OneWayFeature() {
        this.enabled = true;
    }

    public OneWayFeature(boolean enabled) {
        this.enabled = enabled;
    }

    public OneWayFeature(boolean enabled, WSEndpointReference replyTo) {
        this.enabled = enabled;
        this.replyTo = replyTo;
    }

    @FeatureConstructor({Enabled.NAME, "replyTo", Constants.ATTRNAME_FROM, "relatesTo"})
    public OneWayFeature(boolean enabled, WSEndpointReference replyTo, WSEndpointReference from, String relatesTo) {
        this.enabled = enabled;
        this.replyTo = replyTo;
        this.from = from;
        this.relatesToID = relatesTo;
    }

    public OneWayFeature(AddressingPropertySet a2, AddressingVersion v2) {
        this.enabled = true;
        this.messageId = a2.getMessageId();
        this.relatesToID = a2.getRelatesTo();
        this.replyTo = makeEPR(a2.getReplyTo(), v2);
        this.faultTo = makeEPR(a2.getFaultTo(), v2);
    }

    private WSEndpointReference makeEPR(String x2, AddressingVersion v2) {
        if (x2 == null) {
            return null;
        }
        return new WSEndpointReference(x2, v2);
    }

    public String getMessageId() {
        return this.messageId;
    }

    @Override // javax.xml.ws.WebServiceFeature
    @ManagedAttribute
    public String getID() {
        return ID;
    }

    public boolean hasSslEprs() {
        return (this.sslReplyTo == null && this.sslFaultTo == null) ? false : true;
    }

    @ManagedAttribute
    public WSEndpointReference getReplyTo() {
        return this.replyTo;
    }

    public WSEndpointReference getReplyTo(boolean ssl) {
        return (!ssl || this.sslReplyTo == null) ? this.replyTo : this.sslReplyTo;
    }

    public void setReplyTo(WSEndpointReference address) {
        this.replyTo = address;
    }

    public WSEndpointReference getSslReplyTo() {
        return this.sslReplyTo;
    }

    public void setSslReplyTo(WSEndpointReference sslReplyTo) {
        this.sslReplyTo = sslReplyTo;
    }

    @ManagedAttribute
    public WSEndpointReference getFrom() {
        return this.from;
    }

    public void setFrom(WSEndpointReference address) {
        this.from = address;
    }

    @ManagedAttribute
    public String getRelatesToID() {
        return this.relatesToID;
    }

    public void setRelatesToID(String id) {
        this.relatesToID = id;
    }

    public WSEndpointReference getFaultTo() {
        return this.faultTo;
    }

    public WSEndpointReference getFaultTo(boolean ssl) {
        return (!ssl || this.sslFaultTo == null) ? this.faultTo : this.sslFaultTo;
    }

    public void setFaultTo(WSEndpointReference address) {
        this.faultTo = address;
    }

    public WSEndpointReference getSslFaultTo() {
        return this.sslFaultTo;
    }

    public void setSslFaultTo(WSEndpointReference sslFaultTo) {
        this.sslFaultTo = sslFaultTo;
    }

    public boolean isUseAsyncWithSyncInvoke() {
        return this.useAsyncWithSyncInvoke;
    }

    public void setUseAsyncWithSyncInvoke(boolean useAsyncWithSyncInvoke) {
        this.useAsyncWithSyncInvoke = useAsyncWithSyncInvoke;
    }

    public static WSEndpointReference enableSslForEpr(@NotNull WSEndpointReference epr, @Nullable String sslHost, int sslPort) {
        if (!epr.isAnonymous()) {
            String address = epr.getAddress();
            try {
                URL url = new URL(address);
                String protocol = url.getProtocol();
                if (!protocol.equalsIgnoreCase("https")) {
                    String host = url.getHost();
                    if (sslHost != null) {
                        host = sslHost;
                    }
                    int port = url.getPort();
                    if (sslPort > 0) {
                        port = sslPort;
                    }
                    try {
                        String address2 = new URL("https", host, port, url.getFile()).toExternalForm();
                        return new WSEndpointReference(address2, epr.getVersion());
                    } catch (Exception e2) {
                        throw new RuntimeException(e2);
                    }
                }
            } catch (Exception e3) {
                throw new RuntimeException(e3);
            }
        }
        return epr;
    }
}
