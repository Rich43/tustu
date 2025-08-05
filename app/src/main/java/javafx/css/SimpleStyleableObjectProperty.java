package javafx.css;

import javafx.beans.NamedArg;
import javax.swing.JOptionPane;

/* loaded from: jfxrt.jar:javafx/css/SimpleStyleableObjectProperty.class */
public class SimpleStyleableObjectProperty<T> extends StyleableObjectProperty<T> {
    private static final Object DEFAULT_BEAN = null;
    private static final String DEFAULT_NAME = "";
    private final Object bean;
    private final String name;
    private final CssMetaData<? extends Styleable, T> cssMetaData;

    public SimpleStyleableObjectProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, T> cssMetaData) {
        this(cssMetaData, DEFAULT_BEAN, "");
    }

    public SimpleStyleableObjectProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, T> cssMetaData, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) T initialValue) {
        this(cssMetaData, DEFAULT_BEAN, "", initialValue);
    }

    public SimpleStyleableObjectProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, T> cssMetaData, @NamedArg("bean") Object bean, @NamedArg("name") String name) {
        this.bean = bean;
        this.name = name == null ? "" : name;
        this.cssMetaData = cssMetaData;
    }

    public SimpleStyleableObjectProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, T> cssMetaData, @NamedArg("bean") Object bean, @NamedArg("name") String name, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) T initialValue) {
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
    public final CssMetaData<? extends Styleable, T> getCssMetaData() {
        return this.cssMetaData;
    }
}
