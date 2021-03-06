package ninja.shadowfox.shadowfox_botany.common.blocks

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import ninja.shadowfox.shadowfox_botany.client.render.tile.MultipassRenderer
import ninja.shadowfox.shadowfox_botany.common.blocks.base.BlockContainerMod
import ninja.shadowfox.shadowfox_botany.common.blocks.base.IMultipassRenderer
import ninja.shadowfox.shadowfox_botany.common.blocks.tile.TileTreeCrafter
import ninja.shadowfox.shadowfox_botany.common.lexicon.LexiconRegistry
import ninja.shadowfox.shadowfox_botany.lib.Constants
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry
import vazkii.botania.api.wand.IWandHUD
import java.util.*


open class BlockTreeCrafter(name: String, val block: Block) : BlockContainerMod<TileTreeCrafter>(Material.wood), IWandHUD, ILexiconable, IMultipassRenderer {
    internal var random: Random
    override val registerInCreative: Boolean = false

    init {
        setHardness(3.0f)
        setResistance(5.0f)
        setLightLevel(1.0f)
        setStepSound(Block.soundTypeWood)
        setBlockName(name)
        random = Random()
    }

    override fun isOpaqueCube(): Boolean {
        return false
    }

    override fun createNewTileEntity(var1: World, var2: Int): TileTreeCrafter {
        return TileTreeCrafter()
    }

    override fun hasComparatorInputOverride(): Boolean {
        return true
    }

    override fun getComparatorInputOverride(par1World: World?, par2: Int, par3: Int, par4: Int, par5: Int): Int {
        val crafter = par1World!!.getTileEntity(par2, par3, par4) as TileTreeCrafter
        return crafter.signal
    }

    override fun getItemDropped(meta: Int, random: Random?, fortune: Int): Item {
        return Item.getItemFromBlock(innerBlock(0))
    }

    override fun renderHUD(mc: Minecraft, res: ScaledResolution, world: World, x: Int, y: Int, z: Int) {
        (world.getTileEntity(x, y, z) as TileTreeCrafter).renderHUD(mc, res)
    }

    override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?): LexiconEntry? {
        return LexiconRegistry.treeCrafting
    }

    override fun renderAsNormalBlock(): Boolean = false

    override fun canRenderInPass(pass: Int): Boolean {
        MultipassRenderer.pass = pass
        return true
    }

    override fun getRenderBlockPass(): Int = 1
    override fun getRenderType(): Int = Constants.multipassRenderingID

    override fun innerBlock(meta: Int): Block = block
}
