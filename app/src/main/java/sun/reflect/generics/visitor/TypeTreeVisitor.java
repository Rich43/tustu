package sun.reflect.generics.visitor;

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
import sun.reflect.generics.tree.TypeVariableSignature;
import sun.reflect.generics.tree.VoidDescriptor;
import sun.reflect.generics.tree.Wildcard;

/* loaded from: rt.jar:sun/reflect/generics/visitor/TypeTreeVisitor.class */
public interface TypeTreeVisitor<T> {
    T getResult();

    void visitFormalTypeParameter(FormalTypeParameter formalTypeParameter);

    void visitClassTypeSignature(ClassTypeSignature classTypeSignature);

    void visitArrayTypeSignature(ArrayTypeSignature arrayTypeSignature);

    void visitTypeVariableSignature(TypeVariableSignature typeVariableSignature);

    void visitWildcard(Wildcard wildcard);

    void visitSimpleClassTypeSignature(SimpleClassTypeSignature simpleClassTypeSignature);

    void visitBottomSignature(BottomSignature bottomSignature);

    void visitByteSignature(ByteSignature byteSignature);

    void visitBooleanSignature(BooleanSignature booleanSignature);

    void visitShortSignature(ShortSignature shortSignature);

    void visitCharSignature(CharSignature charSignature);

    void visitIntSignature(IntSignature intSignature);

    void visitLongSignature(LongSignature longSignature);

    void visitFloatSignature(FloatSignature floatSignature);

    void visitDoubleSignature(DoubleSignature doubleSignature);

    void visitVoidDescriptor(VoidDescriptor voidDescriptor);
}
