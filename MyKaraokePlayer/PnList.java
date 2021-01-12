import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PnList extends Panel{
    ChooseData[] cda;
    TextField searchField=new TextField();
    Panel pnSwitch = new Panel(null);
    CheckboxGroup cbg = new CheckboxGroup();
    Checkbox cbSong = new Checkbox("�P��",cbg,true);
    Checkbox cbPlayList = new Checkbox("�v���C���X�g",cbg,false);
    
    List list;
    PopListInfo popList;
    PopSongInfo popSong;
    
    PnList(){
        setLayout(null);
        setBackground(Color.black);
        cda=CDFLibrary.getMatchCDSs("");
        list=createListFrom(cda);
        add(list);
        searchField.addTextListener((e)->update(searchField.getText()));
        add(searchField);
        pnSwitch.setBackground(Color.gray);
        pnSwitch.add(cbSong);
        pnSwitch.add(cbPlayList);
        add(pnSwitch);
        ItemListener il=(e)->update(cbSong.getState());
        cbSong.addItemListener(il);
        cbPlayList.addItemListener(il);
        addMouseMotionListener(
            new MouseMotionListener(){
                public void mouseDragged(MouseEvent e){
                    hideListInfo();
                    hideSongInfo();
                }
                public void mouseMoved(MouseEvent e){
                    hideListInfo();
                    hideSongInfo();
                }
            }
        );
    }
    
    public void selfLayout(){
        searchField.setBounds(40,5,getWidth()-80,20);
        pnSwitch.setBounds(0,searchField.getY()+searchField.getHeight()+5,getWidth(),30);
        cbSong.setBounds(40,5,50,20);
        cbPlayList.setBounds(90,5,100,20);
        list.setBounds(40,pnSwitch.getY()+pnSwitch.getHeight()+5,getWidth()-80,getHeight()-70);
    }

    void update(boolean isSong){
        update(searchField.getText(),isSong);
    }
    void update(String searchField){
        update(searchField,cbSong.getState());
    }
    void update(String searchField,boolean isSong){
        if(isSong){
            cda=CDFLibrary.getMatchCDSs(searchField);
        }else{
            cda=CDFLibrary.getMatchCDLs(searchField);
        }
        List l=createListFrom(cda);
        remove(list);
        list=l;
        add(list);
        selfLayout();
    }
    
    private List createListFrom(ChooseData[] cda){
        List res=new List();
        for(ChooseData cd:cda){
            res.add(cd.getName());
        }
        res.addActionListener(
            (e)->{
                CDPlayer.setCD(cda[list.getSelectedIndex()]);
                transferFocus();
            }
        );
        res.addMouseListener(
            new MouseAdapter(){
                public void mouseReleased(MouseEvent e){
                    if(e.getButton()==MouseEvent.BUTTON3&&list.getSelectedIndex()!=-1){
                        ChooseData cd=cda[list.getSelectedIndex()];
                        if(cd.isSong()){
                            showSongInfo(MouseInfo.getPointerInfo().getLocation(),(ChooseDataSong)cd);
                        }else{
                            showListInfo(MouseInfo.getPointerInfo().getLocation(),(ChooseDataList)cd);
                        }
                    }
                }
            }
        );
        return res;
    }
    
    private void showSongInfo(Point p,ChooseDataSong cds){
        hideSongInfo();
        popSong = new PopSongInfo(p,cds);
        popSong.show();
    }
    private void showListInfo(Point p,ChooseDataList cdl){
        hideListInfo();
        popList = new PopListInfo(p,cdl);
        popList.show();
    }
    private void hideSongInfo(){
        if(popSong != null){
            popSong.hide();
            popSong = null;
        }
    }
    private void hideListInfo(){
        if(popList != null){
            popList.hide();
            popList = null;
        }
    }
    
    class PopSongInfo extends Popup{
        public PopSongInfo(Point p,ChooseDataSong cds){
            super(PnList.this,new PnSongInfo(cds),(int)(p.getX()),(int)(p.getY()));
        }
    }
    class PopListInfo extends Popup{
        public PopListInfo(Point p,ChooseDataList cdl){
            super(PnList.this,new PnListInfo(cdl),(int)(p.getX()),(int)(p.getY()));
        }
    }
    
    class PnSongInfo extends Panel{
        PnSongInfo(ChooseDataSong cds){
            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            Label name = new Label(cds.getName());
            Label grade = new Label(String.valueOf(cds.getGrade()));
            Label comment = new Label(cds.getComment());
            Label date = new Label(cds.getDate());
            Label with = new Label(cds.getWith());
            Label score = new Label(cds.getScore());
            Button btEdit = new Button("�ҏW");
            btEdit.addActionListener(
                (e)->{
                    hideListInfo();
                    hideSongInfo();
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
    }
    class PnListInfo extends Panel{
        PnListInfo(ChooseDataList cdl){
            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
            ChooseDataSong[] cdsa=cdl.getSongs();
            List songList = new List(cdl.size(),false);
            songList.addMouseListener(
                new MouseAdapter(){
                    public void mouseReleased(MouseEvent e){
                        if(e.getButton()==MouseEvent.BUTTON3&&songList.getSelectedIndex()!=-1){
                            ChooseDataSong cds=cdsa[songList.getSelectedIndex()];
                            showSongInfo(MouseInfo.getPointerInfo().getLocation(),cds);
                        }
                    }
                }
            );
            for(ChooseDataSong cds:cdsa){
                songList.add(cds.getFname());
            }
            Button editButton = new Button("�ҏW");
            editButton.addActionListener(
                (e)->{
                    hideListInfo();
                    hideSongInfo();
                    new CDLEditor(cdl);
                }
            );
            Button deleteButton = new Button("�폜");
            deleteButton.addActionListener(
                (e)->{
                    hideListInfo();
                    hideSongInfo();
                    CDFLibrary.playListDelete(cdl.getId());
                }
            );
            add(songList);
            add(editButton);
            add(deleteButton);
        }
    }
}