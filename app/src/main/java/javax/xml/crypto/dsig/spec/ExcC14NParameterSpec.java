package javax.xml.crypto.dsig.spec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: rt.jar:javax/xml/crypto/dsig/spec/ExcC14NParameterSpec.class */
public final class ExcC14NParameterSpec implements C14NMethodParameterSpec {
    private List<String> preList;
    public static final String DEFAULT = "#default";

    public ExcC14NParameterSpec() {
        this.preList = Collections.emptyList();
    }

    public ExcC14NParameterSpec(List list) {
        if (list == null) {
            throw new NullPointerException("prefixList cannot be null");
        }
        ArrayList arrayList = new ArrayList(list);
        int size = arrayList.size();
        for (int i2 = 0; i2 < size; i2++) {
            if (!(arrayList.get(i2) instanceof String)) {
                throw new ClassCastException("not a String");
            }
        }
        this.preList = Collections.unmodifiableList(arrayList);
    }

    public List getPrefixList() {
        return this.preList;
    }
}
