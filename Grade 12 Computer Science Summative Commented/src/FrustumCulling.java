import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

/**
 * This class is used to determine whether a model instance should be rendered
 * or not based on visibility to the player
 */

public class FrustumCulling {

	private Vector3 position;

	FrustumCulling() {
		position = new Vector3();
	}

	// Source code for this method is from Xoppa
	public boolean isVisible(final Camera cam, final GameObject instance) {
		instance.transform.getTranslation(position);
		position.add(instance.getCenter());
		return cam.frustum.sphereInFrustum(position, instance.getRadius());
	}

}
