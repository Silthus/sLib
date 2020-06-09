package net.silthus.slib.config;

import lombok.NonNull;

import java.util.Objects;

/**
 * Marks the class as configurable signalling the need for a config.
 *
 * @param <TConfig> type of the config that the implementing class expects.
 */
public interface Configured<TConfig> {

    /**
     * Returns the needed type of the config implementation: TConfig.
     * This is needed to check the generic type at runtime.
     *
     * @return type of the config
     */
    @NonNull Class<TConfig> getConfigClass();

    /**
     * Checks if the given config object matches the needed config type TConfig.
     *
     * @param config config to check type for
     * @return true if type of the config matches and {@link #load(TConfig)} can be called.
     *          Will return false if the config object is null or does not match the needed type from {@link #getConfigClass()}.
     */
    default boolean isConfigType(Object config) {
        if (Objects.isNull(config)) return false;
        return getConfigClass().isInstance(config);
    }

    /**
     * Tries to load the given config object.
     * Checks {@link #isConfigType(Object)} first and then calls {@link #load(Object)}.
     * Using this method has no need for null or type checks.
     * <br>
     * If the type does not match or the config is null {@link #load(Object)} will not be called.
     *
     * @param config config to load. Can be null.
     */
    default void tryLoad(Object config) {
        if (isConfigType(config)) {
            load(getConfigClass().cast(config));
        }
    }

    /**
     * Called with the needed config after the config was loaded.
     *
     * @param config config to load this class with.
     *               Cannot be null.
     */
    void load(@NonNull TConfig config);
}
