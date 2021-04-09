package us.spaceclouds42.ekho

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
import net.minecraft.text.*
import kotlin.system.exitProcess

object Common : ModInitializer {
    override fun onInitialize() {
        val launchGame = false // set to true to test with in game command, else it's a console test
        // runTests(launchGame)
    }

    private fun runTests(launchGame: Boolean) {
        val tests = listOf(
            ekho("test ekho") {
                style { blue }
                "with components"()
                LiteralText("of all kinds")(false) {
                    style { bold }
                }
            },
            // add more tests below as needed
        )

        if (!launchGame) {
            tests.forEach { println("\n${it.uglyPrint()}") }
            exitProcess(0)
        }

        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            for (i in 0..tests.size) {
                dispatcher.root.addChild(
                    CommandManager
                        .literal("testEkho${i + 1}")
                        .executes {
                            it.source.sendFeedback(ekho("running test ekho ${i + 1}"), false)
                            it.source.sendFeedback(tests[i], false)
                            1
                        }
                        .build()
                )
            }
        }
    }

    private fun Text.uglyPrint(): String {
        var ugly = ""

        ugly += "==Root: '${this.asString()}'"
        ugly += " STYLED: ${this.style}=="
        ugly += "\n"

        this.siblings.forEach { text ->
            if (text.siblings.isNotEmpty()) {
                ugly += "--Complex component--\n"
                ugly += "\n${text.uglyPrint()}"
            } else {
                ugly += "..Component: '${text.asString().replace("\n", "NEW_LINE")}'"
                ugly += " STYLED: ${text.style}.."
                ugly += "\n"
            }
        }

        return ugly
    }
}

