import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * This class is used to display the main menu screen when the game launches
 */

public class MainMenu extends BaseScreen {
	public MainMenu(Game g) {
		super(g);
	}

	public void create() {
		BaseActor background = new BaseActor();
		background.setTexture(new Texture(Gdx.files.internal("MainMenu/backgroundhouse.png")));
		uiStage.addActor(background);

		BitmapFont font = new BitmapFont();
		String text = " Press ENTER to start";
		LabelStyle style = new LabelStyle(font, Color.GOLD);
		Label instructions = new Label(text, style);
		instructions.setFontScale(1.8f);
		instructions.setPosition(180, 215);
		// repeating color pulse effect
		instructions.addAction(Actions.forever(Actions.sequence(Actions.color(new Color(1, 1, 0, 1), 0.5f),
				Actions.delay(0.5f), Actions.color(new Color(0.5f, 0.5f, 0, 1), 0.5f))));
		uiStage.addActor(instructions);

		BitmapFont font1 = new BitmapFont();
		String text1 = " Press C for the Controls";
		LabelStyle style1 = new LabelStyle(font1, Color.GOLD);
		Label instructions1 = new Label(text1, style1);
		instructions1.setFontScale(1.8f);
		instructions1.setPosition(180, 170);
		// repeating color pulse effect
		instructions1.addAction(Actions.forever(Actions.sequence(Actions.color(new Color(1, 1, 0, 1), 0.5f),
				Actions.delay(0.5f), Actions.color(new Color(0.5f, 0.5f, 0, 1), 0.5f))));
		uiStage.addActor(instructions1);

		BitmapFont font2 = new BitmapFont();
		String text2 = " Press O for the Objective";
		LabelStyle style2 = new LabelStyle(font2, Color.GOLD);
		Label instructions2 = new Label(text2, style2);
		instructions2.setFontScale(1.8f);
		instructions2.setPosition(180, 125);
		// repeating color pulse effect
		instructions2.addAction(Actions.forever(Actions.sequence(Actions.color(new Color(1, 1, 0, 1), 0.5f),
				Actions.delay(0.5f), Actions.color(new Color(0.5f, 0.5f, 0, 1), 0.5f))));
		uiStage.addActor(instructions2);

		String titleString = "Link 's True Journey";
		Label title = new Label(titleString, style);
		title.setFontScale(4);
		title.setPosition(65, 390);
		// repeating color pulse effect
		title.addAction(Actions.forever(Actions.sequence(Actions.color(new Color(1, 1, 0, 1), 0.5f),
				Actions.delay(0.5f), Actions.color(new Color(0.5f, 0.5f, 0, 1), 0.5f))));
		uiStage.addActor(title);

		String titleString2 = "II";
		Label title2 = new Label(titleString2, style);
		title2.setFontScale(5);
		title2.setPosition(300, 290);
		// repeating color pulse effect
		title2.addAction(Actions.forever(Actions.sequence(Actions.color(new Color(1, 1, 0, 1), 0.5f),
				Actions.delay(0.5f), Actions.color(new Color(0.5f, 0.5f, 0, 1), 0.5f))));
		uiStage.addActor(title2);
	}

	public void update(float dt) {

	}

	public boolean keyDown(int keycode) {
		if (keycode == Keys.ENTER)
			game.setScreen(new MyGdxGame(game));

		if (keycode == Keys.C)
			game.setScreen(new Controls(game));

		if (keycode == Keys.O)
			game.setScreen(new HowToPlay(game));

		return false;
	}
}