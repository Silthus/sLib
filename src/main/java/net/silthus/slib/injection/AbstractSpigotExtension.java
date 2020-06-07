package net.silthus.slib.injection;

import com.google.inject.Binder;
import com.google.inject.Module;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.Objects;
import java.util.logging.Level;

public abstract class AbstractSpigotExtension extends JavaPlugin implements IsabelleExtension, Module {

    /**
     * Message that will warn about missing override of {@link #configure(Binder)}.
     */
    private static final String MISSING_BINDING_OVERRIDE = "Bindings weren't override. Are you sure you don't need any bindings?";


    /**
     * The plugin description file read from the plugin.yml file. Used to determine version and name of the plugin
     * we are currently managing.
     */
    private final PluginDescriptionFile pluginDescriptionFile = getDescription();

    public AbstractSpigotExtension() {
    }

    public AbstractSpigotExtension(
            JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public String getVersion() {

        return pluginDescriptionFile.getVersion();
    }

    @Override
    public void onLoad() {

        bootstrap();
    }

    @Override
    public void onEnable() {

        if (Objects.nonNull(getResource("config.yml"))) {
            saveDefaultConfig();
        }

        reloadConfig();

        start();
    }

    @Override
    public void onDisable() {

        tearDown();
    }

    @Override
    public void configure(Binder binder) {

        getLogger().log(Level.WARNING, MISSING_BINDING_OVERRIDE);
    }
}