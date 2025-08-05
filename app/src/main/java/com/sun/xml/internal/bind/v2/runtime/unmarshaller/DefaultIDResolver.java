package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.xml.internal.bind.IDResolver;
import java.util.HashMap;
import java.util.concurrent.Callable;
import javax.xml.bind.ValidationEventHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/DefaultIDResolver.class */
final class DefaultIDResolver extends IDResolver {
    private HashMap<String, Object> idmap = null;

    DefaultIDResolver() {
    }

    @Override // com.sun.xml.internal.bind.IDResolver
    public void startDocument(ValidationEventHandler eventHandler) throws SAXException {
        if (this.idmap != null) {
            this.idmap.clear();
        }
    }

    @Override // com.sun.xml.internal.bind.IDResolver
    public void bind(String id, Object obj) {
        if (this.idmap == null) {
            this.idmap = new HashMap<>();
        }
        this.idmap.put(id, obj);
    }

    @Override // com.sun.xml.internal.bind.IDResolver
    public Callable resolve(final String id, Class targetType) {
        return new Callable() { // from class: com.sun.xml.internal.bind.v2.runtime.unmarshaller.DefaultIDResolver.1
            @Override // java.util.concurrent.Callable
            public Object call() throws Exception {
                if (DefaultIDResolver.this.idmap == null) {
                    return null;
                }
                return DefaultIDResolver.this.idmap.get(id);
            }
        };
    }
}
