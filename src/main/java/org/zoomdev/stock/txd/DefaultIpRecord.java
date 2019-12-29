package org.zoomdev.stock.txd;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DefaultIpRecord implements IpRecord {


    private File getFile(){
        String folder=System.getProperty("java.io.tmpdir");
        File recordFild = new File(folder,"tdx-ip-record.txt");
        return recordFild;
    }



    @Override
    public IpInfo[] load(IpInfo[] infos) {
        File file = getFile();
        BufferedReader reader = null;
        try{
            Map<String,Integer> map = new HashMap<String, Integer>();
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line = null;
            while((line = reader.readLine())!=null){
                String[] parts = line.split(" ");
                if(parts.length==2){
                    map.put(parts[0],Integer.parseInt(parts[1]));
                }
            }

            for(IpInfo info : infos){
                Integer count = map.get(String.format("%s:%d",info.host,info.port));
                if(count!=null){
                    info.successCount = count;
                }
            }


        } catch (IOException e) {
        } finally {
            Utils.close(reader);
        }
        return infos;
    }

    @Override
    public void save(IpInfo[] infos) {
        File file = getFile();
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

            for(IpInfo info : infos){

                writer.write(String.format("%s:%d %d",info.host,info.port,info.successCount));
                writer.newLine();
            }
            writer.flush();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            Utils.close(writer);
        }
    }
}
