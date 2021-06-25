package ua.lviv.MiniATM.Services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ua.lviv.MiniATM.Exeptions.CardWasNotFoundException;
import ua.lviv.MiniATM.Exeptions.IncorrectSumException;
import ua.lviv.MiniATM.Exeptions.NotEnoughMoneyException;
import ua.lviv.MiniATM.entities.DebitCard;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class DebitCardServiceTest {

    @Autowired
    private DebitCardService debitCardService;

    @Test
    void putMoney() {
        DebitCard debitCard = debitCardService.findByCardNum("1234123412341234").get();
        List<Integer> banknotes = new ArrayList<>();
        banknotes.add(100);
        banknotes.add(200);
        banknotes.add(500);
        long expectedValue = debitCard.getBalance() + 800;
        debitCardService.putMoney(debitCard,banknotes);
        long balance = debitCardService.findByCardNum("1234123412341234").get().getBalance();
        Assert.assertEquals(balance, expectedValue);
    }

    @Test
    void isBanknotesAreAppropriate() {
        List<Integer> banknotes = new ArrayList<>();
        banknotes.add(100);
        banknotes.add(200);
        banknotes.add(500);
        Assert.assertTrue(debitCardService.isBanknotesAreAppropriate(banknotes));
    }

    @Test
    void isBanknotesAreAppropriate2() {
        List<Integer> banknotes = new ArrayList<>();
        banknotes.add(100);
        banknotes.add(200);
        banknotes.add(300);
        Assert.assertFalse(debitCardService.isBanknotesAreAppropriate(banknotes));
    }

    @Test
    void isCardNotExpired() {
        LocalDate expiresEnd = LocalDate.of(2022, 9, 1);
        Assert.assertTrue(debitCardService.isCardNotExpired(expiresEnd));
    }

    @Test
    void isCardNotExpired2() {
        LocalDate expiresEnd = LocalDate.of(2021, 4, 1);
        Assert.assertFalse(debitCardService.isCardNotExpired(expiresEnd));
    }

    @Test
    void withdrawMoney() throws CardWasNotFoundException, NotEnoughMoneyException, IncorrectSumException {
        DebitCard debitCard = debitCardService.findByCardNum("1234123412341234").get();
        debitCardService.withdrawMoney("1234123412341234",300);
        DebitCard debitCard2 = debitCardService.findByCardNum("1234123412341234").get();
        Assert.assertEquals(debitCard.getBalance() - 300, debitCard2.getBalance());
    }

    @Test
    void withdrawMoney2() throws CardWasNotFoundException, NotEnoughMoneyException, IncorrectSumException {
        Assert.assertThrows(CardWasNotFoundException.class, () -> {
            debitCardService.withdrawMoney("12341234123412345",800);
        });
    }

    @Test
    void withdrawMoney3() throws CardWasNotFoundException, NotEnoughMoneyException, IncorrectSumException {
        Assert.assertThrows(NotEnoughMoneyException.class, () -> {
            debitCardService.withdrawMoney("1234123412341234",999999900);
        });
    }

    @Test
    void withdrawMoney4() throws CardWasNotFoundException, NotEnoughMoneyException, IncorrectSumException {
        Assert.assertThrows(IncorrectSumException.class, () -> {
            debitCardService.withdrawMoney("1234123412341234",850);
        });
    }

    @Test
    void transferMoney() throws CardWasNotFoundException, NotEnoughMoneyException, IncorrectSumException {
        DebitCard cardFrom = debitCardService.findByCardNum("1234123412341234").get();
        DebitCard cardTo = debitCardService.findByCardNum("1234567812345678").get();
        debitCardService.transferMoney("1234123412341234", "1234567812345678", 500);
        DebitCard cardFrom2 = debitCardService.findByCardNum("1234123412341234").get();
        DebitCard cardTo2 = debitCardService.findByCardNum("1234567812345678").get();
        Assert.assertEquals(cardFrom.getBalance() - 500, cardFrom2.getBalance());
        Assert.assertEquals(cardTo.getBalance() + 500, cardTo2.getBalance());
    }

    @Test
    void transferMoney2() throws CardWasNotFoundException, NotEnoughMoneyException, IncorrectSumException {
        Assert.assertThrows(CardWasNotFoundException.class, () -> {
            debitCardService.transferMoney("1234123412341234", "12345678123456789", 500);
        });
    }

    @Test
    void transferMoney3() throws CardWasNotFoundException, NotEnoughMoneyException, IncorrectSumException {
        Assert.assertThrows(NotEnoughMoneyException.class, () -> {
            debitCardService.transferMoney("1234123412341234", "1234567812345678", 99999900);
        });
    }

    @Test
    void transferMoney4() throws CardWasNotFoundException, NotEnoughMoneyException, IncorrectSumException {
        Assert.assertThrows(IncorrectSumException.class, () -> {
            debitCardService.transferMoney("1234123412341234", "1234567812345678", -500);
        });
    }

}