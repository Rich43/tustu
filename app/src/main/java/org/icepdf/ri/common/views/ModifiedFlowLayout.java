package org.icepdf.ri.common.views;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

/* loaded from: icepdf-viewer.jar:org/icepdf/ri/common/views/ModifiedFlowLayout.class */
public class ModifiedFlowLayout extends FlowLayout {
    public Dimension computeSize(int w2, Container target) {
        Dimension dimension;
        synchronized (target.getTreeLock()) {
            int hgap = getHgap();
            int vgap = getVgap();
            if (w2 == 0) {
                w2 = Integer.MAX_VALUE;
            }
            Insets insets = target.getInsets();
            if (insets == null) {
                insets = new Insets(0, 0, 0, 0);
            }
            int reqdWidth = 0;
            int maxwidth = w2 - ((insets.left + insets.right) + (hgap * 2));
            int n2 = target.getComponentCount();
            int x2 = 0;
            int y2 = insets.top + vgap;
            int rowHeight = 0;
            for (int i2 = 0; i2 < n2; i2++) {
                Component c2 = target.getComponent(i2);
                if (c2.isVisible()) {
                    Dimension d2 = c2.getPreferredSize();
                    if (x2 == 0 || x2 + d2.width <= maxwidth) {
                        if (x2 > 0) {
                            x2 += hgap;
                        }
                        x2 += d2.width;
                        rowHeight = Math.max(rowHeight, d2.height);
                    } else {
                        x2 = d2.width;
                        y2 += vgap + rowHeight;
                        rowHeight = d2.height;
                    }
                    reqdWidth = Math.max(reqdWidth, x2);
                }
            }
            dimension = new Dimension(reqdWidth + insets.left + insets.right, y2 + rowHeight + insets.bottom);
        }
        return dimension;
    }
}
