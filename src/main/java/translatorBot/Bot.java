package translatorBot;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
  private Boolean waitingTargetLang = false;
  private String textToTranslate = null;


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

    if (waitingTargetLang) {
      translateTextProtocol(id, text);
    } else {
      getLanguageProtocol(id, text);
    }
  }


  private void getLanguageProtocol(Long id, String text) {
    textToTranslate = text;
    sendText(id, "Type in the language that you want me to translate into.");
    waitingTargetLang = true;
  }


  private void translateTextProtocol(Long id, String text) {
    String targetLangCode = getTargetLanguageCode(text);
    if (targetLangCode == null) {
      sendText(id, "The language you provided is either not valid or unavailable,"
          + " try typing the language again.");
    } else {
      try {
        waitingTargetLang = false;
        String translatedText = translateText(textToTranslate, targetLangCode);
        sendText(id, translatedText);
      } catch (Exception e) {
        e.printStackTrace();
      }
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


  private String translateText(String text, String targetLangCode) throws Exception {
    TextResult result =
        translator.translateText(text, null, targetLangCode);
    String translateResult = result.getText();
    System.out.println(translateResult);
    return translateResult;
  }


  private void initializeTranslator() {
    // get DEEPL API Token and initialize translator
    String authKey = getBotConfigProperty("DEEPL_API_TOKEN");
    translator = new Translator(authKey);
  }


  private String getTargetLanguageCode(String targetLang) {
    try {
      List<Language> availLangs = getSourceLanguages();
      for (Language language : availLangs) {
        if (language.getName().equalsIgnoreCase(targetLang)) {
          return language.getCode();
        }
      }
      return null;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  private List<Language> getSourceLanguages() throws Exception {
    return translator.getSourceLanguages();
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
