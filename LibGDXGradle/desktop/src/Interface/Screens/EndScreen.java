package Interface.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.desktop.DCGame;

import externals.GifDecoder;

public class EndScreen implements Screen{

	private SpriteBatch batch;
	private Animation<TextureRegion> animation;
	private float elapsed;
	private DCGame g;
	
	protected Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private static final int WORLD_WIDTH  = 1024;
    private static final int WORLD_HEIGHT = 768;
	
	public EndScreen(DCGame g) {
		this.g = g;
		
		camera = new OrthographicCamera();
		viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT,  camera);
		viewport.apply();
		stage = new Stage(viewport);   
	}
	
	@Override
	public void show() {
		batch = new SpriteBatch();
		animation = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, 
				Gdx.files.internal("EndScreen/endbg.gif").read());
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        
        elapsed += Gdx.graphics.getDeltaTime();
        
        batch.setProjectionMatrix(stage.getCamera().combined);
        camera.update();
        
        batch.begin();
        batch.draw(animation.getKeyFrame(elapsed, true), 0, 0);        
        batch.end();

        if(Gdx.input.justTouched())
        	g.setScreen(new MainMenuScreen(g));

	}

	
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
