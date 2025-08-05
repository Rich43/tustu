package sun.reflect.generics.factory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import sun.reflect.generics.tree.FieldTypeSignature;

/* loaded from: rt.jar:sun/reflect/generics/factory/GenericsFactory.class */
public interface GenericsFactory {
    TypeVariable<?> makeTypeVariable(String str, FieldTypeSignature[] fieldTypeSignatureArr);

    ParameterizedType makeParameterizedType(Type type, Type[] typeArr, Type type2);

    TypeVariable<?> findTypeVariable(String str);

    WildcardType makeWildcard(FieldTypeSignature[] fieldTypeSignatureArr, FieldTypeSignature[] fieldTypeSignatureArr2);

    Type makeNamedType(String str);

    Type makeArrayType(Type type);

    Type makeByte();

    Type makeBool();

    Type makeShort();

    Type makeChar();

    Type makeInt();

    Type makeLong();

    Type makeFloat();

    Type makeDouble();

    Type makeVoid();
}
