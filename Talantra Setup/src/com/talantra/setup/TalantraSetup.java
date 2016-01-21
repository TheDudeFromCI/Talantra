package com.talantra.setup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.DefaultCaret;

public class TalantraSetup{
	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch(Exception exception){
			exception.printStackTrace();
		}
		JFrame frame = new JFrame();
		{
			frame.setResizable(false);
			frame.setSize(433, 490);
			frame.setLocationRelativeTo(null);
			frame.setTitle("Talantra Setup");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			GridBagLayout gridBagLayout = new GridBagLayout();
			gridBagLayout.columnWidths = new int[]{
				0, 0
			};
			gridBagLayout.rowHeights = new int[]{
				0, 0, 0, 0, 0
			};
			gridBagLayout.columnWeights = new double[]{
				1.0, Double.MIN_VALUE
			};
			gridBagLayout.rowWeights = new double[]{
				0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE
			};
			frame.getContentPane().setLayout(gridBagLayout);
			JLabel label =
				new JLabel(new ImageIcon(TalantraSetup.class.getResource("/com/talantra/setup/Splash.png")));
			GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.insets = new Insets(0, 0, 5, 0);
			gbc_label.gridx = 0;
			gbc_label.gridy = 0;
			frame.getContentPane().add(label, gbc_label);
			JLabel lblWelcomeToThe =
				new JLabel("<html><b>Welcome to the Talantra Installation Wizard!</b></html>");
			GridBagConstraints gbc_lblWelcomeToThe = new GridBagConstraints();
			gbc_lblWelcomeToThe.insets = new Insets(0, 0, 5, 0);
			gbc_lblWelcomeToThe.gridx = 0;
			gbc_lblWelcomeToThe.gridy = 1;
			frame.getContentPane().add(lblWelcomeToThe, gbc_lblWelcomeToThe);
			JPanel panel_1 = new JPanel();
			GridBagConstraints gbc_panel_1 = new GridBagConstraints();
			gbc_panel_1.insets = new Insets(65, 0, 70, 0);
			gbc_panel_1.fill = GridBagConstraints.BOTH;
			gbc_panel_1.gridx = 0;
			gbc_panel_1.gridy = 2;
			frame.getContentPane().add(panel_1, gbc_panel_1);
			panel_1.setLayout(new BorderLayout(0, 0));
			JLabel lblInstallationDestination = new JLabel("Installation Destination:");
			lblInstallationDestination.setHorizontalAlignment(SwingConstants.CENTER);
			panel_1.add(lblInstallationDestination, BorderLayout.NORTH);
			textField = new JTextField();
			{
				String path = System.getProperty("user.dir");
				textField.setText(path);
			}
			textField.setBorder(new LineBorder(new Color(165, 172, 178), 1, true));
			textField.setEditable(false);
			panel_1.add(textField, BorderLayout.CENTER);
			textField.setColumns(10);
			JButton btnChange = new JButton("Change");
			btnChange.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setFileFilter(new FileFilter(){
						@Override
						public boolean accept(File f){
							return f.isDirectory();
						}
						@Override
						public String getDescription(){
							return "Any Folder";
						}
					});
					fileChooser.setDialogTitle("Select Install Location.");
					fileChooser.setAcceptAllFileFilterUsed(false);
					fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fileChooser.setMultiSelectionEnabled(false);
					if(fileChooser.showOpenDialog(null)!=JFileChooser.APPROVE_OPTION)
						return;
					textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			});
			panel_1.add(btnChange, BorderLayout.EAST);
			JPanel panel = new JPanel();
			FlowLayout flowLayout = (FlowLayout)panel.getLayout();
			flowLayout.setAlignment(FlowLayout.RIGHT);
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.fill = GridBagConstraints.BOTH;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 3;
			frame.getContentPane().add(panel, gbc_panel);
			JButton btnExport = new JButton("Install");
			JButton btnCancel = new JButton("Exit");
			btnExport.addActionListener(new ActionListener(){
				private boolean done = false;
				public void actionPerformed(ActionEvent e){
					if(done)
						return;
					done = true;
					File file = new File(textField.getText());
					if(!file.exists())
						file.mkdirs();
					JPanel panel_2 = new JPanel();
					JProgressBar progressBar = new JProgressBar();
					progressBar.setStringPainted(true);
					progressBar.setMinimum(0);
					progressBar.setValue(0);
					progressBar.setMaximum(1000);
					progressBar.setForeground(Color.green);
					GridBagConstraints gbc_panel_2 = new GridBagConstraints();
					gbc_panel_2.insets = new Insets(0, 0, 5, 0);
					gbc_panel_2.fill = GridBagConstraints.BOTH;
					gbc_panel_2.gridx = 0;
					gbc_panel_2.gridy = 2;
					frame.remove(panel_1);
					frame.getContentPane().add(panel_2, gbc_panel_2);
					frame.revalidate();
					panel_2.setLayout(new BorderLayout(0, 0));
					panel_2.add(progressBar, BorderLayout.NORTH);
					JTextArea log = new JTextArea();
					log.setFont(new Font("Areial", Font.PLAIN, 12));
					log.setEditable(false);
					log.setText("");
					((DefaultCaret)log.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
					JScrollPane pane = new JScrollPane(log);
					pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
					panel_2.add(pane, BorderLayout.CENTER);
					frame.revalidate();
					frame.repaint();
					new Thread(){
						@Override
						public void run(){
							log.append("Begining Talantra install.\n");
							log.append("Calculating file properties...\n");
							String folder = file.getAbsolutePath();
							try{
								ZipInputStream in =
									new ZipInputStream(TalantraSetup.class.getResourceAsStream("/Talantra.zip"));
								int size;
								{
									InputStream s = TalantraSetup.class.getResourceAsStream("/Talantra.zip");
									size = s.available();
									s.close();
								}
								progressBar.setMaximum(size);
								log.append("Extracting Talatra files...\n");
								ZipEntry entry = null;
								int readBytes = 0;
								while((entry = in.getNextEntry())!=null){
									String path = folder+File.separatorChar+entry.getName();
									if(entry.isDirectory()){
										log.append("Adding directory "+entry.getName()+"... ");
										new File(path).mkdirs();
									}else{
										File originalFile = new File(path);
										if(originalFile.exists()){
											log.append("Deleting old asset "+entry.getName()+"... ");
											originalFile.delete();
											log.append("Complete.\n");
										}
										log.append("Extracting "+entry.getName()+"... ");
										BufferedOutputStream bos =
											new BufferedOutputStream(new FileOutputStream(path));
										byte[] bytesIn = new byte[4096];
										int read = 0;
										while((read = in.read(bytesIn))!=-1){
											bos.write(bytesIn, 0, read);
										}
										bos.close();
									}
									in.closeEntry();
									readBytes += entry.getCompressedSize();
									progressBar.setValue(readBytes);
									log.append("Complete.\n");
								}
								in.close();
							}catch(IOException e1){
								e1.printStackTrace();
							}
							progressBar.setValue(progressBar.getMaximum());
							log.append("Done.");
							JOptionPane.showMessageDialog(null, "Install complete.");
							frame.dispose();
						}
					}.start();
				}
			});
			panel.add(btnExport);
			btnCancel.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					frame.dispose();
				}
			});
			panel.add(btnCancel);
			frame.setVisible(true);
		}
	}
	private static JTextField textField;
}
