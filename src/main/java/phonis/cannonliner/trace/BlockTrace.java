package phonis.cannonliner.trace;

import org.bukkit.Location;

public abstract class BlockTrace extends EntityMoveTrace {
    private final boolean isStart;
    private final boolean isFinish;

    public BlockTrace(Location start, Location finish, boolean isStart, boolean isFinish) {
        super(start.clone().add(0, .49, 0), finish.clone().add(0, .49, 0));

        this.isStart = isStart;
        this.isFinish = isFinish;
    }

    protected abstract ParticleType getSType();

    protected abstract ParticleType getFType();

    @Override
    protected ParticleType getStartType() {
        if (this.isStart) {
            return this.getSType();
        }

        return null;
    }

    @Override
    protected ParticleType getFinishType() {
        if (this.isFinish) {
            return this.getFType();
        }

        return null;
    }

    @Override
    protected OffsetType getStartOffsetType() {
        return OffsetType.BLOCKBOX;
    }

    @Override
    protected OffsetType getFinishOffsetType() {
        return OffsetType.BLOCKBOX;
    }

}
