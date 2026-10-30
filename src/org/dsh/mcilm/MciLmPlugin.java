/*
 * 更好的联机 —— 插件入口
 *
 * 本插件是第三方独立作品，与 HMCL 官方无关。
 */
package org.dsh.mcilm;

import org.jackhuang.hmcl.plugin.HmclPlugin;
import org.jackhuang.hmcl.plugin.PluginMenuService;

/// HMCL plugin that adds MciLm-link as a second way to play together.
///
/// <p>MciLm-link is a cross-network tunnelling tool with a P2P + relay architecture. Its
/// command line client exposes an NDJSON event stream (`--json`) specifically for launchers,
/// so the plugin drives it as a child process: the host mode creates a room and prints the
/// room ID that friends join with, the guest mode joins a room and opens a local port that
/// the game connects to.</p>
///
/// <p>The plugin contributes one tab to the launcher's multiplayer page, next to the
/// built-in 联机大厅 entry.</p>
///
/// <p>The lobby and the EasyTier tunnel were tried and removed: the lobby needs a shared server
/// that is reachable from the player's network, and nothing suitable was available. The room list
/// sources stay in the repository should a server turn up later.</p>
public class MciLmPlugin extends HmclPlugin {

    private MciLmPage page;
    private MciLmTab tab;
    private UiPatcher uiPatcher;

    @Override
    public void onEnable() {
        page = new MciLmPage(getContext());
        tab = new MciLmTab(page);
        PluginMenuService.registerMultiplayerTab(tab);

        uiPatcher = new UiPatcher(getContext());
        uiPatcher.start();

        getContext().logInfo("MciLm-link plugin enabled; executable: " + page.getExecutable());
    }

    @Override
    public void onDisable() {
        if (uiPatcher != null) {
            uiPatcher.stop();
            uiPatcher = null;
        }
        if (tab != null) {
            PluginMenuService.unregisterMultiplayerTab(tab);
            tab = null;
        }
        if (page != null) {
            page.shutdown();
            page = null;
        }
        getContext().logInfo("MciLm-link plugin disabled");
    }
}
