package dev.phonis.cannonliner.trace;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import dev.phonis.cannonliner.networking.CTAdapter;
import dev.phonis.cannonliner.networking.CTVec3;

public class LineEq {

    private final CTVec3 direction;
    private final double yzInty;
    private final double yzIntz;
    private final double yxInty;
    private final double yxIntx;
    private final double zxIntz;
    private final double zxIntx;

    public LineEq(CTVec3 direction, Location location) {
        this.direction = direction;

        if (direction.x != 0) {
            double yzInt = (location.getX() * -1) / direction.x;

            this.yzInty = location.getY() + direction.y * yzInt;
            this.yzIntz = location.getZ() + direction.z * yzInt;
        } else {
            this.yzInty = Double.NaN;
            this.yzIntz = Double.NaN;
        }

        if (direction.y != 0) {
            double zxInt = (location.getY() * -1) / direction.y;

            this.zxIntz = location.getZ() + direction.z * zxInt;
            this.zxIntx = location.getX() + direction.x + zxInt;
        } else {
            this.zxIntz = Double.NaN;
            this.zxIntx = Double.NaN;
        }

        if (direction.z != 0) {
            double yxInt = (location.getZ() * -1) / direction.z;

            this.yxInty = location.getY() + direction.y * yxInt;
            this.yxIntx = location.getX() + direction.x * yxInt;
        } else {
            this.yxInty = Double.NaN;
            this.yxIntx = Double.NaN;
        }
    }

    public LineEq(Vector direction, Location location) {
        this(CTAdapter.fromVector(direction), location);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(this.direction).
                append(this.yzInty).
                append(this.yzIntz).
                append(this.yxInty).
                append(this.yxIntx).
                append(this.zxIntz).
                append(this.zxIntx).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LineEq)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        final LineEq other = (LineEq) obj;

        return new EqualsBuilder().
                append(this.direction, other.direction).
                append(this.yzInty, other.yzInty).
                append(this.yzIntz, other.yzIntz).
                append(this.yxInty, other.yxInty).
                append(this.yxIntx, other.yxIntx).
                append(this.zxIntz, other.zxIntz).
                append(this.zxIntx, other.zxIntx).
                isEquals();
    }

}
