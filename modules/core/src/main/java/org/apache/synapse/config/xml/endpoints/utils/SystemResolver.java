package org.apache.synapse.config.xml.endpoints.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemResolver implements Resolver {

    private final int ENV_VARIABLE_INDEX = 3;
    private static final Pattern envPattern = Pattern.compile("(\\${1})([a-zA-Z0-9]+)_([[a-zA-Z0-9]+_[a-zA-Z0-9]+]+)");

    private boolean isEnvironmentVariable(String variable) {
        Matcher matcher = envPattern.matcher(variable);
        if (matcher.find()) {
            if (System.getenv(matcher.group(ENV_VARIABLE_INDEX)) == null) {
                //error
                System.exit(0);
                return false;
            }
            return true;
        }
        return false;
    }

    private String getEnvironmentVariableValue(String variable) {
        Matcher matcher = envPattern.matcher(variable);
        if (matcher.find()) {
            if (System.getenv(matcher.group(ENV_VARIABLE_INDEX)) == null) {
                //error
                System.exit(0);
                return "";
            }
            return System.getenv(matcher.group(ENV_VARIABLE_INDEX));
        }
        return "";
    }

    @Override
    public String resolve(String input) {

        String envValue = "";
        if (isEnvironmentVariable(input)) {
            envValue = getEnvironmentVariableValue(input);
        }
        return envValue;
    }
}
