package java.beans;

/* loaded from: rt.jar:java/beans/Visibility.class */
public interface Visibility {
    boolean needsGui();

    void dontUseGui();

    void okToUseGui();

    boolean avoidingGui();
}
