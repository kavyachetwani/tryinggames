import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
//this will be used to keep track of the snake's body
import java.util.Random;
//this will be used to present food at random points on the screen
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private class Tile{
        int x;
        int y;
         
        Tile(int x, int y)
        {
            this.x = x;
            this.y = y;
    
        }
        //this class is created to keep a track of all the tiles 
        //in order to draw a tile on the window, we need to specify x, y, width and height 
        //the x and y coordinates begin at the left hand top corner AKA that's origin
        //this makes the bottom right corner 600x600
    }

    
    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    Tile snakeHead;
    ArrayList<Tile> snakeBody;

    Tile food;
    Random random;


    //game logic
    Timer gameloop;
    int velocityX;
    int velocityY;

    boolean gameOver = false;


    SnakeGame(int boardWidth, int boardHeight)
    {  
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black); 

        addKeyListener(this);
        setFocusable(true);


        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10, 10);
        random = new Random();
        placeFood();


        velocityX = 0;
        velocityY = 0;
        //if it were a negative number, the snake would move to the left/upward

        gameloop = new Timer(100, this);
        //first arguement is the time period and the second arguement is the action to do after the time period
        gameloop.start();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g)
    {
        //Grid
        /*for(int i = 0; i < boardWidth/tileSize; i++)
        {
            //x1, y1, x2, y2
            g.setColor(Color.gray);
            //color of lines
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            //veritcal line therefore y axis coordinate changes
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
            //horizontal line
        }*/

        //Food
        g.setColor(Color.red);
        //g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);
        //the 3D fill separates each tile in the snake's body and shows it 

        //SnakeHead
        g.setColor(Color.green);
        //g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        //Snake Body
        for(int i = 0; i < snakeBody.size(); i++)
        {
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize, true);
        }


        //Score 
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameOver)
        {
            g.setColor(Color.red);
            g.drawString("Game Over : " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);

        }
        else
        {
            g.drawString( "Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood()
    {
        food.x = random.nextInt(boardWidth/tileSize);
        food.y = random.nextInt(boardHeight/tileSize);    
    }

    //function to detect collision between the snake's head and the food
    public boolean collision(Tile tile1, Tile tile2)
    {
        //all we need to do is check if they have the same x and y position
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }




    public void move()
    {
        //eating food
        if(collision(snakeHead, food))
        {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //Snake body
        for(int i = snakeBody.size() - 1; i >=0; i --)
        {
            Tile snakePart = snakeBody.get(i);
            if(i == 0)
            //this is if it is the first tile after the snake head
            {
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } 
            else
            {
                Tile prevSnakePart = snakeBody.get(i -1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;


        //Game over conditions
        //first is the snake collides with it's own body
        for(int i = 0; i < snakeBody.size(); i++)
        {
            Tile snakePart = snakeBody.get(i);
            if(collision(snakeHead, snakePart))
            {
                gameOver = true;
            }
        }

        //second condittion is that the snake hits one of the 4 walls 
        if(snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth 
        || snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight)
        {
            gameOver = true;
        }

    
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver)
        {
            gameloop.stop();
        }

        //this action will basically call the draw function over and over again
    }

    

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1)
        {
            velocityX = 0;
            velocityY = -1;
        }
        else if( e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX !=-1)
        {
            velocityX = 1;
            velocityY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1)
        {
            velocityX = -1;
            velocityY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1)
        {
            velocityX = 0;
            velocityY = 1;
        }

    }



    //do not need these 
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }



}