package net.pl3x.bukkit.eggdrop.listener;

import net.pl3x.bukkit.eggdrop.ItemUtil;
import net.pl3x.bukkit.eggdrop.Logger;
import net.pl3x.bukkit.eggdrop.configuration.Config;
import net.pl3x.bukkit.eggdrop.nms.NBTHandler;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;

public class BukkitListener implements Listener {
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) {
            return; // we dont care if player dies
        }

        if (!NBTHandler.hasAI(entity)) {
            return; // entity does not have AI. ignore
        }

        Player player = entity.getKiller();
        if (player == null) {
            return; // not killed by a player
        }

        if (ItemUtil.equals(player.getInventory().getItemInOffHand(), Config.EGG_TOTEM) &&
                ItemUtil.equals(player.getInventory().getItemInMainHand(), Config.EGG_TOTEM)) {
            return; // not holding the egg totem
        }

        // take the egg totem
        if (!ItemUtil.takeItem(player, Config.EGG_TOTEM)) {
            Logger.warn("Unable to remove egg totem from " + player.getName() + "'s hand. Not dropping " + entity.getType() + " spawn egg.");
            return; // cant remove egg totem from hand
        }

        // Make the egg
        ItemStack eggItem = new ItemStack(Material.MONSTER_EGG, 1);
        SpawnEggMeta meta = (SpawnEggMeta) eggItem.getItemMeta();
        meta.setSpawnedType(entity.getType());
        eggItem.setItemMeta(meta);

        // clear the drops and exp
        event.getDrops().clear();
        event.setDroppedExp(0);

        // add the egg to drops
        event.getDrops().add(eggItem);
    }
}