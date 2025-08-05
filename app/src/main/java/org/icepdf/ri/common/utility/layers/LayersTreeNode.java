package org.icepdf.ri.common.utility.layers;

import java.util.Enumeration;
import java.util.Iterator;
import javax.swing.tree.DefaultMutableTreeNode;
import org.icepdf.core.pobjects.OptionalContentGroup;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/layers/LayersTreeNode.class */
public class LayersTreeNode extends DefaultMutableTreeNode {
    public static final int SINGLE_SELECTION = 1;
    public static final int RADIO_SELECTION = 2;
    protected int selectionMode;

    public LayersTreeNode(Object object) {
        this.selectionMode = 1;
        OptionalContentGroup optionalContentGroup = null;
        if (object instanceof String) {
            optionalContentGroup = new OptionalContentGroup((String) object, false);
        } else if (object instanceof OptionalContentGroup) {
            optionalContentGroup = (OptionalContentGroup) object;
        }
        setUserObject(optionalContentGroup);
    }

    public LayersTreeNode(OptionalContentGroup optionalContentGroup) {
        this(optionalContentGroup, true);
        setUserObject(optionalContentGroup);
    }

    public LayersTreeNode(Object userObject, boolean allowsChildren) {
        super(userObject, allowsChildren);
        this.selectionMode = 1;
    }

    public void setSelectionMode(int mode) {
        this.selectionMode = mode;
    }

    public int getSelectionMode() {
        return this.selectionMode;
    }

    public OptionalContentGroup getOptionalContentGroup() {
        return (OptionalContentGroup) getUserObject();
    }

    public void setSelected(boolean isSelected) {
        Enumeration children;
        ((OptionalContentGroup) getUserObject()).setVisible(isSelected);
        if (this.selectionMode == 1 && this.children != null) {
            Iterator i$ = this.children.iterator();
            while (i$.hasNext()) {
                Object child = i$.next();
                ((LayersTreeNode) child).setSelected(isSelected);
            }
            return;
        }
        if (this.selectionMode == 2 && (children = this.parent.children()) != null) {
            while (children.hasMoreElements()) {
                LayersTreeNode layerNode = (LayersTreeNode) children.nextElement2();
                if (!layerNode.equals(this)) {
                    layerNode.getOptionalContentGroup().setVisible(false);
                }
            }
        }
    }

    public boolean isSelected() {
        return ((OptionalContentGroup) getUserObject()).isVisible();
    }

    @Override // javax.swing.tree.DefaultMutableTreeNode
    public String toString() {
        return ((OptionalContentGroup) getUserObject()).getName();
    }
}
