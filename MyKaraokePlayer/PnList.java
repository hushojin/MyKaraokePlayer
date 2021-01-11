import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

public class PnList extends Panel{
    ChooseData cda[];
    PnList pn = this;
    TextField tf=new TextField();
    Panel pnSwitch = new Panel(null);
    CheckboxGroup cbg = new CheckboxGroup();
    Checkbox cbSong = new Checkbox("単曲",cbg,true);
    Checkbox cbPlayList = new Checkbox("プレイリスト",cbg,false);
    
    List ls;
    PopInfo pi;
    PopInfo piS;
    
    PnList(){
        setLayout(null);
        setBackground(Color.black);
        
        cda=CDFLibrary.getMatchCDSs("");
        ls=createListFrom(cda);
        ls.addActionListener(
            (e)->{
                CDPlayer.setCD(cda[ls.getSelectedIndex()]);
                transferFocus();
            }
        );
        ls.addMouseListener(
            new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    if(e.getButton()==MouseEvent.BUTTON3&&ls.getSelectedIndex()!=-1){
                        popupInfo(MouseInfo.getPointerInfo().getLocation(),cda[ls.getSelectedIndex()]);
                    }
                }
            }
        );
        add(ls);
        add(tf);
        tf.addTextListener((e)->update(tf.getText()));
        add(pnSwitch);
        pnSwitch.setBackground(Color.gray);
        pnSwitch.add(cbSong);
        pnSwitch.add(cbPlayList);
        ItemListener il=(e)->update(cbSong.getState());
        cbSong.addItemListener(il);
        cbPlayList.addItemListener(il);
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
    
    public void selfLayout(){
        tf.setBounds(40,5,getWidth()-80,20);
        pnSwitch.setBounds(0,tf.getY()+tf.getHeight()+5,getWidth(),30);
        cbSong.setBounds(40,5,50,20);
        cbPlayList.setBounds(90,5,100,20);
        ls.setBounds(40,pnSwitch.getY()+pnSwitch.getHeight()+5,getWidth()-80,getHeight()-70);
    }

    void update(boolean isSong){
        update(tf.getText(),isSong);
    }
    void update(String tf){
        update(tf,cbSong.getState());
    }
    void update(String tf,boolean isSong){
        if(isSong){
            cda=CDFLibrary.getMatchCDSs(tf);
        }else{
            cda=CDFLibrary.getMatchCDLs(tf);
        }
        List l=createListFrom(cda);
        l.addActionListener(
            (e)->{
                CDPlayer.setCD(cda[ls.getSelectedIndex()]);
                transferFocus();
            }
        );
        l.addMouseListener(
            new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    if(e.getButton()==MouseEvent.BUTTON3&&ls.getSelectedIndex()!=-1){
                        popupInfo(MouseInfo.getPointerInfo().getLocation(),cda[ls.getSelectedIndex()]);
                    }
                }
            }
        );
        remove(ls);
        ls=l;
        add(ls);
        selfLayout();
    }
    
    static List createListFrom(ChooseData[] cda){
      List res=new List();
      for(ChooseData cd:cda){
          res.add(cd.getName());
      }
      return res;
    }
    
    void popupInfo(Point p,ChooseData cd){
        pHide();
        pi = new PopInfo(p,cd);
        pi.show();
    }
    
    void popupInfoS(Point p,ChooseDataSong cds){
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
                ChooseDataSong cds=(ChooseDataSong)cd;
                Label name = new Label(cds.getName());
                Label grade = new Label(String.valueOf(cds.getGrade()));
                Label comment = new Label(cds.getComment());
                Label date = new Label(cds.getDate());
                Label with = new Label(cds.getWith());
                Label score = new Label(cds.getScore());
                Button btEdit = new Button("編集");
                btEdit.addActionListener(
                    (e)->{
                        pHide();
                        pSHide();
                        new CDSEditor(cds);
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
                ChooseDataList cdl=(ChooseDataList)cd;
                List lss = new List(cdl.number()+2,false);
                lss.addMouseListener(
                    new MouseAdapter(){
                        public void mouseReleased(MouseEvent e){
                            if(e.getButton()==MouseEvent.BUTTON3&&lss.getSelectedIndex()!=-1){
                                popupInfoS(MouseInfo.getPointerInfo().getLocation(),CDFLibrary.getCDS(lss.getSelectedItem()));
                            }
                        }
                    }
                );
                for(int i:cdl.getSongs()){
                    lss.add(CDFLibrary.getCDS(i).getFname());
                }
                Button btEdit = new Button("編集");
                btEdit.addActionListener(
                    (e)->{
                        pHide();
                        pSHide();
                        new CDLEditor(cdl);
                    }
                );
                Button btDelete = new Button("削除");
                btDelete.addActionListener(
                    (e)->{
                        pHide();
                        pSHide();
                        CDFLibrary.playListDelete(cdl.getName());
                    }
                );
                add(lss);
                add(btEdit);
                add(btDelete);
            }
        }
    }
}