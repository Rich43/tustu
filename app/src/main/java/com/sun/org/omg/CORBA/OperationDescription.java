package com.sun.org.omg.CORBA;

import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/OperationDescription.class */
public final class OperationDescription implements IDLEntity {
    public String name;
    public String id;
    public String defined_in;
    public String version;
    public TypeCode result;
    public OperationMode mode;
    public String[] contexts;
    public ParameterDescription[] parameters;
    public ExceptionDescription[] exceptions;

    public OperationDescription() {
        this.name = null;
        this.id = null;
        this.defined_in = null;
        this.version = null;
        this.result = null;
        this.mode = null;
        this.contexts = null;
        this.parameters = null;
        this.exceptions = null;
    }

    public OperationDescription(String str, String str2, String str3, String str4, TypeCode typeCode, OperationMode operationMode, String[] strArr, ParameterDescription[] parameterDescriptionArr, ExceptionDescription[] exceptionDescriptionArr) {
        this.name = null;
        this.id = null;
        this.defined_in = null;
        this.version = null;
        this.result = null;
        this.mode = null;
        this.contexts = null;
        this.parameters = null;
        this.exceptions = null;
        this.name = str;
        this.id = str2;
        this.defined_in = str3;
        this.version = str4;
        this.result = typeCode;
        this.mode = operationMode;
        this.contexts = strArr;
        this.parameters = parameterDescriptionArr;
        this.exceptions = exceptionDescriptionArr;
    }
}
