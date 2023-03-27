package translatorBot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.deepl.api.*;

public class Bot extends TelegramLongPollingBot {
  private String botUserName = "HuanTranslaterBot";
  private Translator translator;

  public Bot() {
    initializeTranslator();
  }

  @Override
  public String getBotUsername() {
    return this.botUserName;
  }

  @Override
  public String getBotToken() {
    return getBotConfigProperty("BOT_API_TOKEN");
  }

  @Override
  public void onUpdateReceived(Update update) {
    System.out.println(update);
  }

  private String translateText(String text, String targetLang) throws Exception {
    TextResult result =
        translator.translateText(text, null, targetLang);
    String translateResult = result.getText();
    System.out.println(translateResult);
    return translateResult;
  }

  private void initializeTranslator() {
    // get DEEPL API Token and initialize translator
    String authKey = getBotConfigProperty("DEEPL_API_TOKEN");
    translator = new Translator(authKey);
  }

  private String getBotConfigProperty(String key) {
    try (InputStream input = new FileInputStream("src/main/resources/botConfig.properties")) {
      Properties properties = new Properties();

      // load botConFig properties file
      properties.load(input);

      String botToken = properties.getProperty(key);
      if (botToken == null) {
        throw new NoSuchElementException(key + " field not defined in botConfig.properties.");
      }

      return properties.getProperty(key);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return null;
  }
}
