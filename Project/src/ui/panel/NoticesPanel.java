package ui.panels;

import ui.theme.AppTheme;
import dao.*;
import model.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

public class NoticesPanel extends BasePanel {
    private final NoticeDAO noticeDAO=new NoticeDAO();
    public NoticesPanel(User user){super(user);build();}

    private void build(){
        JPanel content=pageContent();
        JLabel title=AppTheme.sectionTitle("Notices"); title.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel headerRow=new JPanel(new BorderLayout()); headerRow.setOpaque(false);
        headerRow.setAlignmentX(Component.LEFT_ALIGNMENT); headerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
        headerRow.add(title,BorderLayout.WEST);
        if("admin".equals(user.getRole())){
            JButton add=AppTheme.primaryBtn("+ Add Notice");
            add.addActionListener(e->showAddDialog(content));
            headerRow.add(add,BorderLayout.EAST);
        }
        content.add(headerRow); vgap(content,8);

        JPanel noticeList=new JPanel(); noticeList.setOpaque(false); noticeList.setLayout(new BoxLayout(noticeList,BoxLayout.Y_AXIS));
        noticeList.setAlignmentX(Component.LEFT_ALIGNMENT);
        loadNotices(noticeList);
        content.add(noticeList);
        add(scrollWrap(content));
    }

    private void loadNotices(JPanel list){
        list.removeAll();
        noticeDAO.getAllNotices().forEach(n->{ list.add(noticeCard(n)); list.add(Box.createVerticalStrut(12)); });
        list.revalidate(); list.repaint();
    }

    private JPanel noticeCard(Notice n){
        JPanel card=AppTheme.card(12); card.setLayout(new BorderLayout(0,6)); card.setBorder(new EmptyBorder(18,22,18,22));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE,120)); card.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel tl=new JLabel(n.getTitle()); tl.setFont(AppTheme.F_H3); tl.setForeground(AppTheme.TEXT_DARK);
        JLabel bl=new JLabel(n.getContent()!=null&&n.getContent().length()>120?n.getContent().substring(0,117)+"…":n.getContent());
        bl.setFont(AppTheme.F_BODY); bl.setForeground(AppTheme.TEXT_MUTED);
        JLabel dl=new JLabel(n.getCreatedAt()!=null?n.getCreatedAt():""); dl.setFont(AppTheme.F_SMALL); dl.setForeground(AppTheme.TEXT_MUTED);
        JPanel top=new JPanel(new BorderLayout()); top.setOpaque(false);
        top.add(tl,BorderLayout.WEST); top.add(dl,BorderLayout.EAST);
        card.add(top,BorderLayout.NORTH); card.add(bl,BorderLayout.CENTER);
        return card;
    }

    private void showAddDialog(JPanel content){
        JTextField ft=AppTheme.textField("Notice title"); ft.setPreferredSize(new Dimension(400,38));
        JTextArea fb=new JTextArea(4,30); fb.setFont(AppTheme.F_BODY); fb.setLineWrap(true);
        Object[] inp={"Title:",ft,"Content:",new JScrollPane(fb)};
        if(JOptionPane.showConfirmDialog(this,inp,"Add Notice",JOptionPane.OK_CANCEL_OPTION)==0){
            noticeDAO.addNotice(ft.getText().trim(),fb.getText().trim(),user.getId());
            build();
        }
    }
}

