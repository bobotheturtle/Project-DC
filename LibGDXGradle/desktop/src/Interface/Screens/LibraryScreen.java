package Interface.Screens;

import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.desktop.DCGame;
import com.engine.desktop.SaveSys;
import Interface.Stages.Selections.LibrarySelection;

public class LibraryScreen implements Screen{
	
	protected Stage stage;
	private Viewport viewport;
	private OrthographicCamera camera;
	private TextureAtlas atlas;
	protected Skin skin;
	final DCGame game;
	
    private SaveSys fileHandle;
    private String selected_map;
    private Label prevSelected;
    
    private static final int WORLD_WIDTH  = 800;
	private static final int WORLD_HEIGHT = 450;

    public LibraryScreen(DCGame game2) throws IOException {
    	this.game = game2;
    	atlas = new TextureAtlas(Gdx.files.internal("cloud-form-ui.atlas"));
    	skin = new Skin(Gdx.files.internal("cloud-form-ui.json"), atlas);
    	camera = new OrthographicCamera();
    	viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT,  camera);
    	viewport.apply();
    	stage = new Stage(viewport);
    	prevSelected = null;
    	
    	this.fileHandle = new SaveSys();
    }
    
	@Override
	public void show() { 
        //Create Table
        Table mainTable = new Table();
        Table sideTable = new Table();
        
        //Set table to fill stage
        sideTable.setFillParent(true);
        mainTable.setFillParent(true);
    	sideTable.bottom();
        sideTable.padBottom(10);
        mainTable.top();
  
		
        // FONTS
        BitmapFont itemFont = new BitmapFont();
        itemFont.getData().setScale(1, 1);
        
        
        // TITLE
        Image titleImage = new Image(new TextureRegion(new Texture(Gdx.files.internal("LibScreen/libheader.png"))));
        mainTable.add(titleImage);
        mainTable.padTop(20);
        
        
        Table mapTable = new Table();
        // Add maps to list
        final LabelStyle def = new LabelStyle(itemFont, Color.WHITE);
        final LabelStyle selec = new LabelStyle(itemFont, Color.YELLOW);
        
        File[] list = fileHandle.getLibrary();
        for (final File f : list) {
        	final String fileName = f.getName();
        	final Label fileLabel = new Label(fileName, def);
        	fileLabel.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                	fileLabel.setStyle(selec);
                	selected_map =  fileName;     
                	System.out.println("Selected map - " + selected_map);
                	
                	// Unselect prev
                	if(prevSelected != null) {
                		prevSelected.setStyle(def);
                		prevSelected = fileLabel;
                	} else {
                		prevSelected = fileLabel;
                	}
                }
        	});	
        	mapTable.row();
        	mapTable.add(fileLabel);
        }
        
        final ScrollPane scroll = new ScrollPane(mapTable);
        scroll.setFillParent(true);
        scroll.setForceScroll(false,true);

        Table scrolltable = new Table();
        scrolltable.setBounds(175,45, 450, 322 );
        scrolltable.add(scroll).fill().expand();
      
        
        // SIDE TABLE
        // Edit / Run functionality
		for(final LibrarySelection selection: LibrarySelection.values()) {
			TextButton button = generateButton(selection.name());
			sideTable.add(button);
			button.addListener(new ClickListener(){
				@Override
		        public void clicked(InputEvent event, float x, float y) {
					Screen s = null;
					
						try {
							switch(selection) {
							case Edit:
								if(selected_map == null) {
									System.out.println("No map selected!");
									return;
								}
								
								s = new EditorScreen(game);
								System.out.println("Loading into Game: " + selected_map + "...");
								((Game)Gdx.app.getApplicationListener()).setScreen(s);
								((EditorScreen) s).loadModel(fileHandle.Load(selected_map));
								break;
								
							case Play:
								if(selected_map == null) {
									System.out.println("No map selected!");
									return;
								}
								
								s = new GameScreen(game);
								System.out.println("Loading in Editor: " + selected_map + "...");
								((Game)Gdx.app.getApplicationListener()).setScreen(s);
								((GameScreen) s).loadModel(fileHandle.Load(selected_map));
								break;
								
							case Delete:
								if(selected_map == null) {
									System.out.println("No map selected!");
									return;
								}
								
								System.out.println("Deleting map: " + selected_map + "...");
								fileHandle.Delete(selected_map);
								// Refresh screen
								((Game)Gdx.app.getApplicationListener()).setScreen(new LibraryScreen(game));
								break;
							case Delete_All:
								File[] delList = fileHandle.getLibrary();
						        for (final File f : delList) {
						        	f.delete();
						        }
						        // Refresh screen
								((Game)Gdx.app.getApplicationListener()).setScreen(new LibraryScreen(game));
							default:
								break;	
							}
						} catch (IOException e) {
							System.out.println("Cannot load map");
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							System.out.println("No EditorModel class!");
							e.printStackTrace();
						}
		        }
			});
		} 
        
        
        // Put it on the stage
        stage.addActor(new Image(new TextureRegion(new Texture(Gdx.files.internal("LibScreen/rsz_libbg.jpg")))));
        stage.addActor(mainTable);
        stage.addActor(scrolltable);
        stage.addActor(sideTable);
        
        
        // Add back button
        Table backTable = new Table();
        TextButton backButton = generateButton("Back");
        backButton.addListener(new ClickListener(){
			@Override
	        public void clicked(InputEvent event, float x, float y) {
				stage.dispose();
            	((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
			}
		});
        backTable.add(backButton);
        backTable.bottom();
        backTable.left();
        backTable.padLeft(10);
        backTable.padBottom(10);        
        stage.addActor(backTable);
        
        
        
		// ESC key to return to main menu
		InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {

                if ((keycode == Keys.ESCAPE) || (keycode == Keys.BACK)) {
                	stage.dispose();
                	((Game)Gdx.app.getApplicationListener()).setScreen(new MainMenuScreen(game));
                }
                return false;
            }
        };
        
		//Stage should control input:
		InputMultiplexer multiplexer = new InputMultiplexer(stage, backProcessor);
		Gdx.input.setInputProcessor(multiplexer);
				
	}

	@Override
	public void render(float delta) {
        Gdx.gl.glClearColor(.1f, .12f, .16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
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
		skin.dispose();
		atlas.dispose();
	}
	
	private TextButton generateButton(String s) {
		if (s.equals("Delete_All")) {
			s = "Delete All";
		}
		String newString = " " + s + " ";
		TextButton button = new TextButton(newString, skin);
		return button;
	}

}
