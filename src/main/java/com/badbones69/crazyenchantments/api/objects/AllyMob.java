package com.badbones69.crazyenchantments.api.objects;

import com.badbones69.crazyenchantments.api.CrazyManager;
import com.badbones69.crazyenchantments.api.PluginSupport;
import com.badbones69.crazyenchantments.api.enums.Messages;
import com.badbones69.crazyenchantments.api.managers.AllyManager;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import java.util.HashMap;

public class AllyMob {
    
    private final AllyType type;
    private final Player owner;
    private LivingEntity ally;
    private long spawnTime;
    private BukkitTask runnable;
    private final CrazyManager ce = CrazyManager.getInstance();
    private final PluginSupport pluginSupport = PluginSupport.INSTANCE;
    private final AllyMob instance;
    private final static AllyManager allyManager = AllyManager.getInstance();
    
    public AllyMob(Player owner, AllyType type) {
        this.type = type;
        this.owner = owner;
        instance = this;
    }
    
    public AllyType getType() {
        return type;
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public LivingEntity getAlly() {
        return ally;
    }
    
    public void spawnAlly(long spawnTime) {
        spawnAlly(owner.getLocation(), spawnTime);
    }
    
    public void spawnAlly(Location location, long spawnTime) {
        this.spawnTime = spawnTime;
        ally = (LivingEntity) location.getWorld().spawnEntity(location, type.entityType);

        // support.noStack(ally);

        ally.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(type.maxHealth);

        ally.setHealth(type.maxHealth);
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("%Player%", owner.getName());
        placeholders.put("%Mob%", type.entityType.getName());
        ally.setCustomName(Messages.replacePlaceholders(placeholders, type.getName()));
        ally.setCustomNameVisible(true);
        startSpawnTimer();
        allyManager.addAllyMob(instance);
    }
    
    public void forceRemoveAlly() {
        runnable.cancel();
        allyManager.removeAllyMob(instance);
        ally.remove();
    }
    
    public void attackEnemy(LivingEntity enemy) {
        switch (ally.getType()) {
            case WOLF -> {
                Wolf wolf = (Wolf) ally;
                wolf.setTarget(enemy);
            }
            case IRON_GOLEM -> {
                IronGolem iron = (IronGolem) ally;
                iron.setTarget(enemy);
            }
            case ZOMBIE -> {
                Zombie zom = (Zombie) ally;
                zom.setTarget(enemy);
            }
            case ENDERMITE -> {
                Endermite mite = (Endermite) ally;
                mite.setTarget(enemy);
            }
            case SILVERFISH -> {
                Silverfish sfish = (Silverfish) ally;
                sfish.setTarget(enemy);
            }
            case BEE -> {
                Bee bee = (Bee) ally;
                bee.setTarget(enemy);
            }
        }
    }
    
    private void startSpawnTimer() {
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                allyManager.removeAllyMob(instance);
                ally.remove();
            }
        }.runTaskLater(ce.getPlugin(), spawnTime * 20);
    }
    
    public enum AllyType {
        
        WOLF("Wolf", "&b%player%'s Saberwolf", EntityType.WOLF, 16),
        IRON_GOLEM("Iron-Golem", "&6%player%'s Golem", EntityType.IRON_GOLEM, 200),
        ZOMBIE("Zombie", "&2%player%'s Undead", EntityType.ZOMBIE, 45),
        ENDERMITE("Endermite", "&5%player%'s Endermite", EntityType.ENDERMITE, 10),
        SILVERFISH("Silverfish", "&7%player%'s Silverfish", EntityType.SILVERFISH, 10),
        BEE("Bee", "&e%player%'s Bee", EntityType.BEE, 10);
        
        private final String configName;
        private final String defaultName;
        private final EntityType entityType;
        private final int maxHealth;
        
        AllyType(String configName, String defaultName, EntityType entityType, int maxHealth) {
            this.configName = configName;
            this.defaultName = defaultName;
            this.entityType = entityType;
            this.maxHealth = maxHealth;
        }
        
        public String getConfigName() {
            return configName;
        }
        
        public String getDefaultName() {
            return defaultName;
        }
        
        public String getName() {
            return allyManager.getAllyTypeNameCache().get(this);
        }
        
        public EntityType getEntityType() {
            return entityType;
        }
        
        public int getMaxHealth() {
            return maxHealth;
        }
    }
}