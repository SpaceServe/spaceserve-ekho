package us.spaceclouds42.ekho

import net.minecraft.text.Text

fun Text.prettyPrint(): String {
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

val testEkho = ekho("root ") {
    style {
        bold { true }
    }

    // inherits properly
    "hello"()
    newLine

    style {
        strikethrough { true }
    }

    "world"()
    newLine

    // new style (notice inheritance is blocked, so entirely new style is created)
    "string with new style"(false) {
        style {
            italic { true }
        }
        // new style is inherited
        "string that inherits new style"()
        // cancel inherit works here too
        "string that does not inherit"(false)
        newLine
        // if inherit is true (default), style is
        "string that has additional style (inherits and adds)" {
            style {
                underlined { true }
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
