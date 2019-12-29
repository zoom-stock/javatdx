package org.zoomdev.stock.txd.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zoomdev.stock.txd.HexUtils;
import org.zoomdev.stock.txd.TxdInputStream;
import org.zoomdev.stock.txd.TxdOutputStream;

import java.io.IOException;

public class LoginCommand extends GroupCommand {
    private static final Log log = LogFactory.getLog(LoginCommand.class);

    public LoginCommand(){
        super(new Cmd1(),new Cmd2(),new Cmd3());
    }

    static class Cmd1 extends BaseCommand{

        @Override
        protected void doOutput(TxdOutputStream out) throws IOException {
            out.write(HexUtils.decodeHex("0c 02 18 93 00 01 03 00 03 00 0d 00 01".replace(" ","")));
        }

        @Override
        protected void doInput(TxdInputStream inputStream) throws IOException {

        }
    }

    static class Cmd2 extends BaseCommand{

        @Override
        protected void doOutput(TxdOutputStream out) throws IOException {
            out.write(HexUtils.decodeHex("0c 02 18 94 00 01 03 00 03 00 0d 00 02"
                    .replace(" ","").toCharArray()));
        }

        @Override
        protected void doInput(TxdInputStream inputStream) throws IOException {
           log.info("通达信登录成功，准备发送指令");
        }
    }

    static class Cmd3 extends BaseCommand{

        @Override
        protected void doOutput(TxdOutputStream out) throws IOException {
            out.write(HexUtils.decodeHex(("0c 03 18 99 00 01 20 00 20 00 db 0f d5"
                    + "d0 c9 cc d6 a4 a8 af 00 00 00 8f c2 25"
                    + "40 13 00 00 d5 00 c9 cc bd f0 d7 ea 00"
                    + "00 00 02").replaceAll(" ","").toCharArray()));
        }

        @Override
        protected void doInput(TxdInputStream inputStream) throws IOException {

        }
    }


}
