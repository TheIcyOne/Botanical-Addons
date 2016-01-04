package ninja.shadowfox.shadowfox_botany.common.blocks.alt_grass

import cpw.mods.fml.common.IFuelHandler
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockSlab
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraft.world.World
import ninja.shadowfox.shadowfox_botany.common.blocks.ShadowFoxBlocks
import ninja.shadowfox.shadowfox_botany.common.blocks.base.ShadowFoxSlabs
import ninja.shadowfox.shadowfox_botany.common.blocks.base.ShadowFoxStairs
import ninja.shadowfox.shadowfox_botany.common.item.blocks.ItemBlockMod
import ninja.shadowfox.shadowfox_botany.common.item.blocks.ItemRegularSlabMod
import ninja.shadowfox.shadowfox_botany.common.lexicon.LexiconRegistry
import vazkii.botania.api.lexicon.ILexiconable
import vazkii.botania.api.lexicon.LexiconEntry

class BlockAltWoodSlab(full: Boolean, meta: Int, source: Block = ShadowFoxBlocks.altPlanks):
        ShadowFoxSlabs(full, meta, source, source.unlocalizedName.replace("tile.".toRegex(), "") + "Slab" + (if (full) "Full" else "") + meta), ILexiconable, IFuelHandler {

    override fun getFullBlock(): BlockSlab {
        return ShadowFoxBlocks.altSlabsFull[meta] as BlockSlab
    }

    override fun getIcon(side: Int, blockMeta: Int): IIcon {
        return source.getIcon(side, meta)
    }

    override fun register() {
        GameRegistry.registerBlock(this, ItemRegularSlabMod::class.java, name)
    }

    override fun getSingleBlock(): BlockSlab {
        return ShadowFoxBlocks.altSlabs[meta] as BlockSlab
    }

    override fun getBurnTime(fuel: ItemStack): Int {
        return if (fuel.item == Item.getItemFromBlock(this)) 150 else 0
    }

    override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?): LexiconEntry? {
        return LexiconRegistry.irisSapling
    }
}

class BlockAltWoodStairs(meta: Int, source: Block = ShadowFoxBlocks.altPlanks):
        ShadowFoxStairs(source, meta, source.unlocalizedName.replace("tile.".toRegex(), "") + "Stairs" + meta) {
    override fun register() {
        GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
    }

    override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?): LexiconEntry? {
        return LexiconRegistry.irisSapling
    }
}
