package javax.naming;

import java.io.Serializable;
import java.util.Enumeration;

/* loaded from: rt.jar:javax/naming/Name.class */
public interface Name extends Cloneable, Serializable, Comparable<Object> {
    public static final long serialVersionUID = -3617482732056931635L;

    Object clone();

    int compareTo(Object obj);

    int size();

    boolean isEmpty();

    Enumeration<String> getAll();

    String get(int i2);

    Name getPrefix(int i2);

    Name getSuffix(int i2);

    boolean startsWith(Name name);

    boolean endsWith(Name name);

    Name addAll(Name name) throws InvalidNameException;

    Name addAll(int i2, Name name) throws InvalidNameException;

    Name add(String str) throws InvalidNameException;

    Name add(int i2, String str) throws InvalidNameException;

    Object remove(int i2) throws InvalidNameException;
}
