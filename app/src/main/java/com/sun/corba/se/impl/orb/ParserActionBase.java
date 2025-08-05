package com.sun.corba.se.impl.orb;

import com.sun.corba.se.spi.orb.Operation;
import java.util.Properties;

/* loaded from: rt.jar:com/sun/corba/se/impl/orb/ParserActionBase.class */
public abstract class ParserActionBase implements ParserAction {
    private String propertyName;
    private boolean prefix;
    private Operation operation;
    private String fieldName;

    @Override // com.sun.corba.se.impl.orb.ParserAction
    public abstract Object apply(Properties properties);

    public int hashCode() {
        return ((this.propertyName.hashCode() ^ this.operation.hashCode()) ^ this.fieldName.hashCode()) ^ (this.prefix ? 0 : 1);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ParserActionBase)) {
            return false;
        }
        ParserActionBase parserActionBase = (ParserActionBase) obj;
        return this.propertyName.equals(parserActionBase.propertyName) && this.prefix == parserActionBase.prefix && this.operation.equals(parserActionBase.operation) && this.fieldName.equals(parserActionBase.fieldName);
    }

    public ParserActionBase(String str, boolean z2, Operation operation, String str2) {
        this.propertyName = str;
        this.prefix = z2;
        this.operation = operation;
        this.fieldName = str2;
    }

    @Override // com.sun.corba.se.impl.orb.ParserAction
    public String getPropertyName() {
        return this.propertyName;
    }

    @Override // com.sun.corba.se.impl.orb.ParserAction
    public boolean isPrefix() {
        return this.prefix;
    }

    @Override // com.sun.corba.se.impl.orb.ParserAction
    public String getFieldName() {
        return this.fieldName;
    }

    protected Operation getOperation() {
        return this.operation;
    }
}
