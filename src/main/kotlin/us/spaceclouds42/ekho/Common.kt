package us.spaceclouds42.ekho

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.entity.EntityType
import net.minecraft.server.command.CommandManager
import net.minecraft.text.*

object Common : ModInitializer {
    override fun onInitialize() {
        // runTests()
    }

    private fun runTests() {
        // exitProcess(0)
        val textObject: MutableText = ekho("yo") as MutableText
        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.root.addChild(
                CommandManager
                    .literal("clickTest")
                    .executes {
                        it.source.sendFeedback(test0_2_0_click, false); 1
                    }
                    .build()
            )
            dispatcher.root.addChild(
                CommandManager
                    .literal("runTest2")
                    .executes {
                        it.source.sendFeedback(test0_2_0_hover, false)
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

            dispatcher.root.addChild(
                CommandManager
                    .literal("translationTest")
                    .executes {
                        it.source.sendFeedback(
                            ekho(TranslatableText("translation.test.none")) {
                                newLine
                                "test"()
                                ekho("ekho") {
                                    style { bold }
                                }
                                newLine
                                TranslatableText(
                                    "translation.test.complex",
                                    it.source.player.name,
                                    "robot",
                                    "fabric"
                                )() {
                                    style {
                                        color { 0xA4243B }
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
                    .literal("translationTest")
                    .executes {
                        it.source.sendFeedback(
                            ekho(TranslatableText("translation.test.none")) {
                                newLine
                                "test"()
                                newLine
                                TranslatableText(
                                    "translation.test.complex",
                                    it.source.player.name,
                                    "robot",
                                    "fabric"
                                )() {
                                    style {
                                        color { 0xA4243B }
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

