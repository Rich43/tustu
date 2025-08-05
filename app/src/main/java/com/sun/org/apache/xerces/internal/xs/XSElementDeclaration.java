package com.sun.org.apache.xerces.internal.xs;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xs/XSElementDeclaration.class */
public interface XSElementDeclaration extends XSTerm {
    XSTypeDefinition getTypeDefinition();

    short getScope();

    XSComplexTypeDefinition getEnclosingCTDefinition();

    short getConstraintType();

    String getConstraintValue();

    Object getActualVC() throws XSException;

    short getActualVCType() throws XSException;

    ShortList getItemValueTypes() throws XSException;

    boolean getNillable();

    XSNamedMap getIdentityConstraints();

    XSElementDeclaration getSubstitutionGroupAffiliation();

    boolean isSubstitutionGroupExclusion(short s2);

    short getSubstitutionGroupExclusions();

    boolean isDisallowedSubstitution(short s2);

    short getDisallowedSubstitutions();

    boolean getAbstract();

    XSAnnotation getAnnotation();

    XSObjectList getAnnotations();
}
