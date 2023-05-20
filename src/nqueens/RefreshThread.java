package nqueens;

public class RefreshThread implements Runnable {
    @Override
    public void run() {
        while(true)
        {
            GUI.javaF.repaint();
            try {
                Thread.sleep(1000);
            } catch(Exception e){}
        }
    }
}
