package com.iCritic.users.core.utils;

import java.util.Map;

public class NotificationUtils {

    public static String replaceVariables(String template, Map<String, String> variables) {
        if (variables == null || variables.isEmpty()) {
            return template;
        }

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            template = template.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return template;
    }
}
