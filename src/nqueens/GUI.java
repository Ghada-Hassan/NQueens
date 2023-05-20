package nqueens;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI extends JPanel {
    static JFrame javaF = new JFrame("NQueens");
    static GUI javaUI = new GUI();
    static int activeQueenBoard[][]=new int[Search.s][Search.s];
    static Thread SearchThread;
    static int currentHKey=-1;
    static double squareSize;//the size of a chess board square
    public static void main(String[] args) {
        javaF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        javaF.add(javaUI);
        javaF.setSize(750,512);
        javaF.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-javaF.getWidth())/2,
                (Toolkit.getDefaultToolkit().getScreenSize().height-javaF.getHeight())/2);
                //places the frame in the middle of the screen
        javaF.setVisible(true);
        squareSize=(double)(Math.min(javaUI.getHeight(), javaUI.getWidth()-240)-20)/Search.s;
        //resizes the board squares according to the frame size
        Thread TimerThread=new Thread(new RefreshThread());
        TimerThread.start();
        computerThink();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(new Color(200, 100, 0));
        KeyboardFocusManager.getCurrentKeyboardFocusManager().
                addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID()==KeyEvent.KEY_PRESSED) {
                    if (!Search.HMapSolutions.isEmpty()) {
                        if (e.getKeyCode()==KeyEvent.VK_DOWN) {
                            int length=Search.HMapSolutions.size();
                            if (length-1==currentHKey) {
                                currentHKey=0;
                                getSolution();
                            } else {
                                currentHKey++;
                                getSolution();
                            }
                        }
                        if (e.getKeyCode()==KeyEvent.VK_UP) {
                            int length=Search.HMapSolutions.size();
                            if (currentHKey==0) {
                                currentHKey=length-1;
                                getSolution();
                            } else {
                                currentHKey--;
                                getSolution();
                            }
                        }
                    }
                    if (e.getKeyCode()==KeyEvent.VK_RIGHT) {
                        Search.s++;
                        newDimension();
                    }
                    if (e.getKeyCode()==KeyEvent.VK_LEFT) {
                        if (Search.s>4) {
                            Search.s--;
                            newDimension();
                        }
                    }
                }
                return true;
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                squareSize=(double)(Math.min(getHeight(), getWidth()-240)-20)/Search.s;
            }
        });
        if (Search.s%2==0) {
            for (int i=0;i<Search.s*Search.s;i+=2) {//draw chess board
                g.setColor(new Color(255, 200, 100));
                g.fillRect((int)((i%Search.s+(i/Search.s)%2)*squareSize)+10, (int)((i/Search.s)*squareSize)+10, (int)squareSize, (int)squareSize);
                g.setColor(new Color(150, 50, 30));
                g.fillRect((int)(((i+1)%Search.s-((i+1)/Search.s)%2)*squareSize)+10, (int)(((i+1)/Search.s)*squareSize)+10, (int)squareSize, (int)squareSize);
            }
        } else {
            for (int i=0;i<Search.s*Search.s-1;i+=2) {//draw chess board
                g.setColor(new Color(255, 200, 100));
                g.fillRect((int)((i%Search.s)*squareSize)+10, (int)((i/Search.s)*squareSize)+10, (int)squareSize, (int)squareSize);
                g.setColor(new Color(150, 50, 30));
                g.fillRect((int)(((i+1)%Search.s)*squareSize)+10, (int)(((i+1)/Search.s)*squareSize)+10, (int)squareSize, (int)squareSize);
            }
            int i=Search.s*Search.s-1;
            g.setColor(new Color(255, 200, 100));
            g.fillRect((int)((i%Search.s)*squareSize)+10, (int)((i/Search.s)*squareSize)+10, (int)squareSize, (int)squareSize);
        }
        g.setColor(new Color(100, 0, 0));
        g.fill3DRect(0, 10, 10, (int)(Search.s*squareSize), true);
        g.fill3DRect((int)(Search.s*squareSize)+10, 10, 10, (int)(Search.s*squareSize), true);
        g.fill3DRect(10, 0, (int)(Search.s*squareSize), 10, true);
        g.fill3DRect(10, (int)(Search.s*squareSize)+10, (int)(Search.s*squareSize), 10, true);
        g.setColor(Color.BLACK);
        g.fill3DRect(0, 0, 10, 10, true);
        g.fill3DRect((int)(Search.s*squareSize)+10, 0, 10, 10, true);
        g.fill3DRect(0, (int)(Search.s*squareSize)+10, 10, 10, true);
        g.fill3DRect((int)(Search.s*squareSize)+10, (int)(Search.s*squareSize)+10, 10, 10, true);
        
        Image chessPieceImage;
        chessPieceImage=new ImageIcon(System.getProperty("user.dir")+"\\ChessPieces.png").getImage();
        for (int i=0;i<Search.s*Search.s;i++) {
            int j=-1,k=-1;
            if (activeQueenBoard[i/Search.s][i%Search.s]==1) {j=1;k=0;}//k=1 is the other colored queen
            if (j!=-1 && k!=-1) {
                g.drawImage(chessPieceImage, (int)((i%Search.s)*squareSize)+10, (int)((i/Search.s)*squareSize)+10, (int)((i%Search.s+1)*squareSize)+10, (int)((i/Search.s+1)*squareSize)+10, j*64, k*64, (j+1)*64, (k+1)*64, this);
            }
        }
        g.setColor(Color.BLACK);
        Font fontDepth=new Font("Serif", Font.PLAIN, 20);
        g.setFont(fontDepth);
        int x=(int)(Search.s*squareSize)+20+10;
        int y=20;
        String intType;
        switch (currentHKey+1) {//in order of likelyhood of that piece being selected
            case 1: intType="st";
                break;
            case 2: intType="nd";
                break;
            case 3: intType="rd";
                break;
            default: intType="th";
                break;
        }
        g.drawString("STATS",x+60,y);
        g.drawString("1) Grid size: "+Search.s+"x"+Search.s,x,y+2*g.getFont().getSize());
        if (Search.doneSearch) {
            g.drawString("2) There are "+Search.progress+" solutions.",x,y+3*g.getFont().getSize());
            g.drawString("3) "+Search.totalNodes+" positions were",x,y+4*g.getFont().getSize());
            g.drawString("considered.",x,y+5*g.getFont().getSize());
            g.drawString("4) That took "+((Search.endTime-Search.startTime)/1000)+" seconds.",x,y+6*g.getFont().getSize());
            //g.drawString("6) There are "+Search.HMapUnique.size()+" unique solutions.",x,y+17*g.getFont().getSize());
        } else {
            g.drawString("2) Currently, "+Search.progress+" solutions",x,y+3*g.getFont().getSize());
            g.drawString("have been found.",x,y+4*g.getFont().getSize());
            g.drawString("3) "+Search.totalNodes+" positions have",x,y+5*g.getFont().getSize());
            g.drawString("been considered.",x,y+6*g.getFont().getSize());
            g.drawString("4) "+((System.currentTimeMillis()-Search.startTime)/1000)+" seconds",x,y+7*g.getFont().getSize());
            g.drawString("have elapsed so far.",x,y+8*g.getFont().getSize());
        }
        g.drawString("5) You are currently looking at",x,y+10*g.getFont().getSize());
        g.drawString("the "+(currentHKey+1)+intType+" solution.",x,y+11*g.getFont().getSize());
        
        g.setColor(Color.WHITE);
        g.drawString("UP/DOWN arrow keys",x,y+13*g.getFont().getSize());
        g.drawString("navigate through solutions",x,y+14*g.getFont().getSize());
        g.drawString("RIGHT/LEFT arrow keys",x,y+15*g.getFont().getSize());
        g.drawString("adjust grid size",x,y+16*g.getFont().getSize());
    }
    public static void computerThink() {
        activeQueenBoard=new int[Search.s][Search.s];
        SearchThread=new Thread(new Search());
        SearchThread.start();
        javaF.repaint();
    }
    public static void newDimension() {
        computerThink();
        squareSize=(double)(Math.min(javaUI.getHeight(), javaUI.getWidth()-240)-20)/Search.s;
    }
    public static void getSolution() {
        activeQueenBoard=(int[][])Search.HMapSolutions.get(currentHKey);
        javaF.repaint();
    }
}