package javafx.css;

import javafx.beans.NamedArg;
import javax.swing.JOptionPane;

/* loaded from: jfxrt.jar:javafx/css/SimpleStyleableLongProperty.class */
public class SimpleStyleableLongProperty extends StyleableLongProperty {
    private static final Object DEFAULT_BEAN = null;
    private static final String DEFAULT_NAME = "";
    private final Object bean;
    private final String name;
    private final CssMetaData<? extends Styleable, Number> cssMetaData;

    public SimpleStyleableLongProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, Number> cssMetaData) {
        this(cssMetaData, DEFAULT_BEAN, "");
    }

    public SimpleStyleableLongProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, Number> cssMetaData, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) Long initialValue) {
        this(cssMetaData, DEFAULT_BEAN, "", initialValue);
    }

    public SimpleStyleableLongProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, Number> cssMetaData, @NamedArg("bean") Object bean, @NamedArg("name") String name) {
        this.bean = bean;
        this.name = name == null ? "" : name;
        this.cssMetaData = cssMetaData;
    }

    public SimpleStyleableLongProperty(@NamedArg("cssMetaData") CssMetaData<? extends Styleable, Number> cssMetaData, @NamedArg("bean") Object bean, @NamedArg("name") String name, @NamedArg(JOptionPane.INITIAL_VALUE_PROPERTY) Long initialValue) {
        super(initialValue.longValue());
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
