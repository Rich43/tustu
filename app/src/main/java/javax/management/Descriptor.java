package javax.management;

import java.io.Serializable;

/* loaded from: rt.jar:javax/management/Descriptor.class */
public interface Descriptor extends Serializable, Cloneable {
    Object getFieldValue(String str) throws RuntimeOperationsException;

    void setField(String str, Object obj) throws RuntimeOperationsException;

    String[] getFields();

    String[] getFieldNames();

    Object[] getFieldValues(String... strArr);

    void removeField(String str);

    void setFields(String[] strArr, Object[] objArr) throws RuntimeOperationsException;

    Object clone() throws RuntimeOperationsException;

    boolean isValid() throws RuntimeOperationsException;

    boolean equals(Object obj);

    int hashCode();
}
