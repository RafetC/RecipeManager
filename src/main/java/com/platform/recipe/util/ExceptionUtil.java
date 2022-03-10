package com.platform.recipe.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {

    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw, true);
            throwable.printStackTrace(pw);
            return sw.getBuffer().toString();
        } finally {
            try {
                if (sw != null)
                    sw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                if (pw != null)
                    pw.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
