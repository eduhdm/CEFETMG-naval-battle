package com.teca.batalhanaval;

import java.util.Random;

public class TabLogico {
	private int[][] tabuleiro;
	//private int int direcao;
	
	public TabLogico() {
		tabuleiro = new int[10][10];
		for (int i = 0; i < tabuleiro.length; ++i) {
			for (int j = 0; j < tabuleiro[i].length; ++j) {
				tabuleiro[i][j] = 0;
			}
		}
	}
	public void posicionaBarco(int tam){
		int x = 0, y = 0, direcao = -1;
		Random gerador = new Random();
		for(;;){
			x = gerador.nextInt() % 10;
			x = (x < 0)? x *-1:x;
			y = gerador.nextInt() % 10;
			y = (y < 0)? y *-1:y;
			System.out.println(x);
			System.out.println(y);
			direcao = validaDirecao(x, y, tam);
			if(direcao != -1){
				break;
			}
		}
		preencheBlocos(x, y, tam, direcao);
	}
	//0 subir, 1 esquerda, 2 descer, 3 direita
	public void posicionaBloco(){
		
	}
	public int validaDirecao(int x, int y, int tam){
		Random gerador = new Random();
		int subir = 1, esquerda = 1, descer = 1, direita = 1;
		int direcao;
		for(int i=0;i<tam+1;i++){
			//subir
			if(i != tam){
				if(tabuleiro[x-i][y] == 3 || tabuleiro[x-i][y] == 1 || x - i == 0){
					subir = 0;
					break;
				}
			}
			else if(tabuleiro[x-i][y] == 3 ){
				subir = 0;
				break;
			}
			
		}
		for(int i=0;i<tam+1;i++){
			//esquerda
			if(i != tam){
				if(tabuleiro[x][y-i] == 3 || tabuleiro[x][y-i] == 1 || y - i == 0){
					esquerda = 0;
					break;
				}
			}
			else if(tabuleiro[x][y-i] == 3 ){
				esquerda = 0;
				break;
			}
		}
		for(int i=0;i<tam+1;i++){
			//descer
			if(i != tam){
				if(tabuleiro[x+i][y] == 3 || tabuleiro[x+i][y] == 1 || x + i == 9){
					descer = 0;
					break;
				}
			}
			else if(tabuleiro[x+i][y] == 3 ){
				descer = 0;
				break;
			}
		}
		for(int i=0;i<tam+1;i++){
			//direita
			if(i != tam){
				if(tabuleiro[x][y+i] == 3 || tabuleiro[x][y+i] == 1 || y + i == 9){
					direita = 0;
					break;
				}
			}
			else if(tabuleiro[x][y+i] == 3 ){
				direita = 0;
				break;
			}	
		}
		if(subir == 0 && esquerda == 0 && descer == 0 && direita == 0){
			return -1;
		}
		direcao = gerador.nextInt() % 4;
		direcao = (direcao < 0) ? -1 * direcao : direcao;
		for(;;){
			if(direcao == 0 && subir == 0 ){
				direcao = gerador.nextInt() % 4;
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
		switch (direcao) {
		case 0:
			System.out.println("cima");
			for(int i=x;i>x-tam;--i){//CIMA
				tabuleiro[i][y]=3;
				if(i == x && x != 9 && tabuleiro[i+1][y] != 3){
					tabuleiro[i+1][y] = 1;
				}
				if(y == 0 && tabuleiro[i][y+1] != 3){
					tabuleiro[i][y+1] = 1;
				}
				else if(y==9 && tabuleiro[i][y-1] != 3){
					tabuleiro[i][y-1] = 1;
				}
				else if (tabuleiro[i][y-1] != 3){
					tabuleiro[i][y-1] = 1;
					if(tabuleiro[i][y+1] != 3)
					tabuleiro[i][y+1] = 1;
				}
				if(i == x-tam+1 && x != 0 && tabuleiro[i-1][y] != 3 ){
					tabuleiro[i-1][y] = 1;
				}
			}
			
			break;
		case 1:
			System.out.println("esquerda");
			for(int i=y;i>y-tam;--i){//ESQUERDA
				tabuleiro[x][i]=3;
				if(i == y && y != 9 && tabuleiro[x][i+1] != 3){
					tabuleiro[x][i+1] = 1;
				}
				if(x == 0 && tabuleiro[x+1][i] != 3){
					tabuleiro[x+1][i] = 1;
				}
				else if(x == 9 && tabuleiro[x-1][i] != 3){
					tabuleiro[x-1][i] = 1;
				}
				else if(tabuleiro[x-1][i] != 3){
					tabuleiro[x-1][i] = 1;
					if(tabuleiro[x+1][i] != 3){
						tabuleiro[x+1][i] = 1;
					}
				}
				if(i == y-tam+1 && y != 0 && tabuleiro[x][i-1] != 3){
					tabuleiro[x][i-1] = 1;
				}
			}
			break;
		case 2:
			System.out.println("baixo");
			for(int i=x;i<x+tam;++i){//BAIXO
				tabuleiro[i][y]=3;
				if(i == x && x != 0 && tabuleiro[i-1][y] != 3){
					tabuleiro[i-1][y] = 1;
				}
				if(y == 0 && tabuleiro[i][y+1] != 3){
					tabuleiro[i][y+1] = 1;
				}
				else if(y==9 && tabuleiro[i][y-1] != 3){
					tabuleiro[i][y-1] = 1;
				}
				else if(tabuleiro[i][y-1] != 3){
					tabuleiro[i][y-1] = 1;
					if(tabuleiro[i][y+1] != 3){
						tabuleiro[i][y+1] = 1;
					}
					
				}
				if(i == x+tam-1 && x != 9 && tabuleiro[i+1][y] != 3){
					tabuleiro[i+1][y] = 1;
				}
			}
			break;
			
		case 3:
			System.out.println("direita");
			for(int i=y;i<y+tam;++i){//DIREITA
				tabuleiro[x][i]=3;
				if(i == y && y != 0 && tabuleiro[x][i-1] != 3){
					tabuleiro[x][i-1] = 1;
				}
				if(x == 0 && tabuleiro[x+1][i] != 3){
					tabuleiro[x+1][i] = 1;
				}
				else if(x == 9 && tabuleiro[x-1][i] != 3){
					tabuleiro[x-1][i] = 1;
				}
				else if(tabuleiro[x-1][i] != 3){
					tabuleiro[x-1][i] = 1;
					if(tabuleiro[x+1][i] != 3){
						tabuleiro[x+1][i] = 1;
					}
				}
				if(i == y+tam-1 && y != 9 && tabuleiro[x][i+1] != 3){
					tabuleiro[x][i+1] = 1;
				}
			}
			break;
		}
	}
	
	public void showTab() {
		for (int i = 0; i < 10; ++i) {
			for (int j : tabuleiro[i]) {
				if (j == 0) {
					System.out.printf("~");
				} 
				else if(j == 1){
					System.out.printf("o");
				}
				else if (j == 3) {
					System.out.printf("X");
				}
			}
			System.out.printf("\n");
		}
	}
	public static void main(String[] args) {
		TabLogico bat = new TabLogico();
		bat.posicionaBarco(5);
		bat.posicionaBarco(4);
		bat.posicionaBarco(4);
		bat.posicionaBarco(4);
		bat.posicionaBarco(3);
		bat.posicionaBarco(3);
		bat.posicionaBarco(3);
		bat.posicionaBarco(2);
		bat.posicionaBarco(2);
		bat.posicionaBarco(2);
		bat.showTab();
		
	}

}
