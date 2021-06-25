package ua.lviv.MiniATM.Models;

import ua.lviv.MiniATM.entities.DebitCard;

import java.util.List;

public class PutInInfo {

    private DebitCard debitCard;
    private List<Integer> banknotes;

    public DebitCard getDebitCard() {
        return debitCard;
    }

    public void setDebitCard(DebitCard debitCard) {
        this.debitCard = debitCard;
    }

    public List<Integer> getBanknotes() {
        return banknotes;
    }

    public void setBanknotes(List<Integer> banknotes) {
        this.banknotes = banknotes;
    }

}
