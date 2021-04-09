package us.spaceclouds42.ekho

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.text.*
import net.minecraft.util.Formatting
import java.util.UUID

class EkhoBuilder(base: MutableText, method: EkhoBuilder.() -> Unit) {
    private var root = base
    private val siblings = mutableListOf<Text>()
    private var inherit = true

    /**
     * Inserts a new line in the resulting [MutableText] object
     */
    val newLine
        get() = run { siblings.add(LiteralText("\n")); "\n" }

    init {
        method()
    }

    /**
     * Creates the final [MutableText] object from all the inputs
     *
     * @return finalized [MutableText] object
     */
    fun create(): MutableText {
        siblings.forEach { root.append(it) }
        return root
    }

    operator fun String.invoke(inheritStyle: Boolean = true, method: EkhoBuilder.() -> Unit = { }) {
        this@EkhoBuilder.inherit = inheritStyle
        this@EkhoBuilder.siblings.add(
            EkhoBuilder(
                LiteralText(this).let { if (inheritStyle) { it.style = root.style }; it },
                method
            ).create()
        )
    }

    operator fun MutableText.invoke(inheritStyle: Boolean = true, method: EkhoBuilder.() -> Unit = { }) {
        this@EkhoBuilder.inherit = inheritStyle
        this@EkhoBuilder.siblings.add(
            EkhoBuilder(
                LiteralText(this.asString()).let { if (inheritStyle) { it.style = root.style } else { it.style = this.style }; it },
                method
            ).create()
        )
    }

    var style: Style = Style.EMPTY
        set(style) = run { root.style = style }

    fun style(method: StyleBuilder.() -> Unit): Style {
        return StyleBuilder(root.style.let { if (inherit) { it } else { Style.EMPTY } }).apply(method).create().let {
            root.style = it
            it
        }
    }
}

class StyleBuilder(private val parentStyle: Style) {
    private var textColor: TextColor? = null
    private var isBold: Boolean? = null
    private var isItalic: Boolean? = null
    private var isUnderlined: Boolean? = null
    private var isStrikethrough: Boolean? = null
    private var isObfuscated: Boolean? = null
    private var clickEvent: ClickEvent? = null
    private var hoverEvent: HoverEvent? = null

    val black
        get() = colorByCode(Formatting.BLACK)
    val darkBlue
        get() = colorByCode(Formatting.DARK_BLUE)
    val darkGreen
        get() = colorByCode(Formatting.DARK_GREEN)
    val darkAqua
        get() = colorByCode(Formatting.DARK_AQUA)
    val darkRed
        get() = colorByCode(Formatting.DARK_RED)
    val darkPurple
        get() = colorByCode(Formatting.DARK_PURPLE)
    val gold
        get() = colorByCode(Formatting.GOLD)
    val gray
        get() = colorByCode(Formatting.GRAY)
    val darkGray
        get() = colorByCode(Formatting.DARK_GRAY)
    val blue
        get() = colorByCode(Formatting.BLUE)
    val green
        get() = colorByCode(Formatting.GREEN)
    val aqua
        get() = colorByCode(Formatting.AQUA)
    val red
        get() = colorByCode(Formatting.RED)
    val lightPurple
        get() = colorByCode(Formatting.LIGHT_PURPLE)
    val yellow
        get() = colorByCode(Formatting.YELLOW)
    val white
        get() = colorByCode(Formatting.WHITE)

    var rgb: Int? = null
        set(rgb) = run { this.textColor = TextColor.fromRgb(rgb ?: 0x000000) }
    var color: TextColor? = null
        set(color) = run { this.textColor = color ?: TextColor.fromRgb(0x000000) }

    private fun colorByCode(formatting: Formatting) {
        this.textColor = TextColor.fromFormatting(formatting)
    }

    val bold
        get() = run { this.isBold = true }
    val noBold
        get() = run { this.isBold = false }

    val italics
        get() = run { this.isItalic = true }
    val noItalics
        get() = run { this.isItalic = false }

    val underline
        get() = run { this.isUnderlined = true }
    val noUnderline
        get() = run { this.isUnderlined = false }

    val strikethrough
        get() = run { this.isStrikethrough = true }
    val noStrikethrough
        get() = run { this.isStrikethrough = false }

    val obfuscated
        get() = run { this.isObfuscated = true }
    val noObfuscation
        get() = run { this.isObfuscated = false }

    fun clickEvent(method: ClickEventBuilder.() -> Unit) {
        this.clickEvent = ClickEventBuilder().apply(method).create()
    }

    /**
     * Only use this if you already have a [HoverEvent] object created,
     * otherwise use [itemHover], [entityHover], or [textHover] to create
     * one with ease.
     */
    fun hoverEvent(method: StyleBuilder.() -> HoverEvent) {
        this.hoverEvent = method()
    }

    fun itemHover(method: ItemHoverEventBuilder.() -> Unit) {
        this.hoverEvent = ItemHoverEventBuilder().apply(method).create()
    }

    fun entityHover(method: EntityHoverEventBuilder.() -> Unit) {
        this.hoverEvent = EntityHoverEventBuilder().apply(method).create()
    }

    fun textHover(method: TextHoverEventBuilder.() -> Unit) {
        this.hoverEvent = TextHoverEventBuilder().apply(method).create()
    }

    fun create(): Style = Style(
        textColor ?: parentStyle.color,
        isBold ?: parentStyle.isBold,
        isItalic ?: parentStyle.isItalic,
        isUnderlined ?: parentStyle.isUnderlined,
        isStrikethrough ?: parentStyle.isStrikethrough,
        isObfuscated ?: parentStyle.isObfuscated,
        clickEvent ?: parentStyle.clickEvent,
        hoverEvent ?: parentStyle.hoverEvent,
        null,
        Style.DEFAULT_FONT_ID,
    )
}

// Start Hover Events
abstract class HoverEventBuilder {
    abstract fun create(): HoverEvent
}

class ItemHoverEventBuilder : HoverEventBuilder() {
    var itemStack: ItemStack? = null
    var item: Item? = null
    var tag: CompoundTag? = null

    private fun generateItem(): ItemStack {
        return if (itemStack != null) {
            itemStack!!
        } else if (item != null) {
            ItemStack(item, 1).let { it.tag = tag; it }
        } else if (tag != null) {
            ItemStack.fromTag(tag)
        } else {
            ItemStack.EMPTY
        }
    }

    override fun create(): HoverEvent {
        return HoverEvent(
            HoverEvent.Action.SHOW_ITEM,
            HoverEvent.ItemStackContent(
                itemStack ?: generateItem(),
            )
        )
    }
}

class EntityHoverEventBuilder : HoverEventBuilder() {
    var type: EntityType<out Entity>? = null
    var uuid: UUID? = null
    var name: Text? = null

    override fun create(): HoverEvent {
        return HoverEvent(
            HoverEvent.Action.SHOW_ENTITY,
            HoverEvent.EntityContent(
                type ?: EntityType.PLAYER,
                uuid ?: UUID.randomUUID(),
                name,
            ),
        )
    }
}

class TextHoverEventBuilder : HoverEventBuilder() {
    var hoverText: Text? = null

    override fun create(): HoverEvent {
        return HoverEvent(
            HoverEvent.Action.SHOW_TEXT,
            hoverText ?: ekho(),
        )
    }
}
// End Hover Events

class ClickEventBuilder {
    private var action: ClickEvent.Action? = null
    private var value: String? = null

    var openUrl: String = ""
        set(url) = run { action = ClickEvent.Action.OPEN_URL; value = url }
    var openFile: String = ""
        set(path) = run { action = ClickEvent.Action.OPEN_FILE; value = path }
    var runCommand: String = ""
        set(command) = run { action = ClickEvent.Action.RUN_COMMAND; value = command }
    var suggestCommand: String = ""
        set(command) = run { action = ClickEvent.Action.SUGGEST_COMMAND; value = command }
    var changePage: Int = 0
        set(page) = run { action = ClickEvent.Action.CHANGE_PAGE; value = page.toString() }
    var copyToClipboard: String = ""
        set(copyText) = run { action = ClickEvent.Action.COPY_TO_CLIPBOARD; value = copyText }

    fun create(): ClickEvent {
        return ClickEvent(
            action ?: ClickEvent.Action.SUGGEST_COMMAND,
            value ?: "",
        )
    }
}

/**
 * Create a [MutableText] object using [EkhoBuilder]
 *
 * @param base the first part of the text, optional, defaults to empty string
 * @return a [MutableText] object
 */
fun ekho(base: String = "", method: EkhoBuilder.() -> Unit = { }): MutableText {
    return EkhoBuilder(LiteralText(base), method).create()
}

/**
 * Create a [MutableText] object using [EkhoBuilder]
 *
 * @param base the first part of the text, optional, defaults to empty literal text
 * @return a [MutableText] object
 */
fun ekho(base: Text, method: EkhoBuilder.() -> Unit = { }): MutableText {
    return EkhoBuilder(base as MutableText, method).create()
}

/**
 * Creates a [Style] object using [StyleBuilder]
 */
fun style(method: StyleBuilder.() -> Unit): Style = StyleBuilder(Style.EMPTY).apply(method).create()
