package phonis.cannonliner.trace;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Line {

    private Location start;
    private Location finish;
    private final ParticleType type;
    private final boolean connected;
    private final Vector direction;
    private final LineEq lineEq;
    public final Set<Artifact> artifacts = new HashSet<>();

    public Line(Location start, Location finish, ParticleType type, ParticleType startType, ParticleType finishType, OffsetType startOffsetType, OffsetType finishOffsetType, boolean connected) {
        this.start = start;
        this.finish = finish;
        this.type = type;
        this.connected = connected;
        this.direction = this.finish.clone().subtract(this.start).toVector().normalize();
        this.lineEq = new LineEq(direction, this.start);

        if (startType != null && startOffsetType != null) {
            this.artifacts.add(new Artifact(this.start, startType, startOffsetType));
        }

        if (finishType != null && finishOffsetType != null) {
            this.artifacts.add(new Artifact(this.finish, finishType, finishOffsetType));
        }
    }

    public Line(Location start, Location finish, ParticleType type, boolean connected) {
        this(start, finish, type, null, null, null, null, connected);
    }

    public Location getStart() {
        return this.start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getFinish() {
        return this.finish;
    }

    public void setFinish(Location finish) {
        this.finish = finish;
    }

    public Vector getDirection() {
        return this.direction;
    }

    public ParticleType getType() {
        return this.type;
    }

    public Line getCombinedLine(Line other) {
        if (this.start.distance(other.finish) >= other.start.distance(this.finish)) {
            return new Line(this.start, other.finish, this.type, this.connected).addArtifacts(this).addArtifacts(other);
        }

        return new Line(other.start, this.finish, this.type, this.connected).addArtifacts(this).addArtifacts(other);
    }

    public Line addArtifacts(Line other) {
        if (!other.artifacts.isEmpty()) this.artifacts.addAll(other.artifacts);

        return this;
    }

    public LineEq getLineEq() {
        return this.lineEq;
    }

    private List<ParticleLocation> getEndParticles(int life) {
        List<ParticleLocation> ret = new ArrayList<>();

        ret.add(new ParticleLocation(this.start, life, this.type));
        ret.add(new ParticleLocation(this.finish, life, this.type));

        return ret;
    }

    private List<ParticleLocation> getLineParticles(int life) {
        double distance = this.start.distance(this.finish);
        Vector intervalDirection = this.direction.multiply(.25);
        Vector di2 = intervalDirection.clone();

        List<ParticleLocation> ret = new ArrayList<>(this.getEndParticles(life));

        while (di2.length() < distance) {
            ret.add(new ParticleLocation(this.start.clone().add(di2.getX(), di2.getY(), di2.getZ()), life, this.type));
            di2.add(intervalDirection);
        }

        return ret;
    }

    public List<ParticleLocation> getParticles(int life) {
        List<ParticleLocation> ret = new ArrayList<>();

        for (Artifact artifact : this.artifacts) {
            ret.addAll(artifact.getParticles(life));
        }

        if (this.connected) ret.addAll(this.getLineParticles(life));
        else ret.addAll(this.getEndParticles(life));

        return ret;
    }

    public boolean contains(Line other) {
        return (
            (
                this.start.getX() <= this.finish.getX() &&
                this.start.getX() <= other.start.getX() &&
                    this.finish.getX() >= other.finish.getX()
            ) || (
                this.start.getX() >= this.finish.getX() &&
                this.start.getX() >= other.start.getX() &&
                    this.finish.getX() <= other.finish.getX()
            )
        ) && (
            (
                this.start.getY() <= this.finish.getY() &&
                this.start.getY() <= other.start.getY() &&
                    this.finish.getY() >= other.finish.getY()
            ) || (
                this.start.getY() >= this.finish.getY() &&
                this.start.getY() >= other.start.getY() &&
                    this.finish.getY() <= other.finish.getY()
            )
        ) && (
            (
                this.start.getZ() <= this.finish.getZ() &&
                this.start.getZ() <= other.start.getZ() &&
                    this.finish.getZ() >= other.finish.getZ()
            ) || (
                this.start.getZ() >= this.finish.getZ() &&
                this.start.getZ() >= other.start.getZ() &&
                    this.finish.getZ() <= other.finish.getZ()
            )
        );
    }

    public boolean overlaps(Line other) {
        return (
            (
                this.start.getX() <= other.start.getX() &&
                    this.finish.getX() <= other.finish.getX() &&
                    this.finish.getX() >= other.start.getX()
            ) || (
                this.start.getX() >= other.start.getX() &&
                    this.finish.getX() >= other.finish.getX() &&
                    this.finish.getX() <= other.start.getX()
            )
        ) && (
            (
                this.start.getY() <= other.start.getY() &&
                    this.finish.getY() <= other.finish.getY() &&
                    this.finish.getY() >= other.start.getY()
            ) || (
                this.start.getY() >= other.start.getY() &&
                    this.finish.getY() >= other.finish.getY() &&
                    this.finish.getY() <= other.start.getY()
            )
        ) && (
            (
                this.start.getZ() <= other.start.getZ() &&
                    this.finish.getZ() <= other.finish.getZ() &&
                    this.finish.getZ() >= other.start.getZ()
            ) || (
                this.start.getZ() >= other.start.getZ() &&
                    this.finish.getZ() >= other.finish.getZ() &&
                    this.finish.getZ() <= other.start.getZ()
            )
        );
    }

}
