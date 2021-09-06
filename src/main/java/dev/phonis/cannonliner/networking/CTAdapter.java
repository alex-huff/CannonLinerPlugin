package dev.phonis.cannonliner.networking;

import dev.phonis.cannonliner.trace.Artifact;
import dev.phonis.cannonliner.trace.Line;
import dev.phonis.cannonliner.trace.OffsetType;
import dev.phonis.cannonliner.trace.ParticleType;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.*;

public class CTAdapter {

    public static CTVec3 fromVector(Vector vector) {
        return new CTVec3(
            vector.getX(),
            vector.getY(),
            vector.getZ()
        );
    }

    public static CTLine fromLine(Line line) {
        return new CTLine(
            CTAdapter.fromLocation(line.getStart()),
            CTAdapter.fromLocation(line.getFinish()),
            CTAdapter.fromParticleType(line.getType())
        );
    }

    public static Set<CTArtifact> artifactsFromLine(Line line) {
        Set<CTArtifact> artifacts = new HashSet<>();

        for (Artifact artifact : line.artifacts) {
            artifacts.add(CTAdapter.fromArtifact(artifact));
        }

        return artifacts;
    }

    private static CTVec3 fromLocation(Location location) {
        return new CTVec3(
            location.getX(),
            location.getY(),
            location.getZ()
        );
    }

    private static CTLineType fromParticleType(ParticleType type) {
        if (type.equals(ParticleType.TNT)) {
            return CTLineType.TNT;
        } else if (type.equals(ParticleType.SAND)) {
            return CTLineType.SAND;
        } else if (type.equals(ParticleType.PLAYER)) {
            return CTLineType.PLAYER;
        } else if (type.equals(ParticleType.TNTENDPOS)) {
            return CTLineType.TNTENDPOS;
        } else if (type.equals(ParticleType.SANDENDPOS)) {
            return CTLineType.SANDENDPOS;
        } else {
            return null;
        }
    }

    private static CTArtifact fromArtifact(Artifact artifact) {
        return new CTArtifact(
            CTAdapter.fromLocation(artifact.getLocation()),
            CTAdapter.fromParticleType(artifact.getType()),
            CTAdapter.fromOffsetType(artifact.getOffsetType())
        );
    }

    private static CTArtifactType fromOffsetType(OffsetType type) {
        if (type.equals(OffsetType.BLOCKBOX)) {
            return CTArtifactType.BLOCKBOX;
        }

        return null;
    }

}
