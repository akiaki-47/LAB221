/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package micronotepad;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Admin
 */
public class NotepadFrame extends javax.swing.JFrame {

    String title = "Untitled - My Text Editor", pathFile = "", curDir = System.getProperty("user.home"); //curDir để lưu trữ thư mục mở lần cuối
    Vector v = new Vector();
    //tạo vector font
    boolean isSave = true;
    //biến cờ để báo khi nào có thể save
    UndoManager um = new UndoManager();
    //Clipboard clb = Toolkit.getDefaultToolkit().getSystemClipboard();

    private void newFile() {
        title = "Untitled - My Text Editor";
        setTitle(title);
        //khi file mới chưa có title
        txtContent.setText("");
        pathFile = "";
        isSave = true;
        um.discardAllEdits();
        //discard để không undo được nữa, xóa hết các edit stack được lưu
        disableMenuItems();
    }

    private boolean saveAs(String conttent) {
        try {
            JFileChooser jf = new JFileChooser(curDir);
            jf.setFileFilter(new FileNameExtensionFilter("Text Document", "TXT"));
            int opt = jf.showSaveDialog(this);
            curDir = jf.getCurrentDirectory().getAbsolutePath();
            if (opt == JFileChooser.APPROVE_OPTION) {
                File f = jf.getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".txt")) {
                    f = new File(f.getPath() + ".txt");
                }
                pathFile = f.getPath(); // lấy path của file
                if (f.exists()) {
                    int choice = JOptionPane.showConfirmDialog(this, f.getName() + " already exists. \nDo you want to replace it?", "Confirm Save As", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        Files.write(f.toPath(), conttent.getBytes());
                        isSave = true; //file đã lưu nên set = true
                        title = f.getName().replaceAll("\\.txt$", "") + " - My Text Editor";
                        setTitle(title);
                    } else {
                        if (!saveAs(conttent)) {
                            return false;                        }
                    }
                } else {
                    Files.write(f.toPath(), conttent.getBytes());
                    isSave = true;
                    title = f.getName().replaceAll("\\.txt$", "") + " - My text editor";
                    setTitle(title);
                }
            } else {
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(NotepadFrame.class.getName()).log(Level.SEVERE, null, ex);
            return false; //neu co exception thi return la chua save as
        }
        return true;
        //nếu lưu thành công thì return true
    }

    private boolean saveFile(String content) {
        if (pathFile.isEmpty()) {
            //nếu file chưa từng đc lưu
            if (saveAs(content)) {
                return true;
                //lưu file
            }
        } else {
            try {
                Files.write(new File(pathFile).toPath(), content.getBytes());
                //lưu file đã có tên
                isSave = true;
                return true;
            } catch (IOException ex) {
                Logger.getLogger(NotepadFrame.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;
    }

    private void openFile() {
        try {
            JFileChooser jf = new JFileChooser(curDir);
            jf.setFileFilter(new FileNameExtensionFilter("Text Document", "TXT"));
            int opt = jf.showOpenDialog(this);
            curDir = jf.getSelectedFile().getAbsolutePath();
            if (opt == JFileChooser.APPROVE_OPTION) {
                File f = jf.getSelectedFile();
                pathFile = f.getPath();
                try {
                    txtContent.read(Files.newBufferedReader(f.toPath(), StandardCharsets.UTF_8), null);
                } catch (Exception e) {
                    txtContent.read(Files.newBufferedReader(f.toPath(), StandardCharsets.UTF_16), null);
                }
                initListener();
                disableMenuItems();
            }
        } catch (Exception e) {
        }
    }

    public void disableMenuItems() {
        if (um.canUndo()) {
            mUndo.setEnabled(true);
        } else {
            mUndo.setEnabled(false);
        }
        if (um.canRedo()) {
            mRedo.setEnabled(true);
        } else {
            mRedo.setEnabled(false);
        }
        if (txtContent.getText().isEmpty()) {
            mFind.setEnabled(false);
        } else {
            mFind.setEnabled(true);
        }
        if (um.canUndo()) {
            isSave = false;
        } else {
            isSave = true;
        }
    }

    private void loadFonts() {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontName = env.getAvailableFontFamilyNames();
        listFontName = new JList(fontName);
        listFontName.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                String font = listFontName.getSelectedValue();
                Font fontText = new Font(font, Font.PLAIN, 15);
                txtFontText.setFont(fontText);
            }
        });
        scrollName.getViewport().add(listFontName);

        listFontStyle.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    int style = listFontStyle.getSelectedIndex();
                    Font fontStyle = txtFontText.getFont().deriveFont(style);
                    txtFontText.setFont(fontStyle);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        });

        listFontSize.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    Float size = Float.parseFloat(listFontSize.getSelectedValue());
                    Font fontSize = txtFontText.getFont().deriveFont(size);
                    txtFontText.setFont(fontSize);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void initFindDialog() {
        findDialog.setAlwaysOnTop(true);
        findDialog.setTitle("Find");
        findDialog.setSize(450, 200);
        findDialog.setLocationRelativeTo(this);
    }

    private void initReplaceDialog() {
        replaceDialog.setAlwaysOnTop(true);
        replaceDialog.setTitle("Replace");
        replaceDialog.setSize(500, 250);
        replaceDialog.setLocationRelativeTo(this);
    }

    private void initChangeFontDialog() {
        changeFontDialog.setAlwaysOnTop(true);
        changeFontDialog.setTitle("Change Font");
        changeFontDialog.setSize(460, 338);
        changeFontDialog.setLocationRelativeTo(this);
    }

    private void initListener() {
        txtContent.getDocument().addUndoableEditListener((UndoableEditEvent e) -> {
            um.addEdit(e.getEdit());
            //khi có sự kiện có thể undo thì thêm edit vào stack undo
            disableMenuItems();
        });

        txtContent.getDocument().addDocumentListener(new DocumentListener() {
            //nếu textarea phát sinh sự kiện thì phát sinh
            @Override
            public void insertUpdate(DocumentEvent e) {
                disableMenuItems();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                disableMenuItems();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                disableMenuItems();
            }
        });
    }

    private void findStr() {
        String find = txtFind.getText();
        int index = txtContent.getText().indexOf(find, txtContent.getCaretPosition());
        if (index < 0) {
            findDialog.setAlwaysOnTop(false);
            JOptionPane.showMessageDialog(this, "Cannot find \"" + find + "\"", "My Text Editor", JOptionPane.INFORMATION_MESSAGE);
            findDialog.setAlwaysOnTop(true);
        } else {
            txtContent.select(index, index + find.length());
        }
    }

    private void repFindStr() {
        String find = txtReFind.getText();
        int index = txtContent.getText().indexOf(find, txtContent.getCaretPosition());
        if (index < 0) {
            replaceDialog.setAlwaysOnTop(false);
            JOptionPane.showMessageDialog(this, "Can't find\"" + find + "\"", "My Text Editor", JOptionPane.INFORMATION_MESSAGE);
            replaceDialog.setAlwaysOnTop(true);
        } else {
            txtContent.select(index, index + find.length());
        }
    }

    private void initExit() {
        if (isSave) {
            System.exit(0);
        } else {
            int opt = JOptionPane.showConfirmDialog(this, "Do you want to save changes to " + title.split(" - ")[0] + "?", "My Text Editor", JOptionPane.YES_NO_CANCEL_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                if (saveFile(txtContent.getText())) {
                    System.exit(0);
                }
            } else if (opt == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        }
    }

    /**
     * Creates new form NotepadFrame
     */
    public NotepadFrame() {
        initComponents();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                initExit();
            }
        });
        this.setLocationRelativeTo(null);
        this.setTitle(title);
        um.setLimit(-1);
        initListener();
        mCut.setEnabled(false);
        mCopy.setEnabled(false);
        mUndo.setEnabled(false);
        mRedo.setEnabled(false);
        mFind.setEnabled(false);
        initFindDialog();
        initReplaceDialog();
        initChangeFontDialog();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        findDialog = new javax.swing.JDialog();
        lblFind = new javax.swing.JLabel();
        txtFind = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();
        btnCancelFind = new javax.swing.JButton();
        replaceDialog = new javax.swing.JDialog();
        lblRefind = new javax.swing.JLabel();
        lblReplace = new javax.swing.JLabel();
        txtReFind = new javax.swing.JTextField();
        txtReplace = new javax.swing.JTextField();
        btnFindNext = new javax.swing.JButton();
        btnReplace = new javax.swing.JButton();
        btnReplaceAll = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        changeFontDialog = new javax.swing.JDialog();
        pnName = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        scrollName = new javax.swing.JScrollPane();
        listFontName = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        scrollStyle = new javax.swing.JScrollPane();
        listFontStyle = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        scrollSize = new javax.swing.JScrollPane();
        listFontSize = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        txtFontText = new javax.swing.JTextField();
        btnFontCancel = new javax.swing.JButton();
        btnFontApply = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtContent = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        mNew = new javax.swing.JMenuItem();
        mOpen = new javax.swing.JMenuItem();
        mSave = new javax.swing.JMenuItem();
        mSaveAs = new javax.swing.JMenuItem();
        mExit = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        mUndo = new javax.swing.JMenuItem();
        mRedo = new javax.swing.JMenuItem();
        mCut = new javax.swing.JMenuItem();
        mCopy = new javax.swing.JMenuItem();
        mPaste = new javax.swing.JMenuItem();
        mSelectAll = new javax.swing.JMenuItem();
        mFind = new javax.swing.JMenuItem();
        mReplace = new javax.swing.JMenuItem();
        mChangeFont = new javax.swing.JMenuItem();

        lblFind.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblFind.setText("Find What:");

        txtFind.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFindKeyReleased(evt);
            }
        });

        btnFind.setText("Find next");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        btnCancelFind.setText("Cancel");
        btnCancelFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelFindActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout findDialogLayout = new javax.swing.GroupLayout(findDialog.getContentPane());
        findDialog.getContentPane().setLayout(findDialogLayout);
        findDialogLayout.setHorizontalGroup(
            findDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(findDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFind)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(findDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnCancelFind, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFind, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        findDialogLayout.setVerticalGroup(
            findDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(findDialogLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(findDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFind)
                    .addComponent(txtFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFind))
                .addGap(34, 34, 34)
                .addComponent(btnCancelFind)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        lblRefind.setText("Find What:");

        lblReplace.setText("Replace With:");

        txtReFind.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtReFindKeyReleased(evt);
            }
        });

        btnFindNext.setText("Find Next");
        btnFindNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindNextActionPerformed(evt);
            }
        });

        btnReplace.setText("Replace");
        btnReplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReplaceActionPerformed(evt);
            }
        });

        btnReplaceAll.setText("Replace All");
        btnReplaceAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReplaceAllActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout replaceDialogLayout = new javax.swing.GroupLayout(replaceDialog.getContentPane());
        replaceDialog.getContentPane().setLayout(replaceDialogLayout);
        replaceDialogLayout.setHorizontalGroup(
            replaceDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(replaceDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(replaceDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblReplace)
                    .addComponent(lblRefind))
                .addGap(18, 18, 18)
                .addGroup(replaceDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtReFind)
                    .addComponent(txtReplace, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(replaceDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnReplaceAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReplace, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFindNext, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        replaceDialogLayout.setVerticalGroup(
            replaceDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(replaceDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(replaceDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblRefind)
                    .addComponent(txtReFind, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFindNext))
                .addGap(20, 20, 20)
                .addGroup(replaceDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblReplace)
                    .addComponent(txtReplace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReplace))
                .addGap(18, 18, 18)
                .addComponent(btnReplaceAll)
                .addGap(18, 18, 18)
                .addComponent(btnCancel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        changeFontDialog.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel1.setText("Font: ");

        scrollName.setViewportView(listFontName);

        javax.swing.GroupLayout pnNameLayout = new javax.swing.GroupLayout(pnName);
        pnName.setLayout(pnNameLayout);
        pnNameLayout.setHorizontalGroup(
            pnNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNameLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollName, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(pnNameLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnNameLayout.setVerticalGroup(
            pnNameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnNameLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollName, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setText("Font Style: ");

        listFontStyle.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Regular", "Bold", "Italic", "Bold Italic" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        scrollStyle.setViewportView(listFontStyle);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(0, 31, Short.MAX_VALUE))
                    .addComponent(scrollStyle))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollStyle, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setText("Size: ");

        listFontSize.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "8", "9", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28", "36", "48", "72" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        scrollSize.setViewportView(listFontSize);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addContainerGap(53, Short.MAX_VALUE))
                    .addComponent(scrollSize, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scrollSize, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        txtFontText.setEditable(false);
        txtFontText.setText("This is a text");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtFontText, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtFontText, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnFontCancel.setText("Cancel");
        btnFontCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFontCancelActionPerformed(evt);
            }
        });

        btnFontApply.setText("Apply");
        btnFontApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFontApplyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout changeFontDialogLayout = new javax.swing.GroupLayout(changeFontDialog.getContentPane());
        changeFontDialog.getContentPane().setLayout(changeFontDialogLayout);
        changeFontDialogLayout.setHorizontalGroup(
            changeFontDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changeFontDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(changeFontDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(changeFontDialogLayout.createSequentialGroup()
                        .addComponent(pnName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(changeFontDialogLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)))
                .addGroup(changeFontDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFontCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnFontApply, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        changeFontDialogLayout.setVerticalGroup(
            changeFontDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(changeFontDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(changeFontDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(changeFontDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, changeFontDialogLayout.createSequentialGroup()
                        .addComponent(btnFontApply, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFontCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        txtContent.setColumns(20);
        txtContent.setRows(5);
        jScrollPane1.setViewportView(txtContent);

        menuFile.setText("File");
        menuFile.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N

        mNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        mNew.setText("New");
        mNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mNewActionPerformed(evt);
            }
        });
        menuFile.add(mNew);

        mOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mOpen.setText("Open");
        mOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mOpenActionPerformed(evt);
            }
        });
        menuFile.add(mOpen);

        mSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mSave.setText("Save");
        mSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mSaveActionPerformed(evt);
            }
        });
        menuFile.add(mSave);

        mSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mSaveAs.setText("Save As");
        mSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mSaveAsActionPerformed(evt);
            }
        });
        menuFile.add(mSaveAs);

        mExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, java.awt.event.InputEvent.ALT_MASK));
        mExit.setText("Exit");
        mExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mExitActionPerformed(evt);
            }
        });
        menuFile.add(mExit);

        jMenuBar1.add(menuFile);

        menuEdit.setText("Edit");
        menuEdit.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        menuEdit.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                menuEditMenuSelected(evt);
            }
        });

        mUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        mUndo.setText("Undo");
        mUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mUndoActionPerformed(evt);
            }
        });
        menuEdit.add(mUndo);

        mRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        mRedo.setText("Redo");
        mRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mRedoActionPerformed(evt);
            }
        });
        menuEdit.add(mRedo);

        mCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        mCut.setText("Cut");
        mCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCutActionPerformed(evt);
            }
        });
        menuEdit.add(mCut);

        mCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        mCopy.setText("Copy");
        mCopy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mCopyActionPerformed(evt);
            }
        });
        menuEdit.add(mCopy);

        mPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        mPaste.setText("Paste");
        mPaste.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mPasteActionPerformed(evt);
            }
        });
        menuEdit.add(mPaste);

        mSelectAll.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        mSelectAll.setText("Select All");
        mSelectAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mSelectAllActionPerformed(evt);
            }
        });
        menuEdit.add(mSelectAll);

        mFind.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        mFind.setText("Find");
        mFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mFindActionPerformed(evt);
            }
        });
        menuEdit.add(mFind);

        mReplace.setText("Replace");
        mReplace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mReplaceActionPerformed(evt);
            }
        });
        menuEdit.add(mReplace);

        mChangeFont.setText("Change Font");
        mChangeFont.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mChangeFontActionPerformed(evt);
            }
        });
        menuEdit.add(mChangeFont);

        jMenuBar1.add(menuEdit);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 880, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mNewActionPerformed
        if (isSave) {
            newFile();
        } else {
            int opt = JOptionPane.showConfirmDialog(this, "Do you want to save changes to " + title.split(" - ")[0] + "?", "My Text Editor", JOptionPane.YES_NO_CANCEL_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                if (saveFile(txtContent.getText())) {
                    newFile();
                }
            } else if (opt == JOptionPane.NO_OPTION) {
                newFile();
            }
        }
    }//GEN-LAST:event_mNewActionPerformed

    private void mSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mSaveActionPerformed
        saveFile(txtContent.getText());
    }//GEN-LAST:event_mSaveActionPerformed

    private void mSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mSaveAsActionPerformed
        saveAs(txtContent.getText());
    }//GEN-LAST:event_mSaveAsActionPerformed

    private void mOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mOpenActionPerformed
        if (isSave) {
            openFile();
        } else {
            int opt = JOptionPane.showConfirmDialog(this, "Do you want to save changes to " + title.split(" - ")[0] + "?", "My Text Editor", JOptionPane.YES_NO_CANCEL_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                if (saveFile(txtContent.getText())) {
                    openFile();
                }
            } else if (opt == JOptionPane.NO_OPTION) {
                openFile();
            }
        }
    }//GEN-LAST:event_mOpenActionPerformed

    private void mExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mExitActionPerformed
        initExit();
    }//GEN-LAST:event_mExitActionPerformed

    private void mSelectAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mSelectAllActionPerformed
        txtContent.selectAll();
    }//GEN-LAST:event_mSelectAllActionPerformed

    private void mCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCutActionPerformed
        txtContent.cut();
    }//GEN-LAST:event_mCutActionPerformed

    private void mCopyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mCopyActionPerformed
        txtContent.copy();
    }//GEN-LAST:event_mCopyActionPerformed

    private void mPasteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mPasteActionPerformed
        txtContent.paste();
    }//GEN-LAST:event_mPasteActionPerformed

    private void menuEditMenuSelected(javax.swing.event.MenuEvent evt) {//GEN-FIRST:event_menuEditMenuSelected
        if (txtContent.getSelectedText() == null) {
            mCut.setEnabled(false);
            mCopy.setEnabled(false);
        } else {
            mCut.setEnabled(true);
            mCopy.setEnabled(true);
        }
        mPaste.setEnabled(true);
    }//GEN-LAST:event_menuEditMenuSelected

    private void mUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mUndoActionPerformed
        um.undo();
    }//GEN-LAST:event_mUndoActionPerformed

    private void mRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mRedoActionPerformed
        um.redo();
    }//GEN-LAST:event_mRedoActionPerformed

    private void mFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mFindActionPerformed
        findDialog.setVisible(true);
        txtFind.setText("");
        if (txtContent.getSelectedText() != null) {
            txtFind.setText(txtContent.getSelectedText());
        }
    }//GEN-LAST:event_mFindActionPerformed

    private void mReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mReplaceActionPerformed
        replaceDialog.setVisible(true);
        if (txtContent.getSelectedText() != null) {
            txtReFind.setText(txtContent.getSelectedText());
        }
    }//GEN-LAST:event_mReplaceActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        findStr();
    }//GEN-LAST:event_btnFindActionPerformed

    private void btnReplaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReplaceActionPerformed
        if (txtContent.getSelectedText() == null) {
            repFindStr();
        } else {
            txtContent.replaceSelection(txtReplace.getText());
            repFindStr();
        }
    }//GEN-LAST:event_btnReplaceActionPerformed

    private void btnFindNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindNextActionPerformed
        repFindStr();
    }//GEN-LAST:event_btnFindNextActionPerformed

    private void txtFindKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFindKeyReleased
        if (txtFind.getText().isEmpty()) {
            btnFind.setEnabled(false);
        } else {
            btnFind.setEnabled(true);
        }
    }//GEN-LAST:event_txtFindKeyReleased

    private void txtReFindKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtReFindKeyReleased
        if (txtReFind.getText().isEmpty()) {
            btnFindNext.setEnabled(false);
            btnReplace.setEnabled(false);
            btnReplaceAll.setEnabled(false);
        } else {
            btnFindNext.setEnabled(true);
            btnReplace.setEnabled(true);
            btnReplaceAll.setEnabled(true);
        }
    }//GEN-LAST:event_txtReFindKeyReleased

    private void btnReplaceAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReplaceAllActionPerformed
        String rep = txtReFind.getText();
        String replace = txtReplace.getText();
        boolean existed = txtContent.getText().contains(rep);
        if (!existed) {
            replaceDialog.setAlwaysOnTop(false);
            JOptionPane.showMessageDialog(this, "Can't find \"" + rep + "\"", "Can't Find", JOptionPane.OK_OPTION);
            replaceDialog.setAlwaysOnTop(true);
        } else {
            txtContent.setText(txtContent.getText().replaceAll(rep, replace));
        }
    }//GEN-LAST:event_btnReplaceAllActionPerformed

    private void btnCancelFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelFindActionPerformed
        findDialog.dispose();
    }//GEN-LAST:event_btnCancelFindActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        replaceDialog.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void mChangeFontActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mChangeFontActionPerformed
        changeFontDialog.setVisible(true);
        loadFonts();
    }//GEN-LAST:event_mChangeFontActionPerformed

    private void btnFontCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFontCancelActionPerformed
        // TODO add your handling code here:
        changeFontDialog.dispose();
    }//GEN-LAST:event_btnFontCancelActionPerformed

    private void btnFontApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFontApplyActionPerformed
        // TODO add your handling code here:
        Font font = txtFontText.getFont();
        txtContent.setFont(font);
        changeFontDialog.dispose();
    }//GEN-LAST:event_btnFontApplyActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NotepadFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NotepadFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NotepadFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NotepadFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NotepadFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCancelFind;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnFindNext;
    private javax.swing.JButton btnFontApply;
    private javax.swing.JButton btnFontCancel;
    private javax.swing.JButton btnReplace;
    private javax.swing.JButton btnReplaceAll;
    private javax.swing.JDialog changeFontDialog;
    private javax.swing.JDialog findDialog;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFind;
    private javax.swing.JLabel lblRefind;
    private javax.swing.JLabel lblReplace;
    private javax.swing.JList<String> listFontName;
    private javax.swing.JList<String> listFontSize;
    private javax.swing.JList<String> listFontStyle;
    private javax.swing.JMenuItem mChangeFont;
    private javax.swing.JMenuItem mCopy;
    private javax.swing.JMenuItem mCut;
    private javax.swing.JMenuItem mExit;
    private javax.swing.JMenuItem mFind;
    private javax.swing.JMenuItem mNew;
    private javax.swing.JMenuItem mOpen;
    private javax.swing.JMenuItem mPaste;
    private javax.swing.JMenuItem mRedo;
    private javax.swing.JMenuItem mReplace;
    private javax.swing.JMenuItem mSave;
    private javax.swing.JMenuItem mSaveAs;
    private javax.swing.JMenuItem mSelectAll;
    private javax.swing.JMenuItem mUndo;
    private javax.swing.JMenu menuEdit;
    private javax.swing.JMenu menuFile;
    private javax.swing.JPanel pnName;
    private javax.swing.JDialog replaceDialog;
    private javax.swing.JScrollPane scrollName;
    private javax.swing.JScrollPane scrollSize;
    private javax.swing.JScrollPane scrollStyle;
    private javax.swing.JTextArea txtContent;
    private javax.swing.JTextField txtFind;
    private javax.swing.JTextField txtFontText;
    private javax.swing.JTextField txtReFind;
    private javax.swing.JTextField txtReplace;
    // End of variables declaration//GEN-END:variables
}
