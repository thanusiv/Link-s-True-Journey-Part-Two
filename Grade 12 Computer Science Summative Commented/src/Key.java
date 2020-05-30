import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

/**
 * Contains sensitive information for an individual key
 */

public class Key {

	private Model key;
	private ModelInstance keyInstance;
	private btCollisionShape keyShape;
	private btCollisionObject keyCollisionObject;

	Key() {
		key = Map.getAssetManager().get("Key/key.g3db", Model.class);
		keyShape = new btBoxShape(new Vector3(100f, 500f, 100f));
		keyInstance = new ModelInstance(key);
		keyCollisionObject = new btCollisionObject();
	}

	public Model getkeyModel() {
		return key;
	}

	public ModelInstance getModelInstance() {
		return keyInstance;
	}

	public btCollisionObject getCollisionObject() {
		return keyCollisionObject;
	}

	/**
	 * Sets location and collision shape for the key instance
	 * 
	 * @param key
	 */

	public void initializeKey(ModelInstance key) {
		keyInstance = key;
		keyCollisionObject.setCollisionShape(keyShape);
		keyCollisionObject.setWorldTransform(key.transform);
	}

}
