package edu.abhi.tools.activemq.utils;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

public class DownloadUtils {

    public static void downloadToFile(BytesMessage msg, ApplicationOptions options, int count) throws IOException, JMSException {
        long length = msg.getBodyLength();

        String currFileName = options.getDownloadFileName() + count;
        try (OutputStream fout = new FileOutputStream(currFileName)) {
            for(long i=1;i<=length;i++){
                fout.write(msg.readByte());
            }
        }
        System.out.println("Binary Message stored in file : " + currFileName);
    }

    public static void downloadToFile(TextMessage msg, ApplicationOptions options, int count)
            throws IOException, JMSException {
        String currFileName = options.getDownloadFileName() + (options.isMultipleMessage()? 0 : count) + ".txt";
        try (FileWriter fw = new FileWriter(currFileName, true)) {
            fw.write(msg.getText() + (options.isMultipleMessage()? "\n\n" : ""));
            if(!options.isMultipleMessage() || count == 1)
                System.out.println("Text Message generated in file = " + currFileName);
            if(options.isMultipleMessage())
                System.out.println("Writing message(s) : " + count);
        }
    }

}
