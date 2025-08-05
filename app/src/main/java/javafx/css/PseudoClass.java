package javafx.css;

import com.sun.javafx.css.PseudoClassState;

/* loaded from: jfxrt.jar:javafx/css/PseudoClass.class */
public abstract class PseudoClass {
    public abstract String getPseudoClassName();

    public static PseudoClass getPseudoClass(String pseudoClass) {
        return PseudoClassState.getPseudoClass(pseudoClass);
    }
}
