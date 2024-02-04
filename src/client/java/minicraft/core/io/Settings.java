package minicraft.core.io;

import minicraft.screen.entry.ArrayEntry;
import minicraft.screen.entry.BooleanEntry;
import minicraft.screen.entry.RangeEntry;

import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.util.HashMap;

public final class Settings {

	private static final HashMap<String, ArrayEntry<?>> options = new HashMap<>();

	static {
		options.put("fps", new RangeEntry(new Localization.LocalizationString("minicraft.settings.fps"), 10, 300, getDefaultRefreshRate())); // Has to check if the game is running in a headless mode. If it doesn't set the fps to 60
		options.put("screenshot", new ArrayEntry<>(new Localization.LocalizationString("minicraft.settings.screenshot_scale"), 1, 2, 5, 10, 15, 20)); // The magnification of screenshot. I would want to see ultimate sized.
		options.put("diff", new ArrayEntry<>(new Localization.LocalizationString("minicraft.displays.world_gen.options.difficulty"), "minicraft.displays.world_gen.options.difficulty.easy", "minicraft.displays.world_gen.options.difficulty.normal", "minicraft.displays.world_gen.options.difficulty.hard"));
		options.get("diff").setSelection(1);
		options.put("mode", new ArrayEntry<>(new Localization.LocalizationString("minicraft.displays.world_gen.options.game_mode"), "minicraft.displays.world_gen.options.game_mode.survival", "minicraft.displays.world_gen.options.game_mode.creative", "minicraft.displays.world_gen.options.game_mode.hardcore", "minicraft.displays.world_gen.options.game_mode.score"));

		options.put("scoretime", new ArrayEntry<>(new Localization.LocalizationString("minicraft.displays.world_gen.options.score_time"), 10, 20, 40, 60, 120));
		options.get("scoretime").setValueVisibility(10, false);
		options.get("scoretime").setValueVisibility(120, false);

		options.put("sound", new BooleanEntry(new Localization.LocalizationString("minicraft.settings.sound"), true));
		options.put("autosave", new BooleanEntry(new Localization.LocalizationString("minicraft.settings.autosave"), true));

		options.put("size", new ArrayEntry<>(new Localization.LocalizationString("minicraft.displays.world_gen.options.world_size"), 128, 256, 512));
		options.put("theme", new ArrayEntry<>(new Localization.LocalizationString("minicraft.displays.world_gen.options.theme"), "minicraft.displays.world_gen.options.theme.normal", "minicraft.displays.world_gen.options.theme.forest", "minicraft.displays.world_gen.options.theme.desert", "minicraft.displays.world_gen.options.theme.plain", "minicraft.displays.world_gen.options.theme.hell"));
		options.put("type", new ArrayEntry<>(new Localization.LocalizationString("minicraft.displays.world_gen.options.terrain_type"), "minicraft.displays.world_gen.options.terrain_type.island", "minicraft.displays.world_gen.options.terrain_type.box", "minicraft.displays.world_gen.options.terrain_type.mountain", "minicraft.displays.world_gen.options.terrain_type.irregular"));

		// TODO localize these labels
		options.put("tutorials", new BooleanEntry(new Localization.LocalizationString("minicraft.displays.world_gen.options.tutorials"), false));
		options.put("quests", new BooleanEntry(new Localization.LocalizationString("minicraft.displays.world_gen.options.quests"), false));
		options.put("showquests", new BooleanEntry(new Localization.LocalizationString("Quests Panel"), true));

		options.get("mode").setChangeAction(value ->
			options.get("scoretime").setVisible("minicraft.displays.world_gen.options.game_mode.score".equals(value))
		);
	}

	/**
	 * Returns the value of the specified option.
	 *
	 * @param option The setting to get.
	 * @return The value of the setting
	 */
	public static Object get(String option) {
		return options.get(option.toLowerCase()).getValue();
	}

	/**
	 * Returns the index of the value in the list of values for the specified option.
	 *
	 * @param option The setting to get.
	 * @return The index of the setting.
	 */
	public static int getIdx(String option) {
		return options.get(option.toLowerCase()).getSelection();
	}

	/**
	 * Return the ArrayEntry object associated with the given option name.
	 *
	 * @param option The setting to get.
	 * @return The ArrayEntry.
	 */
	public static ArrayEntry<?> getEntry(String option) {
		return options.get(option.toLowerCase());
	}

	/**
	 * Sets the value of the given option name, to the given value, provided it is a valid value for that option.
	 *
	 * @param option The setting to edit.
	 * @param value  The value to change to.
	 */
	public static void set(String option, Object value) {
		options.get(option.toLowerCase()).setValue(value);
	}

	/**
	 * Sets the index of the value of the given option, provided it is a valid index.
	 *
	 * @param option The setting to edit.
	 * @param idx    Index to select.
	 */
	public static void setIdx(String option, int idx) {
		options.get(option.toLowerCase()).setSelection(idx);
	}

	/**
	 * Gets the refresh rate of the default monitor.
	 * Safely handles headless environments (if that were to happen for some reason).
	 *
	 * @return The refresh rate if successful. 60 if not.
	 */
	private static int getDefaultRefreshRate() {
		int hz;
		try {
			hz = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode().getRefreshRate();
		} catch (HeadlessException e) {
			return 60;
		}

		if (hz == DisplayMode.REFRESH_RATE_UNKNOWN) return 60;
		if (hz > 300 || 10 >= hz) return 60;
		return hz;
	}
}
