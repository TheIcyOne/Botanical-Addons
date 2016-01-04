package ninja.shadowfox.shadowfox_botany.common.blocks.rainbow

import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.FMLLaunchHandler
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.block.Block
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.util.MovingObjectPosition
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.client.event.TextureStitchEvent
import net.minecraftforge.common.MinecraftForge
import ninja.shadowfox.shadowfox_botany.common.blocks.ShadowFoxBlocks
import ninja.shadowfox.shadowfox_botany.common.blocks.base.ShadowFoxBlockMod
import ninja.shadowfox.shadowfox_botany.common.blocks.material.MaterialCustomSmeltingWood
import ninja.shadowfox.shadowfox_botany.common.blocks.tile.TileTreeCrafter
import ninja.shadowfox.shadowfox_botany.common.item.blocks.ItemIridescentBlockMod
import ninja.shadowfox.shadowfox_botany.common.lexicon.LexiconRegistry
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.api.wand.IWandable
import vazkii.botania.client.render.block.InterpolatedIcon
import java.util.*
import kotlin.properties.Delegates


public class BlockRainbowPlanks(): ShadowFoxBlockMod(MaterialCustomSmeltingWood.material), ILexiconable, IFuelHandler, IWandable {

    private val name = "rainbowPlanks"
    protected var icons : IIcon by Delegates.notNull()

    init {
        blockHardness = 2F
        setLightLevel(0f)
        stepSound = soundTypeWood

        setBlockName(this.name)
        if (FMLLaunchHandler.side().isClient)
            MinecraftForge.EVENT_BUS.register(this)
        GameRegistry.registerFuelHandler(this)
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    fun loadTextures(event: TextureStitchEvent.Pre) {
        if(event.map.textureType == 0) {
            var icon = InterpolatedIcon("shadowfox_botany:rainbowPlanks")
            if(event.map.setTextureEntry("shadowfox_botany:rainbowPlanks", icon))
                this.icons = icon
        }
    }

    override fun onUsedByWand(p0: EntityPlayer?, p1: ItemStack?, p2: World?, p3: Int, p4: Int, p5: Int, p6: Int): Boolean {
        if (p2 != null) {
            if (TileTreeCrafter.canEnchanterExist(p2, p3, p4, p5, p6, p0)) {
                p2.setBlock(p3, p4, p5, ShadowFoxBlocks.treeCrafterBlockRB, p6, 3)
                p2.playSoundEffect(p3.toDouble(), p4.toDouble(), p5.toDouble(), "botania:enchanterBlock", 0.5F, 0.6F)

                return true
            }
        }

        return false
    }

    override fun isToolEffective(type: String?, metadata: Int): Boolean {
        return (type != null && type.equals("axe", true))
    }

    override fun getHarvestTool(metadata : Int): String {
        return "axe"
    }

    override fun shouldRegisterInNameSet(): Boolean {
        return false
    }

    override fun damageDropped(par1: Int): Int {
        return par1
    }

    override fun setBlockName(par1Str: String): Block {
        register(par1Str)
        return super.setBlockName(par1Str)
    }

    override fun quantityDropped(random: Random): Int { return 1 }

    override fun getItemDropped(meta: Int, random: Random, fortune: Int): Item {
        return Item.getItemFromBlock(this)
    }


    @SideOnly(Side.CLIENT)
    override fun getIcon(side: Int, meta: Int): IIcon {
        return icons
    }

    override fun isWood(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean { return true }

    internal fun register(name: String) {
        GameRegistry.registerBlock(this, ItemIridescentBlockMod::class.java, name)
    }


    override fun getPickBlock(target: MovingObjectPosition?, world: World, x: Int, y: Int, z: Int): ItemStack {
        val meta = world.getBlockMetadata(x, y, z)
        return ItemStack(this, 1, meta)
    }

    override fun registerBlockIcons(par1IconRegister: IIconRegister) {}

    override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?): LexiconEntry? {
        return LexiconRegistry.irisSapling
    }

    override fun getBurnTime(fuel: ItemStack): Int {
        return if (fuel.item == Item.getItemFromBlock(this)) 300 else 0
    }
}
