package nl.grum.bukkit.dev;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.EntityItem;
import net.minecraft.server.ItemStack;
import net.minecraft.server.Packet15Place;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.world.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Miscellaneous test commands
 *
 * @author Grum
 */
public class TestBukkit extends JavaPlugin {
    public void onEnable() {
        registerEvents();
    }

    public void onDisable() {}

    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof CraftPlayer)) return false;
        CraftPlayer player = (CraftPlayer) sender;

        Location loc = player.getLocation();

        String commandName = command.getName().toLowerCase();

        if (commandName.equals("test")) {
            /*
            player.sendMessage("\u00A7alooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong");
            player.sendRawMessage("\u00A7alooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong");
            player.sendMessage("\u00A7alooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong\u00A7");
            player.sendRawMessage("\u00A7alooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong\u00A7");
            */

            /*
             */

            // Block Test
            PluginManager pm = getServer().getPluginManager();

            int x = loc.getBlockX() + 1;
            int y = loc.getBlockY();
            int z = loc.getBlockZ();

            Block block = player.getWorld().getBlockAt(x, y, z);

            player.teleportTo( new Location(player.getWorld(), x-0.5, (double) y, z+0.5, -90f, 40f));

            for (int i = 1; i < 256; i++) {
                Material material = Material.getMaterial(i);
                if (material == null) continue;

                block.setType(Material.AIR);
                putBlock( player, x, y, z, material.getId());
            }

            return true;
        }
        return false;
    }

    public void putBlock( CraftPlayer player, int x, int y, int z, int type ) {
        Packet15Place packet = new Packet15Place();
        packet.a = x;
        packet.b = y-1;
        packet.c = z;
        packet.d = 1;
        packet.e = new ItemStack( type, 1, 0);
        player.sendMessage( "Created packet: "+ packet );

        // Add the item to the hand of the player so it *can* be put down
        player.sendMessage( "Handing player the item with id "+type );
        player.setItemInHand( new org.bukkit.craftbukkit.inventory.CraftItemStack( packet.e.a(1) ));

        // Put down the item
        player.sendMessage( "Injecting packet " );
        player.getHandle().a.a(packet);
    }

    public static String myToString(Object object) {
        if (object == null) return "null";
        if (object instanceof Event) {
            if (object instanceof BlockPhysicsEvent) {
                BlockPhysicsEvent event = (BlockPhysicsEvent) object;
                return String.format("BlockPhysics{block=%s}", myToString(event.getBlock()));
            } else if (object instanceof BlockFromToEvent) {
                BlockFromToEvent event = (BlockFromToEvent) object;
                Block block = event.getBlock();
                String fromBlock = String.format("Block{coords=[%d,%d,%d],type=%s}", block.getX(), block.getY(), block.getZ(), event.getType());
                return String.format("BlockFromTo{from=%s,to=%s}", fromBlock, myToString(event.getToBlock()));
            } else if (object instanceof BlockPlaceEvent) {
                BlockPlaceEvent event = (BlockPlaceEvent) object;
                String loc = String.format(
                    "[%d,%d,%d]",
                    event.getBlockAgainst().getLocation().getBlockX(),
                    event.getBlockAgainst().getLocation().getBlockY(),
                    event.getBlockAgainst().getLocation().getBlockZ()
                );
                return String.format("BlockPlace{player=%s,clicked=%s,old=%s,placed=%s}", myToString(event.getPlayer()), loc, myToString(event.getBlockReplacedState().getType()), myToString(event.getBlock()));
            } else if (object instanceof BlockCanBuildEvent) {
                BlockCanBuildEvent event = (BlockCanBuildEvent) object;
                return String.format("BlockCanBuild{block=%s,new-material=%s}", myToString(event.getBlock()), myToString(event.getMaterial()));
            } else if (object instanceof EntityDeathEvent) {
                EntityDeathEvent event = (EntityDeathEvent) object;
                return String.format("EntityDeath{entity=%s,drops=%s}", myToString(event.getEntity()), myToString(event.getDrops()));
            } else if (object instanceof EntityCombustEvent) {
                EntityCombustEvent event = (EntityCombustEvent) object;
                return String.format("EntityCombust{entity=%s}", myToString(event.getEntity()));
            } else if (object instanceof EntityTargetEvent) {
                EntityTargetEvent event = (EntityTargetEvent) object;
                return String.format("EntityTarget{entity=%s,target=%d,reason=%s}", myToString(event.getEntity()), myToString(event.getTarget()), event.getReason());
            } else if (object instanceof EntityDamageEvent) {
                EntityDamageEvent event = (EntityDamageEvent) object;
                return String.format("EntityDamage{entity=%s,damage=%d,cause=%s}", myToString(event.getEntity()), event.getDamage(), event.getCause());
            } else if (object instanceof BlockRedstoneEvent) {
                BlockRedstoneEvent event = (BlockRedstoneEvent) object;
                return String.format("BlockRedstone{block=%s,from=%d,to=%d}", myToString(event.getBlock()), event.getOldCurrent(), event.getNewCurrent());
            } else if (object instanceof PlayerInteractEvent) {
                PlayerInteractEvent event = (PlayerInteractEvent) object;
                return String.format("PlayerInteract{player=%s,action=%s,block=%s,item=%s}", myToString(event.getPlayer()), myToString(event.getAction()), myToString(event.getClickedBlock()), myToString(event.getItem()));
            } else if (object instanceof PlayerItemEvent) {
                PlayerItemEvent event = (PlayerItemEvent) object;
                return String.format("PlayerItem{player=%s,item=%s,block=%s,face=%s}", myToString(event.getPlayer()), myToString(event.getItem()), myToString(event.getBlockClicked()), event.getBlockFace());
            } else if (object instanceof PlayerPickupItemEvent) {
                PlayerPickupItemEvent event = (PlayerPickupItemEvent) object;
                return String.format("PlayerPickupItem{player=%s,item=%s}", myToString(event.getPlayer()), myToString(event.getItem()));
            } else if (object instanceof PlayerItemEvent) {
                PlayerItemEvent event = (PlayerItemEvent) object;
                return String.format("PlayerItem{player=%s,block=%s,face=%s}", myToString(event.getPlayer()), myToString(event.getBlockClicked()), myToString(event.getBlockFace()));
            } else if (object instanceof PlayerDropItemEvent) {
                PlayerDropItemEvent event = (PlayerDropItemEvent) object;
                return String.format("PlayerDropItem{player=%s,item=%s}", myToString(event.getPlayer()), myToString(event.getItemDrop()));
            } else if (object instanceof PlayerBucketFillEvent) {
                PlayerBucketFillEvent event = (PlayerBucketFillEvent) object;
                return String.format("PlayerBucketFill{player=%s,bucket=%s,item=%s,target=%s,face=%s}", myToString(event.getPlayer()), event.getBucket(), myToString(event.getItemStack()), myToString(event.getBlockClicked()), event.getBlockFace());
            } else if (object instanceof PlayerBucketEmptyEvent) {
                PlayerBucketEmptyEvent event = (PlayerBucketEmptyEvent) object;
                return String.format("PlayerBucketEmpty{player=%s,bucket=%s,item=%s,target=%s,face=%s}", myToString(event.getPlayer()), event.getBucket(), myToString(event.getItemStack()), myToString(event.getBlockClicked()), event.getBlockFace());
            } else if (object instanceof BlockDamageEvent) {
                BlockDamageEvent event = (BlockDamageEvent) object;
                return String.format("BlockDamage{player=%s,block=%s,level=%s}", myToString(event.getPlayer()), myToString(event.getBlock()), event.getDamageLevel());
            } else if (object instanceof PlayerAnimationEvent) {
                PlayerAnimationEvent event = (PlayerAnimationEvent) object;
                return String.format("PlayerAnimation{player=%s,animation=%s}", myToString(event.getPlayer()), event.getAnimationType());
            } else if (object instanceof PlayerMoveEvent) {
                PlayerMoveEvent event = (PlayerMoveEvent) object;
                Location to = event.getTo();
                Location from = event.getFrom();
                return String.format("PlayerMove{from=[%.3f,%.3f,%.3f],to=[%.3f,%.3f,%.3f]}", from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ());
            }
        } else if (object instanceof CraftItem) {
            CraftItem item = (CraftItem) object;
            return String.format("Item{name=%s}", myToString( new CraftItemStack( ((EntityItem)item.getHandle()).a)));
        } else if (object instanceof Player) {
            Player player = (Player) object;
            return String.format("Player{name=%s}", player.getName());
        } else if (object instanceof Block) {
            Block block = (Block) object;
            return String.format("Block{coords=[%d,%d,%d],type=%s,data=%d}", block.getX(), block.getY(), block.getZ(), myToString(block.getType()), block.getData());
        }
        return "" + object;
    }

    private List<Event> list = new ArrayList<Event>(); 
    public void recordEvent(Event event) {
        System.out.println("recorded event: "+ myToString(event));
        //list.add( event );
    }
    
    private void registerEvents() {
        TestBlockListener block = new TestBlockListener(this);
        TestEntityListener entity = new TestEntityListener(this);
        TestPlayerListener player = new TestPlayerListener(this);
        TestServerListener server = new TestServerListener(this);
        TestVehicleListener vehicle = new TestVehicleListener(this);
        TestWorldListener world = new TestWorldListener(this);

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvent( Event.Type.PLAYER_JOIN, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_QUIT, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_RESPAWN, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_KICK, player, Event.Priority.Monitor, this);
        //pm.registerEvent( Event.Type.PLAYER_COMMAND, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_CHAT, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_MOVE, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_TELEPORT, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_INTERACT, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_LOGIN, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_EGG_THROW, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_ANIMATION, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.INVENTORY_OPEN, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_ITEM_HELD, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_DROP_ITEM, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_PICKUP_ITEM, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_TOGGLE_SNEAK, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_BUCKET_EMPTY, player, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLAYER_BUCKET_FILL, player, Event.Priority.Monitor, this);

        // pm.registerEvent( Event.Type.BLOCK_PHYSICS, block, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.BLOCK_CANBUILD, block, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.BLOCK_PLACE, block, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.BLOCK_DAMAGE, block, Event.Priority.Monitor, this);
        //pm.registerEvent( Event.Type.BLOCK_FLOW, block, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.LEAVES_DECAY, block, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.SIGN_CHANGE, block, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.BLOCK_IGNITE, block, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.REDSTONE_CHANGE, block, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.BLOCK_BURN, block, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.BLOCK_BREAK, block, Event.Priority.Monitor, this);

        pm.registerEvent( Event.Type.PLUGIN_ENABLE, server, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.PLUGIN_DISABLE, server, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.SERVER_COMMAND, server, Event.Priority.Monitor, this);

        pm.registerEvent( Event.Type.CHUNK_LOAD, world, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.CHUNK_UNLOAD, world, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.WORLD_SAVE, world, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.WORLD_LOAD, world, Event.Priority.Monitor, this);

        //pm.registerEvent( Event.Type.ENTITY_DAMAGEDBY_BLOCK, entity, Event.Priority.Monitor, this);
        //pm.registerEvent( Event.Type.ENTITY_DAMAGEDBY_ENTITY, entity, Event.Priority.Monitor, this);
        //pm.registerEvent( Event.Type.ENTITY_DAMAGEDBY_PROJECTILE, entity, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.ENTITY_DAMAGE, entity, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.ENTITY_DEATH, entity, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.ENTITY_COMBUST, entity, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.ENTITY_EXPLODE, entity, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.EXPLOSION_PRIME, entity, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.ENTITY_TARGET, entity, Event.Priority.Monitor, this);

        pm.registerEvent( Event.Type.VEHICLE_CREATE, vehicle, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.VEHICLE_DAMAGE, vehicle, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.VEHICLE_COLLISION_BLOCK, vehicle, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.VEHICLE_COLLISION_ENTITY, vehicle, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.VEHICLE_ENTER, vehicle, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.VEHICLE_EXIT, vehicle, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.VEHICLE_MOVE, vehicle, Event.Priority.Monitor, this);
        pm.registerEvent( Event.Type.VEHICLE_UPDATE, vehicle, Event.Priority.Monitor, this);
    }
}

class TestBlockListener extends BlockListener {
    private TestBukkit plugin;

    public TestBlockListener( TestBukkit plugin ) {
        this.plugin = plugin;
    }

    @Override public void onBlockDamage(BlockDamageEvent event) { plugin.recordEvent(event); }
    @Override public void onBlockCanBuild(BlockCanBuildEvent event) { plugin.recordEvent(event); }
    @Override public void onBlockFlow(BlockFromToEvent event) { plugin.recordEvent(event); }
    @Override public void onBlockIgnite(BlockIgniteEvent event) { plugin.recordEvent(event); }
    @Override public void onBlockPhysics(BlockPhysicsEvent event) { plugin.recordEvent(event); }
    @Override public void onBlockPlace(BlockPlaceEvent event) { plugin.recordEvent(event); }
    @Override public void onBlockRedstoneChange(BlockRedstoneEvent event) { plugin.recordEvent(event); }
    @Override public void onLeavesDecay(LeavesDecayEvent event) { plugin.recordEvent(event); }
    @Override public void onSignChange(SignChangeEvent event) { plugin.recordEvent(event); }
    @Override public void onBlockBurn(BlockBurnEvent event) { plugin.recordEvent(event); }
    @Override public void onBlockBreak(BlockBreakEvent event) { plugin.recordEvent(event); }
}

class TestEntityListener extends EntityListener {
    private TestBukkit plugin;

    public TestEntityListener( TestBukkit plugin ) {
        this.plugin = plugin;
    }

    @Override public void onEntityCombust(EntityCombustEvent event) { plugin.recordEvent(event); }
    @Override public void onEntityDamage(EntityDamageEvent event) { plugin.recordEvent(event); }
    @Override public void onEntityExplode(EntityExplodeEvent event) { plugin.recordEvent(event); }
    @Override public void onExplosionPrime(ExplosionPrimeEvent event) { plugin.recordEvent(event); }
    @Override public void onEntityDeath(EntityDeathEvent event) { plugin.recordEvent(event); }
    @Override public void onEntityTarget(EntityTargetEvent event) { plugin.recordEvent(event); }
}

class TestPlayerListener extends PlayerListener {

    private TestBukkit plugin;

    public TestPlayerListener( TestBukkit plugin ) {
        this.plugin = plugin;
    }

    @Override public void onPlayerBucketFill(PlayerBucketFillEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerJoin(PlayerEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerQuit(PlayerEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerKick(PlayerKickEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerChat(PlayerChatEvent event) { plugin.recordEvent(event); }
    //@Override public void onPlayerCommand(PlayerChatEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerMove(PlayerMoveEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerTeleport(PlayerMoveEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerRespawn(PlayerRespawnEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerInteract(PlayerInteractEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerLogin(PlayerLoginEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerEggThrow(PlayerEggThrowEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerAnimation(PlayerAnimationEvent event) { plugin.recordEvent(event); }
    @Override public void onInventoryOpen(PlayerInventoryEvent event) { plugin.recordEvent(event); }
    @Override public void onItemHeldChange(PlayerItemHeldEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerDropItem(PlayerDropItemEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerPickupItem(PlayerPickupItemEvent event) { plugin.recordEvent(event); }
    @Override public void onPlayerToggleSneak(PlayerToggleSneakEvent event) { plugin.recordEvent(event); }
}

class TestServerListener extends ServerListener {
    private TestBukkit plugin;

    public TestServerListener( TestBukkit plugin ) {
        this.plugin = plugin;
    }

    @Override public void onPluginEnable(PluginEvent event) { plugin.recordEvent(event); }
    @Override public void onPluginDisable(PluginEvent event) { plugin.recordEvent(event); }
    @Override public void onServerCommand(ServerCommandEvent event) { plugin.recordEvent(event); }
}

class TestVehicleListener extends VehicleListener {
    private TestBukkit plugin;

    public TestVehicleListener( TestBukkit plugin ) {
        this.plugin = plugin;
    }

    @Override public void onVehicleCreate(VehicleCreateEvent event) { plugin.recordEvent(event); }
    @Override public void onVehicleDamage(VehicleDamageEvent event) { plugin.recordEvent(event); }
    @Override public void onVehicleBlockCollision(VehicleBlockCollisionEvent event) { plugin.recordEvent(event); }
    @Override public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) { plugin.recordEvent(event); }
    @Override public void onVehicleEnter(VehicleEnterEvent event) { plugin.recordEvent(event); }
    @Override public void onVehicleExit(VehicleExitEvent event) { plugin.recordEvent(event); }
    @Override public void onVehicleMove(VehicleMoveEvent event) { plugin.recordEvent(event); }
    @Override public void onVehicleUpdate(VehicleEvent event) { plugin.recordEvent(event); }
}

class TestWorldListener extends WorldListener {
    private TestBukkit plugin;

    public TestWorldListener( TestBukkit plugin ) {
        this.plugin = plugin;
    }

    @Override public void onChunkLoad(ChunkLoadEvent event) { plugin.recordEvent(event); }
    @Override public void onChunkUnload(ChunkUnloadEvent event) { plugin.recordEvent(event); }
    @Override public void onWorldSave(WorldEvent event) { plugin.recordEvent(event); }
    @Override public void onWorldLoad(WorldEvent event) { plugin.recordEvent(event); }
}
