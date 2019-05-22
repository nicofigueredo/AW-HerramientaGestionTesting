package unlam.edu.herramienta;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class HerramientaTesting extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JFileChooser fileChooser;
	private JList<String> filesList;
	private JTextPane codeTextPane;

	/**
	 * Create the frame.
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public HerramientaTesting() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		setTitle("Herramienta de Gestion de Testing");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 850, 690);
		setLocationRelativeTo(null);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnAnalisis = new JMenu("Analisis");
		menuBar.add(mnAnalisis);
		
		JMenuItem mntmElegirCarpeta = new JMenuItem("Elegir Carpeta");
		mntmElegirCarpeta.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mntmElegirCarpeta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				abrirCarpeta();
			}
		});
		
		mnAnalisis.add(mntmElegirCarpeta);
		
		JMenuItem mntmSalirCtrl = new JMenuItem("Salir");
		mntmSalirCtrl.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnAnalisis.add(mntmSalirCtrl);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Seleccione un archivo de la lista");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setForeground(new Color(0, 0, 128));
		lblNewLabel.setBounds(10, 11, 197, 14);
		contentPane.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 521, 99);
		contentPane.add(scrollPane);
		
		filesList = new JList<>();
		filesList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String filename;
				if (filesList.getSelectedIndex() != -1) {
					filename = filesList.getSelectedValue().toString();
					List<String> lineas = leerArchivo(filename);
					Collections.reverse(lineas);
					Document doc = codeTextPane.getDocument();
					for(String linea : lineas)
						try {
							doc.insertString(0, linea + "\n",null );
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}																			
				}				 
			}
		});
		scrollPane.setViewportView(filesList);
		
		JLabel lblSeleccioneUnaClase = new JLabel("Seleccione una clase de la lista");
		lblSeleccioneUnaClase.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSeleccioneUnaClase.setForeground(new Color(0, 0, 128));
		lblSeleccioneUnaClase.setBounds(22, 146, 185, 14);
		contentPane.add(lblSeleccioneUnaClase);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 171, 258, 202);
		contentPane.add(scrollPane_1);
		
		JList<String> classList = new JList<>();
		scrollPane_1.setViewportView(classList);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(278, 171, 253, 202);
		contentPane.add(scrollPane_2);
		
		JList<String> methodList = new JList<>();
		scrollPane_2.setViewportView(methodList);
		
		JLabel lblSeleccioneUnMetodo = new JLabel("Seleccione un metodo de la lista");
		lblSeleccioneUnMetodo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSeleccioneUnMetodo.setForeground(new Color(0, 0, 128));
		lblSeleccioneUnMetodo.setBounds(278, 146, 185, 14);
		contentPane.add(lblSeleccioneUnMetodo);
		
		JLabel lblCodigoDelMetodo = new JLabel("Codigo del metodo seleccionado");
		lblCodigoDelMetodo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCodigoDelMetodo.setForeground(new Color(0, 0, 128));
		lblCodigoDelMetodo.setBounds(10, 384, 197, 14);
		contentPane.add(lblCodigoDelMetodo);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(10, 409, 774, 220);
		contentPane.add(scrollPane_3);
		
		codeTextPane = new JTextPane();
		codeTextPane.setEditable(false);
		scrollPane_3.setViewportView(codeTextPane);
	}
	
	private void abrirCarpeta() {		
		
		fileChooser = new JFileChooser();		
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int fileChooserResult = fileChooser.showOpenDialog(this);
		if(fileChooserResult == JFileChooser.APPROVE_OPTION) {
			File carpeta = fileChooser.getSelectedFile();
			File[] archivos = carpeta.listFiles(new FilenameFilter() {
		        @Override
		        public boolean accept(File dir, String name) {
		            return name.toLowerCase().endsWith(".java");
		        }
		    });
	        DefaultListModel<String> filenames = new DefaultListModel<>();			
			
			for(File file: archivos) {				
				filenames.addElement(file.getAbsolutePath());
			}			
			filesList.setModel(filenames);						
		}
	}
	
	private List<String> leerArchivo(String filename) {
	  List<String> lineas = new ArrayList<String>();
	  try {
	    BufferedReader reader = new BufferedReader(new FileReader(filename));
	    String line;
	    while ((line = reader.readLine()) != null) {
	      lineas.add(line);
	    }
	    reader.close();
	    return lineas;
	  }
	  catch (Exception e) {
	    System.err.format("Ocurrio un error leyendo '%s'.", filename);
	    e.printStackTrace();
	    return null;
	  }
	}
	
	
}
