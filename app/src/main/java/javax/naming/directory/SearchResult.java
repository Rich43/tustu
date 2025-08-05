package javax.naming.directory;

import javax.naming.Binding;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:javax/naming/directory/SearchResult.class */
public class SearchResult extends Binding {
    private Attributes attrs;
    private static final long serialVersionUID = -9158063327699723172L;

    public SearchResult(String str, Object obj, Attributes attributes) {
        super(str, obj);
        this.attrs = attributes;
    }

    public SearchResult(String str, Object obj, Attributes attributes, boolean z2) {
        super(str, obj, z2);
        this.attrs = attributes;
    }

    public SearchResult(String str, String str2, Object obj, Attributes attributes) {
        super(str, str2, obj);
        this.attrs = attributes;
    }

    public SearchResult(String str, String str2, Object obj, Attributes attributes, boolean z2) {
        super(str, str2, obj, z2);
        this.attrs = attributes;
    }

    public Attributes getAttributes() {
        return this.attrs;
    }

    public void setAttributes(Attributes attributes) {
        this.attrs = attributes;
    }

    @Override // javax.naming.Binding, javax.naming.NameClassPair
    public String toString() {
        return super.toString() + CallSiteDescriptor.TOKEN_DELIMITER + ((Object) getAttributes());
    }
}
