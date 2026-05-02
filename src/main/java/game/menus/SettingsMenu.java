package game.menus;

import core.language.CoreSettingNames;
import core.renderables.CoreSettingsRenderable;
import core.settings.CoreFloatSettings;
import core.settings.CoreKeySettings;
import core.settings.CoreOptionSettings;
import core.settings.CoreToggleSettings;

public final class SettingsMenu extends CoreSettingsRenderable {

    public SettingsMenu() {
        addSlider(CoreFloatSettings.GUI_SIZE, CoreSettingNames.GUI_SIZE);
        addSlider(CoreFloatSettings.SENSITIVITY, CoreSettingNames.SENSITIVITY);
        addSlider(CoreFloatSettings.TEXT_SIZE, CoreSettingNames.TEXT_SIZE);
        addSlider(CoreFloatSettings.MASTER_AUDIO, CoreSettingNames.MASTER_AUDIO);
        addSlider(CoreFloatSettings.UI_AUDIO, CoreSettingNames.UI_AUDIO);
        addSlider(CoreFloatSettings.RIM_THICKNESS, CoreSettingNames.RIM_THICKNESS);

        addKeySelector(CoreKeySettings.RESIZE_WINDOW, CoreSettingNames.RESIZE_WINDOW);
        addKeySelector(CoreKeySettings.RELOAD_ASSETS, CoreSettingNames.RELOAD_ASSETS);
        addKeySelector(CoreKeySettings.RELOAD_SETTINGS, CoreSettingNames.RELOAD_SETTINGS);
        addKeySelector(CoreKeySettings.RELOAD_FONT, CoreSettingNames.RELOAD_FONT);

        addOption(CoreOptionSettings.FONT, CoreSettingNames.FONT);
        addOption(CoreOptionSettings.LANGUAGE, CoreSettingNames.LANGUAGE);
        addOption(CoreOptionSettings.TEXTURE_PACK, CoreSettingNames.TEXTURE_PACK);

        addToggle(CoreToggleSettings.RAW_MOUSE_INPUT, CoreSettingNames.RAW_MOUSE_INPUT);
        addToggle(CoreToggleSettings.V_SYNC, CoreSettingNames.V_SYNC);
    }
}
