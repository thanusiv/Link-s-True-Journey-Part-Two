
import java.util.Random;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Array;

/**
 * Loads all the models and creates the map
 */

public class Map {

	private static AssetManager assets;
	private Array<GameObject> instances;
	private Environment environment;
	private btCollisionShape oldHouseShape;
	private btCollisionObject oldHouseCollisionObject[][];
	private btCollisionObject wallHouseCollisionObject[][];
	private int keysCreated = 0;
	private Generator generator;
	private static Array<Key> keys;
	private int wall[][];
	private int oldHouse[][];
	private Random r;
	private int randRow;
	private int randCol;
	private int objectLocations[][];
	private int startingPlayerX;
	private int startingPlayerZ;
	private int startingSpiderX;
	private int startingSpiderZ;

	Map() {

		instances = new Array<GameObject>();
		oldHouseCollisionObject = new btCollisionObject[10][10];
		wallHouseCollisionObject = new btCollisionObject[11][11];
		keys = new Array<Key>();
		wall = new int[][] { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, },
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, }, { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, },
				{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 } };
		oldHouse = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, }, { 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0, },
				{ 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0, }, { 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0, },
				{ 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0, }, { 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0, },
				{ 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0, }, { 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0, },
				{ 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0, }, { 0, 0, 2, 0, 2, 0, 2, 0, 2, 0, 0, },
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
		objectLocations = new int[11][11];

		for (int i = 0; i < 11; i++) {
			for (int g = 0; g < 11; g++) {
				objectLocations[i][g] = 0;
			}
		}

		// These locations cause items to spawn in buildings, so they would not be used
		objectLocations[4][4] = 1;
		objectLocations[2][2] = 1;
		objectLocations[0][0] = 1;
		objectLocations[3][6] = 1;

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.1f, 0.1f, 0.1f, 0.1f));

		assets = new AssetManager();
		// Models taken from free3d
		assets.load("Old House/house.g3db", Model.class);
		assets.load("Spider/Spider_2.g3db", Model.class);
		assets.load("Grenade/Grenade.g3db", Model.class);
		assets.load("Generator/smpgnrtxcxc.g3db", Model.class);
		assets.load("Key/key.g3db", Model.class);

		oldHouseShape = new btBoxShape(new Vector3(990f, 2000f, 600f));
		r = new Random();

		assets.finishLoading();
	}

	public static AssetManager getAssetManager() {
		return assets;
	}

	public Array<GameObject> getInstances() {
		return instances;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public Generator getGenerator() {
		return generator;
	}

	public btCollisionObject[][] getOldHousesCollisionObject() {
		return oldHouseCollisionObject;
	}

	public btCollisionObject[][] getWallHousesCollisionObject() {
		return wallHouseCollisionObject;
	}

	public static Array<Key> getKeys() {
		return keys;
	}

	public int getStartingPlayerX() {
		return startingPlayerX;
	}

	public int getStartingPlayerZ() {
		return startingPlayerZ;
	}

	public int getStartingSpiderX() {
		return startingSpiderX;
	}

	public int getStartingSpiderZ() {
		return startingSpiderZ;
	}

	/**
	 * Creates all the houses on the exterior of the map
	 */

	private void loadWallHouses() {

		Model wallHouse = assets.get("Old House/house.g3db", Model.class);

		for (int i = 0; i < 11; i++) {
			for (int w = 0; w < 11; w++) {

				// Adds a collision object for every model instance
				wallHouseCollisionObject[i][w] = new btCollisionObject();
				wallHouseCollisionObject[i][w].setCollisionShape(oldHouseShape);

				// Checks the array, and creates a house instance at that location
				if (wall[i][w] == 1) {
					String id1 = wallHouse.nodes.get(0).id;
					GameObject wallHouseInstance = new GameObject(wallHouse, id1, false);
					wallHouseInstance.transform.setToTranslation(i * 2000, -100, w * 1200);
					instances.add(wallHouseInstance);
					wallHouseCollisionObject[i][w].setWorldTransform(wallHouseInstance.transform);
				}
			}
		}
	}

	/**
	 * Creates all the houses on the interior of the map
	 */

	private void loadOldHouses() {

		Model house = assets.get("Old House/house.g3db", Model.class);

		for (int i = 0; i < 10; i++) {
			for (int w = 0; w < 10; w++) {

				oldHouseCollisionObject[i][w] = new btCollisionObject();
				oldHouseCollisionObject[i][w].setCollisionShape(oldHouseShape);

				if (oldHouse[i][w] == 2) {

					String id2 = house.nodes.get(0).id;
					GameObject houseInstance = new GameObject(house, id2, false);
					houseInstance.transform.setToTranslation(i * 2400, -100, w * 1200);
					instances.add(houseInstance);
					oldHouseCollisionObject[i][w].setWorldTransform(houseInstance.transform);
				}
			}
		}
	}

	/**
	 * Randomly generates the location of each key, and creates the model instance
	 */

	private void loadKeys() {

		Model keyModel = assets.get("Key/key.g3db", Model.class);
		randRow = r.nextInt(7);
		randCol = r.nextInt(7);

		while (keysCreated < 5) {

			// Checks if there are any other objects in that location, and will be created
			// accordingly
			if (oldHouse[randRow][randCol] == 0 && wall[randRow][randCol] == 0
					&& objectLocations[randRow][randCol] == 0) {

				objectLocations[randRow][randCol] = 1;
				Key keyObject = new Key();
				ModelInstance keyInstance = new ModelInstance(keyModel);
				keyInstance.transform.setToTranslation(randRow * 1800, -300, randCol * 1200);
				keyObject.initializeKey(keyInstance);
				keys.add(keyObject);
				keysCreated++;
				randRow = r.nextInt(7);
				randCol = r.nextInt(7);
			}

			else {
				randRow = r.nextInt(7);
				randCol = r.nextInt(7);
			}
		}
	}

	/**
	 * Randomly generates location of the generator, and creates the model instance
	 */

	private void loadGenerator() {

		Model generatorModel = assets.get("Generator/smpgnrtxcxc.g3db", Model.class);

		randRow = r.nextInt(7);
		randCol = r.nextInt(7);

		while (true) {
			if (oldHouse[randRow][randCol] == 0 && wall[randRow][randCol] == 0
					&& objectLocations[randRow][randCol] == 0) {
				objectLocations[randRow][randCol] = 1;
				generator = new Generator();
				ModelInstance generatorInstance = new ModelInstance(generatorModel);
				generatorInstance.transform.setToTranslation(randRow * 2500, 0, randCol * 1200);
				generator.initializeGenerator(generatorInstance);
				break;
			}

			else {
				randRow = r.nextInt(7);
				randCol = r.nextInt(7);
			}
		}
	}

	/**
	 * Randomly generates the location of the player
	 */

	private void determinePlayerLocation() {

		randRow = r.nextInt(7);
		randCol = r.nextInt(7);

		while (true) {
			if (oldHouse[randRow][randCol] == 0 && wall[randRow][randCol] == 0
					&& objectLocations[randRow][randCol] == 0) {
				objectLocations[randRow][randCol] = 1;
				startingPlayerX = randRow * 2500;
				startingPlayerZ = randCol * 1200;
				break;
			}

			else {
				randRow = r.nextInt(7);
				randCol = r.nextInt(7);
			}
		}

	}

	/**
	 * Randomly generates the location of the spider
	 */

	private void determineSpiderLocation() {

		randRow = r.nextInt(7);
		randCol = r.nextInt(7);

		while (true) {
			if (oldHouse[randRow][randCol] == 0 && wall[randRow][randCol] == 0
					&& objectLocations[randRow][randCol] == 0) {
				objectLocations[randRow][randCol] = 1;
				startingSpiderX = randRow * 2500;
				startingSpiderZ = randCol * 1200;

				break;
			}

			else {
				randRow = r.nextInt(7);
				randCol = r.nextInt(7);
			}
		}
	}

	public void loadWholeMap() {
		loadWallHouses();
		loadOldHouses();
		loadKeys();
		loadGenerator();
		determinePlayerLocation();
		determineSpiderLocation();
	}

	/**
	 * Turns the "lights" on when the generator possesses 5 keys
	 */
	public void lightsOn() {
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1f, 1f, 1f, 1f));
	}

}
