import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * This class is used to take keyboard and mouse input from the user
 */

public class FirstPersonCameraController implements InputProcessor {

	private boolean keyPressedA;
	private boolean keyPressedD;
	private boolean keyPressedW;
	private boolean keyPressedS;
	private boolean keyPressedQ;
	private boolean keyPressedE;
	private int dragX, dragY;
	private float rotateSpeed;
	private double movementSpeed;
	private PerspectiveCamera camera;

	FirstPersonCameraController(PerspectiveCamera cam) {
		camera = cam;
		rotateSpeed = 0.2f;
		movementSpeed = 12;
	}

	public boolean getKeyPressedA() {
		return keyPressedA;
	}

	public boolean getKeyPressedD() {
		return keyPressedD;
	}

	public boolean getKeyPressedW() {
		return keyPressedW;
	}

	public boolean getKeyPressedS() {
		return keyPressedS;
	}

	public boolean getKeyPressedQ() {
		return keyPressedQ;
	}

	public boolean getKeyPressedE() {
		return keyPressedE;
	}

	public void setKeyPressedQToFalse() {
		keyPressedQ = false;
	}

	public void setKeyPressedEToFalse() {
		keyPressedE = false;
	}

	public float getRotateSpeed() {
		return rotateSpeed;
	}

	public double getMovementSpeed() {
		return movementSpeed;
	}

	// Source code for the method ifKeyIsPressed is from StackOverflow
	public void ifKeyIsPressed() {

		if (keyPressedA) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.rotate(Vector3.Y, 90);
			v.x *= movementSpeed;
			v.z *= movementSpeed;
			camera.translate(v);
		}

		if (keyPressedD) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.rotate(Vector3.Y, -90);
			v.x *= movementSpeed;
			v.z *= movementSpeed;
			camera.translate(v);
		}

		if (keyPressedS) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.x = -v.x;
			v.z = -v.z;
			v.x *= movementSpeed;
			v.z *= movementSpeed;
			camera.translate(v);
		}

		if (keyPressedW) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.x *= movementSpeed;
			v.z *= movementSpeed;
			camera.translate(v);
		}

	}

	/**
	 * Reverses player's movement if the player's input results in a collision
	 */

	public void reverseMovement() {

		if (keyPressedA) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.rotate(Vector3.Y, -90);
			v.x *= movementSpeed;
			v.z *= movementSpeed;
			camera.translate(v);
		}

		if (keyPressedD) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.rotate(Vector3.Y, 90);
			v.x *= movementSpeed;
			v.z *= movementSpeed;
			camera.translate(v);
		}

		if (keyPressedW) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.x = -v.x;
			v.z = -v.z;
			v.x *= movementSpeed;
			v.z *= movementSpeed;
			camera.translate(v);
		}

		if (keyPressedS) {
			Vector3 v = camera.direction.cpy();
			v.y = 0f;
			v.x *= movementSpeed;
			v.z *= movementSpeed;
			camera.translate(v);
		}
	}

	@Override
	public boolean keyDown(int keycode) {

		if (keycode == Input.Keys.A)
			keyPressedA = true;

		if (keycode == Input.Keys.D)
			keyPressedD = true;

		if (keycode == Input.Keys.W)
			keyPressedW = true;

		if (keycode == Input.Keys.S)
			keyPressedS = true;

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Input.Keys.A)
			keyPressedA = false;

		if (keycode == Input.Keys.D)
			keyPressedD = false;

		if (keycode == Input.Keys.W)
			keyPressedW = false;

		if (keycode == Input.Keys.S)
			keyPressedS = false;

		if (keycode == Input.Keys.Q)
			keyPressedQ = true;

		if (keycode == Input.Keys.E)
			keyPressedE = true;

		return false;
	}

	// Source code for the method mouseMoved is from StackOverflow
	@Override
	public boolean mouseMoved(int screenX, int screenY) {

		// rotating on the y axis
		float x = dragX - screenX;

		camera.rotate(Vector3.Y, x * rotateSpeed);

		// rotating on the x and z axis is different
		float y = (float) Math.sin((double) (dragY - screenY) / 180f);
		if (Math.abs(camera.direction.y + y * (rotateSpeed * 5.0f)) < 0.9) {
			camera.direction.y += y * (rotateSpeed * 5.0f);
		}

		camera.update();

		dragX = screenX;
		dragY = screenY;

		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}