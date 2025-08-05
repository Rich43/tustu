package org.omg.CosNaming;

import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/CosNaming/Binding.class */
public final class Binding implements IDLEntity {
    public NameComponent[] binding_name;
    public BindingType binding_type;

    public Binding() {
        this.binding_name = null;
        this.binding_type = null;
    }

    public Binding(NameComponent[] nameComponentArr, BindingType bindingType) {
        this.binding_name = null;
        this.binding_type = null;
        this.binding_name = nameComponentArr;
        this.binding_type = bindingType;
    }
}
