import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

public class PnList extends Panel{
    PnList pn = this;
    CDPlayer cdp;
    Tf tf = new Tf(this);
    Panel pnSwitch = new Panel(null);
    CBG cbg = new CBG(this);
    LsList lsl;
    PopInfo pi;
    PopInfo piS;
    
    PnList(CDPlayer cdp){
        setLayout(null);
        lsl = new LsList(cdp);
        this.cdp = cdp;
        setBackground(Color.black);
         add(tf);
         add(pnSwitch);pnSwitch.setBackground(Color.gray);
          pnSwitch.add(cbg.cbSong);
          pnSwitch.add(cbg.cbPlayList);
         add(lsl);
         addMouseMotionListener(
             new MouseMotionListener(){
                 public void mouseDragged(MouseEvent e){
                     pHide();
                     pSHide();
                 }
                 public void mouseMoved(MouseEvent e){
                     pHide();
                     pSHide();
                 }
             }
         );
    }
    
    public void haichi(){
        tf.setBounds(40,5,getWidth()-80,20);
        pnSwitch.setBounds(0,tf.getY()+tf.getHeight()+5,getWidth(),30);
        cbg.cbSong.setBounds(40,5,50,20);
        cbg.cbPlayList.setBounds(90,5,100,20);
        lsl.setBounds(40,pnSwitch.getY()+pnSwitch.getHeight()+5,getWidth()-80,getHeight()-70);
    }

    void update(boolean isSong){
        update(tf.getText(),isSong);
    }
    void update(String tf){
        update(tf,cbg.isSong());
    }
    void update(String tf,boolean isSong){
        CDFLibrary.update(tf,isSong);
    }
    
    void lupdate(ChooseData cda[]){
        lsl.lupdate(cda);
    }
    
    void popupInfo(Point p,ChooseData cd){
        System.out.println(cd.getName());
        pHide();
        pi = new PopInfo(p,cd);
        pi.show();
    }
    
    void popupInfoS(Point p,ChooseDataSong cds){
        System.out.println(cds.getName());
        pSHide();
        piS = new PopInfo(p,cds);
        piS.show();
    }
    
    void pHide(){
        if(pi != null){
            pi.hide();
            pi = null;
        }
    }
    void pSHide(){
        if(piS != null){
            piS.hide();
            piS = null;
        }
    }
    
    class PopInfo extends Popup{
        public PopInfo(Point p,ChooseData cd){
            super(pn,new PnInfo(cd),(int)(p.getX()),(int)(p.getY()));
        }
    }
    
    class PnInfo extends Panel{
        PnInfo pni = this;
        PnInfo(ChooseData cd){
            setLayout(new BoxLayout(pni,BoxLayout.Y_AXIS));
            if(cd.isSong()){
                Label name = new Label(((ChooseDataSong)cd).getName());
                Label grade = new Label(String.valueOf(((ChooseDataSong)cd).getGrade()));
                Label comment = new Label(((ChooseDataSong)cd).getComment());
                Label date = new Label(((ChooseDataSong)cd).getDate());
                Label with = new Label(((ChooseDataSong)cd).getWith());
                Label score = new Label(((ChooseDataSong)cd).getScore());
                Button btEdit = new Button("ï“èW");
                btEdit.addActionListener(
                    new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                            System.out.println(pi==null);
                            pHide();
                            pSHide();
                            System.out.println("PnInfo(Song).btEdit.ActionListener:");
                            new CDSEditor(((Gamen)pn.getParent()),(ChooseDataSong)cd);
                        }
                    }
                );
                add(name);
                add(grade);
                add(comment);
                add(date);
                add(with);
                add(score);
                add(btEdit);
            }
            else{
                List lss = new List(((ChooseDataList)cd).number()+2,false);
                lss.addMouseListener(
                    new MouseAdapter(){
                        public void mouseReleased(MouseEvent e){
                            if(e.getButton()==MouseEvent.BUTTON3&&lss.getSelectedIndex()!=-1){
                                popupInfoS(MouseInfo.getPointerInfo().getLocation(),CDFLibrary.getCDSs()[CDFLibrary.getSongNumber(lss.getSelectedItem())]);
                            }
                        }
                    }
                );
                for(int i:((ChooseDataList)cd).getSongs()){
                    lss.add(CDFLibrary.getCDSs()[i].getFname());
                }
                Button btEdit = new Button("ï“èW");
                btEdit.addActionListener(
                    new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                            pHide();
                            pSHide();
                            System.out.println("PnInfo(List).btEdit.ActionListener:");
                            new CDLEditor(((Gamen)pn.getParent()),(ChooseDataList)cd);
                        }
                    }
                );
                Button btDelete = new Button("çÌèú");
                btDelete.addActionListener(
                    new ActionListener(){
                        public void actionPerformed(ActionEvent e){
                            pHide();
                            pSHide();
                            System.out.println("PnInfo(List).btDelete.ActionListener:");
                            CDFLibrary.playListDelete(CDFLibrary.getPlayListNumber(cd.getName()));
                        }
                    }
                );
                add(lss);
                add(btEdit);
                add(btDelete);
            }
        }
    }
}