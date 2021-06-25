package ua.lviv.MiniATM.Exeptions;

public class CardWasNotFoundException extends Throwable{

    public CardWasNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
