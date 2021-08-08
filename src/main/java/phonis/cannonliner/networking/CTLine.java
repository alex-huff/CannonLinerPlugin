package phonis.cannonliner.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CTLine implements CTSerializable {

    public final CTVec3 start;
    public final CTVec3 finish;
    public final CTLineType type;

    public CTLine(CTVec3 start, CTVec3 finish, CTLineType type) {
        this.start = start;
        this.finish = finish;
        this.type = type;
    }

    public float getR() {
        return this.type.getRGB().r;
    }

    public float getG() {
        return this.type.getRGB().g;
    }

    public float getB() {
        return this.type.getRGB().b;
    }

    @Override
    public void toBytes(DataOutputStream dos) throws IOException {
        this.start.toBytes(dos);
        this.finish.toBytes(dos);
        this.type.toBytes(dos);
    }

    public static CTLine fromBytes(DataInputStream dis) throws IOException {
        return new CTLine(
            CTVec3.fromBytes(dis),
            CTVec3.fromBytes(dis),
            CTLineType.fromBytes(dis)
        );
    }

}
