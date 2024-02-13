import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {
    private class Card {
        String value;
        String type;

        Card(String value, String type)
        {
            this.value = value;
            this.type = type;
        }

        public String toString()
        {
            return value + "-" + type;
        }
         public int getValue()
        {
            if("AJQK".contains(value))
            {
                if(value == "A")
                {
                    return 11;
                }
                return 10;

            }
            //this is if it is a an integer card
            return Integer.parseInt(value);
        }

        public boolean isAce()
        {
            return value == "A";
        }

        public String getImagePath()
        {
            return "./cards/" + toString() + ".png";
        }
        
    }
    
    ArrayList<Card> deck;
    Random random = new Random();//this is for shuffling deck


    //dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    //player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;




    //window
    int boardWidth = 600;
    int boardHeight = boardWidth;

    int cardWidth = 110; //ratio should be 1/1.4 for clear good images
    int cardHeight = 154;


    JFrame frame = new JFrame("BlackJack");
    JPanel gamePanel = new JPanel()
        {
                @Override
                public void paintComponent(Graphics g)
                {
                    super.paintComponent(g);
                    
                    try{
                        //drawing hidden component 
                        
                        Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                        if(!stayButton.isEnabled())
                        {
                            addTimeDelay(200);
                            hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                        }
                        g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);
    
                        //drawing dealer's hand
                        for (int i = 0; i < dealerHand.size(); i++)
                        {
                            addTimeDelay(300); 
                            Card card = dealerHand.get(i);
                            
                            Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                            //here the image path can vary and therefore we define a new function in the cards class
                            g.drawImage(cardImg, cardWidth + 25 + (cardWidth+5)*i, 20, cardWidth, cardHeight, null);
                            //20 for width left before hidden card, then card width and then 5 for the space btwn the two cards
                            //for every subsequent card we do cardwidth times i, where i is the number of cards and then plus 5
                            
                        }
                       
                        
    
                        //drawing player's hand
                        for(int i = 0; i < playerHand.size(); i ++)
                        {
                            
                             Card card = playerHand.get(i);
                             Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                             g.drawImage(cardImg, 20 + (cardWidth + 5)*i, 320, cardWidth, cardHeight, null);
                             //first card starts from 20 and then 5 space between each consequent card
                             
                        }
                        
    
                        if(!stayButton.isEnabled())
                        //therefore if we click on the staybutton we are disabling it and therefore the game from our side is over
                        //and the scores can be calculated
                        {
                            
                            dealerSum = reduceDealerSum();
                            playerSum = reducePlayerAce();
                            System.out.println("STAY : ");
                            System.out.println("DEALER SUM : " + dealerSum);
                            System.out.println("PLAYER SUM : " + playerSum);
                            
                            addTimeDelay(00);
                            
                            String message = " ";
                            if(playerSum > 21)
                            {
                                addTimeDelay(300);
                                message = "BUST!";
                            }
                            else if(dealerSum > 21)
                            {
                                addTimeDelay(300);
                                message = "YOU WIN!";
                            }
                            //otherwise both the player and the dealer have less than 21 points and therfore we compare their values
                            else if(playerSum > dealerSum)
                            {
                                addTimeDelay(300);
                                message = "YOU WIN!";
                            }
                            else if(dealerSum > playerSum)
                            {
                                addTimeDelay(300);
                                message = "YOU LOSE!";
                            }
                            else 
                            {
                                addTimeDelay(300);
                                message = "IT'S A TIE!";
                            }
                            
                            
                            g.setFont(new Font("Arial", Font.PLAIN, 30));
                            g.setColor(Color.white);
                            g.drawString(message, 230, 270);
                        }
                    
                } catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
    };
    //this is what is used for drawing
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");//this is text displayed on the button
    JButton stayButton = new JButton("Stay");

    
    BlackJack()
    {
        startGame();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        //this will open the window at the centre of our screen 
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        frame.add(buttonPanel, BorderLayout.SOUTH); 

        hitButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e )
            {
                
                Card card = deck.remove(deck.size() - 1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);
                if(reducePlayerAce() > 21)
                {
                    hitButton.setEnabled(false);
                }
                addTimeDelay(100);
                gamePanel.repaint();
                //this method is going to call paint component because everytime we change something 
                //in the game we need to update the game panel to reflect the changes
                
            }
            
        }); 

        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e )
            {
                
               hitButton.setEnabled(false);
               stayButton.setEnabled(false);
               
               while(dealerSum < 17)
               {
                addTimeDelay(100);
                Card card = deck.remove(deck.size() - 1);
                dealerSum += card.getValue();
                dealerAceCount += card.isAce()? 1: 0;
                dealerHand.add(card);

               }
               addTimeDelay(100);
               gamePanel.repaint();
            }
        });
        
        gamePanel.repaint();
         
    }
    public void startGame()
    {
        //deck
        buildDeck();
        shuffleDeck();

        


        //dealer
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1); //remove card at last index
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce()? 1 : 0;

        Card card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce()? 1 : 0;
        dealerHand.add(card);

        System.out.println("DEALER: ");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);

        //player
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;
        for(int i = 0 ; i < 2; i++)
        {
            card = deck.remove(deck.size() - 1);
            playerSum += card.getValue();
            playerAceCount += card.isAce()? 1 : 0;
            playerHand.add(card);
            
        }
        System.out.println("PLAYER: ");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);
    }


    public void shuffleDeck()
    {
        for(int i = 0; i < deck.size(); i++)
        {
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
            //the two cards are swapped
        }

        System.out.println("AFTER SHUFFLING: ");
        System.out.println(deck);
    }




    public void buildDeck()
    {
        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for(int i = 0; i < types.length; i++ )
        {
            for(int j = 0; j < values.length; j++)
            {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }

        System.out.println("BUILDING DECK: ");
        System.out.println(deck);
    }

    public int reducePlayerAce()
    {
        while(playerSum > 21 && playerAceCount > 0)
        {
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }
    //creating came function for the dealer 
    public int reduceDealerSum()
    {
        while (dealerSum > 21 && dealerAceCount > 0) 
        {
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }
   
    public void addTimeDelay(int time)
    {
        try{
            Thread.sleep(time);
        }
        catch(InterruptedException evnt)
        {

        }
    }


}