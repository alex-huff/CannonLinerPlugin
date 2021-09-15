package dev.phonis.cannonliner.tasks;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.registry.LegacyWorldData;
import dev.phonis.cannonliner.networking.*;
import dev.phonis.cannonliner.trace.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import dev.phonis.cannonliner.CannonLiner;

import java.util.*;
import java.util.function.Consumer;

public class Tick implements Runnable {

    public static CuboidRegion currentCannon = null;
    public static Set<Vector2D> currentChunks = null;
    public static Consumer<CTPacket> packetConsumer = null;

    private final Set<LocationChange> changes = new HashSet<>();
    public final Map<Integer, EntityLocation> locations = new HashMap<>();
    private final CannonLiner cannonLiner;

    public Tick(CannonLiner cannonLiner) {
        this.cannonLiner = cannonLiner;
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
            }
        } else {
            el = new EntityLocation(loc, true, entity.getType());
        }

        this.locations.put(entity.getEntityId(), el);
    }

    private void processEntities() {
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Entity entity : world.getEntitiesByClasses(FallingBlock.class, TNTPrimed.class, Player.class)) {
                this.processEntity(world, entity);
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
        Consumer<CTPacket> consumer = Tick.packetConsumer;
        Map<LineEq, LineSet> culledLines = new HashMap<>();

        if (consumer == null) {
            return;
        }

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
            consumer.accept(new CTNewLines(totalLines));
        }

        if (totalArtifacts.size() > 0) {
            consumer.accept(new CTNewArtifacts(new ArrayList<>(totalArtifacts)));
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

    public static void removeCannon() throws WorldEditException {
        World world = Bukkit.getWorld("world");
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession((com.sk89q.worldedit.world.World) new BukkitWorld(world), Integer.MAX_VALUE);
        BlockArrayClipboard bac = new BlockArrayClipboard(Tick.currentCannon);
        Operation operation = new ClipboardHolder(
            bac,
            LegacyWorldData.getInstance()
        ).createPaste(editSession, LegacyWorldData.getInstance()).to(Tick.currentCannon.getMinimumPoint()).build();

        editSession.enableQueue();
        Operations.complete(operation);
        Operations.complete(editSession.commit());
        editSession.flushQueue();
    }

}
