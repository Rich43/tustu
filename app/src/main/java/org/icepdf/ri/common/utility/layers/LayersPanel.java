package org.icepdf.ri.common.utility.layers;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.OptionalContent;
import org.icepdf.core.pobjects.OptionalContentGroup;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.AbstractPageViewComponent;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewModel;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/layers/LayersPanel.class */
public class LayersPanel extends JPanel {
    protected DocumentViewController documentViewController;
    protected Document currentDocument;
    private SwingController controller;
    protected LayersTreeNode nodes;
    protected DocumentViewModel documentViewModel;
    ResourceBundle messageBundle;

    public LayersPanel(SwingController controller) {
        super(true);
        setFocusable(true);
        this.controller = controller;
        this.messageBundle = this.controller.getMessageBundle();
    }

    private void buildUI() {
        JTree tree = new LayersTree(this.nodes);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
        tree.addMouseListener(new NodeSelectionListener(tree));
        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(tree, 22, 30);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setDocument(Document document) {
        this.currentDocument = document;
        this.documentViewController = this.controller.getDocumentViewController();
        this.documentViewModel = this.documentViewController.getDocumentViewModel();
        if (this.currentDocument != null) {
            OptionalContent optionalContent = this.currentDocument.getCatalog().getOptionalContent();
            List<Object> layersOrder = optionalContent.getOrder();
            if (layersOrder != null) {
                boolean hasRadioButtons = optionalContent.getRbGroups() != null && optionalContent.getRbGroups().size() > 0;
                this.nodes = new LayersTreeNode("Layers");
                this.nodes.setAllowsChildren(true);
                buildTree(layersOrder, this.nodes, hasRadioButtons);
                buildUI();
                return;
            }
            return;
        }
        removeAll();
    }

    public void buildTree(List<Object> layersOrder, LayersTreeNode parent, boolean radioGroup) {
        LayersTreeNode layersTreeNode;
        LayersTreeNode tmp = null;
        boolean selected = true;
        for (Object obj : layersOrder) {
            if (obj instanceof List) {
                if (parent.getChildCount() > 0) {
                    layersTreeNode = (LayersTreeNode) parent.getLastChild();
                } else {
                    layersTreeNode = parent;
                }
                LayersTreeNode newParent = layersTreeNode;
                buildTree((List) obj, newParent, radioGroup);
            } else if (obj instanceof String) {
                if (tmp != null && selected) {
                    tmp.setSelected(true);
                }
                tmp = new LayersTreeNode(obj);
                tmp.setAllowsChildren(true);
                this.nodes.add(tmp);
                selected = true;
            } else if (obj instanceof OptionalContentGroup) {
                LayersTreeNode node = new LayersTreeNode(obj);
                node.setAllowsChildren(true);
                if (radioGroup) {
                    node.setSelectionMode(2);
                }
                parent.add(node);
                if (!node.isSelected()) {
                    selected = false;
                }
            }
        }
    }

    public void dispose() {
        removeAll();
    }

    /* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/utility/layers/LayersPanel$NodeSelectionListener.class */
    class NodeSelectionListener extends MouseAdapter {
        JTree tree;

        NodeSelectionListener(JTree tree) {
            this.tree = tree;
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseClicked(MouseEvent e2) {
            int x2 = e2.getX();
            int y2 = e2.getY();
            int row = this.tree.getRowForLocation(x2, y2);
            TreePath path = this.tree.getPathForRow(row);
            if (path != null) {
                LayersTreeNode node = (LayersTreeNode) path.getLastPathComponent();
                boolean isSelected = !node.isSelected();
                node.setSelected(isSelected);
                List<AbstractPageViewComponent> pages = LayersPanel.this.documentViewModel.getPageComponents();
                AbstractPageViewComponent page = pages.get(LayersPanel.this.documentViewModel.getViewCurrentPageIndex());
                page.invalidatePageBuffer();
                try {
                    page.getPage().getText().sortAndFormatText();
                } catch (InterruptedException e3) {
                }
                page.repaint();
                this.tree.repaint();
                ((DefaultTreeModel) this.tree.getModel()).nodeChanged(node);
                if (row == 0) {
                    this.tree.revalidate();
                    this.tree.repaint();
                }
            }
        }
    }
}
