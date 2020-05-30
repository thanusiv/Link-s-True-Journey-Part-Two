import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

/**
 * Contains all the spider related information
 */

public class Spider {

	private Model spider;
	private ModelInstance spiderInstance;
	private AnimationController animationController;
	private btCollisionShape spiderShape;
	private btCollisionObject spiderCollisionObject;
	private float spiderX;
	private float spiderY;
	private float spiderZ;

	private enum State {
		TRACKING, STUNNED, DEAD, ATTACKING, MOTIONLESS
	};

	private State state;
	private int grenadeDetonated;
	private static float spiderSpeed;
	private float currentIntercept;
	private int xIncrease;
	private int xDecrease;
	private int zIncrease;
	private int zDecrease;
	private boolean dead;
	private boolean caughtPlayer;
	private CollisionDetection detect;
	// This boolean is required to prevent methods from being called more than once
	private boolean gotCalled;

	Spider(int x, int z) {

		spider = Map.getAssetManager().get("Spider/Spider_2.g3db", Model.class);
		spiderInstance = new ModelInstance(spider);
		spiderShape = new btBoxShape(new Vector3(100f, 100f, 100f));
		spiderCollisionObject = new btCollisionObject();
		spiderCollisionObject.setCollisionShape(spiderShape);
		spiderCollisionObject.setWorldTransform(spiderInstance.transform);
		detect = new CollisionDetection();

		spiderX = x;
		spiderY = -122f;
		spiderZ = z;

		spiderInstance.transform.setToTranslation(spiderX, spiderY, spiderZ);
		spiderInstance.transform.rotate(Vector3.Y, 65);
		spiderSpeed = 13f;
		state = State.TRACKING;
		xIncrease = 0;
		xDecrease = 0;
		zIncrease = 0;
		zDecrease = 0;

		dead = false;
		caughtPlayer = false;
		gotCalled = false;
		grenadeDetonated = -1;

		animationController = new AnimationController(spiderInstance);
		animationController.setAnimation("run_ani_vor", -1);
	}

	public ModelInstance getSpiderInstance() {
		return spiderInstance;
	}

	public Model getSpiderModel() {
		return spider;
	}

	public AnimationController getAnimationController() {
		return animationController;
	}

	public btCollisionObject getSpiderCollisionObject() {
		return spiderCollisionObject;
	}

	public btCollisionShape getSpiderShape() {
		return spiderShape;
	}

	public float getSpiderX() {
		return spiderX;
	}

	public float getSpiderY() {
		return spiderY;
	}

	public float getSpiderZ() {
		return spiderZ;
	}

	public boolean checkMethodCall() {
		return gotCalled;
	}

	public void gotCalled() {
		gotCalled = true;
	}

	public boolean getIsDead() {
		return dead;
	}

	public void killed() {
		state = State.DEAD;
		dead = true;
	}

	public void becomesMotionless() {
		state = State.MOTIONLESS;
	}

	public boolean checkSpiderCaughtPlayer() {
		return caughtPlayer;
	}

	public State getSpiderState() {
		return state;
	}

	public CollisionDetection getDetector() {
		return detect;
	}

	/**
	 * Increases the spider's speed everytime the player picks up a key
	 */
	public static void increaseSpiderSpeed() {
		spiderSpeed++;
	}

	public void updateAnimation() {
		animationController.update(Gdx.graphics.getDeltaTime());
	}

	/**
	 * Calculates the intercept for the path the spider should take everytime it
	 * turns
	 * 
	 * @return float
	 */
	private float calculateInterceptZ() {
		return (float) (spiderZ - (0.42766 * spiderX));
	}

	private float calculateInterceptX() {
		return (float) (spiderX + (0.42766 * spiderZ));
	}

	/**
	 * Decreases the spider's x-coordinate based on the slope
	 */

	private void xDecrease() {

		if (xDecrease == 1) // Checks if the method was called once
			currentIntercept = calculateInterceptZ();

		spiderX = spiderX - spiderSpeed;
		spiderZ = (float) (0.42766 * spiderX + currentIntercept);

		spiderInstance.transform.setToTranslation(spiderX, spiderY, spiderZ);
		spiderInstance.transform.rotate(Vector3.Y, 65);
		spiderCollisionObject.setWorldTransform(spiderInstance.transform);
	}

	/**
	 * Increases the spider's x-coordinate based on the slope
	 */

	private void xIncrease() {

		if (xIncrease == 1)
			currentIntercept = calculateInterceptZ();

		spiderX = spiderX + spiderSpeed;
		spiderZ = (float) (0.42766 * spiderX + currentIntercept);

		spiderInstance.transform.setToTranslation(spiderX, spiderY, spiderZ);
		spiderInstance.transform.rotate(Vector3.Y, -110);
		spiderCollisionObject.setWorldTransform(spiderInstance.transform);
	}

	/**
	 * Increases the spider's z-coordinate based on the slope
	 */

	private void zIncrease() {

		if (zIncrease == 1)
			currentIntercept = calculateInterceptX();

		spiderZ = spiderZ + spiderSpeed;
		spiderX = (float) (-0.42766 * spiderZ + currentIntercept);

		spiderInstance.transform.setToTranslation(spiderX, spiderY, spiderZ);
		spiderInstance.transform.rotate(Vector3.Y, -210);
		spiderCollisionObject.setWorldTransform(spiderInstance.transform);
	}

	/**
	 * Decreases the spider's z-coordinate based on the slope
	 */

	private void zDecrease() {

		if (zDecrease == 1)
			currentIntercept = calculateInterceptX();

		spiderZ = spiderZ - spiderSpeed;
		spiderX = (float) (-0.42766 * spiderZ + currentIntercept);

		spiderInstance.transform.setToTranslation(spiderX, spiderY, spiderZ);
		spiderInstance.transform.rotate(Vector3.Y, -20);
		spiderCollisionObject.setWorldTransform(spiderInstance.transform);
	}

	/**
	 * Based on the spider's proximity to the player and their mines, it will call
	 * the appropriate method
	 */

	public void track(Player player) {

		if (detect.checkCollision(spiderCollisionObject, player.getPlayerCollisionObject()))
			state = State.ATTACKING;

		for (int i = 0; i < player.getMines().size; i++) {
			if (detect.checkCollision(spiderCollisionObject, player.getMines().get(i).getCollisionObjects())) {
				state = State.STUNNED;
				grenadeDetonated = i;
				gotCalled = true;
				break;
			}
		}

		switch (state) {

		case ATTACKING:
			attack(player);
			break;

		case STUNNED:
			hitsMine(player, grenadeDetonated);
			break;

		case TRACKING:

			if (player.getPlayerZ() + 80 < spiderZ && xIncrease == 0) {
				xDecrease = 0;
				xIncrease = 0;
				zDecrease++;
				zIncrease = 0;
				zDecrease();
			}

			else if (player.getPlayerX() + 80 < spiderX) {
				xDecrease++;
				xIncrease = 0;
				zDecrease = 0;
				zIncrease = 0;
				xDecrease();
			}

			else if (player.getPlayerZ() - 80 > spiderZ) {
				xDecrease = 0;
				xIncrease = 0;
				zDecrease = 0;
				zIncrease++;
				zIncrease();
			}

			else if (player.getPlayerX() - 80 > spiderX) {
				xDecrease = 0;
				xIncrease++;
				zDecrease = 0;
				zIncrease = 0;
				xIncrease();
			}

			else
				xIncrease();
			break;

		case DEAD:
			dies();
			break;

		case MOTIONLESS:
			animationController.setAnimation("run_ani_vor", 1);
			break;

		}
	}

	/**
	 * Makes the spider attack if it collides with player
	 * 
	 * @param player
	 */

	private void attack(Player player) {
		player.preventPlayerMovement();
		animationController.setAnimation("Attack", -1);
		player.getCamera().position.set(player.getPlayerX(), -80f, player.getPlayerZ());
		player.getCamera().lookAt(spiderX, 7, spiderZ);

		Timer.schedule(new Task() {
			@Override
			public void run() {
				caughtPlayer = true;
			}
		}, 4);
	}

	/**
	 * Stuns the spider if it collides with a mine
	 * 
	 * @param player
	 * @param grenadeDetonated
	 */

	private void hitsMine(Player player, int grenadeDetonanted) {
		if (gotCalled) {
			player.mineDetonated();

			animationController.setAnimation("die", 1, 1, new AnimationController.AnimationListener() {
				@Override
				public void onEnd(AnimationController.AnimationDesc animation) {
					state = State.TRACKING;
					animationController.setAnimation("run_ani_vor", -1);
				}

				@Override
				public void onLoop(AnimationController.AnimationDesc animation) {
				}
			});

			player.getMines().removeIndex(grenadeDetonated);
			gotCalled = false;
		}
	}

	/**
	 * Called when the player wins the game
	 */

	public void dies() {

		state = State.DEAD;
		spiderInstance.transform.setToTranslation(1207, spiderY, 1007);
		animationController.setAnimation("die", 1, 0.5f, new AnimationController.AnimationListener() {
			@Override
			public void onEnd(AnimationController.AnimationDesc animation) {
			}

			@Override
			public void onLoop(AnimationController.AnimationDesc animation) {
			}
		});
	}

}
