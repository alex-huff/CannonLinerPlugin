package phonis.cannonliner.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CTNewLines implements CTPacket {

    public final List<CTLine> lines;

    public CTNewLines(List<CTLine> lines) {
        this.lines = lines;
    }

    @Override
    public void toBytes(DataOutputStream dos) throws IOException {
        dos.writeShort(this.lines.size());

        for (CTLine line : this.lines) {
            line.toBytes(dos);
        }
    }

    public static CTNewLines fromBytes(DataInputStream dis) throws IOException {
        return new CTNewLines(
            CTNewLines.getLines(dis)
        );
    }

    public static List<CTLine> getLines(DataInputStream dis) throws IOException {
        List<CTLine> ctLines = new ArrayList<CTLine>();
        short length = dis.readShort();

        for (short i = 0; i < length; i++) {
            ctLines.add(CTLine.fromBytes(dis));
        }

        return ctLines;
    }

    @Override
    public byte packetID() {
        return Packets.newLinesID;
    }

}
