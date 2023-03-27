package translatorBot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.deepl.api.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
  private String botUserName = "HuanTranslatorBot";
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
    var msg = update.getMessage();
    var user = msg.getFrom();
    var id = user.getId();
    var text = msg.getText();

    try {
      String translatedText = translateText(text, "ZH");
      sendText(id, translatedText);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


  private void sendText(Long user, String msg) {
    SendMessage sm = SendMessage.builder()
        .chatId(user.toString())
        .text(msg).build();
    try {
      execute(sm);
    } catch (TelegramApiException e) {
      throw new RuntimeException(e);
    }
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
