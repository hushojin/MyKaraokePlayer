import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class MKPWindow extends Frame implements StringDisplay{
    private ListPanel listPanel;
    private PlayerPanel playerPanel;
    MKPWindow() {
        DataLibrary library = new DataLibrary();
        DataPlayer player = new DataPlayer();
        listPanel = new ListPanel(player,library);
        playerPanel = new PlayerPanel(player);
        player.setStringDisplay(this);
        
        setLayout(null);
        setTitle("MyKaraokePlayer");
        setSize(400, 350);
        setMinimumSize(new Dimension(200,99));
        setMenuBar(new MKPMenuBar(library));
        add(listPanel);
        add(playerPanel);
        addWindowListener(
            new WindowAdapter(){
                public void windowClosing(WindowEvent e){
                    System.exit(0);
                }
            }
        );
        addComponentListener(
            new ComponentAdapter(){
                public void componentResized(ComponentEvent e){
                    selfLayout();
                }
            }
        );
        try{
          setIconImage(javax.imageio.ImageIO.read(new File("MKPicon1.png")));
        }catch(IOException e){}
        
        setVisible(true);
    }
    
    public void selfLayout(){
        playerPanel.setBounds(0,getHeight()-getInsets().bottom-40,getWidth(),40);
        playerPanel.selfLayout();
        listPanel.setBounds(0,getInsets().top,getWidth(),playerPanel.getY()-getInsets().top);
        listPanel.selfLayout();
    }
    
    public void setString(String str){
        setTitle(str);
    }
}