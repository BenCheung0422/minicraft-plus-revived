//new features, limited comments.
package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.screen.ContainerMenu;
import com.mojang.ld22.screen.OptionsMenu;

public class Chest extends Furniture {
	public Inventory inventory; // Inventory of the chest
	public boolean isdeathchest; // whether this is a death chest (from when the player dies)
	public int time; // time passed (used for death chest despawn)
	public String name; // perhaps for deathchest name?
	// no idea what these are for-- death chests are new!
	int redtick = 0;
	boolean reverse;

	public Chest() {
		super("Chest"); //Name of the chest..?
		
		inventory = new Inventory(this);
		isdeathchest = false;
		time = 0;
		
		// chest colors
		if (canLight()) {
			col0 = Color.get(-1, 220, 331, 552);
			col1 = Color.get(-1, 220, 331, 552);
			col2 = Color.get(-1, 220, 331, 552);
			col3 = Color.get(-1, 220, 331, 552);
		} else {
			col0 = Color.get(-1, 110, 220, 441);
			col1 = Color.get(-1, 220, 331, 552);
			col2 = Color.get(-1, 110, 220, 441);
			col3 = Color.get(-1, 000, 110, 330);
		}
		
		col = Color.get(-1, 220, 331, 552);
		sprite = 1; // Location of the sprite
	}
	
	// for death chest time count, I imagine.
	public void tick() {
		super.tick();
		if (isdeathchest) {
			name = "Death Chest:" + time / 60 + "S";
			if (inventory.invSize() < 1) {
				remove();
			}

			if (time < 3600) {
				name = "Death Chest:" + time / 60 + "S";
				if (!reverse) {
					redtick++;
				} else {
					redtick--;
				}

				if (redtick < 5) {
					col0 = Color.get(-1, 100, 200, 300);
					col1 = Color.get(-1, 100, 200, 300);
					col2 = Color.get(-1, 100, 200, 300);
					col3 = Color.get(-1, 100, 200, 300);
				} else if (redtick > 7 && redtick < 11) {
					col0 = Color.get(-1, 200, 300, 400);
					col1 = Color.get(-1, 200, 300, 400);
					col2 = Color.get(-1, 200, 300, 400);
					col3 = Color.get(-1, 200, 300, 400);
				} else if (redtick > 10) {
					col0 = Color.get(-1, 300, 400, 500);
					col1 = Color.get(-1, 300, 400, 500);
					col2 = Color.get(-1, 300, 400, 500);
					col3 = Color.get(-1, 300, 400, 500);
				}

				if (redtick > 13) {
					reverse = true;
				}

				if (redtick < 0) {
					reverse = false;
				}
			}

			if (time > 0) {
				time--;
			}

			if (time == 0) {
				remove();
			}
		}
	}
	
	// another constructor! must also be for deathchests...
	public Chest(boolean deathchest) {
		super("Death Chest");
		isdeathchest = true;
		if (OptionsMenu.diff == 1) {
			time = '\u8ca0'; // TODO fix this number; play the game and find out how much time it displays.
		} else if (OptionsMenu.diff == 2) {
			time = 18000;
		} else if (OptionsMenu.diff == 3) {
			time = 1200;
		}

		if (canLight()) {
			col0 = Color.get(-1, 220, 331, 552);
			col1 = Color.get(-1, 220, 331, 552);
			col2 = Color.get(-1, 220, 331, 552);
			col3 = Color.get(-1, 220, 331, 552);
		} else {
			col0 = Color.get(-1, 110, 220, 441);
			col1 = Color.get(-1, 220, 331, 552);
			col2 = Color.get(-1, 110, 220, 441);
			col3 = Color.get(-1, 0, 110, 330);
		}

		col = Color.get(-1, 220, 331, 552);
		sprite = 1;
	}
	
	/** This is what occurs when the player uses the "Menu" command near this */
	public boolean use(Player player, int attackDir) {
		player.game.setMenu(new ContainerMenu(player, "Chest", inventory));
		return true;
	}
}
