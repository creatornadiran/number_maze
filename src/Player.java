import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import enigma.console.TextAttributes;

public class Player {
	private int value;
	private Stack rightStack;
	private Stack rightStackTemp;
	private Stack leftStack;
	private Stack leftStackTemp;
	private int posx;
	private int posy;
	private int score = 0;
	private boolean sleeping;
	private boolean moved;
	public TextAttributes reset = new TextAttributes(Color.WHITE);
	TextAttributes attr5 = new TextAttributes(Color.RED);
	//*********************************************************CONSTRUCTOR*********************************************************
	public Player() {
		this.value = 5;
		this.rightStack = new Stack(8);
		this.leftStack = new Stack(8);
		this.rightStackTemp = new Stack(8);
		this.leftStackTemp = new Stack(8);
		this.posx = 5;
		this.posy = 5;
		this.moved = false;
	}
	//*********************************************************GET-SET*********************************************************
	public int getValue() {		return value;	}
	public void setValue(int value) {		this.value = value;	}
	public Stack getRightStack() {	return rightStack;}
	public void setRightStack(Stack rightStack) {		this.rightStack = rightStack;	}
	public Stack getLeftStack() {		return leftStack;	}
	public void setLeftStack(Stack leftStack) {		this.leftStack = leftStack;	}
	public int getPosx() {		return posx;	}
	public void setPosx(int posx) {		this.posx = posx;	}
	public int getPosy() {		return posy;	}
	public void setPosy(int posy) {		this.posy = posy;	}
	public int getScore() {	  return score;  }
	public void setScore(int score) {		this.score = score;	}
	public boolean getSleeping() {		return sleeping;	}
	public void setSleeping(boolean sleeping) {		this.sleeping = sleeping;	}
	public boolean isMoved() {return moved;}
	public void setMoved(boolean moved) {this.moved = moved;}
	//**********************************DELETING THE EATING ELEMENT FROM BOARD AND ADD TO STACK******************************************
	public void boardElement(Number number) {
		Game.cn.getTextWindow().setCursorPosition(8, 24);
		System.out.println("                                  ");
		if (number.getNumberValue() <= this.value) {
            LeftisFull();
			leftStack.push(number.getNumberValue());
		}
	}
	//**********************************EATING BY THE PLAYER BY THE MOVEMENT OF THE NUMBERS -OR- PLAYER EAT NUMBERS******************************************
	public void eatingCheck(Number[] numbers) throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
		for(int i=0; i<numbers.length; i++) {
			if(numbers[i] != null) {
				if(numbers[i].getPosy() == this.posx && numbers[i].getPosx() == this.posy) {		
					if(this.getValue()>=numbers[i].getNumberValue()) {
						this.boardElement(numbers[i]);
						numbers[Number.returnIndex(numbers,posx,posy)]=null;
					}
					else {
						this.setValue(0);
					}
				}
			}
		}
	}
	//**********************************THE PLAYER'S NUMBERS IS UPDATED AND WRITTEN ON THE SCREEN EVERY EATING******************************************
	public void listbackpack() {
		int row = 15;
		for (int i = 7; i < 16; i++) {
			Game.cn.getTextWindow().setCursorPosition(62, i);
			System.out.println(" ");
			Game.cn.getTextWindow().setCursorPosition(70, i);
			System.out.println(" ");
		}
		while (!leftStack.isEmpty()) {
			leftStackTemp.push(leftStack.pop());
		}
		while (!leftStackTemp.isEmpty()) {
			Game.cn.getTextWindow().setCursorPosition(62, row);
			TextAttributes attr2 = new TextAttributes(Color.YELLOW);
			Game.cn.setTextAttributes(attr2);
			System.out.println(leftStackTemp.peek());
			Game.cn.setTextAttributes(reset);
			leftStack.push(leftStackTemp.pop());
			row--;
		}
		row = 15;
		while (!rightStack.isEmpty()) {
			rightStackTemp.push(rightStack.pop());
		}
		while (!rightStackTemp.isEmpty()) {
			Game.cn.getTextWindow().setCursorPosition(70, row);
			TextAttributes attr2 = new TextAttributes(Color.YELLOW);
			Game.cn.setTextAttributes(attr2);
			System.out.println(rightStackTemp.peek());
			Game.cn.setTextAttributes(reset);
			rightStack.push(rightStackTemp.pop());
			row--;
		}
	}
	//**********************************UPDATING THE STACKS WHEN W OR Q OR PRESSED******************************************
	public void WQPress(boolean left) {
		if (left) {
			if (!leftStack.isEmpty()) {
				RightisFull();
				rightStack.push(leftStack.pop());
			} else {
				Game.cn.getTextWindow().setCursorPosition(8, 24);
				Game.cn.setTextAttributes(attr5);
				System.out.println("Left BackPack is empty.");
				Game.cn.setTextAttributes(reset);
			}
		}
		else {
			if (!rightStack.isEmpty()) {
				LeftisFull();
				leftStack.push(rightStack.pop());
			} else {
				Game.cn.getTextWindow().setCursorPosition(8, 24);
				Game.cn.setTextAttributes(attr5);
				System.out.println("Right BackPack is empty.");
				Game.cn.setTextAttributes(reset);
			}
		}
		listbackpack();
	}
	//**********************************CONTROLLED WHEN W OR Q OR PRESSED. INCREASE THE SCORE IF AVAILABLE.******************************************
	public void MatchandScore() {
		int min = 0;
		int matchNumber = 0;
		if (leftStack.size() <= rightStack.size()) {
			min = leftStack.size();
		} else {
			min = rightStack.size();
		}
		while (!leftStack.isEmpty()) {
			leftStackTemp.push(leftStack.pop());
		}
		while (!rightStack.isEmpty()) {
			rightStackTemp.push(rightStack.pop());
		}
		for (int i = 0; i < min; i++) {
			if ((int) leftStackTemp.peek() == (int) rightStackTemp.peek()) {
				matchNumber = (int) leftStackTemp.pop();
				rightStackTemp.pop();
			} else {
				leftStack.push(leftStackTemp.pop());
				rightStack.push(rightStackTemp.pop());
			}
		}
		while (!leftStackTemp.isEmpty()) {
			leftStack.push(leftStackTemp.pop());
		}
		while (!rightStackTemp.isEmpty()) {
			rightStack.push(rightStackTemp.pop());
		}
		if (matchNumber == 1 || matchNumber == 2 || matchNumber == 3) {
			score += matchNumber;
			this.sleeping = false;
			this.setValue(this.getValue() + 1);                                  //PLAYER'S VALUE INCREASE IN MATCH STATUS
		} else if (matchNumber == 4 || matchNumber == 5 || matchNumber == 6) {
			score += (matchNumber * 5);
			this.sleeping = false;
			this.setValue(this.getValue() + 1);                                  //PLAYER'S VALUE INCREASE IN MATCH STATUS
		} else if (matchNumber == 7 || matchNumber == 8 || matchNumber == 9) {
			score += (matchNumber * 25);
			this.sleeping = false;
			this.setValue(this.getValue() + 1);			                         //PLAYER'S VALUE INCREASE IN MATCH STATUS
		}

	}
	//**********************************CHECKING THE FILLING OF THE LEFT BACKPACK******************************************
	public void LeftisFull() {
		if (leftStack.isFull()) {
			leftStack.pop();
		}
	}
	//**********************************CHECKING THE FILLING OF THE RIGHT BACKPACK******************************************
	public void RightisFull() {
		if (rightStack.isFull()) {
			rightStack.pop();
		}
	}

}
