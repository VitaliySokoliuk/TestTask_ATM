package ua.lviv.MiniATM;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.lviv.MiniATM.Repos.DebitCardRepo;
import ua.lviv.MiniATM.Services.DebitCardService;
import ua.lviv.MiniATM.entities.DebitCard;

import java.time.LocalDate;

@SpringBootApplication
public class MiniAtmApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniAtmApplication.class, args);
	}

}
