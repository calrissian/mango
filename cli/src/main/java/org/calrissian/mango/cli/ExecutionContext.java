package org.calrissian.mango.cli;

import java.util.*;
import java.util.Map.Entry;

public class ExecutionContext {

    Map<String, ExecOption> options;

    public ExecutionContext() {
        options = new HashMap<String,ExecOption>();
    }

    public void addOptionGroup(ExecOptionGroup optionGroup) {
        List<ExecOption> execOptions = optionGroup.getOptions();

        for(ExecOption option : execOptions) {

            addOption(option);
        }
    }

    public void addOption(ExecOption option)
    {
        options.put(option.getShortFlag(), option);
    }

    /* parse all given command line arguments and
      * return true/false based on the validity of those
      * arguments
      */
    public boolean parseArgs(String[] args)
    {
        for(int i = 0; i < args.length; i++)
        {
            // iterate through the options and find out whether
            // or not we have an option binding
            Set<Entry<String,ExecOption>> optionKeys = options.entrySet();
            Iterator<Entry<String,ExecOption>> setItr = optionKeys.iterator();

            while(setItr.hasNext())
            {
                Entry<String,ExecOption> entry = setItr.next();

                if(args[i].equals("-" + entry.getKey()))
                {
                    if(!entry.getValue().isBoolean())
                    {
                        entry.getValue().setValue(args[i+1]);
                    }

                    else {
                        entry.getValue().setValue("true");
                    }
                }
            }
        }

        Collection<ExecOption> optionVals = options.values();
        Iterator<ExecOption> itrVals = optionVals.iterator();

        while(itrVals.hasNext())
        {
            ExecOption curOption = itrVals.next();
            if(!curOption.isOptional() && curOption.getValue() == null && curOption.getDefaultValue() == null)
            {
                return false;
            }
        }

        return true;
    }

    public String get(String option)
    {
        ExecOption execOption = options.get(option);
        return execOption.getValue() != null ? execOption.getValue() : execOption.getDefaultValue();
    }

    public String getUseAsString(Class<? extends Object> useClass) {

        return getUseAsString(null, useClass);
    }

    public String getUseAsString(String errorDescription, Class<? extends Object> useClass) {

        StringBuffer sb = new StringBuffer();


        Collection<ExecOption> optionVals = options.values();
        Iterator<ExecOption> itrVals = optionVals.iterator();


        if(errorDescription == null)
        {
            while(itrVals.hasNext())
            {
                ExecOption curOption = itrVals.next();

                if(!curOption.isOptional() && curOption.getValue() == null && curOption.getDefaultValue() == null)
                {
                    sb.append("\nMissing: " + curOption.getDescription() + "\n");
                    break;
                }
            }
        }

        sb.append("\nUsage: java " + useClass.getCanonicalName() + "\n");

        optionVals = options.values();
        itrVals = optionVals.iterator();

        while(itrVals.hasNext())
        {
            String optionPadding = "";

            ExecOption curOption = itrVals.next();

            for(int i = 0; i < 15 - curOption.getShortFlag().length(); i++)
            {
                optionPadding += " ";
            }

            sb.append("\t-" + curOption.getShortFlag() + optionPadding + curOption.getDescription());

            if(curOption.getDefaultValue() != null)
            {
                sb.append(" [default: " + curOption.getDefaultValue() + "]");
            }

            if(curOption.isOptional())
            {
                sb.append(" - optional");
            }

            else
            {
                sb.append(" - required");
            }

            sb.append("\n");
        }

        sb.append("\n");

        return sb.toString();
    }

}
