package phonis.cannonliner.tasks;

import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.regions.CuboidRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import phonis.cannonliner.CannonLiner;
import phonis.cannonliner.networking.*;
import phonis.cannonliner.trace.*;

import java.util.*;
import java.util.function.Consumer;

public class Tick implements Runnable {

    public static CuboidRegion currentCannon = null;
    public static Set<Vector2D> currentChunks = null;
    public static Consumer<CTPacket> packetConsumer = null;

    private final Set<LocationChange> changes = new HashSet<>();
    private final Set<EntityLocation> lastTicks = new HashSet<>();
    private final Map<Integer, EntityLocation> locations = new HashMap<>();
    private final CannonLiner cannonLiner;

    public Tick(CannonLiner cannonLiner) {
        this.cannonLiner = cannonLiner;
    }

    public Set<EntityLocation> getLastTicks() {
        return this.lastTicks;
    }

    public void start() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this.cannonLiner, this, 0L, 1L);
    }

    public void addChange(LocationChange lc) {
        this.changes.add(lc);
    }

    private void processEntity(World world, Entity entity) {
        Location loc = entity.getLocation();
        int id = entity.getEntityId();
        EntityLocation el;

        if (this.locations.containsKey(id)) {
            Location old = this.locations.get(id).getLocation();

            if (!Objects.equals(old.getWorld(), loc.getWorld())) {
                el = new EntityLocation(loc, true, entity.getType());
            } else {
                LocationChange change;

                if (this.locations.get(id).getNew()) {
                    change = new LocationChange(world, old, loc, entity.getType(), ChangeType.START);
                } else {
                    change = new LocationChange(world, old, loc, entity.getType(), ChangeType.NORMAL);
                }

                if (change.getChangeType().compareTo(ChangeType.NORMAL) != 0 || old.distance(loc) != 0) {
                    this.changes.add(change);
                }

                el = new EntityLocation(loc, false, entity.getType());

                if (entity.getType().equals(EntityType.PRIMED_TNT) && entity.getTicksLived() == 80) {
                    this.lastTicks.add(el);
                }
            }
        } else {
            el = new EntityLocation(loc, true, entity.getType());
        }

        this.locations.put(entity.getEntityId(), el);
    }

    private void processEntities() {
        this.lastTicks.clear();

        for (World world : Bukkit.getServer().getWorlds()) {
            for (Entity entity : world.getEntitiesByClasses(FallingBlock.class, TNTPrimed.class, Player.class)) {
                processEntity(world, entity);
            }
        }

        Set<Integer> keys = this.locations.keySet();
        List<Integer> removeList = new ArrayList<>();

        for (int key : keys) {
            if (this.locations.get(key).getState()) {
                this.locations.get(key).kill();
            } else {
                removeList.add(key);
            }
        }

        for (int key : removeList) {
            this.locations.remove(key);
        }
    }

    private void handleTraces(List<Trace> traces) {
        if (Tick.packetConsumer == null) {
            return;
        }

        Map<LineEq, LineSet> culledLines = new HashMap<>();

        for (Trace trace : traces) {
            List<Line> lines = trace.getLines();

            for (Line line : lines) {
                if (!culledLines.containsKey(line.getLineEq())) {
                    culledLines.put(line.getLineEq(), new LineSet(true));
                }

                culledLines.get(line.getLineEq()).add(line);
            }
        }

        List<CTLine> totalLines = new ArrayList<>();
        Set<CTArtifact> totalArtifacts = new HashSet<>();

        for (LineEq lineEq : culledLines.keySet()) {
            for (Line line : culledLines.get(lineEq)) {
                totalLines.add(CTAdapter.fromLine(line));
                totalArtifacts.addAll(CTAdapter.artifactsFromLine(line));
            }
        }

        if (totalLines.size() > 0) {
            Tick.packetConsumer.accept(new CTNewLines(totalLines));
        }

        if (totalArtifacts.size() > 0) {
            Tick.packetConsumer.accept(new CTNewArtifacts(new ArrayList<>(totalArtifacts)));
        }
    }

    @Override
    public void run() {
        this.processEntities();

        List<Trace> traces = new ArrayList<>();

        for (LocationChange change : this.changes) {
            if (change.getType().equals(EntityType.PRIMED_TNT)) {
                traces.add(
                    new TNTTrace(
                        change.getStart(),
                        change.getFinish(),
                        false,
                        change.getChangeType().equals(ChangeType.END)
                    )
                );
            } else if (change.getType().equals(EntityType.FALLING_BLOCK)) {
                traces.add(
                    new SandTrace(
                        change.getStart(),
                        change.getFinish(),
                        false,
                        false
                    )
                );
            } else if (change.getType().equals(EntityType.PLAYER)) {
                traces.add(
                    new PlayerTrace(
                        change.getStart(),
                        change.getFinish()
                    )
                );
            }
        }

        this.handleTraces(traces);
        this.changes.clear();
    }

}
