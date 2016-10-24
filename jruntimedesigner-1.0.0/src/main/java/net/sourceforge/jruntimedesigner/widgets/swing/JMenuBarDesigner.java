package net.sourceforge.jruntimedesigner.widgets.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class JMenuBarDesigner extends JDialog {
	
	private JMenuBar menubar;

	private final JPanel contentPanel = new JPanel();
	private JLabel lblNewLabel;
	private JTextField tfTxt;
	private JTextField tfAct;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			JMenuBarDesigner dialog = new JMenuBarDesigner();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public JMenuBarDesigner() {
		initComponents();
	}
	public JMenuBarDesigner(JMenuBar menubar) {
		this.menubar = menubar;
		initComponents();
		setModal(true);
		Component c = SwingUtilities.getWindowAncestor(menubar);
		setLocationRelativeTo(c );
	}
	private void initComponents() {
		setTitle("JMenuBar Designer");
		setBounds(100, 100, 511, 374);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JSplitPane splitPane = new JSplitPane();
			splitPane.setResizeWeight(0.3);
			splitPane.setDividerSize(8);
			splitPane.setOneTouchExpandable(true);
			contentPanel.add(splitPane);
			{
				JTree treeMenu = new JTree();
				splitPane.setLeftComponent(treeMenu);
			}
			{
				JPanel panel = new JPanel();
				splitPane.setRightComponent(panel);
				panel.setLayout(null);
				
				this.lblNewLabel = new JLabel("Menu Text:");
				this.lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				this.lblNewLabel.setBounds(10, 10, 74, 22);
				panel.add(this.lblNewLabel);
				{
					this.tfTxt = new JTextField();
					this.tfTxt.setBounds(94, 11, 165, 21);
					panel.add(this.tfTxt);
					this.tfTxt.setColumns(10);
				}
				{
					JLabel lblCommand = new JLabel("Command:");
					lblCommand.setHorizontalAlignment(SwingConstants.RIGHT);
					lblCommand.setBounds(10, 46, 74, 22);
					panel.add(lblCommand);
				}
				{
					this.tfAct = new JTextField();
					this.tfAct.setColumns(10);
					this.tfAct.setBounds(94, 47, 165, 21);
					panel.add(this.tfAct);
				}
				{
					JButton btnAddMenu = new JButton("Add Menu");
					btnAddMenu.setBounds(28, 183, 81, 23);
					panel.add(btnAddMenu);
				}
				{
					JButton btnAddItem = new JButton("Add Item");
					btnAddItem.setBounds(119, 183, 81, 23);
					panel.add(btnAddItem);
				}
			}
			splitPane.setDividerLocation(0.5);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
