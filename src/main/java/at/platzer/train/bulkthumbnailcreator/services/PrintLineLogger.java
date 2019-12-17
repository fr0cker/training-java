package at.platzer.train.bulkthumbnailcreator.services;

import at.platzer.train.bulkthumbnailcreator.api.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintLineLogger implements Logger {

    private SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private void Write(String level, String message, Exception ex){
        if((message == null || message.isBlank()) && ex == null)
            return;

        System.out.println(timeFormat.format(new Date()) + " " + level + message + (ex == null ? "" : "\nException: " + ex));
    }

    @Override
    public void Debug(String message, Exception ex) {
        Write("DEBUG: ", message, ex);
    }

    @Override
    public void Debug(String message) {
        Write("DEBUG: ", message, null);
    }

    @Override
    public void Info(String message, Exception ex) {
        Write("DEBUG: ", message, ex);
    }

    @Override
    public void Info(String message) {
        Write("DEBUG: ", message, null);
    }

    @Override
    public void Warn(String message, Exception ex) {
        Write("WARN: ", message, ex);
    }

    @Override
    public void Warn(String message) {
        Write("WARN: ", message, null);
    }

    @Override
    public void Error(String message, Exception ex) {
        Write("ERROR: ", message, ex);
    }

    @Override
    public void Error(String message) {
        Write("ERROR: ", message, null);
    }
}
