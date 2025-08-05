package com.sun.jndi.ldap.sasl;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.sasl.RealmCallback;
import javax.security.sasl.RealmChoiceCallback;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:com/sun/jndi/ldap/sasl/DefaultCallbackHandler.class */
final class DefaultCallbackHandler implements CallbackHandler {
    private char[] passwd;
    private String authenticationID;
    private String authRealm;

    DefaultCallbackHandler(String str, Object obj, String str2) throws IOException {
        this.authenticationID = str;
        this.authRealm = str2;
        if (obj instanceof String) {
            this.passwd = ((String) obj).toCharArray();
        } else if (obj instanceof char[]) {
            this.passwd = (char[]) ((char[]) obj).clone();
        } else if (obj != null) {
            this.passwd = new String((byte[]) obj, InternalZipConstants.CHARSET_UTF8).toCharArray();
        }
    }

    @Override // javax.security.auth.callback.CallbackHandler
    public void handle(Callback[] callbackArr) throws UnsupportedCallbackException, IOException {
        for (int i2 = 0; i2 < callbackArr.length; i2++) {
            if (callbackArr[i2] instanceof NameCallback) {
                ((NameCallback) callbackArr[i2]).setName(this.authenticationID);
            } else if (callbackArr[i2] instanceof PasswordCallback) {
                ((PasswordCallback) callbackArr[i2]).setPassword(this.passwd);
            } else if (callbackArr[i2] instanceof RealmChoiceCallback) {
                String[] choices = ((RealmChoiceCallback) callbackArr[i2]).getChoices();
                int i3 = 0;
                if (this.authRealm != null && this.authRealm.length() > 0) {
                    i3 = -1;
                    for (int i4 = 0; i4 < choices.length; i4++) {
                        if (choices[i4].equals(this.authRealm)) {
                            i3 = i4;
                        }
                    }
                    if (i3 == -1) {
                        StringBuffer stringBuffer = new StringBuffer();
                        for (String str : choices) {
                            stringBuffer.append(str + ",");
                        }
                        throw new IOException("Cannot match 'java.naming.security.sasl.realm' property value, '" + this.authRealm + "' with choices " + ((Object) stringBuffer) + "in RealmChoiceCallback");
                    }
                }
                ((RealmChoiceCallback) callbackArr[i2]).setSelectedIndex(i3);
            } else if (callbackArr[i2] instanceof RealmCallback) {
                RealmCallback realmCallback = (RealmCallback) callbackArr[i2];
                if (this.authRealm != null) {
                    realmCallback.setText(this.authRealm);
                } else {
                    String defaultText = realmCallback.getDefaultText();
                    if (defaultText != null) {
                        realmCallback.setText(defaultText);
                    } else {
                        realmCallback.setText("");
                    }
                }
            } else {
                throw new UnsupportedCallbackException(callbackArr[i2]);
            }
        }
    }

    void clearPassword() {
        if (this.passwd != null) {
            for (int i2 = 0; i2 < this.passwd.length; i2++) {
                this.passwd[i2] = 0;
            }
            this.passwd = null;
        }
    }

    protected void finalize() throws Throwable {
        clearPassword();
    }
}
