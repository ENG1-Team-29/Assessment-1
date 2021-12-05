package tk.shardsoftware.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.badlogic.gdx.math.Rectangle;

/** @author James Burnell */
public class EntityTest {

	/** Test {@link Entity#setPosition(float, float)} method */
	@Test
	public void testSetPosition() {
		Entity e = new Entity();
		assertEquals(0, e.positionVec.x);
		assertEquals(0, e.positionVec.y);
		e.setPosition(5, 7);
		assertEquals(5, e.positionVec.x);
		assertEquals(7, e.positionVec.y);
	}

	/**
	 * Test that the entity hitbox updates when the entity position is updated
	 */
	@Test
	public void testHitboxUpdate() {
		Entity e = new Entity();
		assertEquals(new Rectangle(), e.hitbox);
		e.setPosition(5, 7);
		assertEquals(new Rectangle(5, 7, 0, 0), e.hitbox);
	}

	/** Test that the position and hitbox stay synchronized */
	@Test
	public void testSetPositionAndHitboxMultiple() {
		Entity e = new Entity();
		for (int x = 0; x < 10; x++) {
			for (int y = 0; y < 10; y++) {
				e.setPosition(x, y);
				assertEquals(x, e.positionVec.x);
				assertEquals(y, e.positionVec.y);
				assertEquals(new Rectangle(x, y, 0, 0), e.hitbox);
			}
		}
	}

	/** Test that the direction stays within 0-360 range */
	@Test
	public void testDirectionWithinRange() {
		Entity e = new Entity();
		e.setDirection(0);
		assertEquals(0, e.direction);
		e.setDirection(180);
		assertEquals(180, e.direction);
		e.setDirection(360);
		assertEquals(0, e.direction);
		e.setDirection(-360);
		assertEquals(0, Math.abs(e.direction));
		e.setDirection(-180);
		assertEquals(180, e.direction);
	}

	/** Test the {@link Entity#update(float)} method */
	@Test
	public void testLogicStep() {
		Entity e = new Entity(5, 5, 10, 10);
		e.setVelocity(1, 1);
		assertEquals(5, e.positionVec.x);
		assertEquals(5, e.positionVec.y);
		e.update(1);
		assertEquals(6, e.positionVec.x);
		assertEquals(6, e.positionVec.y);
		// verify hitbox is in sync
		assertEquals(6, e.hitbox.x);
		assertEquals(6, e.hitbox.y);
	}

	/**
	 * Test that entities can detect collisions with other entities when moving
	 */
	@Test
	public void testCollision() {
		Entity e1 = new Entity(5, 5, 10, 10);
		Entity e2 = new Entity(20, 5, 10, 10);
		assertFalse(e1.hitbox.overlaps(e2.hitbox));
		e1.setVelocity(3, 3);
		e1.update(1);
		assertFalse(e1.hitbox.overlaps(e2.hitbox));
		e1.update(1);
		assertTrue(e1.hitbox.overlaps(e2.hitbox));
	}

}
