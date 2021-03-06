package tk.martijn_heil.kingdomessentials.illegalactions.listeners;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import tk.martijn_heil.kingdomessentials.illegalactions.ModIllegalActions;
import tk.martijn_heil.kingdomessentials.item.util.ItemStacks;
import tk.martijn_heil.nincore.api.entity.NinOnlinePlayer;

import java.util.ArrayList;
import java.util.List;

public class PlayerListener implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST) // If player tries to shoot with a non-soulbound bow.
    public void onEntityShootBow(EntityShootBowEvent e)
    {
        if (ModIllegalActions.getInstance().getConfig().getBoolean("soulbound.preventNonSoulboundWeaponUsage") &&
                e.getEntity() instanceof Player &&
                !ItemStacks.isSoulBound(e.getBow()))
        {
            Player player = (Player) e.getEntity();

            e.setCancelled(true);

            // Update player inventory so that the client doesn't think the arrow was used.
            player.updateInventory();

            NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(player);
            np.sendError(ModIllegalActions.getMessages(np.getLocale()).getString("error.event.cancelled.generic.shootBow"));
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e)
    {
        if (ModIllegalActions.getInstance().getConfig().getBoolean("soulbound.preventNonSoulboundWeaponUsage") && e.getDamager() instanceof Player)
        {
            Player player = (Player) e.getDamager();

            // List of weapons
            List<Material> weapons = new ArrayList<>();

            weapons.add(Material.WOOD_SWORD);
            weapons.add(Material.STONE_SWORD);
            weapons.add(Material.GOLD_SWORD);
            weapons.add(Material.IRON_SWORD);
            weapons.add(Material.DIAMOND_SWORD);


            if (player.getInventory().getItemInMainHand() != null && weapons.contains(player.getInventory().getItemInMainHand().getType()) &&
                    !ItemStacks.isSoulBound(player.getInventory().getItemInMainHand()))
            {
                e.setCancelled(true);

                NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(player);
                np.sendError(ModIllegalActions.getMessages(np.getLocale()).getString("error.event.cancelled.item.combat"));
            }
        }


        if (ModIllegalActions.getInstance().getConfig().getBoolean("soulbound.preventNonSoulboundAxeUsage") && e.getDamager() instanceof Player)
        {
            Player player = (Player) e.getDamager();

            // List of weapons
            List<Material> weapons = new ArrayList<>();

            weapons.add(Material.WOOD_AXE);
            weapons.add(Material.STONE_AXE);
            weapons.add(Material.GOLD_AXE);
            weapons.add(Material.IRON_AXE);
            weapons.add(Material.DIAMOND_AXE);


            ItemStack itemInMainHand = player.getInventory().getItemInMainHand();

            if (itemInMainHand != null && weapons.contains(itemInMainHand.getType()) &&
                    !ItemStacks.isSoulBound(itemInMainHand))
            {
                e.setCancelled(true);

                NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(player);
                np.sendError(ModIllegalActions.getMessages(np.getLocale()).getString("error.event.cancelled.item.combat"));
            }
        }

    }


    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent e)
    {
        if (e.isGliding() && e.getEntity() instanceof Player && ModIllegalActions.getInstance().getConfig().getBoolean("movement.preventElytra") &&
                !e.getEntity().hasPermission("kingdomkits.bypass.elytra"))
        {
            e.setCancelled(true);

            NinOnlinePlayer np = NinOnlinePlayer.fromPlayer((Player) e.getEntity());
            np.sendError(ModIllegalActions.getMessages(np.getLocale()).getString("error.event.cancelled.movement.glide"));
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent e)
    {
        List<String> items = ModIllegalActions.getInstance().getConfig().getStringList("crafting.blacklistedItems");

        if (e.getCurrentItem() != null && e.getInventory() != null)
        {

            // If clicked inventory is a WORKBENCH or CRAFTING inventory
            if (e.getInventory().getType().equals(InventoryType.WORKBENCH) ||
                    e.getInventory().getType().equals(InventoryType.CRAFTING))
            {
                if (e.getSlotType().equals(InventoryType.SlotType.RESULT) &&
                        items.contains(e.getCurrentItem().getType().toString()))
                {
                    e.setCancelled(true);
                    NinOnlinePlayer np = NinOnlinePlayer.fromPlayer((Player) e.getWhoClicked());
                    np.sendError(ModIllegalActions.getMessages(np.getLocale()).getString("error.event.cancelled.item.craft"));
                }
            }
        }

        List<String> items2 = ModIllegalActions.getInstance().getConfig().getStringList("enchanting.blacklistedItems");

        if (e.getCurrentItem() != null && e.getInventory() != null)
        {

            // If clicked inventory is an anvil
            if (e.getInventory().getType().equals(InventoryType.ANVIL))
            {
                // Foreach blacklisted item list
                for (String item : items2)
                {
                    // Cast material string to material object.
                    Material itemMaterial = Material.getMaterial(item);

                    if (itemMaterial != null && e.getInventory().contains(itemMaterial) &&
                            e.getSlotType().equals(InventoryType.SlotType.RESULT))
                    {
                        // If clicked inventory is an anvil && anvil contains enchanted book + a blacklisted item, cancel the event..
                        e.setCancelled(true);

                        NinOnlinePlayer np = NinOnlinePlayer.fromPlayer((Player) e.getWhoClicked());
                        np.sendError(ModIllegalActions.getMessages(np.getLocale()).getString("error.event.cancelled.block.use.anvil"));
                    }
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerUseItem(PlayerInteractEvent e)
    {
        if (e.getItem() != null)
        {
            if (ModIllegalActions.getInstance().getConfig().getList("usage.blacklistedItems").contains(e.getItem().getType().toString()) &&
                    (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)))
            {
                e.setCancelled(true);
                e.getPlayer().updateInventory();

                NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getPlayer());
                np.sendError(ModIllegalActions.getMessages(np.getLocale()).getString("error.event.cancelled.item.use"));
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerItemConsume(PlayerItemConsumeEvent e)
    {
        if (ModIllegalActions.getInstance().getConfig().getList("consume.blacklistedItems").contains(e.getItem().getType().toString())
                && !ItemStacks.isConsumeAllowed(e.getItem()))
        {
            e.setCancelled(true);

            NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getPlayer());
            np.sendError(ModIllegalActions.getMessages(np.getLocale()).getString("error.event.cancelled.item.consume"));
        }

        if (ModIllegalActions.getInstance().getConfig().getBoolean("potions.disablePotions"))
        {
            if (e.getItem().getType().equals(Material.POTION))
            {
                PotionMeta potion = (PotionMeta) e.getItem().getItemMeta();

                if (potion.getBasePotionData().getType() != PotionType.WATER)
                {
                    e.setCancelled(true);

                    NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getPlayer());
                    np.sendError(ModIllegalActions.getMessages(np.getLocale()).getString("error.event.cancelled.potion.drink"));
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST) // Prevent player enchanting soulbound items.
    public void onEnchantItem(EnchantItemEvent e)
    {
        List<String> items = ModIllegalActions.getInstance().getConfig().getStringList("enchanting.blacklistedItems");

        ItemStack enchantedItem = e.getItem();

        Material enchantedItemMaterial = enchantedItem.getType();

        if (items.contains(enchantedItemMaterial.toString()))
        {
            // Cancel enchant event if the item is blacklisted
            e.setCancelled(true);

            NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getEnchanter());
            np.sendError(ModIllegalActions.getMessages(np.getLocale()).getString("error.event.cancelled.item.enchant"));
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockDispenseEvent(BlockDispenseEvent e)
    {
        if (ModIllegalActions.getInstance().getConfig().getBoolean("potions.disablePotions"))
        {
            if (e.getItem().getType().equals(Material.POTION))
            {
                e.setCancelled(true);
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if (ModIllegalActions.getInstance().getConfig().getBoolean("potions.disablePotions"))
        {
            if (e.getItem() != null && e.getItem().getType().equals(Material.POTION))
            {
                Potion potion = Potion.fromItemStack(e.getItem());

                if (potion.getLevel() != 0 && potion.isSplash())
                {
                    e.setCancelled(true);
                    e.getPlayer().updateInventory();

                    NinOnlinePlayer np = NinOnlinePlayer.fromPlayer(e.getPlayer());
                    np.sendError(ModIllegalActions.getMessages(np.getLocale()).getString("error.event.cancelled.potion.throw"));
                }
            }
        }
    }
}
