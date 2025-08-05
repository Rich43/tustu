package org.icepdf.ri.common.utility.annotation;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.tree.DefaultMutableTreeNode;
import org.icepdf.core.pobjects.NameNode;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.StringObject;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/annotation/NameTreeNode.class */
public class NameTreeNode extends DefaultMutableTreeNode {
    private NameNode item;
    private StringObject name;
    private Reference reference;
    private ResourceBundle messageBundle;
    private MessageFormat formatter;
    private boolean rootNode;
    private boolean intermidiatNode;
    private boolean leaf;
    private boolean loadedChildren;

    public NameTreeNode(NameNode item, ResourceBundle messageBundle) {
        this.item = item;
        this.messageBundle = messageBundle;
        if (!item.hasLimits()) {
            this.rootNode = true;
            setUserObject(messageBundle.getString("viewer.utilityPane.action.dialog.goto.nameTree.root.label"));
            return;
        }
        this.intermidiatNode = true;
        Object[] messageArguments = {item.getLowerLimit(), item.getUpperLimit()};
        if (this.formatter == null) {
            this.formatter = new MessageFormat(messageBundle.getString("viewer.utilityPane.action.dialog.goto.nameTree.branch.label"));
        }
        setUserObject(this.formatter.format(messageArguments));
    }

    public NameTreeNode(StringObject name, Reference ref) {
        this.leaf = true;
        this.name = name;
        this.reference = ref;
        setUserObject(name);
    }

    public void recursivelyClearOutlineItems() {
        this.item = null;
        if (this.loadedChildren) {
            int count = getChildCount();
            for (int i2 = 0; i2 < count; i2++) {
                NameTreeNode node = (NameTreeNode) getChildAt(i2);
                node.recursivelyClearOutlineItems();
            }
        }
    }

    public StringObject getName() {
        return this.name;
    }

    public Reference getReference() {
        return this.reference;
    }

    public boolean isRootNode() {
        return this.rootNode;
    }

    public boolean isIntermidiatNode() {
        return this.intermidiatNode;
    }

    @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.TreeNode
    public boolean isLeaf() {
        return this.leaf;
    }

    public void setRootNode(boolean rootNode) {
        this.rootNode = rootNode;
    }

    @Override // javax.swing.tree.DefaultMutableTreeNode, javax.swing.tree.TreeNode
    public int getChildCount() {
        ensureChildrenLoaded();
        return super.getChildCount();
    }

    private void ensureChildrenLoaded() {
        if (this.loadedChildren) {
            return;
        }
        if (this.intermidiatNode || this.rootNode) {
            this.loadedChildren = true;
            if (this.item.getKidsReferences() != null) {
                int count = this.item.getKidsReferences().size();
                for (int i2 = 0; i2 < count; i2++) {
                    NameNode child = this.item.getNode(i2);
                    NameTreeNode childTreeNode = new NameTreeNode(child, this.messageBundle);
                    add(childTreeNode);
                }
            }
            if (this.item.getNamesAndValues() != null) {
                List namesAndValues = this.item.getNamesAndValues();
                int max = namesAndValues.size();
                for (int i3 = 0; i3 < max; i3 += 2) {
                    StringObject name = (StringObject) namesAndValues.get(i3);
                    Reference ref = (Reference) namesAndValues.get(i3 + 1);
                    add(new NameTreeNode(name, ref));
                }
            }
        }
    }
}
