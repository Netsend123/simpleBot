package my.bot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    public int state = 0;
    public String measure;
    public String material;
    public String phase;
    public String typeOfSet;
    public Double iw;

    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText() & (update.getMessage().getText().equals("/start") || update.getMessage().getText().equals("New"))) {
                state = 1;
                try {
                    execute(sendInlineKeyBoardMessageMeasure(update.getMessage().getChatId(), "kVt", "Amper", "Расчет сечения электрокабеля. Выберете единицы измерения"));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else {
                iw = Double.valueOf(update.getMessage().getText());
                SendMessage resout = new SendMessage();
                resout.setChatId(String.valueOf(update.getMessage().getChatId()));
                resout.setText("Введенные параметры:\n" + material + ", " + " " + typeOfSet + ", " + "фаз-" + phase + ", " + "\nзначение - " + iw + " "+ measure+"\n" + " Pезультат - " + counter(iw) + " мм квадратных.\nДля повторного рассчета наберите или нажмите /start или New");
                try {
                    execute(resout);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } else if (update.hasCallbackQuery() & state == 1) {
            measure = update.getCallbackQuery().getData();
            state = 2;
            try {
                execute(sendInlineKeyBoardMessageMeasure(update.getCallbackQuery().getMessage().getChatId(), "1", "3", "Выберете количество фаз"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery() & state == 2) {
            phase = update.getCallbackQuery().getData();
            state = 3;
            try {
                execute(sendInlineKeyBoardMessageMeasure(update.getCallbackQuery().getMessage().getChatId(), "Cu", "Al", "Выберете материал"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery() & state == 3) {
            material = update.getCallbackQuery().getData();
            state = 4;
            try {
                execute(sendInlineKeyBoardMessageMeasure(update.getCallbackQuery().getMessage().getChatId(), "KabelKanal", "Shtukaturka", "Выберете тип монтажа"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery() & state == 4) {
            typeOfSet = update.getCallbackQuery().getData();
            state = 5;
            SendMessage sm = new SendMessage();
            sm.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));
            sm.setText("Введите значение тока или мощности");
            System.out.println(state);
            try {
                execute(sm);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public SendMessage sendInlineKeyBoardMessageMeasure(long chatId, String v1, String v2, String txt) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText(v1);
        inlineKeyboardButton1.setCallbackData(v1);
        inlineKeyboardButton2.setText(v2);
        inlineKeyboardButton2.setCallbackData(v2);
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);
        keyboardButtonsRow1.add(inlineKeyboardButton2);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        SendMessage sm = new SendMessage();
        sm.setChatId(String.valueOf(chatId));
        sm.setText(txt);
        sm.setReplyMarkup(inlineKeyboardMarkup);
        return sm;
    }

    public String counter(Double iw) {
        Double i = 0.0;
        Double s = 0.0;
        if (measure.equals("kVt")) {
            i = iw * 220;
        } else {
            i = iw;
        }
        if (phase.equals("3")) {
            i = i / 1.76;
        }
        if (typeOfSet.equals("Shtukaturka")) {
            i = i * 0.9;
        }
        if (material.equals("Cu")) {
            if (i < 19) s = 1.5;
            else if (i >= 19 & i < 27) s = 2.5;
            else if (i >= 27 & i < 38) s = 4D;
            else if (i >= 38 & i < 46) s = 6D;
            else if (i >= 70 & i < 85) s = 10d;
            else if (i >= 85 & i < 115) s = 16d;
            else if (i >= 115 & i < 135) s = 25d;
            else if (i >= 135 & i < 175) s = 35d;
            else if (i >= 175 & i < 215) s = 50d;
            else if (i >= 215 & i < 260) s = 70d;
            else if (i >= 260 & i < 300) s = 95d;
            else s = 0d;
        }

        if (material.equals("Al")) {
            if (i < 22) s = 2.5;
            else if (i >= 22 & i < 28) s = 4D;
            else if (i >= 28 & i < 36) s = 4D;
            else if (i >= 36 & i < 50) s = 6D;
            else if (i >= 50 & i < 60) s = 10d;
            else if (i >= 60 & i < 85) s = 16d;
            else if (i >= 85 & i < 100) s = 25d;
            else if (i >= 100 & i < 135) s = 35d;
            else if (i >= 135 & i < 165) s = 50d;
            else if (i >= 165 & i < 200) s = 70d;
            else if (i >= 200 & i < 230) s = 95d;
            else s = 0d;
        }

        return String.valueOf(s);
    }

    public String getBotUsername() {
        return "raschetKabelya_bot";
    }

    public String getBotToken() {
        return "5512397953:AAFxM6b2CdoYRdYV8_oOhoxm_azj-k2AT_A";
    }
}

