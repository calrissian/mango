package org.calrissian.mango.cli.impl;

import org.calrissian.mango.cli.ExecOption;
import org.calrissian.mango.cli.ExecOptionGroup;

import java.util.ArrayList;
import java.util.List;

public class JmsExecOptionGroup implements ExecOptionGroup {

    public static final String CLIKEY_URL = "url";
    public static final String CLIKEY_USERNAME = "u";
    public static final String CLIKEY_PASSWORD = "p";
    public static final String CLIKEY_TOPIC = "topic";



    @Override
    public List<ExecOption> getOptions() {
        List<ExecOption> options = new ArrayList<ExecOption>();

        options.add(new ExecOption(CLIKEY_TOPIC, "JMS Topic", false, null));
        options.add(new ExecOption(CLIKEY_URL, "JMS url", false, null));
        options.add(new ExecOption(CLIKEY_USERNAME, "JMS username", true, null));
        options.add(new ExecOption(CLIKEY_PASSWORD, "JMS password", true, null));

        return options;
    }



}
