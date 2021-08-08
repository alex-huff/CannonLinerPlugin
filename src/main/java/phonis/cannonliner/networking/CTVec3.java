package phonis.cannonliner.networking;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class CTVec3 implements CTSerializable {

    public double x;
    public double y;
    public double z;

    public CTVec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CTVec3 plus(CTVec3 other) {
        return new CTVec3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    @Override
    public void toBytes(DataOutputStream dos) throws IOException {
        dos.writeFloat((float) this.x);
        dos.writeFloat((float) this.y);
        dos.writeFloat((float) this.z);
    }

    public static CTVec3 fromBytes(DataInputStream dis) throws IOException {
        return new CTVec3(
            dis.readFloat(),
            dis.readFloat(),
            dis.readFloat()
        );
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
            append(this.x).
            append(this.y).
            append(this.z).
            toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CTVec3) {
            CTVec3 otherVec = (CTVec3) other;

            return this.x == otherVec.x && this.y == otherVec.y && this.z == otherVec.z;
        }

        return false;
    }

}
