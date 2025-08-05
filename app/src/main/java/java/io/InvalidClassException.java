package java.io;

import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:java/io/InvalidClassException.class */
public class InvalidClassException extends ObjectStreamException {
    private static final long serialVersionUID = -4333316296251054416L;
    public String classname;

    public InvalidClassException(String str) {
        super(str);
    }

    public InvalidClassException(String str, String str2) {
        super(str2);
        this.classname = str;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        if (this.classname == null) {
            return super.getMessage();
        }
        return this.classname + VectorFormat.DEFAULT_SEPARATOR + super.getMessage();
    }
}
