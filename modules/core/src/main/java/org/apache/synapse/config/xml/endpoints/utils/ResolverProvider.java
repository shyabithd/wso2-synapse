package org.apache.synapse.config.xml.endpoints.utils;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResolverProvider {

    private static final Pattern resolverPattern = Pattern.compile("(\\${1})([a-zA-Z0-9]+)_([[a-zA-Z0-9]+_[a-zA-Z0-9]+]+)");
    private final String PACKAGE_NAME = "org.apache.synapse.config.xml.endpoints.utils.";
    private final String RESOLVER = "Resolver";
    private Map resolverMap = new HashMap();

    public ResolverProvider() {
        registerResolvers();
    }

    public  Resolver getResolver(String input) {
        Matcher matcher = resolverPattern.matcher(input);
        if (matcher.find()) {
            try {
                String className = StringUtils.capitalize(matcher.group(2).toLowerCase());
                Class c = Class.forName(PACKAGE_NAME + className + RESOLVER);
                try {
                    return (Resolver) c.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    return null;
                }
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
        return null;
    }

    public String resolve(String input) {

        Matcher matcher = resolverPattern.matcher(input);
        if (matcher.find()) {
            Resolver resolverObject = (Resolver) resolverMap.get(matcher.group(2));
            if (resolverObject != null) {
                return resolverObject.resolve(input);
            }
            return null;
        }
        return input;
    }

    public void registerResolvers() {

        resolverMap.put("SYSTEM", new SystemResolver());
    }
}
