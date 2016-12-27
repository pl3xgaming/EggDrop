package net.pl3x.bukkit.eggdrop.nms;

import net.minecraft.server.v1_11_R1.ChatComponentText;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.MojangsonParseException;
import net.minecraft.server.v1_11_R1.MojangsonParser;
import net.pl3x.bukkit.eggdrop.Logger;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class NBTHandler {
    public static ItemStack setItemNBT(ItemStack bukkitItem, String nbt, String path) {
        if (nbt == null || nbt.isEmpty()) {
            return bukkitItem; // nothing to parse
        }

        net.minecraft.server.v1_11_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(bukkitItem);
        try {
            nmsItem.setTag(MojangsonParser.parse(parseNBT(nbt.split(" ")).toPlainText()));
        } catch (MojangsonParseException e) {
            Logger.error("Error parsing NBT: " + path + ".nbt");
            e.printStackTrace();
        }

        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    private static IChatBaseComponent parseNBT(String[] nbt) {
        ChatComponentText component = new ChatComponentText("");
        for (int i = 0; i < nbt.length; i++) {
            if (i > 0) {
                component.a(" ");
            }
            component.addSibling(new ChatComponentText(nbt[i]));
        }
        return component;
    }

    public static boolean hasAI(LivingEntity entity) {
        try {
            Class.forName("org.spigotmc.SpigotConfig");
            return !(((CraftLivingEntity) entity).getHandle().fromMobSpawner || !entity.hasAI());
        } catch (ClassNotFoundException e) {
            return entity.hasAI();
        }
    }
}