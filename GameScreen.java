package com.teca.batalhanaval;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class GameScreen implements Screen {
	final BatalhaNaval game;
	private Texture looseScreen,winScreen;
	private TabLogico logc;
	private Texture agua1, agua2, barraStatus, bomba;
	private Texture miss, target;
	private Texture[] barco5, barco4, barco3, barco2;
	private Vector3 touchPos;
	private int[] pos;
	private int quantidadeDeBombas;
	private OrthographicCamera camera;
	private String tab[][], strStatus;
	private boolean win;
	private BitmapFont status, statusBomba;
	private Sound targetsound, misssound;

	public boolean winGame(){
		int aux=0;
		for (int i = 0; i < 10; i++) {
			if(logc.barcos[i].estouDestruido())
				++aux;
		}
		if(aux==10){
			strStatus = "VOCÊ GANHOU!!!";
			return true;
		}
		else return false;
	}
	
	public void barcoDestruido() {
		Coordenada inicial, fim;
		inicial = new Coordenada();
		fim = new Coordenada();
		int t = 0;
		int aux = 0;
		
		for (int i = 0; i < 10; i++) {
			if (logc.barcos[i].estouDestruido())
				continue;// verifica se o barco já foi considerado destruido

			fim = logc.barcos[i].getPosFinal();
			inicial = logc.barcos[i].getPosInicial();

			if (inicial.coluna == fim.coluna) {
				if (inicial.linha > fim.linha) {
					aux = inicial.linha;
					inicial.linha = fim.linha;
					fim.linha = aux;
				}
				for (int k = inicial.linha; k <= fim.linha; k++) {
					if (tab[k][inicial.coluna] == "target")
						t++;
				}
				if (t == logc.barcos[i].getTamanho()) {
					System.out.println("t=" + t + "logc=" + logc.barcos[i].getTamanho());
					strStatus = "Voce derrubou um " + logc.barcos[i].getNome();
					logc.barcos[i].destruir();
					for (int k = inicial.linha; k <= fim.linha; k++) {
						tab[k][inicial.coluna] = "destruido";
					}
					win=winGame();
				}
				t = 0;
			} else if (inicial.linha == fim.linha) {
				if (inicial.coluna > fim.coluna) {
					aux = inicial.coluna;
					inicial.coluna = fim.coluna;
					fim.coluna = aux;
				}
				for (int k = inicial.coluna; k <= fim.coluna; k++) {
					if (tab[inicial.linha][k] == "target")
						t++;
				}
				if (t == logc.barcos[i].getTamanho()) {
					System.out.println("t=" + t + "logc=" + logc.barcos[i].getTamanho());
					strStatus = "Voce derrubou um " + logc.barcos[i].getNome();
					logc.barcos[i].destruir();
					for (int k = inicial.coluna; k <= fim.coluna; k++) {
						tab[inicial.linha][k] = "destruido";
					}
					win=winGame();
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
		//carrega os sons
		targetsound = Gdx.audio.newSound(Gdx.files.internal("sounds/missilebomb.mp3"));
		misssound = Gdx.audio.newSound(Gdx.files.internal("sounds/missilewater.mp3"));
		// carrega as imagens
		agua1 = new Texture(Gdx.files.internal("agua1.jpg"));
		agua2 = new Texture(Gdx.files.internal("agua2.jpg"));
		barraStatus = new Texture(Gdx.files.internal("barraStatus.png"));
		miss = new Texture(Gdx.files.internal("miss.png"));
		target = new Texture(Gdx.files.internal("target.png"));
		status = new BitmapFont(false);
		
		//Barco de Tamanho 5
		barco5 = new Texture[2];
		barco5[0] = new Texture(Gdx.files.internal("barco5exp_h.png"));
		barco5[1] = new Texture(Gdx.files.internal("barco5exp_v.png"));
		//Barco de Tamanho 4
		barco4 = new Texture[2];
		barco4[0] = new Texture(Gdx.files.internal("barco4exp_h.png"));
		barco4[1] = new Texture(Gdx.files.internal("barco4exp_v.png"));
		//Barco de Tamanho 3
		barco3 = new Texture[2];
		barco3[0] = new Texture(Gdx.files.internal("barco3exp_h.png"));
		barco3[1] = new Texture(Gdx.files.internal("barco3exp_v.png"));
		//Barco de Tamanho 2
		barco2 = new Texture[2];
		barco2[0] = new Texture(Gdx.files.internal("barco2exp_h.png"));
		barco2[1] = new Texture(Gdx.files.internal("barco2exp_v.png"));
		
		Color black = new Color(0, 0, 0.2f, 1);
		status.setColor(black);
		statusBomba = new BitmapFont(false);
		statusBomba.setColor(black);
		bomba = new Texture(Gdx.files.internal("FIRE.png"));
	
		looseScreen = new Texture(Gdx.files.internal("looseScreen.png"));
		winScreen = new Texture(Gdx.files.internal("winScreen.png"));
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
				if ((touchPos.x >= 10 + 64 * j && touchPos.x <= 74 + 64 * j)
						&& (touchPos.y >= 70 + 20 + 64 * i && touchPos.y <= 70 + 20 + 64 * (i + 1))) {
					pos[1] = j;//coluna
					pos[0] = 9-i;//linha
					Gdx.app.log("render,pos", pos[0] + " " + pos[1]);
				}
			}
		}

		if (tab[pos[0]][pos[1]] == "vazio") {
			if (logc.tabuleiro[pos[0]][pos[1]] == 2 
					|| logc.tabuleiro[pos[0]][pos[1]] == 3
					|| logc.tabuleiro[pos[0]][pos[1]] == 4 
					|| logc.tabuleiro[pos[0]][pos[1]] == 5) {
				// se tiver algum barco
				targetsound.play(0.3f);
				switch (logc.tabuleiro[pos[0]][pos[1]]) {
				case 2:
					Timer.schedule(new Task(){
						public void run(){
							tab[pos[0]][pos[1]] = "target";
							strStatus = "Você atingiu um Rebocador!!!";
						}
						
					}, 0.5f);
					
					break;
				case 3:
					Timer.schedule(new Task(){
						public void run(){
							tab[pos[0]][pos[1]] = "target";
							strStatus = "Você atingiu um Contra-Torpedeiro!!!";
						}
						
					}, 0.5f);
					
					break;
				case 4:
					Timer.schedule(new Task(){
						public void run(){
							tab[pos[0]][pos[1]] = "target";
							strStatus = "Você atingiu um Cruzador!!!";
						}
						
					}, 0.5f);
					
					break;
				case 5:
					Timer.schedule(new Task(){
						public void run(){
							tab[pos[0]][pos[1]] = "target";
							strStatus = "Você atingiu um Porta-Aviões!!!";
						}
						
					}, 0.5f);
					
					break;
				}
				Timer.schedule(new Task(){
					public void run(){
						barcoDestruido();
					}
					
				}, 0.5f);
				
			} else {
				// se n tiver
				misssound.play(0.3f);
				Timer.schedule(new Task(){
					public void run(){
						tab[pos[0]][pos[1]] = "miss";
						strStatus = "Você atingiu a Água!!!";	
					}
					
				}, 0.5f);
				
			}
			--quantidadeDeBombas;
		}
	}

	private void desenhaTab() {
		for (int i = 0; i < 10; ++i) { // desenha o tabuleiro
			for (int j = 0; j < 10; ++j) {
				if ((j + i) % 2 == 0) {
					game.batch.draw(agua1, 10 + 64 * j, 70 + 20 + 64 * i, 64, 64);
					if (tab[9 - i][j] == "miss" && tab[9 - i][j] != "destruido") {
						game.batch.draw(miss, 10 + 64 * j, 70 + 20 + 64 * i, 64, 64);
					}
					if (tab[9 - i][j] == "target" && tab[9 - i][j] != "destruido") {
						game.batch.draw(target, 10 + 64 * j, 70 + 20 + 64 * i, 64, 64);
					}

				} else {
					game.batch.draw(agua2, 10 + 64 * j, 70 + 20 + 64 * i, 64, 64);

					if (tab[9 - i][j] == "miss" && tab[9 - i][j] != "destruido") {
						game.batch.draw(miss, 10 + 64 * j, 70 + 20 + 64 * i, 64, 64);
					}
					if (tab[9 - i][j] == "target" && tab[9 - i][j] != "destruido") {
						game.batch.draw(target, 10 + 64 * j, 70 + 20 + 64 * i, 64, 64);
					}

				}
			}
		}
	}

	public void mostraBarcoDestruido() {
		Coordenada inicial, fim;
		inicial = new Coordenada();
		fim = new Coordenada();
		int aux = 0;
		for (int i = 0; i < 10; i++) {
			if (i == 0 && logc.barcos[i].estouDestruido()) {
				inicial = logc.barcos[i].getPosInicial();
				fim = logc.barcos[i].getPosFinal();
				if (inicial.coluna == fim.coluna) {
					if (inicial.linha < fim.linha) {
						aux = inicial.linha;
						inicial.linha = fim.linha;
						fim.linha = aux;
					}
					// game.batch.draw(texture, coluna, linha, largura, altura);
					// game.batch.draw(target, 10 + 64 * j, 70 + 20 + 64 * i,
					// 64, 64);
					game.batch.draw(barco5[1], 10 + 64 * inicial.coluna, 70 + 20 + 64 * (9 - inicial.linha), 64,
							64 * 5);
				} else if (inicial.linha == fim.linha) {
					if (inicial.coluna > fim.coluna) {
						aux = inicial.coluna;
						inicial.coluna = fim.coluna;
						fim.coluna = aux;
					}
					game.batch.draw(barco5[0], 10 + 64 * inicial.coluna, 70 + 20 + 64 * (9 - inicial.linha), 64 * 5,
							64);
				}
			} else if (i == 1 && logc.barcos[i].estouDestruido() || i == 2 && logc.barcos[i].estouDestruido()) {

				inicial = logc.barcos[i].getPosInicial();
				fim = logc.barcos[i].getPosFinal();
				if (inicial.coluna == fim.coluna) {
					if (inicial.linha < fim.linha) {
						aux = inicial.linha;
						inicial.linha = fim.linha;
						fim.linha = aux;
					}
					// game.batch.draw(texture, coluna, linha, largura, altura);
					// game.batch.draw(target, 10 + 64 * j, 70 + 20 + 64 * i,
					// 64, 64);
					game.batch.draw(barco4[1], 10 + 64 * inicial.coluna, 70 + 20 + 64 * (9 - inicial.linha), 64,
							64 * 4);
				} else if (inicial.linha == fim.linha) {
					if (inicial.coluna > fim.coluna) {
						aux = inicial.coluna;
						inicial.coluna = fim.coluna;
						fim.coluna = aux;
					}
					game.batch.draw(barco4[0], 10 + 64 * inicial.coluna, 70 + 20 + 64 * (9 - inicial.linha), 64 * 4,
							64);
				}
			} else if (i >= 3 && i <= 5 && logc.barcos[i].estouDestruido()) {
				inicial = logc.barcos[i].getPosInicial();
				fim = logc.barcos[i].getPosFinal();
				if (inicial.coluna == fim.coluna) {
					if (inicial.linha < fim.linha) {
						aux = inicial.linha;
						inicial.linha = fim.linha;
						fim.linha = aux;
					}
					// game.batch.draw(texture, coluna, linha, largura, altura);
					// game.batch.draw(target, 10 + 64 * j, 70 + 20 + 64 * i,
					// 64, 64);
					game.batch.draw(barco3[1], 10 + 64 * inicial.coluna, 70 + 20 + 64 * (9 - inicial.linha), 64,
							64 * 3);
				} else if (inicial.linha == fim.linha) {
					if (inicial.coluna > fim.coluna) {
						aux = inicial.coluna;
						inicial.coluna = fim.coluna;
						fim.coluna = aux;
					}
					game.batch.draw(barco3[0], 10 + 64 * inicial.coluna, 70 + 20 + 64 * (9 - inicial.linha), 64 * 3,
							64);
				}
			} else if (i >= 6 && logc.barcos[i].estouDestruido()) {
				inicial = logc.barcos[i].getPosInicial();
				fim = logc.barcos[i].getPosFinal();
				if (inicial.coluna == fim.coluna) {
					if (inicial.linha < fim.linha) {
						aux = inicial.linha;
						inicial.linha = fim.linha;
						fim.linha = aux;
					}
					// game.batch.draw(texture, coluna, linha, largura, altura);
					// game.batch.draw(target, 10 + 64 * j, 70 + 20 + 64 * i,
					// 64, 64);
					game.batch.draw(barco2[1], 10 + 64 * inicial.coluna, 70 + 20 + 64 * (9 - inicial.linha), 64,
							64 * 2);
				} else if (inicial.linha == fim.linha) {
					if (inicial.coluna > fim.coluna) {
						aux = inicial.coluna;
						inicial.coluna = fim.coluna;
						fim.coluna = aux;
					}
					game.batch.draw(barco2[0], 10 + 64 * inicial.coluna, 70 + 20 + 64 * (9 - inicial.linha), 64 * 2,
							64);
				}
			}

		}

		// if(barco.[0])
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
		mostraBarcoDestruido();
		game.batch.draw(barraStatus, 10, 10, 640, 70);// desenha a barra de
		status.draw(game.batch, strStatus, 15, 70); // status
		statusBomba.draw(game.batch, quantidadeDeBombas + " X", 550, 50);
		game.batch.draw(bomba, 580, 10, 70, 70);
		if (quantidadeDeBombas <= 0)
			game.batch.draw(looseScreen, 10+100, 100 + 20 + 80 , 440, 440);
		if (win)
			game.batch.draw(winScreen, 10+100, 100 + 20 + 80 , 440, 440);
		game.batch.end();

		// process user input

		if (Gdx.input.justTouched() && quantidadeDeBombas > 0 && !win) {// confere os clicks na matriz
				getTabClick();
		}

		if (quantidadeDeBombas <= 0) {
			strStatus = "ACABARAM SUAS BOMBAS E VOCÊ AINDA NÃO DESTRUIU TODOS OS BARCOS\n"
					+ "SE DESEJA CONTINUAR COM MAIS 20 BOMBAS\n CLIQUE EM CIMA DA IMAGEM DE PERDEU";
			
			if (Gdx.input.justTouched()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				if (touchPos.x >= 10 + 100 && touchPos.x <= 440 + 10 + 100 
					&& touchPos.y >= 100 + 20 && touchPos.y <= 440 + 100 + 20)
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
		barco5[0].dispose();
		barco5[1].dispose();
		barco4[0].dispose();
		barco4[1].dispose();
		barco3[0].dispose();
		barco3[1].dispose();
		barco2[0].dispose();
		barco2[1].dispose();
	}

}
