package phonis.cannontracer.networking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CTArtifact implements Serializable {

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
    private List<CTLine> lines;

    public CTArtifact(CTVec3 location, CTLineType lineType, CTArtifactType artifactType) {
        this.location = location;
        this.lineType = lineType;
        this.artifactType = artifactType;
    }

    private void makeLines() {
        this.lines = new ArrayList<CTLine>();

        if (this.artifactType.equals(CTArtifactType.BLOCKBOX)) {
            for (CTEdge edge : CTArtifact.boxEdges) {
                this.lines.add(
                    new CTLine(
                        null,
                        this.location.plus(edge.start),
                        this.location.plus(edge.finish),
                        this.lineType,
                        null,
                        -1
                    )
                );
            }
        }
    }

    public List<CTLine> getLines() {
        if (this.lines == null) {
            this.makeLines();
        }

        return this.lines;
    }

}
