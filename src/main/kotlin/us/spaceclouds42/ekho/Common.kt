package us.spaceclouds42.ekho

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.entity.EntityType
import net.minecraft.server.command.CommandManager
import net.minecraft.text.HoverEvent
import kotlin.system.exitProcess

object Common : ModInitializer {
    override fun onInitialize() {
        runTests()
    }

    private fun runTests() {
        test2()

        println("\n\n>>>>>>>>>>>>>>>>>>>>>>>>>>>   runTests(): testing complete, exiting..")
        // exitProcess(0)
        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.root.addChild(
                CommandManager
                    .literal("runTest2")
                    .executes {
                        it.source.sendFeedback(test0_2_0, false)
                        1
                    }
                    .build()
            )

            dispatcher.root.addChild(
                CommandManager
                    .literal("displayItem")
                    .executes {
                        it.source.sendFeedback(
                            ekho("Displaying item: ") {
                                style { yellow }
                                "THE ITEM" {
                                    style {
                                        red; bold; underline; italics
                                        itemHover {
                                            itemStack = it.source.player.mainHandStack
                                        }
                                    }
                                }
                            },
                            false
                        )
                        1
                    }
                    .build()
            )

            dispatcher.root.addChild(
                CommandManager
                    .literal("displayEntity")
                    .executes {
                        it.source.sendFeedback(
                            ekho("Displaying entity: ") {
                                style { yellow }
                                "THE ENTITY" {
                                    style {
                                        red; bold; underline; italics
                                        entityHover {
                                            type = EntityType.PLAYER
                                            uuid = it.source.player.uuid
                                            name = it.source.player.displayName
                                        }
                                    }
                                }
                                newLine
                                "standard hover event" {
                                    style {
                                        hoverEvent {
                                            HoverEvent(
                                                HoverEvent.Action.SHOW_ENTITY,
                                                HoverEvent.EntityContent(
                                                    EntityType.PLAYER,
                                                    it.source.player.uuid,
                                                    it.source.player.displayName,
                                                )
                                            )
                                        }
                                    }
                                }
                            },
                            false
                        )
                        1
                    }
                    .build()
            )
        }
    }
}

