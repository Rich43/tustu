package com.sun.xml.internal.ws.util;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.server.SDDocument;
import com.sun.xml.internal.ws.wsdl.SDDocumentResolver;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/MetadataUtil.class */
public class MetadataUtil {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MetadataUtil.class.desiredAssertionStatus();
    }

    public static Map<String, SDDocument> getMetadataClosure(@NotNull String systemId, @NotNull SDDocumentResolver resolver, boolean onlyTopLevelSchemas) {
        Map<String, SDDocument> closureDocs = new HashMap<>();
        Set<String> remaining = new HashSet<>();
        remaining.add(systemId);
        while (!remaining.isEmpty()) {
            Iterator<String> it = remaining.iterator();
            String current = it.next();
            remaining.remove(current);
            SDDocument currentDoc = resolver.resolve(current);
            SDDocument old = closureDocs.put(currentDoc.getURL().toExternalForm(), currentDoc);
            if (!$assertionsDisabled && old != null) {
                throw new AssertionError();
            }
            Set<String> imports = currentDoc.getImports();
            if (!currentDoc.isSchema() || !onlyTopLevelSchemas) {
                for (String importedDoc : imports) {
                    if (closureDocs.get(importedDoc) == null) {
                        remaining.add(importedDoc);
                    }
                }
            }
        }
        return closureDocs;
    }
}
