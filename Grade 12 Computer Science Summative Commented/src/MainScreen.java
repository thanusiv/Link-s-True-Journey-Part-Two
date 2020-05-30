import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * This class is used to display information on the screen for the player to see
 * and to render the screen
 */

public class MainScreen {

	private Stage stage;
	private Label stats;
	private Label keyCollection;
	private Label keysRemaining;
	private BitmapFont font;
	private StringBuilder stringBuilder;
	private int visibleCount;
	private ModelBatch modelBatch;

	MainScreen() {
		stage = new Stage();
		font = new BitmapFont();
		stats = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
		stats.setPosition(0, 870);
		stage.addActor(stats);
		keyCollection = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
		keyCollection.setFontScale(2);
		keyCollection.setPosition(0, 20);
		stage.addActor(keyCollection);
		keysRemaining = new Label(" ", new Label.LabelStyle(font, Color.WHITE));
		keysRemaining.setFontScale(2);
		keysRemaining.setPosition(0, 70);
		stage.addActor(keysRemaining);
		stringBuilder = new StringBuilder();
		visibleCount = 0;
		modelBatch = new ModelBatch();
	}

	public Stage getStage() {
		return stage;
	}

	public Label getLabel() {
		return stats;
	}

	public BitmapFont getFont() {
		return font;
	}

	public StringBuilder getStringBuilder() {
		return stringBuilder;
	}

	public ModelBatch getModelBatch() {
		return modelBatch;
	}

	/**
	 * Displays the text that would be visible on the screen
	 * 
	 * @param player
	 * @param map
	 */
	private void displayText(Player player, Map map) {
		stringBuilder.setLength(0);
		stringBuilder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
		stringBuilder.append(" Visible: ").append(visibleCount);
		stats.setText(stringBuilder);
		stringBuilder.setLength(0);
		stringBuilder.append("Keys Collected: ").append(player.getKeysPossessed().size).append(" (")
				.append(map.getGenerator().keysStored()).append(" Keys in Generator)");
		keyCollection.setText(stringBuilder);
		stringBuilder.setLength(0);
		stringBuilder.append("Keys Remaining: ").append(Map.getKeys().size);
		keysRemaining.setText(stringBuilder);
		stage.draw();
	}

	/**
	 * Renders the various models that would be visible to the player
	 * 
	 * @param player
	 * @param spider
	 * @param map
	 * @param visible
	 */
	
	public void renderScreen(Player player, Spider spider, Map map, FrustumCulling visible) {

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(player.getCamera());

		visibleCount = 0;

		for (final GameObject instance : map.getInstances()) {
			if (visible.isVisible(player.getCamera(), instance)) {
				modelBatch.render(instance, map.getEnvironment());
				visibleCount++;
			}
		}

		modelBatch.render(player.getPlayerInstance(), map.getEnvironment());
		modelBatch.render(spider.getSpiderInstance(), map.getEnvironment());

		for (int i = 0; i < player.getMines().size; i++)
			modelBatch.render(player.getMines().get(i).getModelInstance(), map.getEnvironment());

		for (int i = 0; i < Map.getKeys().size; i++)
			modelBatch.render(Map.getKeys().get(i).getModelInstance(), map.getEnvironment());

		modelBatch.render(map.getGenerator().getModelInstance(), map.getEnvironment());

		modelBatch.end();

		displayText(player, map);
	}
}
