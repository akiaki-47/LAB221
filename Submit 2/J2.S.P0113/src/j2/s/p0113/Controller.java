/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package j2.s.p0113;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Admin
 */
public class Controller {

    Main main = new Main();
    ArrayList<Book> listBook = new ArrayList<>();

    public void control() {
        main.setVisible(true);
        addBookList();
        clickItem();
        clickRemove();
        addBookJList();
        clickSave();
    }

    public void clickRemove() {
        main.getBtnRemove().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (main.getJList1().getSelectedIndex() < 0) {
                    JOptionPane.showMessageDialog(main, "Please choose a book!");
                }
                String bookSelected = main.getJList1().getSelectedValue();
                listBook.remove(getBookByName(bookSelected));
                addBookJList();
                main.getJList1().updateUI();
                main.getTxtBookCode().setText("");
                main.getTxtBookName().setText("");
                main.getTxtAuthor().setText("");
                main.getTxtPublisher().setText("");
                main.getCbForRent().setSelected(false);
                main.getCbxPublishYear().setSelectedIndex(0);
                main.getTxtBookCode().setEnabled(true);
            }
        });
    }

    public void clickSave() {
        main.getBtnSave().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = main.getTxtBookCode().getText();
                String name = main.getTxtBookName().getText();
                String author = main.getTxtAuthor().getText();
                String pub = main.getTxtPublisher().getText();
                Boolean rent = false;
                int pubYear = (int) main.getCbxPublishYear().getSelectedItem();
                if (main.getTxtBookCode().isEnabled() == true) {

                    if (main.getCbForRent().isSelected()) {
                        rent = true;
                    }
                    if (code.isEmpty() || name.isEmpty() || pub.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill full!");
                        return;
                    }
                    if (!checkDuplicate()) {
                        JOptionPane.showMessageDialog(main, "Duplicate code!");
                        return;
                    }
                    Book b = new Book(code, name, author, pub, pubYear, rent);
                    listBook.add(b);
                    addBookJList();
                    JOptionPane.showMessageDialog(main, "Save successfully");
                } else {
                    if (main.getCbForRent().isSelected()) {
                        rent = true;
                    }
                    if (code.isEmpty() || name.isEmpty() || pub.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill full!");
                        return;
                    }
                    String bookSelected = main.getJList1().getSelectedValue();
                    for (Book book : listBook) {
                        if (book.getBookName().equalsIgnoreCase(bookSelected)) {
                            book.setBookCode(code);
                            book.setBookName(name);
                            book.setAuthor(author);
                            book.setPublisher(pub);
                            book.setPublishedYear(pubYear);
                            book.setForRent(rent);
                            JOptionPane.showMessageDialog(main, "Update successfully");
                            return;
                        }
                    }
                }

            }
        });
    }

    public boolean checkDuplicate() {
        for (int i = 0; i < listBook.size(); i++) {
            if (main.getTxtBookCode().getText().equalsIgnoreCase(listBook.get(i).getBookCode())) {
                return false;
            }
        }
        return true;
    }

    public Book getBookByName(String bookSelected) {
        for (Book book : listBook) {
            if (book.getBookName().equalsIgnoreCase(bookSelected)) {
                return book;
            }
        }
        return null;
    }

    public void clickItem() {
        main.getJList1().addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                main.getTxtBookCode().setEnabled(false);
                String bookSelected = main.getJList1().getSelectedValue();
                for (Book book : listBook) {
                    if (book.getBookName().equalsIgnoreCase(bookSelected)) {
                        displayBook(book);
                        return;
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
    }

    public void displayBook(Book book) {
        main.getTxtBookCode().setText(book.getBookCode());
        main.getTxtBookName().setText(book.getBookName());
        main.getTxtAuthor().setText(book.getAuthor());
        main.getTxtPublisher().setText(book.getPublisher());
        main.getCbxPublishYear().setSelectedItem(book.getPublishedYear());
        main.getCbForRent().setSelected(book.isForRent());
    }

    public void addBookJList() {
        ArrayList<String> nameListAccout = new ArrayList<>();
        for (int i = 0; i < listBook.size(); i++) {
            nameListAccout.add(listBook.get(i).getBookName());
        }
        String[] es = new String[nameListAccout.size()];
        es = nameListAccout.toArray(es);
        main.getJList1().setListData(es);
    }

    public void addBookList() {
        listBook.add(new Book("C01", "Introduce to C", "Zuka", "FPT University", 2011, true));
        listBook.add(new Book("JAVA01", "Introduce to Java", "Wukong", "Standford University", 2016, false));
        listBook.add(new Book("JAVA02", "Java Web", "KhanhKT", "FPT University", 2018, true));
        listBook.add(new Book("JAVA03", "Java desktop", "VanTTN", "Harvard University", 2017, false));
    }

    public static void main(String[] args) {
        Controller con = new Controller();
        con.control();
    }
}
