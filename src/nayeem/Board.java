package nayeem;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 200;
    private final int B_HEIGHT = 200;
    private final int DELAY = 0;
    
    private boolean inGame = true;
    private boolean isKeyHit = false;
    private boolean isMoved = false;
    
    private Timer timer;
    private Image pic;
    private int dir;
    private int map[][] = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
    private int Log[] = new int[4097];
    private int max_point;
    private int temp[] = new int[4];
    private int game_level;
    public Board() {
        
    	
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.LIGHT_GRAY);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        //ImageIcon iid = new ImageIcon("src/img/pic.png");
    	
    	ImageIcon iid = new ImageIcon(Main.class.getResource("/img/pic.png"));
    	pic = iid.getImage();
    }

    private void initGame() {

    	game_level=1024;
        calclog();
    	max_point = 0;
        newPoint();
        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    private void calclog()
    {
    	int x=0,y=2;
    	while(y<=game_level)
    	{
    		Log[y]=x;
    		y*=2;
    		x++;
    	}
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {
//        	g.drawImage(pic, newx*50, newy*50, newx*50+50,newy*50+50,
//        			50*(newv/4),0,50*(newv/4)+50,50,this );
        	for(int i=0;i<4;i++)
        	{
        		for(int j=0;j<4;j++)
        		{
        			if(map[i][j]!=0)
        			{
        				g.drawImage(pic,j*50,i*50,j*50+50,i*50+50,
        						50*(Log[ map[i][j] ]),0,50*(Log[ map[i][j] ])+50,50,this );
        			}
        		}
        	}
            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
    	
    	
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        if(max_point >= game_level)
        {
        	String msgp=max_point+"!";
        	String msgt="You Win";
        	g.drawString(msgp,(B_WIDTH - metr.stringWidth(msgp)) / 2, B_HEIGHT / 2 -10);
        	g.drawString(msgt,(B_WIDTH - metr.stringWidth(msgt)) / 2, B_HEIGHT / 2 +10);
        	
        }
        else
        {
        	String msg="Game Over";
            g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);	
        }
        
    }
    
    private void compress_temp(){
    	int j=0;
    	for(int i=0;i<4;i++)
    	{
    		if(temp[i]>0)
    		{
    			temp[j]=temp[i];
    			if(j<i) isMoved = true;
    			j++;
    		}
    	}
    	while(j<4)
    	{
    		temp[j]=0;
    		j++;
    	}
    }
    
    private void add_temp(){
    	compress_temp();
    	for(int i=0;i<4;i++)
    	{
    		if(i+1<4 && temp[i]==temp[i+1])
    		{
    			if(temp[i]>0) isMoved = true;
    			temp[i]=temp[i]+temp[i+1];
    			temp[i+1]=0;
    		}
    		if(temp[i]>max_point) max_point=temp[i];
    	}
    	compress_temp();
    }
    private void move() {
    	//dir-> 0=left , 1=right, 2=up, 3=down
    	isMoved = false;
    	if(dir==0)
    	{
    		for(int i=0;i<4;i++)
    		{
    			for(int j=0;j<4;j++) temp[j]=map[i][j];
    			add_temp();
    			for(int j=0;j<4;j++) map[i][j]=temp[j];
    		}
    	}
    	else if(dir==1)
    	{
    		for(int i=0;i<4;i++)
    		{
    			for(int j=3;j>=0;j--) temp[3-j]=map[i][j];
    			add_temp();
    			for(int j=3;j>=0;j--) map[i][j]=temp[3-j];
    		}
    	}
    	else if(dir==2)
    	{
    		for(int j=0;j<4;j++)
    		{
    			for(int i=0;i<4;i++) temp[i] = map[i][j];
    			add_temp();
    			for(int i=0;i<4;i++) map[i][j] = temp[i];
    		}
    	}
    	else if(dir==3)
    	{
    		for(int j=0;j<4;j++)
    		{
    			for(int i=3;i>=0;i--) temp[3-i]=map[i][j];
    			add_temp();
    			for(int i=3;i>=0;i--) map[i][j]=temp[3-i];
    		}
    	}
    	if( isMoved ) newPoint();
    	
//    	System.out.println();
//    	for(int i=0;i<4;i++)
//    	{
//    		for(int j=0;j<4;j++)
//    		{
//    			System.out.print(map[i][j]+" ");
//    		}
//    		System.out.println();
//    	}
    }

    private void newPoint() {
    	int c=0;
    	for(int i=0;i<4;i++)
    	{
    		for(int j=0;j<4;j++)
    		{
    			if(map[i][j]==0)
    			{
    				c++;
    			}
    		}
    	}
    	if(c==0 || max_point>=game_level)
    	{
    		inGame=false;
    		timer.stop();
    		return;
    	}
    	int x=( (int)(Math.random()*100) )%c;
    	c=0;
    	for(int i=0;i<4;i++)
    	{
    		for(int j=0;j<4;j++)
    		{
    			if(map[i][j]==0)
    			{
    				if(c==x)
    				{
    					int z=( (int)(Math.random()*10) )%6;
    					if(z==5) map[i][j]=4;
    					else map[i][j]=2;
    					return;
    				}
    				c++;
    			}
    		}
    	}
    }
    public boolean movable()
    {
    	for(int i=0;i<4;i++)
    	{
    		for(int j=0;j<4;j++)
    		{
    			if(map[i][j]==0)
    			{
    				return true;
    			}
    			if( ( i<3 && map[i+1][j]==map[i][j] ) || (j<3 && map[i][j+1]==map[i][j]) )
    			{
    				return true;
    			}
    		}
    	}
    	return false;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    	if(!movable())
    	{
    		inGame=false;
    	}
        if (inGame) {
        	
            
            if( isKeyHit )
            {
            	isKeyHit=false;
            	move();
            }
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (( key == KeyEvent.VK_LEFT || key=='A' ) ) {
            	isKeyHit=true;
            	dir = 0;
            }

            if ((key == KeyEvent.VK_RIGHT || key=='D') ) {
            	isKeyHit=true;
            	dir = 1;
            }

            if ((key == KeyEvent.VK_UP || key=='W') ) {
            	isKeyHit=true;
            	dir = 2;
            }

            if ((key == KeyEvent.VK_DOWN || key=='S') ) {
            	isKeyHit=true;
            	dir = 3;
            }
        }
    }
}
