import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CDLEditor extends Dialog{
    PopInfoInCDLE pi;
    CDLEditor cdle = this;
    CDLEditor(Gamen gamen,ChooseDataList cdl,CDFLibrary cdfl){
        super(gamen,"�v���C���X�g�ҏW",true);
        String defaultName = cdl.getName();
        Label ln = new Label("���O:");
        TextField tfn = new TextField(defaultName);
        List lsL = new List();
        Button btR = new Button("�폜>>");
        Button btA = new Button("<<�ǉ�");
        List lsS = new List();
        Button btS = new Button("�ۑ�");
        Button btC = new Button("�L�����Z��");
        
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
                     if( !defaultName.equals(tfn.getText()) && (tfn.getText().isEmpty() || lsL.getItemCount()==0 || cdfl.getPlayListNumber(tfn.getText()) != -1) ){
                         //���̖��O�ƈႢ���i���O������܂���lsL����܂��͊����̖��O�Ɣ��j�Ȃ�΁F�s��
                         System.out.println("CDLE.tfn.TextListener:def,text,"+defaultName+":"+tfn.getText());
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
         for(int cds:cdl.getSongs()){
             lsL.add(cdfl.getCDSs()[cds].getFname());
         }
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
                         popupInfo(MouseInfo.getPointerInfo().getLocation(),cdfl.getCDSs()[cdfl.getSongNumber(lsL.getSelectedItem())]);
/*
cdfl.getCDSs()[]
              [cdfl.getSongNumber()]
                                 (lsL.getSelectedItem())

;

cdfl.getCDSs()[cdfl.getSongNumber(lsL.getSelectedItem())];
*/
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
                     if( defaultName.equals(tfn.getText()) || (!tfn.getText().isEmpty()&&cdfl.getPlayListNumber(tfn.getText())==-1) ){
                     //���O�����Ɠ������i���O���ɓ��e�����芎�����̖��O�Ɣ��Ȃ��j
                         btS.setEnabled(true);
                     }
                 }
             }
         );
         btA.setBounds(155,110,70,20);
        
        add(lsS);
         for(ChooseDataSong cds:cdfl.getCDSs()){
             lsS.add(cds.getFname());
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
                         popupInfo(MouseInfo.getPointerInfo().getLocation(),cdfl.getCDSs()[lsS.getSelectedIndex()]);
/*
cdfl.getCDSs()[]
              [lsS.get];
*/
                     }
                 }
             }
         );
         lsS.setBounds(240,70,125,160);
        
        add(btS);
         btS.addActionListener(
             new ActionListener(){
                 public void actionPerformed(ActionEvent e){
                     System.out.println("CDLE.btS.actionListener");
                     int a[] = new int[lsL.getItemCount()];
                     for(int i=0;i<lsL.getItemCount();i++){
                         a[i] = cdfl.getSongNumber(lsL.getItem(i));
                     }
                     cdfl.playListEdit(cdfl.getPlayListNumber(defaultName),new ChooseDataList(tfn.getText(),a));
                     cdfl.reload(false);
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
        pi = new PopInfoInCDLE(p,cds);
        pi.show();
    }
    
    class PopInfoInCDLE extends Popup{
        public PopInfoInCDLE(Point p,ChooseDataSong cd){
            super(cdle,new PnInfoInCDLE(cd),(int)(p.getX()),(int)(p.getY()));
        }
    }
    
    class PnInfoInCDLE extends Panel{
        PnInfoInCDLE pni = this;
        PnInfoInCDLE(ChooseDataSong cds){
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