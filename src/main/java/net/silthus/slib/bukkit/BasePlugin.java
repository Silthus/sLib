package net.silthus.slib.bukkit;

import com.google.inject.Binder;
import com.netflix.governator.annotations.PreConfiguration;
import lombok.Getter;
import net.silthus.slib.injection.SpigotExtension;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.logging.Level;

/**
 * @author Silthus
 */
@Getter
public abstract class BasePlugin extends SpigotExtension implements CommandExecutor {

    public BasePlugin() {
    }

    public BasePlugin(
            JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @PreConfiguration
    public void onPreConfiguration() {

        load();

        getLogger().info(getName() + "-v" + getVersion() + " loaded.");
    }

    @PostConstruct
    public void onPostConstruct() {

        getDataFolder().mkdirs();

        enable();

        getLogger().info(getName() + "-v" + getVersion() + " enabled.");
    }

    @PreDestroy
    public void onPreDestroy() {

        disable();

        this.getServer().getScheduler().cancelTasks(this);

        getLogger().info(getName() + "-v" + getVersion() + " disabled.");
    }

    /**
     * Called when the plugin is loaded and before it is enabled.
     *
     * @see JavaPlugin#onLoad()
     */
    public void load() {

    }

    /**
     * Called once the plugin is enable.
     *
     * @see JavaPlugin#onEnable()
     */
    public abstract void enable();

    /**
     * Called once the plugin is disabled.
     *
     * @see JavaPlugin#onDisable()
     */
    public abstract void disable();

    public void registerEvents(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void unregisterEvents(Listener listener) {
        HandlerList.unregisterAll(listener);
    }
}
