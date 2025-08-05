package com.efiAnalytics.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bW.class */
final class bW implements PropertyChangeListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f10982a;

    bW(String str) {
        this.f10982a = str;
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        File[] selectedFiles = bV.f10972b.getSelectedFiles();
        String name = selectedFiles.length >= 1 ? selectedFiles[0].getName() : this.f10982a;
        if ((propertyChangeEvent.getNewValue() instanceof C1613cx) && bV.f10972b.isShowing() && name != null && name.contains(".")) {
            bV.f10972b.setSelectedFile(new File(name.substring(0, name.lastIndexOf(".") + 1) + ((C1613cx) propertyChangeEvent.getNewValue()).a()));
        }
    }
}
