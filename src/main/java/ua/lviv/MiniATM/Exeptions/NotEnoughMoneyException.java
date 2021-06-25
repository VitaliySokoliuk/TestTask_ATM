package ua.lviv.MiniATM.Exeptions;

public class NotEnoughMoneyException extends Throwable {

    public NotEnoughMoneyException(String errorMessage) {
        super(errorMessage);
    }
}
