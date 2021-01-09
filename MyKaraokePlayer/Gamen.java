import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Gamen extends Frame {
    PnList pnList;
    PnPlayer pnPlayer;
    
    Gamen(PnPlayer pnplayer) {
        pnList = new PnList();
        pnList.update("",true);
        pnPlayer = pnplayer;

        setLayout(null);
        setTitle("MyKaraokePlayer");
        setSize(400, 350);
        setMinimumSize(new Dimension(0,99));
        setVisible(true);
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
                    haichi();
                }
            }
        );
        MenuBar m = new MenuBar();//無駄オブジェクト メニューバー分の幅を取ってもらう
        m.add(new Menu());
        setMenuBar(m);
        add(pnList);
        add(pnPlayer);pnPlayer.setBackground(Color.gray);
        try{
          setIconImage(javax.imageio.ImageIO.read(new File("MKPicon1.png")));
        }catch(IOException e){}
    }
    
    public void setMB(MB mb){
        setMenuBar(mb);
    }
    
    public void haichi(){
        pnPlayer.setBounds(0,getHeight()-getInsets().bottom-40,getWidth(),40);
         pnPlayer.haichi();
        pnList.setBounds(0,getInsets().top,getWidth(),pnPlayer.getY()-getInsets().top);
         pnList.haichi();
    }
    
}