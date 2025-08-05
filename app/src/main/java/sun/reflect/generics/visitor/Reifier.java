package sun.reflect.generics.visitor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import javafx.fxml.FXMLLoader;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.tree.ArrayTypeSignature;
import sun.reflect.generics.tree.BooleanSignature;
import sun.reflect.generics.tree.BottomSignature;
import sun.reflect.generics.tree.ByteSignature;
import sun.reflect.generics.tree.CharSignature;
import sun.reflect.generics.tree.ClassTypeSignature;
import sun.reflect.generics.tree.DoubleSignature;
import sun.reflect.generics.tree.FloatSignature;
import sun.reflect.generics.tree.FormalTypeParameter;
import sun.reflect.generics.tree.IntSignature;
import sun.reflect.generics.tree.LongSignature;
import sun.reflect.generics.tree.ShortSignature;
import sun.reflect.generics.tree.SimpleClassTypeSignature;
import sun.reflect.generics.tree.TypeArgument;
import sun.reflect.generics.tree.TypeVariableSignature;
import sun.reflect.generics.tree.VoidDescriptor;
import sun.reflect.generics.tree.Wildcard;

/* loaded from: rt.jar:sun/reflect/generics/visitor/Reifier.class */
public class Reifier implements TypeTreeVisitor<Type> {
    private Type resultType;
    private GenericsFactory factory;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Reifier.class.desiredAssertionStatus();
    }

    private Reifier(GenericsFactory genericsFactory) {
        this.factory = genericsFactory;
    }

    private GenericsFactory getFactory() {
        return this.factory;
    }

    public static Reifier make(GenericsFactory genericsFactory) {
        return new Reifier(genericsFactory);
    }

    private Type[] reifyTypeArguments(TypeArgument[] typeArgumentArr) {
        Type[] typeArr = new Type[typeArgumentArr.length];
        for (int i2 = 0; i2 < typeArgumentArr.length; i2++) {
            typeArgumentArr[i2].accept(this);
            typeArr[i2] = this.resultType;
        }
        return typeArr;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public Type getResult() {
        if ($assertionsDisabled || this.resultType != null) {
            return this.resultType;
        }
        throw new AssertionError();
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitFormalTypeParameter(FormalTypeParameter formalTypeParameter) {
        this.resultType = getFactory().makeTypeVariable(formalTypeParameter.getName(), formalTypeParameter.getBounds());
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitClassTypeSignature(ClassTypeSignature classTypeSignature) {
        List<SimpleClassTypeSignature> path = classTypeSignature.getPath();
        if (!$assertionsDisabled && path.isEmpty()) {
            throw new AssertionError();
        }
        Iterator<SimpleClassTypeSignature> it = path.iterator();
        SimpleClassTypeSignature next = it.next();
        StringBuilder sb = new StringBuilder(next.getName());
        next.getDollar();
        while (it.hasNext() && next.getTypeArguments().length == 0) {
            next = it.next();
            sb.append(next.getDollar() ? FXMLLoader.EXPRESSION_PREFIX : ".").append(next.getName());
        }
        if (!$assertionsDisabled && it.hasNext() && next.getTypeArguments().length <= 0) {
            throw new AssertionError();
        }
        Type typeMakeNamedType = getFactory().makeNamedType(sb.toString());
        if (next.getTypeArguments().length == 0) {
            if (!$assertionsDisabled && it.hasNext()) {
                throw new AssertionError();
            }
            this.resultType = typeMakeNamedType;
            return;
        }
        if (!$assertionsDisabled && next.getTypeArguments().length <= 0) {
            throw new AssertionError();
        }
        ParameterizedType parameterizedTypeMakeParameterizedType = getFactory().makeParameterizedType(typeMakeNamedType, reifyTypeArguments(next.getTypeArguments()), null);
        while (it.hasNext()) {
            SimpleClassTypeSignature next2 = it.next();
            sb.append(next2.getDollar() ? FXMLLoader.EXPRESSION_PREFIX : ".").append(next2.getName());
            parameterizedTypeMakeParameterizedType = getFactory().makeParameterizedType(getFactory().makeNamedType(sb.toString()), reifyTypeArguments(next2.getTypeArguments()), parameterizedTypeMakeParameterizedType);
        }
        this.resultType = parameterizedTypeMakeParameterizedType;
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitArrayTypeSignature(ArrayTypeSignature arrayTypeSignature) {
        arrayTypeSignature.getComponentType().accept(this);
        this.resultType = getFactory().makeArrayType(this.resultType);
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitTypeVariableSignature(TypeVariableSignature typeVariableSignature) {
        this.resultType = getFactory().findTypeVariable(typeVariableSignature.getIdentifier());
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitWildcard(Wildcard wildcard) {
        this.resultType = getFactory().makeWildcard(wildcard.getUpperBounds(), wildcard.getLowerBounds());
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitSimpleClassTypeSignature(SimpleClassTypeSignature simpleClassTypeSignature) {
        this.resultType = getFactory().makeNamedType(simpleClassTypeSignature.getName());
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitBottomSignature(BottomSignature bottomSignature) {
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitByteSignature(ByteSignature byteSignature) {
        this.resultType = getFactory().makeByte();
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitBooleanSignature(BooleanSignature booleanSignature) {
        this.resultType = getFactory().makeBool();
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitShortSignature(ShortSignature shortSignature) {
        this.resultType = getFactory().makeShort();
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitCharSignature(CharSignature charSignature) {
        this.resultType = getFactory().makeChar();
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitIntSignature(IntSignature intSignature) {
        this.resultType = getFactory().makeInt();
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitLongSignature(LongSignature longSignature) {
        this.resultType = getFactory().makeLong();
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitFloatSignature(FloatSignature floatSignature) {
        this.resultType = getFactory().makeFloat();
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitDoubleSignature(DoubleSignature doubleSignature) {
        this.resultType = getFactory().makeDouble();
    }

    @Override // sun.reflect.generics.visitor.TypeTreeVisitor
    public void visitVoidDescriptor(VoidDescriptor voidDescriptor) {
        this.resultType = getFactory().makeVoid();
    }
}
