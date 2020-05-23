package net.silthus.slib.injection;

import com.google.inject.Injector;
import com.google.inject.Module;
import com.netflix.governator.guice.BootstrapBinder;
import com.netflix.governator.guice.BootstrapModule;
import com.netflix.governator.guice.LifecycleInjector;
import com.netflix.governator.lifecycle.LifecycleManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;
import java.util.logging.Level;

/**
 * An {@link IsabelleExtension} in shape of a spigot {@link Plugin}. Due to dumb constraints of the bukkit API we have to
 * extend {@link JavaPlugin} here instead of working with {@link Plugin}. A later improvement could be to implement
 * a custom way of loading {@link IsabelleExtension}s.
 * <p>
 * A bukkit plugins lifecycle is describe via three methods:
 * <ul>
 * <li>{@link JavaPlugin#onLoad()}</li>
 * <li>{@link JavaPlugin#onEnable()}</li>
 * <li>{@link JavaPlugin#onDisable()}</li>
 * </ul>
 * Where {@link JavaPlugin#onLoad()} will be called for all plugins in any order first. The dependency tree will be
 * built and some initial work is done. The real functionality should be initialized using {@link JavaPlugin#onEnable()}
 * and destroy using {@link JavaPlugin#onDisable()}.
 * <p>
 * Mapping the bukkit life cycle methods is not easy at all. {@link JavaPlugin#onLoad()} is mapped to
 * {@link IsabelleExtension#bootstrap()}, {@link JavaPlugin#onEnable()} is mapped to {@link IsabelleExtension#start()}
 * and {@link JavaPlugin#onDisable()} will end the lifecycle via {@link IsabelleExtension#tearDown()}.
 * <p>
 * Note: Yes there are some difficulties. The normal life cycle isn't fully compatible, as it cannot be just divided
 * into three pieces, this is a compromise, where bootstrap doesn't really do the bootstrap, but only preparations.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 * @see JavaPlugin Bukkits entry type for plugins.
 * @see IsabelleExtension The interface we are adapting.
 */
public abstract class SpigotExtension extends AbstractSpigotExtension implements IsabelleExtension, Module, BootstrapModule {

    /**
     * Message wrapped into an exception that shows that the lifecycle could not be started properly. Mainly used
     * by {@link #start()}.
     */
    private static final String ERROR_WHILE_STARTING_LIFECYCLE = "Error while starting lifecycle.";

    /**
     * Message that will warn about missing override of {@link #configure(BootstrapBinder)}.
     */
    private static final String MISSING_BOOTSTRAP_OVERRIDE = "Bootstrap binding wasn't overridden. Are you sure you don't want to use some?";

    /**
     * Governators life cycle injector used to create {@link Injector}s and manage some governator related stuff.
     */
    private LifecycleInjector lifecycleInjector;

    /**
     * Governators life cycle manager needed for mapping the lifecycle methods.
     */
    private LifecycleManager lifecycleManager;

    public SpigotExtension() {
    }

    public SpigotExtension(
            JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void bootstrap() {

        lifecycleInjector = LifecycleInjector
                .builder()
                .withBootstrapModule(new SpigotBootstrapModule(this))
                .withAdditionalBootstrapModules(this)
                .withModules(new SpigotModule(this))
                .build();
        lifecycleManager = lifecycleInjector.getLifecycleManager();
    }

    @Override
    public void start() {

        try {
            lifecycleManager.start();
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, ERROR_WHILE_STARTING_LIFECYCLE, e);
        }

        Injector injector = lifecycleInjector.createInjector(this);
        injector.injectMembers(this);
    }

    @Override
    public void tearDown() {

        lifecycleManager.close();
    }

    @Override
    public boolean isAlive() {

        return lifecycleManager != null && lifecycleManager.hasStarted() && isEnabled();
    }

    @Override
    public void configure(BootstrapBinder bootstrapBinder) {

        getLogger().log(Level.WARNING, MISSING_BOOTSTRAP_OVERRIDE);
    }
}
