package org.icepdf.ri.common.views;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.PDimension;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.pobjects.Reference;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.util.Library;
import org.icepdf.ri.viewer.WindowManager;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/DocumentViewComponent.class */
public class DocumentViewComponent extends JComponent implements MouseListener, Runnable {
    private Library parentLibrary;
    private Reference fileReference;
    private String fileName;
    private boolean isPdfDocument;
    public static final String PDF_EXTENSION = ".pdf";
    private static int minimumThumbHeight = 110;
    private static int minimumThumbWidth = 85;
    private Dimension pageSize = new Dimension(minimumThumbWidth, minimumThumbHeight);
    private SoftReference<BufferedImage> documentThumbNail = new SoftReference<>(null);

    public DocumentViewComponent(Library parentLibrary, String fileName, Reference fileReference) {
        this.parentLibrary = parentLibrary;
        this.fileName = fileName;
        this.fileReference = fileReference;
        addMouseListener(this);
        setCursor(Cursor.getPredefinedCursor(12));
        this.isPdfDocument = fileName.toLowerCase().endsWith(PDF_EXTENSION);
        if (this.isPdfDocument) {
            Library.execute(this);
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            Stream fileStream = (Stream) this.parentLibrary.getObject(this.fileReference);
            InputStream fileInputStream = fileStream.getDecodedByteArrayInputStream();
            Document embeddedDocument = new Document();
            embeddedDocument.setInputStream(fileInputStream, this.fileName);
            Page page = embeddedDocument.getPageTree().getPage(0);
            page.init();
            PDimension defaultSize = page.getSize(2, 0.0f, 1.0f);
            float scale = minimumThumbHeight / defaultSize.getHeight();
            this.pageSize = page.getSize(2, 0.0f, scale).toDimension();
            int pageWidth = (int) this.pageSize.getWidth();
            int pageHeight = (int) this.pageSize.getHeight();
            BufferedImage image = new BufferedImage(pageWidth, pageHeight, 1);
            Graphics g2 = image.createGraphics();
            page.paint(g2, 2, 2, 0.0f, scale);
            g2.dispose();
            this.documentThumbNail = new SoftReference<>(image);
            SwingUtilities.invokeLater(new Runnable() { // from class: org.icepdf.ri.common.views.DocumentViewComponent.1
                @Override // java.lang.Runnable
                public void run() {
                    DocumentViewComponent.this.revalidate();
                    DocumentViewComponent.this.repaint();
                }
            });
            embeddedDocument.dispose();
        } catch (Throwable th) {
            this.isPdfDocument = false;
        }
    }

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics gg) {
        BufferedImage pageThumbNail = this.documentThumbNail.get();
        if (pageThumbNail != null) {
            Graphics2D g2 = (Graphics2D) gg;
            g2.drawImage(pageThumbNail, 0, 0, (ImageObserver) null);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return this.pageSize;
    }

    @Override // java.awt.event.MouseListener
    public void mouseClicked(MouseEvent e2) {
        if (e2.getClickCount() == 2 && this.isPdfDocument) {
            try {
                Stream fileStream = (Stream) this.parentLibrary.getObject(this.fileReference);
                InputStream fileInputStream = fileStream.getDecodedByteArrayInputStream();
                Document embeddedDocument = new Document();
                embeddedDocument.setInputStream(fileInputStream, this.fileName);
                WindowManager.getInstance().newWindow(embeddedDocument, this.fileName);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override // java.awt.event.MouseListener
    public void mousePressed(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseReleased(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseEntered(MouseEvent e2) {
    }

    @Override // java.awt.event.MouseListener
    public void mouseExited(MouseEvent e2) {
    }
}
