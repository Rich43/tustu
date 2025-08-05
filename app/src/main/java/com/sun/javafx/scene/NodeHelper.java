package com.sun.javafx.scene;

import com.sun.glass.ui.Accessible;
import javafx.scene.Node;
import javafx.scene.SubScene;
import javafx.scene.text.Font;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/NodeHelper.class */
public class NodeHelper {
    private static NodeAccessor nodeAccessor;

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/NodeHelper$NodeAccessor.class */
    public interface NodeAccessor {
        void layoutNodeForPrinting(Node node);

        boolean isDerivedDepthTest(Node node);

        SubScene getSubScene(Node node);

        void setLabeledBy(Node node, Node node2);

        Accessible getAccessible(Node node);

        void recalculateRelativeSizeProperties(Node node, Font font);
    }

    static {
        forceInit(Node.class);
    }

    private NodeHelper() {
    }

    public static void layoutNodeForPrinting(Node node) {
        nodeAccessor.layoutNodeForPrinting(node);
    }

    public static boolean isDerivedDepthTest(Node node) {
        return nodeAccessor.isDerivedDepthTest(node);
    }

    public static SubScene getSubScene(Node node) {
        return nodeAccessor.getSubScene(node);
    }

    public static Accessible getAccessible(Node node) {
        return nodeAccessor.getAccessible(node);
    }

    public static void recalculateRelativeSizeProperties(Node node, Font fontForRelativeSizes) {
        nodeAccessor.recalculateRelativeSizeProperties(node, fontForRelativeSizes);
    }

    public static void setNodeAccessor(NodeAccessor newAccessor) {
        if (nodeAccessor != null) {
            throw new IllegalStateException();
        }
        nodeAccessor = newAccessor;
    }

    public static NodeAccessor getNodeAccessor() {
        if (nodeAccessor == null) {
            throw new IllegalStateException();
        }
        return nodeAccessor;
    }

    private static void forceInit(Class<?> classToInit) {
        try {
            Class.forName(classToInit.getName(), true, classToInit.getClassLoader());
        } catch (ClassNotFoundException e2) {
            throw new AssertionError(e2);
        }
    }
}
