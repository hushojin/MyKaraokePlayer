import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class MKPWindow extends Frame implements StringDisplay{
    ListPanel pnList;
    PlayerPanel pnPlayer;
    MKPWindow() {
        pnList = new ListPanel();
        pnList.update("",true);
        pnPlayer = new PlayerPanel();

        setLayout(null);
        setTitle("MyKaraokePlayer");
        setSize(400, 350);
        setMinimumSize(new Dimension(200,99));
        setMenuBar(new MKPMenuBar());
        add(pnList);
        add(pnPlayer);
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
        
        DataPlayer.setStringDisplay(this);
        setVisible(true);
    }
    
    public void selfLayout(){
        pnPlayer.setBounds(0,getHeight()-getInsets().bottom-40,getWidth(),40);
        pnPlayer.selfLayout();
        pnList.setBounds(0,getInsets().top,getWidth(),pnPlayer.getY()-getInsets().top);
        pnList.selfLayout();
    }
    
    public void setString(String str){
        setTitle(str);
    }
}