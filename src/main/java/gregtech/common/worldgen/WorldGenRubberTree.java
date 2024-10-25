package gregtech.common.worldgen;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class WorldGenRubberTree extends WorldGenerator {

    public static final WorldGenRubberTree TREE_GROW_INSTANCE = new WorldGenRubberTree(true);
    public static final WorldGenRubberTree WORLD_GEN_INSTANCE = new WorldGenRubberTree(false);

    protected WorldGenRubberTree(boolean notify) {
        super(notify);
    }

    @Override
    public boolean generate(@NotNull World world, @NotNull Random random, @NotNull BlockPos pos) {
        return false; // Prevent world generation of rubber trees
    }

    public boolean generateImpl(@NotNull World world, @NotNull Random random, BlockPos.MutableBlockPos pos) {
        return false; // Prevent manual generation
    }

    public boolean grow(World world, BlockPos pos, Random random) {
        return false; // Prevent sapling growth
    }

    public int getGrowHeight(@NotNull World world, @NotNull BlockPos pos) {
        return 0; // Always return 0 to prevent any growth
    }
}
