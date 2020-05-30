import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

/**
 * This class is used to display the "Win" screen if the player wins the game
 */

public class WinScreen extends BaseScreen {
	public WinScreen(Game g) {
		super(g);
	}

	public void create() {
		BaseActor background = new BaseActor();
		background.setTexture(new Texture(Gdx.files.internal("Win Screen/Win Screen.png")));
		uiStage.addActor(background);

		BitmapFont font = new BitmapFont();
		String text = " Press ENTER to Restart";
		LabelStyle style = new LabelStyle(font, Color.TEAL);
		Label instructions = new Label(text, style);
		instructions.setFontScale(3);
		instructions.setPosition(80, 30);
		// repeating color pulse effect
		instructions.addAction(Actions.forever(Actions.sequence(Actions.color(new Color(1, 1, 0, 1), 0.5f),
				Actions.delay(0.5f), Actions.color(new Color(0.5f, 0.5f, 0, 1), 0.5f))));
		uiStage.addActor(instructions);
	}

	public void update(float dt) {

	}

	public boolean keyDown(int keycode) {
		if (keycode == Keys.ENTER)
			game.setScreen(new MyGdxGame(game));

		return false;
	}
}