package java.awt.font;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

/* loaded from: rt.jar:java/awt/font/TransformAttribute.class */
public final class TransformAttribute implements Serializable {
    private AffineTransform transform;
    public static final TransformAttribute IDENTITY = new TransformAttribute(null);
    static final long serialVersionUID = 3356247357827709530L;

    public TransformAttribute(AffineTransform affineTransform) {
        if (affineTransform != null && !affineTransform.isIdentity()) {
            this.transform = new AffineTransform(affineTransform);
        }
    }

    public AffineTransform getTransform() {
        AffineTransform affineTransform = this.transform;
        return affineTransform == null ? new AffineTransform() : new AffineTransform(affineTransform);
    }

    public boolean isIdentity() {
        return this.transform == null;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException {
        if (this.transform == null) {
            this.transform = new AffineTransform();
        }
        objectOutputStream.defaultWriteObject();
    }

    private Object readResolve() throws ObjectStreamException {
        if (this.transform == null || this.transform.isIdentity()) {
            return IDENTITY;
        }
        return this;
    }

    public int hashCode() {
        if (this.transform == null) {
            return 0;
        }
        return this.transform.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj != null) {
            try {
                TransformAttribute transformAttribute = (TransformAttribute) obj;
                if (this.transform == null) {
                    return transformAttribute.transform == null;
                }
                return this.transform.equals(transformAttribute.transform);
            } catch (ClassCastException e2) {
                return false;
            }
        }
        return false;
    }
}
