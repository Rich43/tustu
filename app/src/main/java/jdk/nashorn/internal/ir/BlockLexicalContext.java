package jdk.nashorn.internal.ir;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/BlockLexicalContext.class */
public class BlockLexicalContext extends LexicalContext {
    private final Deque<List<Statement>> sstack = new ArrayDeque();
    protected Statement lastStatement;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BlockLexicalContext.class.desiredAssertionStatus();
    }

    @Override // jdk.nashorn.internal.ir.LexicalContext
    public <T extends LexicalContextNode> T push(T t2) {
        T t3 = (T) super.push(t2);
        if (t2 instanceof Block) {
            this.sstack.push(new ArrayList());
        }
        return t3;
    }

    protected List<Statement> popStatements() {
        return this.sstack.pop();
    }

    protected Block afterSetStatements(Block block) {
        return block;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // jdk.nashorn.internal.ir.LexicalContext
    public <T extends Node> T pop(T t2) {
        Block blockAfterSetStatements = t2;
        if (t2 instanceof Block) {
            blockAfterSetStatements = afterSetStatements(((Block) t2).setStatements(this, popStatements()));
            if (!this.sstack.isEmpty()) {
                this.lastStatement = lastStatement(this.sstack.peek());
            }
        }
        return (T) super.pop(blockAfterSetStatements);
    }

    public void appendStatement(Statement statement) {
        if (!$assertionsDisabled && statement == null) {
            throw new AssertionError();
        }
        this.sstack.peek().add(statement);
        this.lastStatement = statement;
    }

    public Node prependStatement(Statement statement) {
        if (!$assertionsDisabled && statement == null) {
            throw new AssertionError();
        }
        this.sstack.peek().add(0, statement);
        return statement;
    }

    public void prependStatements(List<Statement> statements) {
        if (!$assertionsDisabled && statements == null) {
            throw new AssertionError();
        }
        this.sstack.peek().addAll(0, statements);
    }

    public Statement getLastStatement() {
        return this.lastStatement;
    }

    private static Statement lastStatement(List<Statement> statements) {
        int s2 = statements.size();
        if (s2 == 0) {
            return null;
        }
        return statements.get(s2 - 1);
    }
}
