
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.utils.Array;

/**
 * Contains all the generator's sensitive information
 */

public class Generator {

	private Model generator;
	private ModelInstance generatorInstance;
	private btCollisionShape generatorShape;
	private btCollisionObject generatorCollisionObject;
	private Array<Key> keysDispensed;

	Generator() {
		generator = Map.getAssetManager().get("Generator/smpgnrtxcxc.g3db", Model.class);
		generatorShape = new btBoxShape(new Vector3(300f, 500f, 450f));
		generatorInstance = new ModelInstance(generator);
		generatorCollisionObject = new btCollisionObject();
		keysDispensed = new Array<Key>();
	}

	public Model getGeneratorModel() {
		return generator;
	}

	public ModelInstance getModelInstance() {
		return generatorInstance;
	}

	public btCollisionObject getCollisionObject() {
		return generatorCollisionObject;
	}

	public int keysStored() {
		return keysDispensed.size;
	}

	/**
	 * Takes keys from the player and stores it in the generator
	 * 
	 * @param key
	 */
	public void obtainKeys(Key key) {
		keysDispensed.add(key);
	}

	/**
	 * Sets location and collision shape for the generator instance
	 * 
	 * @param generator
	 */

	public void initializeGenerator(ModelInstance generator) {
		generatorInstance = generator;
		generatorCollisionObject.setCollisionShape(generatorShape);
		generatorCollisionObject.setWorldTransform(generator.transform);
	}

	/**
	 * If the generator possesses 5 keys, it will light up the abandoned town
	 */

	public boolean turnsOn() {
		if (keysDispensed.size == 5)
			return true;
		else
			return false;
	}

}
