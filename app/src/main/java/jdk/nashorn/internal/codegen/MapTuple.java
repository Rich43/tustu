package jdk.nashorn.internal.codegen;

import javafx.fxml.FXMLLoader;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.ir.Symbol;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/MapTuple.class */
class MapTuple<T> {
    final String key;
    final Symbol symbol;
    final Type type;
    final T value;

    MapTuple(String key, Symbol symbol, Type type) {
        this(key, symbol, type, null);
    }

    MapTuple(String key, Symbol symbol, Type type, T value) {
        this.key = key;
        this.symbol = symbol;
        this.type = type;
        this.value = value;
    }

    public Class<?> getValueType() {
        return null;
    }

    boolean isPrimitive() {
        return (getValueType() == null || !getValueType().isPrimitive() || getValueType() == Boolean.TYPE) ? false : true;
    }

    public String toString() {
        return "[key=" + this.key + ", symbol=" + ((Object) this.symbol) + ", value=" + ((Object) this.value) + " (" + (this.value == null ? FXMLLoader.NULL_KEYWORD : this.value.getClass().getSimpleName()) + ")]";
    }
}
