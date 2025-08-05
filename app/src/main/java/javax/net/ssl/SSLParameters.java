package javax.net.ssl;

import java.security.AlgorithmConstraints;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* loaded from: rt.jar:javax/net/ssl/SSLParameters.class */
public class SSLParameters {
    private String[] cipherSuites;
    private String[] protocols;
    private boolean wantClientAuth;
    private boolean needClientAuth;
    private String identificationAlgorithm;
    private AlgorithmConstraints algorithmConstraints;
    private Map<Integer, SNIServerName> sniNames;
    private Map<Integer, SNIMatcher> sniMatchers;
    private boolean preferLocalCipherSuites;
    private String[] applicationProtocols;

    public SSLParameters() {
        this.sniNames = null;
        this.sniMatchers = null;
        this.applicationProtocols = new String[0];
    }

    public SSLParameters(String[] strArr) {
        this.sniNames = null;
        this.sniMatchers = null;
        this.applicationProtocols = new String[0];
        setCipherSuites(strArr);
    }

    public SSLParameters(String[] strArr, String[] strArr2) {
        this.sniNames = null;
        this.sniMatchers = null;
        this.applicationProtocols = new String[0];
        setCipherSuites(strArr);
        setProtocols(strArr2);
    }

    private static String[] clone(String[] strArr) {
        if (strArr == null) {
            return null;
        }
        return (String[]) strArr.clone();
    }

    public String[] getCipherSuites() {
        return clone(this.cipherSuites);
    }

    public void setCipherSuites(String[] strArr) {
        this.cipherSuites = clone(strArr);
    }

    public String[] getProtocols() {
        return clone(this.protocols);
    }

    public void setProtocols(String[] strArr) {
        this.protocols = clone(strArr);
    }

    public boolean getWantClientAuth() {
        return this.wantClientAuth;
    }

    public void setWantClientAuth(boolean z2) {
        this.wantClientAuth = z2;
        this.needClientAuth = false;
    }

    public boolean getNeedClientAuth() {
        return this.needClientAuth;
    }

    public void setNeedClientAuth(boolean z2) {
        this.wantClientAuth = false;
        this.needClientAuth = z2;
    }

    public AlgorithmConstraints getAlgorithmConstraints() {
        return this.algorithmConstraints;
    }

    public void setAlgorithmConstraints(AlgorithmConstraints algorithmConstraints) {
        this.algorithmConstraints = algorithmConstraints;
    }

    public String getEndpointIdentificationAlgorithm() {
        return this.identificationAlgorithm;
    }

    public void setEndpointIdentificationAlgorithm(String str) {
        this.identificationAlgorithm = str;
    }

    public final void setServerNames(List<SNIServerName> list) {
        if (list != null) {
            if (!list.isEmpty()) {
                this.sniNames = new LinkedHashMap(list.size());
                for (SNIServerName sNIServerName : list) {
                    if (this.sniNames.put(Integer.valueOf(sNIServerName.getType()), sNIServerName) != null) {
                        throw new IllegalArgumentException("Duplicated server name of type " + sNIServerName.getType());
                    }
                }
                return;
            }
            this.sniNames = Collections.emptyMap();
            return;
        }
        this.sniNames = null;
    }

    public final List<SNIServerName> getServerNames() {
        if (this.sniNames != null) {
            if (!this.sniNames.isEmpty()) {
                return Collections.unmodifiableList(new ArrayList(this.sniNames.values()));
            }
            return Collections.emptyList();
        }
        return null;
    }

    public final void setSNIMatchers(Collection<SNIMatcher> collection) {
        if (collection != null) {
            if (!collection.isEmpty()) {
                this.sniMatchers = new HashMap(collection.size());
                for (SNIMatcher sNIMatcher : collection) {
                    if (this.sniMatchers.put(Integer.valueOf(sNIMatcher.getType()), sNIMatcher) != null) {
                        throw new IllegalArgumentException("Duplicated server name of type " + sNIMatcher.getType());
                    }
                }
                return;
            }
            this.sniMatchers = Collections.emptyMap();
            return;
        }
        this.sniMatchers = null;
    }

    public final Collection<SNIMatcher> getSNIMatchers() {
        if (this.sniMatchers != null) {
            if (!this.sniMatchers.isEmpty()) {
                return Collections.unmodifiableList(new ArrayList(this.sniMatchers.values()));
            }
            return Collections.emptyList();
        }
        return null;
    }

    public final void setUseCipherSuitesOrder(boolean z2) {
        this.preferLocalCipherSuites = z2;
    }

    public final boolean getUseCipherSuitesOrder() {
        return this.preferLocalCipherSuites;
    }

    public String[] getApplicationProtocols() {
        return (String[]) this.applicationProtocols.clone();
    }

    public void setApplicationProtocols(String[] strArr) {
        if (strArr == null) {
            throw new IllegalArgumentException("protocols was null");
        }
        String[] strArr2 = (String[]) strArr.clone();
        for (String str : strArr2) {
            if (str == null || str.equals("")) {
                throw new IllegalArgumentException("An element of protocols was null/empty");
            }
        }
        this.applicationProtocols = strArr2;
    }
}
