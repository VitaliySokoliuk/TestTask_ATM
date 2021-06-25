package ua.lviv.MiniATM.Exeptions;

public class IncorrectSumException extends Throwable {
    public IncorrectSumException(String errorMessage) {
        super(errorMessage);
    }
}
