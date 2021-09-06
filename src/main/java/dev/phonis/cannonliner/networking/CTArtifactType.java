package dev.phonis.cannonliner.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public enum CTArtifactType implements CTSerializable {

    BLOCKBOX;

    @Override
    public void toBytes(DataOutputStream dos) throws IOException {
        dos.writeByte(this.ordinal());
    }

    public static CTArtifactType fromBytes(DataInputStream dis) throws IOException {
        return CTArtifactType.values()[dis.readByte()];
    }

}
