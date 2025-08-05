package javafx.css;

import com.sun.javafx.css.converters.FontConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.css.converters.StringConverter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.css.Styleable;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/* loaded from: jfxrt.jar:javafx/css/FontCssMetaData.class */
public abstract class FontCssMetaData<S extends Styleable> extends CssMetaData<S, Font> {
    public FontCssMetaData(String property, Font initial) {
        super(property, FontConverter.getInstance(), initial, true, createSubProperties(property, initial));
    }

    private static <S extends Styleable> List<CssMetaData<? extends Styleable, ?>> createSubProperties(String property, Font initial) {
        ArrayList arrayList = new ArrayList();
        Font defaultFont = initial != null ? initial : Font.getDefault();
        arrayList.add(new CssMetaData<S, String>(property.concat("-family"), StringConverter.getInstance(), defaultFont.getFamily(), true) { // from class: javafx.css.FontCssMetaData.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(S styleable) {
                return false;
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<String> getStyleableProperty(S styleable) {
                return null;
            }
        });
        arrayList.add(new CssMetaData<S, Number>(property.concat("-size"), SizeConverter.getInstance(), Double.valueOf(defaultFont.getSize()), true) { // from class: javafx.css.FontCssMetaData.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(S styleable) {
                return false;
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Number> getStyleableProperty(S styleable) {
                return null;
            }
        });
        arrayList.add(new CssMetaData<S, FontPosture>(property.concat("-style"), FontConverter.FontStyleConverter.getInstance(), FontPosture.REGULAR, true) { // from class: javafx.css.FontCssMetaData.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(S styleable) {
                return false;
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<FontPosture> getStyleableProperty(S styleable) {
                return null;
            }
        });
        arrayList.add(new CssMetaData<S, FontWeight>(property.concat("-weight"), FontConverter.FontWeightConverter.getInstance(), FontWeight.NORMAL, true) { // from class: javafx.css.FontCssMetaData.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(S styleable) {
                return false;
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<FontWeight> getStyleableProperty(S styleable) {
                return null;
            }
        });
        return Collections.unmodifiableList(arrayList);
    }
}
