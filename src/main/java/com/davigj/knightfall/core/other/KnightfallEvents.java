package com.davigj.knightfall.core.other;

import com.davigj.knightfall.common.utils.tags.KnightfallTags;
import com.davigj.knightfall.core.KnightfallConfig;
import com.davigj.knightfall.core.KnightfallMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Iterator;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = KnightfallMod.MOD_ID)
public class KnightfallEvents {
//    public static final ResourceLocation ZOMBIE = new ResourceLocation("minecraft", "zombie");
//    public static final ResourceLocation PIGLIN = new ResourceLocation("minecraft", "piglin");
//    public static final ResourceLocation ZOMBIFIED_PIGLIN = new ResourceLocation("minecraft", "zombified_piglin");
//    public static final ResourceLocation SKELETON = new ResourceLocation("minecraft", "zombie");
//    public static final ResourceLocation WITHER_SKELETON = new ResourceLocation("minecraft", "zombie");

    @SubscribeEvent
    public static void onFallDamage(LivingDamageEvent event) {
        Entity victim = event.getEntity();
        if (event.getSource() == DamageSource.FALL) {
            if (victim instanceof HorseEntity) {
                Iterable<ItemStack> horseArmorList = victim.getArmorInventoryList();
                double weight = 0;
                for (ItemStack itemStack : horseArmorList) {
                    Item horseArmor = itemStack.getItem();
                    if (horseArmor instanceof HorseArmorItem) {
                        for (LivingEntity living : victim.world.getEntitiesWithinAABB(LivingEntity.class, victim.getBoundingBox().grow(5.0D, 12.0D, 5.0D))) {
                            living.sendMessage(ITextComponent.getTextComponentOrEmpty("Armor: " + Objects.requireNonNull(horseArmor.getRegistryName()).toString()), living.getUniqueID());
                        }
                        int armorTypes = 0;
                        if (horseArmor.isIn(KnightfallTags.LIGHT_ARMOR)) {
                            weight = weight + KnightfallConfig.COMMON.lightArmor.get();
                            armorTypes++;
                        }
                        if (horseArmor.isIn(KnightfallTags.MEDIUM_ARMOR)) {
                            weight = weight + KnightfallConfig.COMMON.mediumArmor.get();
                            armorTypes++;
                        }
                        if (horseArmor.isIn(KnightfallTags.HEAVY_ARMOR)) {
                            weight = weight + KnightfallConfig.COMMON.heavyArmor.get();
                            armorTypes++;
                        }
                        if (horseArmor.isIn(KnightfallTags.VERY_HEAVY_ARMOR)) {
                            weight = weight + KnightfallConfig.COMMON.veryHeavyArmor.get();
                            armorTypes++;
                        }
                        if (horseArmor.isIn(KnightfallTags.EXTRA_ARMOR)) {
                            weight = weight + KnightfallConfig.COMMON.extraArmor.get();
                            armorTypes++;
                        }
                        weight = weight / Math.max(1, armorTypes);
                    }
                }
                event.setAmount((float) (event.getAmount() + (weight * KnightfallConfig.COMMON.horseDmgMult.get())));
                for (LivingEntity living : victim.world.getEntitiesWithinAABB(LivingEntity.class, victim.getBoundingBox().grow(5.0D, 12.0D, 5.0D))) {
                    living.sendMessage(ITextComponent.getTextComponentOrEmpty("Damage: " + event.getAmount()), living.getUniqueID());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        Entity victim = event.getEntity();
        if (victim instanceof PlayerEntity) {
            calcFallDamageMultiplier(victim, event, KnightfallConfig.COMMON.gravity.get());
        } else if ((victim instanceof ZombieEntity || victim instanceof SkeletonEntity || victim instanceof WitherSkeletonEntity ||
                victim instanceof ZombifiedPiglinEntity || victim instanceof PiglinEntity) && KnightfallConfig.COMMON.mobKnightfall.get()) {
            calcFallDamageMultiplier(victim, event, KnightfallConfig.COMMON.mobDmgMult.get());
            for (LivingEntity living : victim.world.getEntitiesWithinAABB(LivingEntity.class, victim.getBoundingBox().grow(5.0D, 12.0D, 5.0D))) {
                if (KnightfallConfig.COMMON.debugMode.get()) {
                    living.sendMessage(ITextComponent.getTextComponentOrEmpty("Damage Multiplier: " + event.getDamageMultiplier()), living.getUniqueID());
                    living.sendMessage(ITextComponent.getTextComponentOrEmpty("Regsitry Name: " + victim.getType().getRegistryName().toString()), living.getUniqueID());
                }
            }
        }
    }

    public static void calcFallDamageMultiplier(Entity victim, LivingFallEvent event, double gravity) {
        Iterable<ItemStack> armorList = victim.getArmorInventoryList();
        Iterator<ItemStack> armorPieces = armorList.iterator();
        double armorSlotBonus = 0;
        double totalArmorWeight = 0;
        while (armorPieces.hasNext()) {
            Item armorPiece = ((ItemStack) armorPieces.next()).getItem();
            if (armorPiece instanceof ArmorItem) {
                int armorWeightTypes = 0;
                double indivArmorWeight = 0;
                switch (((ArmorItem) armorPiece).getEquipmentSlot()) {
                    case HEAD:
                        armorSlotBonus = KnightfallConfig.COMMON.headSlotMult.get();
                        break;
                    case CHEST:
                        armorSlotBonus = KnightfallConfig.COMMON.chestSlotMult.get();
                        break;
                    case LEGS:
                        armorSlotBonus = KnightfallConfig.COMMON.legSlotMult.get();
                        break;
                    case FEET:
                        armorSlotBonus = KnightfallConfig.COMMON.footSlotMult.get();
                        break;
                }
                if (armorPiece.isIn(KnightfallTags.LIGHT_ARMOR)) {
                    indivArmorWeight = indivArmorWeight + KnightfallConfig.COMMON.lightArmor.get();
                    armorWeightTypes++;
                }
                if (armorPiece.isIn(KnightfallTags.MEDIUM_ARMOR)) {
                    indivArmorWeight = indivArmorWeight + KnightfallConfig.COMMON.mediumArmor.get();
                    armorWeightTypes++;
                }
                if (armorPiece.isIn(KnightfallTags.HEAVY_ARMOR)) {
                    indivArmorWeight = indivArmorWeight + KnightfallConfig.COMMON.heavyArmor.get();
                    armorWeightTypes++;
                }
                if (armorPiece.isIn(KnightfallTags.VERY_HEAVY_ARMOR)) {
                    indivArmorWeight = indivArmorWeight + KnightfallConfig.COMMON.veryHeavyArmor.get();
                    armorWeightTypes++;
                }
                if (armorPiece.isIn(KnightfallTags.EXTRA_ARMOR)) {
                    indivArmorWeight = indivArmorWeight + KnightfallConfig.COMMON.extraArmor.get();
                    armorWeightTypes++;
                }
                indivArmorWeight = indivArmorWeight / Math.max(1, armorWeightTypes);
                totalArmorWeight = (armorSlotBonus * indivArmorWeight) + totalArmorWeight;
            }
        }
        float dmg = event.getDamageMultiplier();
//          The current formula for calculating the fall damage multiplier.
        if (KnightfallConfig.COMMON.dmgCalcType.get().equals("QUADRATIC")) {
            event.setDamageMultiplier((float) ((KnightfallConfig.COMMON.baseFallCalcDmg.get() + (KnightfallConfig.COMMON.quadraticRate.get() * totalArmorWeight * totalArmorWeight)) * dmg * gravity));
        } else if (KnightfallConfig.COMMON.dmgCalcType.get().equals("LOGISTIC")) {
            event.setDamageMultiplier((float) ((KnightfallConfig.COMMON.baseFallCalcDmg.get() + (1 / ((1 / (KnightfallConfig.COMMON.logisticLimit.get() - KnightfallConfig.COMMON.baseFallCalcDmg.get()))
                    + (Math.pow(2.7, (-1 * KnightfallConfig.COMMON.logisticSlope.get()) * (totalArmorWeight - KnightfallConfig.COMMON.logisticInflectionPoint.get())))))) * dmg * gravity));
        }
        if (KnightfallConfig.COMMON.debugMode.get() && victim instanceof PlayerEntity) {
            victim.sendMessage(ITextComponent.getTextComponentOrEmpty("Armor Weight: " + totalArmorWeight), victim.getUniqueID());
            victim.sendMessage(ITextComponent.getTextComponentOrEmpty("Damage Multiplier: " + event.getDamageMultiplier()), victim.getUniqueID());
        }
    }
}
