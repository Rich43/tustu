package jdk.nashorn.internal.ir;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/JoinPredecessor.class */
public interface JoinPredecessor {
    JoinPredecessor setLocalVariableConversion(LexicalContext lexicalContext, LocalVariableConversion localVariableConversion);

    LocalVariableConversion getLocalVariableConversion();
}
