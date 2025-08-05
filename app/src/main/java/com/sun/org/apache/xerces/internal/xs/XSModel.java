package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSModel.class */
public interface XSModel {
    StringList getNamespaces();

    XSNamespaceItemList getNamespaceItems();

    XSNamedMap getComponents(short s2);

    XSNamedMap getComponentsByNamespace(short s2, String str);

    XSObjectList getAnnotations();

    XSElementDeclaration getElementDeclaration(String str, String str2);

    XSAttributeDeclaration getAttributeDeclaration(String str, String str2);

    XSTypeDefinition getTypeDefinition(String str, String str2);

    XSAttributeGroupDefinition getAttributeGroup(String str, String str2);

    XSModelGroupDefinition getModelGroupDefinition(String str, String str2);

    XSNotationDeclaration getNotationDeclaration(String str, String str2);

    XSObjectList getSubstitutionGroup(XSElementDeclaration xSElementDeclaration);
}
