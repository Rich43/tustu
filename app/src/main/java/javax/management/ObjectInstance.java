package javax.management;

import java.io.Serializable;

/* loaded from: rt.jar:javax/management/ObjectInstance.class */
public class ObjectInstance implements Serializable {
    private static final long serialVersionUID = -4099952623687795850L;
    private ObjectName name;
    private String className;

    public ObjectInstance(String str, String str2) throws MalformedObjectNameException {
        this(new ObjectName(str), str2);
    }

    public ObjectInstance(ObjectName objectName, String str) {
        if (objectName.isPattern()) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid name->" + objectName.toString()));
        }
        this.name = objectName;
        this.className = str;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ObjectInstance)) {
            return false;
        }
        ObjectInstance objectInstance = (ObjectInstance) obj;
        if (!this.name.equals(objectInstance.getObjectName())) {
            return false;
        }
        if (this.className == null) {
            return objectInstance.getClassName() == null;
        }
        return this.className.equals(objectInstance.getClassName());
    }

    public int hashCode() {
        return this.name.hashCode() ^ (this.className == null ? 0 : this.className.hashCode());
    }

    public ObjectName getObjectName() {
        return this.name;
    }

    public String getClassName() {
        return this.className;
    }

    public String toString() {
        return getClassName() + "[" + ((Object) getObjectName()) + "]";
    }
}
