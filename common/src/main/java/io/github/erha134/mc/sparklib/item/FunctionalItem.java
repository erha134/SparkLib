package io.github.erha134.mc.sparklib.item;

import net.minecraft.item.Item;

import java.util.function.UnaryOperator;

public class FunctionalItem extends Item {
    public FunctionalItem(UnaryOperator<Item.Settings> builder) {
        super(builder.apply(new Item.Settings()));
    }

    public FunctionalItem() {
        this(p -> p);
    }
}
