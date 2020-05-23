package net.silthus.slib.injection.annotations;

import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Qualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Binding annotation that indicates that we want the plugin local config injected. By default that should be the
 * config got via {@link JavaPlugin#getConfig()}.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Qualifier
public @interface PluginConfig {

}