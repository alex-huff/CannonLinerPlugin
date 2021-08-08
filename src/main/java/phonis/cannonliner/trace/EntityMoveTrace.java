package phonis.cannonliner.trace;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public abstract class EntityMoveTrace extends Trace {
    protected boolean isConnected;

    public EntityMoveTrace(Location start, Location finish) {
        super(start, finish);
    }

    protected abstract ParticleType getType();

    protected abstract ParticleType getStartType();

    protected abstract ParticleType getFinishType();

    protected abstract OffsetType getStartOffsetType();

    protected abstract OffsetType getFinishOffsetType();

    @Override
    public List<Line> getLines() {
        List<Line> ret = new ArrayList<>();
        Location loc1 = this.getStart().clone();
        loc1.setY(this.getFinish().getY());
        Location loc2 = loc1.clone();
        loc2.setX(this.getFinish().getX());

        ret.add(
            new Line(
                this.getStart(),
                loc1,
                this.getType(),
                this.getStartType(),
                null,
                this.getStartOffsetType(),
                null,
                isConnected
            )
        );

        ret.add(
            new Line(
                loc1,
                loc2,
                this.getType(),
                isConnected
            )
        );

        ret.add(
            new Line(
                loc2,
                this.getFinish(),
                this.getType(),
                null,
                this.getFinishType(),
                null,
                this.getFinishOffsetType(),
                isConnected
            )
        );

        return ret;
    }

}