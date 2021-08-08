package phonis.cannonliner.trace;

import java.util.ArrayList;
import java.util.Iterator;

public class LineSet extends ArrayList<Line> {

    private final boolean isConnected;

    public LineSet(boolean isConnected) {
        this.isConnected = isConnected;
    }

    @Override
    public boolean add(Line line) {
        if (!this.isConnected) {
            super.add(line);

            return true;
        }

        Line toAdd = line;

        while (true) {
            boolean changed = false;
            boolean contained = false;

            Iterator<Line> iterator = this.iterator();

            while (iterator.hasNext()) {
                Line nextLine = iterator.next();

                if (nextLine.getType().equals(toAdd.getType()) && nextLine.contains(toAdd)) {
                    nextLine.addArtifacts(toAdd);

                    contained = true;

                    break;
                } else if (nextLine.getType().equals(toAdd.getType()) && toAdd.contains(nextLine)) {
                    toAdd.addArtifacts(nextLine);

                    iterator.remove();
                } else if (nextLine.getType().equals(toAdd.getType()) && toAdd.overlaps(nextLine)) {
                    toAdd = toAdd.getCombinedLine(nextLine);

                    iterator.remove();

                    if (!changed) {
                        changed = true;
                    }
                }
            }

            if (!contained) {
                super.add(toAdd);
            } else {
                return false;
            }

            if (!changed) break;
        }

        return true;
    }

}
