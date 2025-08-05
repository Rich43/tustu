package jdk.nashorn.internal.runtime;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.SwitchPoint;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.GuardedInvocation;
import jdk.internal.dynalink.linker.LinkRequest;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.linker.NashornCallSiteDescriptor;
import jdk.nashorn.internal.runtime.linker.NashornGuards;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/SetMethodCreator.class */
final class SetMethodCreator {
    private final ScriptObject sobj;
    private final PropertyMap map;
    private final FindProperty find;
    private final CallSiteDescriptor desc;
    private final Class<?> type;
    private final LinkRequest request;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SetMethodCreator.class.desiredAssertionStatus();
    }

    SetMethodCreator(ScriptObject sobj, FindProperty find, CallSiteDescriptor desc, LinkRequest request) {
        this.sobj = sobj;
        this.map = sobj.getMap();
        this.find = find;
        this.desc = desc;
        this.type = desc.getMethodType().parameterType(1);
        this.request = request;
    }

    private String getName() {
        return this.desc.getNameToken(2);
    }

    private PropertyMap getMap() {
        return this.map;
    }

    GuardedInvocation createGuardedInvocation(SwitchPoint builtinSwitchPoint) {
        return createSetMethod(builtinSwitchPoint).createGuardedInvocation();
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/SetMethodCreator$SetMethod.class */
    private class SetMethod {
        private final MethodHandle methodHandle;
        private final Property property;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !SetMethodCreator.class.desiredAssertionStatus();
        }

        SetMethod(MethodHandle methodHandle, Property property) {
            if (!$assertionsDisabled && methodHandle == null) {
                throw new AssertionError();
            }
            this.methodHandle = methodHandle;
            this.property = property;
        }

        GuardedInvocation createGuardedInvocation() {
            boolean explicitInstanceOfCheck = NashornGuards.explicitInstanceOfCheck(SetMethodCreator.this.desc, SetMethodCreator.this.request);
            return new GuardedInvocation(this.methodHandle, NashornGuards.getGuard(SetMethodCreator.this.sobj, this.property, SetMethodCreator.this.desc, explicitInstanceOfCheck), (SwitchPoint) null, explicitInstanceOfCheck ? null : ClassCastException.class);
        }
    }

    private SetMethod createSetMethod(SwitchPoint builtinSwitchPoint) {
        if (this.find != null) {
            return createExistingPropertySetter();
        }
        checkStrictCreateNewVariable();
        if (this.sobj.isScope()) {
            return createGlobalPropertySetter();
        }
        return createNewPropertySetter(builtinSwitchPoint);
    }

    private void checkStrictCreateNewVariable() {
        if (NashornCallSiteDescriptor.isScope(this.desc) && NashornCallSiteDescriptor.isStrict(this.desc)) {
            throw ECMAErrors.referenceError("not.defined", getName());
        }
    }

    private SetMethod createExistingPropertySetter() {
        MethodHandle methodHandle;
        MethodHandle boundHandle;
        Property property = this.find.getProperty();
        boolean isStrict = NashornCallSiteDescriptor.isStrict(this.desc);
        if (NashornCallSiteDescriptor.isDeclaration(this.desc)) {
            if (!$assertionsDisabled && !property.needsDeclaration()) {
                throw new AssertionError();
            }
            PropertyMap oldMap = getMap();
            Property newProperty = property.removeFlags(512);
            PropertyMap newMap = oldMap.replaceProperty(property, newProperty);
            MethodHandle fastSetter = this.find.replaceProperty(newProperty).getSetter(this.type, isStrict, this.request);
            MethodHandle slowSetter = Lookup.MH.insertArguments(ScriptObject.DECLARE_AND_SET, 1, getName()).asType(fastSetter.type());
            MethodHandle casMap = Lookup.MH.dropArguments(Lookup.MH.insertArguments(ScriptObject.CAS_MAP, 1, oldMap, newMap), 1, this.type);
            methodHandle = Lookup.MH.guardWithTest(Lookup.MH.asType(casMap, casMap.type().changeParameterType(0, Object.class)), fastSetter, slowSetter);
        } else {
            methodHandle = this.find.getSetter(this.type, isStrict, this.request);
        }
        if (!$assertionsDisabled && methodHandle == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && property == null) {
            throw new AssertionError();
        }
        if (!(property instanceof UserAccessorProperty) && this.find.isInherited()) {
            boundHandle = ScriptObject.addProtoFilter(methodHandle, this.find.getProtoChainLength());
        } else {
            boundHandle = methodHandle;
        }
        return new SetMethod(boundHandle, property);
    }

    private SetMethod createGlobalPropertySetter() {
        ScriptObject global = Context.getGlobal();
        return new SetMethod(Lookup.MH.filterArguments(global.addSpill(this.type, getName()), 0, ScriptObject.GLOBALFILTER), null);
    }

    private SetMethod createNewPropertySetter(SwitchPoint builtinSwitchPoint) {
        SetMethod sm = this.map.getFreeFieldSlot() > -1 ? createNewFieldSetter(builtinSwitchPoint) : createNewSpillPropertySetter(builtinSwitchPoint);
        this.map.propertyAdded(sm.property, true);
        return sm;
    }

    private SetMethod createNewSetter(Property property, SwitchPoint builtinSwitchPoint) {
        property.setBuiltinSwitchPoint(builtinSwitchPoint);
        PropertyMap oldMap = getMap();
        PropertyMap newMap = getNewMap(property);
        boolean isStrict = NashornCallSiteDescriptor.isStrict(this.desc);
        String name = this.desc.getNameToken(2);
        MethodHandle fastSetter = property.getSetter(this.type, newMap);
        MethodHandle slowSetter = Lookup.MH.insertArguments(Lookup.MH.insertArguments(ScriptObject.SET_SLOW[JSType.getAccessorTypeIndex(this.type)], 3, Integer.valueOf(NashornCallSiteDescriptor.getFlags(this.desc))), 1, name);
        MethodHandle slowSetter2 = Lookup.MH.asType(slowSetter, slowSetter.type().changeParameterType(0, Object.class));
        if (!$assertionsDisabled && !slowSetter2.type().equals((Object) fastSetter.type())) {
            throw new AssertionError((Object) ("slow=" + ((Object) slowSetter2) + " != fast=" + ((Object) fastSetter)));
        }
        MethodHandle casMap = Lookup.MH.dropArguments(Lookup.MH.insertArguments(ScriptObject.CAS_MAP, 1, oldMap, newMap), 1, this.type);
        MethodHandle casGuard = Lookup.MH.guardWithTest(Lookup.MH.asType(casMap, casMap.type().changeParameterType(0, Object.class)), fastSetter, slowSetter2);
        MethodHandle extCheck = Lookup.MH.insertArguments(ScriptObject.EXTENSION_CHECK, 1, Boolean.valueOf(isStrict), name);
        MethodHandle extCheck2 = Lookup.MH.dropArguments(Lookup.MH.asType(extCheck, extCheck.type().changeParameterType(0, Object.class)), 1, this.type);
        MethodHandle nop = JSType.VOID_RETURN.methodHandle();
        return new SetMethod(Lookup.MH.asType(Lookup.MH.guardWithTest(extCheck2, casGuard, Lookup.MH.dropArguments(nop, 0, Object.class, this.type)), fastSetter.type()), property);
    }

    private SetMethod createNewFieldSetter(SwitchPoint builtinSwitchPoint) {
        return createNewSetter(new AccessorProperty(getName(), getFlags(this.sobj), this.sobj.getClass(), getMap().getFreeFieldSlot(), this.type), builtinSwitchPoint);
    }

    private SetMethod createNewSpillPropertySetter(SwitchPoint builtinSwitchPoint) {
        return createNewSetter(new SpillProperty(getName(), getFlags(this.sobj), getMap().getFreeSpillSlot(), this.type), builtinSwitchPoint);
    }

    private PropertyMap getNewMap(Property property) {
        return getMap().addProperty(property);
    }

    private static int getFlags(ScriptObject scriptObject) {
        return scriptObject.useDualFields() ? 2048 : 0;
    }
}
