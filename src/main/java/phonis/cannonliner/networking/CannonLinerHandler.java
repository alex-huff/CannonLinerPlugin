package phonis.cannonliner.networking;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.LegacyWorldData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Button;
import phonis.cannonliner.CannonLiner;
import phonis.cannonliner.tasks.Tick;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;

public class CannonLinerHandler {

    private final Socket socket;

    public CannonLinerHandler(Socket socket) {
        this.socket = socket;
    }

    public void close() {
        try {
            this.socket.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void handle() {
        try {
            socket.setSoTimeout(0);

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            int x = dataInputStream.readInt();
            int y = dataInputStream.readInt();
            int z = dataInputStream.readInt();
            int length = dataInputStream.readInt();
            byte[] schemData = new byte[length];
            OutputStream outputStream = this.socket.getOutputStream();

            dataInputStream.readFully(schemData);
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                CannonLiner.instance,
                () -> {
                    try {
                        this.pasteSchematica(x, y, z, schemData, outputStream);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            );
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void pasteSchematica(int x, int y, int z, byte[] schemData, OutputStream outputStream) throws Exception {
        if (!(Bukkit.getPluginManager().getPlugin("WorldEdit") instanceof WorldEditPlugin)) {
            throw new Exception("Invalid WorldEdit.");
        }

        ClipboardFormat format = ClipboardFormat.SCHEMATIC;
        ClipboardReader reader;
        ByteArrayInputStream bais = new ByteArrayInputStream(schemData);

        try {
            reader = format.getReader(bais);
        } catch (IOException e) {
            throw new Exception("IOException during loading of schematic.");
        }

        org.bukkit.World world = Bukkit.getWorld("world");
        Clipboard clipboard;

        try {
            clipboard = reader.read(LegacyWorldData.getInstance());

            bais.close();
        } catch (IOException e) {
            throw new Exception("IOException during load to clipboard.");
        }

        if (Tick.currentCannon != null) {
            this.removeRegion(world, Tick.currentCannon);
        }

        CuboidRegion schemRegion = (CuboidRegion) clipboard.getRegion();
        Tick.currentCannon = new CuboidRegion(
            new Vector(x, y, z),
            new Vector(x + schemRegion.getWidth() - 1, y + schemRegion.getHeight() - 1, z + schemRegion.getLength() - 1)
        );

        Vector expansionVector = new Vector(32, 0, 32);
        CuboidRegion schemRegionExpanded = new CuboidRegion(Tick.currentCannon.getMinimumPoint().subtract(expansionVector), Tick.currentCannon.getMaximumPoint().add(expansionVector));
        Tick.currentChunks = schemRegionExpanded.getChunks();

        for (Vector2D chunk : Tick.currentChunks) {
            world.loadChunk(chunk.getBlockX(), chunk.getBlockZ());
        }

        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession((World) new BukkitWorld(world), Integer.MAX_VALUE);
        Operation operation = new ClipboardHolder(
            clipboard,
            LegacyWorldData.getInstance()
        ).createPaste(editSession, LegacyWorldData.getInstance()).to(new BlockVector(x, y, z)).build();

        editSession.enableQueue();
        Operations.complete(operation);
        Operations.complete(editSession.commit());
        editSession.flushQueue();

        Vector dimensions = clipboard.getDimensions();
        Vector currentBlock = new Vector();
        Vector buttonLocation = null;

        for (int blockX = 0; blockX < dimensions.getBlockX(); blockX++) {
            currentBlock = currentBlock.setX(blockX);
            for (int blockY = 0; blockY < dimensions.getBlockY(); blockY++) {
                currentBlock = currentBlock.setY(blockY);
                for (int blockZ = 0; blockZ < dimensions.getBlockZ(); blockZ++) {
                    currentBlock = currentBlock.setZ(blockZ);
                    BaseBlock block = clipboard.getBlock(currentBlock);
                    int type = block.getType();

                    if (type == 23) { // dispenser
                        org.bukkit.block.Block bukkitBlock = world.getBlockAt(x + currentBlock.getBlockX(), y + currentBlock.getBlockY(), z + currentBlock.getBlockZ());
                        org.bukkit.block.Dispenser dispenser = (org.bukkit.block.Dispenser) bukkitBlock.getState();

                        dispenser.getInventory().addItem(new ItemStack(Material.TNT, 64));
                    } else if (type == 77 || type == 143) { // stone and wood button
                        buttonLocation = currentBlock;
                    }
                }
            }
        }

        DataOutputStream socketOutputStream = new DataOutputStream(outputStream);

        Tick.packetConsumer = ctPacket -> {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);

                dos.writeByte(ctPacket.packetID());
                ctPacket.toBytes(dos);
                dos.close();

                byte[] data = baos.toByteArray();

                socketOutputStream.writeInt(data.length);
                socketOutputStream.write(data);
                socketOutputStream.flush();
            } catch (IOException e) {
                Tick.packetConsumer = null;
            }
        };

        if (buttonLocation != null) {
//            Vector finalButtonLocation = buttonLocation;
//            Bukkit.getScheduler().scheduleSyncDelayedTask(
//                CannonLiner.instance,
//                () -> this.fire(world.getBlockAt(x + finalButtonLocation.getBlockX(), y + finalButtonLocation.getBlockY(), z + finalButtonLocation.getBlockZ())),
//                100L
//            );
            this.fire(world.getBlockAt(x + buttonLocation.getBlockX(), y + buttonLocation.getBlockY(), z + buttonLocation.getBlockZ()));
        }
    }

    private void fire(Block block) {
        if (
            (
                block.getType().equals(Material.STONE_BUTTON) ||
                block.getType().equals(Material.WOOD_BUTTON)
            ) &&
            block.getState().getData().getData() <= 5
        ) {
            long tickLength;
            Button button = (Button) block.getState().getData();
            BlockState bs = block.getState();
            BlockFace face = button.getAttachedFace();
            Block blockTwo = block.getRelative(face);

            block.setType(Material.AIR);

            Material blockMaterial = blockTwo.getType();
            byte data = blockTwo.getData();

            block.setType(button.getItemType());
            button.setPowered(true);
            bs.setRawData(button.getData());
            bs.update();
            block.getState().update();
            blockTwo.setType(Material.REDSTONE_BLOCK, false);
            blockTwo.setType(blockMaterial);
            blockTwo.setData(data);

            if (block.getType().equals(Material.STONE_BUTTON)) {
                tickLength = 21L;
            } else {
                tickLength = 31L;
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(
                CannonLiner.instance,
                new Runnable() {
                    @Deprecated
                    public void run() {
                        Button button = (Button) block.getState().getData();
                        BlockState bs = block.getState();
                        BlockFace face = button.getAttachedFace();
                        Block blockTwo = block.getRelative(face);

                        block.setType(Material.AIR);

                        Material blockMaterial = blockTwo.getType();
                        byte data = blockTwo.getData();

                        block.setType(button.getItemType());
                        button.setPowered(false);
                        bs.setRawData(button.getData());
                        bs.update();
                        block.getState().update();
                        blockTwo.setType(Material.STONE, false);
                        blockTwo.setType(blockMaterial);
                        blockTwo.setData(data);
                    }
                },
                tickLength
            );
        }
    }

    public void removeRegion(org.bukkit.World world, CuboidRegion region) {
        Iterator<BlockVector> iterator = region.iterator();

        while (iterator.hasNext()) {
            BlockVector blockVector = iterator.next();

            Block block = world.getBlockAt(blockVector.getBlockX(), blockVector.getBlockY(), blockVector.getBlockZ());

            if (block.getType().equals(Material.DISPENSER)) {
                Dispenser dispenser = (Dispenser) block.getState();

                dispenser.getInventory().clear();
            }

            block.setType(Material.AIR);
        }
    }

}
