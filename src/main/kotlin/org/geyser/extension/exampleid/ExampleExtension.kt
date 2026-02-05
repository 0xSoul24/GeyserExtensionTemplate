package org.geyser.extension.exampleid

import org.geysermc.event.subscribe.Subscribe
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineResourcePacksEvent
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent
import org.geysermc.geyser.api.event.lifecycle.GeyserPreInitializeEvent
import org.geysermc.geyser.api.extension.Extension

/**
 * The main class of your extension - must implement extension, and be in the extension.yml file.
 * See [Extension] for available methods - for example to get the path to the configuration folder.
 */
class ExampleExtension : Extension {
    /**
     * Registering custom items/blocks, or adding resource packs (and basically all other events that are fired before Geyser initializes fully)
     * are done in their respective events. See below for an example:
     */
    @Subscribe
    fun onGeyserLoadResourcePacksEvent(event: GeyserDefineResourcePacksEvent) {
        logger().info("Loading: ${event.resourcePacks().size} resource packs.")
    }

    /**
     * You can use the GeyserPostInitializeEvent to run anything after Geyser fully initialized and is ready to accept bedrock player connections.
     */
    @Subscribe
    fun onPostInitialize(event: GeyserPostInitializeEvent?) {
        logger().info("Loading ${description().name()}...")

        // Example: accessing extension data folder
        val exampleDataFolder = dataFolder()
        logger().info(exampleDataFolder.toString())
    }

    /**
     * You can reload your extension - for example, reload the extension config - by listening to Geyser's
     * [org.geysermc.geyser.api.event.lifecycle.GeyserPreReloadEvent]
     */
    @Subscribe
    fun onGeyserReload(event: GeyserPreInitializeEvent?) {
        logger().info("Reloading ${description().name()}!")
    }
}
