package net.silthus.slib.injection;

import com.netflix.governator.guice.BootstrapBinder;
import com.netflix.governator.guice.BootstrapModule;
import net.silthus.slib.injection.annotations.PluginConfig;
import org.bukkit.configuration.Configuration;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class SpigotBootstrapModule implements BootstrapModule {

    private final SpigotExtension extension;

    public SpigotBootstrapModule(SpigotExtension extension) {
        this.extension = extension;
    }

    @Override
    public void configure(BootstrapBinder bootstrapBinder) {

        bootstrapBinder.bind(Configuration.class).annotatedWith(PluginConfig.class).toInstance(extension.getConfig());
        bootstrapBinder.bindConfigurationProvider().to(BukkitConfigurationProvider.class);
    }
}