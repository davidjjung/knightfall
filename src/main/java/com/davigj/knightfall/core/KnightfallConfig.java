package com.davigj.knightfall.core;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class KnightfallConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final KnightfallConfig.Common COMMON;

    static {
        Pair<KnightfallConfig.Common, ForgeConfigSpec> commonSpecPair = (new ForgeConfigSpec.Builder()).configure(KnightfallConfig.Common::new);
        COMMON_SPEC = (ForgeConfigSpec) commonSpecPair.getRight();
        COMMON = (KnightfallConfig.Common) commonSpecPair.getLeft();
    }

    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Double> headSlotMult;
        public final ForgeConfigSpec.ConfigValue<Double> chestSlotMult;
        public final ForgeConfigSpec.ConfigValue<Double> legSlotMult;
        public final ForgeConfigSpec.ConfigValue<Double> footSlotMult;
        public final ForgeConfigSpec.ConfigValue<Double> lightArmor;
        public final ForgeConfigSpec.ConfigValue<Double> mediumArmor;
        public final ForgeConfigSpec.ConfigValue<Double> heavyArmor;
        public final ForgeConfigSpec.ConfigValue<Double> veryHeavyArmor;
        public final ForgeConfigSpec.ConfigValue<Double> extraArmor;
        public final ForgeConfigSpec.ConfigValue<Double> gravity;
        public final ForgeConfigSpec.ConfigValue<Boolean> debugMode;
        public final ForgeConfigSpec.ConfigValue<String> dmgCalcType;
        public final ForgeConfigSpec.ConfigValue<Double> baseFallCalcDmg;
        public final ForgeConfigSpec.ConfigValue<Double> quadraticRate;
        public final ForgeConfigSpec.ConfigValue<Double> logisticLimit;
        public final ForgeConfigSpec.ConfigValue<Double> logisticSlope;
        public final ForgeConfigSpec.ConfigValue<Double> logisticInflectionPoint;
        public final ForgeConfigSpec.ConfigValue<Double> horseDmgMult;
        public final ForgeConfigSpec.ConfigValue<Double> mobDmgMult;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Armor").push("Armor Slot Multipliers");
            this.headSlotMult = builder.comment("The default 'weight' assigned to head armor, relative to all other armor slots. \nDefault: .4").define("headSlotMultiplier", 0.4);
            this.chestSlotMult = builder.comment("The default 'weight' assigned to chest armor, relative to all other armor slots. \nDefault: 0.9").define("chestSlotMultiplier", 0.9);
            this.legSlotMult = builder.comment("The default 'weight' assigned to leg armor, relative to all other armor slots. \nDefault: .75").define("legSlotMultiplier", 0.75);
            this.footSlotMult = builder.comment("The default 'weight' assigned to boots and shoes, relative to all other armor slots. \nDefault: .4").define("footSlotMultiplier", 0.4);
            builder.pop();
            builder.push("Armor Weight Class Multipliers");
            this.lightArmor = builder.comment("How heavy are light armor classes relative to others? \nDefault: 2.0").define("lightArmor", 2.0);
            this.mediumArmor = builder.comment("How heavy are medium armor classes relative to others? \nDefault: 5.0").define("mediumArmor", 5.0);
            this.heavyArmor = builder.comment("How heavy are heavy armor classes relative to others? \nDefault: 8.0").define("heavyArmor", 8.0);
            this.veryHeavyArmor = builder.comment("How heavy are Very heavy armor classes relative to others? \nDefault: 11.0").define("veryHeavyArmor", 11.0);
            this.extraArmor = builder.comment("How heavy are armor items from this extra tier relative to others? \nDefault: 0.0").define("extraArmor", 0.0);

            builder.pop();
            builder.push("Mathemagic");
            this.dmgCalcType = builder.comment("Check out the CurseForge page to see how you can reasonably configure the equations for fall damage." +
                    "\nAlternatively, check out https://www.desmos.com/calculator/jadvkibh0k to tinker with the functions directly." +
                    "\nType can be 'QUADRATIC' (default) or 'LOGISTIC'.").define("damageCalculationType", "QUADRATIC");
            this.baseFallCalcDmg = builder.comment("The damage multiplier's bare minimum upon falling under normal circumstances. \nDefault: 1.0").define("baseFallCalcDmg", 1.0);
            builder.push("Quadratic Settings");
            this.quadraticRate = builder.comment("The rate at which heavier armors deal greater fall damage.\nIncrease drastically at your own risk lol").define("quadraticGrowthRate", 0.0034);
            builder.pop();
            builder.push("Logistic Settings");
            this.logisticLimit = builder.comment("The general upper limit of the damage multiplier, approached at the highest armor weight classes.\nDefault: 3.5").define("logisticUpperBound", 3.5);
            this.logisticSlope = builder.comment("The slope of the curve. Higher the number, steeper the slope near the midpoint.\nDefault: 0.25").define("logisticSlope", 0.25);
            this.logisticInflectionPoint = builder.comment("The armor weight value at which the fall damage penalty increases the fastest.\nDefault: 16.1").define("weightPenaltyMidpoint", 16.1);
            builder.pop();
            builder.pop();
            builder.push("Miscellaneous");
            this.gravity = builder.comment("Consider it a general multiplier for all base player fall damage. Default 1.0. " +
                    "\nSetting to 0 eliminates all fall damage entirely.").define("genericFallDamageMultiplier", 1.0);
            this.mobDmgMult = builder.comment("Consider it a general multiplier for all base fall damage for mobs that can wear armor," +
                    "\nlike zombies and skeletons. Default 1.0.").define("mobFallDamageMultiplier", 1.0);
            this.debugMode = builder.comment("When activated, chat sends messages to players of their collective armor weight, \nand the amount the vanilla damage has been multiplied by, according to the magic formula." +
                    "\nThis occurs whenever the player falls a small distance or greater.").define("debugModeEnabled", false);
            this.horseDmgMult = builder.comment("Unlike the player, the horse follows a linear relationship between weight and fall damage." +
                    "\nThis is a simple multiplier determining the intensity of damage to the horse. Default: 0.5").define("horseDamageMultiplier", 0.5);
            builder.pop();
        }
    }
}
