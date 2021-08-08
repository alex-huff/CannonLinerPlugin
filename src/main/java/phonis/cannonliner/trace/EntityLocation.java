package phonis.cannonliner.trace;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class EntityLocation {
    private final Location location;
    private boolean keepAlive;
    private final boolean newEntity;
    private final EntityType type;

    public EntityLocation(Location location, boolean newEntity, EntityType type) {
        this.location = location;
        this.keepAlive = true;
        this.newEntity = newEntity;
        this.type = type;
    }

    public EntityLocation(Location location, EntityType type) {
        this.location = location;
        this.keepAlive = true;
        this.newEntity = false;
        this.type = type;
    }

    public EntityType getType() {
        return this.type;
    }

    public void kill() {
        this.keepAlive = false;
    }

    public boolean getState() {
        return this.keepAlive;
    }

    public Location getLocation() {
        return this.location;
    }

    public boolean getNew() {
        return this.newEntity;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                                              append(this.location).
                                                                       append(this.type).
                                                                                            toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EntityLocation)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        final EntityLocation other = (EntityLocation) obj;

        return new EqualsBuilder().
                                      append(this.getLocation(), other.getLocation()).
                                                                                         append(this.getType(), other.getType()).
                                                                                                                                    isEquals();
    }

}