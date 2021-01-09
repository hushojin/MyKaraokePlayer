public class MyKaraokePlayer{
    public static void main(String args[]) {
        CDPlayer cdp = new CDPlayer();
        PnList pnList = new PnList(cdp);
        pnList.update("",true);
        PnPlayer pnPlayer = new PnPlayer(cdp);
        cdp.setPnPlayer(pnPlayer);
        Gamen gam = new Gamen(pnList,pnPlayer);
        cdp.setGamen(gam);
        MB mb = new MB(cdp,gam);
        gam.setMB(mb);
        gam.haichi();
    }
}