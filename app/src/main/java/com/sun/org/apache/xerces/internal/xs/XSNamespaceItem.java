package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSNamespaceItem.class */
public interface XSNamespaceItem {
    String getSchemaNamespace();

    XSNamedMap getComponents(short s2);

    XSObjectList getAnnotations();

    XSElementDeclaration getElementDeclaration(String str);

    XSAttributeDeclaration getAttributeDeclaration(String str);

    XSTypeDefinition getTypeDefinition(String str);

    XSAttributeGroupDefinition getAttributeGroup(String str);

    XSModelGroupDefinition getModelGroupDefinition(String str);

    XSNotationDeclaration getNotationDeclaration(String str);

    StringList getDocumentLocations();
}
