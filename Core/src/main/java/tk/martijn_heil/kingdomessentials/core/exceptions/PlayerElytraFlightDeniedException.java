package tk.martijn_heil.kingdomessentials.core.exceptions;

import org.bukkit.command.CommandSender;
import tk.martijn_heil.nincore.api.entity.NinCommandSender;
import tk.martijn_heil.nincore.api.exceptions.ValidationException;
import tk.martijn_heil.nincore.api.util.TranslationUtils;

import java.util.ResourceBundle;


public class PlayerElytraFlightDeniedException extends ValidationException
{
    public PlayerElytraFlightDeniedException(CommandSender target)
    {
        super(target, TranslationUtils.getStaticMsg(ResourceBundle.getBundle("lang.errorMsgs",
                NinCommandSender.fromCommandSender(target).getMinecraftLocale().
                        toLocale()), "error.event.cancelled.movement.glide"), null);
    }
}
