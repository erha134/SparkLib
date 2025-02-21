package io.github.erha134.mc.sparklib.fluid;

import dev.architectury.injectables.annotations.ExpectPlatform;
import io.github.erha134.easylib.util.ObjectNullSafe;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.function.Supplier;

public abstract class SFlowableFluid extends FlowableFluid {
    @Getter(onMethod_ = {@Override})
    private final SFlowableFluid still;
    @Getter(onMethod_ = {@Override})
    private final SFlowableFluid flowing;
    @Getter(onMethod_ = {@Override})
    private final Item bucketItem;
    private final FluidBlock fluidBlock;
    private final boolean infinite;
    private final int flowSpeed;
    private final int levelDecreasePerBlock;
    private final int tickRate;
    @Getter(onMethod_ = {@Override})
    private final float blastResistance;

    public SFlowableFluid(Settings settings) {
        this.still = ObjectNullSafe.requireNonNull(settings.still);
        this.flowing = ObjectNullSafe.requireNonNull(settings.flowing);
        this.bucketItem = ObjectNullSafe.requireNonNull(settings.bucketItem);
        this.fluidBlock = ObjectNullSafe.requireNonNull(settings.fluidBlock);
        this.infinite = settings.infinite;
        this.flowSpeed = settings.flowSpeed;
        this.levelDecreasePerBlock = settings.levelDecreasePerBlock;
        this.tickRate = settings.tickRate;
        this.blastResistance = settings.blastResistance;
        platformSetup(this.still,
                this.flowing,
                settings.stillTexture,
                settings.flowingTexture,
                settings.overlayTexture,
                settings.color);
    }

    @ExpectPlatform
    private static void platformSetup(SFlowableFluid still,
                                      SFlowableFluid flowing,
                                      Identifier stillTexture,
                                      Identifier flowingTexture,
                                      Identifier overlayTexture,
                                      int color) {
        throw new AssertionError();
    }

    @Override
    public BlockState toBlockState(FluidState state) {
        return this.fluidBlock.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(state));
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
        private SFlowableFluid still;
        private SFlowableFluid flowing;
        private Item bucketItem;
        private FluidBlock fluidBlock;
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

        public Settings still(SFlowableFluid still) {
            this.still = still;
            return this;
        }

        public Settings still(Supplier<SFlowableFluid> still) {
            return this.still(still.get());
        }

        public Settings flowing(SFlowableFluid flowing) {
            this.flowing = flowing;
            return this;
        }

        public Settings flowing(Supplier<SFlowableFluid> flowing) {
            return this.flowing(flowing.get());
        }

        public Settings bucketItem(Item bucketItem) {
            this.bucketItem = bucketItem;
            return this;
        }

        public Settings bucketItem(Supplier<Item> bucketItem) {
            return this.bucketItem(bucketItem.get());
        }

        public Settings fluidBlock(FluidBlock fluidBlock) {
            this.fluidBlock = fluidBlock;
            return this;
        }

        public Settings fluidBlock(Supplier<FluidBlock> fluidBlock) {
            return this.fluidBlock(fluidBlock.get());
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
