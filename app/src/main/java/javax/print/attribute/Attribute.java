package javax.print.attribute;

import java.io.Serializable;

/* loaded from: rt.jar:javax/print/attribute/Attribute.class */
public interface Attribute extends Serializable {
    Class<? extends Attribute> getCategory();

    String getName();
}
