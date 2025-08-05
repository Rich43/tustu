package javax.sql.rowset.serial;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: rt.jar:javax/sql/rowset/serial/SerialDatalink.class */
public class SerialDatalink implements Serializable, Cloneable {
    private URL url;
    private int baseType;
    private String baseTypeName;
    static final long serialVersionUID = 2826907821828733626L;

    public SerialDatalink(URL url) throws SerialException {
        if (url == null) {
            throw new SerialException("Cannot serialize empty URL instance");
        }
        this.url = url;
    }

    public URL getDatalink() throws SerialException {
        try {
            return new URL(this.url.toString());
        } catch (MalformedURLException e2) {
            throw new SerialException("MalformedURLException: " + e2.getMessage());
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof SerialDatalink) {
            return this.url.equals(((SerialDatalink) obj).url);
        }
        return false;
    }

    public int hashCode() {
        return 31 + this.url.hashCode();
    }

    public Object clone() {
        try {
            return (SerialDatalink) super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError();
        }
    }
}
