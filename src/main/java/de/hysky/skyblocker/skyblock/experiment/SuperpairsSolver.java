package de.hysky.skyblocker.skyblock.experiment;

import de.hysky.skyblocker.config.configs.HelperConfig;
import de.hysky.skyblocker.utils.render.gui.ColorHighlight;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public final class SuperpairsSolver extends ExperimentSolver {
	private int superpairsPrevClickedSlot = 0;
	private ItemStack superpairsCurrentSlot = ItemStack.EMPTY;
	private final IntSet superpairsDuplicatedSlots = new IntArraySet(14);

	@Override
	public boolean onClickSlot(int slot, ItemStack stack, int screenId) {
		if (getState() == State.SHOW) {
			this.superpairsPrevClickedSlot = slot;
			this.superpairsCurrentSlot = ItemStack.EMPTY;
		}
		return super.onClickSlot(slot, stack, screenId);
	}

	public SuperpairsSolver() {
		super("^Superpairs \\(\\w+\\)$");
	}

	@Override
	protected boolean isEnabled(HelperConfig.Experiments experimentsConfig) {
		return experimentsConfig.enableSuperpairsSolver;
	}

	@Override
	public void start(GenericContainerScreen screen) {
		super.start(screen);
		setState(State.SHOW);
	}

	@Override
	public void reset() {
		superpairsPrevClickedSlot = 0;
		superpairsCurrentSlot = ItemStack.EMPTY;
		superpairsDuplicatedSlots.clear();
	}

	@Override
	protected void tick(GenericContainerScreen screen) {
		if (getState() == State.SHOW && getSlots().get(superpairsPrevClickedSlot) == null) {
			ItemStack itemStack = screen.getScreenHandler().getInventory().getStack(superpairsPrevClickedSlot);
			if (!(itemStack.isOf(Items.CYAN_STAINED_GLASS) || itemStack.isOf(Items.BLACK_STAINED_GLASS_PANE) || itemStack.isOf(Items.AIR))) {
				getSlots().int2ObjectEntrySet().stream()
				          .filter(entry -> ItemStack.areEqual(entry.getValue(), itemStack))
				          .findAny()
				          .ifPresent(entry -> superpairsDuplicatedSlots.add(entry.getIntKey()));
				getSlots().put(superpairsPrevClickedSlot, itemStack);
				superpairsCurrentSlot = itemStack;
			}
		}
	}

	@Override
	public List<ColorHighlight> getColors(Int2ObjectMap<ItemStack> displaySlots) {
		List<ColorHighlight> highlights = new ArrayList<>();
		if (getState() == State.SHOW) {
			for (Int2ObjectMap.Entry<ItemStack> indexStack : displaySlots.int2ObjectEntrySet()) {
				int index = indexStack.getIntKey();
				ItemStack displayStack = indexStack.getValue();
				ItemStack stack = getSlots().get(index);
				if (stack != null && !ItemStack.areEqual(stack, displayStack)) {
					if (ItemStack.areEqual(superpairsCurrentSlot, stack) && displayStack.getName().getString().equals("Click a second button!")) {
						highlights.add(ColorHighlight.green(index));
					} else if (superpairsDuplicatedSlots.contains(index)) {
						highlights.add(ColorHighlight.yellow(index));
					} else {
						highlights.add(ColorHighlight.red(index));
					}
				}
			}
		}
		return highlights;
	}
}
