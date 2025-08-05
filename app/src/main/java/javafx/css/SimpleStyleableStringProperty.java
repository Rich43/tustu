package javafx.css;

import javafx.beans.NamedArg;
import javax.swing.JOptionPane;

/* loaded from: jfxrt.jar:javafx/css/SimpleStyleableStringProperty.class */
public class SimpleStyleableStringProperty extends StyleableStringProperty {
    private static final Object DEFAULT_BEAN = null;
    private static final String DEFAULT_NAME = "";
    private final Object bean;
    private final String name;
    private final CssMetaData<? extends Styleable, String> cssMetaData;

    public SimpleStyleableStringProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, String> cssMetaData) {
        this(cssMetaData, DEFAULT_BEAN, "");
    }

    public SimpleStyleableStringProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, String> cssMetaData, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) String initialValue) {
        this(cssMetaData, DEFAULT_BEAN, "", initialValue);
    }

    public SimpleStyleableStringProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, String> cssMetaData, @NamedArg("bean") Object bean, @NamedArg("name") String name) {
        this.bean = bean;
        this.name = name == null ? "" : name;
        this.cssMetaData = cssMetaData;
    }

    public SimpleStyleableStringProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, String> cssMetaData, @NamedArg("bean") Object bean, @NamedArg("name") String name, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) String initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = name == null ? "" : name;
        this.cssMetaData = cssMetaData;
    }

    @Override // javafx.beans.property.ReadOnlyProperty
    public Object getBean() {
        return this.bean;
    }

    @Override // javafx.beans.property.ReadOnlyProperty
    public String getName() {
        return this.name;
    }

    @Override // javafx.css.StyleableProperty
    public final CssMetaData<? extends Styleable, String> getCssMetaData() {
        return this.cssMetaData;
    }
}
