package ui.theme;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

/**
 * AppTheme.java
 * Central design system — all colours, fonts, and component factories.
 * Every panel imports from here to stay consistent.
 */
public class AppTheme {

    // ── Colour Palette ────────────────────────────────────────
    public static final Color PRIMARY        = new Color(0x2563EB);
    public static final Color PRIMARY_HOVER  = new Color(0x1D4ED8);
    public static final Color PRIMARY_LIGHT  = new Color(0xEFF6FF);
    public static final Color SUCCESS        = new Color(0x10B981);
    public static final Color SUCCESS_LIGHT  = new Color(0xECFDF5);
    public static final Color DANGER         = new Color(0xEF4444);
    public static final Color DANGER_LIGHT   = new Color(0xFEF2F2);
    public static final Color WARNING        = new Color(0xF59E0B);
    public static final Color WARNING_LIGHT  = new Color(0xFFFBEB);
    public static final Color PURPLE         = new Color(0x7C3AED);
    public static final Color PURPLE_LIGHT   = new Color(0xF5F3FF);
    public static final Color TEAL           = new Color(0x0D9488);
    public static final Color TEAL_LIGHT     = new Color(0xF0FDFA);

    public static final Color BG_PAGE        = new Color(0xF9FAFB);
    public static final Color BG_CARD        = Color.WHITE;
    public static final Color BG_SIDEBAR     = new Color(0x1E293B);
    public static final Color BG_SIDEBAR_HVR = new Color(0x334155);
    public static final Color BG_SIDEBAR_ACT = new Color(0x2563EB);
    public static final Color BG_HEADER      = Color.WHITE;
    public static final Color BG_INPUT       = Color.WHITE;
    public static final Color BG_TABLE_ALT   = new Color(0xF8FAFC);
    public static final Color BG_TABLE_SEL   = new Color(0xEFF6FF);

    public static final Color TEXT_DARK      = new Color(0x111827);
    public static final Color TEXT_BODY      = new Color(0x374151);
    public static final Color TEXT_MUTED     = new Color(0x9CA3AF);
    public static final Color TEXT_SIDEBAR   = new Color(0xCBD5E1);
    public static final Color TEXT_SIDEBAR_A = Color.WHITE;
    public static final Color BORDER         = new Color(0xE5E7EB);
    public static final Color BORDER_FOCUS   = new Color(0x93C5FD);
    public static final Color SHADOW_COLOR   = new Color(0, 0, 0, 18);

    // ── Fonts ─────────────────────────────────────────────────
    public static final Font  F_TITLE   = new Font("Segoe UI", Font.BOLD,   22);
    public static final Font  F_H2      = new Font("Segoe UI", Font.BOLD,   16);
    public static final Font  F_H3      = new Font("Segoe UI", Font.BOLD,   14);
    public static final Font  F_BODY    = new Font("Segoe UI", Font.PLAIN,  13);
    public static final Font  F_SMALL   = new Font("Segoe UI", Font.PLAIN,  12);
    public static final Font  F_BOLD    = new Font("Segoe UI", Font.BOLD,   13);
    public static final Font  F_LABEL   = new Font("Segoe UI", Font.PLAIN,  12);
    public static final Font  F_BTN     = new Font("Segoe UI", Font.BOLD,   13);
    public static final Font  F_TABLE   = new Font("Segoe UI", Font.PLAIN,  13);
    public static final Font  F_TABLE_H = new Font("Segoe UI", Font.BOLD,   12);
    public static final Font  F_MONO    = new Font("Consolas",  Font.PLAIN,  12);
    public static final Font  F_STAT    = new Font("Segoe UI", Font.BOLD,   28);
    public static final Font  F_STAT_SM = new Font("Segoe UI", Font.BOLD,   20);

    // ── Global Swing defaults ─────────────────────────────────
    public static void apply() {
        UIManager.put("Panel.background",             BG_PAGE);
        UIManager.put("Label.foreground",             TEXT_BODY);
        UIManager.put("TextField.background",         BG_INPUT);
        UIManager.put("TextField.foreground",         TEXT_DARK);
        UIManager.put("TextField.caretForeground",    PRIMARY);
        UIManager.put("PasswordField.background",     BG_INPUT);
        UIManager.put("PasswordField.foreground",     TEXT_DARK);
        UIManager.put("ComboBox.background",          BG_INPUT);
        UIManager.put("ComboBox.foreground",          TEXT_DARK);
        UIManager.put("Table.background",             BG_CARD);
        UIManager.put("Table.foreground",             TEXT_BODY);
        UIManager.put("Table.gridColor",              BORDER);
        UIManager.put("TableHeader.background",       new Color(0xF8FAFC));
        UIManager.put("TableHeader.foreground",       TEXT_BODY);
        UIManager.put("ScrollPane.background",        BG_PAGE);
        UIManager.put("TabbedPane.background",        BG_PAGE);
        UIManager.put("ToolTip.background",           new Color(0x1E293B));
        UIManager.put("ToolTip.foreground",           Color.WHITE);
        UIManager.put("OptionPane.background",        BG_CARD);
        UIManager.put("OptionPane.messageForeground", TEXT_BODY);
    }

    // ─────────────────────────────────────────────────────────
    //  BUTTON FACTORIES
    // ─────────────────────────────────────────────────────────

    /** Primary filled button */
    public static JButton primaryBtn(String text) {
        return colorBtn(text, PRIMARY, PRIMARY_HOVER, Color.WHITE);
    }

    /** Success green button */
    public static JButton successBtn(String text) {
        return colorBtn(text, SUCCESS, new Color(0x059669), Color.WHITE);
    }

    /** Danger red button */
    public static JButton dangerBtn(String text) {
        return colorBtn(text, DANGER, new Color(0xDC2626), Color.WHITE);
    }

    /** Warning amber button */
    public static JButton warningBtn(String text) {
        return colorBtn(text, WARNING, new Color(0xD97706), Color.WHITE);
    }

    /** Ghost / outline button */
    public static JButton ghostBtn(String text) {
        JButton btn = new JButton(text) {
            boolean hov = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hov=true;  repaint(); }
                public void mouseExited(MouseEvent e)  { hov=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = setup(g);
                g2.setColor(hov ? PRIMARY_LIGHT : Color.WHITE);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),10,10));
                g2.setColor(hov ? PRIMARY : BORDER);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(0.75f,0.75f,getWidth()-1.5f,getHeight()-1.5f,10,10));
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setFont(F_BTN); btn.setForeground(PRIMARY);
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(9,18,9,18));
        return btn;
    }

    /** Small icon-style action button */
    public static JButton iconBtn(String text, Color bg) {
        return colorBtn(text, bg, bg.darker(), Color.WHITE);
    }

    private static JButton colorBtn(String text, Color bg, Color hover, Color fg) {
        JButton btn = new JButton(text) {
            boolean hov=false, press=false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e)  { hov=true;  repaint(); }
                public void mouseExited(MouseEvent e)   { hov=false; press=false; repaint(); }
                public void mousePressed(MouseEvent e)  { press=true; repaint(); }
                public void mouseReleased(MouseEvent e) { press=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = setup(g);
                Color c = press ? hover.darker() : (hov ? hover : bg);
                g2.setColor(c);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),10,10));
                g2.dispose(); super.paintComponent(g);
            }
        };
        btn.setFont(F_BTN); btn.setForeground(fg);
        btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setFocusPainted(false); btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10,20,10,20));
        return btn;
    }

    // ─────────────────────────────────────────────────────────
    //  INPUT FACTORIES
    // ─────────────────────────────────────────────────────────

    public static JTextField textField(String placeholder) {
        JTextField f = new JTextField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = setup(g);
                g2.setColor(isEnabled() ? BG_INPUT : new Color(0xF3F4F6));
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),8,8));
                g2.dispose(); super.paintComponent(g);
            }
        };
        styleInput(f, placeholder); return f;
    }

    public static JPasswordField passwordField(String placeholder) {
        JPasswordField f = new JPasswordField() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = setup(g);
                g2.setColor(BG_INPUT);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),8,8));
                g2.dispose(); super.paintComponent(g);
            }
        };
        styleInput(f, placeholder); return f;
    }

    public static JComboBox<String> comboBox(String... items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(F_BODY); cb.setBackground(BG_INPUT); cb.setForeground(TEXT_DARK);
        cb.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            new EmptyBorder(6,10,6,10)));
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> l, Object v, int i, boolean sel, boolean foc) {
                super.getListCellRendererComponent(l,v,i,sel,foc);
                setBackground(sel ? PRIMARY_LIGHT : BG_INPUT);
                setForeground(sel ? PRIMARY : TEXT_DARK);
                setFont(F_BODY); setBorder(new EmptyBorder(6,12,6,12));
                return this;
            }
        });
        return cb;
    }

    private static void styleInput(JTextField f, String ph) {
        f.setFont(F_BODY); f.setForeground(TEXT_DARK); f.setCaretColor(PRIMARY);
        f.setOpaque(false);
        f.setBorder(new CompoundBorder(new RoundedFocusBorder(f), new EmptyBorder(10,14,10,14)));
        if (ph != null) {
            f.putClientProperty("placeholder", ph);
            f.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { f.repaint(); }
                public void focusLost(FocusEvent e)   { f.repaint(); }
            });
        }
    }

    static class RoundedFocusBorder extends AbstractBorder {
        private final JTextField f;
        RoundedFocusBorder(JTextField fld) { this.f = fld; }
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = setup(g);
            g2.setColor(f.isFocusOwner() ? BORDER_FOCUS : BORDER);
            g2.setStroke(new BasicStroke(f.isFocusOwner() ? 1.8f : 1f));
            g2.draw(new RoundRectangle2D.Float(x+.5f,y+.5f,w-1,h-1,8,8));
            g2.dispose();
        }
        @Override public Insets getBorderInsets(Component c) { return new Insets(1,1,1,1); }
    }

    // ─────────────────────────────────────────────────────────
    //  TABLE FACTORY
    // ─────────────────────────────────────────────────────────

    public static JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model) {
            @Override public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (!isRowSelected(row)) {
                    c.setBackground(row % 2 == 0 ? BG_CARD : BG_TABLE_ALT);
                    c.setForeground(TEXT_BODY);
                } else {
                    c.setBackground(BG_TABLE_SEL);
                    c.setForeground(PRIMARY);
                }
                if (c instanceof JLabel) {
                    ((JLabel)c).setBorder(new EmptyBorder(0,12,0,12));
                    ((JLabel)c).setFont(F_TABLE);
                }
                return c;
            }
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        t.setFont(F_TABLE); t.setRowHeight(34);
        t.setShowGrid(false); t.setShowHorizontalLines(true);
        t.setGridColor(BORDER);
        t.setIntercellSpacing(new Dimension(0,0));
        t.setSelectionBackground(BG_TABLE_SEL);
        t.setSelectionForeground(PRIMARY);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setFillsViewportHeight(true);
        // Header
        JTableHeader hdr = t.getTableHeader();
        hdr.setFont(F_TABLE_H); hdr.setBackground(new Color(0xF8FAFC));
        hdr.setForeground(TEXT_MUTED); hdr.setPreferredSize(new Dimension(0,38));
        hdr.setBorder(BorderFactory.createMatteBorder(0,0,1,0,BORDER));
        hdr.setReorderingAllowed(false);
        hdr.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable tb, Object v, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(tb,v,sel,foc,row,col);
                setText(v!=null?v.toString().toUpperCase():"");
                setBackground(new Color(0xF8FAFC)); setForeground(TEXT_MUTED);
                setFont(F_TABLE_H); setBorder(new EmptyBorder(0,12,0,12));
                setHorizontalAlignment(SwingConstants.LEFT);
                return this;
            }
        });
        return t;
    }

    public static JScrollPane scrollTable(JTable t) {
        JScrollPane sp = new JScrollPane(t);
        sp.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
        sp.setBackground(BG_CARD);
        sp.getViewport().setBackground(BG_CARD);
        sp.getVerticalScrollBar().setUI(new SlimScrollBar());
        sp.getVerticalScrollBar().setPreferredSize(new Dimension(6,0));
        sp.getHorizontalScrollBar().setUI(new SlimScrollBar());
        sp.getHorizontalScrollBar().setPreferredSize(new Dimension(0,6));
        return sp;
    }

    // ─────────────────────────────────────────────────────────
    //  CARD & PANEL FACTORIES
    // ─────────────────────────────────────────────────────────

    /** White rounded card with shadow */
    public static JPanel card(int radius) {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = setup(g);
                // Shadow
                g2.setColor(SHADOW_COLOR);
                g2.fill(new RoundRectangle2D.Float(3,4,getWidth()-6,getHeight()-4,radius+2,radius+2));
                // Card
                g2.setColor(BG_CARD);
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth()-3,getHeight()-3,radius,radius));
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-4,getHeight()-4,radius,radius));
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
    }

    /** Stat card with coloured top accent bar */
    public static JPanel statCard(String value, String label, String sublabel,
                                   Color accent, Color accentLight) {
        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = setup(g);
                g2.setColor(SHADOW_COLOR); g2.fill(new RoundRectangle2D.Float(3,4,getWidth()-6,getHeight()-4,12,12));
                g2.setColor(BG_CARD);     g2.fill(new RoundRectangle2D.Float(0,0,getWidth()-3,getHeight()-3,12,12));
                g2.setColor(BORDER); g2.setStroke(new BasicStroke(0.8f));
                g2.draw(new RoundRectangle2D.Float(0.5f,0.5f,getWidth()-4,getHeight()-4,12,12));
                // Accent bar top-left corner
                g2.setColor(accent);
                g2.fill(new RoundRectangle2D.Float(0,0,4,getHeight()-3,0,0));
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        card.setLayout(new BorderLayout(0,4));
        card.setBorder(new EmptyBorder(18,22,18,22));
        card.setOpaque(false);

        // Icon dot
        JPanel dot = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=setup(g);
                g2.setColor(accentLight); g2.fillOval(0,0,36,36);
                g2.setColor(accent);
                g2.setFont(new Font("Segoe UI",Font.BOLD,16));
                FontMetrics fm=g2.getFontMetrics(); String t=label.substring(0,1);
                g2.drawString(t,18-fm.stringWidth(t)/2,13+fm.getAscent()/2);
                g2.dispose();
            }
            @Override public boolean isOpaque() { return false; }
        };
        dot.setPreferredSize(new Dimension(36,36)); dot.setOpaque(false);

        JLabel valLbl = new JLabel(value); valLbl.setFont(F_STAT); valLbl.setForeground(TEXT_DARK);
        JLabel namLbl = new JLabel(label); namLbl.setFont(F_BOLD); namLbl.setForeground(TEXT_BODY);
        JLabel subLbl = new JLabel(sublabel); subLbl.setFont(F_SMALL); subLbl.setForeground(TEXT_MUTED);

        JPanel textP = new JPanel(); textP.setOpaque(false); textP.setLayout(new BoxLayout(textP,BoxLayout.Y_AXIS));
        textP.add(valLbl); textP.add(Box.createVerticalStrut(2)); textP.add(namLbl); textP.add(subLbl);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0)); right.setOpaque(false); right.add(dot);
        card.add(textP,BorderLayout.CENTER); card.add(right,BorderLayout.EAST);
        return card;
    }

    /** Section header label */
    public static JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text); l.setFont(F_H2); l.setForeground(TEXT_DARK);
        l.setBorder(new EmptyBorder(0,0,16,0)); return l;
    }

    /** Field label above an input */
    public static JLabel fieldLabel(String text) {
        JLabel l = new JLabel(text); l.setFont(new Font("Segoe UI",Font.BOLD,12));
        l.setForeground(TEXT_BODY); l.setBorder(new EmptyBorder(0,0,5,0)); return l;
    }

    /** Status badge */
    public static JLabel badge(String text, Color fg, Color bg) {
        JLabel l = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=setup(g); g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),20,20));
                g2.dispose(); super.paintComponent(g);
            }
        };
        l.setFont(new Font("Segoe UI",Font.BOLD,11)); l.setForeground(fg); l.setBackground(bg);
        l.setOpaque(false); l.setBorder(new EmptyBorder(3,10,3,10));
        return l;
    }

    /** Thin horizontal separator */
    public static JSeparator separator() {
        JSeparator s = new JSeparator(); s.setForeground(BORDER); s.setBackground(BG_PAGE); return s;
    }

    /** Horizontal button row, left-aligned */
    public static JPanel buttonRow(JButton... btns) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT,8,0)); p.setOpaque(false);
        for (JButton b : btns) p.add(b); return p;
    }

    /** Form row: label on left, field on right */
    public static JPanel formRow(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0,5)); p.setOpaque(false);
        p.add(fieldLabel(label), BorderLayout.NORTH); p.add(field, BorderLayout.CENTER); return p;
    }

    // ─────────────────────────────────────────────────────────
    //  PROGRESS BAR
    // ─────────────────────────────────────────────────────────

    /** Custom progress bar with colour based on value */
    public static JPanel progressBar(String label, int percent) {
        JPanel wrap = new JPanel(new BorderLayout(0,4)); wrap.setOpaque(false);
        JLabel lbl = new JLabel(label); lbl.setFont(F_SMALL); lbl.setForeground(TEXT_BODY);
        JLabel pctLbl = new JLabel(percent+"%"); pctLbl.setFont(F_BOLD);
        Color bar = percent>=80 ? SUCCESS : percent==80 ? WARNING : DANGER;
        pctLbl.setForeground(bar);
        JPanel header = new JPanel(new BorderLayout()); header.setOpaque(false);
        header.add(lbl, BorderLayout.WEST); header.add(pctLbl, BorderLayout.EAST);
        JPanel track = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2=setup(g);
                g2.setColor(new Color(0xE5E7EB)); g2.fill(new RoundRectangle2D.Float(0,0,getWidth(),getHeight(),6,6));
                int w=(int)((getWidth()-2)*(percent/100.0));
                g2.setColor(bar); g2.fill(new RoundRectangle2D.Float(1,1,w,getHeight()-2,6,6));
                g2.dispose();
            }
        };
        track.setPreferredSize(new Dimension(0,8)); track.setOpaque(false);
        wrap.add(header,BorderLayout.NORTH); wrap.add(track,BorderLayout.CENTER); return wrap;
    }

    // ─────────────────────────────────────────────────────────
    //  UTILITY
    // ─────────────────────────────────────────────────────────

    public static Graphics2D setup(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,         RenderingHints.VALUE_RENDER_QUALITY);
        return g2;
    }

    public static Color blend(Color a, Color b, float t) {
        return new Color(
            (int)(a.getRed()+(b.getRed()-a.getRed())*t),
            (int)(a.getGreen()+(b.getGreen()-a.getGreen())*t),
            (int)(a.getBlue()+(b.getBlue()-a.getBlue())*t));
    }

    // ─────────────────────────────────────────────────────────
    //  SLIM SCROLLBAR
    // ─────────────────────────────────────────────────────────

    public static class SlimScrollBar extends BasicScrollBarUI {
        @Override protected void configureScrollBarColors() {
            thumbColor = new Color(0x94A3B8); trackColor = new Color(0xF1F5F9);
        }
        @Override protected JButton createDecreaseButton(int o) { return zero(); }
        @Override protected JButton createIncreaseButton(int o) { return zero(); }
        private JButton zero() { JButton b=new JButton(); b.setPreferredSize(new Dimension(0,0)); return b; }
        @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2=setup(g); g2.setColor(thumbColor);
            g2.fill(new RoundRectangle2D.Float(r.x+1,r.y+2,r.width-2,r.height-4,4,4)); g2.dispose();
        }
        @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            g.setColor(trackColor); g.fillRect(r.x,r.y,r.width,r.height);
        }
    }
}
