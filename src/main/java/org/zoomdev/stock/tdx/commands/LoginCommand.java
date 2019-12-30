package org.zoomdev.stock.tdx.commands;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.zoomdev.stock.tdx.HexUtils;
import org.zoomdev.stock.tdx.TdxInputStream;
import org.zoomdev.stock.tdx.TxdOutputStream;

import java.io.IOException;

public class LoginCommand extends GroupCommand {
    private static final Log log = LogFactory.getLog(LoginCommand.class);

    public LoginCommand(){
        super(new Cmd1(),new Cmd2(),new Cmd3());
    }

    static class Cmd1 extends BaseCommand{

        @Override
        protected void doOutput(TxdOutputStream out) throws IOException {
            out.write(HexUtils.decodeHex("0c0218930001030003000d0001"));
        }

        @Override
        protected void doInput(TdxInputStream inputStream) throws IOException {

        }
    }

    static class Cmd2 extends BaseCommand{

        @Override
        protected void doOutput(TxdOutputStream out) throws IOException {
            out.write(HexUtils.decodeHex("0c0218940001030003000d0002"));
        }

        @Override
        protected void doInput(TdxInputStream inputStream) throws IOException {
           log.info("通达信登录成功，准备发送指令");
        }
    }

    static class Cmd3 extends BaseCommand{

        @Override
        protected void doOutput(TxdOutputStream out) throws IOException {
            out.write(HexUtils.decodeHex(("0c031899000120002000db0fd5d0c9ccd6a4a8af0000008fc22540130000d500c9ccbdf0d7ea00000002")));
        }

        @Override
        protected void doInput(TdxInputStream inputStream) throws IOException {

        }
    }


}
