package buff;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;


public class Money {

    private static Economy economia = null;

    public static void register() {
        if (!setupEconomy()) {
            Bukkit.getConsoleSender().sendMessage("�cFalta o Vault ou Iconomy");
        }
    }


    private static boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) return false;

        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);

        if (rsp == null) return false;
        
        economia = (Economy) rsp.getProvider();
        return economia != null;
    }

    public static Double get(Player p) {
        if (economia == null) return 0D;
        return economia.getBalance(p);
    }

    public static void add(Player p, Double valor) {
        if (economia != null) economia.depositPlayer(p, valor);
    }

    public static void retirar(Player p, Double valor) {
        if (economia != null) economia.withdrawPlayer(p, valor);
    }

    public static boolean contains(Player p, Double valor) {
        if (economia != null && economia.getBalance(p) >= valor)
            return true;
        else
            return false;
    }
}