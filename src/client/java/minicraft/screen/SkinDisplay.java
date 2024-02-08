package minicraft.screen;

import minicraft.core.CrashHandler;
import minicraft.core.Game;
import minicraft.core.Renderer;
import minicraft.core.io.FileHandler;
import minicraft.core.io.InputHandler;
import minicraft.core.io.Localization;
import minicraft.entity.mob.Mob;
import minicraft.gfx.Color;
import minicraft.gfx.Font;
import minicraft.gfx.MinicraftImage;
import minicraft.gfx.Point;
import minicraft.gfx.Screen;
import minicraft.gfx.SpriteLinker.LinkedSprite;
import minicraft.saveload.Save;
import minicraft.screen.entry.ListEntry;
import minicraft.screen.entry.SelectEntry;
import minicraft.util.Logging;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import javax.security.auth.DestroyFailedException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * The skins are put in a folder generated by the game called "skins".
 * Many skins can be put according to the number of files.
 */
public class SkinDisplay extends Display {
	private static final LinkedHashMap<String, LinkedSprite[][]> skins = new LinkedHashMap<>();
	private static final ArrayList<String> defaultSkins = new ArrayList<>();
	private static final MinicraftImage defaultSheet;
	private static final File FOLDER_LOCATION = new File(FileHandler.gameDir + "/skins");
	private static String selectedSkin;

	private int step;
	private WatcherThread thread;

	static {
		// Load the default sprite sheet.
		defaultSheet = Renderer.loadDefaultSkinSheet();

		// These are all the generic skins. To add one, just add an entry in this list.
		defaultSkins.add("minicraft.skin.paul");
		defaultSkins.add("minicraft.skin.paul_cape");
		defaultSkins.add("minicraft.skin.minecraft_steve");
		defaultSkins.add("minicraft.skin.minecraft_alex");
		selectedSkin = defaultSkins.get(0);

		Logging.RESOURCEHANDLER_SKIN.debug("Refreshing skins.");
		refreshSkins();
	}

	public SkinDisplay() {
		super(true, true,
			new Menu.Builder(false, 2, RelPos.CENTER)
				.setDisplayLength(8)
				.setSelectable(true)
				.setPositioning(new Point(Screen.w / 2, Screen.h * 3 / 5), RelPos.CENTER)
				.createMenu()
		);

		thread = new WatcherThread();
		refreshSkins();
		refreshEntries();
		menus[0].setSelection(new ArrayList<>(skins.keySet()).indexOf(selectedSkin));
	}

	public static void refreshSkins() {
		Renderer.spriteLinker.clearSkins();
		skins.clear();

		// Pointing the keys to the default sheet,
		Renderer.spriteLinker.setSkin("skin.minicraft.skin.paul", defaultSheet);
		Renderer.spriteLinker.setSkin("skin.minicraft.skin.paul_cape", defaultSheet);
		Renderer.spriteLinker.setSkin("skin.minicraft.skin.minecraft_steve", defaultSheet);
		Renderer.spriteLinker.setSkin("skin.minicraft.skin.minecraft_alex", defaultSheet);

		skins.put("minicraft.skin.paul", Mob.compileMobSpriteAnimations(0, 0, "skin.minicraft.skin.paul"));
		skins.put("minicraft.skin.paul_cape", Mob.compileMobSpriteAnimations(0, 4, "skin.minicraft.skin.paul_cape"));
		skins.put("minicraft.skin.minecraft_steve", Mob.compileMobSpriteAnimations(0, 8, "skin.minicraft.skin.minecraft_steve"));
		skins.put("minicraft.skin.minecraft_alex", Mob.compileMobSpriteAnimations(0, 12, "skin.minicraft.skin.minecraft_alex"));

		// Create folder, and see if it was successful.
		if (FOLDER_LOCATION.mkdirs()) {
			Logging.RESOURCEHANDLER_SKIN.info("Created skin folder at {}.", FOLDER_LOCATION);
		}

		// Read and add the .png file to the skins list.
		ArrayList<File> files = new ArrayList<>();
		for (String skinPath : Objects.requireNonNull(FOLDER_LOCATION.list())) {
			if (skinPath.endsWith(".png")) {
				files.add(new File(FOLDER_LOCATION, skinPath));
			}
		}

		refreshSkinFiles(files);
	}

	private void refreshEntries() {
		List<ListEntry> l = new ArrayList<>();
		for (String s : skins.keySet()) {
			l.add(new SelectEntry(new Localization.LocalizationString(s.startsWith("minicraft.skin"), s), this::confirmExit) {
				@Override
				public int getColor(boolean isSelected) {
					if (s.equals(selectedSkin)) return isSelected ? Color.GREEN : Color.DIMMED_GREEN;
					return super.getColor(isSelected);
				}
			});
		}

		menus[0].setEntries(l);
		menus[0].setSelection(menus[0].getSelection());
	}

	public static void releaseSkins() {
		for (String skin : new ArrayList<>(skins.keySet())) {
			if (!defaultSkins.contains(skin) && !skin.equals(selectedSkin)) {
				Renderer.spriteLinker.setSkin("skin." + skin, null);
				if (skins.containsKey(skin)) for (LinkedSprite[] a : skins.remove(skin)) {
					for (LinkedSprite b : a) {
						try {
							b.destroy();
						} catch (DestroyFailedException e) {
							Logging.RESOURCEHANDLER_SKIN.trace(e);
						}
					}
				}
			}
		}
	}

	/**
	 * Watching the directory changes. Allowing hot-loading.
	 */
	private class WatcherThread extends Thread {
		private WatchService watcher;
		private volatile Thread running = this;

		WatcherThread() {
			super("Skin File Watcher");
			try {
				watcher = FileSystems.getDefault().newWatchService();
				FOLDER_LOCATION.toPath().register(watcher, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY);
			} catch (IOException e) {
				CrashHandler.errorHandle(e, new CrashHandler.ErrorInfo("Unable to Watch File", CrashHandler.ErrorInfo.ErrorType.UNHANDLEABLE, "Unable to create file water service."));
			}

			start();
			Logging.RESOURCEHANDLER_SKIN.debug("WatcherThread started.");
		}

		@Override
		public void run() {
			while (running == this) {
				try {
					ArrayList<File> files = new ArrayList<>();
					for (WatchEvent<?> event : watcher.take().pollEvents()) {
						if (event.kind() == StandardWatchEventKinds.OVERFLOW)
							continue;

						@SuppressWarnings("unchecked")
						WatchEvent<Path> ev = (WatchEvent<Path>) event;
						Path filename = ev.context();
						files.add(FOLDER_LOCATION.toPath().resolve(filename).toFile());
					}

					if (files.size() > 0) {
						Logging.RESOURCEHANDLER_SKIN.debug("Refreshing resource packs.");
						refreshSkinFiles(files);
						refreshEntries();
					}
				} catch (InterruptedException e) {
					Logging.RESOURCEHANDLER_SKIN.trace("File watcher terminated.");
					return;
				}
			}

			Logging.RESOURCEHANDLER_SKIN.trace("File watcher terminated.");
		}

		public void close() {
			running = null;
		}
	}

	private synchronized static void refreshSkinFiles(List<File> files) {
		for (File file : files) {
			String skinPath = file.getName();
			String name = skinPath.substring(0, skinPath.length() - 4);
			if (file.exists()) try {
				MinicraftImage sheet = new MinicraftImage(ImageIO.read(new FileInputStream(file)), 64, 32);
				Renderer.spriteLinker.setSkin("skin." + name, sheet);
				skins.put(name, Mob.compileMobSpriteAnimations(0, 0, "skin." + name));
			} catch (IOException e) {
				Logging.RESOURCEHANDLER_SKIN.error("Could not read image at path {}. The file is probably missing or formatted wrong.", skinPath);
			} catch (SecurityException e) {
				Logging.RESOURCEHANDLER_SKIN.error("Access to file located at {} was denied. Check if game is given permission.", skinPath);
			}
			else {
				Renderer.spriteLinker.setSkin("skin." + name, null);
				if (skins.containsKey(name)) for (LinkedSprite[] a : skins.remove(name)) {
					for (LinkedSprite b : a) {
						try {
							b.destroy();
						} catch (DestroyFailedException e) {
							Logging.RESOURCEHANDLER_SKIN.trace(e);
						}
					}
				}
			}
		}
	}

	/**
	 * If we exited by selecting a skin.
	 */
	private void confirmExit() {
		Game.exitDisplay();
		selectedSkin = new ArrayList<>(skins.keySet()).get(menus[0].getSelection());

		// Achieve Fashion Show:
		AchievementsDisplay.setAchievement(true, "minicraft.achievement.skin", true);

		// Tell the player to apply changes.
		if (Game.player != null) {
			Game.player.updateSprites();
		}

		// Save the selected skin.
		new Save();
	}

	@Override
	public void onExit() {
		thread.close();
		releaseSkins();
	}

	@Override
	public void tick(InputHandler input) {
		super.tick(input);
	}

	@Override
	public void render(Screen screen) {
		super.render(screen);
		step++;

		// Title.
		Font.drawCentered(Localization.getLocalized("minicraft.displays.skin"), screen, 16, Color.SILVER);

		int xOffset = Screen.w / 2 - 8; // Put this in the center of the screen
		int yOffset = 40; // Player sprite Y position

		int spriteIndex = (step / 40) % 8; // 9 = 8 Frames for sprite

		// Render preview of skin.
		LinkedSprite sprite = new ArrayList<>(skins.values()).get(menus[0].getSelection())[spriteIndex / 2][spriteIndex % 2];
		screen.render(null, xOffset, yOffset, sprite);

		// Help text.
		Font.drawCentered(Localization.getLocalized("minicraft.displays.skin.display.help.select", Game.input.getMapping("SELECT"), Game.input.getMapping("EXIT")), screen, Screen.h - 9, Color.GRAY);
	}

	public static String getSelectedSkin() {
		return selectedSkin;
	}

	public static void setSelectedSkin(String selectedSkin) {
		SkinDisplay.selectedSkin = selectedSkin;
	}

	// First array is one of the four animations.
	@NotNull
	public static LinkedSprite[][][] getSkinAsMobSprite() {
		LinkedSprite[][][] mobSprites = new LinkedSprite[2][][];

		if (!skins.keySet().contains(selectedSkin)) selectedSkin = defaultSkins.get(0);
		if (defaultSkins.contains(selectedSkin)) {
			mobSprites[0] = Mob.compileMobSpriteAnimations(0, defaultSkins.indexOf(selectedSkin) * 4, "skin." + selectedSkin);
			mobSprites[1] = Mob.compileMobSpriteAnimations(0, defaultSkins.indexOf(selectedSkin) * 4 + 2, "skin." + selectedSkin);
		} else {
			mobSprites[0] = Mob.compileMobSpriteAnimations(0, 0, "skin." + selectedSkin);
			mobSprites[1] = Mob.compileMobSpriteAnimations(0, 2, "skin." + selectedSkin);
		}

		return mobSprites;
	}
}
