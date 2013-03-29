package org.calrissian.mango.cli.impl;

import org.calrissian.mango.cli.ExecOption;
import org.calrissian.mango.cli.ExecOptionGroup;

import java.util.ArrayList;
import java.util.List;

public class AccumuloExecOptionGroup implements ExecOptionGroup {

    public static final String CLIKEY_ZOOKEEPERS = "zk";
    public static final String CLIKEY_INSTANCE = "inst";
    public static final String CLIKEY_USERNAME = "u";
    public static final String CLIKEY_PASSWORD = "p";
    public static final String CLIKEY_AUTHS = "a";

    @Override
    public List<ExecOption> getOptions() {
        List<ExecOption> options = new ArrayList<ExecOption>();

        options.add(new ExecOption(CLIKEY_ZOOKEEPERS, "Accumulo Zookeepers", false, null));
        options.add(new ExecOption(CLIKEY_INSTANCE, "Accumulo instance", false, null));
        options.add(new ExecOption(CLIKEY_USERNAME, "Accumulo username", false, null));
        options.add(new ExecOption(CLIKEY_PASSWORD, "Accumulo password", false, null));
        options.add(new ExecOption(CLIKEY_AUTHS, "Accumulo auths (ex:U,FOUO)", false, null));

        return options;
    }
}
