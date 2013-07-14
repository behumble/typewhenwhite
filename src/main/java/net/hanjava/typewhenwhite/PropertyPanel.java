package net.hanjava.typewhenwhite;

import com.android.chimpchat.core.IChimpDevice;

import javax.swing.*;

class PropertyPanel extends JScrollPane {
    private InputOntoDroid main;
    private JTextArea textArea;

    PropertyPanel(InputOntoDroid main) {
        this.main = main;
        textArea = new JTextArea();
        textArea.setColumns(30);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        setViewportView(textArea);
    }

    final void setText(String text) {
        textArea.setText(text);
    }

    void extractFrom(IChimpDevice device) {
        String result = device.shell("getprop");
        setText(result);
    }
}