package com.efiAnalytics.ui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eI.class */
class eI implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ eD f11483a;

    eI(eD eDVar) {
        this.f11483a = eDVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        this.f11483a.f11474a = new Stage();
        this.f11483a.f11474a.setTitle("Hello Java FX");
        this.f11483a.f11474a.setResizable(true);
        Group group = new Group();
        Scene scene = new Scene(group, 80.0d, 20.0d);
        this.f11483a.f11474a.setScene(scene);
        this.f11483a.f11475b = new WebView();
        this.f11483a.f11478e = this.f11483a.f11475b.getEngine();
        this.f11483a.f11478e.load("https://www.efianalytics.com/register/browseProducts.jsp");
        group.getChildren().add(this.f11483a.f11475b);
        this.f11483a.f11476c.setScene(scene);
    }
}
