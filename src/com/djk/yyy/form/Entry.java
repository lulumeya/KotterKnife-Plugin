package com.djk.yyy.form;




import com.djk.yyy.iface.OnCheckBoxStateChangedListener;
import com.djk.yyy.model.Element;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

public class Entry extends JPanel {

    private EntryList mParent;
    private Element mElement;
    private ArrayList<String> mGeneratedIDs;
    private OnCheckBoxStateChangedListener mListener;
    private OnCheckBoxStateChangedListener mOptionalListener;
    private JCheckBox mCheck;
    private JLabel mType;
    private JLabel mID;
    private JCheckBox mIsOptional;
    private JTextField mName;
    private Color mNameDefaultColor;
    private Color mNameErrorColor = new Color(0x880000);

    public JCheckBox getCheck() {
        return mCheck;
    }

    public JCheckBox getmIsOptional() {
        return mIsOptional;
    }

    public void setListener(final OnCheckBoxStateChangedListener onStateChangedListener) {
        this.mListener = onStateChangedListener;
    }

    public void setOptionalListener(final OnCheckBoxStateChangedListener onStateChangedListener) {
        this.mOptionalListener = onStateChangedListener;
    }

    public Entry(EntryList parent, Element element, ArrayList<String> ids) {
        mElement = element;
        mParent = parent;
        mGeneratedIDs = ids;

        mCheck = new JCheckBox();
        mCheck.setPreferredSize(new Dimension(40, 26));
        if (!mGeneratedIDs.contains(element.getFullID())) {
            mCheck.setSelected(mElement.used);
        } else {
            mCheck.setSelected(false);
        }
        mCheck.addChangeListener(new CheckListener());

        mIsOptional = new JCheckBox();
        mIsOptional.setPreferredSize(new Dimension(100, 26));
        mIsOptional.addChangeListener(new CheckListener());

        mType = new JLabel(mElement.name);
        mType.setPreferredSize(new Dimension(100, 26));

        mID = new JLabel(mElement.id);
        mID.setPreferredSize(new Dimension(100, 26));

        mName = new JTextField(mElement.fieldName, 10);
        mNameDefaultColor = mName.getBackground();
        mName.setPreferredSize(new Dimension(100, 26));
        mName.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                // empty
            }

            @Override
            public void focusLost(FocusEvent e) {
                syncElement();
            }
        });

        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setMaximumSize(new Dimension(Short.MAX_VALUE, 54));
        add(mCheck);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mType);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mID);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mIsOptional);
        add(Box.createRigidArea(new Dimension(10, 0)));
        add(mName);
        add(Box.createHorizontalGlue());

        checkState();
    }

    public Element syncElement() {
        mElement.used = mCheck.isSelected();
        mElement.isOptional = mIsOptional.isSelected();
        mElement.fieldName = mName.getText();

        if (mElement.checkValidity()) {
            mName.setBackground(mNameDefaultColor);
        } else {
            mName.setBackground(mNameErrorColor);
        }

        return mElement;
    }

    private void checkState() {
        if (mCheck.isSelected()) {
            mType.setEnabled(true);
            mID.setEnabled(true);
            mName.setEnabled(true);
        } else {
            mType.setEnabled(false);
            mID.setEnabled(false);
            mName.setEnabled(false);
        }

        if (mListener != null) {
            mListener.changeState(mCheck.isSelected());
        }

        if (mOptionalListener != null){
            mOptionalListener.changeState(mCheck.isSelected());
        }
    }

    // classes

    public class CheckListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent event) {
            checkState();
        }
    }

}
