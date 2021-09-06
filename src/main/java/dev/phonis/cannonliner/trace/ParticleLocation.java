package dev.phonis.cannonliner.trace;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Location;

public class ParticleLocation {

    private final Location location;
    private int life;
    private final ParticleType type;

    public ParticleLocation(Location location, int life, ParticleType type) {
        this.location = location;
        this.life = life;
        this.type = type;
    }

    public Location getLocation() {
        return this.location;
    }

    public int getLife() {
        return this.life;
    }

    public void decLife() {
        this.life--;
    }

    public ParticleType getType() {
        return this.type;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                                              append(this.getLocation()).
                                                                            append(this.getType()).
                                                                                                      toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ParticleLocation)) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        final ParticleLocation pl = (ParticleLocation) obj;

        return new EqualsBuilder().
                                      append(this.getLocation(), pl.getLocation()).
                                                                                      append(this.getType(), pl.getType()).
                                                                                                                              isEquals();
    }

}