import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CDLEditor extends Dialog{
    PopSongInfo popSong;
    CDLEditor(ListData cdl){
        super((Frame)null,"プレイリスト編集",true);
        String defaultName = cdl.getName();
        Label ln = new Label("名前:");
        TextField tfn = new TextField(defaultName);
        java.util.List<SongData> listSongs=new java.util.ArrayList<SongData>();
        List listSongList = new List();
        Button removeButton = new Button("削除>>");
        Button addButton = new Button("<<追加");
        SongData[] allSongs=CDFLibrary.getMatchCDSs("");
        List allSongList = new List();
        Button saveButton = new Button("保存");
        Button canselButton = new Button("キャンセル");
        
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
                    if(popSong!=null){
                        popSong.hide();
                        popSong=null;
                    }
                }
                public void mouseMoved(MouseEvent e){
                    if(popSong!=null){
                        popSong.hide();
                        popSong=null;
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
                    if( !defaultName.equals(tfn.getText()) && (tfn.getText().isEmpty() || listSongList.getItemCount()==0 || CDFLibrary.existsListName(tfn.getText()) ) ){
                        saveButton.setEnabled(false);
                    }else{
                        saveButton.setEnabled(true);
                    }
                }
            }
        );
        tfn.setBounds(50,40,95,20);
        
        add(listSongList);
        for(SongData cds:cdl.getSongs()){
            listSongList.add(cds.getName());
            listSongs.add(cds);
        }
        listSongList.addItemListener(
            new ItemListener(){
                public void itemStateChanged(ItemEvent e){
                    if(e.getStateChange()==ItemEvent.SELECTED){
                        removeButton.setEnabled(true);
                    }
                    else if(e.getStateChange()==ItemEvent.DESELECTED){
                        removeButton.setEnabled(false);
                    }
                }
            }
        );
        listSongList.addMouseListener(
            new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    if(e.getButton()==MouseEvent.BUTTON3&&listSongList.getSelectedIndex()!=-1){
                        popupInfo(
                            MouseInfo.getPointerInfo().getLocation(),
                            listSongs.get(listSongList.getSelectedIndex())
                        );
                    }
                }
            }
        );
        listSongList.setBounds(20,70,125,160);
        
        add(removeButton);
        removeButton.setEnabled(false);
        removeButton.addActionListener(
            (e)->{
                int index = listSongList.getSelectedIndex();
                listSongList.remove(index);
                listSongs.remove(index);
                if(listSongList.getItemCount()==0){
                    removeButton.setEnabled(false);
                    saveButton.setEnabled(false);
                }else{
                    listSongList.select( index<listSongList.getItemCount() ? index : index-1 );
                }
            }
        );
        removeButton.setBounds(155,70,70,20);
        
        add(addButton);
        addButton.setEnabled(false);
        addButton.addActionListener(
            (e)->{
                listSongList.add(allSongList.getSelectedItem());
                listSongs.add(allSongs[allSongList.getSelectedIndex()]);
                if( defaultName.equals(tfn.getText()) || (!tfn.getText().isEmpty()&&!CDFLibrary.existsListName(tfn.getText())) ){
                    saveButton.setEnabled(true);
                }
            }
        );
        addButton.setBounds(155,110,70,20);
        
        add(allSongList);
        for(SongData cds:allSongs){
            allSongList.add(cds.getName());
        }
        allSongList.addItemListener(
            new ItemListener(){
                public void itemStateChanged(ItemEvent e){
                    if(e.getStateChange()==ItemEvent.SELECTED){
                        addButton.setEnabled(true);
                    }
                    else if(e.getStateChange()==ItemEvent.DESELECTED){
                        addButton.setEnabled(false);
                    }
                }
            }
        );
        allSongList.addMouseListener(
            new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    if(e.getButton()==MouseEvent.BUTTON3&&allSongList.getSelectedIndex()!=-1){
                        popupInfo(
                            MouseInfo.getPointerInfo().getLocation(),
                            allSongs[allSongList.getSelectedIndex()]
                        );
                    }
                }
            }
        );
        allSongList.setBounds(240,70,125,160);
        
        add(saveButton);
        saveButton.addActionListener(
            (e)->{
                int a[] = new int[listSongList.getItemCount()];
                for(int i=0;i<listSongList.getItemCount();i++){
                    a[i] = listSongs.get(i).getId();
                }
                CDFLibrary.editList(cdl.getId(),tfn.getText(),a);
                setVisible(false);
            }
        );
        saveButton.setBounds(210,250,70,20);
        
        add(canselButton);
        canselButton.addActionListener((e)->setVisible(false));
        canselButton.setBounds(290,250,70,20);
        
        setLayout(null);
        setSize(380,290);
        setResizable(false);
        setVisible(true);
    }
    
    
    void popupInfo(Point p,SongData cds){
        if(popSong!=null){
            popSong.hide();
            popSong=null;
        }
        popSong = new PopSongInfo(p,cds);
        popSong.show();
    }
    
    class PopSongInfo extends Popup{
        public PopSongInfo(Point p,SongData cds){
            super(CDLEditor.this,new PnSongInfo(cds),(int)(p.getX()),(int)(p.getY()));
        }
    }
    
    class PnSongInfo extends Panel{
        PnSongInfo(SongData cds){
            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
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