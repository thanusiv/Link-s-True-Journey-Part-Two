import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * This class is used to display the "Controls" screen from the main menu
 */

public class Controls extends BaseScreen {
	public Controls(Game g) {
		super(g);
	}

	public void create() {
		BaseActor background = new BaseActor();
		background.setTexture(new Texture(Gdx.files.internal("Controls/WASD.png")));
		uiStage.addActor(background);

		BitmapFont font = new BitmapFont();
		String text = " Press ESC to go back";
		LabelStyle style = new LabelStyle(font, Color.WHITE);
		Label instructions = new Label(text, style);
		instructions.setFontScale(1.5f);
		instructions.setPosition(15, 430);
		uiStage.addActor(instructions);
	}

	public void update(float dt) {

	}

	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE)
			game.setScreen(new MainMenu(game));

		return false;
	}
}