package io.github.erha134.mc.sparklib.patched.math;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class SBlockPos {
    public static BlockPos of(Vec3d pos) {
        return BlockPos.ofFloored(pos);
    }
}
