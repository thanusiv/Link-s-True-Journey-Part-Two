import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

/**
 * This class holds all mine related information
 */

public class LandMine {

	private Model mine;
	private ModelInstance mineInstance;
	private btCollisionShape mineShape;
	private btCollisionObject mineCollisionObject;

	LandMine() {
		mine = Map.getAssetManager().get("Grenade/Grenade.g3db", Model.class);
		mineShape = new btBoxShape(new Vector3(5f, 5f, 5f));
		mineInstance = new ModelInstance(mine);
		mineCollisionObject = new btCollisionObject();
	}

	public Model getMineModel() {
		return mine;
	}

	public ModelInstance getModelInstance() {
		return mineInstance;
	}

	public btCollisionObject getCollisionObjects() {
		return mineCollisionObject;
	}

	/**
	 * Sets location and collision shape for the mine instance
	 * 
	 * @param mine
	 */

	public void initializeMine(ModelInstance mine) {
		mineInstance = mine;
		mineCollisionObject.setCollisionShape(mineShape);
		mineCollisionObject.setWorldTransform(mine.transform);
	}

}
