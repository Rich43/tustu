package javax.accessibility;

/* loaded from: rt.jar:javax/accessibility/AccessibleHypertext.class */
public interface AccessibleHypertext extends AccessibleText {
    int getLinkCount();

    AccessibleHyperlink getLink(int i2);

    int getLinkIndex(int i2);
}
