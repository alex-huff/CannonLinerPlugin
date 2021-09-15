package dev.phonis.cannonliner;

import com.sk89q.worldedit.WorldEditException;
import dev.phonis.cannonliner.listeners.BlockBreakEvent;
import dev.phonis.cannonliner.listeners.ChunkUnloadEvent;
import dev.phonis.cannonliner.listeners.EntityChangeFormEvent;
import dev.phonis.cannonliner.tasks.Tick;
import org.bukkit.plugin.java.JavaPlugin;
import dev.phonis.cannonliner.networking.CannonLinerServer;

public class CannonLiner extends JavaPlugin {

    public static CannonLiner instance;

    private CannonLinerServer cannonLinerServer;

    @Override
    public void onEnable() {
        CannonLiner.instance = this;
        Tick tick = new Tick(this);
        this.cannonLinerServer = new CannonLinerServer();

        new EntityChangeFormEvent(this, tick);
        new BlockBreakEvent(this);
        new ChunkUnloadEvent(this);

        tick.start();
        this.cannonLinerServer.start();
    }

    @Override
    public void onDisable() {
        this.cannonLinerServer.close();

        if (Tick.currentCannon != null) {
            try {
                Tick.removeCannon();
            } catch (WorldEditException ignored) { }
        }
    }

}
