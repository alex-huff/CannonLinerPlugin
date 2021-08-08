package phonis.cannonliner;

import org.bukkit.plugin.java.JavaPlugin;
import phonis.cannonliner.listeners.BlockBreakEvent;
import phonis.cannonliner.listeners.ChunkUnloadEvent;
import phonis.cannonliner.listeners.ExplosionEvent;
import phonis.cannonliner.listeners.SandBlockEvent;
import phonis.cannonliner.networking.CannonLinerServer;
import phonis.cannonliner.tasks.Tick;

public class CannonLiner extends JavaPlugin {

    public static CannonLiner instance;

    private CannonLinerServer cannonLinerServer;

    @Override
    public void onEnable() {
        CannonLiner.instance = this;
        Tick tick = new Tick(this);
        this.cannonLinerServer = new CannonLinerServer();

        new ExplosionEvent(this, tick);
        new SandBlockEvent(this, tick);
        new BlockBreakEvent(this);
        new ChunkUnloadEvent(this);

        tick.start();
        this.cannonLinerServer.start();
    }

    @Override
    public void onDisable() {
        this.cannonLinerServer.close();
    }

}
