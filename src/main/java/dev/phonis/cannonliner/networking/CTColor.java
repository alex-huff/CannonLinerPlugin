package dev.phonis.cannonliner.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CTColor implements CTSerializable {

    public final float r;
    public final float g;
    public final float b;

    public CTColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public void toBytes(DataOutputStream dos) throws IOException {
        dos.writeFloat(this.r);
        dos.writeFloat(this.g);
        dos.writeFloat(this.b);
    }

    public static CTColor fromBytes(DataInputStream dis) throws IOException {
        return new CTColor(
            dis.readFloat(),
            dis.readFloat(),
            dis.readFloat()
        );
    }

}
