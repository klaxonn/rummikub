package rummikub;

import rummikub.ihm.InterfaceConsole;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class Main implements CommandLineRunner {

	@Override
    public void run(String... args) throws Exception {
		new InterfaceConsole().DemarrerPartie();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

