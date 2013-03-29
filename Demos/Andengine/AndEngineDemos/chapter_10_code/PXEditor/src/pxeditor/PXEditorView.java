/*
 * PXEditorView.java
 */

package pxeditor;

import java.awt.Color;
import java.io.File;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;

/**
 * The application's main frame.
 */
public class PXEditorView extends FrameView implements ChangeListener {
    
    PXEmitter mPXEmitter;
    PXSystem mPXSystem;
    JFileChooser fc;
    String[] shapeList = new String[]{"unknown", "circle", "circle_outline", "point", "rectangle", "rectangle_outline"};
    String[] textureList = new String[]{"particle_point.png", "particle_fire.png"};
    JTable alphaTable, colorTable, rotationTable, scaleTable;
    AlphaTableModel alphaTableModel;
    ColorTableModel colorTableModel;
    RotationTableModel rotationTableModel;
    ScaleTableModel scaleTableModel;
    File inFile, outFile;

    public PXEditorView(SingleFrameApplication app) {
        super(app);
        mPXEmitter = new PXEmitter("point", 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        mPXSystem = new PXSystem(mPXEmitter, 0, 0, 0, "particle_fire.png");
        alphaTableModel = new AlphaTableModel();
        colorTableModel = new ColorTableModel();
        rotationTableModel = new RotationTableModel();
        scaleTableModel = new ScaleTableModel();
        initComponents();
        initSpinners();
        //Set up renderer and editor for the Color columns.
        tblColorMods.setDefaultRenderer(Color.class,
                                 new pxeditor.ColorRenderer(true));
        tblColorMods.setDefaultEditor(Color.class,
                               new pxeditor.ColorEditor());

     }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = PXEditorApp.getApplication().getMainFrame();
            aboutBox = new PXEditorAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        PXEditorApp.getApplication().show(aboutBox);
    }
    
    public void initSpinners() {
        spnCenterX.addChangeListener(this);
        spnCenterY.addChangeListener(this);
        spnHeight.addChangeListener(this);
        spnIAccelMaxX.addChangeListener(this);
        spnIAccelMaxY.addChangeListener(this);
        spnIAccelMinX.addChangeListener(this);
        spnIAccelMinY.addChangeListener(this);
        spnIAlphaMax.addChangeListener(this);
        spnIAlphaMin.addChangeListener(this);
        spnIColorBlueMax.addChangeListener(this);
        spnIColorBlueMin.addChangeListener(this);
        spnIColorGreenMax.addChangeListener(this);
        spnIColorGreenMin.addChangeListener(this);
        spnIColorRedMax.addChangeListener(this);
        spnIColorRedMin.addChangeListener(this);
        spnIExpireMax.addChangeListener(this);
        spnIExpireMin.addChangeListener(this);
        spnIRotationMax.addChangeListener(this);
        spnIRotationMin.addChangeListener(this);
        spnIVelMaxX.addChangeListener(this);
        spnIVelMaxY.addChangeListener(this);
        spnIVelMinX.addChangeListener(this);
        spnIVelMinY.addChangeListener(this);
        spnMaxParticles.addChangeListener(this);
        spnMaxRate.addChangeListener(this);
        spnMinRate.addChangeListener(this);
        spnRadiusX.addChangeListener(this);
        spnRadiusY.addChangeListener(this);
        spnWidth.addChangeListener(this);

    }
    
    public void stateChanged(ChangeEvent e) {
        JSpinner mySpinner = (JSpinner)(e.getSource());
        if (mySpinner.getName().equals("spnCenterX")) mPXEmitter.mPECenterX = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnCenterY")) mPXEmitter.mPECenterY = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnHeight")) mPXEmitter.mPEHeight = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnWidth")) mPXEmitter.mPEWidth = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnRadiusX")) mPXEmitter.mPERadiusX = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnRadiusY")) mPXEmitter.mPERadiusY = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnMinRate")) mPXSystem.minRate = (Integer)mySpinner.getValue();
        if (mySpinner.getName().equals("spnMaxRate")) mPXSystem.maxRate = (Integer)mySpinner.getValue();
        if (mySpinner.getName().equals("spnMaxParticles")) mPXSystem.maxParticles = (Integer)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIAccelMinX")) mPXSystem.mPSIAccelMinX = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIAccelMaxX")) mPXSystem.mPSIAccelMaxX = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIAccelMinY")) mPXSystem.mPSIAccelMinY = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIAccelMaxY")) mPXSystem.mPSIAccelMaxY = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIVelMinX")) mPXSystem.mPSIVelMinX = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIVelMaxX")) mPXSystem.mPSIVelMaxX = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIVelMinY")) mPXSystem.mPSIVelMinY = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIVelMaxY")) mPXSystem.mPSIVelMaxY = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIAlphaMin")) mPXSystem.mPSIAlphaMin = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIAlphaMax")) mPXSystem.mPSIAlphaMax = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIRotationMin")) mPXSystem.mPSIRotationMin = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIRotationMax")) mPXSystem.mPSIRotationMax = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIRedMin")) mPXSystem.mPSIRedMin = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIRedMax")) mPXSystem.mPSIRedMax = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIGreenMin")) mPXSystem.mPSIGreenMin = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIGreenMax")) mPXSystem.mPSIGreenMax = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIBlueMin")) mPXSystem.mPSIBlueMin = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIBlueMax")) mPXSystem.mPSIBlueMax = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIExpireMin")) mPXSystem.mPSIExpireMin = (Float)mySpinner.getValue();
        if (mySpinner.getName().equals("spnIExpireMax")) mPXSystem.mPSIExpireMax = (Float)mySpinner.getValue();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        spnIVelMinX = new javax.swing.JSpinner();
        spnIVelMaxX = new javax.swing.JSpinner();
        jLabel13 = new javax.swing.JLabel();
        spnIVelMinY = new javax.swing.JSpinner();
        jLabel14 = new javax.swing.JLabel();
        spnIVelMaxY = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        spnIAlphaMin = new javax.swing.JSpinner();
        spnIAlphaMax = new javax.swing.JSpinner();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        spnIRotationMin = new javax.swing.JSpinner();
        jLabel22 = new javax.swing.JLabel();
        spnIRotationMax = new javax.swing.JSpinner();
        cbxGravity = new javax.swing.JCheckBox();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        spnIAccelMinX = new javax.swing.JSpinner();
        jLabel26 = new javax.swing.JLabel();
        spnIAccelMaxX = new javax.swing.JSpinner();
        jLabel27 = new javax.swing.JLabel();
        spnIAccelMinY = new javax.swing.JSpinner();
        jLabel28 = new javax.swing.JLabel();
        spnIAccelMaxY = new javax.swing.JSpinner();
        jLabel29 = new javax.swing.JLabel();
        spnIExpireMin = new javax.swing.JSpinner();
        txtIColorMin = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        spnMinRate = new javax.swing.JSpinner();
        jLabel31 = new javax.swing.JLabel();
        spnMaxRate = new javax.swing.JSpinner();
        jLabel32 = new javax.swing.JLabel();
        spnMaxParticles = new javax.swing.JSpinner();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        txtIColorMax = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        spnIColorRedMin = new javax.swing.JSpinner();
        spnIColorRedMax = new javax.swing.JSpinner();
        spnIColorGreenMin = new javax.swing.JSpinner();
        spnIColorGreenMax = new javax.swing.JSpinner();
        spnIColorBlueMin = new javax.swing.JSpinner();
        spnIColorBlueMax = new javax.swing.JSpinner();
        pnlAlphaModifiers = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        spnIExpireMax = new javax.swing.JSpinner();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        comboShape = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        spnCenterX = new javax.swing.JSpinner();
        spnCenterY = new javax.swing.JSpinner();
        spnRadiusX = new javax.swing.JSpinner();
        spnRadiusY = new javax.swing.JSpinner();
        spnWidth = new javax.swing.JSpinner();
        spnHeight = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        lblTextureName = new javax.swing.JLabel();
        lblTextureImage = new javax.swing.JLabel();
        comboTexture = new javax.swing.JComboBox();
        lblFileName = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        lblModifiers = new javax.swing.JLabel();
        btnAddAlphaMod = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlphaMods = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        btnAddColorMod = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblColorMods = new javax.swing.JTable();
        jLabel42 = new javax.swing.JLabel();
        btnAddRotationMod = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblRotationMods = new javax.swing.JTable();
        jLabel43 = new javax.swing.JLabel();
        btnAddScaleMod = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblScaleMods = new javax.swing.JTable();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        mnuItemNew = new javax.swing.JMenuItem();
        mnuItemOpen = new javax.swing.JMenuItem();
        mnuItemSave = new javax.swing.JMenuItem();
        mnuItemSaveAs = new javax.swing.JMenuItem();
        javax.swing.JMenuItem mnuItemExit = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(2000, 1000));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setAutoscrolls(true);
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(pxeditor.PXEditorApp.class).getContext().getResourceMap(PXEditorView.class);
        jLabel2.setFont(resourceMap.getFont("jLabel2.font")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(264, 13, -1, -1));

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 100, -1, -1));

        jLabel12.setText(resourceMap.getString("jLabel12.text")); // NOI18N
        jLabel12.setName("jLabel12"); // NOI18N
        jPanel1.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 100, -1, -1));

        spnIVelMinX.setName("spnIVelMinX"); // NOI18N
        jPanel1.add(spnIVelMinX, new org.netbeans.lib.awtextra.AbsoluteConstraints(158, 97, 49, -1));

        spnIVelMaxX.setName("spnIVelMaxX"); // NOI18N
        jPanel1.add(spnIVelMaxX, new org.netbeans.lib.awtextra.AbsoluteConstraints(262, 97, 49, -1));

        jLabel13.setText(resourceMap.getString("jLabel13.text")); // NOI18N
        jLabel13.setName("jLabel13"); // NOI18N
        jPanel1.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, 100, -1, -1));

        spnIVelMinY.setName("spnIVelMinY"); // NOI18N
        jPanel1.add(spnIVelMinY, new org.netbeans.lib.awtextra.AbsoluteConstraints(362, 97, 49, -1));

        jLabel14.setText(resourceMap.getString("jLabel14.text")); // NOI18N
        jLabel14.setName("jLabel14"); // NOI18N
        jPanel1.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(329, 100, -1, -1));

        spnIVelMaxY.setName("spnIVelMaxY"); // NOI18N
        jPanel1.add(spnIVelMaxY, new org.netbeans.lib.awtextra.AbsoluteConstraints(464, 97, 49, -1));

        jLabel15.setText(resourceMap.getString("jLabel15.text")); // NOI18N
        jLabel15.setName("jLabel15"); // NOI18N
        jPanel1.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(421, 100, -1, -1));

        spnIAlphaMin.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.1f)));
        spnIAlphaMin.setName("spnIAlphaMin"); // NOI18N
        jPanel1.add(spnIAlphaMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 123, 49, -1));

        spnIAlphaMax.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.1f)));
        spnIAlphaMax.setName("spnIAlphaMax"); // NOI18N
        jPanel1.add(spnIAlphaMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(259, 123, 49, -1));

        jLabel17.setText(resourceMap.getString("jLabel17.text")); // NOI18N
        jLabel17.setName("jLabel17"); // NOI18N
        jPanel1.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(111, 126, -1, -1));

        jLabel18.setText(resourceMap.getString("jLabel18.text")); // NOI18N
        jLabel18.setName("jLabel18"); // NOI18N
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 129, -1, -1));

        jLabel19.setText(resourceMap.getString("jLabel19.text")); // NOI18N
        jLabel19.setName("jLabel19"); // NOI18N
        jPanel1.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(225, 126, -1, -1));

        jLabel16.setText(resourceMap.getString("jLabel16.text")); // NOI18N
        jLabel16.setName("jLabel16"); // NOI18N
        jPanel1.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 206, -1, -1));

        jLabel20.setText(resourceMap.getString("jLabel20.text")); // NOI18N
        jLabel20.setName("jLabel20"); // NOI18N
        jPanel1.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 149, -1, -1));

        jLabel21.setText(resourceMap.getString("jLabel21.text")); // NOI18N
        jLabel21.setName("jLabel21"); // NOI18N
        jPanel1.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(111, 152, -1, -1));

        spnIRotationMin.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        spnIRotationMin.setName("spnIRotMin"); // NOI18N
        jPanel1.add(spnIRotationMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 149, 49, -1));

        jLabel22.setText(resourceMap.getString("jLabel22.text")); // NOI18N
        jLabel22.setName("jLabel22"); // NOI18N
        jPanel1.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 150, -1, -1));

        spnIRotationMax.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(360), null, Integer.valueOf(360), Integer.valueOf(1)));
        spnIRotationMax.setName("spnIRotMax"); // NOI18N
        jPanel1.add(spnIRotationMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(267, 149, 49, -1));

        cbxGravity.setText(resourceMap.getString("cbGravity.text")); // NOI18N
        cbxGravity.setName("cbGravity"); // NOI18N
        cbxGravity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxGravityActionPerformed(evt);
            }
        });
        jPanel1.add(cbxGravity, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 263, -1, -1));

        jLabel24.setText(resourceMap.getString("jLabel24.text")); // NOI18N
        jLabel24.setName("jLabel24"); // NOI18N
        jPanel1.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 74, -1, -1));

        jLabel25.setText(resourceMap.getString("jLabel25.text")); // NOI18N
        jLabel25.setName("jLabel25"); // NOI18N
        jPanel1.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(124, 74, -1, -1));

        spnIAccelMinX.setName("spnIAccelMinX"); // NOI18N
        jPanel1.add(spnIAccelMinX, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 71, 49, -1));

        jLabel26.setText(resourceMap.getString("jLabel26.text")); // NOI18N
        jLabel26.setName("jLabel26"); // NOI18N
        jPanel1.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(224, 74, -1, -1));

        spnIAccelMaxX.setName("spnIAccelMaxX"); // NOI18N
        jPanel1.add(spnIAccelMaxX, new org.netbeans.lib.awtextra.AbsoluteConstraints(259, 71, 49, -1));

        jLabel27.setText(resourceMap.getString("jLabel27.text")); // NOI18N
        jLabel27.setName("jLabel27"); // NOI18N
        jPanel1.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 74, -1, -1));

        spnIAccelMinY.setName("spnIAccelMinY"); // NOI18N
        jPanel1.add(spnIAccelMinY, new org.netbeans.lib.awtextra.AbsoluteConstraints(365, 71, 49, -1));

        jLabel28.setText(resourceMap.getString("jLabel28.text")); // NOI18N
        jLabel28.setName("jLabel28"); // NOI18N
        jPanel1.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(424, 74, -1, -1));

        spnIAccelMaxY.setName("spnIAccelMaxY"); // NOI18N
        jPanel1.add(spnIAccelMaxY, new org.netbeans.lib.awtextra.AbsoluteConstraints(461, 71, 49, -1));

        jLabel29.setText(resourceMap.getString("jLabel29.text")); // NOI18N
        jLabel29.setName("jLabel29"); // NOI18N
        jPanel1.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(12, 175, -1, -1));

        spnIExpireMin.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        spnIExpireMin.setName("spnIExpireMin"); // NOI18N
        jPanel1.add(spnIExpireMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 175, 49, -1));

        txtIColorMin.setText(resourceMap.getString("txtIColorMin.text")); // NOI18N
        txtIColorMin.setName("txtIColorMin"); // NOI18N
        jPanel1.add(txtIColorMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 206, 47, -1));

        jLabel30.setText(resourceMap.getString("jLabel30.text")); // NOI18N
        jLabel30.setName("jLabel30"); // NOI18N
        jPanel1.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 48, -1, -1));

        spnMinRate.setModel(new javax.swing.SpinnerNumberModel());
        spnMinRate.setName("spnMinRate"); // NOI18N
        jPanel1.add(spnMinRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(157, 45, 49, -1));

        jLabel31.setText(resourceMap.getString("jLabel31.text")); // NOI18N
        jLabel31.setName("jLabel31"); // NOI18N
        jPanel1.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(224, 48, -1, -1));

        spnMaxRate.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        spnMaxRate.setName("spnMaxRate"); // NOI18N
        jPanel1.add(spnMaxRate, new org.netbeans.lib.awtextra.AbsoluteConstraints(278, 45, 49, -1));

        jLabel32.setText(resourceMap.getString("jLabel32.text")); // NOI18N
        jLabel32.setName("jLabel32"); // NOI18N
        jPanel1.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(345, 48, -1, -1));

        spnMaxParticles.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(5)));
        spnMaxParticles.setName("spnMaxParticles"); // NOI18N
        jPanel1.add(spnMaxParticles, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 45, 49, -1));

        jLabel33.setText(resourceMap.getString("jLabel33.text")); // NOI18N
        jLabel33.setName("jLabel33"); // NOI18N
        jPanel1.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(111, 206, -1, -1));

        jLabel34.setText(resourceMap.getString("jLabel34.text")); // NOI18N
        jLabel34.setName("jLabel34"); // NOI18N
        jPanel1.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(107, 235, -1, -1));

        txtIColorMax.setName("txtIColorMax"); // NOI18N
        jPanel1.add(txtIColorMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(135, 232, 47, -1));

        jLabel35.setText(resourceMap.getString("jLabel35.text")); // NOI18N
        jLabel35.setName("jLabel35"); // NOI18N
        jPanel1.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(308, 209, 14, -1));

        jLabel36.setText(resourceMap.getString("jLabel36.text")); // NOI18N
        jLabel36.setName("jLabel36"); // NOI18N
        jPanel1.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 209, -1, -1));

        jLabel37.setText(resourceMap.getString("jLabel37.text")); // NOI18N
        jLabel37.setName("jLabel37"); // NOI18N
        jPanel1.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(381, 209, -1, -1));

        jLabel38.setText(resourceMap.getString("jLabel38.text")); // NOI18N
        jLabel38.setName("jLabel38"); // NOI18N
        jPanel1.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(308, 235, 14, -1));

        jLabel39.setText(resourceMap.getString("jLabel39.text")); // NOI18N
        jLabel39.setName("jLabel39"); // NOI18N
        jPanel1.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 235, -1, -1));

        jLabel40.setText(resourceMap.getString("jLabel40.text")); // NOI18N
        jLabel40.setName("jLabel40"); // NOI18N
        jPanel1.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(381, 235, -1, -1));

        spnIColorRedMin.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        spnIColorRedMin.setName("spnIColorRedMin"); // NOI18N
        jPanel1.add(spnIColorRedMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 206, 45, -1));

        spnIColorRedMax.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        spnIColorRedMax.setName("spnIColorRedMax"); // NOI18N
        jPanel1.add(spnIColorRedMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(326, 232, 45, -1));

        spnIColorGreenMin.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        spnIColorGreenMin.setName("spnIColorGreenMin"); // NOI18N
        jPanel1.add(spnIColorGreenMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(221, 206, 45, -1));

        spnIColorGreenMax.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        spnIColorGreenMax.setName("spnIColorGreenMax"); // NOI18N
        jPanel1.add(spnIColorGreenMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(221, 232, 45, -1));

        spnIColorBlueMin.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        spnIColorBlueMin.setName("spnIColorBlueMin"); // NOI18N
        jPanel1.add(spnIColorBlueMin, new org.netbeans.lib.awtextra.AbsoluteConstraints(401, 206, 45, -1));

        spnIColorBlueMax.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), Float.valueOf(0.0f), Float.valueOf(1.0f), Float.valueOf(0.01f)));
        spnIColorBlueMax.setName("spnIColorBlueMax"); // NOI18N
        jPanel1.add(spnIColorBlueMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(401, 232, 45, -1));

        pnlAlphaModifiers.setName("pnlAlphaModifiers"); // NOI18N
        jPanel1.add(pnlAlphaModifiers, new org.netbeans.lib.awtextra.AbsoluteConstraints(126, 324, -1, -1));

        jPanel3.setName("jPanel3"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 7, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 288, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 318, -1, -1));

        spnIExpireMax.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(0), Integer.valueOf(0), null, Integer.valueOf(1)));
        spnIExpireMax.setName("spnIExpireMax"); // NOI18N
        jPanel1.add(spnIExpireMax, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 180, 49, -1));

        jLabel44.setText(resourceMap.getString("jLabel44.text")); // NOI18N
        jLabel44.setName("jLabel44"); // NOI18N
        jPanel1.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, 24, -1));

        jLabel45.setText(resourceMap.getString("jLabel45.text")); // NOI18N
        jLabel45.setName("jLabel45"); // NOI18N
        jPanel1.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 180, -1, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setName("jPanel2"); // NOI18N

        jLabel3.setFont(resourceMap.getFont("jLabel3.font")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        comboShape.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Unknown", "Circle", "Circle Outline", "Point", "Rectangle", "Rectangle Outline" }));
        comboShape.setName("comboShape"); // NOI18N
        comboShape.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                comboShapeItemStateChanged(evt);
            }
        });
        comboShape.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboShapeActionPerformed(evt);
            }
        });

        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N

        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel10.setText(resourceMap.getString("jLabel10.text")); // NOI18N
        jLabel10.setName("jLabel10"); // NOI18N

        jLabel11.setText(resourceMap.getString("jLabel11.text")); // NOI18N
        jLabel11.setName("jLabel11"); // NOI18N

        spnCenterX.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(1.0f)));
        spnCenterX.setName("spnCenterX"); // NOI18N

        spnCenterY.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(1.0f)));
        spnCenterY.setName("spnCenterY"); // NOI18N

        spnRadiusX.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(1.0f)));
        spnRadiusX.setName("spnRadiusX"); // NOI18N

        spnRadiusY.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(1.0f)));
        spnRadiusY.setName("spnRadiusY"); // NOI18N

        spnWidth.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(1.0f)));
        spnWidth.setName("spnWidth"); // NOI18N

        spnHeight.setModel(new javax.swing.SpinnerNumberModel(Float.valueOf(0.0f), null, null, Float.valueOf(1.0f)));
        spnHeight.setName("spnHeight"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        lblTextureName.setText(resourceMap.getString("lblTextureName.text")); // NOI18N
        lblTextureName.setName("lblTextureName"); // NOI18N

        lblTextureImage.setBackground(resourceMap.getColor("lblTextureImage.background")); // NOI18N
        lblTextureImage.setText(resourceMap.getString("lblTextureImage.text")); // NOI18N
        lblTextureImage.setName("lblTextureImage"); // NOI18N

        comboTexture.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "particle_point.png", "particle_fire.png", " " }));
        comboTexture.setName("comboTexture"); // NOI18N
        comboTexture.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboTextureActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lblTextureName)
                                        .addComponent(jLabel4)))
                                .addGap(18, 18, 18)
                                .addComponent(lblTextureImage, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(jLabel9)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(spnRadiusY)
                                    .addComponent(spnRadiusX)
                                    .addComponent(spnCenterY)
                                    .addComponent(spnCenterX, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                    .addComponent(spnWidth)
                                    .addComponent(spnHeight, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
                                    .addComponent(comboShape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(comboTexture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(jLabel3)))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboShape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnCenterX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnCenterY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnRadiusX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnRadiusY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnWidth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addComponent(jLabel4)
                                .addGap(9, 9, 9)
                                .addComponent(lblTextureName))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(lblTextureImage, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(comboTexture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19))))
        );

        lblFileName.setFont(resourceMap.getFont("lblFileName.font")); // NOI18N
        lblFileName.setText(resourceMap.getString("lblFileName.text")); // NOI18N
        lblFileName.setName("lblFileName"); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        jPanel5.setName("jPanel5"); // NOI18N

        lblModifiers.setFont(resourceMap.getFont("lblModifiers.font")); // NOI18N
        lblModifiers.setText(resourceMap.getString("lblModifiers.text")); // NOI18N
        lblModifiers.setName("lblModifiers"); // NOI18N

        btnAddAlphaMod.setFont(resourceMap.getFont("btnAddAlphaMod.font")); // NOI18N
        btnAddAlphaMod.setText(resourceMap.getString("btnAddAlphaMod.text")); // NOI18N
        btnAddAlphaMod.setName("btnAddAlphaMod"); // NOI18N
        btnAddAlphaMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddAlphaModActionPerformed(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        tblAlphaMods.setModel(alphaTableModel);
        tblAlphaMods.setName("tblAlphaMods"); // NOI18N
        jScrollPane1.setViewportView(tblAlphaMods);

        jLabel23.setFont(resourceMap.getFont("jLabel23.font")); // NOI18N
        jLabel23.setText(resourceMap.getString("jLabel23.text")); // NOI18N
        jLabel23.setName("jLabel23"); // NOI18N

        jLabel41.setFont(resourceMap.getFont("jLabel41.font")); // NOI18N
        jLabel41.setText(resourceMap.getString("jLabel41.text")); // NOI18N
        jLabel41.setName("jLabel41"); // NOI18N

        btnAddColorMod.setFont(resourceMap.getFont("btnAddColorMod.font")); // NOI18N
        btnAddColorMod.setText(resourceMap.getString("btnAddColorMod.text")); // NOI18N
        btnAddColorMod.setName("btnAddColorMod"); // NOI18N
        btnAddColorMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddColorModActionPerformed(evt);
            }
        });

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        tblColorMods.setModel(colorTableModel);
        tblColorMods.setName("tblColorMods"); // NOI18N
        jScrollPane2.setViewportView(tblColorMods);

        jLabel42.setFont(resourceMap.getFont("jLabel42.font")); // NOI18N
        jLabel42.setText(resourceMap.getString("jLabel42.text")); // NOI18N
        jLabel42.setName("jLabel42"); // NOI18N

        btnAddRotationMod.setFont(resourceMap.getFont("btnAddRotationMod.font")); // NOI18N
        btnAddRotationMod.setText(resourceMap.getString("btnAddRotationMod.text")); // NOI18N
        btnAddRotationMod.setName("btnAddRotationMod"); // NOI18N
        btnAddRotationMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRotationModActionPerformed(evt);
            }
        });

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        tblRotationMods.setModel(rotationTableModel);
        tblRotationMods.setName("tblRotationMods"); // NOI18N
        jScrollPane3.setViewportView(tblRotationMods);

        jLabel43.setFont(resourceMap.getFont("jLabel43.font")); // NOI18N
        jLabel43.setText(resourceMap.getString("jLabel43.text")); // NOI18N
        jLabel43.setName("jLabel43"); // NOI18N

        btnAddScaleMod.setFont(resourceMap.getFont("btnAddScaleMod.font")); // NOI18N
        btnAddScaleMod.setText(resourceMap.getString("btnAddScaleMod.text")); // NOI18N
        btnAddScaleMod.setName("btnAddScaleMod"); // NOI18N
        btnAddScaleMod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddScaleModActionPerformed(evt);
            }
        });

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        tblScaleMods.setModel(scaleTableModel);
        tblScaleMods.setName("tblScaleMods"); // NOI18N
        jScrollPane4.setViewportView(tblScaleMods);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(301, 301, 301)
                        .addComponent(lblModifiers))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnAddRotationMod)
                                    .addComponent(jLabel42))
                                .addGap(50, 50, 50)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(278, 278, 278))
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel23)
                                        .addComponent(btnAddAlphaMod))
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel41)
                                        .addComponent(btnAddColorMod)))
                                .addGap(59, 59, 59)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(278, 278, 278))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(76, 76, 76)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel43)
                            .addComponent(btnAddScaleMod))
                        .addGap(59, 59, 59)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 424, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(lblModifiers)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddAlphaMod))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel41)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddColorMod))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddRotationMod))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAddScaleMod))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1860, 1860, 1860)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblFileName, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 562, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFileName)
                .addGap(17, 17, 17)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, 0, 0, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1044, 1044, 1044))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N
        fileMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuActionPerformed(evt);
            }
        });

        mnuItemNew.setText(resourceMap.getString("mnuItemNew.text")); // NOI18N
        mnuItemNew.setName("mnuItemNew"); // NOI18N
        fileMenu.add(mnuItemNew);

        mnuItemOpen.setText(resourceMap.getString("mnuItemOpen.text")); // NOI18N
        mnuItemOpen.setName("mnuItemOpen"); // NOI18N
        mnuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemOpenActionPerformed(evt);
            }
        });
        fileMenu.add(mnuItemOpen);

        mnuItemSave.setText(resourceMap.getString("mnuItemSave.text")); // NOI18N
        mnuItemSave.setName("mnuItemSave"); // NOI18N
        mnuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemSaveActionPerformed(evt);
            }
        });
        fileMenu.add(mnuItemSave);

        mnuItemSaveAs.setText(resourceMap.getString("mnuItemSaveAs.text")); // NOI18N
        mnuItemSaveAs.setName("mnuItemSaveAs"); // NOI18N
        fileMenu.add(mnuItemSaveAs);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(pxeditor.PXEditorApp.class).getContext().getActionMap(PXEditorView.class, this);
        mnuItemExit.setAction(actionMap.get("quit")); // NOI18N
        mnuItemExit.setName("mnuItemExit"); // NOI18N
        fileMenu.add(mnuItemExit);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void comboShapeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_comboShapeItemStateChanged

    }//GEN-LAST:event_comboShapeItemStateChanged

    private void comboShapeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboShapeActionPerformed
        mPXSystem.mPXEmitter.mPEShape = shapeList[comboShape.getSelectedIndex()];
        newShape(comboShape.getSelectedIndex());
    }//GEN-LAST:event_comboShapeActionPerformed

    private void fileMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuActionPerformed

    }//GEN-LAST:event_fileMenuActionPerformed

    private void cbxGravityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxGravityActionPerformed
        if (cbxGravity.isSelected()) {
            mPXSystem.mGravity = true;
        } else {
            mPXSystem.mGravity = false;
        }
    }//GEN-LAST:event_cbxGravityActionPerformed

    private void mnuItemOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemOpenActionPerformed
       //Handle open menu action.
        JFrame mainFrame = PXEditorApp.getApplication().getMainFrame();
        fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(mainFrame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            inFile = fc.getSelectedFile();
            String fileName = inFile.getAbsolutePath().replace(System.getProperty("file.separator"), "/");
            try {
               PXLoader pxl = new PXLoader();
               mPXSystem = pxl.load(fileName);
               lblFileName.setText(fileName);
            } catch ( PXLoadException e) {
                 JOptionPane.showMessageDialog(
                 mainFrame,
                 e.toString(),
                 "IOException",
                 JOptionPane.OK_OPTION);                   
            }

            // refresh the form data
            updateForm();
        }
    }//GEN-LAST:event_mnuItemOpenActionPerformed

    private void comboTextureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboTextureActionPerformed
        mPXSystem.mTextureName = textureList[comboTexture.getSelectedIndex()];
    }//GEN-LAST:event_comboTextureActionPerformed

    private void mnuItemSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemSaveActionPerformed
       //Handle open menu action.
        JFrame mainFrame = PXEditorApp.getApplication().getMainFrame();
        fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(mainFrame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            outFile = fc.getSelectedFile();
            try {
                PXSaver mPXSaver = new PXSaver();
                mPXSaver.save(mPXSystem, outFile);
            }
            catch (Exception e) {
                 JOptionPane.showMessageDialog(
                 mainFrame,
                 e.toString(),
                 "PXSaveException",
                 JOptionPane.OK_OPTION);                   
            }
        }
    }//GEN-LAST:event_mnuItemSaveActionPerformed

    private void btnAddAlphaModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddAlphaModActionPerformed
        mPXSystem.mAlphaMods.add(new PXAlphaModifier(0.0f, 0.0f, 0.0f, 0.0f));
        alphaTableModel.fireTableDataChanged();
    }//GEN-LAST:event_btnAddAlphaModActionPerformed

    private void btnAddColorModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddColorModActionPerformed
        mPXSystem.mColorMods.add(new PXColorModifier(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        colorTableModel.fireTableDataChanged();
    }//GEN-LAST:event_btnAddColorModActionPerformed

    private void btnAddRotationModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRotationModActionPerformed
        mPXSystem.mRotationMods.add(new PXRotationModifier(0.0f, 0.0f, 0.0f, 0.0f));
        rotationTableModel.fireTableDataChanged();
    }//GEN-LAST:event_btnAddRotationModActionPerformed

    private void btnAddScaleModActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddScaleModActionPerformed
        mPXSystem.mScaleMods.add(new PXScaleModifier(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f));
        scaleTableModel.fireTableDataChanged();
    }//GEN-LAST:event_btnAddScaleModActionPerformed

    private void updateForm(){
        int shapeIndex, textureIndex;
        
        spnCenterX.setValue(mPXSystem.mPXEmitter.mPECenterX);
        spnCenterY.setValue(mPXSystem.mPXEmitter.mPECenterY);
        spnRadiusX.setValue(mPXSystem.mPXEmitter.mPERadiusX);
        spnRadiusY.setValue(mPXSystem.mPXEmitter.mPERadiusY);
        spnWidth.setValue(mPXSystem.mPXEmitter.mPEWidth);
        spnHeight.setValue(mPXSystem.mPXEmitter.mPEHeight);
        shapeIndex = -1;
        for (int i=0; i<shapeList.length; i++){
            if (shapeList[i].equals(mPXSystem.mPXEmitter.mPEShape)) {
                shapeIndex = i;
                break;
            }
        }
        if (shapeIndex < 0) {
            shapeIndex = 0; // mark it as unknown
        }
        textureIndex = -1;
        for (int i=0; i<textureList.length; i++){
            if (textureList[i].equals(mPXSystem.mTextureName)) {
                textureIndex = i;
                break;
            }
        }
        comboShape.setSelectedIndex(shapeIndex);
        comboTexture.setSelectedIndex(textureIndex);
        newShape(shapeIndex);
        spnMinRate.setValue(mPXSystem.minRate);
        spnMaxRate.setValue(mPXSystem.maxRate);
        spnMaxParticles.setValue(mPXSystem.maxParticles);
        lblTextureName.setText(mPXSystem.mTextureName);
        String textureFileName = lblFileName.getText();
        String textureFilePath = inFile.getParent();
        lblTextureImage.setIcon(new javax.swing.ImageIcon(textureFilePath + mPXSystem.mTextureName));
        
        spnIAccelMinX.setValue(mPXSystem.mPSIAccelMinX);
        spnIAccelMaxX.setValue(mPXSystem.mPSIAccelMaxX);
        spnIAccelMinY.setValue(mPXSystem.mPSIAccelMinY);
        spnIAccelMaxY.setValue(mPXSystem.mPSIAccelMaxY);
        spnIVelMinX.setValue(mPXSystem.mPSIVelMinX);
        spnIVelMaxX.setValue(mPXSystem.mPSIVelMaxX);
        spnIVelMinY.setValue(mPXSystem.mPSIVelMinY);
        spnIVelMaxY.setValue(mPXSystem.mPSIVelMaxY);
        spnIAlphaMin.setValue(mPXSystem.mPSIAlphaMin);
        spnIAlphaMax.setValue(mPXSystem.mPSIAlphaMax);
        spnIRotationMin.setValue(mPXSystem.mPSIRotationMin);
        spnIRotationMax.setValue(mPXSystem.mPSIRotationMax);

        spnIColorRedMin.setValue(mPXSystem.mPSIRedMin);
        spnIColorRedMax.setValue(mPXSystem.mPSIRedMax);
        spnIColorGreenMin.setValue(mPXSystem.mPSIGreenMin);
        spnIColorGreenMax.setValue(mPXSystem.mPSIGreenMax);
        spnIColorBlueMin.setValue(mPXSystem.mPSIBlueMin);
        spnIColorBlueMax.setValue(mPXSystem.mPSIBlueMax);
        txtIColorMin.setBackground( new Color( (int)mPXSystem.mPSIRedMin*255,
                (int)mPXSystem.mPSIGreenMin*255, 
                (int)mPXSystem.mPSIBlueMin*255));
        txtIColorMax.setBackground( new Color( (int)mPXSystem.mPSIRedMax*255,
                (int)mPXSystem.mPSIGreenMax*255, 
                (int)mPXSystem.mPSIBlueMax*255));
        spnIExpireMin.setValue(mPXSystem.mPSIExpireMin);
        spnIExpireMax.setValue(mPXSystem.mPSIExpireMax);
        cbxGravity.setSelected(mPXSystem.mGravity);
        
        alphaTableModel.fireTableDataChanged();
        colorTableModel.fireTableDataChanged();
        rotationTableModel.fireTableDataChanged();
        scaleTableModel.fireTableDataChanged();
        
    }
    
    private void newShape(int pShapeIndex){
        // Adjust enablement based on new shape index
                switch (pShapeIndex) {
            case 1:
            case 2:
                //disable width and height for circle shapes
                spnRadiusX.setEnabled(true);
                spnRadiusY.setEnabled(true);
                spnWidth.setEnabled(false);
                spnHeight.setEnabled(false);
                break;
            case 3:
                //disable radii, width and height for point shape
                spnRadiusX.setEnabled(false);
                spnRadiusY.setEnabled(false);
                spnWidth.setEnabled(false);
                spnHeight.setEnabled(false);
                break;
            case 4:
            case 5:
                //disable radii for rectangle shapes
                spnRadiusX.setEnabled(false);
                spnRadiusY.setEnabled(false);
                spnWidth.setEnabled(true);
                spnHeight.setEnabled(true);
                break;
        }

    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddAlphaMod;
    private javax.swing.JButton btnAddColorMod;
    private javax.swing.JButton btnAddRotationMod;
    private javax.swing.JButton btnAddScaleMod;
    private javax.swing.JCheckBox cbxGravity;
    private javax.swing.JComboBox comboShape;
    private javax.swing.JComboBox comboTexture;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblFileName;
    private javax.swing.JLabel lblModifiers;
    private javax.swing.JLabel lblTextureImage;
    private javax.swing.JLabel lblTextureName;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem mnuItemNew;
    private javax.swing.JMenuItem mnuItemOpen;
    private javax.swing.JMenuItem mnuItemSave;
    private javax.swing.JMenuItem mnuItemSaveAs;
    private javax.swing.JPanel pnlAlphaModifiers;
    private javax.swing.JSpinner spnCenterX;
    private javax.swing.JSpinner spnCenterY;
    private javax.swing.JSpinner spnHeight;
    private javax.swing.JSpinner spnIAccelMaxX;
    private javax.swing.JSpinner spnIAccelMaxY;
    private javax.swing.JSpinner spnIAccelMinX;
    private javax.swing.JSpinner spnIAccelMinY;
    private javax.swing.JSpinner spnIAlphaMax;
    private javax.swing.JSpinner spnIAlphaMin;
    private javax.swing.JSpinner spnIColorBlueMax;
    private javax.swing.JSpinner spnIColorBlueMin;
    private javax.swing.JSpinner spnIColorGreenMax;
    private javax.swing.JSpinner spnIColorGreenMin;
    private javax.swing.JSpinner spnIColorRedMax;
    private javax.swing.JSpinner spnIColorRedMin;
    private javax.swing.JSpinner spnIExpireMax;
    private javax.swing.JSpinner spnIExpireMin;
    private javax.swing.JSpinner spnIRotationMax;
    private javax.swing.JSpinner spnIRotationMin;
    private javax.swing.JSpinner spnIVelMaxX;
    private javax.swing.JSpinner spnIVelMaxY;
    private javax.swing.JSpinner spnIVelMinX;
    private javax.swing.JSpinner spnIVelMinY;
    private javax.swing.JSpinner spnMaxParticles;
    private javax.swing.JSpinner spnMaxRate;
    private javax.swing.JSpinner spnMinRate;
    private javax.swing.JSpinner spnRadiusX;
    private javax.swing.JSpinner spnRadiusY;
    private javax.swing.JSpinner spnWidth;
    private javax.swing.JTable tblAlphaMods;
    private javax.swing.JTable tblColorMods;
    private javax.swing.JTable tblRotationMods;
    private javax.swing.JTable tblScaleMods;
    private javax.swing.JTextField txtIColorMax;
    private javax.swing.JTextField txtIColorMin;
    // End of variables declaration//GEN-END:variables

    private JDialog aboutBox;
    
    class AlphaTableModel extends AbstractTableModel {
        private String[] columnNames = {"Del",
                                        "From Alpha",
                                        "To Alpha",
                                        "From Time",
                                        "To Time"};
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return mPXSystem.mAlphaMods.size();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            switch (col) {
                case 0:
                    return false;
                case 1:
                    return mPXSystem.mAlphaMods.get(row).mFromAlpha;
                case 2:
                    return mPXSystem.mAlphaMods.get(row).mToAlpha;
                case 3:
                    return mPXSystem.mAlphaMods.get(row).mFromTime;
                case 4:
                    return mPXSystem.mAlphaMods.get(row).mToTime;
            }
            return 0.0f;
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        @Override
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return true;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        @Override
        public void setValueAt(Object value, int row, int col) {
            switch (col) {
                case 0:
                    mPXSystem.mAlphaMods.remove(row);
                    alphaTableModel.fireTableRowsDeleted(row, row);
                    break;
                case 1:
                    mPXSystem.mAlphaMods.get(row).mFromAlpha = (Float)value;
                    break;
                case 2:
                    mPXSystem.mAlphaMods.get(row).mToAlpha = (Float)value;
                    break;
                case 3:
                    mPXSystem.mAlphaMods.get(row).mFromTime = (Float)value;
                    break;
                case 4:
                    mPXSystem.mAlphaMods.get(row).mToTime = (Float)value;
                    break;
            }
            fireTableCellUpdated(row, col);
        }
    }
    class ColorTableModel extends AbstractTableModel {
        private String[] columnNames = {"Del",
                                        "From",
                                        "To",
                                        "FromTime",
                                        "ToTime"};
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return mPXSystem.mColorMods.size();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            switch (col) {
                case 0:
                    return false;
                case 1: 
                    return new Color( mPXSystem.mColorMods.get(row).mFromRed,
                           mPXSystem.mColorMods.get(row).mFromGreen,
                           mPXSystem.mColorMods.get(row).mFromBlue );
                case 2: 
                    return new Color( mPXSystem.mColorMods.get(row).mToRed,
                           mPXSystem.mColorMods.get(row).mToGreen,
                           mPXSystem.mColorMods.get(row).mToBlue );
                case 3:
                    return mPXSystem.mColorMods.get(row).mFromTime;
                case 4:
                    return mPXSystem.mColorMods.get(row).mToTime;
            }
            return 0.0f;
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        @Override
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return true;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        @Override
        public void setValueAt(Object value, int row, int col) {
            switch (col) {
                case 0:
                    mPXSystem.mColorMods.remove(row);
                    colorTableModel.fireTableRowsDeleted(row, row);
                    break;
                case 1:
                    int fromRedi = ((Color)value).getRed();
                    mPXSystem.mColorMods.get(row).mFromRed = fromRedi/ 255.0f;
                    int fromGreeni = ((Color)value).getGreen();
                    mPXSystem.mColorMods.get(row).mFromGreen = fromGreeni/ 255.0f;
                    int fromBluei = ((Color)value).getBlue();
                    mPXSystem.mColorMods.get(row).mFromBlue = fromBluei/ 255.0f;
                    mPXSystem.mColorMods.get(row).mFromRed = fromRedi / 255.0f;
                    mPXSystem.mColorMods.get(row).mFromGreen = fromGreeni / 255.0f;
                    mPXSystem.mColorMods.get(row).mFromBlue = fromBluei / 255.0f;
                    break;
                case 2:
                    int toRedi = ((Color)value).getRed();
                    mPXSystem.mColorMods.get(row).mToRed = toRedi/ 255.0f;
                    int toGreeni = ((Color)value).getGreen();
                    mPXSystem.mColorMods.get(row).mToGreen = toGreeni/ 255.0f;
                    int toBluei = ((Color)value).getBlue();
                    mPXSystem.mColorMods.get(row).mToBlue = toBluei/ 255.0f;
                    mPXSystem.mColorMods.get(row).mToRed = toRedi / 255.0f;
                    mPXSystem.mColorMods.get(row).mToGreen = toGreeni / 255.0f;
                    mPXSystem.mColorMods.get(row).mToBlue = toBluei / 255.0f;
                    break;
                case 3:
                    mPXSystem.mColorMods.get(row).mFromTime = (Float)value;
                    break;
                case 4:
                    mPXSystem.mColorMods.get(row).mToTime = (Float)value;
                    break;
            }
            fireTableCellUpdated(row, col);
       }
    }
    
        class RotationTableModel extends AbstractTableModel {
        private String[] columnNames = {"Del",
                                        "From Rot",
                                        "To Rot",
                                        "From Time",
                                        "To Time"};
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return mPXSystem.mRotationMods.size();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            switch (col) {
                case 0:
                    return false;
                case 1:
                    return mPXSystem.mRotationMods.get(row).mFromRotation;
                case 2:
                    return mPXSystem.mRotationMods.get(row).mToRotation;
                case 3:
                    return mPXSystem.mRotationMods.get(row).mFromTime;
                case 4:
                    return mPXSystem.mRotationMods.get(row).mToTime;
            }
            return 0.0f;
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        @Override
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return true;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        @Override
        public void setValueAt(Object value, int row, int col) {
            switch (col) {
                case 0:
                    mPXSystem.mRotationMods.remove(row);
                    rotationTableModel.fireTableRowsDeleted(row, row);
                    break;
                case 1:
                    mPXSystem.mRotationMods.get(row).mFromRotation = (Float)value;
                    break;
                case 2:
                    mPXSystem.mRotationMods.get(row).mToRotation = (Float)value;
                    break;
                case 3:
                    mPXSystem.mRotationMods.get(row).mFromTime = (Float)value;
                    break;
                case 4:
                    mPXSystem.mRotationMods.get(row).mToTime = (Float)value;
                    break;
            }
            fireTableCellUpdated(row, col);
        }
    }

    class ScaleTableModel extends AbstractTableModel {
        private String[] columnNames = {"Del",
                                        "From Scale X",
                                        "To Scale X",
                                        "From Scale Y",
                                        "To Scale Y",
                                        "From Time",
                                        "To Time"};
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return mPXSystem.mScaleMods.size();
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
            switch (col) {
                case 0:
                    return false;
                case 1:
                    return mPXSystem.mScaleMods.get(row).mFromScaleX;
                case 2:
                    return mPXSystem.mScaleMods.get(row).mToScaleX;
                case 3:
                    return mPXSystem.mScaleMods.get(row).mFromScaleY;
                case 4:
                    return mPXSystem.mScaleMods.get(row).mToScaleY;
                case 5:
                    return mPXSystem.mScaleMods.get(row).mFromTime;
                case 6:
                    return mPXSystem.mScaleMods.get(row).mToTime;
            }
            return 0.0f;
        }

        /*
         * JTable uses this method to determine the default renderer/
         * editor for each cell.  If we didn't implement this method,
         * then the last column would contain text ("true"/"false"),
         * rather than a check box.
         */
        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /*
         * Don't need to implement this method unless your table's
         * editable.
         */
        @Override
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            return true;
        }

        /*
         * Don't need to implement this method unless your table's
         * data can change.
         */
        @Override
        public void setValueAt(Object value, int row, int col) {
            switch (col) {
                case 0:
                    mPXSystem.mScaleMods.remove(row);
                    scaleTableModel.fireTableRowsDeleted(row, row);
                    break;
                case 1:
                    mPXSystem.mScaleMods.get(row).mFromScaleX = (Float)value;
                    break;
                case 2:
                    mPXSystem.mScaleMods.get(row).mToScaleX = (Float)value;
                    break;
                case 3:
                    mPXSystem.mScaleMods.get(row).mFromScaleX = (Float)value;
                    break;
                case 4:
                    mPXSystem.mScaleMods.get(row).mToScaleX = (Float)value;
                    break;
                case 5:
                    mPXSystem.mScaleMods.get(row).mFromTime = (Float)value;
                    break;
                case 6:
                    mPXSystem.mScaleMods.get(row).mToTime = (Float)value;
                    break;
            }
            fireTableCellUpdated(row, col);
        }
    }

}
