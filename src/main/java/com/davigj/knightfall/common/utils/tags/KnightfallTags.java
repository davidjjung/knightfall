package com.davigj.knightfall.common.utils.tags;

import com.davigj.knightfall.core.KnightfallMod;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

public class KnightfallTags {
    public static final ITag.INamedTag<Item> LIGHT_ARMOR = modItemTag("light_armor");
    public static final ITag.INamedTag<Item> MEDIUM_ARMOR = modItemTag("medium_armor");
    public static final ITag.INamedTag<Item> HEAVY_ARMOR = modItemTag("heavy_armor");
    public static final ITag.INamedTag<Item> VERY_HEAVY_ARMOR = modItemTag("very_heavy_armor");
    public static final ITag.INamedTag<Item> EXTRA_ARMOR = modItemTag("extra_armor");


    public KnightfallTags() {
    }

    private static ITag.INamedTag<Item> modItemTag(String path) {
        return ItemTags.bind(KnightfallMod.MOD_ID + ":" + path);
    }
}
