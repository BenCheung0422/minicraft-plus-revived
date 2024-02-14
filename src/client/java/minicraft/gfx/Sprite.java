package minicraft.gfx;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * This class represents a group of pixels on their sprite sheet(s).
 */
public class Sprite {
	/**
	 * This class needs to store a list of similar segments that make up a sprite, just once for everything. There's usually four groups, but the components are:
	 * -spritesheet location (x, y)
	 * -mirror type
	 * <p>
	 * That's it!
	 * The screen's render method only draws one 8x8 pixel of the spritesheet at a time, so the "sprite size" will be determined by how many repetitions of the above group there are.
	 */

	public Px[][] spritePixels;
	public int color = -1;
	// spritePixels is arranged so that the pixels are in their correct positions relative to the top left of the full sprite. This means that their render positions are built-in to the array.

	public Sprite(Px[][] pixels) {
		spritePixels = pixels;
	}

	public String toString() {
		StringBuilder out = new StringBuilder(getClass().getName().replace("minicraft.gfx.", "") + "; pixels:");
		for (Px[] row : spritePixels)
			for (Px pixel : row)
				out.append("\n").append(pixel.toString());
		out.append("\n");

		return out.toString();
	}

	/**
	 * This class represents a pixel on the sprite sheet.
	 */
	public static class Px {
		protected final int x, y, mirror;
		protected final BufferedImage cell;

		public Px(int sheetX, int sheetY, int mirroring, MinicraftImage sheet) {
			// pixelX and pixelY are the relative positions each pixel should have relative to the top-left-most pixel of the sprite.
			x = sheetX;
			y = sheetY;
			mirror = mirroring;
			cell = new BufferedImage(8, 8, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = cell.createGraphics();
			g.drawImage(sheet.image,
				0, 0, 8, 8, x * 8, y * 8, x * 8 + 8, y * 8 + 8, null);
		}

		public String toString() {
			return "SpritePixel:x=" + x + ";y=" + y + ";mirror=" + mirror;
		}
	}
}
