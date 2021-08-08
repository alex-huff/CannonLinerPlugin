package phonis.cannonliner.trace;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Location;
import phonis.cannonliner.util.Offset;

import java.util.ArrayList;
import java.util.List;

public class Artifact {

    private final Location location;
    private final ParticleType type;
    private final OffsetType offsetType;

    public Artifact(Location location, ParticleType type, OffsetType offsetType) {
        this.location = location;
        this.type = type;
        this.offsetType = offsetType;
    }

    public Location getLocation() {
        return this.location;
    }

    public ParticleType getType() {
        return this.type;
    }

    public OffsetType getOffsetType() {
        return this.offsetType;
    }

    public List<ParticleLocation> getParticles(int life) {
        List<ParticleLocation> ret = new ArrayList<>();

        for (Offset offset : this.getOffsetType().getOffset()) {
            ret.add(
                new ParticleLocation(
                    this.location.clone().add(offset.getX(), offset.getY(), offset.getZ()),
                    life,
                    this.type
                )
            );
        }

        return ret;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                                              append(this.location).
                                                                       append(this.type).
                                                                                            append(this.offsetType).
                                                                                                                       toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Artifact)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        final Artifact other = (Artifact) obj;

        return new EqualsBuilder().
                                      append(this.location, other.location).
                                                                               append(this.type, other.type).
                                                                                                                append(this.offsetType, other.offsetType).
                                                                                                                                                             isEquals();
    }

}
