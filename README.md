# HuanTranslaterBot
A Telegram Bot that translates a message to another language using DeepL Translator.

## Setup
Create a `botConfig.properties` file under `src/main/resources` directory. Modify the file to include the following:
```
BOT_API_TOKEN = <YOUR_TELEGRAM_BOT_API_TOKEN>
DEEPL_API_TOKEN = <YOUR_DEEPL_API_TOKEN>
```
replacing the tagged fields with your respective API keys.

Here are some useful links to help you set up and retrieve the API keys:
- Telegram Bot: https://core.telegram.org/bots/tutorial#introduction
- DeepL: https://www.deepl.com/pro-api

Your Telegram bot should have the following commands set:
- /help - Help prompt that provides user information on how to use the bot.
- /settargetlanguage - Set the language that the user wants the bot to translate to.

## Activate the bot
Run the `main` function in `src/main/java/Main.java`, and your bot will be up and running!
