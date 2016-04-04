package com.teca.batalhanaval;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;

public class GameScreen implements Screen {
	final BatalhaNaval game;
	private TabLogico logc;
	private Texture agua1, agua2, barraStatus, bomba;
	private Texture miss, target;
	private Vector3 touchPos;
	int[] pos;
	int quantidadeDeBombas;
	OrthographicCamera camera;
	private String tab[][], strStatus;

	private BitmapFont status, statusBomba;

	public void barcoDestruido() {
		Coordenada inicial, fim;
		inicial = new Coordenada();
		fim = new Coordenada();
		int t = 0;
		int aux = 0;

		for (int i = 0; i < 10; i++) {
			if(logc.barcos[i].estouDestruido())
				continue;//verifica se o barco já foi considerado destruido
						
			fim = logc.barcos[i].getPosFinal();
			inicial = logc.barcos[i].getPosInicial();
			
			if (inicial.y == fim.y) {
				if (inicial.x > fim.x) {
					aux = inicial.x;
					inicial.x = fim.x;
					fim.x = aux;
				}
				for (int k = inicial.x; k <= fim.x; k++) {
					if (tab[k][inicial.y] == "target")
						t++;
				}
				if (t == logc.barcos[i].getTamanho()) {
					System.out.println("t=" + t + "logc=" + logc.barcos[i].getTamanho());
					strStatus = "Voce derrubou um " + logc.barcos[i].getNome();
					logc.barcos[i].destruir();
				}
				t = 0;
			} 
			else if (inicial.x == fim.x) {
				if (inicial.y > fim.y) {
					aux = inicial.y;
					inicial.y = fim.y;
					fim.y = aux;
				}
				for (int k = inicial.y; k <= fim.y; k++) {
					if (tab[inicial.x][k] == "target")
						t++;
				}
				if (t == logc.barcos[i].getTamanho()) {
					System.out.println("t=" + t + "logc=" + logc.barcos[i].getTamanho());
					strStatus = "Voce derrubou um " + logc.barcos[i].getNome();
					logc.barcos[i].destruir();
				}
				t = 0;
			}
		}
	}

	public GameScreen(final BatalhaNaval gam) {
		this.game = gam;

		// inicia o tabuleiro logico
		logc = new TabLogico();
		strStatus = " Bem Vindo ao Jogo Batalha Naval\n"
				+ "É possivel ver a quantidade de bombas que voce tem no canto inferior direito\n"
				+ "Marcas azuis representam tiro na agua e marcas vermelhas tiro nos Barcos";
		// cria o vetor de coordenadas
		pos = new int[2];

		// cria o tabuleiro de acertos e erros
		tab = new String[10][10];
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				tab[i][j] = "vazio";
			}
		}
		// Define a dificuldade
		if (gam.getDificuldade() == "easy") {
			quantidadeDeBombas = 60;
		} else if (gam.getDificuldade() == "medium") {
			quantidadeDeBombas = 50;
		} else if (gam.getDificuldade() == "hard") {
			quantidadeDeBombas = 40;
		}

		// carrega as imagens
		agua1 = new Texture(Gdx.files.internal("agua1.jpg"));
		agua2 = new Texture(Gdx.files.internal("agua2.jpg"));
		barraStatus = new Texture(Gdx.files.internal("barraStatus.png"));
		miss = new Texture(Gdx.files.internal("miss.png"));
		target = new Texture(Gdx.files.internal("target.png"));
		status = new BitmapFont(false);
		statusBomba = new BitmapFont(false);
		bomba = new Texture(Gdx.files.internal("FIRE.png"));
		// load the sound effects
		// dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 640 + 20, 640 + 20 + 80);
		touchPos = new Vector3();
	}

	private void getTabClick() {
		touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
		camera.unproject(touchPos);

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if ((touchPos.x >= 10 + 64 * i && touchPos.x <= 74 + 64 * i)
						&& (touchPos.y >= 70 + 20 + 64 * j && touchPos.y <= 70 + 20 + 64 * (j + 1))) {
					pos[1] = i;
					pos[0] = 9 - j;
					Gdx.app.log("render,pos", pos[0] + " " + pos[1]);
				}
			}
		}

		if (tab[pos[1]][pos[0]] == "vazio") {
			if (logc.tabuleiro[pos[1]][pos[0]] == 2 
					|| logc.tabuleiro[pos[1]][pos[0]] == 3
					|| logc.tabuleiro[pos[1]][pos[0]] == 4 
					|| logc.tabuleiro[pos[1]][pos[0]] == 5) {
				// se tiver algum barco
				tab[pos[1]][pos[0]] = "target";
				switch (logc.tabuleiro[pos[1]][pos[0]]) {
				case 2:
					strStatus = "Você atingiu um Rebocador!!!";
					break;
				case 3:
					strStatus = "Você atingiu um Contra-Torpedeiro!!!";
					break;
				case 4:
					strStatus = "Você atingiu um Cruzador!!!";
					break;
				case 5:
					strStatus = "Você atingiu um Porta-Aviões!!!";
					break;
				}
				barcoDestruido();
			} else {
				// se n tiver
				tab[pos[1]][pos[0]] = "miss";
				strStatus = "Você atingiu a Água!!!";
			}
			--quantidadeDeBombas;
		}
	}

	private void desenhaTab() {
		for (int i = 0; i < 10; ++i) { // desenha o tabuleiro
			for (int j = 0; j < 10; ++j) {
				if ((j + i) % 2 == 0) {
					game.batch.draw(agua1, 10 + 64 * i, 70 + 20 + 64 * j, 64, 64);
					if (tab[i][9 - j] == "miss") {
						game.batch.draw(miss, 10 + 64 * i, 70 + 20 + 64 * j, 64, 64);
					}
					if (tab[i][9 - j] == "target") {
						game.batch.draw(target, 10 + 64 * i, 70 + 20 + 64 * j, 64, 64);
					}

				} else {
					game.batch.draw(agua2, 10 + 64 * i, 70 + 20 + 64 * j, 64, 64);

					if (tab[i][9 - j] == "miss" && tab[i][9 - j] != "destruido") {
						game.batch.draw(miss, 10 + 64 * i, 70 + 20 + 64 * j, 64, 64);
					}
					if (tab[i][9 - j] == "target" && tab[i][9 - j] != "destruido") {
						game.batch.draw(target, 10 + 64 * i, 70 + 20 + 64 * j, 64, 64);
					}

				}
			}
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// tell the camera to update its matrices.
		camera.update();

		game.batch.setProjectionMatrix(camera.combined);
		// 640x640
		game.batch.begin();

		desenhaTab();
		game.batch.draw(barraStatus, 10, 10, 640, 70);// desenha a barra de
		status.draw(game.batch, strStatus, 15, 70); // status
		statusBomba.draw(game.batch, quantidadeDeBombas + " X", 550, 50);
		game.batch.draw(bomba, 580, 10, 70, 70);
		game.batch.end();

		// process user input
		
		if (Gdx.input.justTouched()) {
			if (quantidadeDeBombas > 0) {// confere os clicks na matriz
				getTabClick();
			}
		}

		if (quantidadeDeBombas <= 0) {
			strStatus = "ACABARAM SUAS BOMBAS E VOCÊ AINDA N DESTRUIU TODOS OS BARCOS\n"
					+ "SE DESEJA CONTINUAR COM MAIS 20 BOMBAS CLIQUE EM CIMA DA IMAGEM DA BOMBA";
			if (Gdx.input.justTouched()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				if (touchPos.x >= 600 && touchPos.x <= 650 && touchPos.y >= 15 && touchPos.y <= 50 + 15)
					quantidadeDeBombas = 20;
			}
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown

	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		agua1.dispose();
		agua2.dispose();
		barraStatus.dispose();
		target.dispose();
		miss.dispose();
	}

}
