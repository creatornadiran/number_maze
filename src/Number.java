public class Number {
	private int numberValue; //value of the number
	private int posx; //x position of number
	private int posy; //y position of number
	private Stack pathStack;
	
	public Number(int numberValue, int posx, int posy) {
		this.numberValue = numberValue;
		this.posx = posx;
		this.posy = posy;
		if(this.numberValue >= 7) //Define path stack if value of the number greater than 7
			this.pathStack = new Stack(1265);
	}
	//getter & setter methods
	public int getNumberValue() {	return numberValue;}
	public void setNumberValue(int numberValue) {this.numberValue = numberValue;}
	public int getPosx() {return posx;}
	public void setPosx(int posx) {this.posx = posx;}
	public int getPosy() {return posy;}
	public void setPosy(int posy) {this.posy = posy;}
	public Stack getPathStack() {return pathStack;}
	public void setPathStack(Stack pathStack) {this.pathStack = pathStack;}
	
	static public int random_choosing(int x,int y) { //This function generates random numbers according to the difficulty level
		int rand = ((int)(Math.random()*100 +1));
		int number;
		if(rand<=x)
			number = ((int)(Math.random()*3 +1));
		else if(rand>=y)
			number = ((int)(Math.random()*3 +7));
		else
			number = ((int)(Math.random()*3 +4));
			
		return number;
	}                                                //[y][x] board
	static int returnIndex (Number [] numbers, int positionX,int positionY) { //This function searches in numbers array by given position and returns number's index
		int returnNumber=-1;
		for (int i = 0; i < numbers.length; i++) {
			if(numbers[i]!=null && numbers[i].posy==positionX && numbers[i].posx==positionY  ) 
				returnNumber = i;											
		}		
		return returnNumber;
	}
	
	static public void numberMovement(Number[] numbers, char[][] board, boolean ismoved, int pvalue, Player p ) {   //Moves all numbers in the numbers array
		int m=-1;
		int n=-1;
		boolean doublebreak=false;
		for (int x = 0; x < 23; x++) {
			for (int y= 0; y < 55; y++) {
				if(board[x][y]=='P') {
					m=x;
					n=y;
					doublebreak=true;
					break;
				}
			}
			if(doublebreak) break;
		}
		for(int k=0; k<numbers.length; k++) { //loop in numbers list
			if(numbers[k]!=null ) {
				if (numbers[k].getNumberValue()>=4 ) { //Only numbers greater than 4 move
					int rand = 0 ;
					int i=numbers[k].getPosx();
					int j=numbers[k].getPosy();
					if((board[i+1][j] != ' ' && board[i+1][j] != 'P') && (board[i-1][j] != ' ' && board[i-1][j] != 'P') && (board[i][j-1] != ' ' && board[i][j-1] != 'P') && (board[i][j+1] != ' ' && board[i][j+1] != 'P'))
						break;
					while(true) {
						rand = ((int)(Math.random()*4));
						if(rand == 0  && numbers[k].getNumberValue()<7  && (board[i+1][j] == ' ' || board[i+1][j] == 'P'))  {
							board[i][j] = ' ';
							numbers[k].setPosx(i+1);
							break;
						}
						else if (rand ==1   && numbers[k].getNumberValue()<7  && (board[i-1][j] == ' ' || board[i-1][j] == 'P'))  {
							board[i][j] = ' ';
							numbers[k].setPosx(i-1);
							break;
						}
						else if (rand == 2  && numbers[k].getNumberValue()<7  && (board[i][j+1] == ' ' || board[i][j+1] == 'P'))  {
							board[i][j] = ' ';
							numbers[k].setPosy(j+1);
							break;
						}
						else if (rand == 3  && numbers[k].getNumberValue()<7  && (board[i][j-1] == ' ' || board[i][j-1] == 'P'))  {
							board[i][j] = ' ';
							numbers[k].setPosy(j-1);
							break;
						}
						if(numbers[k].getNumberValue()>=7) { //Only numbers greater than 6 follow player
							Stack temp = new Stack(600);
							Stack temp2 = new Stack(600);
							if(numbers[k].getNumberValue() <= pvalue) { //Runs away if the number is less than the player value
								numbers[k].escape(board, m,n);
								numbers[k].setPathStack(null);
								break;
							}
							if(ismoved || p.getSleeping()) { //If the player isn't moving don't find a new path
								int max =((int)euclideanDistance(m,n, i, j))*500;
								numbers[k].pathStack = numbers[k].recursive(board, m,n,i,j,temp,max);
								numbers[k].pathStack = fixPath(numbers[k].pathStack);
							}
							if(numbers[k].pathStack== null || numbers[k].pathStack.size() == 0) break;
							int size=numbers[k].pathStack.size();
							for(int l=1; l<size; l++) {
								temp2.push(numbers[k].pathStack.pop());
							}
							char val =board[((int[])numbers[k].pathStack.peek())[0]][((int[])numbers[k].pathStack.peek())[1]];
							if(val == '7' || val == '8' ||val == '9') {
								for(int l=1; l<size; l++) {
									numbers[k].pathStack.push(temp2.pop());
								}
								break;
							}
							board[i][j] = ' ';
							numbers[k].setPosy(((int[])numbers[k].pathStack.peek())[1]);
							numbers[k].setPosx(((int[])numbers[k].pathStack.pop())[0]);
							for(int l=1; l<size; l++) {
								numbers[k].pathStack.push(temp2.pop());
							}
							break;
						}		
					}				
				}
				board[numbers[k].getPosx()][numbers[k].getPosy()] = (numbers[k].getNumberValue()+"").charAt(0);
			}
		}
	}
	public Stack recursive(char[][] board,int posM,int posN, int x, int y, Stack stack, int max) { //Function to find path (recursively)
			  if(stack.size()>max) {
					return null;
				}
				if(posM == x && posN ==  y) {
					return stack;
				}
				double w=euclideanDistance(posM, posN, x-1,y); //Calculates the euclidean distances
			    double a=euclideanDistance(posM, posN, x,y-1);
			    double s=euclideanDistance(posM, posN, x+1,y);
			    double d=euclideanDistance(posM, posN, x,y+1);
				Stack tempStack = new Stack(600);
			    boolean flag=true;
				while(true) { 
					if(w==999 && a==999 && s== 999 && d==999) {
						return null;
					}
					if(w<=a && w<=s && w<=d) {
						if(x>=2 && !isVisited(stack,x-1,y) && (board[x-1][y]==' '|| board[x-1][y]=='P' || board[x-1][y]=='7' || board[x-1][y]=='8' || board[x-1][y]=='9' )){
							if(!flag) {
								x= ((int[])stack.peek())[0];
								y= ((int[])stack.peek())[1];
							}
							int[]loc= {x-1,y};
							stack.push(loc);
							tempStack=Stack.Copy(recursive(board, posM,posN,x-1,y,stack,max));
							if (!stack.isEmpty())
								stack.pop();
							if(tempStack != null) return tempStack;
							else {
								if(flag) flag=false;
								else {
									for (int i = 0; i < 3; i++) {
										if (!stack.isEmpty())
											stack.pop();
									}
								}
								boolean bool = randomStackMov(board,stack,x,y,1);
								if(!bool) return null;
								if(stack != null && !stack.isEmpty()){
									w=euclideanDistance(posM, posN, ((int[])stack.peek())[0]-1,((int[])stack.peek())[1]);
								    a=euclideanDistance(posM, posN, ((int[])stack.peek())[0],((int[])stack.peek())[1]-1);
								    s=euclideanDistance(posM, posN, ((int[])stack.peek())[0]+1,((int[])stack.peek())[1]);
								    d=euclideanDistance(posM, posN, ((int[])stack.peek())[0],((int[])stack.peek())[1]+1);				
								}
								else return null;
								continue;
							}
						}
						else {
							w = 999;
						}
					}
					else if( a<=w && a<=s && a<=d) {
						if(y>=2 && !isVisited(stack,x,y-1) && (board[x][y-1]==' '|| board[x][y-1]=='P' || board[x][y-1]=='7' || board[x][y-1]=='8' || board[x][y-1]=='9' )){
							if(!flag) {
								x= ((int[])stack.peek())[0];
								y= ((int[])stack.peek())[1];
							}
							int[]loc= {x,y-1};
							stack.push(loc);
							tempStack=Stack.Copy(recursive(board, posM,posN,x,y-1,stack,max));
							if (!stack.isEmpty())
								stack.pop();
							if(tempStack != null) return tempStack;
							else {
								if(flag) flag=false;
								else {
									for (int i = 0; i < 3; i++) {
										if (!stack.isEmpty())
											stack.pop();
									}
								}
								boolean bool = randomStackMov(board,stack,x,y,1);
								if(!bool) return null;
								if(stack != null && !stack.isEmpty()){
									w=euclideanDistance(posM, posN, ((int[])stack.peek())[0]-1,((int[])stack.peek())[1]);
								    a=euclideanDistance(posM, posN, ((int[])stack.peek())[0],((int[])stack.peek())[1]-1);
								    s=euclideanDistance(posM, posN, ((int[])stack.peek())[0]+1,((int[])stack.peek())[1]);
								    d=euclideanDistance(posM, posN, ((int[])stack.peek())[0],((int[])stack.peek())[1]+1);				
								}
								else return null;
								continue;
							}
						}
						else {
							a = 999;
						}
					}
					else if( s<=a && s<=w && s<=d) {
						if(!isVisited(stack,x+1,y) && x<21 &&(board[x+1][y]==' '|| board[x+1][y]=='P' || board[x+1][y]=='7' || board[x+1][y]=='8' || board[x+1][y]=='9' )){
							if(!flag) {
								x= ((int[])stack.peek())[0];
								y= ((int[])stack.peek())[1];
							}
							int[]loc= {x+1,y};
							stack.push(loc);
							tempStack=Stack.Copy(recursive(board, posM,posN,x+1,y,stack,max));
							if (!stack.isEmpty())
								stack.pop();
							if(tempStack != null) return tempStack;
							else {
								if(flag) flag=false;
								else {
									for (int i = 0; i < 3; i++) {
										if (!stack.isEmpty())
											stack.pop();
									}
								}
								boolean bool = randomStackMov(board,stack,x,y,1);
								if(!bool) return null;
								if(stack != null && !stack.isEmpty()){
									w=euclideanDistance(posM, posN, ((int[])stack.peek())[0]-1,((int[])stack.peek())[1]);
								    a=euclideanDistance(posM, posN, ((int[])stack.peek())[0],((int[])stack.peek())[1]-1);
								    s=euclideanDistance(posM, posN, ((int[])stack.peek())[0]+1,((int[])stack.peek())[1]);
								    d=euclideanDistance(posM, posN, ((int[])stack.peek())[0],((int[])stack.peek())[1]+1);				
								}
								else return null;
								continue;
							}
						}
						else {
							s = 999;
						}
					}
					else if(d<=a && d<=s && d<=w) {
						if(!isVisited(stack,x,y+1) && y<53 &&(board[x][y+1]==' '|| board[x][y+1]=='P' || board[x][y+1]=='7' || board[x][y+1]=='8' || board[x][y+1]=='9' )) {
							if(!flag) {
								x= ((int[])stack.peek())[0];
								y= ((int[])stack.peek())[1];
							}
							int[]loc= {x,y+1};
							stack.push(loc);
							tempStack=Stack.Copy(recursive(board, posM,posN,x,y+1,stack,max));
							if (!stack.isEmpty())
								stack.pop();
							if(tempStack != null) return tempStack;
							else {
								if(flag) flag=false;
								else {
									for (int i = 0; i < 3; i++) {
										if (!stack.isEmpty())
											stack.pop();
									}
								}
								boolean bool = randomStackMov(board,stack,x,y,1);
								if(!bool) return null;
								if(stack != null && !stack.isEmpty()){
									w=euclideanDistance(posM, posN, ((int[])stack.peek())[0]-1,((int[])stack.peek())[1]);
								    a=euclideanDistance(posM, posN, ((int[])stack.peek())[0],((int[])stack.peek())[1]-1);
								    s=euclideanDistance(posM, posN, ((int[])stack.peek())[0]+1,((int[])stack.peek())[1]);
								    d=euclideanDistance(posM, posN, ((int[])stack.peek())[0],((int[])stack.peek())[1]+1);				
								}
								else return null;
								continue;
							}
						}
						else {
							d = 999;
						}
					}
				}	
	}
	public void escape(char[][] board,int posM,int posN) { //Finds the best way to run away from player (according to euclidean distance)
		int x= this.posx;
		int y= this.posy;
		double w,a,s,d;
		w=euclideanDistance(posM, posN, x-1,y);
	    a=euclideanDistance(posM, posN, x,y-1);;
	    s=euclideanDistance(posM, posN, x+1,y);
	    d=euclideanDistance(posM, posN, x,y+1);
	    while(true) {
	    	if(w ==0 && a==0 && s == 0 && d == 0) break;
	    	if(w>=a && w>=s && w>=d) {
	    		if(board[x-1][y] == ' ') {
	    			this.setPosx(x-1);
	    			board[x][y] = ' ';
	    			break ;
	    		}
	    		else w=0;
	    	}
	    	else if(a>=w && a>=s && a>=d){
	    		if(board[x][y-1] == ' ') {
	    			this.setPosy(y-1);
	    			board[x][y] = ' ';
	    			break;
	    		}
	    		else a=0;
	    	}
	    	else if(s>=w && s>=a && s>=d){
	    		if(board[x+1][y] == ' ') {
	    			this.setPosx(x+1);
	    			board[x][y] = ' ';
	    			break;
	    		}
	    		else s=0;
	    	}
	    	else if(d>=w && d>=s && d>=a){
	    		if(board[x][y+1] == ' ') {
		    		this.setPosy(y+1);
		    		board[x][y] = ' ';
	    			break;
	    		}
	    		else d=0;
	    	}
	    }
	}
	public static boolean randomStackMov(char[][] board, Stack stack,int x, int y ,int depth) { //Adds random movements to path stack (pays attention to walls and other numbers)
		if(depth ==10){
			return false;
		}
		boolean bool=true;
		int count=0;
		int  main_count=0;
		int w=x;
		int v=y;
		while(count!=3) {
			int rand = ((int) (Math.random() * 4));
			if (rand == 0 && (board[w + 1][v] == ' ' || board[w + 1][v] == 'P') && !isVisited(stack,w+1,v)) {
				int[] loc = { ++w, v };
				stack.push(loc);
				count++;
			} else if (rand == 1 && (board[w - 1][v] == ' ' || board[w- 1][v] == 'P') && !isVisited(stack,w-1,v)) {
				int[] loc = { --w, v };
				stack.push(loc);
				count++;
			} else if (rand == 2 && (board[w][v + 1] == ' ' || board[w][v + 1] == 'P') && !isVisited(stack,w,v+1)) {
				int[] loc = { w, ++v };
				stack.push(loc);
				count++;
			} else if (rand == 3 && (board[w][v - 1] == ' ' || board[w][v - 1] == 'P') && !isVisited(stack,w,v-1)) {
				int[] loc = { w, --v };
				stack.push(loc);
				count++;
			}
			main_count++;
			if(main_count >50) {
				for(int i=0; i<count; i++) {
					stack.pop();
				}
				bool = randomStackMov(board, stack, x,y, depth+1);
				break;
			}
			
		}
		return bool;
	}
	public static double euclideanDistance(int posM, int posN, int posX, int posY) { //Calculates euclidean distance
		return Math.sqrt((posM-posX)*(posM-posX)+(posN-posY)*(posN-posY));
	}
	public static boolean isVisited(Stack stack, int x, int y) { //Controls that whether given positions visited before
		Stack temp = new Stack(600);
		temp = Stack.Copy(stack);
		for (int i = 0; i < stack.size(); i++) {
			if (((int[])temp.peek())[0]==x & ((int[])temp.pop())[1]==y) 
				return true;
		}
		return false;	
	}
	public static Stack fixPath(Stack stack) { //Fixes the path (cuts path at intersections)
		if(stack == null) return null;
		if(stack.size() <3) return stack;
		Stack stack1=Stack.Copy(stack);
		Stack stack2=Stack.Copy(stack);
		int count =1;
		while(true) {
			Stack stack3 = new Stack(600);
			Stack stack4 = new Stack(600);
			boolean flag=false;
			while(!stack2.isEmpty())
				stack3.push(stack2.pop());
			if(count > stack1.size()-3) break;
     		int loop = stack1.size() - (stack4.size() +1);
			for(int i =0; i<count; i++) {
				stack4.push(stack1.pop());
			}
			for(int i =0; i<loop; i++) {
				stack2.push(stack3.pop());
				if(euclideanDistance(((int[])stack4.peek())[0],((int[])stack4.peek())[1],((int[])stack2.peek())[0],((int[])stack2.peek())[1]) == 1) {
					flag= true;
					break;
				}
			}
			if(flag) {
				while(!stack2.isEmpty() &&  !stack4.isFull()) {
					stack4.push(stack2.pop());
				}
				while(!stack1.isEmpty())
					stack1.pop();
				while(!stack4.isEmpty())
					stack1.push(stack4.pop());
				stack2=Stack.Copy(stack1);
			}
			else {
				while(!stack3.isEmpty())
					stack2.push(stack3.pop());
				while(!stack4.isEmpty())
					stack1.push(stack4.pop());
			}
			count++;
		}
		return stack1;
	}
}
