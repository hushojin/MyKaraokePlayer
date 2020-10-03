  public class MyKaraokePlayer{
    public static void main(String args[]) {
        CDPlayer cdp = new CDPlayer();
        PnList pnlist = new PnList(cdp);
        CDFLibrary.setPnList(pnlist);
        PnPlayer pnplayer = new PnPlayer(cdp);
        cdp.setPnPlayer(pnplayer);
        Gamen gam = new Gamen(pnlist,pnplayer);
        cdp.setGamen(gam);
        MB mb = new MB(cdp,gam);
        gam.setMB(mb);
        gam.haichi();
    }
}