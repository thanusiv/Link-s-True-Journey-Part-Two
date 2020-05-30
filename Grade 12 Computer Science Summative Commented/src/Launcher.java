import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Launches the game and sets the window size
 */

public class Launcher {
	public static void main(String[] args) {
		HorrorGame game = new HorrorGame();
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 900;
		config.width = 1400;
		@SuppressWarnings("unused")
		LwjglApplication launcher = new LwjglApplication(game, config);
	}
}