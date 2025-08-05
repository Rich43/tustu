package org.icepdf.ri.common.views;

import java.util.ArrayList;
import javax.swing.JScrollPane;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.PageTree;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/DocumentViewModelImpl.class */
public class DocumentViewModelImpl extends AbstractDocumentViewModel {
    public DocumentViewModelImpl(Document document, JScrollPane parentScrollPane) {
        super(document);
        PageViewComponentImpl pageViewComponentImpl = null;
        PageTree pageTree = document.getPageTree();
        int numberOfPages = document.getNumberOfPages();
        int avgPageWidth = 0;
        int avgPageHeight = 0;
        this.pageComponents = new ArrayList(numberOfPages);
        for (int i2 = 0; i2 < numberOfPages; i2++) {
            if (i2 < 10) {
                pageViewComponentImpl = new PageViewComponentImpl(this, pageTree, i2, parentScrollPane);
                avgPageWidth += pageViewComponentImpl.getPreferredSize().width;
                avgPageHeight += pageViewComponentImpl.getPreferredSize().height;
            } else if (i2 > 10) {
                pageViewComponentImpl = new PageViewComponentImpl(this, pageTree, i2, parentScrollPane, avgPageWidth, avgPageHeight);
            } else if (i2 == 10) {
                avgPageWidth /= 10;
                avgPageHeight /= 10;
                pageViewComponentImpl = new PageViewComponentImpl(this, pageTree, i2, parentScrollPane, avgPageWidth, avgPageHeight);
            }
            this.pageComponents.add(pageViewComponentImpl);
        }
    }
}
