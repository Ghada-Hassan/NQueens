package nqueens;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.util.*;

public class Search implements Runnable {
    static int s = 4 ; //s: size of the board
    static HashMap HMapSolutions = new HashMap();
    static int progress; //ay variable static byb2a el initial bta3o b 0
    static int solutions;
    static boolean doneSearch;//indicates if the thread should terminate or not
    static int totalNodes;//the number of position analyzed
    static long startTime, endTime;
    
    public static void masterController() {
        doneSearch = false;
        totalNodes = 0;
        HMapSolutions.clear();
        GUI.currentHKey = -1;
        progress = 0;
        solutions = 0;
        guess(0, 0, new int[s][s]);
        //unique();
        doneSearch = true;
        
    }
    
    public static void guess(int x, int y, int[][]queenBoard){
        while(y < s){
            queenBoard[x][y] = 1;
            totalNodes++;
            if(Legal.possibleQ(x*s + y, queenBoard)){/*awel makan byb2a f 1 b3d kda byro7 lel makan 10 fa ka2eno drb 1 f size el board ely hwa 8 w zawed 3liha rkm el row ely hwa 2 fa b2eit 10 */
                if(x+1 == s){/*kol row 5ls fa kda yb2a deh to3tbr answer mzbota*/
                   int queenBoard2 [][] = new int[s][s];
                   for(int i = 0; i < s; i++){System.arraycopy(queenBoard[i], 0, queenBoard2[i], 0, s);}/*source array,starting position,destination array,starting position,length of array elements to be copied*/
                   HMapSolutions.put(progress, queenBoard2 );
                   progress++;
                   if (GUI.currentHKey == -1) {
                       GUI.currentHKey = 0;
                       GUI.getSolution();
                   }
                   return;    
                }
                guess(x+1, 0, queenBoard);//btnzl lel row ely b3do w tbd2 mn awel column fih
            }
            queenBoard[x][y]=0;//clean board
            y++;//abd2 mn makan mo5tlf
        }
        
    }
    
    @Override
    public void run() {
        GUI.javaF.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        startTime = System.currentTimeMillis();
        masterController();
        endTime = System.currentTimeMillis();
        Toolkit.getDefaultToolkit().beep();
        GUI.javaF.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        GUI.javaF.repaint();
    }
}
