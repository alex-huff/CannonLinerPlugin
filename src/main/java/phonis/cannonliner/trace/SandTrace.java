package phonis.cannonliner.trace;

import org.bukkit.Location;

public class SandTrace extends BlockTrace {

    public SandTrace(Location start, Location finish, boolean isStart, boolean isFinish) {
        super(start, finish, isStart, isFinish);
    }

    @Override
    protected ParticleType getType() {
        return ParticleType.SAND;
    }

    @Override
    protected ParticleType getSType() {
        return ParticleType.SAND;
    }

    @Override
    protected ParticleType getFType() {
        return ParticleType.SANDENDPOS;
    }

}
