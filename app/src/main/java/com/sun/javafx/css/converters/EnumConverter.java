package com.sun.javafx.css.converters;

import com.sun.javafx.css.StringStore;
import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.cursor.CursorType;
import com.sun.javafx.util.Logging;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.Enum;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BlurType;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:com/sun/javafx/css/converters/EnumConverter.class */
public final class EnumConverter<E extends Enum<E>> extends StyleConverterImpl<String, E> {
    final Class<E> enumClass;
    private static Map<String, StyleConverter<?, ?>> converters;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !EnumConverter.class.desiredAssertionStatus();
    }

    public EnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override // javafx.css.StyleConverter
    public E convert(ParsedValue<String, E> parsedValue, Font font) {
        if (this.enumClass == null) {
            return null;
        }
        String value = parsedValue.getValue();
        int iLastIndexOf = value.lastIndexOf(46);
        if (iLastIndexOf > -1) {
            value = value.substring(iLastIndexOf + 1);
        }
        try {
            value = value.replace('-', '_');
            return (E) Enum.valueOf(this.enumClass, value.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e2) {
            return (E) Enum.valueOf(this.enumClass, value);
        }
    }

    @Override // com.sun.javafx.css.StyleConverterImpl
    public void writeBinary(DataOutputStream os, StringStore sstore) throws IOException {
        super.writeBinary(os, sstore);
        String ename = this.enumClass.getName();
        int index = sstore.addString(ename);
        os.writeShort(index);
    }

    public static StyleConverter<?, ?> readBinary(DataInputStream is, String[] strings) throws IOException {
        short index = is.readShort();
        String ename = (0 > index || index > strings.length) ? null : strings[index];
        if (ename == null || ename.isEmpty()) {
            return null;
        }
        if (converters == null || !converters.containsKey(ename)) {
            StyleConverter<?, ?> converter = getInstance(ename);
            if (converter == null) {
                PlatformLogger logger = Logging.getCSSLogger();
                if (logger.isLoggable(PlatformLogger.Level.SEVERE)) {
                    logger.severe("could not deserialize EnumConverter for " + ename);
                }
            }
            if (converters == null) {
                converters = new HashMap();
            }
            converters.put(ename, converter);
            return converter;
        }
        return converters.get(ename);
    }

    public static StyleConverter<?, ?> getInstance(String ename) {
        StyleConverter<?, ?> converter;
        converter = null;
        switch (ename) {
            case "com.sun.javafx.cursor.CursorType":
                converter = new EnumConverter<>(CursorType.class);
                break;
            case "javafx.scene.layout.BackgroundRepeat":
            case "com.sun.javafx.scene.layout.region.Repeat":
                converter = new EnumConverter<>(BackgroundRepeat.class);
                break;
            case "javafx.geometry.HPos":
                converter = new EnumConverter<>(HPos.class);
                break;
            case "javafx.geometry.Orientation":
                converter = new EnumConverter<>(Orientation.class);
                break;
            case "javafx.geometry.Pos":
                converter = new EnumConverter<>(Pos.class);
                break;
            case "javafx.geometry.Side":
                converter = new EnumConverter<>(Side.class);
                break;
            case "javafx.geometry.VPos":
                converter = new EnumConverter<>(VPos.class);
                break;
            case "javafx.scene.effect.BlendMode":
                converter = new EnumConverter<>(BlendMode.class);
                break;
            case "javafx.scene.effect.BlurType":
                converter = new EnumConverter<>(BlurType.class);
                break;
            case "javafx.scene.paint.CycleMethod":
                converter = new EnumConverter<>(CycleMethod.class);
                break;
            case "javafx.scene.shape.ArcType":
                converter = new EnumConverter<>(ArcType.class);
                break;
            case "javafx.scene.shape.StrokeLineCap":
                converter = new EnumConverter<>(StrokeLineCap.class);
                break;
            case "javafx.scene.shape.StrokeLineJoin":
                converter = new EnumConverter<>(StrokeLineJoin.class);
                break;
            case "javafx.scene.shape.StrokeType":
                converter = new EnumConverter<>(StrokeType.class);
                break;
            case "javafx.scene.text.FontPosture":
                converter = new EnumConverter<>(FontPosture.class);
                break;
            case "javafx.scene.text.FontSmoothingType":
                converter = new EnumConverter<>(FontSmoothingType.class);
                break;
            case "javafx.scene.text.FontWeight":
                converter = new EnumConverter<>(FontWeight.class);
                break;
            case "javafx.scene.text.TextAlignment":
                converter = new EnumConverter<>(TextAlignment.class);
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError((Object) ("EnumConverter<" + ename + "> not expected"));
                }
                PlatformLogger logger = Logging.getCSSLogger();
                if (logger.isLoggable(PlatformLogger.Level.SEVERE)) {
                    logger.severe("EnumConverter : converter Class is null for : " + ename);
                    break;
                }
                break;
        }
        return converter;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || !(other instanceof EnumConverter)) {
            return false;
        }
        return this.enumClass.equals(((EnumConverter) other).enumClass);
    }

    public int hashCode() {
        return this.enumClass.hashCode();
    }

    public String toString() {
        return "EnumConveter[" + this.enumClass.getName() + "]";
    }
}
