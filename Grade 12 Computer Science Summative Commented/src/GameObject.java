import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

/**
 * This class is used in conjunction with the FrustumCulling class to determine
 * various characteristics of a model instance
 */

public class GameObject extends ModelInstance {

	// Source code for the fields and constructor are from Xoppa
	private final Vector3 center = new Vector3();
	private final Vector3 dimensions = new Vector3();
	private final float radius;
	private final static BoundingBox bounds = new BoundingBox();

	public GameObject(Model model, String rootNode, boolean mergeTransform) {

		super(model, rootNode, mergeTransform);
		calculateBoundingBox(bounds);
		bounds.getCenter(center);
		bounds.getDimensions(dimensions);
		radius = dimensions.len() / 2f;
	}

	public Vector3 getCenter() {
		return center;
	}

	public Vector3 getDimensions() {
		return dimensions;
	}

	public float getRadius() {
		return radius;
	}

}