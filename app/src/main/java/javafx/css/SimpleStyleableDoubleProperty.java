package javafx.css;

import javafx.beans.NamedArg;
import javax.swing.JOptionPane;

/* loaded from: jfxrt.jar:javafx/css/SimpleStyleableDoubleProperty.class */
public class SimpleStyleableDoubleProperty extends StyleableDoubleProperty {
    private static final Object DEFAULT_BEAN = null;
    private static final String DEFAULT_NAME = "";
    private final Object bean;
    private final String name;
    private final CssMetaData<? extends Styleable, Number> cssMetaData;

    public SimpleStyleableDoubleProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, Number> cssMetaData) {
        this(cssMetaData, DEFAULT_BEAN, "");
    }

    public SimpleStyleableDoubleProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, Number> cssMetaData, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) Double initialValue) {
        this(cssMetaData, DEFAULT_BEAN, "", initialValue);
    }

    public SimpleStyleableDoubleProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, Number> cssMetaData, @NamedArg("bean") Object bean, @NamedArg("name") String name) {
        this.bean = bean;
        this.name = name == null ? "" : name;
        this.cssMetaData = cssMetaData;
    }

    public SimpleStyleableDoubleProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, Number> cssMetaData, @NamedArg("bean") Object bean, @NamedArg("name") String name, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) Double initialValue) {
        super(initialValue.doubleValue());
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
    public final CssMetaData<? extends Styleable, Number> getCssMetaData() {
        return this.cssMetaData;
    }
}
