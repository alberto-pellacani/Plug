package com.plug.auth;

import java.util.regex.Pattern;

public class AuthorizationPattern extends Authorization {

    private String[] method;

    private String path;

    private Pattern pattern;

    public String[] getMethod() {
        return method;
    }

    public void setMethod(String[] method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Pattern getPattern() {
        if(this.pattern==null) {
            this.pattern = Pattern.compile(this.toPattern(this.path));
        }
        return this.pattern;
    }

    private String toPattern(String path) {

        StringBuffer buff = new StringBuffer();
        int start = 0;
        char[] chars = path.toCharArray();
        for(int i=0; i<chars.length; i++) {
            char ch = chars[i];
            if(ch=='*') {
                if(start<i) {
                    buff.append("\\Q").append(chars,start,i-start).append("\\E");

                }
                buff.append(".*");
                start=i+1;
            }
        }
        if(start<chars.length) {
            buff.append("\\Q").append(chars,start,chars.length-start).append("\\E");
        }
        return buff.toString();
    }
}
