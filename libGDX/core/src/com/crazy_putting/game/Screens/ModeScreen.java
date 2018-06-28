package com.crazy_putting.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

        public class ModeScreen implements Screen {

            private GolfGame golfGame;

            private Stage stage;
            private Skin skin;

            private Table table;
            private TextButton mode1Button;
            private TextButton mode2Button;
            private TextButton backButton;

            public ModeScreen(final GolfGame golfGame) {

                this.golfGame = golfGame;

                skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
                stage = new Stage(new ScreenViewport());
                Gdx.input.setInputProcessor(stage);


                // buttons
                mode1Button = new TextButton("Mode 1", skin);
                mode1Button.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.getApplicationListener().dispose();
                        golfGame.setScreen(new ChooseCoursesScreen(golfGame, 1)); // go to "Select course" screen, with mode 1
                    }
                });

                mode2Button = new TextButton("Mode 2", skin);
                mode2Button.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.getApplicationListener().dispose();
                        golfGame.setScreen(new ChooseCoursesScreen(golfGame,2)); // go to "Select course" screen, with mode 2
                    }
                });

                backButton = new TextButton("Back", skin);
                backButton.setPosition(10, 10);
                backButton.addListener(new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.getApplicationListener().dispose();
                        golfGame.setScreen(new MenuScreen(golfGame)); // go back to "MenuScreen" screen
                    }
                });

                // table
                table = new Table();
                table.setWidth(stage.getWidth());
                table.align(Align.center|Align.top);
                table.setPosition(0, Gdx.graphics.getHeight());

                table.padTop(170);
                table.add(mode1Button).size(300, 50).padBottom(20);
                table.row();
                table.add(mode2Button).size(300, 50);

                stage.addActor(table);
                stage.addActor(backButton);
            }

            @Override
            public void show() {

            }

            @Override
            public void render(float delta) {
                // background
                int red = 34;
                int green = 137;
                int blue = 34;
                Gdx.gl.glClearColor((float)(red/255.0), (float)(green/255.0), (float)(blue/255.0), 1);

                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

                stage.act(Gdx.graphics.getDeltaTime());
                stage.draw();
            }

            @Override
            public void resize(int width, int height) {

            }

            @Override
            public void pause() {

            }

            @Override
            public void resume() {

            }

            @Override
            public void hide() {

            }

            @Override
            public void dispose() {

            }

        }
