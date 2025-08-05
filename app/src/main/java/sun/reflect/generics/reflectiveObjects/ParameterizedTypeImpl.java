package sun.reflect.generics.reflectiveObjects;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:sun/reflect/generics/reflectiveObjects/ParameterizedTypeImpl.class */
public class ParameterizedTypeImpl implements ParameterizedType {
    private final Type[] actualTypeArguments;
    private final Class<?> rawType;
    private final Type ownerType;

    private ParameterizedTypeImpl(Class<?> cls, Type[] typeArr, Type type) {
        this.actualTypeArguments = typeArr;
        this.rawType = cls;
        this.ownerType = type != null ? type : cls.getDeclaringClass();
        validateConstructorArguments();
    }

    private void validateConstructorArguments() {
        if (this.rawType.getTypeParameters().length != this.actualTypeArguments.length) {
            throw new MalformedParameterizedTypeException();
        }
        for (int i2 = 0; i2 < this.actualTypeArguments.length; i2++) {
        }
    }

    public static ParameterizedTypeImpl make(Class<?> cls, Type[] typeArr, Type type) {
        return new ParameterizedTypeImpl(cls, typeArr, type);
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type[] getActualTypeArguments() {
        return (Type[]) this.actualTypeArguments.clone();
    }

    @Override // java.lang.reflect.ParameterizedType
    public Class<?> getRawType() {
        return this.rawType;
    }

    @Override // java.lang.reflect.ParameterizedType
    public Type getOwnerType() {
        return this.ownerType;
    }

    public boolean equals(Object obj) {
        if (obj instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) obj;
            if (this == parameterizedType) {
                return true;
            }
            return Objects.equals(this.ownerType, parameterizedType.getOwnerType()) && Objects.equals(this.rawType, parameterizedType.getRawType()) && Arrays.equals(this.actualTypeArguments, parameterizedType.getActualTypeArguments());
        }
        return false;
    }

    public int hashCode() {
        return (Arrays.hashCode(this.actualTypeArguments) ^ Objects.hashCode(this.ownerType)) ^ Objects.hashCode(this.rawType);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.ownerType != null) {
            if (this.ownerType instanceof Class) {
                sb.append(((Class) this.ownerType).getName());
            } else {
                sb.append(this.ownerType.toString());
            }
            sb.append(FXMLLoader.EXPRESSION_PREFIX);
            if (this.ownerType instanceof ParameterizedTypeImpl) {
                sb.append(this.rawType.getName().replace(((ParameterizedTypeImpl) this.ownerType).rawType.getName() + FXMLLoader.EXPRESSION_PREFIX, ""));
            } else {
                sb.append(this.rawType.getSimpleName());
            }
        } else {
            sb.append(this.rawType.getName());
        }
        if (this.actualTypeArguments != null && this.actualTypeArguments.length > 0) {
            sb.append("<");
            boolean z2 = true;
            for (Type type : this.actualTypeArguments) {
                if (!z2) {
                    sb.append(", ");
                }
                sb.append(type.getTypeName());
                z2 = false;
            }
            sb.append(">");
        }
        return sb.toString();
    }
}
