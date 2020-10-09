import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CDLNewer extends Dialog{
    PopInfoInCDLN pi;
    CDLNewer cdln = this;
    CDLNewer(Gamen gamen){
        super(gamen,"プレイリスト新規作成",true);
        Label ln = new Label("名前:");
        TextField tfn = new TextField();
        List lsL = new List();
        Button btR = new Button("削除>>");
        Button btA = new Button("<<追加");
        List lsS = new List();
        Button btS = new Button("保存");
        Button btC = new Button("キャンセル");
        
        addWindowListener(
            new WindowAdapter(){
                public void windowClosing(WindowEvent e){
                    setVisible(false);
                }
            }
        );
        
        addMouseMotionListener(
            new MouseMotionListener(){
                public void mouseDragged(MouseEvent e){
                    if(pi!=null){
                        pi.hide();
                        pi=null;
                    }
                }
                public void mouseMoved(MouseEvent e){
                    if(pi!=null){
                        pi.hide();
                        pi=null;
                    }
                }
            }
        );
        
        add(ln);
         ln.setBounds(20,40,30,20);
        
        add(tfn);
         tfn.addTextListener(
             new TextListener(){
                 public void textValueChanged(TextEvent e){
                     if( tfn.getText().isEmpty() || lsL.getItemCount()==0 || CDFLibrary.getPlayListNumber(tfn.getText())!=-1 ){
                         btS.setEnabled(false);
                     }
                     else{
                         btS.setEnabled(true);
                     }
                 }
             }
         );
         tfn.setBounds(50,40,95,20);
        
        add(lsL);
         lsL.addItemListener(
             new ItemListener(){
                 public void itemStateChanged(ItemEvent e){
                     if(e.getStateChange()==ItemEvent.SELECTED){
                         btR.setEnabled(true);
                     }
                     else if(e.getStateChange()==ItemEvent.DESELECTED){
                         btR.setEnabled(false);
                     }
                 }
             }
         );
         lsL.addMouseListener(
             new MouseAdapter(){
                 public void mouseReleased(MouseEvent e){
                     if(e.getButton()==MouseEvent.BUTTON3&&lsL.getSelectedIndex()!=-1){
                         popupInfo(
                             MouseInfo.getPointerInfo().getLocation(),
                             CDFLibrary.getCDS(
                                 lsL.getSelectedItem()
                             )
                         );
                     }
                 }
             }
         );
         lsL.setBounds(20,70,125,160);
        
        add(btR);
         btR.setEnabled(false);
         btR.addActionListener(
             new ActionListener(){
                 public void actionPerformed(ActionEvent e){
                     int t = lsL.getSelectedIndex();
                     lsL.remove(t);
                     if(lsL.getItemCount()==0){
                         btR.setEnabled(false);
                         btS.setEnabled(false);
                     }
                     else{
                         lsL.select( t<lsL.getItemCount() ? t : t-1 );
                     }
                 }
             }
         );
         btR.setBounds(155,70,70,20);
        
        add(btA);
         btA.setEnabled(false);
         btA.addActionListener(
             new ActionListener(){
                 public void actionPerformed(ActionEvent e){
                     lsL.add(lsS.getSelectedItem());
                     if( !tfn.getText().isEmpty() && CDFLibrary.getPlayListNumber(tfn.getText())==-1){
                         btS.setEnabled(true);
                     }
                 }
             }
         );
         btA.setBounds(155,110,70,20);
        
        add(lsS);
         for(String fname:CDFLibrary.getFNames()){
             lsS.add(fname);
         }
         lsS.addItemListener(
             new ItemListener(){
                 public void itemStateChanged(ItemEvent e){
                     if(e.getStateChange()==ItemEvent.SELECTED){
                         btA.setEnabled(true);
                     }
                     else if(e.getStateChange()==ItemEvent.DESELECTED){
                         btA.setEnabled(false);
                     }
                 }
             }
         );
         lsS.addMouseListener(
             new MouseAdapter(){
                 public void mouseReleased(MouseEvent e){
                     if(e.getButton()==MouseEvent.BUTTON3&&lsS.getSelectedIndex()!=-1){
                         popupInfo(
                             MouseInfo.getPointerInfo().getLocation(),
                             CDFLibrary.getCDS(lsS.getSelectedItem())
                         );
                     }
                 }
             }
         );
         lsS.setBounds(240,70,125,160);
        
        add(btS);
         btS.setEnabled(false);
         btS.addActionListener(
             new ActionListener(){
                 public void actionPerformed(ActionEvent e){
                     int a[] = new int[lsL.getItemCount()];
                     for(int i=0;i<lsL.getItemCount();i++){
                         a[i] = CDFLibrary.getSongNumber(lsL.getItem(i));
                     }
                     CDFLibrary.addNewPlayListData(tfn.getText(),a);
                     setVisible(false);
                 }
             }
         );
         btS.setBounds(210,250,70,20);
        
        add(btC);
         btC.addActionListener(
             new ActionListener(){
                 public void actionPerformed(ActionEvent e){
                     setVisible(false);
                 }
             }
         );
         btC.setBounds(290,250,70,20);
        
        setLayout(null);
        setSize(380,290);
        setResizable(false);
        setVisible(true);
    }
    
    
    void popupInfo(Point p,ChooseDataSong cds){
        System.out.println(cds.getName());
        if(pi!=null){
            pi.hide();
            pi=null;
        }
        pi = new PopInfoInCDLN(p,cds);
        pi.show();
    }
    
    class PopInfoInCDLN extends Popup{
        public PopInfoInCDLN(Point p,ChooseDataSong cd){
            super(cdln,new PnInfoInCDLN(cd),(int)(p.getX()),(int)(p.getY()));
        }
    }
    
    class PnInfoInCDLN extends Panel{
        PnInfoInCDLN pni = this;
        PnInfoInCDLN(ChooseDataSong cds){
            setLayout(new BoxLayout(pni,BoxLayout.Y_AXIS));
            Label name = new Label(cds.getName());
            Label grade = new Label(String.valueOf(cds.getGrade()));
            Label comment = new Label(cds.getComment());
            Label date = new Label(cds.getDate());
            Label with = new Label(cds.getWith());
            Label score = new Label(cds.getScore());
            add(name);
            add(grade);
            add(comment);
            add(date);
            add(with);
            add(score);
            
        }
    }
}