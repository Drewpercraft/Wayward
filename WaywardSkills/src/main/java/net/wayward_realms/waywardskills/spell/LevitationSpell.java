package net.wayward_realms.waywardskills.spell;

import net.wayward_realms.waywardlib.character.Character;
import net.wayward_realms.waywardlib.combat.Fight;
import net.wayward_realms.waywardlib.skills.SkillType;
import net.wayward_realms.waywardlib.skills.SkillsPlugin;
import net.wayward_realms.waywardlib.skills.SpellBase;
import net.wayward_realms.waywardskills.WaywardSkills;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.HashMap;
import java.util.Map;

public class LevitationSpell extends SpellBase {

    private final WaywardSkills plugin;

    public LevitationSpell(WaywardSkills plugin) {
        this.plugin = plugin;
        setCoolDown(300);
        setManaCost(50);
        setName("Levitation");
        setType(SkillType.MAGIC_ILLUSION);
    }

    @Override
    public boolean use(final Player player) {
        player.setAllowFlight(true);
        player.sendMessage(ChatColor.GREEN + "You have five seconds of flight!");
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                player.setAllowFlight(false);
                player.sendMessage(ChatColor.RED + "Flight ended.");
            }
        }, 100L);
        return true;
    }

    @Override
    public boolean use(Fight fight, Character attacking, Character defending, ItemStack weapon) {
        return false;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack icon = new ItemStack(Material.FEATHER);
        ItemMeta meta = icon.getItemMeta();
        meta.setDisplayName("Levitate");
        icon.setItemMeta(meta);
        return icon;
    }

    @Override
    public boolean canUse(Character character) {
        return character.getSkillPoints(SkillType.MAGIC_ILLUSION) >= 50;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialised = new HashMap<>();
        serialised.put("cooldown", getCoolDown());
        serialised.put("mana-cost", getManaCost());
        serialised.put("name", getName());
        return serialised;
    }

    public static LevitationSpell deserialize(Map<String, Object> serialised) {
        RegisteredServiceProvider<SkillsPlugin> skillsPluginProvider = Bukkit.getServer().getServicesManager().getRegistration(SkillsPlugin.class);
        if (skillsPluginProvider != null) {
            SkillsPlugin skillsPlugin = skillsPluginProvider.getProvider();
            if (skillsPlugin instanceof WaywardSkills) {
                LevitationSpell deserialised = new LevitationSpell((WaywardSkills) skillsPlugin);
                deserialised.setName((String) serialised.get("name"));
                deserialised.setCoolDown((int) serialised.get("cooldown"));
                deserialised.setManaCost((int) serialised.get("mana-cost"));
                return deserialised;
            }
        }
        return null;
    }

}
