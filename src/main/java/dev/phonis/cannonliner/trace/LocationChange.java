package dev.phonis.cannonliner.trace;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class LocationChange {

    private final World world;
    private final Location start;
    private final Location finish;
    private final EntityType type;
    private final ChangeType changeType;
    private final double velocity;

    public LocationChange(World world, Location start, Location finish, EntityType type, ChangeType changeType) {
        this.world = world;
        this.start = start;
        this.finish = finish;
        this.type = type;
        this.changeType = changeType;
        this.velocity = this.start.distance(this.finish);
    }

    public LocationChange(World world, Location start, Location finish, EntityType type, ChangeType changeType, double velocity) {
        this.world = world;
        this.start = start;
        this.finish = finish;
        this.type = type;
        this.changeType = changeType;
        this.velocity = velocity;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                                              append(this.getWorld().getUID()).
                                                                                  append(this.getStart()).
                                                                                                             append(this.getFinish()).
                                                                                                                                         append(this.getType()).
                                                                                                                                                                   append(this.getChangeType()).
                                                                                                                                                                                                   toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LocationChange)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        final LocationChange other = (LocationChange) obj;

        return new EqualsBuilder().
                                      append(this.getWorld().getUID(), other.getWorld().getUID()).
                                                                                                     append(this.getStart(), other.getStart()).
                                                                                                                                                  append(this.getFinish(), other.getFinish()).
                                                                                                                                                                                                 append(this.getType(), other.getType()).
                                                                                                                                                                                                                                            append(this.getChangeType(), other.getChangeType()).
                                                                                                                                                                                                                                                                                                   isEquals();
    }

    public World getWorld() {
        return this.world;
    }

    public Location getStart() {
        return this.start;
    }

    public Location getFinish() {
        return this.finish;
    }

    public double getVelocity() {
        return this.velocity;
    }

    public EntityType getType() {
        return this.type;
    }

    public ChangeType getChangeType() {
        return this.changeType;
    }

}