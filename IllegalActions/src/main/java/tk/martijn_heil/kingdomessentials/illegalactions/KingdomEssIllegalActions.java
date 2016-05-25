package tk.martijn_heil.kingdomessentials.illegalactions;


import lombok.Getter;
import org.bukkit.Bukkit;
import tk.martijn_heil.kingdomessentials.illegalactions.listeners.EntityListener;
import tk.martijn_heil.kingdomessentials.illegalactions.listeners.InventoryListener;
import tk.martijn_heil.kingdomessentials.illegalactions.listeners.PlayerListener;
import tk.martijn_heil.nincore.api.Core;

import java.util.Locale;
import java.util.ResourceBundle;

public class KingdomEssIllegalActions extends Core
{
    @Getter
    private static KingdomEssIllegalActions instance;


    public KingdomEssIllegalActions()
    {
        instance = this;
    }


    @Override
    public void onEnableInner()
    {
        this.getNinLogger().info("Registering listeners..");
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new EntityListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }


    public static ResourceBundle getMessages(Locale inLocale)
    {
        return ResourceBundle.getBundle("tk.martijn_heil.kingdomessentials.illegalactions.res", inLocale);
    }
}
