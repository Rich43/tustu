package javax.swing;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/* loaded from: rt.jar:javax/swing/TablePrintable.class */
class TablePrintable implements Printable {
    private JTable table;
    private JTableHeader header;
    private TableColumnModel colModel;
    private int totalColWidth;
    private JTable.PrintMode printMode;
    private MessageFormat headerFormat;
    private MessageFormat footerFormat;
    private int last = -1;
    private int row = 0;
    private int col = 0;
    private final Rectangle clip = new Rectangle(0, 0, 0, 0);
    private final Rectangle hclip = new Rectangle(0, 0, 0, 0);
    private final Rectangle tempRect = new Rectangle(0, 0, 0, 0);
    private static final int H_F_SPACE = 8;
    private static final float HEADER_FONT_SIZE = 18.0f;
    private static final float FOOTER_FONT_SIZE = 12.0f;
    private Font headerFont;
    private Font footerFont;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !TablePrintable.class.desiredAssertionStatus();
    }

    public TablePrintable(JTable jTable, JTable.PrintMode printMode, MessageFormat messageFormat, MessageFormat messageFormat2) {
        this.table = jTable;
        this.header = jTable.getTableHeader();
        this.colModel = jTable.getColumnModel();
        this.totalColWidth = this.colModel.getTotalColumnWidth();
        if (this.header != null) {
            this.hclip.height = this.header.getHeight();
        }
        this.printMode = printMode;
        this.headerFormat = messageFormat;
        this.footerFormat = messageFormat2;
        this.headerFont = jTable.getFont().deriveFont(1, HEADER_FONT_SIZE);
        this.footerFont = jTable.getFont().deriveFont(0, 12.0f);
    }

    @Override // java.awt.print.Printable
    public int print(Graphics graphics, PageFormat pageFormat, int i2) throws PrinterException {
        int imageableWidth = (int) pageFormat.getImageableWidth();
        int imageableHeight = (int) pageFormat.getImageableHeight();
        if (imageableWidth <= 0) {
            throw new PrinterException("Width of printable area is too small.");
        }
        Object[] objArr = {Integer.valueOf(i2 + 1)};
        String str = null;
        if (this.headerFormat != null) {
            str = this.headerFormat.format(objArr);
        }
        String str2 = null;
        if (this.footerFormat != null) {
            str2 = this.footerFormat.format(objArr);
        }
        Rectangle2D stringBounds = null;
        Rectangle2D stringBounds2 = null;
        int iCeil = 0;
        int iCeil2 = 0;
        int i3 = imageableHeight;
        if (str != null) {
            graphics.setFont(this.headerFont);
            stringBounds = graphics.getFontMetrics().getStringBounds(str, graphics);
            iCeil = (int) Math.ceil(stringBounds.getHeight());
            i3 -= iCeil + 8;
        }
        if (str2 != null) {
            graphics.setFont(this.footerFont);
            stringBounds2 = graphics.getFontMetrics().getStringBounds(str2, graphics);
            iCeil2 = (int) Math.ceil(stringBounds2.getHeight());
            i3 -= iCeil2 + 8;
        }
        if (i3 <= 0) {
            throw new PrinterException("Height of printable area is too small.");
        }
        double d2 = 1.0d;
        if (this.printMode == JTable.PrintMode.FIT_WIDTH && this.totalColWidth > imageableWidth) {
            if (!$assertionsDisabled && imageableWidth <= 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && this.totalColWidth <= 1) {
                throw new AssertionError();
            }
            d2 = imageableWidth / this.totalColWidth;
        }
        if (!$assertionsDisabled && d2 <= 0.0d) {
            throw new AssertionError();
        }
        while (this.last < i2) {
            if (this.row >= this.table.getRowCount() && this.col == 0) {
                return 1;
            }
            findNextClip((int) (imageableWidth / d2), (int) ((i3 - this.hclip.height) / d2));
            this.last++;
        }
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        if (str2 != null) {
            AffineTransform transform = graphics2D.getTransform();
            graphics2D.translate(0, imageableHeight - iCeil2);
            printText(graphics2D, str2, stringBounds2, this.footerFont, imageableWidth);
            graphics2D.setTransform(transform);
        }
        if (str != null) {
            printText(graphics2D, str, stringBounds, this.headerFont, imageableWidth);
            graphics2D.translate(0, iCeil + 8);
        }
        this.tempRect.f12372x = 0;
        this.tempRect.f12373y = 0;
        this.tempRect.width = imageableWidth;
        this.tempRect.height = i3;
        graphics2D.clip(this.tempRect);
        if (d2 != 1.0d) {
            graphics2D.scale(d2, d2);
        } else {
            graphics2D.translate((imageableWidth - this.clip.width) / 2, 0);
        }
        AffineTransform transform2 = graphics2D.getTransform();
        Shape clip = graphics2D.getClip();
        if (this.header != null) {
            this.hclip.f12372x = this.clip.f12372x;
            this.hclip.width = this.clip.width;
            graphics2D.translate(-this.hclip.f12372x, 0);
            graphics2D.clip(this.hclip);
            this.header.print(graphics2D);
            graphics2D.setTransform(transform2);
            graphics2D.setClip(clip);
            graphics2D.translate(0, this.hclip.height);
        }
        graphics2D.translate(-this.clip.f12372x, -this.clip.f12373y);
        graphics2D.clip(this.clip);
        this.table.print(graphics2D);
        graphics2D.setTransform(transform2);
        graphics2D.setClip(clip);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(0, 0, this.clip.width, this.hclip.height + this.clip.height);
        graphics2D.dispose();
        return 0;
    }

    private void printText(Graphics2D graphics2D, String str, Rectangle2D rectangle2D, Font font, int i2) {
        int width;
        if (rectangle2D.getWidth() < i2) {
            width = (int) ((i2 - rectangle2D.getWidth()) / 2.0d);
        } else if (this.table.getComponentOrientation().isLeftToRight()) {
            width = 0;
        } else {
            width = -((int) (Math.ceil(rectangle2D.getWidth()) - i2));
        }
        int iCeil = (int) Math.ceil(Math.abs(rectangle2D.getY()));
        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(font);
        graphics2D.drawString(str, width, iCeil);
    }

    private void findNextClip(int i2, int i3) {
        boolean zIsLeftToRight = this.table.getComponentOrientation().isLeftToRight();
        if (this.col == 0) {
            if (zIsLeftToRight) {
                this.clip.f12372x = 0;
            } else {
                this.clip.f12372x = this.totalColWidth;
            }
            this.clip.f12373y += this.clip.height;
            this.clip.width = 0;
            this.clip.height = 0;
            int rowCount = this.table.getRowCount();
            int rowHeight = this.table.getRowHeight(this.row);
            do {
                this.clip.height += rowHeight;
                int i4 = this.row + 1;
                this.row = i4;
                if (i4 >= rowCount) {
                    break;
                } else {
                    rowHeight = this.table.getRowHeight(this.row);
                }
            } while (this.clip.height + rowHeight <= i3);
        }
        if (this.printMode == JTable.PrintMode.FIT_WIDTH) {
            this.clip.f12372x = 0;
            this.clip.width = this.totalColWidth;
            return;
        }
        if (zIsLeftToRight) {
            this.clip.f12372x += this.clip.width;
        }
        this.clip.width = 0;
        int columnCount = this.table.getColumnCount();
        int width = this.colModel.getColumn(this.col).getWidth();
        do {
            this.clip.width += width;
            if (!zIsLeftToRight) {
                this.clip.f12372x -= width;
            }
            int i5 = this.col + 1;
            this.col = i5;
            if (i5 >= columnCount) {
                this.col = 0;
                return;
            }
            width = this.colModel.getColumn(this.col).getWidth();
        } while (this.clip.width + width <= i2);
    }
}
