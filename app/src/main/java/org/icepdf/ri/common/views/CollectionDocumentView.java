package org.icepdf.ri.common.views;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.icepdf.core.pobjects.NameTree;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.StringObject;
import org.icepdf.core.pobjects.actions.FileSpecification;
import org.icepdf.core.util.Library;
import org.icepdf.core.util.Utils;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/CollectionDocumentView.class */
public class CollectionDocumentView extends AbstractDocumentView {
    private JPanel collectionDocumentPanel;

    public CollectionDocumentView(DocumentViewController documentViewController, JScrollPane documentScrollpane, DocumentViewModel documentViewModel) {
        super(documentViewController, documentScrollpane, documentViewModel);
        buildGUI();
    }

    private void buildGUI() {
        setCursor(Cursor.getPredefinedCursor(0));
        final ModifiedFlowLayout layout = new ModifiedFlowLayout();
        layout.setHgap(15);
        layout.setVgap(15);
        this.collectionDocumentPanel = new JPanel(layout);
        this.collectionDocumentPanel.setBackground(backgroundColor);
        setLayout(new BorderLayout());
        add(this.collectionDocumentPanel, BorderLayout.CENTER);
        this.documentScrollpane.getViewport().addChangeListener(new ChangeListener() { // from class: org.icepdf.ri.common.views.CollectionDocumentView.1
            @Override // javax.swing.event.ChangeListener
            public void stateChanged(ChangeEvent e2) {
                JViewport tmp = (JViewport) e2.getSource();
                Dimension dim = layout.computeSize(tmp.getWidth(), CollectionDocumentView.this.collectionDocumentPanel);
                CollectionDocumentView.this.collectionDocumentPanel.setPreferredSize(dim);
            }
        });
        this.documentScrollpane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() { // from class: org.icepdf.ri.common.views.CollectionDocumentView.2
            @Override // java.awt.event.AdjustmentListener
            public void adjustmentValueChanged(AdjustmentEvent e2) {
                if (!e2.getValueIsAdjusting()) {
                    CollectionDocumentView.this.repaint();
                }
            }
        });
        Library library = this.currentDocument.getCatalog().getLibrary();
        NameTree embeddedFilesNameTree = this.currentDocument.getCatalog().getNames().getEmbeddedFilesNameTree();
        List filePairs = embeddedFilesNameTree.getRoot().getNamesAndValues();
        int max = filePairs.size();
        for (int i2 = 0; i2 < max; i2 += 2) {
            String fileName = Utils.convertStringObject(library, (StringObject) filePairs.get(i2));
            HashMap tmp = (HashMap) library.getObject((Reference) filePairs.get(i2 + 1));
            FileSpecification fileSpec = new FileSpecification(library, tmp);
            HashMap tmp2 = fileSpec.getEmbeddedFileDictionary();
            Reference fileRef = (Reference) tmp2.get(FileSpecification.F_KEY);
            DocumentViewComponent documentViewComponent = new DocumentViewComponent(library, fileName, fileRef);
            JPanel documentViewPanel = new JPanel();
            documentViewPanel.setLayout(new BoxLayout(documentViewPanel, 1));
            documentViewPanel.setBackground(backgroundColor);
            PageViewDecorator pageViewComponent = new PageViewDecorator(documentViewComponent);
            pageViewComponent.setAlignmentX(0.5f);
            documentViewPanel.add(pageViewComponent);
            JLabel fileNameLabel = new JLabel(fileName);
            fileNameLabel.setAlignmentX(0.5f);
            documentViewPanel.add(fileNameLabel);
            this.collectionDocumentPanel.add(documentViewPanel);
        }
        this.collectionDocumentPanel.revalidate();
        this.documentScrollpane.validate();
    }

    @Override // org.icepdf.ri.common.views.AbstractDocumentView, org.icepdf.ri.common.views.DocumentView
    public void dispose() {
        super.dispose();
        removeMouseListener(this);
        this.collectionDocumentPanel.removeAll();
    }

    @Override // org.icepdf.ri.common.views.AbstractDocumentView, org.icepdf.ri.common.views.DocumentView
    public void updateDocumentView() {
    }

    @Override // org.icepdf.ri.common.views.DocumentView
    public int getNextPageIncrement() {
        return 0;
    }

    @Override // org.icepdf.ri.common.views.DocumentView
    public int getPreviousPageIncrement() {
        return 0;
    }

    @Override // org.icepdf.ri.common.views.DocumentView
    public Dimension getDocumentSize() {
        return null;
    }
}
