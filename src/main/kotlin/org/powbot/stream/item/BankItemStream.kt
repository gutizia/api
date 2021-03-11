package org.powbot.stream.item

import org.powerbot.script.rt4.ClientContext
import org.powerbot.script.rt4.Item
import java.util.stream.*

class BankItemStream(ctx: ClientContext, stream: Stream<Item>) : ItemStream<Item, Item, BankItemStream>(ctx, stream, { it }) {
    override fun nil(): Item {
        return Item.NIL
    }
}
