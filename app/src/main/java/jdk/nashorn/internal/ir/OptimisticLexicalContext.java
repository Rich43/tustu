package jdk.nashorn.internal.ir;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import jdk.nashorn.internal.codegen.types.Type;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/OptimisticLexicalContext.class */
public class OptimisticLexicalContext extends LexicalContext {
    private final boolean isEnabled;
    private final Deque<List<Assumption>> optimisticAssumptions = new ArrayDeque();

    /* loaded from: nashorn.jar:jdk/nashorn/internal/ir/OptimisticLexicalContext$Assumption.class */
    class Assumption {
        Symbol symbol;
        Type type;

        Assumption(Symbol symbol, Type type) {
            this.symbol = symbol;
            this.type = type;
        }

        public String toString() {
            return this.symbol.getName() + "=" + ((Object) this.type);
        }
    }

    public OptimisticLexicalContext(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void logOptimisticAssumption(Symbol symbol, Type type) {
        if (this.isEnabled) {
            List<Assumption> peek = this.optimisticAssumptions.peek();
            peek.add(new Assumption(symbol, type));
        }
    }

    public List<Assumption> getOptimisticAssumptions() {
        return Collections.unmodifiableList(this.optimisticAssumptions.peek());
    }

    public boolean hasOptimisticAssumptions() {
        return (this.optimisticAssumptions.isEmpty() || getOptimisticAssumptions().isEmpty()) ? false : true;
    }

    @Override // jdk.nashorn.internal.ir.LexicalContext
    public <T extends LexicalContextNode> T push(T t2) {
        if (this.isEnabled && (t2 instanceof FunctionNode)) {
            this.optimisticAssumptions.push(new ArrayList());
        }
        return (T) super.push(t2);
    }

    @Override // jdk.nashorn.internal.ir.LexicalContext
    public <T extends Node> T pop(T t2) {
        T t3 = (T) super.pop(t2);
        if (this.isEnabled && (t2 instanceof FunctionNode)) {
            this.optimisticAssumptions.pop();
        }
        return t3;
    }
}
