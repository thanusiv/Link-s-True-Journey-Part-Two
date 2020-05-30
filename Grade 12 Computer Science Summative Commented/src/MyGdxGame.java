import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MyGdxGame extends BaseScreen {

	Map map;
	Player player;
	Spider spider;
	FrustumCulling visible;
	MainScreen screen;

	public MyGdxGame(Game g) {
		super(g);
	}

	@Override
	public void create() {
		Bullet.init();

		map = new Map();
		map.loadWholeMap();
		player = new Player(map.getStartingPlayerX(), map.getStartingPlayerZ());
		spider = new Spider(map.getStartingSpiderX(), map.getStartingSpiderZ());
		visible = new FrustumCulling();
		screen = new MainScreen();

		Gdx.input.setInputProcessor(player.getController());
	}

	@Override
	public void render(float arg0) {
		Gdx.input.setCursorCatched(true);

		player.takeControllerInput();

		// This will run the ending cinematic once if the spider is dead
		if (spider.getIsDead() && !spider.checkMethodCall()) {
			spider.dies();
			spider.gotCalled();
			Timer.schedule(new Task() { // Timer from StackOverflow
				@Override
				public void run() {
					game.setScreen(new WinScreen(game));
				}
			}, 6);
		} else
			player.movePlayer(map);

		if (!spider.getIsDead()) {

			// Checks if the generator can turn on or not
			if (map.getGenerator().turnsOn()) {
				spider.becomesMotionless();
				map.lightsOn();

				Timer.schedule(new Task() {
					@Override
					public void run() {
						spider.killed();
						player.endingCinematic();
					}
				}, 5);

			}
		}

		player.updatePositions();
		player.addLantern(map);
		spider.track(player);
		
		if (spider.checkSpiderCaughtPlayer())
			game.setScreen(new GameOver(game));
		
		spider.updateAnimation();
		player.updateCamera();
		screen.renderScreen(player, spider, map, visible);
		player.removeLantern(map);

	}

	@Override
	public void dispose() {

		screen.getModelBatch().dispose();
		map.getInstances().clear();
		Map.getAssetManager().dispose();
		player.getPlayerModel().dispose();
		spider.getSpiderCollisionObject().dispose();
		spider.getSpiderShape().dispose();
		player.getPlayerCollisionObject().dispose();
		player.getPlayerShape().dispose();
		player.getDetector().getCollisionConfiguration().dispose();
		player.getDetector().getDispatcher().dispose();
		spider.getDetector().getCollisionConfiguration().dispose();
		spider.getDetector().getDispatcher().dispose();
	}

	@Override
	public void hide() {
	}

	@Override
	public void show() {
	}

	@Override
	public void update(float dt) {
	}
}