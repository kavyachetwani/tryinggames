import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
       int boardWidth = 600;
       int boardHeight = boardWidth;
       //both height and width are set to 600 pixels 
       //these dimensions include the title bar containing the name of the game

       JFrame frame = new JFrame("Snake");
       frame.setVisible(true);
       frame.setSize(boardWidth, boardHeight);
       frame.setLocationRelativeTo(null);
       //this sets our window at the centre of our screen 
       frame.setResizable(false);
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       //program terminates when the X button on the window is clicked



       SnakeGame snakegame = new SnakeGame(boardWidth, boardHeight);
       frame.add(snakegame);
       frame.pack();
       //it places thes JPanel inside the frame with the full dimensions 

       //we partition the 600 pixels into 24 rows and 24 columns
       //each block 25x25 pixels 


       snakegame.requestFocus();
       //this shows that our snake game will be the one listening for the key presses
       //THE SNAKE IF MOVING AHEAD SHOULD NOT BE ABLE TO MOVE BACKWARDS BECAUSE IT WILL
       //RUN INTO IT'S OWN BODY, FIXING THAT IN KEYPRESSED METHOD 
    }
}
