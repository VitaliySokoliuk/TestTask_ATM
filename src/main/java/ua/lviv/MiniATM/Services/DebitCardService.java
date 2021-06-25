package ua.lviv.MiniATM.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.lviv.MiniATM.Exeptions.CardWasNotFoundException;
import ua.lviv.MiniATM.Exeptions.IncorrectSumException;
import ua.lviv.MiniATM.Exeptions.NotEnoughMoneyException;
import ua.lviv.MiniATM.Repos.DebitCardRepo;
import ua.lviv.MiniATM.entities.DebitCard;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DebitCardService {

    private DebitCardRepo debitCardRepo;

    @Autowired
    public DebitCardService(DebitCardRepo debitCardRepo) {
        this.debitCardRepo = debitCardRepo;
    }


    public void save(DebitCard debitCard) {
        debitCardRepo.save(debitCard);
    }

    public void putMoney(DebitCard debitCard, List<Integer> banknotes) {
        int sum = banknotes.stream().mapToInt(a -> a).sum();
        debitCard.setBalance(debitCard.getBalance() + sum);
        debitCardRepo.save(debitCard);
    }

    public boolean isBanknotesAreAppropriate(List<Integer> banknotes){
        return banknotes.stream().allMatch(x -> x == 500 || x == 200 || x == 100);
    }

    public boolean isCardNotExpired(LocalDate expiresEnd){
        LocalDate now = LocalDate.now();
        if(expiresEnd.getYear() < now.getYear()){
            return false;
        }
        return expiresEnd.getMonthValue() >= now.getMonthValue();
    }

    public Optional<DebitCard> findByCardNum(String cardNum){
        return debitCardRepo.findByCardNum(cardNum);
    }

    public void withdrawMoney(String cardNum, long sumToTakeOut)
            throws CardWasNotFoundException, NotEnoughMoneyException, IncorrectSumException {
        Optional<DebitCard> maybeDebitCard = findByCardNum(cardNum);
        if(!maybeDebitCard.isPresent()){
            throw new CardWasNotFoundException("Card wasn't found");
        }
        if(sumToTakeOut % 100 != 0){
            throw new IncorrectSumException("Incorrect sum to withdraw");
        }
        DebitCard debitCard = maybeDebitCard.get();
        if(debitCard.getBalance() < sumToTakeOut){
            throw new NotEnoughMoneyException("Not enough money on the card");
        }
        debitCard.setBalance(debitCard.getBalance() - sumToTakeOut);
        debitCardRepo.save(debitCard);
    }

    public void transferMoney(String cardNumFrom, String cardNumTo, long sumToTransfer)
            throws CardWasNotFoundException, IncorrectSumException, NotEnoughMoneyException {
        Optional<DebitCard> maybeCardFrom = findByCardNum(cardNumFrom);
        Optional<DebitCard> maybeCardTo = findByCardNum(cardNumTo);
        if(!maybeCardFrom.isPresent() || !maybeCardTo.isPresent()){
            throw new CardWasNotFoundException("Card wasn't found");
        }
        if(sumToTransfer < 0){
            throw new IncorrectSumException("Sum cannot be less than 0");
        }
        DebitCard cardFrom = maybeCardFrom.get();
        if(cardFrom.getBalance() < sumToTransfer){
            throw new NotEnoughMoneyException("Not enough money on the card");
        }
        cardFrom.setBalance(cardFrom.getBalance() - sumToTransfer);
        DebitCard cardTo = maybeCardTo.get();
        cardTo.setBalance(cardTo.getBalance() + sumToTransfer);
        debitCardRepo.save(cardFrom);
        debitCardRepo.save(cardTo);
    }

}
