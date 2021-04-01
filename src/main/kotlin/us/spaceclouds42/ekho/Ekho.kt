package us.spaceclouds42.ekho

import net.minecraft.text.*
import net.minecraft.util.Formatting

class EkhoBuilder(base: LiteralText, method: EkhoBuilder.() -> Unit) {
    private var root: MutableText = base
    private val siblings = mutableListOf<Text>() 
    private var inherit = true

    /**
     * Inserts a new line in the resulting [Text] object
     */
    val newLine
        get() = run { siblings.add(LiteralText("\n")); "\n" }

    init {
        method()
    }

    /**
     * Creates the final [Text] object from all the inputs
     *
     * @return finalized [Text] object
     */
    fun create(): Text {
        siblings.forEach { root.append(it) }
        return root
    }

    operator fun String.invoke(inheritStyle: Boolean = true, method: EkhoBuilder.() -> Unit = { }) {
        inherit = inheritStyle
        if (method == { }) {
            LiteralText(this).let { it.style = root.style; siblings.add(it) }
        } else {
            siblings.add(EkhoBuilder(
                LiteralText(this).let { if (inheritStyle) { it.style = root.style }; it },
                method
            ).create())
        }
    }

    fun style(method: StyleBuilder.() -> Unit) {
        root.style = StyleBuilder(root.style.let { if (inherit) { it } else { Style.EMPTY } }).apply(method).create()
    }
}

class StyleBuilder(private val parentStyle: Style) {
    private var color: TextColor? = null
    private var bold: Boolean? = null
    private var italic: Boolean? = null
    private var underlined: Boolean? = null
    private var strikethrough: Boolean? = null
    private var obfuscated: Boolean? = null
    private var clickEvent: ClickEvent? = null
    private var hoverEvent: HoverEvent? = null

    fun color(method: StyleBuilder.() -> Int) {
        this.color = TextColor.fromRgb(method()) ?: TextColor.fromFormatting(Formatting.WHITE)
    }

    fun bold(method: StyleBuilder.() -> Boolean) {
        this.bold = method()
    }

    fun italic(method: StyleBuilder.() -> Boolean) {
        this.italic = method()
    }

    fun underlined(method: StyleBuilder.() -> Boolean) {
        this.underlined = method()
    }

    fun strikethrough(method: StyleBuilder.() -> Boolean) {
        this.strikethrough = method()
    }

    fun obfuscated(method: StyleBuilder.() -> Boolean) {
        this.obfuscated = method()
    }

    fun clickEvent(method: StyleBuilder.() -> ClickEvent) {
        this.clickEvent = method()
    }

    fun hoverEvent(method: StyleBuilder.() -> HoverEvent) {
        this.hoverEvent = method()
    }

    fun create(): Style = Style(
        color ?: parentStyle.color,
        bold ?: parentStyle.isBold,
        italic ?: parentStyle.isItalic,
        underlined ?: parentStyle.isUnderlined,
        strikethrough ?: parentStyle.isStrikethrough,
        obfuscated ?: parentStyle.isObfuscated,
        clickEvent ?: parentStyle.clickEvent,
        hoverEvent ?: parentStyle.hoverEvent,
        null,
        Style.DEFAULT_FONT_ID,
    )
}

/**
 * Create a [Text] object using [EkhoBuilder]
 *
 * @param base the first part of the text, optional, defaults to empty string
 * @return a [Text] object
 */
fun ekho(base: String = "", method: EkhoBuilder.() -> Unit = { }): Text {
    return EkhoBuilder(LiteralText(base), method).create()
}
