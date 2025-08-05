package javax.xml.crypto.dsig.spec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: rt.jar:javax/xml/crypto/dsig/spec/XPathFilter2ParameterSpec.class */
public final class XPathFilter2ParameterSpec implements TransformParameterSpec {
    private final List<XPathType> xPathList;

    public XPathFilter2ParameterSpec(List list) {
        if (list == null) {
            throw new NullPointerException("xPathList cannot be null");
        }
        ArrayList arrayList = new ArrayList(list);
        if (arrayList.isEmpty()) {
            throw new IllegalArgumentException("xPathList cannot be empty");
        }
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (!(arrayList.get(i2) instanceof XPathType)) {
                throw new ClassCastException("xPathList[" + i2 + "] is not a valid type");
            }
        }
        this.xPathList = Collections.unmodifiableList(arrayList);
    }

    public List getXPathList() {
        return this.xPathList;
    }
}
