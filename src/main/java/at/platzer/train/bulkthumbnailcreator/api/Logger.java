package at.platzer.train.bulkthumbnailcreator.api;

public interface Logger {
    void Debug(String message, Exception ex);
    void Debug(String message);
    void Info(String message, Exception ex);
    void Info(String message);
    void Warn(String message, Exception ex);
    void Warn(String message);
    void Error(String message, Exception ex);
    void Error(String message);
}
