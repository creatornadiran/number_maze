import enigma.core.Enigma;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import enigma.console.*;
import java.awt.Color;
import java.io.*;
import java.util.Scanner;

public class Game { //Duvarda Path birakma - path kisaltma - sayilarin kaybolmasi
	public static enigma.console.Console cn = Enigma.getConsole("MAZE",85,27,15);
	public KeyListener klis;
	public int keypr;
	public int rkey;
	public  TextAttributes reset = new TextAttributes(Color.WHITE);
	public TextAttributes attr_back = new TextAttributes(Color.LIGHT_GRAY, Color.LIGHT_GRAY);
	public TextAttributes attr3 = new TextAttributes(Color.MAGENTA);
	public TextAttributes attr2 = new TextAttributes(Color.CYAN);
	public TextAttributes attr1 = new TextAttributes(Color.YELLOW);
	public TextAttributes attr4 = new TextAttributes(Color.RED);
	public TextAttributes attr5 = new TextAttributes(Color.GREEN);
	private CircularQueue number_queue = new CircularQueue(26);
	private Number[] numbers = new Number[9999];
	private Player p = new Player();
	private static int number_counter = 0;
	
	Game() throws Exception {
		Menu menu = new Menu();
		int selectionMode = menu.getOutput();
		
		for (int i = 0; i <26; i++) {
    		cn.getTextWindow().setCursorPosition(0, i);
			System.out.println("                                                                                    ");
		}
		Music music=new Music();
		if(p.getValue()!=0) {
			music.play("pacman.wav");
		}
		//*********************************************************WRITING OF THE DEFAULT CASE OF BACKPACK*********************************************************
		cn.getTextWindow().setCursorPosition(60, 6);		
		cn.setTextAttributes(attr2);
		System.out.println("  Backpacks");
		cn.setTextAttributes(reset);
		for (int i = 0; i < 9; i++) {
			cn.getTextWindow().setCursorPosition(60, i+8);
			System.out.println("|    |  |    | ");
			if (i==8) {
				cn.getTextWindow().setCursorPosition(60, i+8);
				System.out.println("+----+  +----+");
				cn.setTextAttributes(attr2);
				cn.getTextWindow().setCursorPosition(60, i+9);
				System.out.println(" Left   Right");
				cn.getTextWindow().setCursorPosition(60, i+10);
				System.out.println("  Q       W");
				cn.setTextAttributes(reset);
			}
		}
		//*********************************************************WRITING OF THE DEFAULT CASE OF INPUT*********************************************************
		cn.getTextWindow().setCursorPosition(60, 1);
		System.out.println("Input");
		cn.setTextAttributes(attr_back);
		cn.getTextWindow().setCursorPosition(60, 2);
		System.out.println("<<<<<<<<<<");
		cn.getTextWindow().setCursorPosition(60, 4);
		System.out.println("<<<<<<<<<<");
		cn.setTextAttributes(reset);
		//*********************************************************FILLING INPUT QUEUE AS A RANDOM WITH 25 NUMBERS*********************************************************
		for (int i = 0; i < 25; i++) 
			if(selectionMode ==1)
				number_queue.Enqueue(Number.random_choosing(80,97));
			else if(selectionMode ==2)
				number_queue.Enqueue(Number.random_choosing(75,95));
			else if(selectionMode ==3)
				number_queue.Enqueue(Number.random_choosing(65,90));		
		cn.getTextWindow().setCursorPosition(60, 3);
		for (int i = 0; i < 10; i++) {
			System.out.print(number_queue.Peek());
			number_queue.Enqueue(number_queue.Dequeue());	
		}
		for (int i = 0; i < 15; i++) 
			number_queue.Enqueue(number_queue.Dequeue());	
		//*********************************************************VARIABLES FOR KEY MOVEMENT*********************************************************
		klis = new KeyListener() {
			public void keyTyped(KeyEvent e) {
			}

			public void keyPressed(KeyEvent e) {
				if (keypr == 0) {
					keypr = 1;
					rkey = e.getKeyCode();
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		};
		cn.getTextWindow().addKeyListener(klis);
		//*********************************************************READING THE MAZE FROM TXT AND PRINTING ON THE SCREEN*********************************************************
		int px = 5, py = 5;
		char[][] board = new char[23][55];
		File myObj = new File("maze.txt");
		Scanner myReader = new Scanner(myObj);
		int row = 0;
		int column = 0;
		cn.getTextWindow().setCursorPosition(0, 0);
		while (myReader.hasNextLine()) {
			String line = myReader.nextLine();
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) == '#') {
					cn.setTextAttributes(attr_back);
					board[row][i] = line.charAt(i);
				} else {
					cn.setTextAttributes(reset);
					board[row][i] = ' ';
				}
				System.out.print(board[row][i]);
			}
			row++;
			System.out.println();
		}
		cn.setTextAttributes(reset);
		myReader.close();
		//*********************************************************NEW NUMBER TO THE BOARD FROM INPUT********************************************************
		for (int i = 0; i < 25; i++) {
			if(selectionMode ==1)
				number_queue.Enqueue(Number.random_choosing(80,97));
			else if(selectionMode ==2)
				number_queue.Enqueue(Number.random_choosing(75,95));
			else if(selectionMode ==3)
				number_queue.Enqueue(Number.random_choosing(65,90));
			while (true) {
				row = ((int) (Math.random() * 55));
				column = ((int) (Math.random() * 23));
				if ((char)board[column][row] == '#' || (char)board[column][row] == 'P') {
					continue;
				}
				int b = ((int) number_queue.Dequeue());
				Number number = new Number(b, column, row);
				numbers[number_counter] = number;
				number_counter++;
				break;
			}
		}
		//*********************************************************WRITING NUMBERS ON THE SCREEN********************************************************-
		cn.getTextWindow().setCursorPosition(0, 0);
		for(int i=0;i<23;i++) {
			for(int j=0;j<55;j++) {
				if(board[i][j] != '#' ) {
					cn.getTextWindow().setCursorPosition(j, i);
					System.out.print(board[i][j]);
				}
			}
			System.out.println();
		}
		//*********************************************************SLEEP MODE********************************************************
		long start = System.currentTimeMillis();
		long start2=1;
		while (true) {
			p.eatingCheck(numbers);
			p.MatchandScore();
			if (p.getValue()==10) {
				p.setValue(1);	
				p.setSleeping(true);
				start2 = System.currentTimeMillis();
			}
			p.listbackpack();
			//*********************************************************WRITING OF TIME AND SCORE DEFAULT********************************************************
			cn.getTextWindow().setCursorPosition(60, 20);
			System.out.print("Score : ");
			cn.getTextWindow().setCursorPosition(68, 20);
           System.out.println(p.getScore());			
			cn.getTextWindow().setCursorPosition(60, 22);
			System.out.print("Time : ");
			cn.getTextWindow().setCursorPosition(67, 22);
			//*********************************************************ACTIONS TAKEN BY THE PAST TIME********************************************************
			System.out.println((System.currentTimeMillis()-start)/1000);
			long finish = System.currentTimeMillis();
			
			if ((finish - start)% 500 ==0 && finish!=start) {   //************MOVEMENTS OF 4, 5 AND 6 IN 0.5 SECONDS************
				Number.numberMovement(numbers, board, p.isMoved(),p.getValue(),p);
				p.setMoved(false);
				for(int i =0; i<23; i++) {
					for(int j =0; j<55; j++) {
						if(board[i][j] == ' ' || board[i][j] == '.') {
							Game.cn.getTextWindow().setCursorPosition(j,i);
							System.out.print(' ');
						}
					}
				}
				for(int k=0; k<numbers.length; k++) {    //************DRAWING THE PATH************
					if(numbers[k]!= null) {
						if(numbers[k].getNumberValue() > 6 && numbers[k].getPathStack() != null && !numbers[k].getPathStack().isEmpty() ) {
							Stack tempStack = new Stack(600);
							tempStack = Stack.Copy(numbers[k].getPathStack());
							tempStack.pop();
							for(int l=1; l<numbers[k].getPathStack().size();l++) {
								if(board[((int[])tempStack.peek())[0]][((int[])tempStack.peek())[1]] == ' ') {
									Game.cn.getTextWindow().setCursorPosition(((int[])tempStack.peek())[1], ((int[])tempStack.peek())[0]);
									System.out.print('.');
								}
								tempStack.pop();
							}
						}
						
					}
				}
			}
			
			if ((finish-start2)>4000 && p.getSleeping()&& finish!=start2) {   //************EXITING SLEEP MODE************
				p.setSleeping(false);
				p.setValue(2);
			}
//			for(int l=0; l<numbers.length; l++) {
//				if(numbers[l]!= null) {
//					board[numbers[l].getPosx()][numbers[l].getPosy()] = (""+numbers[l].getNumberValue()).charAt(0);
//				}
//			}
			//*********************************************************SLEEP MODE CONTROL********************************************************
			board[p.getPosy()][p.getPosx()] = 'P';
			for (int i = 0; i < 23; i++) {
				for (int j = 0; j < 55; j++) {
					if ((char)board[i][j] != '#') {
						cn.getTextWindow().setCursorPosition(j, i);
						if (board[i][j]=='P') {
							if (p.getSleeping()) {    
								cn.setTextAttributes(attr3);
								System.out.println(p.getValue());
								continue;
							}
							cn.setTextAttributes(attr2);
							System.out.println(p.getValue());
							cn.setTextAttributes(reset);
							continue;						
						}
						if(board[i][j] != ' ') {
							if (board[i][j]=='1' || board[i][j]=='2' ||board[i][j]=='3') {
								cn.setTextAttributes(attr5);
							}
							else if (board[i][j]=='4' || board[i][j]=='5' ||board[i][j]=='6') {
								cn.setTextAttributes(attr1);
							}
							else if (board[i][j]=='7' || board[i][j]=='8' ||board[i][j]=='9') {
								cn.setTextAttributes(attr4);
							}
							//else if(board[i][j] == ' ') continue;
							System.out.println(board[i][j]);
							cn.setTextAttributes(reset);
						}
					}
				}
			}
			//*********************************************************INCREASE THE NUMBER PER INPUT IN 0.5 SECONDS TO A RANDOM PART OF THE BOARD********************************************************
			if ((finish - start)% 5000 ==0 && finish!=start) {
				if(selectionMode ==1)
					number_queue.Enqueue(Number.random_choosing(80,97));
				else if(selectionMode ==2)
					number_queue.Enqueue(Number.random_choosing(75,95));
				else if(selectionMode ==3)
					number_queue.Enqueue(Number.random_choosing(65,90));
				while (true) {
					row = ((int) (Math.random() * 55));
					column = ((int) (Math.random() * 23));
					if ((char)board[column][row] == '#' || (char)board[column][row] == 'P') {
						continue;
					}
					int b = ((int) number_queue.Dequeue());
					Number number = new Number(b, column, row);
					numbers[number_counter] = number;
					number_counter++;
					break;
				}
				cn.getTextWindow().setCursorPosition(60, 3);
				for (int i = 0; i < 10; i++) {
					System.out.print(number_queue.Peek());
					number_queue.Enqueue(number_queue.Dequeue());
				}
				for (int i = 0; i < 15; i++) {
					number_queue.Enqueue(number_queue.Dequeue());
				}
			}
			//*********************************************************ACTION WITH THE DIRECTION KEYS********************************************************
			if (keypr == 1) {
				cn.getTextWindow().setCursorPosition(px, py);
				System.out.println(' ');
				if (rkey == KeyEvent.VK_LEFT && (char)board[py][px - 1] != '#' && !p.getSleeping() ) {
					board[py][px] = ' ';
					p.setPosx(p.getPosx()-1);
					p.setMoved(true);
				}
				else if (rkey == KeyEvent.VK_RIGHT && (char)board[py][px + 1] != '#'  && !p.getSleeping()) {
					board[py][px] = ' ';
					p.setPosx(p.getPosx()+1);
					p.setMoved(true);
				}
				else if (rkey == KeyEvent.VK_UP && (char)board[py - 1][px] != '#' && !p.getSleeping()) {
					board[py][px] = ' ';
					p.setPosy(p.getPosy()-1);
					p.setMoved(true);
				}
				else if (rkey == KeyEvent.VK_DOWN && (char)board[py + 1][px] != '#' && !p.getSleeping()) {
					board[py][px] = ' ';
					p.setPosy(p.getPosy()+1);
					p.setMoved(true);
				}	
				else if (rkey == KeyEvent.VK_W) {
					p.WQPress(true);
				}
				else if (rkey == KeyEvent.VK_Q) {
					p.WQPress(false);
				}
				px=p.getPosx();
				py=p.getPosy();			
				char rckey = (char) rkey;
				cn.setTextAttributes(attr1);
				cn.setTextAttributes(reset);
				keypr = 0; // last action
			}
			//*********************************************************MUSİC********************************************************
			if(p.getValue() == 0) {
				music.stop();
				Music music2=new Music();
				music2.play("mario_end.wav");
				Thread.sleep(4000);
				music2.stop();
				Game.cn.getTextWindow().setCursorPosition(0, 0);
				for (int m = 0; m <26; m++) {
		    		Game.cn.getTextWindow().setCursorPosition(0, m);
					System.out.println("                                                                                    ");
				}
				int i=0;
				//*********************************************************PRINTING THE GAMEOVER LETTER********************************************************
				try {
					File myFile = new File("gameover.txt");
					Scanner myReader1 = new Scanner(myFile,"UTF-8");
					while(myReader1.hasNextLine()) {
						String line = myReader1.nextLine().trim();
						Game.cn.getTextWindow().setCursorPosition(3, i);
						System.out.println(line);
						i++;
					}
					myReader1.close();
				} catch (FileNotFoundException error) {
					System.out.println("An error occured");
					error.printStackTrace();
				}
				break;
			}
		}
	}
}
