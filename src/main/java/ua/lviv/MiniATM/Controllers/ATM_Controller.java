package ua.lviv.MiniATM.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.lviv.MiniATM.Exeptions.CardWasNotFoundException;
import ua.lviv.MiniATM.Exeptions.IncorrectSumException;
import ua.lviv.MiniATM.Exeptions.NotEnoughMoneyException;
import ua.lviv.MiniATM.Models.PutInInfo;
import ua.lviv.MiniATM.Services.DebitCardService;

import java.util.List;

@RestController
public class ATM_Controller {

    private DebitCardService debitCardService;

    @Autowired
    public ATM_Controller(DebitCardService debitCardService) {
        this.debitCardService = debitCardService;
    }

    @PostMapping(value = "/putMoneyIn")
    public ResponseEntity<String> putMoneyIn(@RequestBody PutInInfo putInInfo){
        List<Integer> banknotes = putInInfo.getBanknotes();
        if(!debitCardService.isBanknotesAreAppropriate(banknotes)){
            return new ResponseEntity<>("Was found unknown banknote", HttpStatus.BAD_REQUEST);
        }
        if(!debitCardService.isCardNotExpired(putInInfo.getDebitCard().getExpiresEnd())){
            return new ResponseEntity<>("Card expired", HttpStatus.BAD_REQUEST);
        }
        debitCardService.putMoney(putInInfo.getDebitCard(), banknotes);
        return new ResponseEntity<>("Money was put in the user card", HttpStatus.OK);
    }

    @GetMapping(value = "/withdrawMoney")
    public ResponseEntity<String> withdrawMoney(@RequestParam String cardNum,
                                                @RequestParam long sumToTakeOut){
        try {
            debitCardService.withdrawMoney(cardNum, sumToTakeOut);
        } catch (CardWasNotFoundException | NotEnoughMoneyException | IncorrectSumException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Money was withdrawn from the user card", HttpStatus.OK);
    }

    @GetMapping(value = "/transferMoney")
    public ResponseEntity<String> transferMoney(@RequestParam String cardNumFrom,
                                                @RequestParam String cardNumTo,
                                                @RequestParam long sumToTakeOut){
        try {
            debitCardService.transferMoney(cardNumFrom, cardNumTo, sumToTakeOut);
        } catch (CardWasNotFoundException | NotEnoughMoneyException | IncorrectSumException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Money was transferred", HttpStatus.OK);
    }

}
