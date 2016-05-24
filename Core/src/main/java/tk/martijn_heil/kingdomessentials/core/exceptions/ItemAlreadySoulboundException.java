package tk.martijn_heil.kingdomessentials.core.exceptions;

import org.bukkit.command.CommandSender;
import tk.martijn_heil.nincore.api.entity.NinCommandSender;
import tk.martijn_heil.nincore.api.exceptions.ValidationException;
import tk.martijn_heil.nincore.api.util.TranslationUtils;

import java.util.ResourceBundle;

public class ItemAlreadySoulboundException extends ValidationException
{
    public ItemAlreadySoulboundException(CommandSender target)
    {
        super(target, TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                NinCommandSender.fromCommandSender(target).getMinecraftLocale().
                        toLocale()), "commandError.itemAlreadySoulbound"), null);
    }
}
