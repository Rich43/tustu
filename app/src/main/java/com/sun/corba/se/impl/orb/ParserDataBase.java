package com.sun.corba.se.impl.orb;

import com.sun.corba.se.spi.orb.Operation;
import com.sun.corba.se.spi.orb.ParserData;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserDataBase.class */
public abstract class ParserDataBase implements ParserData {
    private String propertyName;
    private Operation operation;
    private String fieldName;
    private Object defaultValue;
    private Object testValue;

    protected ParserDataBase(String str, Operation operation, String str2, Object obj, Object obj2) {
        this.propertyName = str;
        this.operation = operation;
        this.fieldName = str2;
        this.defaultValue = obj;
        this.testValue = obj2;
    }

    @Override // com.sun.corba.se.spi.orb.ParserData
    public String getPropertyName() {
        return this.propertyName;
    }

    @Override // com.sun.corba.se.spi.orb.ParserData
    public Operation getOperation() {
        return this.operation;
    }

    @Override // com.sun.corba.se.spi.orb.ParserData
    public String getFieldName() {
        return this.fieldName;
    }

    @Override // com.sun.corba.se.spi.orb.ParserData
    public Object getDefaultValue() {
        return this.defaultValue;
    }

    @Override // com.sun.corba.se.spi.orb.ParserData
    public Object getTestValue() {
        return this.testValue;
    }
}
