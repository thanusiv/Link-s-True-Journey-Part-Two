
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btSphereShape;
import com.badlogic.gdx.utils.Array;

/**
 * Contains all of the player related information
 */

public class Player {

	private btCollisionShape playerShape;
	private btCollisionObject playerCollisionObject;
	private Model playerModel;
	private GameObject playerInstance;
	private PerspectiveCamera camera;
	private FirstPersonCameraController controller;
	private float playerX;
	private float playerZ;
	private boolean canMove;
	private CollisionDetection detect;
	private Array<LandMine> mines;
	private Array<Key> keysPossessed;
	private int minesPlaced;
	private PointLight lantern;

	/**
	 * Creates the collision object for the player and sets the cameras position
	 */

	@SuppressWarnings("deprecation")
	Player(int x, int z) {
		playerX = x;
		playerZ = z;
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(playerX, 7f, playerZ);
		camera.lookAt(87, 27, 30);
		camera.near = 1f;
		camera.far = 10000f;
		camera.update();

		detect = new CollisionDetection();
		controller = new FirstPersonCameraController(camera);
		lantern = new PointLight();

		ModelBuilder mb = new ModelBuilder();
		mb.begin();
		mb.node().id = "player";
		mb.part("player", GL20.GL_TRIANGLES, Usage.Position | Usage.Normal,
				new Material(ColorAttribute.createDiffuse(Color.BLACK))).sphere(20f, 20f, 20f, 100, 100);
		playerModel = mb.end();

		playerInstance = new GameObject(playerModel, "player", false);
		playerShape = new btSphereShape(20f);
		playerCollisionObject = new btCollisionObject();
		playerCollisionObject.setCollisionShape(playerShape);
		playerCollisionObject.setWorldTransform(playerInstance.transform);
		playerInstance.transform.setToTranslation(camera.position.x, camera.position.y - 30, camera.position.z);
		canMove = true;
		mines = new Array<LandMine>();
		keysPossessed = new Array<Key>();
		minesPlaced = 0;
	}

	public btCollisionObject getPlayerCollisionObject() {
		return playerCollisionObject;
	}

	public btCollisionShape getPlayerShape() {
		return playerShape;
	}

	public Model getPlayerModel() {
		return playerModel;
	}

	public GameObject getPlayerInstance() {
		return playerInstance;
	}

	public PerspectiveCamera getCamera() {
		return camera;
	}

	public FirstPersonCameraController getController() {
		return controller;
	}

	public float getPlayerX() {
		return playerX;
	}

	public float getPlayerZ() {
		return playerZ;
	}

	public Array<LandMine> getMines() {
		return mines;
	}

	public Array<Key> getKeysPossessed() {
		return keysPossessed;
	}

	public int getMinesPlaced() {
		return minesPlaced;
	}

	/**
	 * Allows the player to place an additional mine if the spider has hit one
	 */
	public void mineDetonated() {
		minesPlaced--;
	}

	public boolean getCanMove() {
		return canMove;
	}

	public CollisionDetection getDetector() {
		return detect;
	}

	/**
	 * Adds a light to the environment based on the player's position every frame
	 * 
	 * @param map
	 */
	public void addLantern(Map map) {
		lantern.set(0.8f, 0.8f, 0.8f, playerX, getCamera().position.y + 250, playerZ, 80000f);
		map.getEnvironment().add(lantern);
	}

	/**
	 * Removes the light once the screen has been rendered
	 * 
	 * @param map
	 */
	public void removeLantern(Map map) {
		map.getEnvironment().remove(lantern);
	}

	public void preventPlayerMovement() {
		canMove = false;
	}

	public void updatePositions() {
		playerX = camera.position.x;
		playerZ = camera.position.z;
	}

	public void updateCamera() {
		camera.update();
	}

	/**
	 * Checks if the player is colliding with any models and moves the player
	 * accordingly
	 * 
	 * @param map
	 */

	public void movePlayer(Map map) {
		playerInstance.transform.setToTranslation(camera.position.x, camera.position.y - 30, camera.position.z);
		playerCollisionObject.setWorldTransform(playerInstance.transform);

		for (int i = 0; i < 11; i++) {

			for (int y = 0; y < 11; y++) {

				if (detect.checkCollision(map.getWallHousesCollisionObject()[i][y], playerCollisionObject))
					controller.reverseMovement();
			}
		}

		for (int i = 0; i < 10; i++) {

			for (int y = 0; y < 10; y++) {

				if (detect.checkCollision(map.getOldHousesCollisionObject()[i][y], playerCollisionObject))
					controller.reverseMovement();
			}
		}

		if (detect.checkCollision(map.getGenerator().getCollisionObject(), playerCollisionObject)) {
			controller.reverseMovement();
			dispensekeys(map.getGenerator());
		}

		playerInstance.transform.setToTranslation(camera.position.x, camera.position.y - 30, camera.position.z);
		playerCollisionObject.setWorldTransform(playerInstance.transform);
	}

	public void takeControllerInput() {
		if (canMove) {
			controller.ifKeyIsPressed();
			placeMine();
			pickKey();
		}
	}

	/**
	 * Creates a mine when placed
	 */

	private void placeMine() {
		if (minesPlaced < 3) {
			if (controller.getKeyPressedQ()) {
				LandMine mine = new LandMine();
				mines.add(mine);
				ModelInstance mineInstance = new ModelInstance(mines.get(minesPlaced).getMineModel());
				mineInstance.transform.setToTranslation(playerX, -140, playerZ);
				mines.get(minesPlaced).initializeMine(mineInstance);
				minesPlaced++;
			}
		}
		controller.setKeyPressedQToFalse();
	}

	/**
	 * Removes the key and adds it to the player
	 */

	private void pickKey() {

		for (int i = 0; i < Map.getKeys().size; i++) {

			if (detect.checkCollision(playerCollisionObject, Map.getKeys().get(i).getCollisionObject())
					&& controller.getKeyPressedE()) {
				keysPossessed.add(Map.getKeys().get(i));
				Map.getKeys().removeIndex(i);
				Spider.increaseSpiderSpeed();
			}
		}
		controller.setKeyPressedEToFalse();
	}

	/**
	 * Removes the keys from the player and gives it to the generator
	 * 
	 * @param generator
	 */

	private void dispensekeys(Generator generator) {

		if (keysPossessed.size > 0) {
			for (int i = 0; i < keysPossessed.size; i++)
				generator.obtainKeys(keysPossessed.get(i));
			keysPossessed.clear();
		}
	}

	/**
	 * Moves the player for the ending cinematic if the player wins
	 */

	public void endingCinematic() {
		preventPlayerMovement();
		playerInstance.transform.setToTranslation(3000, 2000, 3000);
		camera.position.set(1407, 100f, 1007);
		camera.lookAt(1207, 7, 1007);
	}

}