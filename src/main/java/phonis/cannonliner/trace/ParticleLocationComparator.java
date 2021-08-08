package phonis.cannonliner.trace;

import org.bukkit.Location;

import java.util.Comparator;

public class ParticleLocationComparator implements Comparator<ParticleLocation> {

    private final Location loc;

    public ParticleLocationComparator(Location loc) {
        this.loc = loc;
    }

    @Override
    public int compare(ParticleLocation o1, ParticleLocation o2) {
        return Double.compare(loc.distance(o1.getLocation()), loc.distance(o2.getLocation()));
    }

}
