
import enigma.core.Enigma;
import java.awt.event.KeyEvent;
import java.awt.Color;
import enigma.console.TextAttributes;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu {
	public static enigma.console.Console cn = Enigma.getConsole("MAZE",85,27,15);
    public static TextAttributes reset = new TextAttributes(Color.WHITE);
    public static TextAttributes attr2 = new TextAttributes(Color.CYAN);      
    public static TextAttributes attr3 = new TextAttributes(Color.RED);
    public static TextAttributes attr4 = new TextAttributes(Color.MAGENTA);
    public static TextAttributes attr5 = new TextAttributes(Color.YELLOW);
    public static KeyListener klis;
    public static int keypr= 0;
    public static int rkey= 0;
    private static int output = 0 ;
       
    
    Menu() throws InterruptedException{
    	int selection = menu();
    	if(selection == 1 ) {
    		int selection2 = innerMenu();
    		if(selection2 == 1)
    			output = 1;
    		else if(selection2 == 2)
    			output = 2;
    		else
    			output = 3;
    	}      		
    	else
    		System.exit(0);
    }
  
	public static int getOutput() {		return output;	}
    public static int menu() throws InterruptedException {
    	int choice = 0 ;
    	
    	try {
			File myFile = new File("menu1.txt");
			Scanner myReader = new Scanner(myFile,"UTF-8");
			while(myReader.hasNextLine()) {
				String line = myReader.nextLine().trim();
				System.out.println(line);
			}
			myReader.close();
		} catch (FileNotFoundException error) {
			System.out.println("An error occured");
			error.printStackTrace();
		}

         cn.setTextAttributes(attr2);
         cn.getTextWindow().setCursorPosition(30, 7);
         System.out.println(" THE MAZE NUMBER");
         cn.setTextAttributes(attr4);
         cn.getTextWindow().setCursorPosition(30, 9);
         System.out.println("    -MAIN MENU-");
         cn.setTextAttributes(attr5);
         cn.getTextWindow().setCursorPosition(30, 11);
         System.out.println("     Play Mode");
         cn.getTextWindow().setCursorPosition(30, 13);
         System.out.println("       Exit");  
         cn.setTextAttributes(reset);

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
         while(true) {  
             if (keypr == 1) {
                 if (rkey == KeyEvent.VK_UP ) {
                 	cn.getTextWindow().setCursorPosition(30, 13);
                     cn.getTextWindow().output("       Exit", attr5);
                     cn.getTextWindow().setCursorPosition(30, 11);
                     cn.getTextWindow().output("     Play Mode", attr3);    
                     choice = 1;
                 }
                 else if (rkey == KeyEvent.VK_DOWN ) {
                 	cn.getTextWindow().setCursorPosition(30, 11);
                     cn.getTextWindow().output("     Play Mode", attr5);
                     cn.getTextWindow().setCursorPosition(30, 13);                
                     cn.getTextWindow().output("       Exit", attr3);  
                     choice = 2;
                 }               
                 else if (rkey == KeyEvent.VK_ENTER ) { 
                	 break;
                 }
                 keypr = 0;
             }
             Thread.sleep(20);
         }
         return choice ;
    }
    
    
    public static int innerMenu() throws InterruptedException {
    	int choice = 0;
    	for (int i = 0; i <24; i++) {
    		cn.getTextWindow().setCursorPosition(0, i);
			System.out.println("                                                                               ");
		}
    	cn.getTextWindow().setCursorPosition(0, 0);
    	try {
			File myFile = new File("menu2.txt");
			Scanner myReader = new Scanner(myFile,"UTF-8");
			while(myReader.hasNextLine()) {
				String line = myReader.nextLine().trim();
				System.out.println(line);
			}
			myReader.close();
		} catch (FileNotFoundException error) {
			System.out.println("An error occured");
			error.printStackTrace();
		}
       	
      cn.setTextAttributes(attr2);
      cn.getTextWindow().setCursorPosition(31, 6);
      System.out.println("  THE MAZE NUMBER  ");
      cn.getTextWindow().setCursorPosition(31, 7);
      System.out.println("                   ");
      cn.setTextAttributes(attr4);
      cn.getTextWindow().setCursorPosition(31, 8);
      System.out.println("   -GAME LEVELS-   ");
      cn.getTextWindow().setCursorPosition(31, 9);
      System.out.println("                   ");
      cn.setTextAttributes(attr5);
      cn.getTextWindow().setCursorPosition(31, 10);
      System.out.println("     Easy Mode     ");
      cn.getTextWindow().setCursorPosition(31, 11);
      System.out.println("                   ");
      cn.getTextWindow().setCursorPosition(31, 12);
      System.out.println("    Normal Mode    ");
      cn.getTextWindow().setCursorPosition(31, 13);
      System.out.println("                   ");
      cn.getTextWindow().setCursorPosition(31, 14);
      System.out.println("     Hard Mode     ");
      cn.getTextWindow().setCursorPosition(31, 15);
      cn.setTextAttributes(reset);
      
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
      keypr = 0;
      int cx = 31,cy=10;
      while(true) {         	  
          if (keypr == 1) {                    
              if(rkey == KeyEvent.VK_UP && cy>8) cy -= 2;
              else if (rkey == KeyEvent.VK_DOWN && cy<12) cy += 2;
              cn.getTextWindow().setCursorPosition(31, 10);
              cn.getTextWindow().output("     Easy Mode    ", attr5);
              cn.getTextWindow().setCursorPosition(31, 12);
              cn.getTextWindow().output("    Normal Mode   ", attr5);
              cn.getTextWindow().setCursorPosition(31, 14);
              cn.getTextWindow().output("     Hard Mode    ", attr5);
              if(cy==8) {
            	  cn.getTextWindow().setCursorPosition(31, 10);
            	  cn.getTextWindow().output("     Easy Mode    ", attr3);
                  choice = 1;
              }
              else if(cy==10)  {
            	  cn.getTextWindow().setCursorPosition(31, 12);
                  cn.getTextWindow().output("    Normal Mode   ", attr3); 
                  choice = 2;
              } 
              else if(cy==12) {
            	  cn.getTextWindow().setCursorPosition(31, 14);
                  cn.getTextWindow().output("     Hard Mode    ", attr3);
                  choice = 3;
              }
              if (rkey == KeyEvent.VK_ENTER ) break;
              keypr = 0;
          }
          Thread.sleep(20);
      }
      return choice;
    }

}
