package io.github.erha134.mc.sparklib.fluid;

import dev.architectury.injectables.annotations.ExpectPlatform;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

public abstract class SFlowableFluid extends FlowableFluid {
    private final boolean infinite;
    private final int flowSpeed;
    private final int levelDecreasePerBlock;
    private final int tickRate;
    @Getter(onMethod_ = {@Override})
    private final float blastResistance;

    public SFlowableFluid(Settings settings) {
        this.infinite = settings.infinite;
        this.flowSpeed = settings.flowSpeed;
        this.levelDecreasePerBlock = settings.levelDecreasePerBlock;
        this.tickRate = settings.tickRate;
        this.blastResistance = settings.blastResistance;
        platformSetup(this.getStill(),
                this.getFlowing(),
                settings.stillTexture,
                settings.flowingTexture,
                settings.overlayTexture,
                settings.color);
    }

    @ExpectPlatform
    private static void platformSetup(Fluid still,
                                      Fluid flowing,
                                      Identifier stillTexture,
                                      Identifier flowingTexture,
                                      Identifier overlayTexture,
                                      int color) {
        throw new AssertionError();
    }

    public abstract Block getFluidBlock();

    @Override
    public BlockState toBlockState(FluidState state) {
        return this.getFluidBlock().getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(state));
    }

    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == this.getStill() || fluid == this.getFlowing();
    }

    @Override
    public boolean isInfinite(World world) {
        return this.infinite;
    }

    @Override
    public int getFlowSpeed(WorldView world) {
        return this.flowSpeed;
    }

    @Override
    public int getLevelDecreasePerBlock(WorldView world) {
        return this.levelDecreasePerBlock;
    }

    @Override
    public int getTickRate(WorldView world) {
        return this.tickRate;
    }

    @Override
    public void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) {
//        final BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
//        Block.dropStacks(state, world, pos, blockEntity);
    }

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockView world, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    public abstract static class Still extends SFlowableFluid {
        public Still(Settings settings) {
            super(settings);
        }

        @Override
        public final boolean isStill(FluidState state) {
            return true;
        }
    }

    public abstract static class Flowing extends SFlowableFluid {
        public Flowing(Settings settings) {
            super(settings);
        }

        @Override
        public void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
        }

        @Override
        public final boolean isStill(FluidState state) {
            return false;
        }
    }

    public static final class Settings {
        private boolean infinite;
        private int flowSpeed = 4;
        private int levelDecreasePerBlock = 1;
        private int tickRate = 5;
        private float blastResistance = 100.0F;
        private int color = -1;
        private Identifier stillTexture;
        private Identifier flowingTexture;
        private Identifier overlayTexture;

        public Settings() {
        }

        public Settings infinite() {
            this.infinite = true;
            return this;
        }

        public Settings flowSpeed(int flowSpeed) {
            this.flowSpeed = flowSpeed;
            return this;
        }

        public Settings levelDecreasePerBlock(int levelDecreasePerBlock) {
            this.levelDecreasePerBlock = levelDecreasePerBlock;
            return this;
        }

        public Settings tickRate(int tickRate) {
            this.tickRate = tickRate;
            return this;
        }

        public Settings blastResistance(float blastResistance) {
            this.blastResistance = blastResistance;
            return this;
        }

        public Settings color(int color) {
            this.color = color;
            return this;
        }

        public Settings stillTexture(Identifier stillTexture) {
            this.stillTexture = stillTexture;
            return this;
        }

        public Settings flowingTexture(Identifier flowingTexture) {
            this.flowingTexture = flowingTexture;
            return this;
        }

        public Settings overlayTexture(Identifier overlayTexture) {
            this.overlayTexture = overlayTexture;
            return this;
        }
    }
}
