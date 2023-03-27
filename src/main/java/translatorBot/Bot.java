package translatorBot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramLongPollingBot {

  @Override
  public String getBotUsername() {
    return "HuanTranslaterBot";
  }

  @Override
  public String getBotToken() {
    try (InputStream input = new FileInputStream("src/main/resources/botConfig.properties")) {
      Properties properties = new Properties();

      // load botConFig properties file
      properties.load(input);

      String botToken = properties.getProperty("BOT_API_TOKEN");
      if (botToken == null) {
        throw new NoSuchElementException("BOT_API_TOKEN field not defined in botConfig.properties.");
      }

      return properties.getProperty("BOT_API_TOKEN");

    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }

  @Override
  public void onUpdateReceived(Update update) {
    System.out.println(update);
  }
}
