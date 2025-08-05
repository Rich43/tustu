package org.apache.commons.net.telnet;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/telnet/WindowSizeOptionHandler.class */
public class WindowSizeOptionHandler extends TelnetOptionHandler {
    private int m_nWidth;
    private int m_nHeight;
    protected static final int WINDOW_SIZE = 31;

    public WindowSizeOptionHandler(int nWidth, int nHeight, boolean initlocal, boolean initremote, boolean acceptlocal, boolean acceptremote) {
        super(31, initlocal, initremote, acceptlocal, acceptremote);
        this.m_nWidth = 80;
        this.m_nHeight = 24;
        this.m_nWidth = nWidth;
        this.m_nHeight = nHeight;
    }

    public WindowSizeOptionHandler(int nWidth, int nHeight) {
        super(31, false, false, false, false);
        this.m_nWidth = 80;
        this.m_nHeight = 24;
        this.m_nWidth = nWidth;
        this.m_nHeight = nHeight;
    }

    @Override // org.apache.commons.net.telnet.TelnetOptionHandler
    public int[] startSubnegotiationLocal() {
        int nCompoundWindowSize = (this.m_nWidth * 65536) + this.m_nHeight;
        int nResponseSize = 5;
        if (this.m_nWidth % 256 == 255) {
            nResponseSize = 5 + 1;
        }
        if (this.m_nWidth / 256 == 255) {
            nResponseSize++;
        }
        if (this.m_nHeight % 256 == 255) {
            nResponseSize++;
        }
        if (this.m_nHeight / 256 == 255) {
            nResponseSize++;
        }
        int[] response = new int[nResponseSize];
        response[0] = 31;
        int nIndex = 1;
        int nShift = 24;
        while (nIndex < nResponseSize) {
            int nTurnedOnBits = 255 << nShift;
            response[nIndex] = (nCompoundWindowSize & nTurnedOnBits) >>> nShift;
            if (response[nIndex] == 255) {
                nIndex++;
                response[nIndex] = 255;
            }
            nIndex++;
            nShift -= 8;
        }
        return response;
    }
}
