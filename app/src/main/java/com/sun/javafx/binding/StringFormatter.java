package com.sun.javafx.binding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;

/* loaded from: jfxrt.jar:com/sun/javafx/binding/StringFormatter.class */
public abstract class StringFormatter extends StringBinding {
    /* JADX INFO: Access modifiers changed from: private */
    public static Object extractValue(Object obj) {
        return obj instanceof ObservableValue ? ((ObservableValue) obj).getValue2() : obj;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object[] extractValues(Object[] objs) {
        int n2 = objs.length;
        Object[] values = new Object[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            values[i2] = extractValue(objs[i2]);
        }
        return values;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static ObservableValue<?>[] extractDependencies(Object... args) {
        List<ObservableValue<?>> dependencies = new ArrayList<>();
        for (Object obj : args) {
            if (obj instanceof ObservableValue) {
                dependencies.add((ObservableValue) obj);
            }
        }
        return (ObservableValue[]) dependencies.toArray(new ObservableValue[dependencies.size()]);
    }

    public static StringExpression convert(final ObservableValue<?> observableValue) {
        if (observableValue == null) {
            throw new NullPointerException("ObservableValue must be specified");
        }
        if (observableValue instanceof StringExpression) {
            return (StringExpression) observableValue;
        }
        return new StringBinding() { // from class: com.sun.javafx.binding.StringFormatter.1
            {
                super.bind(observableValue);
            }

            @Override // javafx.beans.binding.StringBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(observableValue);
            }

            @Override // javafx.beans.binding.StringBinding
            protected String computeValue() {
                Object value = observableValue.getValue2();
                return value == null ? FXMLLoader.NULL_KEYWORD : value.toString();
            }

            @Override // javafx.beans.binding.StringBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableValue<?>> getDependencies() {
                return FXCollections.singletonObservableList(observableValue);
            }
        };
    }

    public static StringExpression concat(final Object... args) {
        if (args == null || args.length == 0) {
            return StringConstant.valueOf("");
        }
        if (args.length == 1) {
            Object cur = args[0];
            return cur instanceof ObservableValue ? convert((ObservableValue) cur) : StringConstant.valueOf(cur.toString());
        }
        if (extractDependencies(args).length == 0) {
            StringBuilder builder = new StringBuilder();
            for (Object obj : args) {
                builder.append(obj);
            }
            return StringConstant.valueOf(builder.toString());
        }
        return new StringFormatter() { // from class: com.sun.javafx.binding.StringFormatter.2
            {
                super.bind(StringFormatter.extractDependencies(args));
            }

            @Override // javafx.beans.binding.StringBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(StringFormatter.extractDependencies(args));
            }

            @Override // javafx.beans.binding.StringBinding
            protected String computeValue() {
                StringBuilder builder2 = new StringBuilder();
                for (Object obj2 : args) {
                    builder2.append(StringFormatter.extractValue(obj2));
                }
                return builder2.toString();
            }

            @Override // javafx.beans.binding.StringBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableValue<?>> getDependencies() {
                return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(StringFormatter.extractDependencies(args)));
            }
        };
    }

    public static StringExpression format(final Locale locale, final String format, final Object... args) {
        if (format == null) {
            throw new NullPointerException("Format cannot be null.");
        }
        if (extractDependencies(args).length == 0) {
            return StringConstant.valueOf(String.format(locale, format, args));
        }
        StringFormatter formatter = new StringFormatter() { // from class: com.sun.javafx.binding.StringFormatter.3
            {
                super.bind(StringFormatter.extractDependencies(args));
            }

            @Override // javafx.beans.binding.StringBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(StringFormatter.extractDependencies(args));
            }

            @Override // javafx.beans.binding.StringBinding
            protected String computeValue() {
                Object[] values = StringFormatter.extractValues(args);
                return String.format(locale, format, values);
            }

            @Override // javafx.beans.binding.StringBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableValue<?>> getDependencies() {
                return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(StringFormatter.extractDependencies(args)));
            }
        };
        formatter.get();
        return formatter;
    }

    public static StringExpression format(final String format, final Object... args) {
        if (format == null) {
            throw new NullPointerException("Format cannot be null.");
        }
        if (extractDependencies(args).length == 0) {
            return StringConstant.valueOf(String.format(format, args));
        }
        StringFormatter formatter = new StringFormatter() { // from class: com.sun.javafx.binding.StringFormatter.4
            {
                super.bind(StringFormatter.extractDependencies(args));
            }

            @Override // javafx.beans.binding.StringBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(StringFormatter.extractDependencies(args));
            }

            @Override // javafx.beans.binding.StringBinding
            protected String computeValue() {
                Object[] values = StringFormatter.extractValues(args);
                return String.format(format, values);
            }

            @Override // javafx.beans.binding.StringBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableValue<?>> getDependencies() {
                return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(StringFormatter.extractDependencies(args)));
            }
        };
        formatter.get();
        return formatter;
    }
}
