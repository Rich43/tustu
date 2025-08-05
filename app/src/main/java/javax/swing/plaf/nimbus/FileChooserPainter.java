package javax.swing.plaf.nimbus;

import com.sun.imageio.plugins.jpeg.JPEG;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;
import javax.swing.plaf.nimbus.AbstractRegionPainter;

/* loaded from: rt.jar:javax/swing/plaf/nimbus/FileChooserPainter.class */
final class FileChooserPainter extends AbstractRegionPainter {
    static final int BACKGROUND_ENABLED = 1;
    static final int FILEICON_ENABLED = 2;
    static final int DIRECTORYICON_ENABLED = 3;
    static final int UPFOLDERICON_ENABLED = 4;
    static final int NEWFOLDERICON_ENABLED = 5;
    static final int COMPUTERICON_ENABLED = 6;
    static final int HARDDRIVEICON_ENABLED = 7;
    static final int FLOPPYDRIVEICON_ENABLED = 8;
    static final int HOMEFOLDERICON_ENABLED = 9;
    static final int DETAILSVIEWICON_ENABLED = 10;
    static final int LISTVIEWICON_ENABLED = 11;
    private int state;
    private AbstractRegionPainter.PaintContext ctx;
    private Path2D path = new Path2D.Float();
    private Rectangle2D rect = new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private RoundRectangle2D roundRect = new RoundRectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    private Ellipse2D ellipse = new Ellipse2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    private Color color1 = decodeColor("control", 0.0f, 0.0f, 0.0f, 0);
    private Color color2 = decodeColor("nimbusBlueGrey", 0.007936537f, -0.065654516f, -0.13333333f, 0);
    private Color color3 = new Color(97, 98, 102, 255);
    private Color color4 = decodeColor("nimbusBlueGrey", -0.032679737f, -0.043332636f, 0.24705881f, 0);
    private Color color5 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, 0);
    private Color color6 = decodeColor("nimbusBase", 0.0077680945f, -0.51781034f, 0.3490196f, 0);
    private Color color7 = decodeColor("nimbusBase", 0.013940871f, -0.599277f, 0.41960782f, 0);
    private Color color8 = decodeColor("nimbusBase", 0.004681647f, -0.4198052f, 0.14117646f, 0);
    private Color color9 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, -127);
    private Color color10 = decodeColor("nimbusBlueGrey", 0.0f, 0.0f, -0.21f, -99);
    private Color color11 = decodeColor("nimbusBase", 2.9569864E-4f, -0.45978838f, 0.2980392f, 0);
    private Color color12 = decodeColor("nimbusBase", 0.0015952587f, -0.34848025f, 0.18823528f, 0);
    private Color color13 = decodeColor("nimbusBase", 0.0015952587f, -0.30844158f, 0.09803921f, 0);
    private Color color14 = decodeColor("nimbusBase", 0.0015952587f, -0.27329817f, 0.035294116f, 0);
    private Color color15 = decodeColor("nimbusBase", 0.004681647f, -0.6198413f, 0.43921566f, 0);
    private Color color16 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, -125);
    private Color color17 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, -50);
    private Color color18 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, -100);
    private Color color19 = decodeColor("nimbusBase", 0.0012094378f, -0.23571429f, -0.0784314f, 0);
    private Color color20 = decodeColor("nimbusBase", 2.9569864E-4f, -0.115166366f, -0.2627451f, 0);
    private Color color21 = decodeColor("nimbusBase", 0.0027436614f, -0.335015f, 0.011764705f, 0);
    private Color color22 = decodeColor("nimbusBase", 0.0024294257f, -0.3857143f, 0.031372547f, 0);
    private Color color23 = decodeColor("nimbusBase", 0.0018081069f, -0.3595238f, -0.13725492f, 0);
    private Color color24 = new Color(255, 200, 0, 255);
    private Color color25 = decodeColor("nimbusBase", 0.004681647f, -0.44904763f, 0.039215684f, 0);
    private Color color26 = decodeColor("nimbusBase", 0.0015952587f, -0.43718487f, -0.015686274f, 0);
    private Color color27 = decodeColor("nimbusBase", 2.9569864E-4f, -0.39212453f, -0.24313727f, 0);
    private Color color28 = decodeColor("nimbusBase", 0.004681647f, -0.6117143f, 0.43137252f, 0);
    private Color color29 = decodeColor("nimbusBase", 0.0012094378f, -0.28015873f, -0.019607842f, 0);
    private Color color30 = decodeColor("nimbusBase", 0.00254488f, -0.07049692f, -0.2784314f, 0);
    private Color color31 = decodeColor("nimbusBase", 0.0015952587f, -0.28045115f, 0.04705882f, 0);
    private Color color32 = decodeColor("nimbusBlueGrey", 0.0f, 5.847961E-4f, -0.21568626f, 0);
    private Color color33 = decodeColor("nimbusBase", -0.0061469674f, 0.3642857f, 0.14509803f, 0);
    private Color color34 = decodeColor("nimbusBase", 0.0053939223f, 0.3642857f, -0.0901961f, 0);
    private Color color35 = decodeColor("nimbusBase", 0.0f, -0.6357143f, 0.45098037f, 0);
    private Color color36 = decodeColor("nimbusBase", -0.006044388f, -0.23963585f, 0.45098037f, 0);
    private Color color37 = decodeColor("nimbusBase", -0.0063245893f, 0.01592505f, 0.4078431f, 0);
    private Color color38 = decodeColor("nimbusBlueGrey", 0.0f, -0.110526316f, 0.25490195f, -170);
    private Color color39 = decodeColor("nimbusOrange", -0.032758567f, -0.018273294f, 0.25098038f, 0);
    private Color color40 = new Color(255, 255, 255, 255);
    private Color color41 = new Color(252, 255, 92, 255);
    private Color color42 = new Color(253, 191, 4, 255);
    private Color color43 = new Color(160, 161, 163, 255);
    private Color color44 = new Color(0, 0, 0, 255);
    private Color color45 = new Color(239, 241, 243, 255);
    private Color color46 = new Color(197, 201, 205, 255);
    private Color color47 = new Color(105, 110, 118, 255);
    private Color color48 = new Color(63, 67, 72, 255);
    private Color color49 = new Color(56, 51, 25, 255);
    private Color color50 = new Color(144, 255, 0, 255);
    private Color color51 = new Color(243, 245, 246, 255);
    private Color color52 = new Color(208, 212, 216, 255);
    private Color color53 = new Color(191, 193, 194, 255);
    private Color color54 = new Color(170, 172, 175, 255);
    private Color color55 = new Color(152, 155, 158, 255);
    private Color color56 = new Color(59, 62, 66, 255);
    private Color color57 = new Color(46, 46, 46, 255);
    private Color color58 = new Color(64, 64, 64, 255);
    private Color color59 = new Color(43, 43, 43, 255);
    private Color color60 = new Color(164, 179, 206, 255);
    private Color color61 = new Color(97, 123, 170, 255);
    private Color color62 = new Color(53, 86, 146, 255);
    private Color color63 = new Color(48, 82, 144, 255);
    private Color color64 = new Color(71, 99, 150, 255);
    private Color color65 = new Color(224, 224, 224, 255);
    private Color color66 = new Color(JPEG.APP8, JPEG.APP8, JPEG.APP8, 255);
    private Color color67 = new Color(231, 234, 237, 255);
    private Color color68 = new Color(205, 211, 215, 255);
    private Color color69 = new Color(149, 153, 156, 54);
    private Color color70 = new Color(255, 122, 101, 255);
    private Color color71 = new Color(54, 78, 122, 255);
    private Color color72 = new Color(51, 60, 70, 255);
    private Color color73 = new Color(228, JPEG.APP8, 237, 255);
    private Color color74 = new Color(27, 57, 87, 255);
    private Color color75 = new Color(75, 109, 137, 255);
    private Color color76 = new Color(77, 133, 185, 255);
    private Color color77 = new Color(81, 59, 7, 255);
    private Color color78 = new Color(97, 74, 18, 255);
    private Color color79 = new Color(137, 115, 60, 255);
    private Color color80 = new Color(174, 151, 91, 255);
    private Color color81 = new Color(114, 92, 13, 255);
    private Color color82 = new Color(64, 48, 0, 255);
    private Color color83 = new Color(244, 222, 143, 255);
    private Color color84 = new Color(160, 161, 162, 255);
    private Color color85 = new Color(226, 230, 233, 255);
    private Color color86 = new Color(221, 225, 230, 255);
    private Color color87 = decodeColor("nimbusBase", 0.004681647f, -0.48756614f, 0.19215685f, 0);
    private Color color88 = decodeColor("nimbusBase", 0.004681647f, -0.48399013f, 0.019607842f, 0);
    private Color color89 = decodeColor("nimbusBase", -0.0028941035f, -0.5906323f, 0.4078431f, 0);
    private Color color90 = decodeColor("nimbusBase", 0.004681647f, -0.51290727f, 0.34509802f, 0);
    private Color color91 = decodeColor("nimbusBase", 0.009583652f, -0.5642857f, 0.3843137f, 0);
    private Color color92 = decodeColor("nimbusBase", -0.0072231293f, -0.6074885f, 0.4235294f, 0);
    private Color color93 = decodeColor("nimbusBase", 7.13408E-4f, -0.52158386f, 0.17254901f, 0);
    private Color color94 = decodeColor("nimbusBase", 0.012257397f, -0.5775132f, 0.19215685f, 0);
    private Color color95 = decodeColor("nimbusBase", 0.08801502f, -0.6164835f, -0.14117649f, 0);
    private Color color96 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.5019608f, 0);
    private Color color97 = decodeColor("nimbusBase", -0.0036516786f, -0.555393f, 0.42745095f, 0);
    private Color color98 = decodeColor("nimbusBase", -0.0010654926f, -0.3634138f, 0.2862745f, 0);
    private Color color99 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.29803923f, 0);
    private Color color100 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, 0.12156862f, 0);
    private Color color101 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.54901963f, 0);
    private Color color102 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.48627454f, 0);
    private Color color103 = decodeColor("nimbusBase", -0.57865167f, -0.6357143f, -0.007843137f, 0);
    private Color color104 = decodeColor("nimbusBase", -0.0028941035f, -0.5408867f, -0.09411767f, 0);
    private Color color105 = decodeColor("nimbusBase", -0.011985004f, -0.54721874f, -0.10588238f, 0);
    private Color color106 = decodeColor("nimbusBase", -0.0022627711f, -0.4305861f, -0.0901961f, 0);
    private Color color107 = decodeColor("nimbusBase", -0.00573498f, -0.447479f, -0.21568629f, 0);
    private Color color108 = decodeColor("nimbusBase", 0.004681647f, -0.53271f, 0.36470586f, 0);
    private Color color109 = decodeColor("nimbusBase", 0.004681647f, -0.5276062f, -0.11372551f, 0);
    private Color color110 = decodeColor("nimbusBase", -8.738637E-4f, -0.5278006f, -0.0039215684f, 0);
    private Color color111 = decodeColor("nimbusBase", -0.0028941035f, -0.5338625f, -0.12549022f, 0);
    private Color color112 = decodeColor("nimbusBlueGrey", -0.03535354f, -0.008674465f, -0.32156864f, 0);
    private Color color113 = decodeColor("nimbusBlueGrey", -0.027777791f, -0.010526314f, -0.3529412f, 0);
    private Color color114 = decodeColor("nimbusBase", -0.0028941035f, -0.5234694f, -0.1647059f, 0);
    private Color color115 = decodeColor("nimbusBase", 0.004681647f, -0.53401935f, -0.086274534f, 0);
    private Color color116 = decodeColor("nimbusBase", 0.004681647f, -0.52077174f, -0.20784315f, 0);
    private Color color117 = new Color(108, 114, 120, 255);
    private Color color118 = new Color(77, 82, 87, 255);
    private Color color119 = decodeColor("nimbusBase", -0.004577577f, -0.52179027f, -0.2392157f, 0);
    private Color color120 = decodeColor("nimbusBase", -0.004577577f, -0.547479f, -0.14901963f, 0);
    private Color color121 = new Color(186, 186, 186, 50);
    private Color color122 = new Color(186, 186, 186, 40);
    private Object[] componentColors;

    public FileChooserPainter(AbstractRegionPainter.PaintContext paintContext, int i2) {
        this.state = i2;
        this.ctx = paintContext;
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected void doPaint(Graphics2D graphics2D, JComponent jComponent, int i2, int i3, Object[] objArr) {
        this.componentColors = objArr;
        switch (this.state) {
            case 1:
                paintBackgroundEnabled(graphics2D);
                break;
            case 2:
                paintfileIconEnabled(graphics2D);
                break;
            case 3:
                paintdirectoryIconEnabled(graphics2D);
                break;
            case 4:
                paintupFolderIconEnabled(graphics2D);
                break;
            case 5:
                paintnewFolderIconEnabled(graphics2D);
                break;
            case 7:
                painthardDriveIconEnabled(graphics2D);
                break;
            case 8:
                paintfloppyDriveIconEnabled(graphics2D);
                break;
            case 9:
                painthomeFolderIconEnabled(graphics2D);
                break;
            case 10:
                paintdetailsViewIconEnabled(graphics2D);
                break;
            case 11:
                paintlistViewIconEnabled(graphics2D);
                break;
        }
    }

    @Override // javax.swing.plaf.nimbus.AbstractRegionPainter
    protected final AbstractRegionPainter.PaintContext getPaintContext() {
        return this.ctx;
    }

    private void paintBackgroundEnabled(Graphics2D graphics2D) {
        this.rect = decodeRect1();
        graphics2D.setPaint(this.color1);
        graphics2D.fill(this.rect);
    }

    private void paintfileIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath1();
        graphics2D.setPaint(this.color2);
        graphics2D.fill(this.path);
        this.rect = decodeRect2();
        graphics2D.setPaint(this.color3);
        graphics2D.fill(this.rect);
        this.path = decodePath2();
        graphics2D.setPaint(decodeGradient1(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath3();
        graphics2D.setPaint(decodeGradient2(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath4();
        graphics2D.setPaint(this.color8);
        graphics2D.fill(this.path);
        this.path = decodePath5();
        graphics2D.setPaint(this.color9);
        graphics2D.fill(this.path);
    }

    private void paintdirectoryIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath6();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.path);
        this.path = decodePath7();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath8();
        graphics2D.setPaint(decodeGradient4(this.path));
        graphics2D.fill(this.path);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect4();
        graphics2D.setPaint(this.color17);
        graphics2D.fill(this.rect);
        this.rect = decodeRect5();
        graphics2D.setPaint(this.color18);
        graphics2D.fill(this.rect);
        this.path = decodePath9();
        graphics2D.setPaint(decodeGradient5(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath10();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath11();
        graphics2D.setPaint(this.color24);
        graphics2D.fill(this.path);
    }

    private void paintupFolderIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath12();
        graphics2D.setPaint(decodeGradient7(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath13();
        graphics2D.setPaint(decodeGradient8(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath14();
        graphics2D.setPaint(decodeGradient9(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath15();
        graphics2D.setPaint(decodeGradient10(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath16();
        graphics2D.setPaint(this.color32);
        graphics2D.fill(this.path);
        this.path = decodePath17();
        graphics2D.setPaint(decodeGradient11(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath18();
        graphics2D.setPaint(this.color35);
        graphics2D.fill(this.path);
        this.path = decodePath19();
        graphics2D.setPaint(decodeGradient12(this.path));
        graphics2D.fill(this.path);
    }

    private void paintnewFolderIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath6();
        graphics2D.setPaint(this.color10);
        graphics2D.fill(this.path);
        this.path = decodePath7();
        graphics2D.setPaint(decodeGradient3(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath8();
        graphics2D.setPaint(decodeGradient4(this.path));
        graphics2D.fill(this.path);
        this.rect = decodeRect3();
        graphics2D.setPaint(this.color16);
        graphics2D.fill(this.rect);
        this.rect = decodeRect4();
        graphics2D.setPaint(this.color17);
        graphics2D.fill(this.rect);
        this.rect = decodeRect5();
        graphics2D.setPaint(this.color18);
        graphics2D.fill(this.rect);
        this.path = decodePath9();
        graphics2D.setPaint(decodeGradient5(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath10();
        graphics2D.setPaint(decodeGradient6(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath11();
        graphics2D.setPaint(this.color24);
        graphics2D.fill(this.path);
        this.path = decodePath20();
        graphics2D.setPaint(this.color38);
        graphics2D.fill(this.path);
        this.path = decodePath21();
        graphics2D.setPaint(this.color39);
        graphics2D.fill(this.path);
        this.path = decodePath22();
        graphics2D.setPaint(decodeRadial1(this.path));
        graphics2D.fill(this.path);
    }

    private void painthardDriveIconEnabled(Graphics2D graphics2D) {
        this.rect = decodeRect6();
        graphics2D.setPaint(this.color43);
        graphics2D.fill(this.rect);
        this.rect = decodeRect7();
        graphics2D.setPaint(this.color44);
        graphics2D.fill(this.rect);
        this.rect = decodeRect8();
        graphics2D.setPaint(decodeGradient13(this.rect));
        graphics2D.fill(this.rect);
        this.path = decodePath23();
        graphics2D.setPaint(decodeGradient14(this.path));
        graphics2D.fill(this.path);
        this.rect = decodeRect9();
        graphics2D.setPaint(this.color49);
        graphics2D.fill(this.rect);
        this.rect = decodeRect10();
        graphics2D.setPaint(this.color49);
        graphics2D.fill(this.rect);
        this.ellipse = decodeEllipse1();
        graphics2D.setPaint(this.color50);
        graphics2D.fill(this.ellipse);
        this.path = decodePath24();
        graphics2D.setPaint(decodeGradient15(this.path));
        graphics2D.fill(this.path);
        this.ellipse = decodeEllipse2();
        graphics2D.setPaint(this.color53);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse3();
        graphics2D.setPaint(this.color53);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse4();
        graphics2D.setPaint(this.color54);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse5();
        graphics2D.setPaint(this.color55);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse6();
        graphics2D.setPaint(this.color55);
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse7();
        graphics2D.setPaint(this.color55);
        graphics2D.fill(this.ellipse);
        this.rect = decodeRect11();
        graphics2D.setPaint(this.color56);
        graphics2D.fill(this.rect);
        this.rect = decodeRect12();
        graphics2D.setPaint(this.color56);
        graphics2D.fill(this.rect);
        this.rect = decodeRect13();
        graphics2D.setPaint(this.color56);
        graphics2D.fill(this.rect);
    }

    private void paintfloppyDriveIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath25();
        graphics2D.setPaint(decodeGradient16(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath26();
        graphics2D.setPaint(decodeGradient17(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath27();
        graphics2D.setPaint(decodeGradient18(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath28();
        graphics2D.setPaint(decodeGradient19(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath29();
        graphics2D.setPaint(this.color69);
        graphics2D.fill(this.path);
        this.rect = decodeRect14();
        graphics2D.setPaint(this.color70);
        graphics2D.fill(this.rect);
        this.rect = decodeRect15();
        graphics2D.setPaint(this.color40);
        graphics2D.fill(this.rect);
        this.rect = decodeRect16();
        graphics2D.setPaint(this.color67);
        graphics2D.fill(this.rect);
        this.rect = decodeRect17();
        graphics2D.setPaint(this.color71);
        graphics2D.fill(this.rect);
        this.rect = decodeRect18();
        graphics2D.setPaint(this.color44);
        graphics2D.fill(this.rect);
    }

    private void painthomeFolderIconEnabled(Graphics2D graphics2D) {
        this.path = decodePath30();
        graphics2D.setPaint(this.color72);
        graphics2D.fill(this.path);
        this.path = decodePath31();
        graphics2D.setPaint(this.color73);
        graphics2D.fill(this.path);
        this.rect = decodeRect19();
        graphics2D.setPaint(decodeGradient20(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect20();
        graphics2D.setPaint(this.color76);
        graphics2D.fill(this.rect);
        this.path = decodePath32();
        graphics2D.setPaint(decodeGradient21(this.path));
        graphics2D.fill(this.path);
        this.rect = decodeRect21();
        graphics2D.setPaint(decodeGradient22(this.rect));
        graphics2D.fill(this.rect);
        this.path = decodePath33();
        graphics2D.setPaint(decodeGradient23(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath34();
        graphics2D.setPaint(this.color83);
        graphics2D.fill(this.path);
        this.path = decodePath35();
        graphics2D.setPaint(decodeGradient24(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath36();
        graphics2D.setPaint(decodeGradient25(this.path));
        graphics2D.fill(this.path);
    }

    private void paintdetailsViewIconEnabled(Graphics2D graphics2D) {
        this.rect = decodeRect22();
        graphics2D.setPaint(decodeGradient26(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect23();
        graphics2D.setPaint(decodeGradient27(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect24();
        graphics2D.setPaint(this.color93);
        graphics2D.fill(this.rect);
        this.rect = decodeRect5();
        graphics2D.setPaint(this.color93);
        graphics2D.fill(this.rect);
        this.rect = decodeRect25();
        graphics2D.setPaint(this.color93);
        graphics2D.fill(this.rect);
        this.rect = decodeRect26();
        graphics2D.setPaint(this.color94);
        graphics2D.fill(this.rect);
        this.ellipse = decodeEllipse8();
        graphics2D.setPaint(decodeGradient28(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.ellipse = decodeEllipse9();
        graphics2D.setPaint(decodeRadial2(this.ellipse));
        graphics2D.fill(this.ellipse);
        this.path = decodePath37();
        graphics2D.setPaint(decodeGradient29(this.path));
        graphics2D.fill(this.path);
        this.path = decodePath38();
        graphics2D.setPaint(decodeGradient30(this.path));
        graphics2D.fill(this.path);
        this.rect = decodeRect27();
        graphics2D.setPaint(this.color104);
        graphics2D.fill(this.rect);
        this.rect = decodeRect28();
        graphics2D.setPaint(this.color105);
        graphics2D.fill(this.rect);
        this.rect = decodeRect29();
        graphics2D.setPaint(this.color106);
        graphics2D.fill(this.rect);
        this.rect = decodeRect30();
        graphics2D.setPaint(this.color107);
        graphics2D.fill(this.rect);
    }

    private void paintlistViewIconEnabled(Graphics2D graphics2D) {
        this.rect = decodeRect31();
        graphics2D.setPaint(decodeGradient26(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect32();
        graphics2D.setPaint(decodeGradient31(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect33();
        graphics2D.setPaint(this.color109);
        graphics2D.fill(this.rect);
        this.rect = decodeRect34();
        graphics2D.setPaint(decodeGradient32(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect35();
        graphics2D.setPaint(this.color111);
        graphics2D.fill(this.rect);
        this.rect = decodeRect36();
        graphics2D.setPaint(this.color112);
        graphics2D.fill(this.rect);
        this.rect = decodeRect37();
        graphics2D.setPaint(this.color113);
        graphics2D.fill(this.rect);
        this.rect = decodeRect38();
        graphics2D.setPaint(decodeGradient33(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect39();
        graphics2D.setPaint(this.color116);
        graphics2D.fill(this.rect);
        this.rect = decodeRect40();
        graphics2D.setPaint(decodeGradient34(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect41();
        graphics2D.setPaint(decodeGradient35(this.rect));
        graphics2D.fill(this.rect);
        this.rect = decodeRect42();
        graphics2D.setPaint(this.color119);
        graphics2D.fill(this.rect);
        this.rect = decodeRect43();
        graphics2D.setPaint(this.color121);
        graphics2D.fill(this.rect);
        this.rect = decodeRect44();
        graphics2D.setPaint(this.color121);
        graphics2D.fill(this.rect);
        this.rect = decodeRect45();
        graphics2D.setPaint(this.color121);
        graphics2D.fill(this.rect);
        this.rect = decodeRect46();
        graphics2D.setPaint(this.color122);
        graphics2D.fill(this.rect);
        this.rect = decodeRect47();
        graphics2D.setPaint(this.color121);
        graphics2D.fill(this.rect);
        this.rect = decodeRect48();
        graphics2D.setPaint(this.color122);
        graphics2D.fill(this.rect);
        this.rect = decodeRect49();
        graphics2D.setPaint(this.color122);
        graphics2D.fill(this.rect);
        this.rect = decodeRect50();
        graphics2D.setPaint(this.color121);
        graphics2D.fill(this.rect);
        this.rect = decodeRect51();
        graphics2D.setPaint(this.color122);
        graphics2D.fill(this.rect);
        this.rect = decodeRect52();
        graphics2D.setPaint(this.color122);
        graphics2D.fill(this.rect);
    }

    private Rectangle2D decodeRect1() {
        this.rect.setRect(decodeX(1.0f), decodeY(1.0f), decodeX(2.0f) - decodeX(1.0f), decodeY(2.0f) - decodeY(1.0f));
        return this.rect;
    }

    private Path2D decodePath1() {
        this.path.reset();
        this.path.moveTo(decodeX(0.2f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.2f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.4f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.9197531f), decodeY(0.2f));
        this.path.lineTo(decodeX(2.6f), decodeY(0.9f));
        this.path.lineTo(decodeX(2.6f), decodeY(3.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(3.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(0.88888896f));
        this.path.lineTo(decodeX(1.9537036f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.2f), decodeY(0.0f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect2() {
        this.rect.setRect(decodeX(0.4f), decodeY(2.8f), decodeX(2.6f) - decodeX(0.4f), decodeY(3.0f) - decodeY(2.8f));
        return this.rect;
    }

    private Path2D decodePath2() {
        this.path.reset();
        this.path.moveTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath3() {
        this.path.reset();
        this.path.moveTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath4() {
        this.path.reset();
        this.path.moveTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.6234567f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.6296296f), decodeY(1.2037038f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.2006173f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath5() {
        this.path.reset();
        this.path.moveTo(decodeX(1.8333333f), decodeY(0.4f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.4f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6f), decodeY(0.4f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(0.4f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath6() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(2.4f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(3.0f));
        this.path.lineTo(decodeX(2.6f), decodeY(3.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.8f), decodeY(2.4f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.4f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath7() {
        this.path.reset();
        this.path.moveTo(decodeX(0.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6037037f), decodeY(1.8425925f));
        this.path.lineTo(decodeX(0.8f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.6f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath8() {
        this.path.reset();
        this.path.moveTo(decodeX(0.2f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.40833336f), decodeY(1.8645833f));
        this.path.lineTo(decodeX(0.79583335f), decodeY(0.8f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.8f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.5f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.4f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.6f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.6f), decodeY(0.4f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(2.6f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect3() {
        this.rect.setRect(decodeX(0.2f), decodeY(0.6f), decodeX(0.4f) - decodeX(0.2f), decodeY(0.8f) - decodeY(0.6f));
        return this.rect;
    }

    private Rectangle2D decodeRect4() {
        this.rect.setRect(decodeX(0.6f), decodeY(0.2f), decodeX(1.3333334f) - decodeX(0.6f), decodeY(0.4f) - decodeY(0.2f));
        return this.rect;
    }

    private Rectangle2D decodeRect5() {
        this.rect.setRect(decodeX(1.5f), decodeY(0.6f), decodeX(2.4f) - decodeX(1.5f), decodeY(0.8f) - decodeY(0.6f));
        return this.rect;
    }

    private Path2D decodePath9() {
        this.path.reset();
        this.path.moveTo(decodeX(3.0f), decodeY(0.8f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.5f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.4f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.5888889f), decodeY(0.20370372f));
        this.path.lineTo(decodeX(0.5962963f), decodeY(0.34814817f));
        this.path.lineTo(decodeX(0.34814817f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(2.774074f), decodeY(1.1604939f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.0f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.8925927f), decodeY(1.1882716f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(2.8f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.2f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.65185183f));
        this.path.lineTo(decodeX(0.63703704f), decodeY(0.0f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.0f));
        this.path.lineTo(decodeX(1.5925925f), decodeY(0.4f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.4f));
        this.path.lineTo(decodeX(2.6f), decodeY(0.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(0.8f));
        this.path.lineTo(decodeX(3.0f), decodeY(0.8f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath10() {
        this.path.reset();
        this.path.moveTo(decodeX(2.4f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.8f));
        this.path.lineTo(decodeX(0.74814814f), decodeY(0.8f));
        this.path.lineTo(decodeX(0.4037037f), decodeY(1.8425925f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.5925926f), decodeY(2.225926f));
        this.path.lineTo(decodeX(0.916f), decodeY(0.996f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath11() {
        this.path.reset();
        this.path.moveTo(decodeX(2.2f), decodeY(2.2f));
        this.path.lineTo(decodeX(2.2f), decodeY(2.2f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath12() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.2f), decodeY(3.0f));
        this.path.lineTo(decodeX(2.6f), decodeY(3.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.8333333f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.5f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.5f), decodeY(0.4f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.6f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.4f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.2f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.8f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.8f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath13() {
        this.path.reset();
        this.path.moveTo(decodeX(0.2f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.2f), decodeY(0.8f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.8f));
        this.path.lineTo(decodeX(0.6f), decodeY(0.6f));
        this.path.lineTo(decodeX(0.6f), decodeY(0.4f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.4f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.5f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.5f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.2f), decodeY(2.8f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath14() {
        this.path.reset();
        this.path.moveTo(decodeX(0.4f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.6f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(0.8f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.8f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.4f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.4f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.0f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath15() {
        this.path.reset();
        this.path.moveTo(decodeX(0.6f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.0f));
        this.path.lineTo(decodeX(0.8f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.0f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.8f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath16() {
        this.path.reset();
        this.path.moveTo(decodeX(1.1702899f), decodeY(1.2536231f));
        this.path.lineTo(decodeX(1.1666666f), decodeY(1.0615941f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.0978261f));
        this.path.lineTo(decodeX(2.7782607f), decodeY(1.25f));
        this.path.lineTo(decodeX(2.3913045f), decodeY(1.3188406f));
        this.path.lineTo(decodeX(2.3826087f), decodeY(1.7246377f));
        this.path.lineTo(decodeX(2.173913f), decodeY(1.9347827f));
        this.path.lineTo(decodeX(1.8695652f), decodeY(1.923913f));
        this.path.lineTo(decodeX(1.710145f), decodeY(1.7246377f));
        this.path.lineTo(decodeX(1.710145f), decodeY(1.3115941f));
        this.path.lineTo(decodeX(1.1702899f), decodeY(1.2536231f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath17() {
        this.path.reset();
        this.path.moveTo(decodeX(1.1666666f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(1.1666666f), decodeY(0.9130435f));
        this.path.lineTo(decodeX(1.9456522f), decodeY(0.0f));
        this.path.lineTo(decodeX(2.0608697f), decodeY(0.0f));
        this.path.lineTo(decodeX(2.9956522f), decodeY(0.9130435f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(2.2f), decodeY(1.8333333f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(1.8333333f));
        this.path.lineTo(decodeX(1.6666667f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(1.6666667f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(1.1666666f), decodeY(1.1666666f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath18() {
        this.path.reset();
        this.path.moveTo(decodeX(1.2717391f), decodeY(0.9956522f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(2.2f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(2.2f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.8652174f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.0f), decodeY(0.13043478f));
        this.path.lineTo(decodeX(1.2717391f), decodeY(0.9956522f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath19() {
        this.path.reset();
        this.path.moveTo(decodeX(1.8333333f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.3913044f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.9963768f), decodeY(0.25652176f));
        this.path.lineTo(decodeX(2.6608696f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.2f), decodeY(1.0f));
        this.path.lineTo(decodeX(2.2f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(1.8333333f), decodeY(1.6666667f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath20() {
        this.path.reset();
        this.path.moveTo(decodeX(0.22692308f), decodeY(0.061538465f));
        this.path.lineTo(decodeX(0.75384617f), decodeY(0.37692308f));
        this.path.lineTo(decodeX(0.91923076f), decodeY(0.01923077f));
        this.path.lineTo(decodeX(1.2532052f), decodeY(0.40769228f));
        this.path.lineTo(decodeX(1.7115386f), decodeY(0.13846155f));
        this.path.lineTo(decodeX(1.6923077f), decodeY(0.85f));
        this.path.lineTo(decodeX(2.169231f), decodeY(0.9115385f));
        this.path.lineTo(decodeX(1.7852564f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(1.9166667f), decodeY(1.9679487f));
        this.path.lineTo(decodeX(1.3685898f), decodeY(1.8301282f));
        this.path.lineTo(decodeX(1.1314102f), decodeY(2.2115386f));
        this.path.lineTo(decodeX(0.63076925f), decodeY(1.8205128f));
        this.path.lineTo(decodeX(0.22692308f), decodeY(1.9262822f));
        this.path.lineTo(decodeX(0.31153846f), decodeY(1.4871795f));
        this.path.lineTo(decodeX(0.0f), decodeY(1.1538461f));
        this.path.lineTo(decodeX(0.38461536f), decodeY(0.68076926f));
        this.path.lineTo(decodeX(0.22692308f), decodeY(0.061538465f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath21() {
        this.path.reset();
        this.path.moveTo(decodeX(0.23461537f), decodeY(0.33076924f));
        this.path.lineTo(decodeX(0.32692307f), decodeY(0.21538463f));
        this.path.lineTo(decodeX(0.9653846f), decodeY(0.74615383f));
        this.path.lineTo(decodeX(1.0160257f), decodeY(0.01923077f));
        this.path.lineTo(decodeX(1.1506411f), decodeY(0.01923077f));
        this.path.lineTo(decodeX(1.2275641f), decodeY(0.72307694f));
        this.path.lineTo(decodeX(1.6987178f), decodeY(0.20769231f));
        this.path.lineTo(decodeX(1.8237178f), decodeY(0.37692308f));
        this.path.lineTo(decodeX(1.3878205f), decodeY(0.94230765f));
        this.path.lineTo(decodeX(1.9775641f), decodeY(1.0256411f));
        this.path.lineTo(decodeX(1.9839742f), decodeY(1.1474359f));
        this.path.lineTo(decodeX(1.4070512f), decodeY(1.2083334f));
        this.path.lineTo(decodeX(1.7980769f), decodeY(1.7307692f));
        this.path.lineTo(decodeX(1.7532051f), decodeY(1.8269231f));
        this.path.lineTo(decodeX(1.2211539f), decodeY(1.3365384f));
        this.path.lineTo(decodeX(1.1506411f), decodeY(1.9839742f));
        this.path.lineTo(decodeX(1.0288461f), decodeY(1.9775641f));
        this.path.lineTo(decodeX(0.95384616f), decodeY(1.3429488f));
        this.path.lineTo(decodeX(0.28846154f), decodeY(1.8012822f));
        this.path.lineTo(decodeX(0.20769231f), decodeY(1.7371795f));
        this.path.lineTo(decodeX(0.75f), decodeY(1.173077f));
        this.path.lineTo(decodeX(0.011538462f), decodeY(1.1634616f));
        this.path.lineTo(decodeX(0.015384616f), decodeY(1.0224359f));
        this.path.lineTo(decodeX(0.79615384f), decodeY(0.94230765f));
        this.path.lineTo(decodeX(0.23461537f), decodeY(0.33076924f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath22() {
        this.path.reset();
        this.path.moveTo(decodeX(0.58461535f), decodeY(0.6615385f));
        this.path.lineTo(decodeX(0.68846154f), decodeY(0.56923074f));
        this.path.lineTo(decodeX(0.9884615f), decodeY(0.80769235f));
        this.path.lineTo(decodeX(1.0352564f), decodeY(0.43076926f));
        this.path.lineTo(decodeX(1.1282052f), decodeY(0.43846154f));
        this.path.lineTo(decodeX(1.1891025f), decodeY(0.80769235f));
        this.path.lineTo(decodeX(1.4006411f), decodeY(0.59615386f));
        this.path.lineTo(decodeX(1.4967948f), decodeY(0.70384616f));
        this.path.lineTo(decodeX(1.3173077f), decodeY(0.9384615f));
        this.path.lineTo(decodeX(1.625f), decodeY(1.0256411f));
        this.path.lineTo(decodeX(1.6282051f), decodeY(1.1346154f));
        this.path.lineTo(decodeX(1.2564102f), decodeY(1.176282f));
        this.path.lineTo(decodeX(1.4711539f), decodeY(1.3910257f));
        this.path.lineTo(decodeX(1.4070512f), decodeY(1.4807693f));
        this.path.lineTo(decodeX(1.1858975f), decodeY(1.2724359f));
        this.path.lineTo(decodeX(1.1474359f), decodeY(1.6602564f));
        this.path.lineTo(decodeX(1.0416666f), decodeY(1.6602564f));
        this.path.lineTo(decodeX(0.9769231f), decodeY(1.2884616f));
        this.path.lineTo(decodeX(0.6923077f), decodeY(1.5f));
        this.path.lineTo(decodeX(0.6423077f), decodeY(1.3782052f));
        this.path.lineTo(decodeX(0.83076924f), decodeY(1.176282f));
        this.path.lineTo(decodeX(0.46923074f), decodeY(1.1474359f));
        this.path.lineTo(decodeX(0.48076925f), decodeY(1.0064102f));
        this.path.lineTo(decodeX(0.8230769f), decodeY(0.98461545f));
        this.path.lineTo(decodeX(0.58461535f), decodeY(0.6615385f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect6() {
        this.rect.setRect(decodeX(0.2f), decodeY(0.0f), decodeX(2.8f) - decodeX(0.2f), decodeY(2.2f) - decodeY(0.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect7() {
        this.rect.setRect(decodeX(0.2f), decodeY(2.2f), decodeX(2.8f) - decodeX(0.2f), decodeY(3.0f) - decodeY(2.2f));
        return this.rect;
    }

    private Rectangle2D decodeRect8() {
        this.rect.setRect(decodeX(0.4f), decodeY(0.2f), decodeX(2.6f) - decodeX(0.4f), decodeY(2.2f) - decodeY(0.2f));
        return this.rect;
    }

    private Path2D decodePath23() {
        this.path.reset();
        this.path.moveTo(decodeX(0.4f), decodeY(2.2f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.4f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.4f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.2f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.2f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect9() {
        this.rect.setRect(decodeX(0.6f), decodeY(2.8f), decodeX(1.6666667f) - decodeX(0.6f), decodeY(3.0f) - decodeY(2.8f));
        return this.rect;
    }

    private Rectangle2D decodeRect10() {
        this.rect.setRect(decodeX(1.8333333f), decodeY(2.8f), decodeX(2.4f) - decodeX(1.8333333f), decodeY(3.0f) - decodeY(2.8f));
        return this.rect;
    }

    private Ellipse2D decodeEllipse1() {
        this.ellipse.setFrame(decodeX(0.6f), decodeY(2.4f), decodeX(0.8f) - decodeX(0.6f), decodeY(2.6f) - decodeY(2.4f));
        return this.ellipse;
    }

    private Path2D decodePath24() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0f), decodeY(0.4f));
        this.path.curveTo(decodeAnchorX(1.0f, 1.0f), decodeAnchorY(0.4f, -1.0f), decodeAnchorX(2.0f, -1.0f), decodeAnchorY(0.4f, -1.0f), decodeX(2.0f), decodeY(0.4f));
        this.path.curveTo(decodeAnchorX(2.0f, 1.0f), decodeAnchorY(0.4f, 1.0f), decodeAnchorX(2.2f, 0.0f), decodeAnchorY(1.0f, -1.0f), decodeX(2.2f), decodeY(1.0f));
        this.path.curveTo(decodeAnchorX(2.2f, 0.0f), decodeAnchorY(1.0f, 1.0f), decodeAnchorX(2.2f, 0.0f), decodeAnchorY(1.5f, -2.0f), decodeX(2.2f), decodeY(1.5f));
        this.path.curveTo(decodeAnchorX(2.2f, 0.0f), decodeAnchorY(1.5f, 2.0f), decodeAnchorX(1.6666667f, 1.0f), decodeAnchorY(1.8333333f, 0.0f), decodeX(1.6666667f), decodeY(1.8333333f));
        this.path.curveTo(decodeAnchorX(1.6666667f, -1.0f), decodeAnchorY(1.8333333f, 0.0f), decodeAnchorX(1.3333334f, 1.0f), decodeAnchorY(1.8333333f, 0.0f), decodeX(1.3333334f), decodeY(1.8333333f));
        this.path.curveTo(decodeAnchorX(1.3333334f, -1.0f), decodeAnchorY(1.8333333f, 0.0f), decodeAnchorX(0.8f, 0.0f), decodeAnchorY(1.5f, 2.0f), decodeX(0.8f), decodeY(1.5f));
        this.path.curveTo(decodeAnchorX(0.8f, 0.0f), decodeAnchorY(1.5f, -2.0f), decodeAnchorX(0.8f, 0.0f), decodeAnchorY(1.0f, 1.0f), decodeX(0.8f), decodeY(1.0f));
        this.path.curveTo(decodeAnchorX(0.8f, 0.0f), decodeAnchorY(1.0f, -1.0f), decodeAnchorX(1.0f, -1.0f), decodeAnchorY(0.4f, 1.0f), decodeX(1.0f), decodeY(0.4f));
        this.path.closePath();
        return this.path;
    }

    private Ellipse2D decodeEllipse2() {
        this.ellipse.setFrame(decodeX(0.6f), decodeY(0.2f), decodeX(0.8f) - decodeX(0.6f), decodeY(0.4f) - decodeY(0.2f));
        return this.ellipse;
    }

    private Ellipse2D decodeEllipse3() {
        this.ellipse.setFrame(decodeX(2.2f), decodeY(0.2f), decodeX(2.4f) - decodeX(2.2f), decodeY(0.4f) - decodeY(0.2f));
        return this.ellipse;
    }

    private Ellipse2D decodeEllipse4() {
        this.ellipse.setFrame(decodeX(2.2f), decodeY(1.0f), decodeX(2.4f) - decodeX(2.2f), decodeY(1.1666666f) - decodeY(1.0f));
        return this.ellipse;
    }

    private Ellipse2D decodeEllipse5() {
        this.ellipse.setFrame(decodeX(2.2f), decodeY(1.6666667f), decodeX(2.4f) - decodeX(2.2f), decodeY(1.8333333f) - decodeY(1.6666667f));
        return this.ellipse;
    }

    private Ellipse2D decodeEllipse6() {
        this.ellipse.setFrame(decodeX(0.6f), decodeY(1.6666667f), decodeX(0.8f) - decodeX(0.6f), decodeY(1.8333333f) - decodeY(1.6666667f));
        return this.ellipse;
    }

    private Ellipse2D decodeEllipse7() {
        this.ellipse.setFrame(decodeX(0.6f), decodeY(1.0f), decodeX(0.8f) - decodeX(0.6f), decodeY(1.1666666f) - decodeY(1.0f));
        return this.ellipse;
    }

    private Rectangle2D decodeRect11() {
        this.rect.setRect(decodeX(0.8f), decodeY(2.2f), decodeX(1.0f) - decodeX(0.8f), decodeY(2.6f) - decodeY(2.2f));
        return this.rect;
    }

    private Rectangle2D decodeRect12() {
        this.rect.setRect(decodeX(1.1666666f), decodeY(2.2f), decodeX(1.3333334f) - decodeX(1.1666666f), decodeY(2.6f) - decodeY(2.2f));
        return this.rect;
    }

    private Rectangle2D decodeRect13() {
        this.rect.setRect(decodeX(1.5f), decodeY(2.2f), decodeX(1.6666667f) - decodeX(1.5f), decodeY(2.6f) - decodeY(2.2f));
        return this.rect;
    }

    private Path2D decodePath25() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.2f), decodeY(0.0f));
        this.path.lineTo(decodeX(2.6f), decodeY(0.0f));
        this.path.lineTo(decodeX(3.0f), decodeY(0.4f));
        this.path.lineTo(decodeX(3.0f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.8f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.2f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.0f), decodeY(0.2f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath26() {
        this.path.reset();
        this.path.moveTo(decodeX(0.2f), decodeY(0.4f));
        this.path.lineTo(decodeX(0.4f), decodeY(0.2f));
        this.path.lineTo(decodeX(2.4f), decodeY(0.2f));
        this.path.lineTo(decodeX(2.8f), decodeY(0.6f));
        this.path.lineTo(decodeX(2.8f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.2f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.2f), decodeY(0.4f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath27() {
        this.path.reset();
        this.path.moveTo(decodeX(0.8f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(1.0f), decodeY(1.5f));
        this.path.lineTo(decodeX(2.0f), decodeY(1.5f));
        this.path.lineTo(decodeX(2.2f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(2.2f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.8f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.8f), decodeY(1.6666667f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath28() {
        this.path.reset();
        this.path.moveTo(decodeX(1.1666666f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.1666666f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(2.2f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(2.2f), decodeY(0.4f));
        this.path.lineTo(decodeX(2.0f), decodeY(0.4f));
        this.path.lineTo(decodeX(2.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.6666667f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.6666667f), decodeY(0.4f));
        this.path.lineTo(decodeX(2.2f), decodeY(0.4f));
        this.path.lineTo(decodeX(2.2f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.1666666f), decodeY(0.2f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath29() {
        this.path.reset();
        this.path.moveTo(decodeX(0.8f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.0f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.0f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.5f), decodeY(0.2f));
        this.path.lineTo(decodeX(1.5f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.6666667f), decodeY(1.0f));
        this.path.lineTo(decodeX(1.6666667f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(0.8f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(0.8f), decodeY(0.2f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect14() {
        this.rect.setRect(decodeX(0.8f), decodeY(2.6f), decodeX(2.2f) - decodeX(0.8f), decodeY(2.8f) - decodeY(2.6f));
        return this.rect;
    }

    private Rectangle2D decodeRect15() {
        this.rect.setRect(decodeX(0.36153847f), decodeY(2.3576922f), decodeX(0.63461536f) - decodeX(0.36153847f), decodeY(2.6807692f) - decodeY(2.3576922f));
        return this.rect;
    }

    private Rectangle2D decodeRect16() {
        this.rect.setRect(decodeX(2.376923f), decodeY(2.3807693f), decodeX(2.6384616f) - decodeX(2.376923f), decodeY(2.6846154f) - decodeY(2.3807693f));
        return this.rect;
    }

    private Rectangle2D decodeRect17() {
        this.rect.setRect(decodeX(0.4f), decodeY(2.4f), decodeX(0.6f) - decodeX(0.4f), decodeY(2.6f) - decodeY(2.4f));
        return this.rect;
    }

    private Rectangle2D decodeRect18() {
        this.rect.setRect(decodeX(2.4f), decodeY(2.4f), decodeX(2.6f) - decodeX(2.4f), decodeY(2.6f) - decodeY(2.4f));
        return this.rect;
    }

    private Path2D decodePath30() {
        this.path.reset();
        this.path.moveTo(decodeX(0.4f), decodeY(1.5f));
        this.path.lineTo(decodeX(0.4f), decodeY(2.6f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.4f), decodeY(2.8f));
        this.path.lineTo(decodeX(2.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.5f));
        this.path.lineTo(decodeX(0.4f), decodeY(1.5f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath31() {
        this.path.reset();
        this.path.moveTo(decodeX(0.6f), decodeY(1.5f));
        this.path.lineTo(decodeX(0.6f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.4f), decodeY(2.6f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.5f));
        this.path.lineTo(decodeX(1.5f), decodeY(0.8f));
        this.path.lineTo(decodeX(0.6f), decodeY(1.5f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect19() {
        this.rect.setRect(decodeX(1.6666667f), decodeY(1.6666667f), decodeX(2.2f) - decodeX(1.6666667f), decodeY(2.2f) - decodeY(1.6666667f));
        return this.rect;
    }

    private Rectangle2D decodeRect20() {
        this.rect.setRect(decodeX(1.8333333f), decodeY(1.8333333f), decodeX(2.0f) - decodeX(1.8333333f), decodeY(2.0f) - decodeY(1.8333333f));
        return this.rect;
    }

    private Path2D decodePath32() {
        this.path.reset();
        this.path.moveTo(decodeX(1.0f), decodeY(2.8f));
        this.path.lineTo(decodeX(1.5f), decodeY(2.8f));
        this.path.lineTo(decodeX(1.5f), decodeY(1.8333333f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(1.1666666f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(1.0f), decodeY(1.8333333f));
        this.path.lineTo(decodeX(1.0f), decodeY(2.8f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect21() {
        this.rect.setRect(decodeX(1.1666666f), decodeY(1.8333333f), decodeX(1.3333334f) - decodeX(1.1666666f), decodeY(2.6f) - decodeY(1.8333333f));
        return this.rect;
    }

    private Path2D decodePath33() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(0.0f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(0.4f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(1.3974359f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.596154f), decodeY(0.6f));
        this.path.lineTo(decodeX(2.6f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(3.0f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(1.6666667f), decodeY(0.0f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.0f));
        this.path.lineTo(decodeX(0.0f), decodeY(1.3333334f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath34() {
        this.path.reset();
        this.path.moveTo(decodeX(0.2576923f), decodeY(1.3717948f));
        this.path.lineTo(decodeX(0.2f), decodeY(1.5f));
        this.path.lineTo(decodeX(0.3230769f), decodeY(1.4711539f));
        this.path.lineTo(decodeX(1.4006411f), decodeY(0.40384617f));
        this.path.lineTo(decodeX(1.5929487f), decodeY(0.4f));
        this.path.lineTo(decodeX(2.6615386f), decodeY(1.4615384f));
        this.path.lineTo(decodeX(2.8f), decodeY(1.5f));
        this.path.lineTo(decodeX(2.7461538f), decodeY(1.3653846f));
        this.path.lineTo(decodeX(1.6089742f), decodeY(0.19615385f));
        this.path.lineTo(decodeX(1.4070512f), decodeY(0.2f));
        this.path.lineTo(decodeX(0.2576923f), decodeY(1.3717948f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath35() {
        this.path.reset();
        this.path.moveTo(decodeX(0.6f), decodeY(1.5f));
        this.path.lineTo(decodeX(1.3333334f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.5f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.5f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(1.0f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(0.6f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(0.6f), decodeY(1.5f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath36() {
        this.path.reset();
        this.path.moveTo(decodeX(1.6666667f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.5f), decodeY(0.6f));
        this.path.lineTo(decodeX(1.5f), decodeY(1.1666666f));
        this.path.lineTo(decodeX(2.0f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.6666667f));
        this.path.lineTo(decodeX(2.4f), decodeY(1.3333334f));
        this.path.lineTo(decodeX(1.6666667f), decodeY(0.6f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect22() {
        this.rect.setRect(decodeX(0.2f), decodeY(0.0f), decodeX(3.0f) - decodeX(0.2f), decodeY(2.8f) - decodeY(0.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect23() {
        this.rect.setRect(decodeX(0.4f), decodeY(0.2f), decodeX(2.8f) - decodeX(0.4f), decodeY(2.6f) - decodeY(0.2f));
        return this.rect;
    }

    private Rectangle2D decodeRect24() {
        this.rect.setRect(decodeX(1.0f), decodeY(0.6f), decodeX(1.3333334f) - decodeX(1.0f), decodeY(0.8f) - decodeY(0.6f));
        return this.rect;
    }

    private Rectangle2D decodeRect25() {
        this.rect.setRect(decodeX(1.5f), decodeY(1.3333334f), decodeX(2.4f) - decodeX(1.5f), decodeY(1.5f) - decodeY(1.3333334f));
        return this.rect;
    }

    private Rectangle2D decodeRect26() {
        this.rect.setRect(decodeX(1.5f), decodeY(2.0f), decodeX(2.4f) - decodeX(1.5f), decodeY(2.2f) - decodeY(2.0f));
        return this.rect;
    }

    private Ellipse2D decodeEllipse8() {
        this.ellipse.setFrame(decodeX(0.6f), decodeY(0.8f), decodeX(2.2f) - decodeX(0.6f), decodeY(2.4f) - decodeY(0.8f));
        return this.ellipse;
    }

    private Ellipse2D decodeEllipse9() {
        this.ellipse.setFrame(decodeX(0.8f), decodeY(1.0f), decodeX(2.0f) - decodeX(0.8f), decodeY(2.2f) - decodeY(1.0f));
        return this.ellipse;
    }

    private Path2D decodePath37() {
        this.path.reset();
        this.path.moveTo(decodeX(0.0f), decodeY(2.8f));
        this.path.lineTo(decodeX(0.0f), decodeY(3.0f));
        this.path.lineTo(decodeX(0.4f), decodeY(3.0f));
        this.path.lineTo(decodeX(1.0f), decodeY(2.2f));
        this.path.lineTo(decodeX(0.8f), decodeY(1.8333333f));
        this.path.lineTo(decodeX(0.0f), decodeY(2.8f));
        this.path.closePath();
        return this.path;
    }

    private Path2D decodePath38() {
        this.path.reset();
        this.path.moveTo(decodeX(0.1826087f), decodeY(2.7217393f));
        this.path.lineTo(decodeX(0.2826087f), decodeY(2.8217392f));
        this.path.lineTo(decodeX(1.0181159f), decodeY(2.095652f));
        this.path.lineTo(decodeX(0.9130435f), decodeY(1.9891305f));
        this.path.lineTo(decodeX(0.1826087f), decodeY(2.7217393f));
        this.path.closePath();
        return this.path;
    }

    private Rectangle2D decodeRect27() {
        this.rect.setRect(decodeX(1.0f), decodeY(1.3333334f), decodeX(1.3333334f) - decodeX(1.0f), decodeY(1.5f) - decodeY(1.3333334f));
        return this.rect;
    }

    private Rectangle2D decodeRect28() {
        this.rect.setRect(decodeX(1.5f), decodeY(1.3333334f), decodeX(1.8333333f) - decodeX(1.5f), decodeY(1.5f) - decodeY(1.3333334f));
        return this.rect;
    }

    private Rectangle2D decodeRect29() {
        this.rect.setRect(decodeX(1.5f), decodeY(1.6666667f), decodeX(1.8333333f) - decodeX(1.5f), decodeY(1.8333333f) - decodeY(1.6666667f));
        return this.rect;
    }

    private Rectangle2D decodeRect30() {
        this.rect.setRect(decodeX(1.0f), decodeY(1.6666667f), decodeX(1.3333334f) - decodeX(1.0f), decodeY(1.8333333f) - decodeY(1.6666667f));
        return this.rect;
    }

    private Rectangle2D decodeRect31() {
        this.rect.setRect(decodeX(0.0f), decodeY(0.0f), decodeX(3.0f) - decodeX(0.0f), decodeY(2.8f) - decodeY(0.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect32() {
        this.rect.setRect(decodeX(0.2f), decodeY(0.2f), decodeX(2.8f) - decodeX(0.2f), decodeY(2.6f) - decodeY(0.2f));
        return this.rect;
    }

    private Rectangle2D decodeRect33() {
        this.rect.setRect(decodeX(0.8f), decodeY(0.6f), decodeX(1.1666666f) - decodeX(0.8f), decodeY(0.8f) - decodeY(0.6f));
        return this.rect;
    }

    private Rectangle2D decodeRect34() {
        this.rect.setRect(decodeX(1.3333334f), decodeY(0.6f), decodeX(2.2f) - decodeX(1.3333334f), decodeY(0.8f) - decodeY(0.6f));
        return this.rect;
    }

    private Rectangle2D decodeRect35() {
        this.rect.setRect(decodeX(1.3333334f), decodeY(1.0f), decodeX(2.0f) - decodeX(1.3333334f), decodeY(1.1666666f) - decodeY(1.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect36() {
        this.rect.setRect(decodeX(0.8f), decodeY(1.0f), decodeX(1.1666666f) - decodeX(0.8f), decodeY(1.1666666f) - decodeY(1.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect37() {
        this.rect.setRect(decodeX(0.8f), decodeY(1.3333334f), decodeX(1.1666666f) - decodeX(0.8f), decodeY(1.5f) - decodeY(1.3333334f));
        return this.rect;
    }

    private Rectangle2D decodeRect38() {
        this.rect.setRect(decodeX(1.3333334f), decodeY(1.3333334f), decodeX(2.2f) - decodeX(1.3333334f), decodeY(1.5f) - decodeY(1.3333334f));
        return this.rect;
    }

    private Rectangle2D decodeRect39() {
        this.rect.setRect(decodeX(0.8f), decodeY(1.6666667f), decodeX(1.1666666f) - decodeX(0.8f), decodeY(1.8333333f) - decodeY(1.6666667f));
        return this.rect;
    }

    private Rectangle2D decodeRect40() {
        this.rect.setRect(decodeX(1.3333334f), decodeY(1.6666667f), decodeX(2.0f) - decodeX(1.3333334f), decodeY(1.8333333f) - decodeY(1.6666667f));
        return this.rect;
    }

    private Rectangle2D decodeRect41() {
        this.rect.setRect(decodeX(1.3333334f), decodeY(2.0f), decodeX(2.2f) - decodeX(1.3333334f), decodeY(2.2f) - decodeY(2.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect42() {
        this.rect.setRect(decodeX(0.8f), decodeY(2.0f), decodeX(1.1666666f) - decodeX(0.8f), decodeY(2.2f) - decodeY(2.0f));
        return this.rect;
    }

    private Rectangle2D decodeRect43() {
        this.rect.setRect(decodeX(0.8f), decodeY(0.8f), decodeX(1.1666666f) - decodeX(0.8f), decodeY(1.0f) - decodeY(0.8f));
        return this.rect;
    }

    private Rectangle2D decodeRect44() {
        this.rect.setRect(decodeX(1.3333334f), decodeY(0.8f), decodeX(2.2f) - decodeX(1.3333334f), decodeY(1.0f) - decodeY(0.8f));
        return this.rect;
    }

    private Rectangle2D decodeRect45() {
        this.rect.setRect(decodeX(0.8f), decodeY(1.1666666f), decodeX(1.1666666f) - decodeX(0.8f), decodeY(1.3333334f) - decodeY(1.1666666f));
        return this.rect;
    }

    private Rectangle2D decodeRect46() {
        this.rect.setRect(decodeX(1.3333334f), decodeY(1.1666666f), decodeX(2.0f) - decodeX(1.3333334f), decodeY(1.3333334f) - decodeY(1.1666666f));
        return this.rect;
    }

    private Rectangle2D decodeRect47() {
        this.rect.setRect(decodeX(0.8f), decodeY(1.5f), decodeX(1.1666666f) - decodeX(0.8f), decodeY(1.6666667f) - decodeY(1.5f));
        return this.rect;
    }

    private Rectangle2D decodeRect48() {
        this.rect.setRect(decodeX(1.3333334f), decodeY(1.5f), decodeX(2.2f) - decodeX(1.3333334f), decodeY(1.6666667f) - decodeY(1.5f));
        return this.rect;
    }

    private Rectangle2D decodeRect49() {
        this.rect.setRect(decodeX(1.3333334f), decodeY(1.8333333f), decodeX(2.0f) - decodeX(1.3333334f), decodeY(2.0f) - decodeY(1.8333333f));
        return this.rect;
    }

    private Rectangle2D decodeRect50() {
        this.rect.setRect(decodeX(0.8f), decodeY(1.8333333f), decodeX(1.1666666f) - decodeX(0.8f), decodeY(2.0f) - decodeY(1.8333333f));
        return this.rect;
    }

    private Rectangle2D decodeRect51() {
        this.rect.setRect(decodeX(0.8f), decodeY(2.2f), decodeX(1.1666666f) - decodeX(0.8f), decodeY(2.4f) - decodeY(2.2f));
        return this.rect;
    }

    private Rectangle2D decodeRect52() {
        this.rect.setRect(decodeX(1.3333334f), decodeY(2.2f), decodeX(2.2f) - decodeX(1.3333334f), decodeY(2.4f) - decodeY(2.2f));
        return this.rect;
    }

    private Paint decodeGradient1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.046296295f * width) + x2, (0.9675926f * height) + y2, (0.4861111f * width) + x2, (0.5324074f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color4, decodeColor(this.color4, this.color5, 0.5f), this.color5});
    }

    private Paint decodeGradient2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color6, decodeColor(this.color6, this.color7, 0.5f), this.color7});
    }

    private Paint decodeGradient3(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.04191617f, 0.10329342f, 0.16467066f, 0.24550897f, 0.3263473f, 0.6631737f, 1.0f}, new Color[]{this.color11, decodeColor(this.color11, this.color12, 0.5f), this.color12, decodeColor(this.color12, this.color13, 0.5f), this.color13, decodeColor(this.color13, this.color14, 0.5f), this.color14});
    }

    private Paint decodeGradient4(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color6, decodeColor(this.color6, this.color15, 0.5f), this.color15});
    }

    private Paint decodeGradient5(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color19, decodeColor(this.color19, this.color20, 0.5f), this.color20});
    }

    private Paint decodeGradient6(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.12724552f, 0.25449103f, 0.62724555f, 1.0f}, new Color[]{this.color21, decodeColor(this.color21, this.color22, 0.5f), this.color22, decodeColor(this.color22, this.color23, 0.5f), this.color23});
    }

    private Paint decodeGradient7(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.06392045f, 0.1278409f, 0.5213069f, 0.91477275f}, new Color[]{this.color25, decodeColor(this.color25, this.color26, 0.5f), this.color26, decodeColor(this.color26, this.color27, 0.5f), this.color27});
    }

    private Paint decodeGradient8(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.048295453f, 0.09659091f, 0.5482955f, 1.0f}, new Color[]{this.color28, decodeColor(this.color28, this.color6, 0.5f), this.color6, decodeColor(this.color6, this.color15, 0.5f), this.color15});
    }

    private Paint decodeGradient9(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color29, decodeColor(this.color29, this.color30, 0.5f), this.color30});
    }

    private Paint decodeGradient10(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.06534091f, 0.13068181f, 0.3096591f, 0.48863637f, 0.7443182f, 1.0f}, new Color[]{this.color11, decodeColor(this.color11, this.color12, 0.5f), this.color12, decodeColor(this.color12, this.color31, 0.5f), this.color31, decodeColor(this.color31, this.color14, 0.5f), this.color14});
    }

    private Paint decodeGradient11(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color33, decodeColor(this.color33, this.color34, 0.5f), this.color34});
    }

    private Paint decodeGradient12(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color36, decodeColor(this.color36, this.color37, 0.5f), this.color37});
    }

    private Paint decodeRadial1(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        return decodeRadialGradient((0.5f * ((float) bounds2D.getWidth())) + x2, (1.0f * ((float) bounds2D.getHeight())) + y2, 0.53913116f, new float[]{0.11290322f, 0.17419355f, 0.23548387f, 0.31129032f, 0.38709676f, 0.47903225f, 0.57096773f}, new Color[]{this.color40, decodeColor(this.color40, this.color41, 0.5f), this.color41, decodeColor(this.color41, this.color41, 0.5f), this.color41, decodeColor(this.color41, this.color42, 0.5f), this.color42});
    }

    private Paint decodeGradient13(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color45, decodeColor(this.color45, this.color46, 0.5f), this.color46});
    }

    private Paint decodeGradient14(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color47, decodeColor(this.color47, this.color48, 0.5f), this.color48});
    }

    private Paint decodeGradient15(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.3983871f, 0.7967742f, 0.8983871f, 1.0f}, new Color[]{this.color51, decodeColor(this.color51, this.color52, 0.5f), this.color52, decodeColor(this.color52, this.color51, 0.5f), this.color51});
    }

    private Paint decodeGradient16(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.061290324f, 0.12258065f, 0.5016129f, 0.88064516f, 0.9403226f, 1.0f}, new Color[]{this.color57, decodeColor(this.color57, this.color58, 0.5f), this.color58, decodeColor(this.color58, this.color59, 0.5f), this.color59, decodeColor(this.color59, this.color44, 0.5f), this.color44});
    }

    private Paint decodeGradient17(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.05f, 0.1f, 0.19193548f, 0.28387097f, 0.5209677f, 0.7580645f, 0.87903225f, 1.0f}, new Color[]{this.color60, decodeColor(this.color60, this.color61, 0.5f), this.color61, decodeColor(this.color61, this.color62, 0.5f), this.color62, decodeColor(this.color62, this.color63, 0.5f), this.color63, decodeColor(this.color63, this.color64, 0.5f), this.color64});
    }

    private Paint decodeGradient18(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.058064517f, 0.090322584f, 0.12258065f, 0.15645161f, 0.19032258f, 0.22741935f, 0.26451612f, 0.31290323f, 0.36129034f, 0.38225806f, 0.4032258f, 0.4596774f, 0.516129f, 0.54193544f, 0.56774193f, 0.61451614f, 0.66129035f, 0.70645165f, 0.7516129f}, new Color[]{this.color65, decodeColor(this.color65, this.color40, 0.5f), this.color40, decodeColor(this.color40, this.color40, 0.5f), this.color40, decodeColor(this.color40, this.color65, 0.5f), this.color65, decodeColor(this.color65, this.color65, 0.5f), this.color65, decodeColor(this.color65, this.color40, 0.5f), this.color40, decodeColor(this.color40, this.color40, 0.5f), this.color40, decodeColor(this.color40, this.color66, 0.5f), this.color66, decodeColor(this.color66, this.color66, 0.5f), this.color66, decodeColor(this.color66, this.color40, 0.5f), this.color40});
    }

    private Paint decodeGradient19(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color67, decodeColor(this.color67, this.color67, 0.5f), this.color67});
    }

    private Paint decodeGradient20(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color74, decodeColor(this.color74, this.color75, 0.5f), this.color75});
    }

    private Paint decodeGradient21(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color77, decodeColor(this.color77, this.color78, 0.5f), this.color78});
    }

    private Paint decodeGradient22(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color79, decodeColor(this.color79, this.color80, 0.5f), this.color80});
    }

    private Paint decodeGradient23(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color81, decodeColor(this.color81, this.color82, 0.5f), this.color82});
    }

    private Paint decodeGradient24(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.43076923f * width) + x2, (0.37820512f * height) + y2, (0.7076923f * width) + x2, (0.6730769f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color84, decodeColor(this.color84, this.color85, 0.5f), this.color85});
    }

    private Paint decodeGradient25(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.63076925f * width) + x2, (0.3621795f * height) + y2, (0.28846154f * width) + x2, (0.73397434f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color84, decodeColor(this.color84, this.color86, 0.5f), this.color86});
    }

    private Paint decodeGradient26(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color87, decodeColor(this.color87, this.color88, 0.5f), this.color88});
    }

    private Paint decodeGradient27(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.056818184f, 0.11363637f, 0.34232956f, 0.57102275f, 0.7855114f, 1.0f}, new Color[]{this.color89, decodeColor(this.color89, this.color90, 0.5f), this.color90, decodeColor(this.color90, this.color91, 0.5f), this.color91, decodeColor(this.color91, this.color92, 0.5f), this.color92});
    }

    private Paint decodeGradient28(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.75f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.5f, 1.0f}, new Color[]{this.color95, decodeColor(this.color95, this.color96, 0.5f), this.color96});
    }

    private Paint decodeRadial2(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        return decodeRadialGradient((0.49223602f * ((float) bounds2D.getWidth())) + x2, (0.9751553f * ((float) bounds2D.getHeight())) + y2, 0.73615754f, new float[]{0.0f, 0.40625f, 1.0f}, new Color[]{this.color97, decodeColor(this.color97, this.color98, 0.5f), this.color98});
    }

    private Paint decodeGradient29(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.0f * width) + x2, (0.0f * height) + y2, (1.0f * width) + x2, (1.0f * height) + y2, new float[]{0.38352272f, 0.4190341f, 0.45454547f, 0.484375f, 0.51420456f}, new Color[]{this.color99, decodeColor(this.color99, this.color100, 0.5f), this.color100, decodeColor(this.color100, this.color101, 0.5f), this.color101});
    }

    private Paint decodeGradient30(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((1.0f * width) + x2, (0.0f * height) + y2, (0.0f * width) + x2, (1.0f * height) + y2, new float[]{0.12215909f, 0.16051137f, 0.19886364f, 0.2627841f, 0.32670453f, 0.43039775f, 0.53409094f}, new Color[]{this.color102, decodeColor(this.color102, this.color35, 0.5f), this.color35, decodeColor(this.color35, this.color35, 0.5f), this.color35, decodeColor(this.color35, this.color103, 0.5f), this.color103});
    }

    private Paint decodeGradient31(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.5f * width) + x2, (0.0f * height) + y2, (0.5f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.038352273f, 0.07670455f, 0.24289773f, 0.4090909f, 0.7045455f, 1.0f}, new Color[]{this.color89, decodeColor(this.color89, this.color90, 0.5f), this.color90, decodeColor(this.color90, this.color108, 0.5f), this.color108, decodeColor(this.color108, this.color92, 0.5f), this.color92});
    }

    private Paint decodeGradient32(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.0f * width) + x2, (0.0f * height) + y2, (1.0f * width) + x2, (1.0f * height) + y2, new float[]{0.25f, 0.33522725f, 0.42045453f, 0.50142044f, 0.5823864f}, new Color[]{this.color109, decodeColor(this.color109, this.color110, 0.5f), this.color110, decodeColor(this.color110, this.color109, 0.5f), this.color109});
    }

    private Paint decodeGradient33(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.25f * width) + x2, (0.0f * height) + y2, (0.75f * width) + x2, (1.0f * height) + y2, new float[]{0.0f, 0.24147727f, 0.48295453f, 0.74147725f, 1.0f}, new Color[]{this.color114, decodeColor(this.color114, this.color115, 0.5f), this.color115, decodeColor(this.color115, this.color114, 0.5f), this.color114});
    }

    private Paint decodeGradient34(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.0f * width) + x2, (0.0f * height) + y2, (1.0f * width) + x2, (0.0f * height) + y2, new float[]{0.0f, 0.21732955f, 0.4346591f}, new Color[]{this.color117, decodeColor(this.color117, this.color118, 0.5f), this.color118});
    }

    private Paint decodeGradient35(Shape shape) {
        Rectangle2D bounds2D = shape.getBounds2D();
        float x2 = (float) bounds2D.getX();
        float y2 = (float) bounds2D.getY();
        float width = (float) bounds2D.getWidth();
        float height = (float) bounds2D.getHeight();
        return decodeGradient((0.0f * width) + x2, (0.0f * height) + y2, (1.0f * width) + x2, (0.0f * height) + y2, new float[]{0.0f, 0.21448864f, 0.42897728f, 0.7144886f, 1.0f}, new Color[]{this.color119, decodeColor(this.color119, this.color120, 0.5f), this.color120, decodeColor(this.color120, this.color119, 0.5f), this.color119});
    }
}
