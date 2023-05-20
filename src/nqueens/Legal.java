package nqueens;

public class Legal {
    //i is the location of the last queen placed
    public static boolean possibleQ(int i, int[][] queenBoard){
        int r = i / Search.s, c = i % Search.s;
        int temp = 1;
        try{//while there is no queen in up and left (diagonal)
            while(0 == queenBoard[r-temp][c-temp]) 
                temp++;         
            if (1 == queenBoard[r-temp][c-temp]){
                return false;
            }   
        } catch(Exception e){} 
        
        temp = 1;

        try{//while there is no queen in up 
            while(0 == queenBoard[r-temp][c]) 
                temp++;         
            if (1 == queenBoard[r-temp][c]){
                return false;
            }   
        } catch(Exception e){} 
        
        temp = 1;
        
        try{//while there is no queen in up and right (diagonal)
            while(0 == queenBoard[r-temp][c+temp]) 
                temp++;         
            if (1 == queenBoard[r-temp][c+temp]){
                return false;
            }   
        } catch(Exception e){} 
                 
        /*The other directions will be useless as we start from left one by one*/
        return true;
    }
}
