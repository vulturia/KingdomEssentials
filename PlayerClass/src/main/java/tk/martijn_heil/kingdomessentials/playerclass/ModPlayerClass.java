package tk.martijn_heil.kingdomessentials.playerclass;


import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tk.martijn_heil.kingdomessentials.playerclass.hooks.FactionsHook;
import tk.martijn_heil.kingdomessentials.playerclass.hooks.PlaceHolderApiHook;
import tk.martijn_heil.nincore.api.Core;

import java.util.Locale;
import java.util.ResourceBundle;

import static com.google.common.base.Preconditions.checkNotNull;

public class ModPlayerClass extends Core
{
    @Getter
    private static ModPlayerClass instance;


    @Getter
    private PlaceHolderApiHook placeHolderApiHook;
    @Getter
    private FactionsHook factionsHook;


    public ModPlayerClass()
    {
        instance = this;
    }


    @Override
    public void onEnableInner()
    {
        this.placeHolderApiHook = new PlaceHolderApiHook();
        this.factionsHook = new FactionsHook();

        this.saveDefaultConfig();

        if (!this.getDataManager().dataFileExists())
        {
            this.getDataManager().createDataFile();
        }

        this.getDataManager().loadDataFile();
        this.getDataManager().scheduleAutomaticDataFileSave(6000);
    }


    public FileConfiguration getRawData()
    {
        return this.getDataManager().getData();
    }


    /**
     * Check if an item is part of a given kit. The item is known to be part of a certain kit if the lore contains
     * {@literal '§b§oKitNameHere'}
     *
     * @param item    The {@link ItemStack} to check.
     * @param kitName The name of the kit to check if this item is part of it.
     * @return true if the item is part of this kit.
     */
    public static boolean isPartOfKit(@Nullable ItemStack item, @Nullable String kitName)
    {
        return (item != null) && (item.getItemMeta().getLore() != null) && (item.getItemMeta().getLore() != null) &&
                item.getItemMeta().getLore().contains("§b§o" + kitName);
        // NOTE: §b instead of §6
    }


    public static ResourceBundle getMessages(@NotNull Locale inLocale)
    {
        checkNotNull(inLocale, "inLocale can not be null.");
        return ResourceBundle.getBundle("tk.martijn_heil.kingomessentials.playerclass.res.messages", inLocale);
    }


    public static ResourceBundle getMessages()
    {
        return ResourceBundle.getBundle("tk.martijn_heil.kingomessentials.playerclass.res.messages");
    }
}