package us.spaceclouds42.ekho

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager
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
        }
    }
}

