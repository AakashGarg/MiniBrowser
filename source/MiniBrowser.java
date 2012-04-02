/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 *
 * @author Aakash
 */
public class MiniBrowser extends JFrame implements HyperlinkListener, ActionListener {

    private JTextField urlField;
    private JEditorPane htmlpane;
    private String initialURL;
    public static final String NIMBUS_LAF_NAME = "Nimbus";
    private static JFrame frame;
    private static final List<SupportedLaF> supportedLaFs =
            new ArrayList<SupportedLaF>();
    private static SupportedLaF nimbusLaF;

    private static class SupportedLaF {

        private final String name;
        private final LookAndFeel laf;

        SupportedLaF(String name, LookAndFeel laf) {
            this.name = name;
            this.laf = laf;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) {

        /*        final String NIMBUS_LAF_NAME = "Nimbus";
         */        
        UIManager.LookAndFeelInfo[] installedLafs = UIManager.getInstalledLookAndFeels();

        for (UIManager.LookAndFeelInfo lafInfo : installedLafs) {
            try {
                Class<?> lnfClass = Class.forName(lafInfo.getClassName());
                LookAndFeel laf = (LookAndFeel) (lnfClass.newInstance());
                if (laf.isSupportedLookAndFeel()) {
                    String name = lafInfo.getName();
                    SupportedLaF supportedLaF = new SupportedLaF(name, laf);
                    supportedLaFs.add(supportedLaF);
                    if (NIMBUS_LAF_NAME.equals(name)) {
                        nimbusLaF = supportedLaF;
                    }
                }
            } catch (Exception ignored) {
                // If ANYTHING weird happens, don't add this L&F
            }
        }


        new MiniBrowser("http://google.com");
    }

    public MiniBrowser(String initialURL) {
        super("Simple Swing Browser");
        this.initialURL = initialURL;
        // addWindowListener(new Listener());
        //  UIManager.setLookAndFeel("Nimbus");

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.LIGHT_GRAY);

        JLabel urlLabel = new JLabel("URL: ");
        urlField = new JTextField(30);
        urlField.setText(initialURL);
        urlField.addActionListener(this);

        topPanel.add(urlLabel);
        topPanel.add(urlField);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        try {
            htmlpane = new JEditorPane(initialURL);
            htmlpane.setEditable(false);
            htmlpane.addHyperlinkListener(this);
            JScrollPane scrollPane = new JScrollPane(htmlpane);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
        } catch (Exception ioException) {
            warnUser("Cant build HTML pane for " + initialURL + ":" + ioException);
        }

        Toolkit t = getToolkit();
        Dimension screenSize = t.getScreenSize();
        int width = screenSize.width * 8 / 10;
        int height = screenSize.height * 8 / 10;
        setBounds(width / 8, height / 8, width, height);
        setVisible(true);

    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                htmlpane.setPage(e.getURL());
                urlField.setText(e.getURL().toExternalForm());
            } catch (Exception ioException) {
                warnUser("can't follow link to " + e.getURL().toExternalForm() + ":" + ioException);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String url;
        if (e.getSource() == urlField) {
            url = urlField.getText();
        } else {
            url = initialURL;
        }

        try {
            htmlpane.setPage(new URL(url));
            urlField.setText(url);
        } catch (IOException ioException) {
            warnUser("can't follow link to " + url + ":" + ioException);
        }
    }

    private void warnUser(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
