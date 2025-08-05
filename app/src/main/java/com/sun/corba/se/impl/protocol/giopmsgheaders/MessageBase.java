package com.sun.corba.se.impl.protocol.giopmsgheaders;

import com.sun.corba.se.impl.encoding.ByteBufferWithInfo;
import com.sun.corba.se.impl.encoding.CDRInputStream_1_0;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.protocol.AddressingDispositionException;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.ObjectKey;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.ior.iiop.RequestPartitioningComponent;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import com.sun.corba.se.spi.transport.CorbaConnection;
import com.sun.corba.se.spi.transport.ReadTimeouts;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Principal;
import org.omg.CORBA.SystemException;
import org.omg.IOP.TaggedProfile;
import sun.corba.SharedSecrets;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:com/sun/corba/se/impl/protocol/giopmsgheaders/MessageBase.class */
public abstract class MessageBase implements Message {
    public byte[] giopHeader;
    private ByteBuffer byteBuffer;
    private int threadPoolToUse;
    byte encodingVersion = 0;
    private static ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_PROTOCOL);

    public static String typeToString(int i2) {
        return typeToString((byte) i2);
    }

    public static String typeToString(byte b2) {
        String str;
        String str2 = ((int) b2) + "/";
        switch (b2) {
            case 0:
                str = str2 + "GIOPRequest";
                break;
            case 1:
                str = str2 + "GIOPReply";
                break;
            case 2:
                str = str2 + "GIOPCancelRequest";
                break;
            case 3:
                str = str2 + "GIOPLocateRequest";
                break;
            case 4:
                str = str2 + "GIOPLocateReply";
                break;
            case 5:
                str = str2 + "GIOPCloseConnection";
                break;
            case 6:
                str = str2 + "GIOPMessageError";
                break;
            case 7:
                str = str2 + "GIOPFragment";
                break;
            default:
                str = str2 + "Unknown";
                break;
        }
        return str;
    }

    public static MessageBase readGIOPMessage(ORB orb, CorbaConnection corbaConnection) {
        return (MessageBase) readGIOPBody(orb, corbaConnection, readGIOPHeader(orb, corbaConnection));
    }

    public static MessageBase readGIOPHeader(ORB orb, CorbaConnection corbaConnection) {
        MessageBase fragmentMessage_1_2 = null;
        try {
            ByteBuffer byteBuffer = corbaConnection.read(12, 0, 12, orb.getORBData().getTransportTCPReadTimeouts().get_max_giop_header_time_to_wait());
            if (orb.giopDebugFlag) {
                dprint(".readGIOPHeader: " + typeToString(byteBuffer.get(7)));
                dprint(".readGIOPHeader: GIOP header is: ");
                ByteBuffer byteBufferAsReadOnlyBuffer = byteBuffer.asReadOnlyBuffer();
                byteBufferAsReadOnlyBuffer.position(0).limit(12);
                ByteBufferWithInfo byteBufferWithInfo = new ByteBufferWithInfo(orb, byteBufferAsReadOnlyBuffer);
                byteBufferWithInfo.buflen = 12;
                CDRInputStream_1_0.printBuffer(byteBufferWithInfo);
            }
            int i2 = ((byteBuffer.get(0) << 24) & (-16777216)) | ((byteBuffer.get(1) << 16) & 16711680) | ((byteBuffer.get(2) << 8) & NormalizerImpl.CC_MASK) | ((byteBuffer.get(3) << 0) & 255);
            if (i2 != 1195986768) {
                throw wrapper.giopMagicError(CompletionStatus.COMPLETED_MAYBE);
            }
            byte b2 = 0;
            if (byteBuffer.get(4) == 13 && byteBuffer.get(5) <= 1 && byteBuffer.get(5) > 0 && orb.getORBData().isJavaSerializationEnabled()) {
                b2 = byteBuffer.get(5);
                byteBuffer.put(4, (byte) 1);
                byteBuffer.put(5, (byte) 2);
            }
            GIOPVersion gIOPVersion = orb.getORBData().getGIOPVersion();
            if (orb.giopDebugFlag) {
                dprint(".readGIOPHeader: Message GIOP version: " + ((int) byteBuffer.get(4)) + '.' + ((int) byteBuffer.get(5)));
                dprint(".readGIOPHeader: ORB Max GIOP Version: " + ((Object) gIOPVersion));
            }
            if ((byteBuffer.get(4) > gIOPVersion.getMajor() || (byteBuffer.get(4) == gIOPVersion.getMajor() && byteBuffer.get(5) > gIOPVersion.getMinor())) && byteBuffer.get(7) != 6) {
                throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
            }
            AreFragmentsAllowed(byteBuffer.get(4), byteBuffer.get(5), byteBuffer.get(6), byteBuffer.get(7));
            switch (byteBuffer.get(7)) {
                case 0:
                    if (orb.giopDebugFlag) {
                        dprint(".readGIOPHeader: creating RequestMessage");
                    }
                    if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 0) {
                        fragmentMessage_1_2 = new RequestMessage_1_0(orb);
                        break;
                    } else if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 1) {
                        fragmentMessage_1_2 = new RequestMessage_1_1(orb);
                        break;
                    } else if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 2) {
                        fragmentMessage_1_2 = new RequestMessage_1_2(orb);
                        break;
                    } else {
                        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
                    }
                    break;
                case 1:
                    if (orb.giopDebugFlag) {
                        dprint(".readGIOPHeader: creating ReplyMessage");
                    }
                    if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 0) {
                        fragmentMessage_1_2 = new ReplyMessage_1_0(orb);
                        break;
                    } else if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 1) {
                        fragmentMessage_1_2 = new ReplyMessage_1_1(orb);
                        break;
                    } else if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 2) {
                        fragmentMessage_1_2 = new ReplyMessage_1_2(orb);
                        break;
                    } else {
                        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
                    }
                    break;
                case 2:
                    if (orb.giopDebugFlag) {
                        dprint(".readGIOPHeader: creating CancelRequestMessage");
                    }
                    if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 0) {
                        fragmentMessage_1_2 = new CancelRequestMessage_1_0();
                        break;
                    } else if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 1) {
                        fragmentMessage_1_2 = new CancelRequestMessage_1_1();
                        break;
                    } else if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 2) {
                        fragmentMessage_1_2 = new CancelRequestMessage_1_2();
                        break;
                    } else {
                        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
                    }
                    break;
                case 3:
                    if (orb.giopDebugFlag) {
                        dprint(".readGIOPHeader: creating LocateRequestMessage");
                    }
                    if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 0) {
                        fragmentMessage_1_2 = new LocateRequestMessage_1_0(orb);
                        break;
                    } else if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 1) {
                        fragmentMessage_1_2 = new LocateRequestMessage_1_1(orb);
                        break;
                    } else if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 2) {
                        fragmentMessage_1_2 = new LocateRequestMessage_1_2(orb);
                        break;
                    } else {
                        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
                    }
                    break;
                case 4:
                    if (orb.giopDebugFlag) {
                        dprint(".readGIOPHeader: creating LocateReplyMessage");
                    }
                    if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 0) {
                        fragmentMessage_1_2 = new LocateReplyMessage_1_0(orb);
                        break;
                    } else if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 1) {
                        fragmentMessage_1_2 = new LocateReplyMessage_1_1(orb);
                        break;
                    } else if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 2) {
                        fragmentMessage_1_2 = new LocateReplyMessage_1_2(orb);
                        break;
                    } else {
                        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
                    }
                    break;
                case 5:
                case 6:
                    if (orb.giopDebugFlag) {
                        dprint(".readGIOPHeader: creating Message for CloseConnection or MessageError");
                    }
                    if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 0) {
                        fragmentMessage_1_2 = new Message_1_0();
                        break;
                    } else if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 1) {
                        fragmentMessage_1_2 = new Message_1_1();
                        break;
                    } else if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 2) {
                        fragmentMessage_1_2 = new Message_1_1();
                        break;
                    } else {
                        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
                    }
                    break;
                case 7:
                    if (orb.giopDebugFlag) {
                        dprint(".readGIOPHeader: creating FragmentMessage");
                    }
                    if (byteBuffer.get(4) != 1 || byteBuffer.get(5) != 0) {
                        if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 1) {
                            fragmentMessage_1_2 = new FragmentMessage_1_1();
                            break;
                        } else if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 2) {
                            fragmentMessage_1_2 = new FragmentMessage_1_2();
                            break;
                        } else {
                            throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
                        }
                    }
                    break;
                default:
                    if (orb.giopDebugFlag) {
                        dprint(".readGIOPHeader: UNKNOWN MESSAGE TYPE: " + ((int) byteBuffer.get(7)));
                    }
                    throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
            }
            if (byteBuffer.get(4) == 1 && byteBuffer.get(5) == 0) {
                Message_1_0 message_1_0 = (Message_1_0) fragmentMessage_1_2;
                message_1_0.magic = i2;
                message_1_0.GIOP_version = new GIOPVersion(byteBuffer.get(4), byteBuffer.get(5));
                message_1_0.byte_order = byteBuffer.get(6) == 1;
                fragmentMessage_1_2.threadPoolToUse = 0;
                message_1_0.message_type = byteBuffer.get(7);
                message_1_0.message_size = readSize(byteBuffer.get(8), byteBuffer.get(9), byteBuffer.get(10), byteBuffer.get(11), message_1_0.isLittleEndian()) + 12;
            } else {
                FragmentMessage_1_2 fragmentMessage_1_22 = (Message_1_1) fragmentMessage_1_2;
                fragmentMessage_1_22.magic = i2;
                fragmentMessage_1_22.GIOP_version = new GIOPVersion(byteBuffer.get(4), byteBuffer.get(5));
                fragmentMessage_1_22.flags = (byte) (byteBuffer.get(6) & 3);
                fragmentMessage_1_2.threadPoolToUse = (byteBuffer.get(6) >>> 2) & 63;
                fragmentMessage_1_22.message_type = byteBuffer.get(7);
                fragmentMessage_1_22.message_size = readSize(byteBuffer.get(8), byteBuffer.get(9), byteBuffer.get(10), byteBuffer.get(11), fragmentMessage_1_22.isLittleEndian()) + 12;
            }
            if (orb.giopDebugFlag) {
                dprint(".readGIOPHeader: header construction complete.");
                ByteBuffer byteBufferAsReadOnlyBuffer2 = byteBuffer.asReadOnlyBuffer();
                byte[] bArr = new byte[12];
                byteBufferAsReadOnlyBuffer2.position(0).limit(12);
                byteBufferAsReadOnlyBuffer2.get(bArr, 0, bArr.length);
                fragmentMessage_1_2.giopHeader = bArr;
            }
            fragmentMessage_1_2.setByteBuffer(byteBuffer);
            fragmentMessage_1_2.setEncodingVersion(b2);
            return fragmentMessage_1_2;
        } catch (IOException e2) {
            throw wrapper.ioexceptionWhenReadingConnection(e2);
        }
    }

    public static Message readGIOPBody(ORB orb, CorbaConnection corbaConnection, Message message) {
        ReadTimeouts transportTCPReadTimeouts = orb.getORBData().getTransportTCPReadTimeouts();
        ByteBuffer byteBuffer = message.getByteBuffer();
        byteBuffer.position(12);
        try {
            ByteBuffer byteBuffer2 = corbaConnection.read(byteBuffer, 12, message.getSize() - 12, transportTCPReadTimeouts.get_max_time_to_wait());
            message.setByteBuffer(byteBuffer2);
            if (orb.giopDebugFlag) {
                dprint(".readGIOPBody: received message:");
                ByteBuffer byteBufferAsReadOnlyBuffer = byteBuffer2.asReadOnlyBuffer();
                byteBufferAsReadOnlyBuffer.position(0).limit(message.getSize());
                CDRInputStream_1_0.printBuffer(new ByteBufferWithInfo(orb, byteBufferAsReadOnlyBuffer));
            }
            return message;
        } catch (IOException e2) {
            throw wrapper.ioexceptionWhenReadingConnection(e2);
        }
    }

    private static RequestMessage createRequest(ORB orb, GIOPVersion gIOPVersion, byte b2, int i2, boolean z2, byte[] bArr, String str, ServiceContexts serviceContexts, Principal principal) {
        byte b3;
        if (gIOPVersion.equals(GIOPVersion.V1_0)) {
            return new RequestMessage_1_0(orb, serviceContexts, i2, z2, bArr, str, principal);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_1)) {
            return new RequestMessage_1_1(orb, serviceContexts, i2, z2, new byte[]{0, 0, 0}, bArr, str, principal);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_2)) {
            if (z2) {
                b3 = 3;
            } else {
                b3 = 0;
            }
            TargetAddress targetAddress = new TargetAddress();
            targetAddress.object_key(bArr);
            RequestMessage_1_2 requestMessage_1_2 = new RequestMessage_1_2(orb, i2, b3, new byte[]{0, 0, 0}, targetAddress, str, serviceContexts);
            requestMessage_1_2.setEncodingVersion(b2);
            return requestMessage_1_2;
        }
        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v51, types: [com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage] */
    public static RequestMessage createRequest(ORB orb, GIOPVersion gIOPVersion, byte b2, int i2, boolean z2, IOR ior, short s2, String str, ServiceContexts serviceContexts, Principal principal) {
        byte b3;
        RequestMessage_1_2 requestMessage_1_2;
        IIOPProfile profile = ior.getProfile();
        if (s2 == 0) {
            profile = ior.getProfile();
            requestMessage_1_2 = createRequest(orb, gIOPVersion, b2, i2, z2, profile.getObjectKey().getBytes(orb), str, serviceContexts, principal);
        } else {
            if (!gIOPVersion.equals(GIOPVersion.V1_2)) {
                throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
            }
            if (z2) {
                b3 = 3;
            } else {
                b3 = 0;
            }
            TargetAddress targetAddress = new TargetAddress();
            if (s2 == 1) {
                profile = ior.getProfile();
                targetAddress.profile(profile.getIOPProfile());
            } else if (s2 == 2) {
                targetAddress.ior(new IORAddressingInfo(0, ior.getIOPIOR()));
            } else {
                throw wrapper.illegalTargetAddressDisposition(CompletionStatus.COMPLETED_NO);
            }
            requestMessage_1_2 = new RequestMessage_1_2(orb, i2, b3, new byte[]{0, 0, 0}, targetAddress, str, serviceContexts);
            requestMessage_1_2.setEncodingVersion(b2);
        }
        if (gIOPVersion.supportsIORIIOPProfileComponents()) {
            int requestPartitioningId = 0;
            Iterator itIteratorById = ((IIOPProfileTemplate) profile.getTaggedProfileTemplate()).iteratorById(ORBConstants.TAG_REQUEST_PARTITIONING_ID);
            if (itIteratorById.hasNext()) {
                requestPartitioningId = ((RequestPartitioningComponent) itIteratorById.next()).getRequestPartitioningId();
            }
            if (requestPartitioningId < 0 || requestPartitioningId > 63) {
                throw wrapper.invalidRequestPartitioningId(new Integer(requestPartitioningId), new Integer(0), new Integer(63));
            }
            requestMessage_1_2.setThreadPoolToUse(requestPartitioningId);
        }
        return requestMessage_1_2;
    }

    public static ReplyMessage createReply(ORB orb, GIOPVersion gIOPVersion, byte b2, int i2, int i3, ServiceContexts serviceContexts, IOR ior) {
        if (gIOPVersion.equals(GIOPVersion.V1_0)) {
            return new ReplyMessage_1_0(orb, serviceContexts, i2, i3, ior);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_1)) {
            return new ReplyMessage_1_1(orb, serviceContexts, i2, i3, ior);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_2)) {
            ReplyMessage_1_2 replyMessage_1_2 = new ReplyMessage_1_2(orb, i2, i3, serviceContexts, ior);
            replyMessage_1_2.setEncodingVersion(b2);
            return replyMessage_1_2;
        }
        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
    }

    public static LocateRequestMessage createLocateRequest(ORB orb, GIOPVersion gIOPVersion, byte b2, int i2, byte[] bArr) {
        if (gIOPVersion.equals(GIOPVersion.V1_0)) {
            return new LocateRequestMessage_1_0(orb, i2, bArr);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_1)) {
            return new LocateRequestMessage_1_1(orb, i2, bArr);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_2)) {
            TargetAddress targetAddress = new TargetAddress();
            targetAddress.object_key(bArr);
            LocateRequestMessage_1_2 locateRequestMessage_1_2 = new LocateRequestMessage_1_2(orb, i2, targetAddress);
            locateRequestMessage_1_2.setEncodingVersion(b2);
            return locateRequestMessage_1_2;
        }
        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
    }

    public static LocateReplyMessage createLocateReply(ORB orb, GIOPVersion gIOPVersion, byte b2, int i2, int i3, IOR ior) {
        if (gIOPVersion.equals(GIOPVersion.V1_0)) {
            return new LocateReplyMessage_1_0(orb, i2, i3, ior);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_1)) {
            return new LocateReplyMessage_1_1(orb, i2, i3, ior);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_2)) {
            LocateReplyMessage_1_2 locateReplyMessage_1_2 = new LocateReplyMessage_1_2(orb, i2, i3, ior);
            locateReplyMessage_1_2.setEncodingVersion(b2);
            return locateReplyMessage_1_2;
        }
        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
    }

    public static CancelRequestMessage createCancelRequest(GIOPVersion gIOPVersion, int i2) {
        if (gIOPVersion.equals(GIOPVersion.V1_0)) {
            return new CancelRequestMessage_1_0(i2);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_1)) {
            return new CancelRequestMessage_1_1(i2);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_2)) {
            return new CancelRequestMessage_1_2(i2);
        }
        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
    }

    public static Message createCloseConnection(GIOPVersion gIOPVersion) {
        if (gIOPVersion.equals(GIOPVersion.V1_0)) {
            return new Message_1_0(Message.GIOPBigMagic, false, (byte) 5, 0);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_1)) {
            return new Message_1_1(Message.GIOPBigMagic, GIOPVersion.V1_1, (byte) 0, (byte) 5, 0);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_2)) {
            return new Message_1_1(Message.GIOPBigMagic, GIOPVersion.V1_2, (byte) 0, (byte) 5, 0);
        }
        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
    }

    public static Message createMessageError(GIOPVersion gIOPVersion) {
        if (gIOPVersion.equals(GIOPVersion.V1_0)) {
            return new Message_1_0(Message.GIOPBigMagic, false, (byte) 6, 0);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_1)) {
            return new Message_1_1(Message.GIOPBigMagic, GIOPVersion.V1_1, (byte) 0, (byte) 6, 0);
        }
        if (gIOPVersion.equals(GIOPVersion.V1_2)) {
            return new Message_1_1(Message.GIOPBigMagic, GIOPVersion.V1_2, (byte) 0, (byte) 6, 0);
        }
        throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
    }

    public static FragmentMessage createFragmentMessage(GIOPVersion gIOPVersion) {
        return null;
    }

    public static int getRequestId(Message message) {
        switch (message.getType()) {
            case 0:
                return ((RequestMessage) message).getRequestId();
            case 1:
                return ((ReplyMessage) message).getRequestId();
            case 2:
                return ((CancelRequestMessage) message).getRequestId();
            case 3:
                return ((LocateRequestMessage) message).getRequestId();
            case 4:
                return ((LocateReplyMessage) message).getRequestId();
            case 5:
            case 6:
            default:
                throw wrapper.illegalGiopMsgType(CompletionStatus.COMPLETED_MAYBE);
            case 7:
                return ((FragmentMessage) message).getRequestId();
        }
    }

    public static void setFlag(ByteBuffer byteBuffer, int i2) {
        byteBuffer.put(6, (byte) (byteBuffer.get(6) | i2));
    }

    public static void clearFlag(byte[] bArr, int i2) {
        bArr[6] = (byte) (bArr[6] & (255 ^ i2));
    }

    private static void AreFragmentsAllowed(byte b2, byte b3, byte b4, byte b5) {
        if (b2 == 1 && b3 == 0 && b5 == 7) {
            throw wrapper.fragmentationDisallowed(CompletionStatus.COMPLETED_MAYBE);
        }
        if ((b4 & 2) == 2) {
            switch (b5) {
                case 2:
                case 5:
                case 6:
                    throw wrapper.fragmentationDisallowed(CompletionStatus.COMPLETED_MAYBE);
                case 3:
                case 4:
                    if (b2 == 1 && b3 == 1) {
                        throw wrapper.fragmentationDisallowed(CompletionStatus.COMPLETED_MAYBE);
                    }
                    return;
                default:
                    return;
            }
        }
    }

    static ObjectKey extractObjectKey(byte[] bArr, ORB orb) {
        if (bArr != null) {
            try {
                ObjectKey objectKeyCreate = orb.getObjectKeyFactory().create(bArr);
                if (objectKeyCreate != null) {
                    return objectKeyCreate;
                }
            } catch (Exception e2) {
            }
        }
        throw wrapper.invalidObjectKey();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    static ObjectKey extractObjectKey(TargetAddress targetAddress, ORB orb) {
        ObjectKey objectKey;
        ObjectKey objectKey2;
        ObjectKey objectKeyCreate;
        short gIOPTargetAddressPreference = orb.getORBData().getGIOPTargetAddressPreference();
        short sDiscriminator = targetAddress.discriminator();
        switch (gIOPTargetAddressPreference) {
            case 0:
                if (sDiscriminator != 0) {
                    throw new AddressingDispositionException((short) 0);
                }
                break;
            case 1:
                if (sDiscriminator != 1) {
                    throw new AddressingDispositionException((short) 1);
                }
                break;
            case 2:
                if (sDiscriminator != 2) {
                    throw new AddressingDispositionException((short) 2);
                }
                break;
            case 3:
                break;
            default:
                throw wrapper.orbTargetAddrPreferenceInExtractObjectkeyInvalid();
        }
        switch (sDiscriminator) {
            case 0:
                byte[] bArrObject_key = targetAddress.object_key();
                if (bArrObject_key != null && (objectKeyCreate = orb.getObjectKeyFactory().create(bArrObject_key)) != null) {
                    return objectKeyCreate;
                }
                throw wrapper.invalidObjectKey();
            case 1:
                TaggedProfile taggedProfileProfile = targetAddress.profile();
                if (taggedProfileProfile != null && (objectKey2 = IIOPFactories.makeIIOPProfile(orb, taggedProfileProfile).getObjectKey()) != null) {
                    return objectKey2;
                }
                throw wrapper.invalidObjectKey();
            case 2:
                IORAddressingInfo iORAddressingInfoIor = targetAddress.ior();
                if (iORAddressingInfoIor != null && (objectKey = IIOPFactories.makeIIOPProfile(orb, iORAddressingInfoIor.ior.profiles[iORAddressingInfoIor.selected_profile_index]).getObjectKey()) != null) {
                    return objectKey;
                }
                throw wrapper.invalidObjectKey();
            default:
                throw wrapper.invalidObjectKey();
        }
    }

    private static int readSize(byte b2, byte b3, byte b4, byte b5, boolean z2) {
        int i2;
        int i3;
        int i4;
        int i5;
        if (!z2) {
            i2 = (b2 << 24) & (-16777216);
            i3 = (b3 << 16) & 16711680;
            i4 = (b4 << 8) & NormalizerImpl.CC_MASK;
            i5 = (b5 << 0) & 255;
        } else {
            i2 = (b5 << 24) & (-16777216);
            i3 = (b4 << 16) & 16711680;
            i4 = (b3 << 8) & NormalizerImpl.CC_MASK;
            i5 = (b2 << 0) & 255;
        }
        return i2 | i3 | i4 | i5;
    }

    static void nullCheck(Object obj) {
        if (obj == null) {
            throw wrapper.nullNotAllowed();
        }
    }

    static SystemException getSystemException(String str, int i2, CompletionStatus completionStatus, String str2, ORBUtilSystemException oRBUtilSystemException) {
        SystemException systemException;
        try {
            Class<?> clsLoadClass = SharedSecrets.getJavaCorbaAccess().loadClass(str);
            if (str2 == null) {
                systemException = (SystemException) clsLoadClass.newInstance();
            } else {
                systemException = (SystemException) clsLoadClass.getConstructor(String.class).newInstance(str2);
            }
            systemException.minor = i2;
            systemException.completed = completionStatus;
            return systemException;
        } catch (Exception e2) {
            throw oRBUtilSystemException.badSystemExceptionInReply(CompletionStatus.COMPLETED_MAYBE, e2);
        }
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void callback(MessageHandler messageHandler) throws IOException {
        messageHandler.handleInput(this);
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public ByteBuffer getByteBuffer() {
        return this.byteBuffer;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void setByteBuffer(ByteBuffer byteBuffer) {
        this.byteBuffer = byteBuffer;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public int getThreadPoolToUse() {
        return this.threadPoolToUse;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public byte getEncodingVersion() {
        return this.encodingVersion;
    }

    @Override // com.sun.corba.se.impl.protocol.giopmsgheaders.Message
    public void setEncodingVersion(byte b2) {
        this.encodingVersion = b2;
    }

    private static void dprint(String str) {
        ORBUtility.dprint("MessageBase", str);
    }
}
