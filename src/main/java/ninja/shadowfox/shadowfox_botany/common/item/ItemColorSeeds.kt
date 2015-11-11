package ninja.shadowfox.shadowfox_botany.common.item

import java.awt.Color
import java.util.ArrayList
import java.util.HashMap
import java.util.Random
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ChunkCoordinates
import net.minecraft.util.IIcon
import net.minecraft.world.World
import vazkii.botania.client.core.helper.IconHelper
import vazkii.botania.common.Botania
import vazkii.botania.common.block.decor.IFloatingFlower.IslandType
import vazkii.botania.common.lib.LibItemNames
import ninja.shadowfox.shadowfox_botany.common.blocks.ShadowFoxBlocks
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.gameevent.TickEvent.Phase
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly

class ItemColorSeeds() : ColorfulItem("irisSeeds") {
    private val blockSwappers = HashMap<Int, ArrayList<BlockSwapper?>>()

    val TYPES = 16;
    override fun getSubItems(par1: Item, par2: CreativeTabs?, par3: MutableList<Any?>) {
        for(i in 0..(TYPES-1))
            par3.add(ItemStack(par1, 1, i));
    }

    override fun onItemUse(par1ItemStack: ItemStack, par2EntityPlayer: EntityPlayer, par3World: World, par4: Int, par5: Int, par6: Int, par7: Int, par8: Float, par9: Float, par10: Float): Boolean {
        val block = par3World.getBlock(par4, par5, par6)
        val bmeta = par3World.getBlockMetadata(par4, par5, par6)

        val color = Color(getColorFromItemStack(par1ItemStack, 0))
        val r = color.getRed() / 255F
        val g = color.getGreen() / 255F
        val b = color.getBlue() / 255F

        var x: Double
        var y: Double
        var z: Double
        val velMul = 0.025f

        if ((block === Blocks.dirt || block == Blocks.grass) && bmeta == 0) {
            var meta = par1ItemStack.getItemDamage()
            var swapper = addBlockSwapper(par3World, par4, par5, par6, meta)
            par3World.setBlock(par4, par5, par6, swapper.blockToSet, swapper.metaToSet, 1 or 2)
            for (i in 0..49) {
                x = (Math.random() - 0.5) * 3
                y = Math.random() - 0.5 + 1
                z = (Math.random() - 0.5) * 3
                Botania.proxy.wispFX(par3World, par4 + 0.5 + x, par5 + 0.5 + y, par6 + 0.5 + z, r, g, b, Math.random().toFloat() * 0.15f + 0.15f, (-x).toFloat() * velMul, (-y).toFloat() * velMul, (-z).toFloat() * velMul)
            }
        }
        par1ItemStack.stackSize--
        return true
    }

    @SubscribeEvent
    public fun onTickEnd(event: TickEvent.WorldTickEvent) {
        if(event.phase == Phase.END) {
            var dim = event.world.provider.dimensionId;
            if(blockSwappers.containsKey(dim)) {
                var swappers = blockSwappers.get(dim) as ArrayList<BlockSwapper?>;
                for (s in swappers)
                    if (s!!.tick())
                        swappers.remove(s)
            }
        }
    }

    private fun addBlockSwapper(world: World, x: Int, y: Int, z: Int, meta: Int): BlockSwapper {
        var swapper = swapperFromMeta(world, x, y, z, meta);

        var dim = world.provider.dimensionId;
        if(!blockSwappers.containsKey(dim))
            blockSwappers.put(dim, ArrayList<BlockSwapper?>());
        println(blockSwappers.get(dim)!!.toString())
        blockSwappers.get(dim)!!.add(swapper);

        return swapper;
    }

    private fun swapperFromMeta(world: World, x: Int, y: Int, z: Int, meta: Int): BlockSwapper {
        return BlockSwapper(world, ChunkCoordinates(x, y, z), ShadowFoxBlocks.coloredDirtBlock, meta);
    }

    private class BlockSwapper(world: World, coords: ChunkCoordinates, block: Block, meta: Int) {

        var world: World;
        var rand: Random;
        var blockToSet: Block;
        var metaToSet: Int;

        var startCoords: ChunkCoordinates;
        var ticksExisted = 0;

        init {
            this.world = world;
            blockToSet = block;
            metaToSet = meta;
            var seed = coords.posX xor coords.posY xor coords.posZ
            rand = Random(seed.toLong())
            startCoords = coords;
        }

        fun tick() : Boolean {
            ticksExisted++
            if (ticksExisted % 20 === 0) {
                var range = 3;
                for(i in -range..range) {
                    for(j in -range..range) {
                        var x = startCoords.posX + i;
                        var y = startCoords.posY;
                        var z = startCoords.posZ + j;
                        var block: Block = world.getBlock(x, y, z);
                        var meta: Int = world.getBlockMetadata(x, y, z);

                        if(block === blockToSet && meta === metaToSet) {
                            if(ticksExisted % 20 == 0) {
                                var validCoords = ArrayList<ChunkCoordinates>();
                                for(k in -1..1)
                                    for(l in -1..1) {
                                        var x1 = x + k;
                                        var z1 = z + l;
                                        var block1 = world.getBlock(x1, y, z1);
                                        var meta1 = world.getBlockMetadata(x1, y, z1);
                                        if((block1 == Blocks.dirt || block1 == Blocks.grass) && meta1 == 0)
                                            validCoords.add(ChunkCoordinates(x1, y, z1));
                                    }
                                if(!validCoords.isEmpty() && !world.isRemote) {
                                    var coords = validCoords.get(rand.nextInt(validCoords.size));
                                    world.setBlock(coords.posX, coords.posY, coords.posZ, blockToSet, metaToSet, 1 or 2);
                                }
                            }
                        }
                    }
                }
            }
            
            if(ticksExisted >= 80)
                return false
            return true
        }
    }
}
