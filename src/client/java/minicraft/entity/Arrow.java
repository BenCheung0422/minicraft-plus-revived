package minicraft.entity;

import minicraft.entity.mob.Mob;
import minicraft.entity.mob.Player;
import minicraft.gfx.Color;
import minicraft.gfx.Rectangle;
import minicraft.gfx.Screen;
import minicraft.gfx.SpriteLinker.LinkedSprite;
import minicraft.gfx.SpriteLinker.SpriteType;
import minicraft.item.Item;
import minicraft.level.Level;
import minicraft.level.tile.Tile;
import minicraft.util.DamageSource;
import minicraft.util.Logging;
import org.jetbrains.annotations.Nullable;

import javax.security.auth.DestroyFailedException;

import java.util.List;

public class Arrow extends Entity implements ClientTickable {
	private Direction dir;
	private int damage;
	public Mob owner;
	private int speed;
	private LinkedSprite sprite = new LinkedSprite(SpriteType.Entity, "arrow").setSpriteSize(1, 1);

	public Arrow(Mob owner, Direction dir, int dmg) {
		this(owner, owner.x, owner.y, dir, dmg);
	}

	public Arrow(Mob owner, int x, int y, Direction dir, int dmg) {
		super(Math.abs(dir.getX()) + 1, Math.abs(dir.getY()) + 1);
		this.owner = owner;
		this.x = x;
		this.y = y;
		this.dir = dir;

		damage = dmg;
		col = Color.get(-1, 111, 222, 430);

		int xt = 0;
		if (dir == Direction.LEFT) xt = 1;
		if (dir == Direction.UP) xt = 2;
		if (dir == Direction.DOWN) xt = 3;
		sprite.setSpritePos(xt, 0);

		if (damage > 3) speed = 8;
		else if (damage >= 0) speed = 7;
		else speed = 6;
	}

	/**
	 * Generates information about the arrow.
	 * @return string representation of owner, xdir, ydir and damage.
	 */
	public String getData() {
		return owner.eid + ":" + dir.ordinal() + ":" + damage;
	}

	@Override
	public void tick() {
		if (x < 0 || x >> 4 > level.w || y < 0 || y >> 4 > level.h) {
			remove(); // Remove when out of bounds
			return;
		}

		x += dir.getX() * speed;
		y += dir.getY() * speed;

		// TODO I think I can just use the xr yr vars, and the normal system with touchedBy(entity) to detect collisions instead.

		List<Entity> entitylist = level.getEntitiesInRect(new Rectangle(x, y, 0, 0, Rectangle.CENTER_DIMS));
		boolean criticalHit = random.nextInt(11) < 9;
		for (Entity hit : entitylist) {
			if (hit instanceof Mob && hit != owner) {
				Mob mob = (Mob) hit;
				damage += (hit instanceof Player ? 0 : 3) + (criticalHit ? 0 : 1); // Extra damage bonus.
				mob.hurt(new DamageSource.OtherDamageSource(DamageSource.OtherDamageSource.DamageType.ARROW, owner),
					dir, damage); // normal hurting to other mobs
			}

			if (!level.getTile(x >> 4, y >> 4).mayPass(level, x >> 4, y >> 4, this)
				&& !level.getTile(x >> 4, y >> 4).connectsToFluid(level, x >> 4, y >> 4)
				&& level.getTile(x >> 4, y >> 4).id != 16) {
				this.remove();
				try {
					sprite.destroy();
				} catch (DestroyFailedException e) {
					Logging.SPRITE.trace(e);
				}
			}
		}
	}

	public boolean isSolid() {
		return false;
	}

	@Override
	public boolean isAttackable(Entity source, @Nullable Item item, Direction attackDir) {
		return false;
	}

	@Override
	public boolean isAttackable(Tile source, Level level, int x, int y, Direction attackDir) {
		return false;
	}

	@Override
	public boolean isUsable() {
		return false;
	}

	@Override
	protected void handleDamage(DamageSource source, Direction attackDir, int damage) {}

	@Override
	public boolean hurt(DamageSource source, Direction attackDir, int damage) {
		return false;
	}

	@Override
	public void render(Screen screen) {
		screen.render(x - 4, y - 4, sprite);
	}
}
