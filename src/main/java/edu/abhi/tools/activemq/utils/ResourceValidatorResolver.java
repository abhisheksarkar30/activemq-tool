package edu.abhi.tools.activemq.utils;

import edu.abhi.tools.activemq.constants.ActionType;
import edu.abhi.tools.activemq.constants.Constants;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResourceValidatorResolver {

    private ApplicationOptions options;

    public ApplicationOptions resolve(String[] args) {
        //init
        if(args.length > 1) {
            ResourceLoader.init(args[1]);
        } else {
            System.out.println("Context file name or Config file path missing!");
            System.exit(-1);
        }

        options = new ApplicationOptions();

        options.setQueue1Name(resolveQueueName(Constants.QUEUE1_NAME));

        resolveActionType();
        resolveMessageType();

        switch(options.getActionTypeEnum()) {
            case UPLOAD:
                resolveFolderLocation();
                resolveMessageFormatDetails();
                resolveMsgCopies();
                resolveMultipleMsgPerFile();
                break;
            case TRANSFER:
                options.setQueue2Name(resolveQueueName(Constants.QUEUE2_NAME));
                resolveMsgCopies();
                break;
            case DOWNLOAD:
                resolveFolderLocation();
                resolveFormattedDate();
                resolveMultipleMsgPerFile();
                break;
            case CONSUME:
                resolveMessageStore();
                resolveFormattedDate();
                resolveMultipleMsgPerFile();
                break;
        }

        return options;
    }

    private void resolveMessageStore() {
        String storeMsg = ResourceLoader.getResourceProperty(Constants.MESSAGE_STORE);

        if(StringUtils.isEmpty(storeMsg)) {
            System.out.println("Please provide property value : " + Constants.MESSAGE_STORE);
            System.exit(-1);
        } else {
            options.setMessageStore(Constants.CONST_YES.equalsIgnoreCase(storeMsg));

            if(options.isMessageStore())
                resolveFolderLocation();
        }
    }

    private void resolveMsgCopies() {
        String msgCopies = ResourceLoader.getResourceProperty(Constants.MESSAGE_COPIES);

        if(StringUtils.isEmpty(msgCopies)) {
            System.out.println("Please provide property value : " + Constants.MESSAGE_COPIES);
            System.exit(-1);
        } else
            options.setMsgCopies(Integer.parseInt(msgCopies));
    }

    private void resolveMultipleMsgPerFile() {
        String multipleMsg = ResourceLoader.getResourceProperty(Constants.MESSAGE_MULTIPLE);

        if(StringUtils.isEmpty(multipleMsg)) {
            System.out.println("Please provide property value : " + Constants.MESSAGE_MULTIPLE);
            System.exit(-1);
        } else
            options.setMultipleMessage(Constants.CONST_YES.equalsIgnoreCase(multipleMsg));
    }

    private void resolveFormattedDate() {
        String datePattern = ResourceLoader.getResourceProperty(Constants.FILE_DATE_PATTERN);

        options.setFormattedDate(datePattern != null && !datePattern.isEmpty()? new SimpleDateFormat(datePattern)
                .format(new Date()) : "" + System.currentTimeMillis());
    }

    private void resolveMessageType() {
        options.setMessageType(ResourceLoader.getResourceProperty(Constants.MESSAGE_TYPE));

        if (!Constants.CONST_BINARY.equalsIgnoreCase(options.getMessageType()) &&
                !Constants.CONST_TEXT.equalsIgnoreCase(options.getMessageType())) {
            System.out.println("Please provide binary/text message type");
            System.exit(-1);
        }
    }

    private String resolveQueueName(String queueNameProperty) {
        String queueName = ResourceLoader.getResourceProperty(queueNameProperty);

        if (StringUtils.isEmpty(queueName)) {
            System.out.println("Please provide property value : " + queueNameProperty);
            System.exit(-1);
        }
        return queueName;
    }

    private void resolveActionType() {
        options.setActionType(ResourceLoader.getResourceProperty(Constants.ACTION_TYPE));

        if (StringUtils.isEmpty(options.getActionType())) {
            System.out.println("Please provide action type");
            System.exit(-1);
        } else {
            options.setActionTypeEnum(ActionType.valueOf(options.getActionType().toUpperCase()));
            if(options.getActionTypeEnum() == null) {
                System.out.println("Please provide valid action type");
                System.exit(-1);
            }
        }
    }

    private boolean resolveMessageFormat() {
        options.setMessageFormat(ResourceLoader.getResourceProperty(Constants.MESSAGE_FORMAT));

        if (StringUtils.isEmpty(options.getMessageFormat())) {
            System.out.println("Please provide message type : xml/swift/default ");
            System.exit(-1);
        } else if(options.getMessageFormat().equalsIgnoreCase(Constants.CONST_DEFAULT))
            return false;
        return true;
    }

    private void resolveMessageFormatDetails() {
        if(!resolveMessageFormat()) return;

        options.setFirstLineStarts(ResourceLoader.getResourceProperty(Constants.MESSAGE_STARTS + options.getMessageFormat()));
        options.setLastLineStarts(ResourceLoader.getResourceProperty(Constants.MESSAGE_ENDS + options.getMessageFormat()));

        if (StringUtils.isEmpty(options.getFirstLineStarts()) || StringUtils.isEmpty(options.getLastLineStarts())) {
            System.out.println("Please provide message starts/ends with properties for message type : " + options.getMessageFormat());
            System.exit(-1);
        }
    }

    private void resolveFolderLocation() {
        options.setFolderLocation(ResourceLoader.getResourceProperty(Constants.FOLDER_LOCATION));

        if (StringUtils.isEmpty(options.getFolderLocation())) {
            System.out.println("Please provide folder location from/to where message will be (up/down)loaded");
            System.exit(-1);
        } else {
            File dir = new File(options.getFolderLocation());
            if(!dir.exists() && !dir.mkdirs())
                System.out.println("Specified directory doesn't exist and couldn't be created!");
        }
    }
}
