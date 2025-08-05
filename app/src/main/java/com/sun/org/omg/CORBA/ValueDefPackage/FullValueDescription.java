package com.sun.org.omg.CORBA.ValueDefPackage;

import com.sun.org.omg.CORBA.AttributeDescription;
import com.sun.org.omg.CORBA.Initializer;
import com.sun.org.omg.CORBA.OperationDescription;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.ValueMember;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/org/omg/CORBA/ValueDefPackage/FullValueDescription.class */
public final class FullValueDescription implements IDLEntity {
    public String name;
    public String id;
    public boolean is_abstract;
    public boolean is_custom;
    public String defined_in;
    public String version;
    public OperationDescription[] operations;
    public AttributeDescription[] attributes;
    public ValueMember[] members;
    public Initializer[] initializers;
    public String[] supported_interfaces;
    public String[] abstract_base_values;
    public boolean is_truncatable;
    public String base_value;
    public TypeCode type;

    public FullValueDescription() {
        this.name = null;
        this.id = null;
        this.is_abstract = false;
        this.is_custom = false;
        this.defined_in = null;
        this.version = null;
        this.operations = null;
        this.attributes = null;
        this.members = null;
        this.initializers = null;
        this.supported_interfaces = null;
        this.abstract_base_values = null;
        this.is_truncatable = false;
        this.base_value = null;
        this.type = null;
    }

    public FullValueDescription(String str, String str2, boolean z2, boolean z3, String str3, String str4, OperationDescription[] operationDescriptionArr, AttributeDescription[] attributeDescriptionArr, ValueMember[] valueMemberArr, Initializer[] initializerArr, String[] strArr, String[] strArr2, boolean z4, String str5, TypeCode typeCode) {
        this.name = null;
        this.id = null;
        this.is_abstract = false;
        this.is_custom = false;
        this.defined_in = null;
        this.version = null;
        this.operations = null;
        this.attributes = null;
        this.members = null;
        this.initializers = null;
        this.supported_interfaces = null;
        this.abstract_base_values = null;
        this.is_truncatable = false;
        this.base_value = null;
        this.type = null;
        this.name = str;
        this.id = str2;
        this.is_abstract = z2;
        this.is_custom = z3;
        this.defined_in = str3;
        this.version = str4;
        this.operations = operationDescriptionArr;
        this.attributes = attributeDescriptionArr;
        this.members = valueMemberArr;
        this.initializers = initializerArr;
        this.supported_interfaces = strArr;
        this.abstract_base_values = strArr2;
        this.is_truncatable = z4;
        this.base_value = str5;
        this.type = typeCode;
    }
}
