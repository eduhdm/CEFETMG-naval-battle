package com.teca.batalhanaval;

import java.util.Random;

public class TabLogico {
	public int[][] tabuleiro;
	public Barco[] barcos;
	//private int int direcao;
	
	public TabLogico() {
		tabuleiro = new int[10][10];
		for (int i = 0; i < tabuleiro.length; ++i) {
			for (int j = 0; j < tabuleiro[i].length; ++j) {
				tabuleiro[i][j] = 0;
			}
		}
		setTabuleiroBatalhaNaval();
	}
	
	public void reset(){
		for (int i = 0; i < tabuleiro.length; ++i) {
			for (int j = 0; j < tabuleiro[i].length; ++j) {
				tabuleiro[i][j] = 0;
			}
		}
		setTabuleiroBatalhaNaval();
	}
	
	public void setTabuleiroBatalhaNaval(){
		barcos = new Barco[10];
		/*for(int i=0;i<10;i++){
			barcos[i] = new Barco();
		}*/
		
		posicionaBarco(5,0);
		posicionaBarco(4,1);
		posicionaBarco(4,2);
		posicionaBarco(3,3);
		posicionaBarco(3,4);
		posicionaBarco(3,5);
		posicionaBarco(2,6);
		posicionaBarco(2,7);
		posicionaBarco(2,8);
		posicionaBarco(2,9);
	}
	public void posicionaBarco(int tam, int indice_barco){
		int x = 0, y = 0, direcao = -1;
		barcos[indice_barco] = new Barco();
		Random gerador = new Random();
		for(;;){
			x = gerador.nextInt() % 10;
			x = (x < 0)? x *-1:x;
			y = gerador.nextInt() % 10;
			y = (y < 0)? y *-1:y;
			//System.out.println(x);
			//System.out.println(y);
			direcao = validaDirecao(x, y, tam);
			if(direcao != -1){
				break;
			}
		}
		barcos[indice_barco].setPosInicial(x, y);
		System.out.println("Inicial:" + x + ", " + y);
		barcos[indice_barco].setTamanho(tam);
		if(direcao == 0){//cima
			barcos[indice_barco].setPosFinal(x - tam+1, y);
			System.out.println("Final:" + (x-tam+1) + ", " + y);
		}
		if(direcao == 1){//esquerda
			barcos[indice_barco].setPosFinal(x, y-tam+1);
			System.out.println("Final:" + x + ", " + (y-tam+1));
		}
		if(direcao == 2){//baixo
			barcos[indice_barco].setPosFinal(x + tam-1, y);
			System.out.println("Final:" + (x+tam-1) + ", " + y);
		}
		if(direcao == 3){//direita
			barcos[indice_barco].setPosFinal(x, y+tam-1);
			System.out.println("Final:" + x + ", " + (y+tam-1));
		}
		if(tam == 2){
			barcos[indice_barco].setNome("Rebocador");
			System.out.println(barcos[indice_barco].getNome());
		}
		if(tam == 3){
			barcos[indice_barco].setNome("Contra-torpedeiro");
			System.out.println(barcos[indice_barco].getNome());
		}
		if(tam == 4){
			barcos[indice_barco].setNome("Cruzador");
			System.out.println(barcos[indice_barco].getNome());
		}
		if(tam == 5){
			barcos[indice_barco].setNome("Porta-aviões");
			System.out.println(barcos[indice_barco].getNome());
		}
		
		preencheBlocos(x, y, tam, direcao);
	}
	//0 subir, 1 esquerda, 2 descer, 3 direita
	public int validaDirecao(int x, int y, int tam){
		Random gerador = new Random();
		int subir = 1, esquerda = 1, descer = 1, direita = 1;
		int direcao;
		for(int i=0;i<tam+1;i++){// cadas for praticamente valida se ha espaco
			//subir					para que o barco seja posicionado em determinada posicao
			if(i != tam){			// o for vai ate tam+1 para que o barco nao 'encoste' em outro barco
				if(tabuleiro[x-i][y] >= 1 && tabuleiro[x-i][y] <= 5|| tabuleiro[x-i][y] == 8 || x - i == 0){
					subir = 0;
					break;
				}
			}
			else if(tabuleiro[x-i][y] >= 1 && tabuleiro[x-i][y] <= 5 ){// o segundo if e para assegurar que ele deixara de ser 
				subir = 0;					 // posicionado somente se o bloco proximo for um barco 
				break;
			}
			
		}
		for(int i=0;i<tam+1;i++){
			//esquerda
			if(i != tam){
				if(tabuleiro[x][y-i] >= 1 && tabuleiro[x][y-i] <= 5|| tabuleiro[x][y-i] == 8 || y - i == 0){
					esquerda = 0;
					break;
				}
			}
			else if(tabuleiro[x][y-i] >= 1 && tabuleiro[x][y-i] <= 5){
				esquerda = 0;
				break;
			}
		}
		for(int i=0;i<tam+1;i++){
			//descer
			if(i != tam){
				if(tabuleiro[x+i][y] >= 1 && tabuleiro[x+i][y] <= 5|| tabuleiro[x+i][y] == 8 || x + i == 9){
					descer = 0;
					break;
				}
			}
			else if(tabuleiro[x+i][y] >= 1 && tabuleiro[x+i][y] <= 5 ){
				descer = 0;
				break;
			}
		}
		for(int i=0;i<tam+1;i++){
			//direita
			if(i != tam){
				if(tabuleiro[x][y+i] >= 1 && tabuleiro[x][y+i] <= 5|| tabuleiro[x][y+i] == 8 || y + i == 9){
					direita = 0;
					break;
				}
			}
			else if(tabuleiro[x][y+i] >= 1 && tabuleiro[x][y+i] <= 5 ){
				direita = 0;
				break;
			}	
		}
		if(subir == 0 && esquerda == 0 && descer == 0 && direita == 0){// retorna -1 caso 
			return -1;												// nenhuma direcao seja valida 
		}															//para determinado barco
		direcao = gerador.nextInt() % 4;
		direcao = (direcao < 0) ? -1 * direcao : direcao;
		for(;;){
			if(direcao == 0 && subir == 0 ){// testa se a direcao foi setada e se o valor gerado
				direcao = gerador.nextInt() % 4;// corresponde com a determinada direcao
				direcao = (direcao < 0) ? -1 * direcao : direcao;
			}
			else if(direcao == 1 && esquerda == 0 ){
				direcao = gerador.nextInt() % 4;
				direcao = (direcao < 0) ? -1 * direcao : direcao;
			}
			else if(direcao == 2 && descer == 0){
				direcao = gerador.nextInt() % 4;
				direcao = (direcao < 0) ? -1 * direcao : direcao;
			}
			else if(direcao == 3 && direita == 0 ){
				direcao = gerador.nextInt() % 4;
				direcao = (direcao < 0) ? -1 * direcao : direcao;
			}
			else{
				break;
			}
			
		}
		return direcao;
		
	
	}
	public void preencheBlocos(int x,int y,int tam, int direcao){
		int navio = tam;
		switch (direcao) {					//as funcoes preenchem o espaço com o 
		case 0:								// tamanho do determinado barco
			System.out.println("cima");
			
			for(int i=x;i>x-tam;--i){//CIMA
				tabuleiro[i][y]=navio;
				if(i == x && x != 9){
					if(tabuleiro[i+1][y] == 0 || tabuleiro[i+1][y] == 8 ){// preenche a area que outro barco nao pode ocupar com 1
						tabuleiro[i+1][y] = 8;
					}	
				}
				if(y == 0 && tabuleiro[i][y+1] == 0 || y == 0 && tabuleiro[i][y+1] == 8){// o teste tabuleiro[x][y] != 3 em todos ifs
					tabuleiro[i][y+1] = 8;			 // é para que onde tinha valor 3(navio) nao ser substituido por 1
				}									// ao se posicionar o navio
				else if(y==9 && tabuleiro[i][y-1] == 0 || y==9 && tabuleiro[i][y-1] == 8){
					tabuleiro[i][y-1] = 8;
				}
				else if (tabuleiro[i][y-1] == 0 || tabuleiro[i][y-1] == 8){
					tabuleiro[i][y-1] = 8;
					if(tabuleiro[i][y+1] == 0 || tabuleiro[i][y+1] == 8)
					tabuleiro[i][y+1] = 8;
				}
				if(i == x-tam+1 && x != 0){
					if(tabuleiro[i-1][y] == 0 || tabuleiro[i-1][y] == 8){
						tabuleiro[i-1][y] = 8;
					}
				}
			}
		showTab();
			break;
		case 1:
			System.out.println("esquerda");
			for(int i=y;i>y-tam;--i){//ESQUERDA
				tabuleiro[x][i]=navio;
				if(i == y && y != 9){
					if(tabuleiro[x][i+1] == 0 || tabuleiro[x][i+1] == 8){// na frente do barco
						tabuleiro[x][i+1] = 8;
					}
				}
				if(x == 0 && tabuleiro[x+1][i] == 0 || x == 0 && tabuleiro[x+1][i] == 8){// em apenas uma lateral
					tabuleiro[x+1][i] = 8;			// se estiver no canto
				}
				else if(x == 9 && tabuleiro[x-1][i] == 0 || x == 9 && tabuleiro[x-1][i] == 8){
					tabuleiro[x-1][i] = 8;
				}
				else if(tabuleiro[x-1][i] == 0 || tabuleiro[x-1][i] == 8){// normalmente
					tabuleiro[x-1][i] = 8;		//ambos os lados
					if(tabuleiro[x+1][i] == 0 || tabuleiro[x+1][i] == 8){
						tabuleiro[x+1][i] = 8;
					}
				}
				if(i == y-tam+1 && y != 0){
					if(tabuleiro[x][i-1] == 0 || tabuleiro[x][i-1] == 8){//atras do barco
						tabuleiro[x][i-1] = 8;
					}
				}
			}
			
			break;
		case 2:
			System.out.println("baixo");
			for(int i=x;i<x+tam;++i){//BAIXO
				tabuleiro[i][y]=navio;
				if(i == x && x != 0 ){
					if(tabuleiro[i-1][y] == 0 || tabuleiro[i-1][y] == 8){
						tabuleiro[i-1][y] = 8;
					}
				}
				if(y == 0 && tabuleiro[i][y+1] == 0 || y == 0 && tabuleiro[i][y+1] == 8){
					tabuleiro[i][y+1] = 8;
				}
				else if(y==9 && tabuleiro[i][y-1] == 0 ||y==9 &&  tabuleiro[i][y-1] == 8){
					tabuleiro[i][y-1] = 8;
				}
				else if(tabuleiro[i][y-1] == 0 || tabuleiro[i][y-1] == 8){
					tabuleiro[i][y-1] = 8;
					if(tabuleiro[i][y+1] == 0 || tabuleiro[i][y+1] == 8){
						tabuleiro[i][y+1] = 8;
					}
					
				}
				if(i == x+tam-1 && x != 9 ){
					if(tabuleiro[i+1][y] == 0 || tabuleiro[i+1][y] == 8){
						tabuleiro[i+1][y] = 8;
					}
				}
			}
			break;
			
		case 3:
			System.out.println("direita");
			for(int i=y;i<y+tam;++i){//DIREITA
				tabuleiro[x][i]=navio;
				if(i == y && y != 0){
					if(tabuleiro[x][i-1] == 0 || tabuleiro[x][i-1] == 8){
						tabuleiro[x][i-1] = 8;
					}
				}
				if(x == 0 && tabuleiro[x+1][i] == 0 || x == 0 && tabuleiro[x+1][i] == 8){
					tabuleiro[x+1][i] = 8;
				}
				else if(x == 9 && tabuleiro[x-1][i] == 0 || x == 9 && tabuleiro[x-1][i] == 8){
					tabuleiro[x-1][i] = 8;
				}
				else if(tabuleiro[x-1][i] == 0 || tabuleiro[x-1][i] == 8){
					tabuleiro[x-1][i] = 8;
					if(tabuleiro[x+1][i] == 0 || tabuleiro[x+1][i] == 8){
						tabuleiro[x+1][i] = 8;
					}
				}
				if(i == y+tam-1 && y != 9){
					if(tabuleiro[x][i+1] == 0 || tabuleiro[x][i+1] == 8){
						tabuleiro[x][i+1] = 8;
					}
				}
			}
			break;
			
		}
		showTab();
		
	}
	
	public void showTab() {
		for (int i = 0; i < 10; ++i) {
			for (int j : tabuleiro[i]) {
				if (j == 0) {
					System.out.printf(" ~ ");
				} 
				else if(j == 8){
					System.out.printf(" o ");
				}
				else {
					System.out.printf(" %d ", j);
				}
			}
			System.out.printf("\n");
		}
		System.out.println("------------------------------------------");
	}
	public static void main(String[] args) {
		TabLogico bat = new TabLogico();
		
		
	}

}
class Coordenada{
	public int linha, coluna;
	
}



class Barco{
	private int tam;
	private Coordenada pos_inicial;
	private Coordenada pos_final;
	private String nome;
	private boolean Destruido;
	Barco(){
		Destruido = false;
	}
	
	public void destruir(){
		
		Destruido = true;
	}
	public boolean estouDestruido(){
		return Destruido;
	}
	public void setTamanho(int t) {
		tam = t;
		
	}
	public int getTamanho(){
		return tam;
	}
	public Coordenada getPosInicial(){
		return pos_inicial;
	}
	public void setPosInicial(int x, int y){
		pos_inicial = new Coordenada();
		pos_inicial.linha = x;
		pos_inicial.coluna = y;
	}
	public Coordenada getPosFinal(){
		return pos_final;
	}
	public void setPosFinal(int x, int y){
		pos_final = new Coordenada();
		pos_final.linha = x;
		pos_final.coluna = y;
	}
	public void setNome(String n){
		nome = n;
	}
	public String getNome(){
		return nome;
	}
}
