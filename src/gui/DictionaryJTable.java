package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import domain.Dictionary;
import domain.Main;
import utils.Logger;

public class DictionaryJTable extends JPanel {

	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Word", "Exclude?" };
	private Object[][] data;
	private JTable table;
	private JFrame frame;
	private JButton doneButton;
	private JScrollPane scrollPane;
	private Dictionary dictionary;
	
	public DictionaryJTable(Dictionary dictionary) {
		this.dictionary = dictionary;
		initData();
		initTable();
		initButton();
		showTable();
	}
	
	private void initData() {
		Object[] dictArr = dictionary.toArray();
		data = new Object[dictArr.length][columnNames.length];
		
		for( int i = 0; i < dictArr.length; i++ ) {
			data[i][0] = dictArr[i];
			data[i][1] = false;
		}
	}
	
	private void initTable() {
		table = new JTable(data, columnNames) {
			private static final long serialVersionUID = 1L;
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int column) {
				switch( column ) {
					case 1: return Boolean.class;
					default: return String.class;
				}
			}
			
		};
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(table);
	}
	
	private void initButton() {
		doneButton = new JButton();
		doneButton.setText("OK");
		doneButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				for( int i = 0; i < data.length; i++ ) {
					Main.l.logln("Dict "+i+" "+data[i][0]+":"+data[i][1], Logger.INF);
				}
				
				frame.setVisible(false);
				
			}
			
		});
	}
	
	public void showTable() {
		frame = new JFrame("Dictionary");
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        JPanel panel = new JPanel(new GridLayout());

       // DictionaryJTable newContentPane = new DictionaryJTable(dictionary);
       // newContentPane.setOpaque(true);
       // frame.setContentPane(newContentPane);
        
        panel.add(scrollPane);
        panel.add(doneButton);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
	}

}
