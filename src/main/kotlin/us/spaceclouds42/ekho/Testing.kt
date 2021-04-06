package us.spaceclouds42.ekho

import net.minecraft.entity.EntityType
import net.minecraft.item.Items
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.Formatting
import java.util.*

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

val test0_1_0 =
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
val test0_2_0_hover =
    ekho {
        val green = style { green } // style method returns the style object, which can be reused
        "show item" {
            style {
                itemHover {
                    item = Items.DIAMOND_SHOVEL
                }
            }
        }
        newLine
        "show text" {
            style {
                textHover {
                    hoverText = ekho("have some hover text :yeef:") { style = green }
                }
            }
        }
        newLine
        "show entity" {
            style {
                entityHover {
                    type = EntityType.PLAYER
                    uuid = UUID.randomUUID()
                    name = ekho("player name pls work") {
                        style { rgb = 0xAF9DC7 }
                    }
                }
            }
        }
    }

val test0_2_0_click =
    ekho("click testing :D") {
        style { gold; italics }
        newLine

        "open url" {
            style {
                red
                clickEvent {
                    openUrl = "https://github.com/SpaceClouds42/Ekho/wiki/Hover-and-Click-Events"
                }
            }
        }
        newLine

        "suggest command" {
            style {
                blue
                clickEvent {
                    suggestCommand = "this text is a suggestion ;)"
                }
            }
        }
        newLine

        "run command" {
            style {
                green
                clickEvent {
                    runCommand = "/me command has run :D"
                }
            }
        }
    }

val styleTest1 = style { green }
val styleTest2 = ekho {
    style { blue }
}
// yay it worked :>