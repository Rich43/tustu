package sun.nio.cs.ext;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import sun.nio.cs.HistoricallyNamedCharset;
import sun.nio.cs.ext.DoubleByte;

/* loaded from: charsets.jar:sun/nio/cs/ext/JIS_X_0208_Solaris.class */
public class JIS_X_0208_Solaris extends Charset implements HistoricallyNamedCharset {
    static final String b2cSBStr = "����������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������������";
    static char[] b2cSB;
    static final String[] b2cStr = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "��������？�������������������������������������������������������������������������������������", null, null, null, null, null, null, null, null, null, null, null, "①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑯⑰⑱⑲⑳ⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩ�㍉㌔㌢㍍㌘㌧㌃㌶㍑㍗㌍㌦㌣㌫㍊㌻㎜㎝㎞㎎㎏㏄㎡��������㍻〝〟№㏍℡㊤㊥㊦㊧㊨㈱㈲㈹㍾㍽㍼≒≡∫∮∑√⊥∠∟⊿∵∩∪��", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "纊褜鍈銈蓜俉炻昱棈鋹曻彅丨仡仼伀伃伹佖侒侊侚侔俍偀倢俿倞偆偰偂傔僴僘兊兤冝冾凬刕劜劦勀勛匀匇匤卲厓厲叝﨎咜咊咩哿喆坙坥垬埈埇﨏塚增墲夋奓奛奝奣妤妺孖寀甯寘寬尞岦岺峵崧嵓﨑嵂嵭嶸嶹巐弡弴彧德", "忞恝悅悊惞惕愠惲愑愷愰憘戓抦揵摠撝擎敎昀昕昻昉昮昞昤晥晗晙晴晳暙暠暲暿曺朎朗杦枻桒柀栁桄棏﨓楨﨔榘槢樰橫橆橳橾櫢櫤毖氿汜沆汯泚洄涇浯涖涬淏淸淲淼渹湜渧渼溿澈澵濵瀅瀇瀨炅炫焏焄煜煆煇凞燁燾犱", "犾猤猪獷玽珉珖珣珒琇珵琦琪琩琮瑢璉璟甁畯皂皜皞皛皦益睆劯砡硎硤硺礰礼神祥禔福禛竑竧靖竫箞精絈絜綷綠緖繒罇羡羽茁荢荿菇菶葈蒴蕓蕙蕫﨟薰蘒﨡蠇裵訒訷詹誧誾諟諸諶譓譿賰賴贒赶﨣軏﨤逸遧郞都鄕鄧釚", "釗釞釭釮釤釥鈆鈐鈊鈺鉀鈼鉎鉙鉑鈹鉧銧鉷鉸鋧鋗鋙鋐﨧鋕鋠鋓錥錡鋻﨨錞鋿錝錂鍰鍗鎤鏆鏞鏸鐱鑅鑈閒隆﨩隝隯霳霻靃靍靏靑靕顗顥飯飼餧館馞驎髙髜魵魲鮏鮱鮻鰀鵰鵫鶴鸙黑��ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹげ¦＇＂", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "ⅰⅱⅲⅳⅴⅵⅶⅷⅸⅹⅠⅡⅢⅣⅤⅥⅦⅧⅨⅩげ¦＇＂㈱№℡の纊褜鍈銈蓜俉炻昱棈鋹曻彅丨仡仼伀伃伹佖侒侊侚侔俍偀倢俿倞偆偰偂傔僴僘兊兤冝冾凬刕劜劦勀勛匀匇匤卲厓厲叝﨎咜咊咩哿喆坙坥垬埈埇﨏塚增墲", "夋奓奛奝奣妤妺孖寀甯寘寬尞岦岺峵崧嵓﨑嵂嵭嶸嶹巐弡弴彧德忞恝悅悊惞惕愠惲愑愷愰憘戓抦揵摠撝擎敎昀昕昻昉昮昞昤晥晗晙晴晳暙暠暲暿曺朎朗杦枻桒柀栁桄棏﨓楨﨔榘槢樰橫橆橳橾櫢櫤毖氿汜沆汯泚洄涇浯", "涖涬淏淸淲淼渹湜渧渼溿澈澵濵瀅瀇瀨炅炫焏焄煜煆煇凞燁燾犱犾猤猪獷玽珉珖珣珒琇珵琦琪琩琮瑢璉璟甁畯皂皜皞皛皦益睆劯砡硎硤硺礰礼神祥禔福禛竑竧靖竫箞精絈絜綷綠緖繒罇羡羽茁荢荿菇菶葈蒴蕓蕙蕫﨟薰", "蘒﨡蠇裵訒訷詹誧誾諟諸諶譓譿賰賴贒赶﨣軏﨤逸遧郞都鄕鄧釚釗釞釭釮釤釥鈆鈐鈊鈺鉀鈼鉎鉙鉑鈹鉧銧鉷鉸鋧鋗鋙鋐﨧鋕鋠鋓錥錡鋻﨨錞鋿錝錂鍰鍗鎤鏆鏞鏸鐱鑅鑈閒隆﨩隝隯霳霻靃靍靏靑靕顗顥飯飼餧館馞驎髙", "髜魵魲鮏鮱鮻鰀鵰鵫鶴鸙黑����������������������������������������������������������������������������������", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};
    static char[][] b2c = new char[b2cStr.length];
    private static volatile boolean b2cInitialized = false;
    static char[] c2b = new char[20992];
    static char[] c2bIndex = new char[256];
    private static volatile boolean c2bInitialized = false;

    public JIS_X_0208_Solaris() {
        super("x-JIS0208_Solaris", ExtendedCharsets.aliasesFor("x-JIS0208_Solaris"));
    }

    @Override // sun.nio.cs.HistoricallyNamedCharset
    public String historicalName() {
        return "JIS0208_Solaris";
    }

    @Override // java.nio.charset.Charset
    public boolean contains(Charset charset) {
        return charset instanceof JIS_X_0208_Solaris;
    }

    @Override // java.nio.charset.Charset
    public CharsetDecoder newDecoder() {
        initb2c();
        return new DoubleByte.Decoder_DBCSONLY(this, b2c, b2cSB, 33, 126);
    }

    @Override // java.nio.charset.Charset
    public CharsetEncoder newEncoder() {
        initc2b();
        return new DoubleByte.Encoder_DBCSONLY(this, new byte[]{33, 41}, c2b, c2bIndex);
    }

    static void initb2c() {
        if (b2cInitialized) {
            return;
        }
        synchronized (b2c) {
            if (b2cInitialized) {
                return;
            }
            for (int i2 = 0; i2 < b2cStr.length; i2++) {
                if (b2cStr[i2] == null) {
                    b2c[i2] = DoubleByte.B2C_UNMAPPABLE;
                } else {
                    b2c[i2] = b2cStr[i2].toCharArray();
                }
            }
            b2cSB = b2cSBStr.toCharArray();
            b2cInitialized = true;
        }
    }

    static void initc2b() {
        if (c2bInitialized) {
            return;
        }
        synchronized (c2b) {
            if (c2bInitialized) {
                return;
            }
            DoubleByte.Encoder.initC2B(b2cStr, b2cSBStr, "礡纊礢褜礣鍈礤銈礥蓜礦俉礧炻礨昱礩棈礪鋹礫曻礬彅礭丨礮仡礯仼礰伀礱伃礲伹礳佖礴侒礵侊礶侚礷侔礸俍礹偀示倢礻俿礼倞礽偆社偰礿偂祀傔祁僴祂僘祃兊祄兤祅冝祆冾祇凬祈刕祉劜祊劦祋勀祌勛祍匀祎匇祏匤祐卲祑厓祒厲祓叝祔﨎祕咜祖咊祗咩祘哿祙喆祚坙祛坥祜垬祝埈神埇祟﨏祠塚祡增祢墲祣夋祤奓祥奛祦奝祧奣票妤祩妺祪孖祫寀祬甯祭寘祮寬祯尞祰岦祱岺祲峵祳崧祴嵓祵﨑祶嵂祷嵭祸嶸祹嶹祺巐祻弡祼弴祽彧祾德稡忞稢恝稣悅稤悊稥惞稦惕稧愠稨惲稩愑稪愷稫愰稬憘稭戓種抦稯揵稰摠稱撝稲擎稳敎稴昀稵昕稶昻稷昉稸昮稹昞稺昤稻晥稼晗稽晙稾晴稿晳穀暙穁暠穂暲穃暿穄曺穅朎穆朗穇杦穈枻穉桒穊柀穋栁穌桄積棏穎﨓穏楨穐﨔穑榘穒槢穓樰穔橫穕橆穖橳穗橾穘櫢穙櫤穚毖穛氿穜汜穝沆穞汯穟泚穠洄穡涇穢浯穣涖穤涬穥淏穦淸穧淲穨淼穩渹穪湜穫渧穬渼穭溿穮澈穯澵穰濵穱瀅穲瀇穳瀨穴炅穵炫究焏穷焄穸煜穹煆空煇穻凞穼燁穽燾穾犱笡犾笢猤笣猪笤獷笥玽符珉笧珖笨珣笩珒笪琇笫珵第琦笭琪笮琩笯琮笰瑢笱璉笲璟笳甁笴畯笵皂笶皜笷皞笸皛笹皦笺益笻睆笼劯笽砡笾硎笿硤筀硺筁礰筂礼筃神筄祥筅禔筆福筇禛筈竑等竧筊靖筋竫筌箞筍精筎絈筏絜筐綷筑綠筒緖筓繒答罇筕羡策羽筗茁筘荢筙荿筚菇筛菶筜葈筝蒴筞蕓筟蕙筠蕫筡﨟筢薰筣蘒筤﨡筥蠇筦裵筧訒筨訷筩詹筪誧筫誾筬諟筭諸筮諶筯譓筰譿筱賰筲賴筳贒筴赶筵﨣筶軏筷﨤筸逸筹遧筺郞筻都筼鄕筽鄧签釚簡釗簢釞簣釭簤釮簥釤簦釥簧鈆簨鈐簩鈊簪鈺簫鉀簬鈼簭鉎簮鉙簯鉑簰鈹簱鉧簲銧簳鉷簴鉸簵鋧簶鋗簷鋙簸鋐簹﨧簺鋕簻鋠簼鋓簽錥簾錡簿鋻籀﨨籁錞籂鋿籃錝籄錂籅鍰籆鍗籇鎤籈鏆籉鏞籊鏸籋鐱籌鑅籍鑈籎閒籏隆籐﨩籑隝籒隯籓霳籔霻籕靃籖靍籗靏籘靑籙靕籚顗籛顥籜飯籝飼籞餧籟館籠馞籡驎籢髙籣髜籤魵籥魲籦鮏籧鮱籨鮻籩鰀籪鵰籫鵫籬鶴籭鸙籮黑籱ⅰ籲ⅱ米ⅲ籴ⅳ籵ⅴ籶ⅵ籷ⅶ籸ⅷ籹ⅸ籺ⅹ类げ籼¦籽＇籾＂錫Ⅰ錬Ⅱ錭Ⅲ錮Ⅳ錯Ⅴ錰Ⅵ錱Ⅶ録Ⅷ錳Ⅸ錴Ⅹ錹㈱錺№錻℡", null, 33, 126, c2b, c2bIndex);
            c2bInitialized = true;
        }
    }
}
