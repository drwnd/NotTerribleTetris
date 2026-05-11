import core.rendering_api.Window;
import core.settings.Settings;
import game.logic.Game;
import game.menus.MainMenu;
import game.settings.*;

public class Launcher {
    public static void main(String[] args) {
        Settings.registerSettingsEnums(FloatSettings.class, IntSettings.class, KeySettings.class, ToggleSettings.class, OptionSettings.class);
        Window.init("Game Title");
        Window.pushRenderable(new MainMenu());
        Window.renderLoop();
        Window.cleanUp();
        Game.getInstance().saveScoreIfRunning();
    }
}
