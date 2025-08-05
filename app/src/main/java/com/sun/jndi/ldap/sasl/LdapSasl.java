package com.sun.jndi.ldap.sasl;

import com.sun.jndi.ldap.Connection;
import com.sun.jndi.ldap.LdapClient;
import com.sun.jndi.ldap.LdapResult;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.naming.AuthenticationException;
import javax.naming.AuthenticationNotSupportedException;
import javax.naming.NamingException;
import javax.naming.ldap.Control;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

/* loaded from: rt.jar:com/sun/jndi/ldap/sasl/LdapSasl.class */
public final class LdapSasl {
    private static final String SASL_CALLBACK = "java.naming.security.sasl.callback";
    private static final String SASL_AUTHZ_ID = "java.naming.security.sasl.authorizationId";
    private static final String SASL_REALM = "java.naming.security.sasl.realm";
    private static final int LDAP_SUCCESS = 0;
    private static final int LDAP_SASL_BIND_IN_PROGRESS = 14;
    private static final byte[] NO_BYTES = new byte[0];

    private LdapSasl() {
    }

    public static LdapResult saslBind(LdapClient ldapClient, Connection connection, String str, String str2, Object obj, String str3, Hashtable<?, ?> hashtable, Control[] controlArr) throws IOException, NamingException {
        boolean z2 = false;
        CallbackHandler defaultCallbackHandler = hashtable != null ? (CallbackHandler) hashtable.get(SASL_CALLBACK) : null;
        if (defaultCallbackHandler == null) {
            defaultCallbackHandler = new DefaultCallbackHandler(str2, obj, (String) hashtable.get(SASL_REALM));
            z2 = true;
        }
        try {
            try {
                SaslClient saslClientCreateSaslClient = Sasl.createSaslClient(getSaslMechanismNames(str3), hashtable != null ? (String) hashtable.get(SASL_AUTHZ_ID) : null, "ldap", str, hashtable, defaultCallbackHandler);
                if (saslClientCreateSaslClient == null) {
                    throw new AuthenticationNotSupportedException(str3);
                }
                String mechanismName = saslClientCreateSaslClient.getMechanismName();
                LdapResult ldapResultLdapBind = ldapClient.ldapBind(null, saslClientCreateSaslClient.hasInitialResponse() ? saslClientCreateSaslClient.evaluateChallenge(NO_BYTES) : null, controlArr, mechanismName, true);
                while (true) {
                    if (saslClientCreateSaslClient.isComplete() || !(ldapResultLdapBind.status == 14 || ldapResultLdapBind.status == 0)) {
                        break;
                    }
                    byte[] bArrEvaluateChallenge = saslClientCreateSaslClient.evaluateChallenge(ldapResultLdapBind.serverCreds != null ? ldapResultLdapBind.serverCreds : NO_BYTES);
                    if (ldapResultLdapBind.status == 0) {
                        if (bArrEvaluateChallenge != null) {
                            throw new AuthenticationException("SASL client generated response after success");
                        }
                    } else {
                        ldapResultLdapBind = ldapClient.ldapBind(null, bArrEvaluateChallenge, controlArr, mechanismName, true);
                    }
                }
                if (ldapResultLdapBind.status == 0) {
                    if (!saslClientCreateSaslClient.isComplete()) {
                        throw new AuthenticationException("SASL authentication not complete despite server claims");
                    }
                    String str4 = (String) saslClientCreateSaslClient.getNegotiatedProperty(Sasl.QOP);
                    if (str4 != null && (str4.equalsIgnoreCase("auth-int") || str4.equalsIgnoreCase("auth-conf"))) {
                        connection.replaceStreams(new SaslInputStream(saslClientCreateSaslClient, connection.inStream), new SaslOutputStream(saslClientCreateSaslClient, connection.outStream));
                    } else {
                        saslClientCreateSaslClient.dispose();
                    }
                }
                return ldapResultLdapBind;
            } catch (SaslException e2) {
                AuthenticationException authenticationException = new AuthenticationException(str3);
                authenticationException.setRootCause(e2);
                throw authenticationException;
            }
        } finally {
            if (z2) {
                ((DefaultCallbackHandler) defaultCallbackHandler).clearPassword();
            }
        }
    }

    private static String[] getSaslMechanismNames(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        Vector vector = new Vector(10);
        while (stringTokenizer.hasMoreTokens()) {
            vector.addElement(stringTokenizer.nextToken());
        }
        String[] strArr = new String[vector.size()];
        for (int i2 = 0; i2 < vector.size(); i2++) {
            strArr[i2] = (String) vector.elementAt(i2);
        }
        return strArr;
    }
}
