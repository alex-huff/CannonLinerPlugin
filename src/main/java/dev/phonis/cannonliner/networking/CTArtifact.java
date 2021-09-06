package dev.phonis.cannonliner.networking;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CTArtifact implements CTSerializable {

    private static List<CTEdge> boxEdges;

    static {
        CTArtifact.boxEdges = new ArrayList<CTEdge>(12);
        CTVec3 vu1 = new CTVec3(-.49d, .49d, -.49d);
        CTVec3 vu2 = new CTVec3(.49d, .49d, -.49d);
        CTVec3 vu3 = new CTVec3(-.49d, .49d, .49d);
        CTVec3 vu4 = new CTVec3(.49d, .49d, .49d);
        CTVec3 vd1 = new CTVec3(-.49d, -.49d, -.49d);
        CTVec3 vd2 = new CTVec3(.49d, -.49d, -.49d);
        CTVec3 vd3 = new CTVec3(-.49d, -.49d, .49d);
        CTVec3 vd4 = new CTVec3(.49d, -.49d, .49d);

        CTArtifact.boxEdges.add(new CTEdge(vu1, vu2));
        CTArtifact.boxEdges.add(new CTEdge(vu2, vu4));
        CTArtifact.boxEdges.add(new CTEdge(vu4, vu3));
        CTArtifact.boxEdges.add(new CTEdge(vu3, vu1));
        CTArtifact.boxEdges.add(new CTEdge(vd1, vd2));
        CTArtifact.boxEdges.add(new CTEdge(vd2, vd4));
        CTArtifact.boxEdges.add(new CTEdge(vd4, vd3));
        CTArtifact.boxEdges.add(new CTEdge(vd3, vd1));
        CTArtifact.boxEdges.add(new CTEdge(vu1, vd1));
        CTArtifact.boxEdges.add(new CTEdge(vu2, vd2));
        CTArtifact.boxEdges.add(new CTEdge(vu3, vd3));
        CTArtifact.boxEdges.add(new CTEdge(vu4, vd4));
    }

    public final CTVec3 location;
    public final CTLineType lineType;
    public final CTArtifactType artifactType;

    public CTArtifact(CTVec3 location, CTLineType lineType, CTArtifactType artifactType) {
        this.location = location;
        this.lineType = lineType;
        this.artifactType = artifactType;
    }

    public List<CTLine> makeLines() {
        List<CTLine> lines = new ArrayList<CTLine>();

        if (this.artifactType.equals(CTArtifactType.BLOCKBOX)) {
            for (CTEdge edge : CTArtifact.boxEdges) {
                lines.add(
                    new CTLine(
                        this.location.plus(edge.start),
                        this.location.plus(edge.finish),
                        this.lineType
                    )
                );
            }
        }

        return lines;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CTArtifact) {
            CTArtifact otherArtifact = (CTArtifact) other;

            return this.location.equals(otherArtifact.location) &&
                this.lineType.equals(otherArtifact.lineType) &&
                this.artifactType.equals(otherArtifact.artifactType);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
            append(this.location).
            append(this.lineType.ordinal()).
            append(this.artifactType.ordinal()).
            toHashCode();
    }

    @Override
    public void toBytes(DataOutputStream dos) throws IOException {
        this.location.toBytes(dos);
        this.lineType.toBytes(dos);
        this.artifactType.toBytes(dos);
    }

    public static CTArtifact fromBytes(DataInputStream dis) throws IOException {
        return new CTArtifact(
            CTVec3.fromBytes(dis),
            CTLineType.fromBytes(dis),
            CTArtifactType.fromBytes(dis)
        );
    }

}
