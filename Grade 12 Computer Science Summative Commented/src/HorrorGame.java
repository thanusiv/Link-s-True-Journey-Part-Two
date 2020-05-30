import com.badlogic.gdx.Game;

/**
 * Launches the main menu when the game starts
 */

public class HorrorGame extends Game {
	public void create() {
		MainMenu menu = new MainMenu(this);
		setScreen(menu);

	}
}
