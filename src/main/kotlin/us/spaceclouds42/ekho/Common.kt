package us.spaceclouds42.ekho

import net.fabricmc.api.ModInitializer
import kotlin.system.exitProcess

object Common : ModInitializer {
    override fun onInitialize() {
        runTests()
    }

    private fun runTests() {
        test2()

        println("\n\n>>>>>>>>>>>>>>>>>>>>>>>>>>>   runTests(): testing complete, exiting..")
        exitProcess(0)
    }
}

