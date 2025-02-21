package com.example.test.fluid;

import io.github.erha134.mc.sparklib.fluid.SFlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.state.StateManager;

public final class TestFluid {
    public static final class Still extends SFlowableFluid.Still {
        public Still(Settings settings) {
            super(settings);
        }

        @Override
        public int getLevel(FluidState state) {
            return 8;
        }
    }

    public static final class Flowing extends SFlowableFluid.Flowing {
        public Flowing(Settings settings) {
            super(settings);
        }

        @Override
        public void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState state) {
            return state.get(LEVEL);
        }
    }
}
