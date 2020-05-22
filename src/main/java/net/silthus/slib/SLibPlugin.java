package net.silthus.slib;

import net.silthus.slib.bukkit.BasePlugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

@kr.entree.spigradle.Plugin
public class SLibPlugin extends BasePlugin {

    public SLibPlugin() {
    }

    public SLibPlugin(
            JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void enable() {
        
    }

    @Override
    public void disable() {

    }
}
