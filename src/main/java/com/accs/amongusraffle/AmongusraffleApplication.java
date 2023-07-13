package com.accs.amongusraffle;

import com.accs.amongusraffle.service.EmailSender;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class AmongusraffleApplication implements CommandLineRunner {

  public static final String CIUDADANO_BODY =
      "Estimado monitor:\n\nTu rol en el juego de la mano negra es: Ciudadano.\n\nLos policías son: %s y %s. Éste es un mensaje automático generado por un programa informático. La recomendación es que borres éste mensaje tras su recepción.\n\nMucha suerte y felices colonias.";
  public static final String MANO_NEGRA_BODY =
      "Estimado monitor:\n\nTu rol en el juego de la mano negra es: Mano negra. La otra mano negra es: %s.\n\nÉste es un mensaje automático generado por un programa informático. La recomendación es que borres éste mensaje tras su recepción.\n\nMucha suerte y felices colonias.";
  public static final String POLICIA_BODY =
      "Estimado monitor:\n\nTu rol en el juego de la mano negra es: Policía. Tu compañero es: %s.\n\nÉste es un mensaje automático generado por un programa informático. La recomendación es que borres éste mensaje tras su recepción.\n\nMucha suerte y felices colonias.";
  public static final String SUBJECT = "Tu rol en la mano negra";

  private final EmailSender emailSender;

  public static void main(String[] args) {
    SpringApplication.run(AmongusraffleApplication.class, args);
  }

  @Override
  public void run(String... args) {
    List<Tuple2<String, String>> personas =
        new ArrayList<>(
            Arrays.asList(
                Tuple.of("<Nombre>", "<Email>"))); // TODO: Rellenar con todos los nombres y emails

    log.info("Enviando {} emails...", personas.size());
    Random rnd = new Random();
    List<Tuple2<String, String>> manosNegras = new ArrayList<>();
    List<Tuple2<String, String>> policias = new ArrayList<>();

    for (int i = 0; i < 2; i++) {
      Tuple2<String, String> result = personas.remove(rnd.nextInt(personas.size()));
      manosNegras.add(result);
    }

    for (int i = 0; i < 2; i++) {
      Tuple2<String, String> result = personas.remove(rnd.nextInt(personas.size()));
      policias.add(result);
    }

    for (Tuple2<String, String> persona : personas) {
      emailSender.sendEmail(
          persona._2,
          SUBJECT,
          String.format(CIUDADANO_BODY, policias.get(0)._1, policias.get(1)._1));
    }

    emailSender.sendEmail(
        policias.get(0)._2, SUBJECT, String.format(POLICIA_BODY, policias.get(1)._1));

    emailSender.sendEmail(
        policias.get(1)._2, SUBJECT, String.format(POLICIA_BODY, policias.get(0)._1));

    emailSender.sendEmail(
        manosNegras.get(0)._2, SUBJECT, String.format(MANO_NEGRA_BODY, manosNegras.get(1)._1));

    emailSender.sendEmail(
        manosNegras.get(1)._2, SUBJECT, String.format(MANO_NEGRA_BODY, manosNegras.get(0)._1));

    log.info("Todos los emails han sido enviados");
    System.out.println("Policías: " + policias.get(0)._1 + ", " + policias.get(1)._1);
  }
}
