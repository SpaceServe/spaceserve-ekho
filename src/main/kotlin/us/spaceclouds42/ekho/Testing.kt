package us.spaceclouds42.ekho

import net.minecraft.text.HoverEvent
import net.minecraft.text.Text

private fun Text.prettyPrint(): String {
    var pretty = ""

    pretty += "==Root: '${this.asString()}'"
    pretty += " STYLED: ${this.style}=="
    pretty += "\n"

    this.siblings.forEach { text ->
        if (text.siblings.isNotEmpty()) {
            pretty += "--Complex component--\n"
            pretty += "\n${text.prettyPrint()}"
        } else {
            pretty += "..Component: '${text.asString()}'"
            pretty += " STYLED: ${text.style}.."
            pretty += "\n"
        }
    }

    return pretty
}

val testEkho =
ekho("root ") {
    style {
        bold
        hoverEvent {
            HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                ekho ("hover text") {
                    style {
                        obfuscated
                    }
                }
            )
        }
    }

    // inherits properly
    "hello"()
    newLine
    "world"()
    newLine

    // new style (notice inheritance is blocked, so entirely new style is created)
    "string with new style"(false) {
        style {
            italics
            red
        }
        // new style is inherited
        "string that inherits new style"()
        // cancel inherit works like this too (will have no style w/o using style { })
        "string that does not inherit"(false)
        newLine
        // if inherit is true (default), style is properly inherited
        // and each style property can be individually overwritten
        // here, italic remains true (inherited), and underlined is
        // overwritten to now be true
        "string that has additional style (inherits and adds)" {
            style {
                underline
                green
            }
        }
    }
}

fun testEkho() {
    println("=============< " +
            "testEkho() start" +
            " >=============" +
            "\n\n" +
            testEkho.prettyPrint())
    println("==============< " +
            "testEkho end" +
            " >==============")
}
