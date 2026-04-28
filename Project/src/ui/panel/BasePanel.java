package ui.panels;

import ui.theme.AppTheme;
import ui.MainFrame;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import javax.swing.table.DefaultTableModel;


public abstract class BasePanel extends JPanel {

    protected final User user;

    public BasePanel(User user) {
        this.user = user;
        setBackground(AppTheme.BG_PAGE);
        setLayout(new BorderLayout(0,0));
    }


    protected JScrollPane scrollWrap(JPanel content) {
        JScrollPane sp = new JScrollPane(content);
        sp.setBorder(null); sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUI(new AppTheme.SlimScrollBar());
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(6,0));
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return sp;
    }


    protected JPanel pageContent() {
        JPanel p = new JPanel(); p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(24,24,24,24));
        return p;
    }

    protected JPanel row(Component... comps) {
        JPanel p = new JPanel(new GridLayout(1, comps.length, 16, 0)); p.setOpaque(false);
        for (Component c : comps) p.add(c); return p;
    }

    protected void vgap(JPanel target, int h) { target.add(Box.createVerticalStrut(h)); }

    protected DefaultTableModel model(String[] cols) {
        return new DefaultTableModel(cols,0){ public boolean isCellEditable(int r,int c){return false;} };
    }

    protected void goTo(String cardKey) {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w instanceof MainFrame) {
            ((MainFrame) w).navigateTo(cardKey);
        }
    }
}
