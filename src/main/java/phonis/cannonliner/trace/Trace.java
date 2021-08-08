package phonis.cannonliner.trace;

import org.bukkit.Location;

import java.util.List;

public abstract class Trace {
    private final Location start;
    private final Location finish;

    public Trace(Location start, Location finish) {
        this.start = start;
        this.finish = finish;
    }

    public abstract List<Line> getLines();

    protected Location getStart() {
        return this.start;
    }

    protected Location getFinish() {
        return this.finish;
    }

}
