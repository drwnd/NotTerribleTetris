package core.language;

public enum CoreSettingNames implements Translatable {
    GUI_SIZE,
    SENSITIVITY,
    TEXT_SIZE,
    MASTER_AUDIO,
    UI_AUDIO,
    RIM_THICKNESS,
    RESIZE_WINDOW,
    RELOAD_ASSETS,
    RELOAD_SETTINGS,
    RELOAD_FONT,
    FONT,
    LANGUAGE,
    TEXTURE_PACK,
    RAW_MOUSE_INPUT,
    V_SYNC;

    @Override
    public String translationFileName() {
        return "coreSettingNames";
    }
}
