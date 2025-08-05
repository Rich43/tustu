package javax.swing.text;

/* loaded from: rt.jar:javax/swing/text/Element.class */
public interface Element {
    Document getDocument();

    Element getParentElement();

    String getName();

    AttributeSet getAttributes();

    int getStartOffset();

    int getEndOffset();

    int getElementIndex(int i2);

    int getElementCount();

    Element getElement(int i2);

    boolean isLeaf();
}
