package jdk.nashorn.internal.codegen;

import java.util.List;
import jdk.nashorn.internal.codegen.CodeGenerator;
import jdk.nashorn.internal.codegen.types.Type;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.PropertyMap;
import jdk.nashorn.internal.runtime.ScriptObject;

/* loaded from: nashorn.jar:jdk/nashorn/internal/codegen/ObjectCreator.class */
public abstract class ObjectCreator<T> implements CodeGenerator.SplitLiteralCreator {
    final List<MapTuple<T>> tuples;
    final CodeGenerator codegen;
    protected PropertyMap propertyMap;
    private final boolean isScope;
    private final boolean hasArguments;

    protected abstract void createObject(MethodEmitter methodEmitter);

    protected abstract PropertyMap makeMap();

    protected abstract Class<? extends ScriptObject> getAllocatorClass();

    protected abstract void loadValue(T t2, Type type);

    ObjectCreator(CodeGenerator codegen, List<MapTuple<T>> tuples, boolean isScope, boolean hasArguments) {
        this.codegen = codegen;
        this.tuples = tuples;
        this.isScope = isScope;
        this.hasArguments = hasArguments;
    }

    public void makeObject(MethodEmitter method) {
        createObject(method);
        int objectSlot = method.getUsedSlotsWithLiveTemporaries();
        Type objectType = method.peekType();
        method.storeTemp(objectType, objectSlot);
        populateRange(method, objectType, objectSlot, 0, this.tuples.size());
    }

    protected MapCreator<?> newMapCreator(Class<? extends ScriptObject> clazz) {
        return new MapCreator<>(clazz, this.tuples);
    }

    protected void loadScope(MethodEmitter method) {
        method.loadCompilerConstant(CompilerConstants.SCOPE);
    }

    protected MethodEmitter loadMap(MethodEmitter method) {
        this.codegen.loadConstant(this.propertyMap);
        return method;
    }

    PropertyMap getMap() {
        return this.propertyMap;
    }

    protected boolean isScope() {
        return this.isScope;
    }

    protected boolean hasArguments() {
        return this.hasArguments;
    }

    MethodEmitter loadTuple(MethodEmitter method, MapTuple<T> tuple, boolean pack) {
        loadValue(tuple.value, tuple.type);
        if (!this.codegen.useDualFields() || !tuple.isPrimitive()) {
            method.convert(Type.OBJECT);
        } else if (pack) {
            method.pack();
        }
        return method;
    }

    MethodEmitter loadIndex(MethodEmitter method, long index) {
        return JSType.isRepresentableAsInt(index) ? method.load((int) index) : method.load(index);
    }
}
