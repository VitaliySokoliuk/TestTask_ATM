package ua.lviv.MiniATM.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class DebitCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "cardNumber", unique = true)
    private String cardNum;
    private LocalDate expiresEnd;
    private short cvvCode;
    private long balance;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public LocalDate getExpiresEnd() {
        return expiresEnd;
    }

    public void setExpiresEnd(LocalDate expiresEnd) {
        this.expiresEnd = expiresEnd;
    }

    public short getCvvCode() {
        return cvvCode;
    }

    public void setCvvCode(short cvvCode) {
        this.cvvCode = cvvCode;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "DebitCard{" +
                "cardNum='" + cardNum + '\'' +
                ", balance=" + balance +
                '}';
    }
}
