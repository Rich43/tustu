package com.efiAnalytics.apps.ts.tuningViews;

import G.C0113cs;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.eJ;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/TuneViewComponent.class */
public abstract class TuneViewComponent extends JPanel implements InterfaceC1565bc {

    /* renamed from: i, reason: collision with root package name */
    private JButton f9756i;

    /* renamed from: c, reason: collision with root package name */
    private double f9749c = 0.0d;

    /* renamed from: d, reason: collision with root package name */
    private double f9750d = 0.0d;

    /* renamed from: e, reason: collision with root package name */
    private double f9751e = 0.25d;

    /* renamed from: f, reason: collision with root package name */
    private double f9752f = 0.3d;

    /* renamed from: g, reason: collision with root package name */
    private String f9753g = "";

    /* renamed from: h, reason: collision with root package name */
    private boolean f9754h = false;

    /* renamed from: a, reason: collision with root package name */
    List f9755a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    protected String f9757b = "";

    /* renamed from: j, reason: collision with root package name */
    private boolean f9758j = false;

    public TuneViewComponent() {
        this.f9756i = null;
        if (G.T.a().d().length > 1) {
            this.f9756i = new JButton(G.T.a().c().c());
            this.f9756i.setPreferredSize(eJ.a(170, 16));
            this.f9756i.addActionListener(new C1436i(this));
        }
    }

    public abstract void initializeComponents();

    /* JADX INFO: Access modifiers changed from: private */
    public void showEcuConfigPopup() {
        JPopupMenu jPopupMenu = new JPopupMenu();
        C1437j c1437j = new C1437j(this);
        for (String str : G.T.a().d()) {
            JMenuItem jMenuItemAdd = jPopupMenu.add(str);
            jMenuItemAdd.setActionCommand(str);
            jMenuItemAdd.addActionListener(c1437j);
            jPopupMenu.add(jMenuItemAdd);
        }
        this.f9756i.add(jPopupMenu);
        jPopupMenu.show(this.f9756i, 0, this.f9756i.getHeight());
    }

    public double getRelativeX() {
        return this.f9749c;
    }

    public void setRelativeX(double d2) {
        this.f9749c = d2;
    }

    public double getRelativeY() {
        return this.f9750d;
    }

    public void setRelativeY(double d2) {
        this.f9750d = d2;
    }

    public double getRelativeWidth() {
        return this.f9751e;
    }

    public void setRelativeWidth(double d2) {
        this.f9751e = d2;
    }

    public double getRelativeHeight() {
        return this.f9752f;
    }

    public void setRelativeHeight(double d2) {
        this.f9752f = d2;
    }

    @Override // java.awt.Component
    public String toString() throws SecurityException {
        Field[] declaredFields = getClass().getDeclaredFields();
        AccessibleObject.setAccessible(declaredFields, true);
        String name = getClass().getName();
        for (Field field : declaredFields) {
            try {
                name = name + "\n\t" + field.getName() + "=" + field.get(this) + ", Generic String:" + field.toGenericString() + ", ";
            } catch (Exception e2) {
            }
        }
        return name + "\n";
    }

    public boolean isInvalidState() {
        return this.f9758j;
    }

    protected void setInvalidState(boolean z2) {
        this.f9758j = z2;
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        super.setBounds(i2, i3, i4, i5);
    }

    public void updateRelativeBoundsToCurrent() {
        if (getParent() == null || getParent().getWidth() <= 0 || getParent().getHeight() <= 0) {
            return;
        }
        this.f9749c = super.getX() / getParent().getWidth();
        this.f9750d = super.getY() / getParent().getHeight();
        this.f9751e = super.getWidth() / getParent().getWidth();
        this.f9752f = super.getHeight() / getParent().getHeight();
    }

    public String getEcuConfigurationName() {
        return (this.f9757b == null || !this.f9757b.equals(C0113cs.f1154a)) ? (aE.a.A() == null || !aE.a.A().u().equals(this.f9757b)) ? (this.f9757b == null || this.f9757b.isEmpty()) ? this.f9757b : this.f9757b : "" : this.f9757b;
    }

    public void setEcuConfigurationName(String str) {
        if (str == null || str.equals(FXMLLoader.NULL_KEYWORD)) {
            this.f9757b = "";
        } else {
            this.f9757b = str;
        }
        if (this.f9756i != null) {
            if (this.f9757b.isEmpty()) {
                this.f9756i.setText(G.T.a().c().c());
            } else {
                this.f9756i.setText(this.f9757b);
            }
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public abstract void close();

    public String getId() {
        return this.f9753g;
    }

    public void setId(String str) {
        this.f9753g = str;
    }

    public void enableEditMode(boolean z2) {
        if (z2) {
            setBorder(BorderFactory.createMatteBorder(eJ.a(14), eJ.a(7), eJ.a(14), eJ.a(7), Color.GRAY));
            if (this.f9756i != null) {
                add(this.f9756i);
            }
        } else {
            setBorder(null);
            if (this.f9756i != null) {
                remove(this.f9756i);
            }
        }
        this.f9754h = z2;
    }

    public boolean isShieldedDuringEdit() {
        return true;
    }

    @Override // java.awt.Component
    public void addMouseListener(MouseListener mouseListener) {
        super.addMouseListener(mouseListener);
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            getComponent(i2).addMouseListener(mouseListener);
        }
    }

    @Override // java.awt.Component
    public void addMouseMotionListener(MouseMotionListener mouseMotionListener) {
        super.addMouseMotionListener(mouseMotionListener);
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            getComponent(i2).addMouseMotionListener(mouseMotionListener);
        }
    }

    @Override // java.awt.Component
    public void removeMouseListener(MouseListener mouseListener) {
        super.removeMouseListener(mouseListener);
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            getComponent(i2).removeMouseListener(mouseListener);
        }
    }

    @Override // java.awt.Component
    public void removeMouseMotionListener(MouseMotionListener mouseMotionListener) {
        super.removeMouseMotionListener(mouseMotionListener);
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            getComponent(i2).removeMouseMotionListener(mouseMotionListener);
        }
    }

    @Override // javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    public abstract boolean isDirty();

    public abstract void setClean(boolean z2);

    public boolean isEditMode() {
        return this.f9754h;
    }

    public void addEditComponent(Component component) {
        this.f9755a.add(component);
    }

    public void removeEditComponent(Component component) {
        this.f9755a.remove(component);
    }

    public boolean isEditComponent(Component component) {
        return this.f9755a.contains(component);
    }

    protected JButton getBtnSelectEcuConfig() {
        return this.f9756i;
    }
}
