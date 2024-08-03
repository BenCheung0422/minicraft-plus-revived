package minicraft.item;

import minicraft.core.Game;
import minicraft.core.io.Localization;
import minicraft.core.io.Sound;
import minicraft.entity.Direction;
import minicraft.entity.mob.Player;
import minicraft.gfx.SpriteManager;
import minicraft.gfx.SpriteManager.SpriteLink;
import minicraft.gfx.SpriteManager.SpriteType;
import minicraft.level.Level;
import minicraft.level.tile.Tile;
import minicraft.level.tile.Tiles;
import minicraft.screen.AchievementsDisplay;
import minicraft.screen.SignDisplay;
import minicraft.util.AdvancementElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TileItem extends StackableItem {

	protected static ArrayList<Item> getAllInstances() {
		ArrayList<Item> items = new ArrayList<>();

		/// TileItem sprites all have 1x1 sprites.
		items.add(new TileItem("Flower", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "white_flower").createSpriteLink(), new TileModel("flower"), "grass"));
		items.add(new TileItem("Acorn", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "acorn").createSpriteLink(), new TileModel("tree Sapling"), "grass"));
		items.add(new TileItem("Dirt", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "dirt").createSpriteLink(), new TileModel("dirt"), "hole", "water", "lava"));
		items.add(new TileItem("Natural Rock", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "stone").createSpriteLink(), new TileModel("rock"), "hole", "dirt", "sand", "grass", "path", "water", "lava"));

		String[] solidTiles = { "dirt", "Wood Planks", "Stone Bricks", "Obsidian", "Wool", "Red Wool", "Blue Wool",
			"Green Wool", "Yellow Wool", "Black Wool", "grass", "sand", "path", "ornate stone", "ornate obsidian", "Raw Obsidian", "Stone" };
		TileModel.TileDataGetter placeOverWithID = (model1, target, level, xt, yt, player, attackDir) -> target.id;

		items.add(new TileItem("Plank", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "plank").createSpriteLink(), new TileModel("Wood Planks"), "hole", "water", "cloud"));
		items.add(new TileItem("Ornate Wood", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "plank").createSpriteLink(), new TileModel("Ornate Wood"), "hole", "water", "cloud"));
		items.add(new TileItem("Plank Wall", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "plank_wall").createSpriteLink(), new TileModel("Wood Wall"), "Wood Planks"));
		items.add(new TileItem("Wood Door", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "wood_door").createSpriteLink(), new TileModel("Wood Door"), "Wood Planks"));
		items.add(new TileItem("Wood Fence", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "wood_fence").createSpriteLink(), new TileModel("Wood Fence", placeOverWithID), solidTiles));
		items.add(new TileItem("Stone", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "stone").createSpriteLink(), new TileModel("Stone"), "hole", "water", "cloud", "lava"));
		items.add(new TileItem("Stone Brick", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "stone_brick").createSpriteLink(), new TileModel("Stone Bricks"), "hole", "water", "cloud", "lava"));
		items.add(new TileItem("Ornate Stone", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "stone_brick").createSpriteLink(), new TileModel("Ornate Stone"), "hole", "water", "cloud", "lava"));
		items.add(new TileItem("Stone Wall", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "stone_wall").createSpriteLink(), new TileModel("Stone Wall"), "Stone Bricks"));
		items.add(new TileItem("Stone Door", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "stone_door").createSpriteLink(), new TileModel("Stone Door"), "Stone Bricks"));
		items.add(new TileItem("Stone Fence", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "stone_fence").createSpriteLink(), new TileModel("Stone Fence", placeOverWithID), solidTiles));
		items.add(new TileItem("Raw Obsidian", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "obsidian").createSpriteLink(), new TileModel("Raw Obsidian"), "hole", "water", "cloud", "lava"));
		items.add(new TileItem("Obsidian Brick", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "obsidian_brick").createSpriteLink(), new TileModel("Obsidian"), "hole", "water", "cloud", "lava"));
		items.add(new TileItem("Ornate Obsidian", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "obsidian_brick").createSpriteLink(), new TileModel("Ornate Obsidian"), "hole", "water", "cloud", "lava"));
		items.add(new TileItem("Obsidian Wall", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "obsidian_wall").createSpriteLink(), new TileModel("Obsidian Wall"), "Obsidian"));
		items.add(new TileItem("Obsidian Door", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "obsidian_door").createSpriteLink(), new TileModel("Obsidian Door"), "Obsidian"));
		items.add(new TileItem("Obsidian Fence", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "obsidian_fence").createSpriteLink(), new TileModel("Obsidian Fence", placeOverWithID), solidTiles));

		items.add(new TileItem("Wool", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "wool").createSpriteLink(), new TileModel("Wool"), "hole", "water"));
		items.add(new TileItem("Red Wool", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "red_wool").createSpriteLink(), new TileModel("Red Wool"), "hole", "water"));
		items.add(new TileItem("Blue Wool", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "blue_wool").createSpriteLink(), new TileModel("Blue Wool"), "hole", "water"));
		items.add(new TileItem("Green Wool", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "green_wool").createSpriteLink(), new TileModel("Green Wool"), "hole", "water"));
		items.add(new TileItem("Yellow Wool", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "yellow_wool").createSpriteLink(), new TileModel("Yellow Wool"), "hole", "water"));
		items.add(new TileItem("Black Wool", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "black_wool").createSpriteLink(), new TileModel("Black Wool"), "hole", "water"));

		items.add(new TileItem("Sand", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "sand").createSpriteLink(), new TileModel("sand"), "hole", "water", "lava"));
		items.add(new TileItem("Cactus", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "cactus").createSpriteLink(), new TileModel("cactus Sapling"), "sand"));
		items.add(new TileItem("Cloud", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "cloud").createSpriteLink(), new TileModel("cloud"), "Infinite Fall"));

		TileModel.TileDataGetter seedPlanting = (model1, target, level, xt, yt, player, attackDir) -> {
			AchievementsDisplay.setAchievement("minicraft.achievement.plant_seed", true);
			return TileModel.KEEP_DATA.getTileData(model1, target, level, xt, yt, player, attackDir);
		};
		items.add(new TileItem("Wheat Seeds", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "seed").createSpriteLink(), new TileModel("wheat", seedPlanting), "farmland"));
		items.add(new TileItem("Potato", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "potato").createSpriteLink(), new TileModel("potato", TileModel.KEEP_DATA), "farmland"));
		items.add(new TileItem("Carrot", new SpriteManager.SpriteLink.SpriteLinkBuilder(SpriteType.Item, "carrot").createSpriteLink(), new TileModel("carrot", TileModel.KEEP_DATA), "farmland"));
		items.add(new TileItem("Tomato Seeds", new SpriteManager.SpriteLink.SpriteLinkBuilder(SpriteType.Item, "seed").createSpriteLink(), new TileModel("tomato", seedPlanting), "farmland"));
		items.add(new TileItem("Heavenly Berries", new SpriteManager.SpriteLink.SpriteLinkBuilder(SpriteType.Item, "heavenly_berries").createSpriteLink(), new TileModel("heavenly berries", TileModel.KEEP_DATA), "farmland"));
		items.add(new TileItem("Hellish Berries", new SpriteManager.SpriteLink.SpriteLinkBuilder(SpriteType.Item, "hellish_berries").createSpriteLink(), new TileModel("hellish berries", TileModel.KEEP_DATA), "farmland"));
		items.add(new TileItem("Grass Seeds", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "seed").createSpriteLink(), new TileModel("grass"), "dirt"));

		items.add(new TileItem("Torch", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "torch").createSpriteLink(), new TileModel("Torch", placeOverWithID), solidTiles));
		items.add(new TileItem("Sign", new SpriteLink.SpriteLinkBuilder(SpriteType.Item, "sign").createSpriteLink(), new TileModel("Sign", (model1, target, level, xt, yt, player, attackDir) -> {
			Game.setDisplay(new SignDisplay(level, xt, yt));
			return placeOverWithID.getTileData(model1, target, level, xt, yt, player, attackDir);
		}), solidTiles));

		// Creative mode available tiles:
		items.add(new TileItem("Farmland", SpriteManager.missingTexture(SpriteType.Item), new TileModel("farmland"), "dirt", "grass", "hole"));
		items.add(new TileItem("Hole", SpriteManager.missingTexture(SpriteType.Item), new TileModel("hole"), "dirt", "grass"));
		items.add(new TileItem("Lava", SpriteManager.missingTexture(SpriteType.Item), new TileModel("lava"), "dirt", "grass", "hole"));
		items.add(new TileItem("Path", SpriteManager.missingTexture(SpriteType.Item), new TileModel("path"), "dirt", "grass", "hole"));
		items.add(new TileItem("Water", SpriteManager.missingTexture(SpriteType.Item), new TileModel("water"), "dirt", "grass", "hole"));

		return items;
	}

	public final @Nullable TileModel model;
	public final List<String> validTiles;

	protected TileItem(String name, SpriteLink sprite, TileModel model, String... validTiles) {
		this(name, sprite, 1, model, Arrays.asList(validTiles));
	}

	protected TileItem(String name, SpriteLink sprite, int count, TileModel model, String... validTiles) {
		this(name, sprite, count, model, Arrays.asList(validTiles));
	}

	protected TileItem(String name, SpriteLink sprite, int count, @Nullable TileModel model, List<String> validTiles) {
		super(name, sprite, count);
		this.model = model;
		this.validTiles = new ArrayList<>();
		for (String tile : validTiles)
			this.validTiles.add(tile.toUpperCase());
	}

	public static class TileModel {
		public static final TileDataGetter DEFAULT_DATA = ((model, target, level, xt, yt, player, attackDir) -> model.getDefaultData());
		public static final TileDataGetter KEEP_DATA = ((model, target, level, xt, yt, player, attackDir) -> level.getData(xt, yt));

		public final @NotNull String tile;
		public final TileDataGetter data;

		@FunctionalInterface
		public interface TileDataGetter {
			int getTileData(Tile model, Tile target, Level level, int xt, int yt, Player player, Direction attackDir);
		}

		public TileModel(String tile) {
			this(tile, DEFAULT_DATA);
		}

		public TileModel(String tile, TileDataGetter data) {
			this.tile = tile.toUpperCase();
			this.data = data;
		}

		public static Tile getTile(@Nullable TileModel model) {
			return model == null ? Tiles.get((short) 0) : Tiles.get(model.tile);
		}

		public static int getTileData(@Nullable TileModel model, Tile tile, Tile target, Level level, int xt, int yt, Player player, Direction attackDir) {
			if (model == null) return DEFAULT_DATA.getTileData(tile, target, level, xt, yt, player, attackDir);
			return model.data.getTileData(tile, target, level, xt, yt, player, attackDir);
		}
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, Direction attackDir) {
		for (String tilename : validTiles) {
			if (tile.matches(level.getData(xt, yt), tilename)) {
				Tile t = TileModel.getTile(model);
				level.setTile(xt, yt, t, TileModel.getTileData(model, t, tile, level, xt, yt, player, attackDir));
				AdvancementElement.AdvancementTrigger.PlacedTileTrigger.INSTANCE.trigger(
					new AdvancementElement.AdvancementTrigger.PlacedTileTrigger.PlacedTileTriggerConditionHandler.PlacedTileTriggerConditions(
						this, level.getTile(xt, yt), level.getData(xt, yt), xt, yt, level.depth
					));

				Sound.play("craft");
				return super.interactOn(true);
			}
		}

		Logger.tag("TileItem").debug("{} cannot be placed on {}.", model, tile.name);

		if (model != null) {
			String note = "";
			if (model.tile.contains("WALL")) {
				note = Localization.getLocalized("minicraft.notification.invalid_placement", Tiles.getName(validTiles.get(0)));
			} else if (model.tile.contains("DOOR")) {
				note = Localization.getLocalized("minicraft.notification.invalid_placement", Tiles.getName(validTiles.get(0)));
			} else if ((model.tile.contains("BRICK") || model.tile.contains("PLANK") || model.tile.equals("STONE") || model.tile.contains("ORNATE"))) {
				note = Localization.getLocalized("minicraft.notification.dig_hole");
			}

			if (note.length() > 0) {
				Game.notifications.add(note);
			}
		}

		return super.interactOn(false);
	}

	@Override
	public boolean equals(Item other) {
		return super.equals(other) && (model == null || model.equals(((TileItem) other).model));
	}

	@Override
	public int hashCode() {
		return super.hashCode() + (model == null ? 0xFF123 : model.hashCode());
	}

	public @NotNull TileItem copy() {
		return new TileItem(getName(), sprite, count, model, validTiles);
	}
}
