import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;

/**
 * This class is used to display the "How to Play" screen from the main menu
 *
 */

public class HowToPlay extends BaseScreen {
	public HowToPlay(Game g) {
		super(g);
	}

	public void create() {
		BaseActor background = new BaseActor();
		background.setTexture(new Texture(Gdx.files.internal("HowToPlay/Objective.png")));
		uiStage.addActor(background);
	}

	public void update(float dt) {

	}

	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE)
			game.setScreen(new MainMenu(game));

		return false;
	}
}