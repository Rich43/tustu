package com.sun.org.apache.xerces.internal.impl.dv;

import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/DatatypeException.class */
public class DatatypeException extends Exception {
    static final long serialVersionUID = 1940805832730465578L;
    protected String key;
    protected Object[] args;

    public DatatypeException(String key, Object[] args) {
        super(key);
        this.key = key;
        this.args = args;
    }

    public String getKey() {
        return this.key;
    }

    public Object[] getArgs() {
        return this.args;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        ResourceBundle resourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLSchemaMessages");
        if (resourceBundle == null) {
            throw new MissingResourceException("Property file not found!", "com.sun.org.apache.xerces.internal.impl.msg.XMLSchemaMessages", this.key);
        }
        String msg = resourceBundle.getString(this.key);
        if (msg == null) {
            throw new MissingResourceException(resourceBundle.getString("BadMessageKey"), "com.sun.org.apache.xerces.internal.impl.msg.XMLSchemaMessages", this.key);
        }
        if (this.args != null) {
            try {
                msg = MessageFormat.format(msg, this.args);
            } catch (Exception e2) {
                msg = resourceBundle.getString("FormatFailed") + " " + resourceBundle.getString(this.key);
            }
        }
        return msg;
    }
}
