package buff;

import java.io.File;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin implements Listener{
	
	private static Economy econ = null;
	
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		ConsoleCommandSender c = Bukkit.getConsoleSender();
		c.sendMessage("§8===================");
		c.sendMessage("§7Nome: §eBuff");
		c.sendMessage("§7Criador:§ayurirp4");
		c.sendMessage("§7Stats: §aAtivo");
		c.sendMessage("§7Versão: 1.4");
		c.sendMessage("§8===================");
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
		if (!setupEconomy()) {
			getLogger().severe(
					String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}
	
	public void onDisable() {
		ConsoleCommandSender c = Bukkit.getConsoleSender();
		c.sendMessage("§8===================");
		c.sendMessage("§7Nome: §eBuff");
		c.sendMessage("§7Criador:§cyurirp4");
		c.sendMessage("§7Stats: §cDesativado");
		c.sendMessage("§7Versão: §c1.4");
		c.sendMessage("§8===================");
	}
	
	public static void inv(Player p){
		Inventory inv = Bukkit.createInventory(null, 3*9, "Buffs");
		ItemStack vidro = new ItemStack(Material.STAINED_GLASS_PANE);
		ItemMeta vidrom = vidro.getItemMeta();
		vidrom.setDisplayName("§c");
		vidrom.addEnchant(Enchantment.DURABILITY, 10, true);
		vidro.setItemMeta(vidrom);
		ItemStack ferro = new ItemStack(Material.IRON_BLOCK);
		ItemMeta ferrom = ferro.getItemMeta();
		ferrom.setDisplayName("§aBuff");
		ferrom.setLore(
			    Main.getPlugin(Main.class).getConfig().getStringList("Lore")
		        .stream()
		        .map(string -> string.replaceAll("&", "§"))
		        .collect(Collectors.toList()));
		ferro.setItemMeta(ferrom);
		ItemStack ouro = new ItemStack(Material.GOLD_BLOCK);
		ItemMeta ourom = ouro.getItemMeta();
		ourom.setDisplayName("§aBuff Vip");
		ourom.setLore(
			    Main.getPlugin(Main.class).getConfig().getStringList("LoreVip")
		        .stream()
		        .map(string -> string.replaceAll("&", "§"))
		        .collect(Collectors.toList()));
		ouro.setItemMeta(ourom);
		inv.setItem(0, vidro);
		inv.setItem(1, vidro);
		inv.setItem(2, vidro);
		inv.setItem(3, vidro);
		inv.setItem(4, vidro);
		inv.setItem(5, vidro);
		inv.setItem(6, vidro);
		inv.setItem(7, vidro);
		inv.setItem(8, vidro);
		inv.setItem(9, vidro);
		inv.setItem(10, vidro);
		inv.setItem(11, vidro);
		inv.setItem(12, ferro);
		inv.setItem(13, vidro);
		inv.setItem(14, ouro);
		inv.setItem(15, vidro);
		inv.setItem(16, vidro);
		inv.setItem(17, vidro);
		inv.setItem(18, vidro);
		inv.setItem(19, vidro);
		inv.setItem(20, vidro);
		inv.setItem(21, vidro);
		inv.setItem(22, vidro);
		inv.setItem(23, vidro);
		inv.setItem(24, vidro);
		inv.setItem(25, vidro);
		inv.setItem(26, vidro);
		
		p.openInventory(inv);
		
	}
	
	@EventHandler
	public void click(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		if (e.getInventory().getName().equalsIgnoreCase("buffs")){
			e.setCancelled(true);
		}
		if (e.getCurrentItem().getType() == Material.IRON_BLOCK){
			if (econ.getBalance(p) >= getConfig().getInt("preco")) {
				econ.withdrawPlayer(p, getConfig().getInt("preco"));
				p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20*getConfig().getInt("Tempo"), 1));
				p.closeInventory();
				p.sendMessage(getConfig().getString("Sucesso").replace("&", "§"));
			} else {
				p.sendMessage(getConfig().getString("SemMoney").replace("&", "§"));
				p.closeInventory();
			}
		}
		if (e.getCurrentItem().getType() == Material.GOLD_BLOCK){
			if (!p.hasPermission("buff.vip")){
				p.sendMessage(getConfig().getString("SemPermission").replace("&", "§"));
				return;
			}
				if (econ.getBalance(p) >= getConfig().getDouble("preco_vip")) {
					econ.withdrawPlayer(p, getConfig().getDouble("preco_vip"));
					p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 20*getConfig().getInt("Tempo_Vip"), 2));
					p.closeInventory();
					p.sendMessage(getConfig().getString("Sucesso_vip").replace("&", "§"));
				} else {
					p.sendMessage(getConfig().getString("SemMoney").replace("&", "§"));
					p.closeInventory();
				}
			}
		}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)){
			sender.sendMessage("§cTu nao e um player");
			return true;
		}
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("buff")){
			Main.inv(p);
			p.sendMessage(getConfig().getString("Menu-Aberto").replace("&", "§"));
			if (getConfig().getBoolean("Som-Menu")){
				p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 5f, 4f);
			}
		}
		return false;
	}
}
