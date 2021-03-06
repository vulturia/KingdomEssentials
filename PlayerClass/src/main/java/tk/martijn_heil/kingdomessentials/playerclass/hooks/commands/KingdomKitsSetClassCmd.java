package tk.martijn_heil.kingdomessentials.playerclass.hooks.commands;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import tk.martijn_heil.kingdomessentials.playerclass.exceptions.CoolDownHasNotExpiredException;
import tk.martijn_heil.kingdomessentials.playerclass.exceptions.PlayerCannotBecomeClassException;
import tk.martijn_heil.kingdomessentials.playerclass.exceptions.PlayerClassNotFoundException;
import tk.martijn_heil.kingdomessentials.playerclass.model.COfflinePlayer;
import tk.martijn_heil.kingdomessentials.playerclass.model.COnlinePlayer;
import tk.martijn_heil.kingdomessentials.playerclass.model.PlayerClass;
import tk.martijn_heil.nincore.api.command.executors.NinSubCommandExecutor;
import tk.martijn_heil.nincore.api.exceptions.TechnicalException;
import tk.martijn_heil.nincore.api.exceptions.ValidationException;
import tk.martijn_heil.nincore.api.exceptions.validationexceptions.AccessDeniedException;
import tk.martijn_heil.nincore.api.exceptions.validationexceptions.NotEnoughArgumentsException;
import tk.martijn_heil.nincore.api.exceptions.validationexceptions.PlayerNotFoundException;

public class KingdomKitsSetClassCmd extends NinSubCommandExecutor
{
    @Override
    public void execute(CommandSender sender, String[] args) throws ValidationException, TechnicalException
    {
        // Not enough arguments
        if (args.length <= 0)
        {
            throw new NotEnoughArgumentsException(sender);
        }
        else if (args.length == 1) // Class has been given, target player has not been given.
        {
            String className = args[0];

            // If sender doesn't have the kingdomkits.setclass.[class] permission
            if (!sender.hasPermission("kingdomess.setclass." + className)) throw new AccessDeniedException(sender);


            // Class validation
            if (!PlayerClass.PlayerClassExists(className)) throw new PlayerClassNotFoundException(sender);


            // Integration with factions validation.
            if (sender instanceof Player)
            {
                COnlinePlayer ninOnlinePlayer = new COnlinePlayer(((Player) sender).getUniqueId());

                if (!ninOnlinePlayer.canBecomeClass(className))
                {
                    throw new PlayerCannotBecomeClassException(ninOnlinePlayer.toPlayer());
                }
            }


            // Player class switch cool down validation.
            if (sender instanceof Player)
            {
                COnlinePlayer ninOnlinePlayer = new COnlinePlayer(((Player) sender).getUniqueId());

                if (!ninOnlinePlayer.hasPlayerClassSwitchCoolDownExpired() &&
                        !sender.hasPermission("kingdomess.playerclass.bypass.changeclasscooldown"))
                {
                    throw new CoolDownHasNotExpiredException(sender, ninOnlinePlayer.getNextPossibleClassSwitchTime());
                }
            }


            // If class validation has passed..

            if (sender instanceof Player)
            {
                COnlinePlayer ninOnlinePlayer = new COnlinePlayer(((Player) sender).getUniqueId());

                ninOnlinePlayer.setPlayerClass(new PlayerClass(className), true, false);

            } // Target player has to be specified when this command is executed from console.
            else if (sender instanceof ConsoleCommandSender)
            {
                throw new NotEnoughArgumentsException(sender);
            }
        }
        else if (args.length == 2) // Class has been given, target player has been given.
        {
            String className = args[0];

            // If sender doesn't have the kingdomkits.setclass.[class] permission
            if (!sender.hasPermission("kingdomess.playerclass.setclass." + className))
                throw new AccessDeniedException(sender);


            if (!sender.hasPermission("kingdomess.playerclass.setclass.others." + className)) throw new AccessDeniedException(sender);

            // Class validation
            if (!PlayerClass.PlayerClassExists(className)) throw new PlayerClassNotFoundException(sender);


            // Class validation has passed..

            String targetPlayerName = args[1];
            OfflinePlayer op = Bukkit.getOfflinePlayer(targetPlayerName);

            // Player validation..
            if (op == null) throw new PlayerNotFoundException(sender);

            // Player validation & class validation has passed..

            COfflinePlayer ninOfflinePlayer = new COfflinePlayer(op);


            // Player can become class validation..
            if (!ninOfflinePlayer.canBecomeClass(className)) throw new PlayerCannotBecomeClassException(sender);


            // Cooldown validation..
            if (!ninOfflinePlayer.hasPlayerClassSwitchCoolDownExpired() &&
                    !sender.hasPermission("kingdomess.playerclass.bypass.changeclasscooldown"))
            {
                throw new CoolDownHasNotExpiredException(sender, ninOfflinePlayer.getNextPossibleClassSwitchTime(),
                        ninOfflinePlayer.toOfflinePlayer().getName());
            }


            // All validation has passed, set player's class..
            ninOfflinePlayer.setPlayerClass(new PlayerClass(className), true, false);
        }
    }
}
