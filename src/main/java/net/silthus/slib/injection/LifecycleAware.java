package net.silthus.slib.injection;

/**
 * Marks an unit that is aware of its lifecycle. The lifecycle is started via {@link #bootstrap()} followed by
 * {@link #start()} and ended via {@link #tearDown()}.
 * <p>
 * You may check the status of the lifecycle at any time using {@link #isAlive()}.
 *
 * @author Felix Klauke <info@felix-klauke.de>
 */
public interface LifecycleAware {

    /**
     * Bootstrap the life cycle.
     */
    void bootstrap();

    /**
     * Start the life cycle-
     */
    void start();

    /**
     * End the life cycle.
     */
    void tearDown();

    /**
     * Checks if the lifecycle is currently active.
     *
     * @return If the lifecycle is alive.
     */
    boolean isAlive();
}
