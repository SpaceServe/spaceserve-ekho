package us.spaceclouds42.ekho

import net.fabricmc.api.ModInitializer
import kotlin.system.exitProcess

object Common : ModInitializer {
    override fun onInitialize() {
        testEkho()

        println("\n\n>>>>>>>>>>>>>>>>>>>>>>>>>>>   Common(): testing complete, exiting..")
        exitProcess(0)
    }
}

