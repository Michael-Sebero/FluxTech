package gregtech.api.worldgen.populator;

import gregtech.api.unification.FluidUnifier;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.stack.UnificationEntry;
import gregtech.api.worldgen.config.OreConfigUtils;
import gregtech.api.worldgen.config.OreDepositDefinition;
import gregtech.api.worldgen.generator.GridEntryInfo;
import gregtech.common.blocks.MetaBlocks;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;

import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.Random;

public class SurfaceRockPopulator implements VeinChunkPopulator {

    private Material material;
    private int failedGenerationCounter = 0;

    public SurfaceRockPopulator() {}

    public SurfaceRockPopulator(Material material) {
        this.material = material;
    }

    @Override
    public void loadFromConfig(JsonObject object) {
        this.material = OreConfigUtils.getMaterialByName(object.get("material").getAsString());
    }

    @Override
    public void initializeForVein(OreDepositDefinition definition) {}

    private static boolean hasUndergroundMaterials(Collection<IBlockState> generatedBlocks) {
        for (IBlockState blockState : generatedBlocks) {
            if (blockState.getBlock() instanceof IFluidBlock || blockState.getBlock() instanceof BlockLiquid) {
                Fluid fluid = FluidRegistry.lookupFluidForBlock(blockState.getBlock());
                if (fluid != null && FluidUnifier.getMaterialFromFluid(fluid) != null) {
                    return true;
                }
            } else {
                ItemStack itemStack = new ItemStack(blockState.getBlock(), 1,
                        blockState.getBlock().damageDropped(blockState));
                UnificationEntry entry = OreDictUnifier.getUnificationEntry(itemStack);
                if (entry != null && entry.material != null && entry.material.hasProperty(PropertyKey.ORE)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void setStoneBlock(World world, BlockPos blockPos) {
        boolean surfaceRockPlaced = world.setBlockState(blockPos,
                MetaBlocks.SURFACE_ROCK.get(this.material).getBlock(this.material));
        if (!surfaceRockPlaced)
            failedGenerationCounter++;
    }

    /**
     * Generates the Surface Rock for an underground vein. Replaces the applicable topmost block in the chunk with a
     * Surface Rock, at a random position in the chunk. Does not run on a Flat world type
     *
     * @param world         - The Minecraft world. Used for finding the top most block and its state
     * @param chunkX        - The X chunk coordinate
     * @param chunkZ        - The Z chunk coordinate
     * @param random        - A Random parameter. Used for determining the number of spawned Surface Blocks and their
     *                      position
     * @param definition    - The Ore Vein definition
     * @param gridEntryInfo - Information about the ore generation grid for the current generation section
     */
    @Override
    public void populateChunk(World world, int chunkX, int chunkZ, Random random, OreDepositDefinition definition,
                              GridEntryInfo gridEntryInfo) {
        // Early return to prevent any population
        return;
    }

    public void generateSurfaceRock(World world, BlockPos pos) {
        // Early return to prevent surface rock generation
        return;
    }

    public Material getMaterial() {
        return material;
    }

    public static BlockPos findSpawnHeight(World world, BlockPos pos) {
        Chunk chunk = world.getChunk(pos);
        BlockPos.PooledMutableBlockPos blockpos = BlockPos.PooledMutableBlockPos.retain();
        blockpos.setPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ());
        int airBlocks = 0;
        while (blockpos.getY() > 20) {
            blockpos.move(EnumFacing.DOWN);
            IBlockState state = chunk.getBlockState(blockpos);
            if (state.getMaterial() == net.minecraft.block.material.Material.AIR ||
                    state.getMaterial() == net.minecraft.block.material.Material.LEAVES ||
                    state.getMaterial() == net.minecraft.block.material.Material.VINE ||
                    state.getBlock().isFoliage(world, blockpos)) {
                airBlocks++;
            } else {
                if (airBlocks >= 10 && state.isSideSolid(world, blockpos, EnumFacing.UP)) {
                    blockpos.move(EnumFacing.UP);
                    break;
                }
                airBlocks = 0;
            }
        }
        pos = blockpos.toImmutable();
        blockpos.release();
        return pos;
    }
}
