package lib;

import java.awt.CardLayout;
import javax.swing.JPanel;

public class UIHelper {
    protected static void switchPanel(JPanel containerPanel, String panelName) {
        CardLayout cl = (CardLayout) (containerPanel.getLayout());
        cl.show(containerPanel, panelName);
    }
}