package jdk.nashorn.internal.ir.debug;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import jdk.nashorn.internal.ir.BinaryNode;
import jdk.nashorn.internal.ir.Block;
import jdk.nashorn.internal.ir.Expression;
import jdk.nashorn.internal.ir.IdentNode;
import jdk.nashorn.internal.ir.Node;
import jdk.nashorn.internal.ir.Statement;
import jdk.nashorn.internal.ir.Symbol;
import jdk.nashorn.internal.ir.Terminal;
import jdk.nashorn.internal.ir.TernaryNode;
import jdk.nashorn.internal.ir.annotations.Ignore;
import jdk.nashorn.internal.ir.annotations.Reference;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.runtime.Context;
import jdk.nashorn.internal.runtime.Debug;
import org.icepdf.core.util.PdfOps;

/* loaded from: nashorn.jar:jdk/nashorn/internal/ir/debug/ASTWriter.class */
public final class ASTWriter {
    private final Node root;
    private static final int TABWIDTH = 4;

    public ASTWriter(Node root) {
        this.root = root;
    }

    public String toString() throws SecurityException {
        StringBuilder sb = new StringBuilder();
        printAST(sb, null, null, "root", this.root, 0);
        return sb.toString();
    }

    public Node[] toArray() throws SecurityException {
        List<Node> preorder = new ArrayList<>();
        printAST(new StringBuilder(), preorder, null, "root", this.root, 0);
        return (Node[]) preorder.toArray(new Node[preorder.size()]);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void printAST(StringBuilder sb, List<Node> preorder, Field field, String name, Node node, int indent) throws SecurityException {
        Symbol symbol;
        indent(sb, indent);
        if (node == 0) {
            sb.append("[Object ");
            sb.append(name);
            sb.append(" null]\n");
            return;
        }
        if (preorder != null) {
            preorder.add(node);
        }
        boolean isReference = field != null && field.isAnnotationPresent(Reference.class);
        Class<?> clazz = node.getClass();
        String type = clazz.getName();
        String type2 = type.substring(type.lastIndexOf(46) + 1, type.length());
        int truncate = type2.indexOf("Node");
        if (truncate == -1) {
            truncate = type2.indexOf("Statement");
        }
        if (truncate != -1) {
            type2 = type2.substring(0, truncate);
        }
        String type3 = type2.toLowerCase();
        if (isReference) {
            type3 = "ref: " + type3;
        }
        if (node instanceof IdentNode) {
            symbol = ((IdentNode) node).getSymbol();
        } else {
            symbol = null;
        }
        if (symbol != null) {
            type3 = type3 + ">" + ((Object) symbol);
        }
        if ((node instanceof Block) && ((Block) node).needsScope()) {
            type3 = type3 + " <scope>";
        }
        List<Field> children = new LinkedList<>();
        if (!isReference) {
            enqueueChildren(node, clazz, children);
        }
        String status = "";
        if ((node instanceof Terminal) && ((Terminal) node).isTerminal()) {
            status = status + " Terminal";
        }
        if ((node instanceof Statement) && ((Statement) node).hasGoto()) {
            status = status + " Goto ";
        }
        if (symbol != null) {
            status = status + ((Object) symbol);
        }
        String status2 = status.trim();
        if (!"".equals(status2)) {
            status2 = " [" + status2 + "]";
        }
        if (symbol != null) {
            String tname = ((Expression) node).getType().toString();
            if (tname.indexOf(46) != -1) {
                tname = tname.substring(tname.lastIndexOf(46) + 1, tname.length());
            }
            status2 = status2 + " (" + tname + ")";
        }
        String status3 = status2 + " @" + Debug.id(node);
        if (children.isEmpty()) {
            sb.append("[").append(type3).append(' ').append(name).append(" = '").append((Object) node).append(PdfOps.SINGLE_QUOTE_TOKEN).append(status3).append("] ").append('\n');
            return;
        }
        sb.append("[").append(type3).append(' ').append(name).append(' ').append(Token.toString(node.getToken())).append(status3).append("]").append('\n');
        for (Field child : children) {
            if (!child.isAnnotationPresent(Ignore.class)) {
                try {
                    Object value = child.get(node);
                    if (value instanceof Node) {
                        printAST(sb, preorder, child, child.getName(), (Node) value, indent + 1);
                    } else if (value instanceof Collection) {
                        int pos = 0;
                        indent(sb, indent + 1);
                        sb.append('[').append(child.getName()).append("[0..").append(((Collection) value).size()).append("]]").append('\n');
                        for (Node member : (Collection) value) {
                            int i2 = pos;
                            pos++;
                            printAST(sb, preorder, child, child.getName() + "[" + i2 + "]", member, indent + 2);
                        }
                    }
                } catch (IllegalAccessException | IllegalArgumentException e2) {
                    Context.printStackTrace(e2);
                    return;
                }
            }
        }
    }

    private static void enqueueChildren(Node node, Class<?> nodeClass, List<Field> children) throws SecurityException {
        ArrayDeque arrayDeque = new ArrayDeque();
        Class<?> clazz = nodeClass;
        do {
            arrayDeque.push(clazz);
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        if (node instanceof TernaryNode) {
            arrayDeque.push(arrayDeque.removeLast());
        }
        Iterator itDescendingIterator = node instanceof BinaryNode ? arrayDeque.descendingIterator() : arrayDeque.iterator();
        while (itDescendingIterator.hasNext()) {
            Class<?> c2 = (Class) itDescendingIterator.next();
            for (Field f2 : c2.getDeclaredFields()) {
                try {
                    f2.setAccessible(true);
                    Object child = f2.get(node);
                    if (child != null) {
                        if (child instanceof Node) {
                            children.add(f2);
                        } else if ((child instanceof Collection) && !((Collection) child).isEmpty()) {
                            children.add(f2);
                        }
                    }
                } catch (IllegalAccessException | IllegalArgumentException e2) {
                    return;
                }
            }
        }
    }

    private static void indent(StringBuilder sb, int indent) {
        for (int i2 = 0; i2 < indent; i2++) {
            for (int j2 = 0; j2 < 4; j2++) {
                sb.append(' ');
            }
        }
    }
}
