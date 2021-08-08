package phonis.cannonliner.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public enum CTLineType implements CTSerializable {

    SAND, PLAYER, TNT, TNTENDPOS, SANDENDPOS, ALL;

    final static CTColor colorSand = new CTColor(1f, 1f, 0);
    final static CTColor colorTNT = new CTColor(1f, 0, 0);
    final static CTColor colorExplosion = new CTColor(1f, 0, 1f);
    final static CTColor colorPlayer = new CTColor(0, 0, 1f);

    public CTColor getRGB() {
        if (this == SAND) {
            return colorSand;
        } else if (this == TNT) {
            return colorTNT;
        } else if (this == PLAYER) {
            return colorPlayer;
        } else {
            return colorExplosion;
        }
    }

    @Override
    public void toBytes(DataOutputStream dos) throws IOException {
        dos.writeByte(this.ordinal());
    }

    public static CTLineType fromBytes(DataInputStream dis) throws IOException {
        return CTLineType.values()[dis.readByte()];
    }

}
