package org.omg.Dynamic;

import org.omg.CORBA.Any;
import org.omg.CORBA.ParameterMode;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:org/omg/Dynamic/Parameter.class */
public final class Parameter implements IDLEntity {
    public Any argument;
    public ParameterMode mode;

    public Parameter() {
        this.argument = null;
        this.mode = null;
    }

    public Parameter(Any any, ParameterMode parameterMode) {
        this.argument = null;
        this.mode = null;
        this.argument = any;
        this.mode = parameterMode;
    }
}
